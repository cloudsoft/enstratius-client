package io.cloudsoft.enstratius.api.features;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import io.cloudsoft.enstratius.api.model.Cloud;
import io.cloudsoft.enstratius.api.model.Datacenter;
import io.cloudsoft.enstratius.api.model.Region;
import io.cloudsoft.enstratius.api.model.Subscription;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNotNull;

@Test(groups = "live", testName = "GeographyApiTest")
public class GeographyApiLiveTest extends BasicEnstratusLiveTest {

   private GeographyApi api;

   @BeforeClass
   public void beforeClass() {
      api = enstratiusAPI.getGeographyApi();
      assertNotNull(api);
   }

   public void testListRegions() throws Exception {
      List<Region> regions = api.listRegions();
      for (Region region : regions) {
         assertNotNull(region.getRegionId());
      }
   }

   public void testListClouds() throws Exception {
      for (Cloud cloud : api.listClouds()) {
         assertNotNull(cloud.getCloudId());
      }
   }

   public void testGetRegion() throws Exception {
      Region region = Iterables.tryFind(api.listRegions(), Predicates.notNull()).orNull();
      Assert.assertNotNull(region);
      assertNotNull(api.getRegion(region.getRegionId()));
   }

   public void testListDatacenters() throws Exception {
      Region region = Iterables.tryFind(api.listRegions(), Predicates.notNull()).orNull();
      Assert.assertNotNull(region);
      for (Datacenter datacenter : api.listDatacenters(region.getRegionId())) {
         assertNotNull(datacenter.getDataCenterId());
      }
   }

   public void testSubscriptions() throws Exception {
      for (Subscription subscription : api.listSubscriptions()) {
         assertNotNull(subscription.getRegionId());
      }
   }
}
