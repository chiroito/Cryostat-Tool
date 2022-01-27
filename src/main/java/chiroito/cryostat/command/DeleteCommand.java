package chiroito.cryostat.command;

import chiroito.cryostat.api.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import picocli.CommandLine;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@CommandLine.Command(name = "delete")
public class DeleteCommand implements Runnable {

    @Inject
    @RestClient
    TargetRecordingHandler targetRecordingHandler;

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

        List<VMInfo> skippedVms = new ArrayList<>(vMs.size());

        for (VMInfo vm : vMs) {
            // Get the list of JFRs from each VM and download each JFR file.

            final String podName = vm.alias;
            if (!podName.matches(podNameFilter)) {
                skippedVms.add(vm);
                continue;
            }

            final String targetId = vm.annotations.cryostat.HOST + ":" + vm.annotations.cryostat.PORT;

            try {
                // Get the list of JFRs from VM
                Response res = targetRecordingsHandler.getList(targetId);
                if (res.getStatus() != 200) {
                    System.err.println(podName + " : " + "Could not get Jfr list");
                    continue;
                }
                Set<JfrInfo> jfrSet = res.readEntity(setJfrInfoType);

                for (JfrInfo jfrInfo : jfrSet) {

                    // Download the JFR file
                    final String recordingName = jfrInfo.name;
                    Response jfrFileResponse = targetRecordingHandler.delete(targetId, recordingName);
                    if (jfrFileResponse.getStatus() != 200) {
                        continue;
                    }

                    System.out.println("Success to delete the recording " + recordingName + " @ " + podName);

                }
            } catch (Exception e) {
                System.err.println("Fail to load JFR list for " + podName);
                e.printStackTrace();
            }
        }

        for (VMInfo vm : skippedVms) {
            System.out.println(vm.alias + " is skipped");
        }
    }
}
