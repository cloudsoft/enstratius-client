package io.cloudsoft.enstratius.api.client;

import io.cloudsoft.enstratius.api.mime.TypedOutput;
import org.apache.http.Header;
import org.apache.http.NameValuePair;

import java.util.List;

import static io.cloudsoft.enstratius.api.utils.EnstratiusConstants.DEFAULT_VERSION;

public abstract class AbstractRequest implements Request {

   protected String resolveUri(String apiCall) {
      return String.format("/api/enstratus/%s/%s", DEFAULT_VERSION, apiCall);
   }

   @Override
   public TypedOutput getBody() {
      return null;
   }

   @Override
   public List<NameValuePair> getQueryParameters() {
      return null;
   }

   @Override
   public String getPathToResult() {
      return null;
   }

   @Override
   public Header[] getHeaders() {
      return new Header[0];
   }
}
