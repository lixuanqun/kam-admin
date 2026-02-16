package io.kamailio.admin.modules.location.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64)
    private String ruid;
    @Column(length = 64, nullable = false)
    private String username;
    @Column(length = 64)
    private String domain;
    @Column(length = 512, nullable = false)
    private String contact;
    @Column(length = 128)
    private String received;
    @Column(length = 512)
    private String path;
    @Column(nullable = false)
    private java.time.Instant expires;
    @Column(columnDefinition = "float default -1")
    private Double q;
    @Column(length = 255)
    private String callid;
    @Column(columnDefinition = "int default 0")
    private Integer cseq;
    @Column(name = "last_modified")
    private java.time.Instant lastModified;
    @Column(columnDefinition = "int default 0")
    private Integer flags;
    @Column(columnDefinition = "int default 0")
    private Integer cflags;
    @Column(name = "user_agent", length = 255)
    private String userAgent;
    @Column(length = 64)
    private String socket;
    @Column(columnDefinition = "bigint default 4294967295")
    private Long methods;
    @Column(length = 255)
    private String instance;
    @Column(name = "reg_id")
    private Integer regId;
    @Column(name = "server_id")
    private Integer serverId;
    @Column(name = "connection_id")
    private Integer connectionId;
    @Column(columnDefinition = "int default 0")
    private Integer keepalive;
    @Column(columnDefinition = "int default 0")
    private Integer partition;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getRuid() { return ruid; }
    public void setRuid(String ruid) { this.ruid = ruid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getReceived() { return received; }
    public void setReceived(String received) { this.received = received; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public java.time.Instant getExpires() { return expires; }
    public void setExpires(java.time.Instant expires) { this.expires = expires; }
    public Double getQ() { return q; }
    public void setQ(Double q) { this.q = q; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public Integer getCseq() { return cseq; }
    public void setCseq(Integer cseq) { this.cseq = cseq; }
    public java.time.Instant getLastModified() { return lastModified; }
    public void setLastModified(java.time.Instant lastModified) { this.lastModified = lastModified; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getCflags() { return cflags; }
    public void setCflags(Integer cflags) { this.cflags = cflags; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }
    public Long getMethods() { return methods; }
    public void setMethods(Long methods) { this.methods = methods; }
    public String getInstance() { return instance; }
    public void setInstance(String instance) { this.instance = instance; }
    public Integer getRegId() { return regId; }
    public void setRegId(Integer regId) { this.regId = regId; }
    public Integer getServerId() { return serverId; }
    public void setServerId(Integer serverId) { this.serverId = serverId; }
    public Integer getConnectionId() { return connectionId; }
    public void setConnectionId(Integer connectionId) { this.connectionId = connectionId; }
    public Integer getKeepalive() { return keepalive; }
    public void setKeepalive(Integer keepalive) { this.keepalive = keepalive; }
    public Integer getPartition() { return partition; }
    public void setPartition(Integer partition) { this.partition = partition; }
}
