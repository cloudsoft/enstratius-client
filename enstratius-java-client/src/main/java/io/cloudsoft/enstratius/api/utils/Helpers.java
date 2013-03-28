package io.cloudsoft.enstratius.api.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.cloudsoft.enstratius.api.features.AdminApi;
import io.cloudsoft.enstratius.api.features.GeographyApi;
import io.cloudsoft.enstratius.api.features.InfrastructureApi;
import io.cloudsoft.enstratius.api.features.NetworkApi;
import io.cloudsoft.enstratius.api.model.*;

import java.io.IOException;

import static com.google.common.collect.Iterables.tryFind;

public class Helpers {
   public static Region tryFindFirstRegionOrNull(Jurisdiction jurisdiction, GeographyApi geographyApi) throws IOException {
      return tryFind(geographyApi.listRegions(null, jurisdiction, null),
              Predicates.notNull()).orNull();
   }

   public static BillingCode tryFindFirstBillingCodeOrNull(String regionId, AdminApi adminApi) throws IOException {
      return tryFind(adminApi.listBillingCodes(regionId), Predicates.notNull()).orNull();
   }

   public static Datacenter tryFindFirstDatacenterOrNull(String regionId, GeographyApi geographyApi) throws IOException {
      return tryFind(geographyApi.listDatacenters(regionId), Predicates.notNull()).orNull();
   }

   public static MachineImage tryFindFirstMachineImageOrNull(String regionId, InfrastructureApi infrastructureApi)
           throws IOException {
      return tryFind(infrastructureApi.listMachineImages(regionId), Predicates.notNull())
              .orNull();
   }

   public static MachineImage tryFindMachineImageMatchingPredicateOrNull(String regionId,
                                                                         InfrastructureApi infrastructureApi,
                                                                         Predicate<MachineImage> predicate)
           throws IOException {
      return tryFind(infrastructureApi.listMachineImages(regionId), predicate).orNull();
   }

   public static Firewall tryFindFirstFirewallOrNull(String regionId, NetworkApi networkApi) throws IOException {
      return tryFind(networkApi.listFirewalls(regionId), Predicates.notNull()).orNull();
   }

   public static Firewall tryFindFirewallMatchingPredicateOrNull(String regionId,
                                                                 NetworkApi networkApi,
                                                                 Predicate<Firewall> predicate)
           throws IOException {
      return tryFind(networkApi.listFirewalls(regionId), predicate).orNull();
   }
}
