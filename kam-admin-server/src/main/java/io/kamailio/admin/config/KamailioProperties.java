package io.kamailio.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kamailio")
public class KamailioProperties {

    private Rpc rpc = new Rpc();
    private String kamctlPath = "/usr/sbin/kamctl";
    private String defaultDomain = "localhost";

    public static class Rpc {
        private String host = "localhost";
        private int port = 5060;
        private String path = "/RPC";
        private int timeout = 10000;

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
    }

    public Rpc getRpc() { return rpc; }
    public void setRpc(Rpc rpc) { this.rpc = rpc; }
    public String getKamctlPath() { return kamctlPath; }
    public void setKamctlPath(String kamctlPath) { this.kamctlPath = kamctlPath; }
    public String getDefaultDomain() { return defaultDomain; }
    public void setDefaultDomain(String defaultDomain) { this.defaultDomain = defaultDomain; }
}
