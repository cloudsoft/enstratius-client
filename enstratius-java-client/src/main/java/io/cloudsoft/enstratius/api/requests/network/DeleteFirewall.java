package io.cloudsoft.enstratius.api.requests.network;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteFirewall extends AbstractRequest implements Request {

   private final static String API_CALL = "network/Firewall/%s";

   private final String firewallId;
   private final String reason;

   public DeleteFirewall(String firewallId, String reason) {
      this.firewallId = checkNotNull(firewallId, "firewallId");
      this.reason = checkNotNull(reason, "reason");
   }

   @Override
   public String getURI() {
      return String.format(resolveUri(API_CALL), firewallId);
   }

   @Override
   public HttpMethod getRestMethodName() {
      return HttpMethod.DELETE;
   }

   @Override
   public List<NameValuePair> getQueryParameters() {
      List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
      queryParams.add(new BasicNameValuePair("reason", reason));
      return queryParams;
   }

}
