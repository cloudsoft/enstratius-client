package io.cloudsoft.enstratius.api.requests.geography;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.model.Jurisdiction;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ListRegions extends AbstractRequest implements Request {

   private final static String API_CALL = "geography/Region";
   private final String accountId;
   private final Jurisdiction jurisdiction;
   private final String scope;

   public ListRegions(String accountId, Jurisdiction jurisdiction, String scope) {
      this.accountId = accountId;
      this.jurisdiction = jurisdiction;
      this.scope = scope;
   }

   @Override
   public String getURI() {
      return resolveUri(API_CALL);
   }

   @Override
   public HttpMethod getRestMethodName() {
      return HttpMethod.GET;
   }

   @Override
   public List<NameValuePair> getQueryParameters() {
      List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
      if (accountId != null) queryParams.add(new BasicNameValuePair("accountId", accountId));
      if (jurisdiction != null) queryParams.add(new BasicNameValuePair("jurisdiction", jurisdiction.toString()));
      if (scope != null) queryParams.add(new BasicNameValuePair("scope", scope));
      return queryParams;
   }

   @Override
   public String getPathToResult() {
      return "regions";
   }
}
