package io.kamailio.admin.modules.carrierroute.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "domain_name")
public class DomainName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String domain;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
