package io.cloudsoft.enstratius.api.requests.network;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetFirewall extends AbstractRequest implements Request {

   private final static String API_CALL = "network/Firewall/%s";
   private final String firewallId;
   private final String accountId;
   private final String regionId;

   public GetFirewall(String firewallId) {
      this(checkNotNull(firewallId, "firewallId"), null, null);
   }

   public GetFirewall(String firewallId, String accountId, String regionId) {
      this.firewallId = checkNotNull(firewallId, "firewallId");
      this.accountId = accountId;
      this.regionId = regionId;
   }

   @Override
   public String getURI() {
      return String.format(resolveUri(API_CALL), firewallId);
   }

   @Override
   public HttpMethod getRestMethodName() {
      return HttpMethod.GET;
   }

   @Override
   public List<NameValuePair> getQueryParameters() {
      List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
      if (accountId != null) queryParams.add(new BasicNameValuePair("accountId", accountId));
      if (regionId != null) queryParams.add(new BasicNameValuePair("regionId", regionId));
      return queryParams;
   }

   @Override
   public String getPathToResult() {
      return "firewalls";
   }

}
