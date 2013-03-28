package brooklyn.location.enstratius;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.NoSuchElementException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import brooklyn.config.BrooklynProperties;
import brooklyn.location.LocationRegistry;
import brooklyn.location.basic.BasicLocationRegistry;
import brooklyn.location.basic.LocationResolverTest;
import brooklyn.management.ManagementContext;
import brooklyn.management.internal.LocalManagementContext;
import brooklyn.util.MutableMap;

import com.google.common.collect.ImmutableMap;

public class EnstratiusLocationResolverTest {
    
    private BrooklynProperties brooklynProperties;
    private ManagementContext managementContext;
    private LocationRegistry registry;

    @BeforeMethod(alwaysRun=true)
    public void setUp() throws Exception {
        brooklynProperties = BrooklynProperties.Factory.newEmpty();
        managementContext = new LocalManagementContext(brooklynProperties);
        registry = new BasicLocationRegistry(managementContext);
        
        brooklynProperties.put("brooklyn.enstratius.identity", "id");
        brooklynProperties.put("brooklyn.enstratius.credential", "cred");
    }
    
    public static final Map<String, String> PROPS = MutableMap.of("brooklyn.enstratius.identity", "x", 
            "brooklyn.enstratius.credential", "y");

    @Test
    public void testEnstratiusLoads() {
        Assert.assertTrue(LocationResolverTest.resolve(PROPS, "enstratius:us-east-1:eu-west-1a") instanceof EnstratiusLocation);
    }
    
    @Test
    public void testEnstratiusRegionOnlyLoads() {
        Assert.assertTrue(LocationResolverTest.resolve(PROPS, "enstratius:us-east-1") instanceof EnstratiusLocation);
    }
    
    @Test
    public void testResolvesEnstratius() throws Exception {
        // test with provider that has no region
        assertEnstratiusEquals(resolve("enstratius:eu-west-1:eu-west-1a"), "eu-west-1", "eu-west-1a");
    }
    
    private void assertEnstratiusEquals(EnstratiusLocation loc, String expectedRegion, String expectedDatacenter) {
        assertEquals(loc.getRegion(), expectedRegion);
        assertEquals(loc.getDatacenter(), expectedDatacenter);
    }

    private EnstratiusLocation resolve(String spec) {
        return new EnstratiusResolver().newLocationFromString(ImmutableMap.of(), spec, registry);
    }
    
    @Test(expectedExceptions={ NoSuchElementException.class, IllegalArgumentException.class },
            expectedExceptionsMessageRegExp="No resolver found for.*")
    public void testEnstratiusOnlyFails() {
        LocationResolverTest.resolve("enstratus");
    }
}
