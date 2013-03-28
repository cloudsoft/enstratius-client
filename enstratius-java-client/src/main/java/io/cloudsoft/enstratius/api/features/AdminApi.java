package io.cloudsoft.enstratius.api.features;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudsoft.enstratius.api.client.Client;
import io.cloudsoft.enstratius.api.client.Request;
import io.cloudsoft.enstratius.api.client.Response;
import io.cloudsoft.enstratius.api.model.BillingCode;
import io.cloudsoft.enstratius.api.model.Job;
import io.cloudsoft.enstratius.api.requests.admin.GetJob;
import io.cloudsoft.enstratius.api.requests.admin.ListBillingCodes;
import io.cloudsoft.enstratius.api.requests.admin.ListJobs;
import io.cloudsoft.enstratius.api.utils.JsonConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminApi {

   private final Client client;

   public AdminApi(Client client) {
      this.client = client;
   }

   public List<BillingCode> listBillingCodes(String regionId) throws IOException {
      Request request = new ListBillingCodes(regionId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<BillingCode>>> typeReference =
              new TypeReference<HashMap<String, List<BillingCode>>>() {
              };
      Map<String, List<BillingCode>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public List<Job> listJobs() throws IOException {
      Request request = new ListJobs();
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Job>>> typeReference =
              new TypeReference<HashMap<String, List<Job>>>() {
              };
      Map<String, List<Job>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult());
   }

   public Job getJob(String jobId) throws IOException {
      Request request = new GetJob(jobId);
      Response response = client.execute(request);
      TypeReference<HashMap<String, List<Job>>> typeReference =
              new TypeReference<HashMap<String, List<Job>>>() {
              };
      Map<String, List<Job>> map = JsonConverter.fromBody(response.getBody(), typeReference);
      return map.get(request.getPathToResult()).get(0);
   }
}