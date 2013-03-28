package brooklyn.location.enstratius;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brooklyn.location.NoMachinesAvailableException;
import brooklyn.location.basic.SshMachineLocation;
import brooklyn.management.ManagementContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import brooklyn.entity.basic.Entities;

import com.google.common.collect.ImmutableMap;

@Test(groups = { "Live" })
public class EnstratiusLocationLiveTest {
	
	public static final Logger LOG = LoggerFactory.getLogger(EnstratiusLocationLiveTest.class);
	
	private static final String EUWEST_REGION_NAME = "eu-west-1";
    private static final String PROVIDER_ID = "ami-524e4726";
    private static final String GROUP_NAME = "default";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

	private final String provider;
	protected ManagementContext managementContext;
	protected EnstratiusLocation location;
	protected List<SshMachineLocation> machines = Lists.newArrayList();
			
    protected EnstratiusLocationLiveTest(String provider) {
        this.provider = "enstratius";
    }

    @DataProvider(name = "fromProviderId")
    public Object[][] providerIdAndGroupName() {
        return new Object[][] { { EUWEST_REGION_NAME, PROVIDER_ID, GROUP_NAME, USER, PASSWORD } };
    }
    
    @BeforeMethod(alwaysRun=true)
    public void setUp() {
    	managementContext = Entities.newManagementContext(ImmutableMap.of("provider", provider));
    }
    
    @Test(dataProvider = "fromProviderId")
    public void testProvisionVmUsingImageId(String regionName, String providerId, String groupName, String user, 
            String password) {
		location = (EnstratiusLocation) managementContext.getLocationRegistry().resolve(provider + ":" + regionName);
        SshMachineLocation machine;
		try {
			machine = obtainMachine(ImmutableMap.of("providerId", providerId, "groupName", groupName, "user", user, 
			        "password", password));
	        LOG.info("Provisioned vm {}; checking if ssh'able", machine.toString());
	        assertTrue(machine.isSshable());
		} catch (NoMachinesAvailableException e) {
			fail();
		}
    }
    
    // Use this utility method to ensure machines are released on tearDown
    protected SshMachineLocation obtainMachine(Map<?, ?> flags) throws NoMachinesAvailableException {
        SshMachineLocation result = location.obtain(flags);
        machines.add(result);
        return result;
    }
    
    @AfterMethod(alwaysRun=true)
    public void tearDown() throws Exception {
        List<Exception> exceptions = Lists.newArrayList();
        for(SshMachineLocation machine : machines) {
            try {
                location.release(machine);
            } catch (Exception e) {
                LOG.warn("Error releasing machine {}; continuing...", machine.toString());
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            throw exceptions.get(0);
        }
        machines.clear();
    }
}
