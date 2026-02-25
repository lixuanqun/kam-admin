package io.kamailio.admin.rpc;

import io.kamailio.admin.common.service.KamailioRpcService;
import io.kamailio.admin.config.KamailioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KamailioRpcServiceImpl implements KamailioRpcService {

    private static final Logger log = LoggerFactory.getLogger(KamailioRpcServiceImpl.class);

    private final WebClient webClient;
    private final AtomicInteger requestId = new AtomicInteger(0);

    @Autowired
    public KamailioRpcServiceImpl(KamailioProperties properties) {
        var rpc = properties.getRpc();
        String baseUrl = "http://" + rpc.getHost() + ":" + rpc.getPort() + rpc.getPath();
        int timeoutMs = Math.max(1000, rpc.getTimeout());
        this.webClient = buildWebClient(baseUrl, timeoutMs);
    }

    /**
     * 用于多租户：按租户 RPC 地址与超时创建实例（由调用方缓存，见 TenantAwareKamailioRpcService）。
     */
    public KamailioRpcServiceImpl(String baseUrl, int timeoutMs) {
        String url = baseUrl != null && !baseUrl.isBlank() ? baseUrl.trim() : "http://localhost:5060/RPC";
        if (!url.startsWith("http")) url = "http://" + url;
        int effectiveTimeout = timeoutMs > 0 ? timeoutMs : 10000;
        this.webClient = buildWebClient(url, effectiveTimeout);
    }

    private static WebClient buildWebClient(String baseUrl, int timeoutMs) {
        Duration timeout = Duration.ofMillis(timeoutMs);
        HttpClient httpClient = HttpClient.create().responseTimeout(timeout);
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(connector)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Mono<T> call(String method, Object... params) {
        int id = requestId.incrementAndGet();
        Map<String, Object> payload = Map.of(
                "jsonrpc", "2.0",
                "method", method,
                "params", params != null ? params : new Object[0],
                "id", id
        );

        return webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(resp -> {
                    if (resp.containsKey("error")) {
                        var err = (Map<?, ?>) resp.get("error");
                        String msg = String.valueOf(err.get("message"));
                        log.error("RPC call failed: {} - {}", method, msg);
                        return Mono.error(new RuntimeException("RPC Error: " + msg));
                    }
                    return Mono.just((T) resp.get("result"));
                })
                .onErrorResume(e -> {
                    log.error("RPC call failed: {}", method, e);
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Boolean> ping() {
        return call("core.version")
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
