package io.cloudsoft.enstratius.api.requests.admin;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;

public class ListJobs extends AbstractRequest implements Request {

   private final static String API_CALL = "admin/Job";

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
      return "jobs";
   }
}
