package io.cloudsoft.enstratius.api.requests.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.mime.TypedOutput;
import io.cloudsoft.enstratius.api.model.Direction;
import io.cloudsoft.enstratius.api.model.Protocol;
import io.cloudsoft.enstratius.api.utils.JsonConverter;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddFirewallRule extends AbstractRequest implements Request {

   private final static String API_CALL = "network/FirewallRule";

   private final String firewallId;
   private final String cidr;
   private final String startPort;
   private final String endPort;
   private final Direction direction;
   private final Protocol protocol;

   public AddFirewallRule(String firewallId, String cidr, String startPort, String endPort, Direction direction, Protocol protocol) {
      this.firewallId = checkNotNull(firewallId, "firewallId");
      this.cidr = checkNotNull(cidr, "cidr");
      this.startPort = checkNotNull(startPort, "startPort");
      this.endPort = checkNotNull(endPort, "endPort");
      this.direction = checkNotNull(direction, "direction");
      this.protocol = checkNotNull(protocol, "protocol");
   }

   @Override
   public String getURI() {
      return resolveUri(API_CALL);
   }

   @Override
   public HttpMethod getRestMethodName() {
      return HttpMethod.POST;
   }

   @Override
   public TypedOutput getBody() {

      Map<String, Object> firewallRule = Maps.newLinkedHashMap();
      firewallRule.put("firewallId", firewallId);
      firewallRule.put("cidr", cidr);
      firewallRule.put("startPort", startPort);
      firewallRule.put("endPort", endPort);
      firewallRule.put("direction", direction);
      firewallRule.put("protocol", protocol);

      List<Object> rulesList = Lists.newArrayList();
      rulesList.add(firewallRule);
      Map<String, Object> addFirewallRuleBody = Maps.newLinkedHashMap();
      addFirewallRuleBody.put("addRule", rulesList);
      return JsonConverter.toBody(addFirewallRuleBody);
   }

   @Override
   public String getPathToResult() {
      return "jobs";
   }
}
