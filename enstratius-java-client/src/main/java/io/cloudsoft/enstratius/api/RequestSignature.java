package io.cloudsoft.enstratius.api;

import com.google.common.base.Charsets;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.ByteStreams.readBytes;

public class RequestSignature {

   public static String sign(String key, String toSign) throws Exception {
      final Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
      ByteProcessor<byte[]> processor = asByteProcessor(mac);
      InputSupplier inputSupplier = ByteStreams.newInputStreamSupplier(toSign.getBytes(Charsets.UTF_8));
      return new BASE64Encoder().encodeBuffer(readBytes(inputSupplier, processor)).trim();
   }

   public static ByteProcessor<byte[]> asByteProcessor(final Mac mac) {
      checkNotNull(mac, "mac");
      return new ByteProcessor<byte[]>() {
         public boolean processBytes(byte[] buf, int off, int len) {
            mac.update(buf, off, len);
            return true;
         }
         public byte[] getResult() {
            return mac.doFinal();
         }
      };
   }
}
