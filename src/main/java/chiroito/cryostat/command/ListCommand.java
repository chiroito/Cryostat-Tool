package chiroito.cryostat.command;

import chiroito.cryostat.api.JfrInfo;
import chiroito.cryostat.api.TargetRecordingsHandler;
import chiroito.cryostat.api.TargetsHandler;
import chiroito.cryostat.api.VMInfo;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import picocli.CommandLine;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@CommandLine.Command(name = "list")
public class ListCommand implements Runnable {

    @Inject
    @RestClient
    TargetRecordingsHandler targetRecordingsHandler;

    @Inject
    @RestClient
    TargetsHandler targetsHandler;

    @CommandLine.Option(names = {"-f", "--pod-name-filter"}, defaultValue = ".+", description = "")
    String podNameFilter;

    private static final GenericType<Set<JfrInfo>> setJfrInfoType = new GenericType<>() {
    };

    @Override
    public void run() {
        // Find the target VM to get JFR.
        List<VMInfo> vMs = targetsHandler.getVMs();
        Collections.sort(vMs, new VmNameComparator());

        if (vMs.size() == 0) {
            System.err.println("No VMs were found");
            return;
        }

        for (VMInfo vm : vMs) {

            final String targetId = vm.annotations.cryostat.HOST + ":" + vm.annotations.cryostat.PORT;
            final String podName = vm.alias;

            if (!podName.matches(podNameFilter)) {
                continue;
            }

            try {
                // Get the list of JFRs from VM
                Response res = targetRecordingsHandler.getList(targetId);
                if (res.getStatus() != 200) {
                    System.err.println("Could not get Jfr list from " + podName);
                    continue;
                }
                Set<JfrInfo> jfrSet = res.readEntity(setJfrInfoType);

                for (JfrInfo jfrInfo : jfrSet) {
                    System.out.println(podName + " has " + jfrInfo.name + " which is " + jfrInfo.state);
                }
            } catch (Exception e) {
                System.err.println("Fail to load JFR list for " + podName);
                e.printStackTrace();
            }
        }
    }
}