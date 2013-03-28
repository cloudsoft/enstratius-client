package io.cloudsoft.enstratius.api.features;

import io.cloudsoft.enstratius.api.EnstratiusAPI;
import io.cloudsoft.enstratius.api.client.EnstratiusApacheClient;
import org.testng.annotations.Test;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.cloudsoft.enstratius.api.utils.EnstratiusConstants.*;

@Test(groups = "live")
public class BasicEnstratusLiveTest {

   protected final String baseUrl;
   protected final String apiVersion;
   protected final String accessKey;
   protected final String secretKey;

   protected final EnstratiusAPI enstratiusAPI;

   protected BasicEnstratusLiveTest() {
      this.baseUrl = checkNotNull(System.getProperty(ENSTRATIUS_API_ENDPOINT, DEFAULT_BASEURL), ENSTRATIUS_API_ENDPOINT);
      this.apiVersion = checkNotNull(System.getProperty(ENSTRATIUS_API_VERSION, DEFAULT_VERSION),
              ENSTRATIUS_API_VERSION);
      this.accessKey = checkNotNull(System.getProperty(ENSTRATIUS_API_ACCESS_KEY), ENSTRATIUS_API_ACCESS_KEY);
      this.secretKey = checkNotNull(System.getProperty(ENSTRATIUS_API_SECRET_KEY), ENSTRATIUS_API_SECRET_KEY);
      this.enstratiusAPI = new EnstratiusAPI(new EnstratiusApacheClient(accessKey, secretKey));
   }

}
