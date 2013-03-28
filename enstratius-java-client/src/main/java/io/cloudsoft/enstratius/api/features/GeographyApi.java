package io.cloudsoft.enstratius.api.features;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudsoft.enstratius.api.client.Client;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.client.Response;
import io.cloudsoft.enstratius.api.model.*;
import io.cloudsoft.enstratius.api.requests.geography.*;
import io.cloudsoft.enstratius.api.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeographyApi {

   private final Client client;

   public GeographyApi(Client client) {
      this.client = client;
   }

   public List<Region> listRegions() throws IOException {
      return this.listRegions(null, null, null);
   }

   public List<Region> listRegions(String accountId, Jurisdiction jurisdiction, String scope) throws IOException {
      Request request = new ListRegions(accountId, jurisdiction, scope);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Region>>> typeReference =
              new TypeReference<HashMap<String, List<Region>>>() {
              };
      Map<String, List<Region>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public Region getRegion(String regionId) throws IOException {
      return this.getRegion(regionId, null, null, null);
   }

   public Region getRegion(String regionId, String accountId, Jurisdiction jurisdiction, String scope) throws IOException {
      Request request = new GetRegion(regionId, accountId, jurisdiction, scope);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Region>>> typeReference = new TypeReference<HashMap<String, List<Region>>>() {
      };
      Map<String, List<Region>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }

   public List<Cloud> listClouds() throws IOException {
      Request request = new ListClouds();
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Cloud>>> typeReference = new TypeReference<HashMap<String, List<Cloud>>>() {
      };
      Map<String, List<Cloud>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public List<Datacenter> listDatacenters(String regionId) throws IOException {
      Request request = new ListDatacenters(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Datacenter>>> typeReference = new TypeReference<HashMap<String, List<Datacenter>>>() {
      };
      Map<String, List<Datacenter>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public List<Subscription> listSubscriptions() throws IOException {
      Request request = new ListSubscriptions();
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Subscription>>> typeReference = new TypeReference<HashMap<String, List<Subscription>>>() {
      };
      Map<String, List<Subscription>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

}
