package io.cloudsoft.enstratius.api.features;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudsoft.enstratius.api.client.Client;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.client.Response;
import io.cloudsoft.enstratius.api.model.*;
import io.cloudsoft.enstratius.api.requests.network.*;
import io.cloudsoft.enstratius.api.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkApi {

   private final Client client;

   public NetworkApi(Client client) {
      this.client = client;
   }

   public List<Firewall> listFirewalls(String regionId) throws IOException {
      Request request = new ListFirewalls(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Firewall>>> typeReference =
              new TypeReference<HashMap<String, List<Firewall>>>() {
              };
      Map<String, List<Firewall>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public Job addFirewall(String name, String description, String budgetId, String regionId,
                          Color label) throws Exception {
      Request request = new AddFirewall(name, description, budgetId, regionId, label);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Job>>> typeReference =
              new TypeReference<HashMap<String, List<Job>>>() {
              };
      Map<String, List<Job>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }

   public void addFirewallRule(String firewallId, String cidr, String startPort, String endPort, Direction direction,
                               Protocol protocol) throws Exception {
      // TODO at the moment no returned object
      Request request = new AddFirewallRule(firewallId, cidr, startPort, endPort, direction, protocol);
      client.execute(request);
   }

   public Firewall getFirewall(String firewallId) throws IOException {
      return this.getFirewall(firewallId, null, null);
   }

   public Firewall getFirewall(String firewallId, String accountId, String regionId) throws IOException {
      Request request = new GetFirewall(firewallId, accountId, regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Firewall>>> typeReference = new TypeReference<HashMap<String, List<Firewall>>>() {
      };
      Map<String, List<Firewall>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }

   public void deleteFirewall(String firewallId, String reason) throws Exception {
      if (reason.length() < 10)
         throw new RuntimeException();
      Request request = new DeleteFirewall(firewallId, reason);
      client.execute(request);
   }
}
