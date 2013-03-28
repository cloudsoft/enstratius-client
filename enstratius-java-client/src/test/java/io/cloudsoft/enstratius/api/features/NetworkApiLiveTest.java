package io.cloudsoft.enstratius.api.features;

import io.cloudsoft.enstratius.api.model.*;
import io.cloudsoft.enstratius.api.utils.Jobs;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstBillingCodeOrNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstRegionOrNull;
import static org.testng.Assert.*;

@Test(groups = "live", testName = "NetworkApiLiveTest")
public class NetworkApiLiveTest extends BasicEnstratusLiveTest {
   private NetworkApi api;
   private String regionId;
   private String firewallId;

   @BeforeClass
   public void beforeClass() throws Exception {
      api = enstratiusAPI.getNetworkApi();
      assertNotNull(api);
      Region region = tryFindFirstRegionOrNull(Jurisdiction.EU, enstratiusAPI.getGeographyApi());
      if (region == null)
         Assert.fail();
      regionId = region.getRegionId();
   }

   @Test
   public void addFirewall() throws Exception {
      String name = "firewallIntegrationTest";
      String description = "firewall test";
      Color label = Color.RED;

      BillingCode billingCode = tryFindFirstBillingCodeOrNull(regionId, enstratiusAPI.getAdminApi());
      if (billingCode == null)
         Assert.fail();
      String budgetId = billingCode.getBillingCodeId();
      Job job = api.addFirewall(name, description, budgetId, regionId, label);
      assertNotNull(job.getJobId());
      job = Jobs.waitForJob(job, enstratiusAPI.getAdminApi());
      Assert.assertTrue((Jobs.isComplete(job)));
      firewallId = job.getMessage();
   }


   @Test(dependsOnMethods = "addFirewall")
   public void getFirewalls() throws Exception {
      assertNotNull(api.getFirewall(firewallId));
   }

   @Test(dependsOnMethods = "addFirewall")
   public void addFirewallRule() throws Exception {
      String cidr = "157.166.224.26/32";
      String startPort = "8080";
      String endPort = "8081";
      Assert.assertNotNull(firewallId);
      try {
         api.addFirewallRule(firewallId, cidr, startPort, endPort, Direction.INGRESS, Protocol.TCP);
      } finally {
         api.deleteFirewall(firewallId, "just a test");
      }
   }

   @Test(dependsOnMethods = "addFirewallRule")
   public void listFirewallsWithoutFirewall() throws Exception {
      assertFalse(api.listFirewalls(regionId).contains(api.getFirewall(firewallId)));
   }

}
