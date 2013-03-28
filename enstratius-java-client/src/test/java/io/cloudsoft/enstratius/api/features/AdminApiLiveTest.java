package io.cloudsoft.enstratius.api.features;

import io.cloudsoft.enstratius.api.model.BillingCode;
import io.cloudsoft.enstratius.api.model.Job;
import io.cloudsoft.enstratius.api.model.Jurisdiction;
import io.cloudsoft.enstratius.api.model.Region;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstRegionOrNull;
import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "AdminApiLiveTest")
public class AdminApiLiveTest extends BasicEnstratusLiveTest {
   private AdminApi api;
   private Region region;

   @BeforeClass
   public void beforeClass() throws Exception {
      api = enstratiusAPI.getAdminApi();
      assertNotNull(api);
      region = tryFindFirstRegionOrNull(Jurisdiction.EU, enstratiusAPI.getGeographyApi());
      if (region == null)
         Assert.fail();
   }

   public void listBillingCodes() throws Exception {
      for (BillingCode billingCode : api.listBillingCodes(region.getRegionId())) {
         assertNotNull(billingCode.getBillingCodeId());
      }
   }

   public void listJobs() throws Exception {
      for (Job job : api.listJobs()) {
         assertNotNull(job.getJobId());
      }
   }
}
