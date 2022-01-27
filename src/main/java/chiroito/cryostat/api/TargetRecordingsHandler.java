package chiroito.cryostat.api;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/v1/targets/{targetId}/recordings")
@RegisterRestClient(configKey = "cryostat")
@RegisterClientHeaders(CryostatCustomHeaderFactory.class)
public interface TargetRecordingsHandler {

    @GET
    @Retry
    Response getList(@PathParam("targetId") String targetId);
}
