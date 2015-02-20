package restwars.rest.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.*;

import java.io.IOException;

@AtmosphereHandlerService
public class WebSocketHandler implements AtmosphereHandler {
    /**
     * Name of the round broadcaster.
     */
    private static final String ROUND_BROADCASTER_NAME = "round";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void onRequest(AtmosphereResource atmosphereResource) throws IOException {
        BroadcasterFactory broadcasterFactory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
        Broadcaster broadcaster = getBroadcaster(broadcasterFactory);
        atmosphereResource.setBroadcaster(broadcaster);
        atmosphereResource.suspend();
    }

    private boolean isBroadcast(AtmosphereResourceEvent event) {
        return event.getMessage() != null;
    }

    @Override
    public void onStateChange(AtmosphereResourceEvent event) throws IOException {
        AtmosphereResource resource = event.getResource();

        if (isBroadcast(event)) {
            resource.write(event.getMessage().toString());

            switch (resource.transport()) {
                case WEBSOCKET:
                case STREAMING:
                    resource.getResponse().flushBuffer();
                    break;
                default:
                    resource.resume();
                    break;
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }

    private static class Round {
        private final long round;

        public Round(long round) {
            this.round = round;
        }

        public long getRound() {
            return round;
        }

    }

    public static void broadcastRound(BroadcasterFactory broadcasterFactory, long round) {
        Round message = new Round(round);
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to convert round object to json", e);
        }

        getBroadcaster(broadcasterFactory).broadcast(json);
    }

    private static Broadcaster getBroadcaster(BroadcasterFactory broadcasterFactory) {
        return broadcasterFactory.lookup(ROUND_BROADCASTER_NAME, true);
    }
}
