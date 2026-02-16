package io.kamailio.admin.modules.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "domain")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, unique = true, nullable = false)
    private String domain;
    @Column(name = "did", length = 64)
    private String did;
    @Column(name = "last_modified")
    private java.time.Instant lastModified;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getDid() { return did; }
    public void setDid(String did) { this.did = did; }
    public java.time.Instant getLastModified() { return lastModified; }
    public void setLastModified(java.time.Instant lastModified) { this.lastModified = lastModified; }
}
