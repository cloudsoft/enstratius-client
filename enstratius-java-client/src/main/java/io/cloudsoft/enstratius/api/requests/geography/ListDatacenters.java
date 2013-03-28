package io.cloudsoft.enstratius.api.requests.geography;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A datacenter is a zone on aws terminology
 *
 * @author andrea
 */
public class ListDatacenters extends AbstractRequest implements Request {

   private final static String API_CALL = "geography/DataCenter";
   private final String regionId;

   public ListDatacenters(String regionId) {
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
      return "dataCenters";
   }
}
