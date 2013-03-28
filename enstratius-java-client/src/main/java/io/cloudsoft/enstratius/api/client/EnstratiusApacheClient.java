package io.cloudsoft.enstratius.api.client;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import io.cloudsoft.enstratius.api.Details;
import io.cloudsoft.enstratius.api.RequestSignature;
import org.apache.http.client.methods.HttpUriRequest;

public class EnstratiusApacheClient extends ApacheClient {

   private static final String ACCEPT_JSON = "application/json";
   protected static final String USER_AGENT = "enStratius Java example";

   private String accessKey;
   private String secretKey;

   public EnstratiusApacheClient(String accessKey, String secretKey) {
      this.accessKey = accessKey;
      this.secretKey = secretKey;
   }

   @Override
   protected void prepareRequest(HttpUriRequest request) {
      super.prepareRequest(request);
      String timestamp = getTimestamp();
      String signature = generateSignature(accessKey, secretKey, request.getMethod(), request.getURI().getPath(), timestamp);
      request.addHeader("x-esauth-signature", signature);
      request.addHeader("x-esauth-timestamp", timestamp);
      request.addHeader("User-Agent", USER_AGENT);
      request.addHeader("Accept", ACCEPT_JSON);
      request.addHeader("x-es-details", Details.BASIC.toString());
      request.addHeader("x-es-with-perms", "false");
      request.addHeader("x-esauth-access", accessKey);
   }

   private static String getTimestamp() {
      return Long.toString(System.currentTimeMillis());
   }

   private static String generateSignature(String accessKey, String secretKey, String methodName, String uri,
                                           String timestamp) {
      String toSign = Joiner.on(":").join(ImmutableList.of(accessKey, methodName, uri, timestamp, USER_AGENT));
      String signature;
      try {
         signature = RequestSignature.sign(secretKey, toSign);
      } catch (Exception e) {
         throw Throwables.propagate(e);
      }
      return signature;
   }

}
