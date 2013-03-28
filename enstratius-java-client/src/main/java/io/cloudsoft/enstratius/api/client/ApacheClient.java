package io.cloudsoft.enstratius.api.client;

import io.cloudsoft.enstratius.api.mime.TypedByteArray;
import io.cloudsoft.enstratius.api.mime.TypedOutput;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Throwables.propagate;
import static io.cloudsoft.enstratius.api.utils.EnstratiusConstants.DEFAULT_BASEURL;

public class ApacheClient implements Client {

   private static final Logger log = LoggerFactory.getLogger(ApacheClient.class);

   private final HttpClient client;

   public ApacheClient() {
      this(new DefaultHttpClient());
   }

   public ApacheClient(HttpClient client) {
      this.client = client;
   }

   @Override
   public Response execute(Request request) throws IOException {
      // Create and prepare the Apache request object.
      HttpUriRequest apacheRequest = createRequest(request);
      prepareRequest(apacheRequest);

      // Obtain and prepare the Apache response object.
      HttpResponse apacheResponse = client.execute(apacheRequest);
      prepareResponse(apacheResponse);

      return parseResponse(apacheResponse);
   }

   /**
    * Callback for additional preparation of the request before execution.
    */
   protected void prepareRequest(HttpUriRequest request) {
   }

   /**
    * Callback for additional preparation of the response before parsing.
    */
   protected void prepareResponse(HttpResponse response) {
   }

   static HttpUriRequest createRequest(Request request) {
      return new GenericHttpRequest(request);
   }

   static Response parseResponse(HttpResponse response) throws IOException {
      StatusLine statusLine = response.getStatusLine();
      int status = statusLine.getStatusCode();
      String reason = statusLine.getReasonPhrase();
      String contentType = "application/octet-stream";
      List<Header> headers = new ArrayList<Header>();

      for (Header header : response.getAllHeaders()) {
         String name = header.getName();
         String value = header.getValue();
         if ("Content-Type".equalsIgnoreCase(name)) {
            contentType = value;
         }
         headers.add(new BasicHeader(name, value) {
         });
      }
      TypedByteArray body = null;
      HttpEntity entity = response.getEntity();
      if (entity != null) {
         byte[] bytes = EntityUtils.toByteArray(entity);
         body = new TypedByteArray(contentType, bytes);
      }

      return new Response(status, reason, headers, body);
   }

   private static class GenericHttpRequest extends HttpEntityEnclosingRequestBase {
      private final String method;

      GenericHttpRequest(Request request) {
         super();
         method = request.getRestMethodName().name();
         URL base = null;
         URI uri = null;
         try {
            base = new URL(DEFAULT_BASEURL);
            uri = new URI(base.getProtocol(), base.getHost(), request.getURI(), null);
         } catch (MalformedURLException e) {
            throw propagate(e);
         } catch (URISyntaxException e) {
            throw propagate(e);
         }

         // Add all headers.
         for (Header header : request.getHeaders()) {
            addHeader(new BasicHeader(header.getName(), header.getValue()));
         }
         // Add the content body, if any.
         TypedOutput body = request.getBody();
         if (body != null) {
            setEntity(new TypedOutputEntity(body));
         }

         // Add query parameters, if any
         List<NameValuePair> queryParameters = request.getQueryParameters();
         if (queryParameters != null && !queryParameters.isEmpty()) {
            try {
               uri = new URIBuilder(uri).setQuery(URLEncodedUtils.format(queryParameters, "UTF-8")).build();
            } catch (URISyntaxException e) {
               throw propagate(e);
            }
         }
         setURI(uri);
      }

      @Override
      public String getMethod() {
         return method;
      }
   }

   /**
    * Container class for passing an entire {@link TypedOutput} as an {@link org.apache.http.HttpEntity}.
    */
   static class TypedOutputEntity extends AbstractHttpEntity {
      final TypedOutput typedOutput;

      TypedOutputEntity(TypedOutput typedOutput) {
         this.typedOutput = typedOutput;
         setContentType(typedOutput.mimeType());
      }

      @Override
      public boolean isRepeatable() {
         return true;
      }

      @Override
      public long getContentLength() {
         return typedOutput.length();
      }

      @Override
      public InputStream getContent() throws IOException {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         typedOutput.writeTo(out);
         return new ByteArrayInputStream(out.toByteArray());
      }

      @Override
      public void writeTo(OutputStream out) throws IOException {
         typedOutput.writeTo(out);
      }

      @Override
      public boolean isStreaming() {
         return false;
      }
   }

}