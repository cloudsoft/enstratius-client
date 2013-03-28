package io.cloudsoft.enstratius.api.requests.infrastructure;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.client.AbstractRequest;
import io.cloudsoft.enstratius.api.client.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteServer extends AbstractRequest implements Request {

   private final static String API_CALL = "infrastructure/Server/%s";

   private final String serverId;
   private final String reason;

   public DeleteServer(String serverId, String reason) {
      this.serverId = checkNotNull(serverId, "serverId");
      this.reason = checkNotNull(reason, "reason");
   }

   @Override
   public String getURI() {
      return String.format(resolveUri(API_CALL), serverId);
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
