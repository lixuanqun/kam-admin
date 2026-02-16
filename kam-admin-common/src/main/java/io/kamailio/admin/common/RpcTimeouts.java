package io.kamailio.admin.common;

import java.time.Duration;

/**
 * RPC 调用在 Servlet 线程中 block 时使用的超时时间，避免无限阻塞。
 * 应与 Kamailio RPC 的 responseTimeout 相当或略大（如 10s RPC → 15s block）。
 */
public final class RpcTimeouts {

    /** 在 Service 层对 Mono.block(...) 使用的默认超时，建议略大于 kamailio.rpc.timeout */
    public static final Duration DEFAULT_BLOCK = Duration.ofSeconds(15);

    private RpcTimeouts() {}
}
