package io.cloudsoft.enstratius.api.features;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudsoft.enstratius.api.client.Client;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.client.Response;
import io.cloudsoft.enstratius.api.model.Job;
import io.cloudsoft.enstratius.api.model.MachineImage;
import io.cloudsoft.enstratius.api.model.Server;
import io.cloudsoft.enstratius.api.model.ServerProduct;
import io.cloudsoft.enstratius.api.requests.infrastructure.*;
import io.cloudsoft.enstratius.api.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfrastructureApi {

   private final Client client;

   public InfrastructureApi(Client client) {
      this.client = client;
   }

   public List<MachineImage> listMachineImages(String regionId) throws IOException {
      Request request = new ListMachineImages(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<MachineImage>>> typeReference =
              new TypeReference<HashMap<String, List<MachineImage>>>() {
              };
      Map<String, List<MachineImage>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public List<ServerProduct> listServerProducts(String regionId) throws IOException {
      Request request = new ListServerProducts(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<ServerProduct>>> typeReference =
              new TypeReference<HashMap<String, List<ServerProduct>>>() {
              };
      Map<String, List<ServerProduct>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public List<Server> listServers(String regionId) throws IOException {
      Request request = new ListServers(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Server>>> typeReference =
              new TypeReference<HashMap<String, List<Server>>>() {
              };
      Map<String, List<Server>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public Server getServer(String serverId, String regionId) throws IOException {
      Request request = new GetServer(serverId, regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Server>>> typeReference =
              new TypeReference<HashMap<String, List<Server>>>() {
              };
      Map<String, List<Server>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }

   public Job launchServer(String name, String description, String budgetId, String machineImageId,
                           String dataCenterId, String firewallId) throws IOException {
      Request request = new LaunchServer(name, description, budgetId, machineImageId, dataCenterId, firewallId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Job>>> typeReference =
              new TypeReference<HashMap<String, List<Job>>>() {
              };
      Map<String, List<Job>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }

   public void deleteServer(String serverId, String reason) throws IOException {
      if (reason.length() < 10)
         throw new RuntimeException();

      Request request = new DeleteServer(serverId, reason);
      client.execute(request);
   }
}