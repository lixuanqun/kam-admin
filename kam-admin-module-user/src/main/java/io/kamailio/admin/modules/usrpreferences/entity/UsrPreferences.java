package io.kamailio.admin.modules.usrpreferences.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usr_preferences")
public class UsrPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64)
    private String uuid = "";
    @Column(length = 255)
    private String username = "0";
    @Column(length = 64)
    private String domain = "";
    @Column(length = 64)
    private String attribute = "";
    @Column
    private Integer type = 0;
    @Column(length = 255)
    private String value = "";
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getAttribute() { return attribute; }
    public void setAttribute(String attribute) { this.attribute = attribute; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}
