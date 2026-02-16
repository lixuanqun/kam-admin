package io.kamailio.admin.modules.userdata.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "speed_dial")
public class SpeedDial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64)
    private String username;
    @Column(length = 64)
    private String domain;
    @Column(name = "sd_username", length = 64)
    private String sdUsername;
    @Column(name = "sd_domain", length = 64)
    private String sdDomain;
    @Column(name = "new_uri", length = 255)
    private String newUri;
    @Column(length = 64)
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getSdUsername() { return sdUsername; }
    public void setSdUsername(String sdUsername) { this.sdUsername = sdUsername; }
    public String getSdDomain() { return sdDomain; }
    public void setSdDomain(String sdDomain) { this.sdDomain = sdDomain; }
    public String getNewUri() { return newUri; }
    public void setNewUri(String newUri) { this.newUri = newUri; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
