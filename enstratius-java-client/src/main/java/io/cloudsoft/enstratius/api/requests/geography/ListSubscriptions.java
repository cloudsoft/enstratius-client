package io.cloudsoft.enstratius.api.requests.geography;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;

/**
 * A subscription describes the capabilities of a specific region as matched by
 * your subscription to the region.
 *
 * @author andrea
 */
public class ListSubscriptions extends AbstractRequest implements Request {

   private final static String API_CALL = "geography/Subscription";

   @Override
   public String getURI() {
      return resolveUri(API_CALL);
   }

   @Override
   public HttpMethod getRestMethodName() {
      return HttpMethod.GET;
   }


   @Override
   public String getPathToResult() {
      return "subscriptions";
   }

}
