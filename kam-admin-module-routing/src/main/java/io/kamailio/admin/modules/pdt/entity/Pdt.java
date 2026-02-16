package io.kamailio.admin.modules.pdt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pdt")
public class Pdt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 255, nullable = false)
    private String sdomain;
    @Column(length = 32, nullable = false)
    private String prefix;
    @Column(length = 255, nullable = false)
    private String domain;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSdomain() { return sdomain; }
    public void setSdomain(String sdomain) { this.sdomain = sdomain; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
}
