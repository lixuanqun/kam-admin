package io.kamailio.admin.modules.permissions.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "grp", nullable = false)
    private Integer grp = 1;
    @Column(name = "ip_addr", length = 50, nullable = false)
    private String ipAddr;
    @Column(nullable = false)
    private Integer mask = 32;
    @Column(nullable = false)
    private Integer port = 0;
    @Column(length = 64)
    private String tag;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getGrp() { return grp; }
    public void setGrp(Integer grp) { this.grp = grp; }
    public String getIpAddr() { return ipAddr; }
    public void setIpAddr(String ipAddr) { this.ipAddr = ipAddr; }
    public Integer getMask() { return mask; }
    public void setMask(Integer mask) { this.mask = mask; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
