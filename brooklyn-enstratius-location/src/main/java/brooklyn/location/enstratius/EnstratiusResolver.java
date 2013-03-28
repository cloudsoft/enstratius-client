package brooklyn.location.enstratius;

import static brooklyn.location.cloud.CloudLocationConfig.CLOUD_REGION_ID;
import static brooklyn.location.enstratius.EnstratiusLocationConfig.CLOUD_DATACENTER;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brooklyn.location.LocationRegistry;
import brooklyn.location.basic.BasicLocationRegistry;
import brooklyn.location.basic.RegistryLocationResolver;
import brooklyn.util.MutableMap;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class EnstratiusResolver implements RegistryLocationResolver {

    public static final Logger log = LoggerFactory.getLogger(EnstratiusResolver.class);
    
    public static final Collection<String> AWS_REGIONS = Arrays.asList(
            // from http://docs.amazonwebservices.com/general/latest/gr/rande.html as of Apr 2012.
            // it is suggested not to maintain this list here, instead to require aws-ec2 explicitly named.
            "eu-west-1","us-east-1","us-west-1","us-west-2","ap-southeast-1","ap-northeast-1","sa-east-1");

    public static final String ENSTRATIUS = "enstratius";

    @Override
    public String getPrefix() {
        return ENSTRATIUS;
    }

    @Override
    public boolean accepts(String spec, LocationRegistry registry) {
        return BasicLocationRegistry.isResolverPrefixForSpec(this, spec, true);
    }

    @Override
    public EnstratiusLocation newLocationFromString(Map properties, String spec) {
        return newLocationFromString(spec, null, properties, new MutableMap());
    }

    @Override
    public EnstratiusLocation newLocationFromString(Map locationFlags, String spec, LocationRegistry registry) {
        return newLocationFromString(spec, registry, registry.getProperties(), locationFlags);
    }

    @SuppressWarnings("unchecked")
    protected EnstratiusLocation newLocationFromString(String spec, LocationRegistry registry, Map properties, Map locationFlags) {
        EnstratiusSpecParser details = EnstratiusSpecParser.parse(spec, false);
        if (registry!=null) properties.putAll(registry.getProperties());
        properties.putAll(locationFlags);
        properties.put(CLOUD_REGION_ID, details.region);
        properties.put(CLOUD_DATACENTER, details.datacenter);

        return new EnstratiusLocation(properties);
    }
    
    protected static class EnstratiusSpecParser {
        String region;
        String datacenter;
        
        public static EnstratiusSpecParser parse(String spec, boolean dryrun) {
            EnstratiusSpecParser result = new EnstratiusSpecParser();
            Iterable<String> splittedSpec = Splitter.on(":").split(spec);
            if (spec.startsWith(ENSTRATIUS)) {
                if(Iterables.size(splittedSpec) == 1) {
                    if (dryrun) return null;
                    throw new IllegalArgumentException("Cannot use '"+spec+"' as a location ID; it is insufficient. " +
                		"Try enstratius:us-west-1 (for example).");
                } else {
                    result.region = Iterables.get(splittedSpec, 1);
                    String last = Iterables.getLast(splittedSpec);
                    result.datacenter = last.equals(result.region) ? "" : last;
                }
            }
            return result;
        }
        
        public String getRegion() {
            return region;
        }
        
        public String getDatacenter() {
            return datacenter;
        }
        
    }


}
