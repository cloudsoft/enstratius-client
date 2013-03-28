package brooklyn.location.enstratius;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.cloudsoft.enstratius.api.model.Jurisdiction.valueOf;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstBillingCodeOrNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstDatacenterOrNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirewallMatchingPredicateOrNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindFirstRegionOrNull;
import static io.cloudsoft.enstratius.api.utils.Helpers.tryFindMachineImageMatchingPredicateOrNull;
import io.cloudsoft.enstratius.api.EnstratiusAPI;
import io.cloudsoft.enstratius.api.client.EnstratiusApacheClient;
import io.cloudsoft.enstratius.api.model.Job;
import io.cloudsoft.enstratius.api.model.Server;
import io.cloudsoft.enstratius.api.utils.FirewallMatcher;
import io.cloudsoft.enstratius.api.utils.Jobs;
import io.cloudsoft.enstratius.api.utils.MachineImageMatcher;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brooklyn.location.NoMachinesAvailableException;
import brooklyn.location.basic.SshMachineLocation;
import brooklyn.location.cloud.AbstractCloudMachineProvisioningLocation;
import brooklyn.util.MutableMap;
import brooklyn.util.config.ConfigBag;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class EnstratiusLocation extends AbstractCloudMachineProvisioningLocation implements EnstratiusLocationConfig {

    public static final Logger log = LoggerFactory.getLogger(EnstratiusLocation.class);

    private final Map<EnstratiusSshMachineLocation, String> serverIds = Maps.newLinkedHashMap();
    private final String jurisdiction;
    private final EnstratiusAPI enstratiusAPI;
    private final ConfigBag setup;

    public EnstratiusLocation(Map<?, ?> conf) {
        super(conf);
        setup = ConfigBag.newInstanceExtending(getConfigBag(), conf);
        jurisdiction = setup.get(EnstratiusLocationConfig.JURISDICTION);
        enstratiusAPI = new EnstratiusAPI(new EnstratiusApacheClient(getIdentity(), getCredential()));
    }
    
    /** returns the location ID used by the provider, if set, e.g. us-west-1 */
    public String getRegion() {
        return getConfig(CLOUD_REGION_ID);
    }
    
    public String getDatacenter() {
        return getConfig(CLOUD_DATACENTER);
    }
    
    public String getIdentity() {
        return getConfig(ACCESS_IDENTITY);
    }

    public String getCredential() {
        return getConfig(ACCESS_CREDENTIAL);
    }
    
    public String getUser() {
        return getConfig(USER);
    }
    
    public String getPassword() {
        return getConfig(PASSWORD);
    }

    /**
     * maximum time to wait for a job completion
     * 
     * @return number of seconds to wait (default is 15*60= 900 secs)
     */
    public int getMaxWaitInSec() {
        return Integer.parseInt(getConfig(MAX_WAIT_IN_SEC));
    }
    
    @Override
    public EnstratiusSshMachineLocation obtain(Map<?, ?> flags) throws NoMachinesAvailableException {
        ConfigBag setup = ConfigBag.newInstanceExtending(getConfigBag(), flags);

        String name = "brooklyn-enstratus-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
        String regionId, budgetId, dataCenterId, machineImageId, firewallId;

        String groupName = setup.get(GROUP_NAME);
        String providerId = setup.get(PROVIDER_ID); 
        try {
            regionId = getRegionId();
            budgetId = getBudgetId(regionId);
            dataCenterId = getDatacenterId(regionId);
            machineImageId = getMachineImageIdMatchingPredicate(regionId, providerId);
            firewallId = getFirewallIdMatchingPredicate(regionId, groupName);
            String description = String.format("launch server(%s)", name);
            Job job = checkNotNull(enstratiusAPI.getInfrastructureApi().launchServer(
                                   name, description, budgetId, machineImageId, dataCenterId, firewallId), "job");
            job = Jobs.waitForJob(job, getMaxWaitInSec(), enstratiusAPI.getAdminApi());
            if (!Jobs.isComplete(job)) {
                throw new RuntimeException(String.format("job id(%s), description(%s), failed with message(%s)", job
                        .getJobId(), job.getDescription(),
                        job.getMessage() == null ? "Try to increase maxWaitInSec in 'waitForJob'" : job.getMessage()));
            }
            String serverId = checkNotNull(job.getMessage(), "Server ID must not be null");
            Server server = enstratiusAPI.getInfrastructureApi().getServer(serverId, regionId);
            String vmPublicIpAddress = checkNotNull(server.getPublicIpAddress(), "public ip address must not be null");
            return registerEnstratiusSshMachineLocation(vmPublicIpAddress, serverId);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void release(SshMachineLocation machine) {
        try {
            String serverIdMsg = String.format("Server ID for machine(%s) must not be null", machine.getName());
            String serverId = checkNotNull(serverIds.get(machine), serverIdMsg);
            String reason = String.format("Brooklyn released %s", machine.getName());
            enstratiusAPI.getInfrastructureApi().deleteServer(serverId, reason);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    protected EnstratiusSshMachineLocation registerEnstratiusSshMachineLocation(String ipAddress, String serverId) {
        EnstratiusSshMachineLocation machine = createEnstratiusSshMachineLocation(ipAddress, serverId);
        machine.setParentLocation(this);
        serverIds.put(machine, serverId);
        return machine;
    }

    protected EnstratiusSshMachineLocation createEnstratiusSshMachineLocation(String ipAddress, String serverId) {
        if (LOG.isDebugEnabled())
            LOG.debug("creating EnstratiusSshMachineLocation representation for {}@{} for {} with {}", 
                    new Object[] { getUser(), ipAddress, setup.getDescription() });
        return new EnstratiusSshMachineLocation(MutableMap.builder().put("serverId", serverId)
                .put("address", ipAddress)
                .put("displayName", ipAddress)
                .put(USER, getUser())
                .put(PASSWORD, getPassword())
                .build());
    }

    private String getFirewallIdMatchingPredicate(String regionId, String groupName) throws IOException {
        return checkNotNull(tryFindFirewallMatchingPredicateOrNull(regionId, enstratiusAPI.getNetworkApi(), 
                new FirewallMatcher(groupName)), "firewall").getFirewallId();
    }

    private String getMachineImageIdMatchingPredicate(String regionId, String providerId) throws IOException {
        return checkNotNull(tryFindMachineImageMatchingPredicateOrNull(regionId, enstratiusAPI.getInfrastructureApi(),
                new MachineImageMatcher(providerId)), "machineImage").getMachineImageId();
    }

    private String getDatacenterId(String regionId) throws IOException {
        return checkNotNull(tryFindFirstDatacenterOrNull(regionId, enstratiusAPI.getGeographyApi()), "datacenter")
                .getDataCenterId();
    }

    private String getBudgetId(String regionId) throws IOException {
        return checkNotNull(tryFindFirstBillingCodeOrNull(regionId, enstratiusAPI.getAdminApi()), "billingCode")
                .getBillingCodeId();
    }

    private String getRegionId() throws IOException {
        return checkNotNull(tryFindFirstRegionOrNull(valueOf(jurisdiction), enstratiusAPI.getGeographyApi()), "region")
                .getRegionId();
    }

}
