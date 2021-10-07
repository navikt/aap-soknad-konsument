package no.nav.aap.api.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

import no.nav.aap.api.config.AbstractRestConfig;
import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;


public abstract class AbstractWebClientConnection implements RetryAware, PingEndpointAware {
    protected final WebClient webClient;
    protected final AbstractRestConfig cfg;

    public AbstractWebClientConnection(WebClient webClient, AbstractRestConfig cfg) {
        this.webClient = webClient;
        this.cfg = cfg;
    }

    @Override
    public String ping() {
        return webClient
                .get()
                .uri(pingEndpoint())
                .accept(APPLICATION_JSON, TEXT_PLAIN)
                .retrieve()
                .toEntity(String.class)
                .block()
                .getBody();
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingEndpoint();
    }
}