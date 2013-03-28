package brooklyn.location.enstratius;

import brooklyn.config.ConfigKey;
import brooklyn.event.basic.BasicConfigKey.StringConfigKey;
import brooklyn.location.basic.LocationConfigKeys;
import brooklyn.location.cloud.CloudLocationConfig;

public interface EnstratiusLocationConfig extends CloudLocationConfig {

    /**
     * defaultValus is 15 * 60 = 900 secs
     */
    public static final ConfigKey<String> MAX_WAIT_IN_SEC = new StringConfigKey("maxWaitInSec", "maxWaitInSec", "900");
    
    public static final ConfigKey<String> CLOUD_DATACENTER = new StringConfigKey("datacenter", "datacenter", null);
    
    public static final ConfigKey<String> JURISDICTION = new StringConfigKey("jurisdiction", "jurisdiction", "EU");
    
    public static final ConfigKey<String> GROUP_NAME = new StringConfigKey("groupName");
    
    public static final ConfigKey<String> PROVIDER_ID = new StringConfigKey("providerId");
}
