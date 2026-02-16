package io.kamailio.admin.modules.userdata.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "grp")
public class Grp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64)
    private String username;
    @Column(length = 64)
    private String domain;
    @Column(name = "grp", length = 64)
    private String grp;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getGrp() { return grp; }
    public void setGrp(String grp) { this.grp = grp; }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}
