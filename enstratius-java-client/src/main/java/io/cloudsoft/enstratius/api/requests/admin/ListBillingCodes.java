package io.cloudsoft.enstratius.api.requests.admin;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListBillingCodes extends AbstractRequest implements Request {

   private final static String API_CALL = "admin/BillingCode";
   private final String regionId;

   public ListBillingCodes(String regionId) {
      this.regionId = checkNotNull(regionId, "regionId");
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
      queryParams.add(new BasicNameValuePair("regionId", regionId));
      return queryParams;
   }

   @Override
   public String getPathToResult() {
      return "billingCodes";
   }
}
