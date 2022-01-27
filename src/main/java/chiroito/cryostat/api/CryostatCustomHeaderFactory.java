package chiroito.cryostat.api;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class CryostatCustomHeaderFactory implements ClientHeadersFactory {
    @ConfigProperty(name = "cryostat.token")
    String token;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", "Bearer " + token);
        return result;
    }
}
