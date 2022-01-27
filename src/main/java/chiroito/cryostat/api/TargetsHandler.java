package chiroito.cryostat.api;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/api/v1/targets")
@RegisterRestClient(configKey = "cryostat")
@RegisterClientHeaders(CryostatCustomHeaderFactory.class)
public interface TargetsHandler {

    @GET
    @Retry
    List<VMInfo> getVMs();
}
