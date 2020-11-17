package demo.kafka.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import demo.kafka.consumer.model.TramDataAggregated;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

@ServerEndpoint("/kafka")
@ApplicationScoped
public class KafkaSocket {
	private static final Logger LOG = Logger.getLogger(KafkaSocket.class);

	private Jsonb jsonb;

	@Inject
	@Channel("tram-stats")
	Flowable<TramDataAggregated> stream;

	private Map<Integer, TramDataAggregated> tramInfo = new HashMap<Integer, TramDataAggregated>();
	
	private List<Session> sessions = new CopyOnWriteArrayList<>();
	private Disposable subscription;
	
	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}

	@PostConstruct
	public void subscribe() {
		 subscription = stream.subscribe(tramAggregation -> sessions.forEach(session -> write(session, tramAggregation)));
	}

	@PreDestroy
	public void cleanup() throws Exception {
		subscription.dispose();
		jsonb.close();
	}
	
	private void write(Session session, TramDataAggregated obj) {
		jsonb = JsonbBuilder.create();
		tramInfo.put(obj.getTramId(), obj);
        session.getAsyncRemote().sendText(jsonb.toJson(tramInfo.values()), result -> {
            if (result.getException() != null) {
                LOG.error("Unable to write message to web socket", result.getException());
            }
        });
    }
}