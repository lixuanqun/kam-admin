package io.kamailio.admin.modules.drouting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dr_gateways")
public class DrGateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String gwid;
    @Column(nullable = false)
    private Integer type = 0;
    @Column(length = 128, nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer strip = 0;
    @Column(name = "pri_prefix", length = 16)
    private String priPrefix;
    @Column(length = 255)
    private String attrs;
    @Column(name = "probe_mode")
    private Integer probeMode = 0;
    @Column(nullable = false)
    private Integer state = 0;
    @Column(length = 128)
    private String socket;
    @Column(length = 128)
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getGwid() { return gwid; }
    public void setGwid(String gwid) { this.gwid = gwid; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getStrip() { return strip; }
    public void setStrip(Integer strip) { this.strip = strip; }
    public String getPriPrefix() { return priPrefix; }
    public void setPriPrefix(String priPrefix) { this.priPrefix = priPrefix; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
    public Integer getProbeMode() { return probeMode; }
    public void setProbeMode(Integer probeMode) { this.probeMode = probeMode; }
    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }
    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
