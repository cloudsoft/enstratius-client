package io.cloudsoft.enstratius.api.requests.admin;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;

public class GetJob extends AbstractRequest {

   private final static String API_CALL = "admin/Job/%s";
   private String jobId;

   public GetJob(String jobId) {
      this.jobId = jobId;
   }

   @Override
   public String getURI() {
      return String.format(resolveUri(API_CALL), jobId);
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
