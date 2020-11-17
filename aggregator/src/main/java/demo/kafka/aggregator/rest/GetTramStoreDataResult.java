package demo.kafka.aggregator.rest;

import java.util.Optional;
import java.util.OptionalInt;

import demo.kafka.aggregator.model.TramStoreData;

public class GetTramStoreDataResult {

	private static GetTramStoreDataResult NOT_FOUND =
            new GetTramStoreDataResult(null, null, null);

    private final TramStoreData result;
    private final String host;
    private final Integer port;

    private GetTramStoreDataResult(TramStoreData result, String host,
            Integer port) {
        this.result = result;
        this.host = host;
        this.port = port;
    }

    public static GetTramStoreDataResult found(TramStoreData data) {
        return new GetTramStoreDataResult(data, null, null);
    }

    public static GetTramStoreDataResult foundRemotely(String host, int port) {
        return new GetTramStoreDataResult(null, host, port);
    }

    public static GetTramStoreDataResult notFound() {
        return NOT_FOUND;
    }

    public Optional<TramStoreData> getResult() {
        return Optional.ofNullable(result);
    }

    public Optional<String> getHost() {
        return Optional.ofNullable(host);
    }

    public OptionalInt getPort() {
        return port != null ? OptionalInt.of(port) : OptionalInt.empty();
    }
}