package io.kamailio.admin.modules.drouting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dr_groups")
public class DrGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String username;
    @Column(length = 128)
    private String domain = "";
    @Column(nullable = false)
    private Integer groupid = 0;
    @Column(length = 128)
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public Integer getGroupid() { return groupid; }
    public void setGroupid(Integer groupid) { this.groupid = groupid; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
