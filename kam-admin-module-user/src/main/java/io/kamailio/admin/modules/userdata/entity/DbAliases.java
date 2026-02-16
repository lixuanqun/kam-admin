package io.kamailio.admin.modules.userdata.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dbaliases")
public class DbAliases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "alias_username", length = 64)
    private String aliasUsername;
    @Column(name = "alias_domain", length = 64)
    private String aliasDomain;
    @Column(length = 64)
    private String username;
    @Column(length = 64)
    private String domain;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getAliasUsername() { return aliasUsername; }
    public void setAliasUsername(String aliasUsername) { this.aliasUsername = aliasUsername; }
    public String getAliasDomain() { return aliasDomain; }
    public void setAliasDomain(String aliasDomain) { this.aliasDomain = aliasDomain; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
