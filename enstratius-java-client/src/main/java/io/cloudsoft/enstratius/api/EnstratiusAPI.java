package io.cloudsoft.enstratius.api;

import io.cloudsoft.enstratius.api.client.Client;
import io.cloudsoft.enstratius.api.features.AdminApi;
import io.cloudsoft.enstratius.api.features.GeographyApi;
import io.cloudsoft.enstratius.api.features.InfrastructureApi;
import io.cloudsoft.enstratius.api.features.NetworkApi;

import static com.google.common.base.Preconditions.checkNotNull;

public class EnstratiusAPI {

   private final GeographyApi geographyApi;
   private final AdminApi adminApi;
   private final InfrastructureApi infrastructureApi;
   private final NetworkApi networkApi;

   public EnstratiusAPI(Client client) {
      checkNotNull(client, "client");
      this.adminApi = new AdminApi(client);
      this.geographyApi = new GeographyApi(client);
      this.infrastructureApi = new InfrastructureApi(client);
      this.networkApi = new NetworkApi(client);
   }

   public AdminApi getAdminApi() {
      return adminApi;
   }

   public GeographyApi getGeographyApi() {
      return geographyApi;
   }

   public InfrastructureApi getInfrastructureApi() {
      return infrastructureApi;
   }

   public NetworkApi getNetworkApi() {
      return networkApi;
   }

}
