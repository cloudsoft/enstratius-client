package io.cloudsoft.enstratius.api.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudsoft.enstratius.api.mime.TypedInput;
import io.cloudsoft.enstratius.api.mime.TypedOutput;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Throwables.propagate;

public class JsonConverter {

   public static <T> Map<String, List<T>> fromBody(TypedInput body, TypeReference typeRef) throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      InputStreamReader isr = new InputStreamReader(body.in(), "UTF-8");
      return mapper.readValue(isr, typeRef);
   }

   public static TypedOutput toBody(Object object) {
      ObjectMapper mapper = new ObjectMapper();
      try {
         String s = mapper.writeValueAsString(object);
         return new JsonTypedOutput(s.getBytes());
      } catch (UnsupportedEncodingException e) {
         throw propagate(e);
      } catch (JsonMappingException e) {
         throw propagate(e);
      } catch (JsonGenerationException e) {
         throw propagate(e);
      } catch (IOException e) {
         throw propagate(e);
      }
   }

   static class JsonTypedOutput implements TypedOutput {
      private final byte[] jsonBytes;

      JsonTypedOutput(byte[] jsonBytes) {
         this.jsonBytes = jsonBytes;
      }

      @Override
      public String mimeType() {
         return "application/json; charset=UTF-8";
      }

      @Override
      public long length() {
         return jsonBytes.length;
      }

      @Override
      public void writeTo(OutputStream out) throws IOException {
         out.write(jsonBytes);
      }
   }
}
