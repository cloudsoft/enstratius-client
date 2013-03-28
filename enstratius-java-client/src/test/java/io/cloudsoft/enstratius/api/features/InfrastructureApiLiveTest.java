package io.cloudsoft.enstratius.api.features;

import io.cloudsoft.enstratius.api.model.*;
import io.cloudsoft.enstratius.api.utils.Jobs;
import io.cloudsoft.enstratius.api.utils.MachineImageMatcher;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test(groups = "live", testName = "InfrastructureApiLiveTest")
public class InfrastructureApiLiveTest extends BasicEnstratusLiveTest {
   private InfrastructureApi api;
   private String regionId;
   private String budgetId;
   private String dataCenterId;
   private String machineImageId;
   private String firewallId;

   @BeforeClass
   public void beforeClass() throws Exception {
      api = enstratiusAPI.getInfrastructureApi();
      assertNotNull(api);
      Region region = tryFindFirstRegionOrNull(Jurisdiction.EU, enstratiusAPI.getGeographyApi());
      if (region == null)
         Assert.fail();
      regionId = region.getRegionId();

      BillingCode billingCode = tryFindFirstBillingCodeOrNull(regionId, enstratiusAPI.getAdminApi());
      Assert.assertNotNull(billingCode);
      budgetId = billingCode.getBillingCodeId();

      Datacenter datacenter = tryFindFirstDatacenterOrNull(regionId, enstratiusAPI.getGeographyApi());
      Assert.assertNotNull(datacenter);
      dataCenterId = checkNotNull(datacenter.getDataCenterId(), "dataCenterId");

      String providerId = "ami-640a0610"; //RightImage_CentOS_5.4_i386_v5.5.9_EBS
      MachineImage machineImage = tryFindMachineImageMatchingPredicateOrNull(regionId,
              enstratiusAPI.getInfrastructureApi(),
              new MachineImageMatcher(providerId));
      Assert.assertNotNull(machineImage);
      machineImageId = checkNotNull(machineImage.getMachineImageId(), "machineImageId");

      Firewall firewall = tryFindFirstFirewallOrNull(regionId, enstratiusAPI.getNetworkApi());
      Assert.assertNotNull(firewall);
      firewallId = checkNotNull(firewall.getFirewallId(), "firewallId");
   }

   @Test(dependsOnMethods = "listMachineImages")
   public void launchServer() throws Exception {
      String name = "serverTest";
      String description = "server test";
      Job job = null;
      try {
         job = api.launchServer(name, description, budgetId, machineImageId, dataCenterId, null);
         assertNotNull(job.getJobId());
         job = Jobs.waitForJob(job, 10 * 60, enstratiusAPI.getAdminApi());
      } finally {
         if (Jobs.isComplete(job)) {
            String serverId = job.getMessage();
            api.deleteServer(serverId, "InfrastructureApiLiveTest - launchServer");
            assertNull(api.getServer(serverId, regionId));
         }
      }
   }

   @Test(dependsOnMethods = "launchServer")
   public void launchServerWithFirewall() throws Exception {
      String name = "serverTestWithFirewall";
      String description = "server test with firewall";

      Job job = null;
      try {
         job = api.launchServer(name, description, budgetId, machineImageId, dataCenterId, firewallId);
         assertNotNull(job.getJobId());
         job = Jobs.waitForJob(job, 10 * 60, enstratiusAPI.getAdminApi());
      } finally {
         if (Jobs.isComplete(job)) {
            String serverId = job.getMessage();
            api.deleteServer(serverId, "InfrastructureApiLiveTest - launchServerWithFirewall");
            assertNull(api.getServer(serverId, regionId));
         }
      }
   }

   @Test
   public void listMachineImages() throws Exception {
      for (MachineImage machineImage : api.listMachineImages(regionId)) {
         assertNotNull(machineImage.getMachineImageId());
      }
   }

   @Test
   public void listServerProducts() throws Exception {
      for (ServerProduct serverProduct : api.listServerProducts(regionId)) {
         assertNotNull(serverProduct.getProductId());
      }
   }

   @Test
   public void listServers() throws Exception {
      for (Server server : api.listServers(regionId)) {
         assertNotNull(server.getServerId());
      }
   }

}
