package io.kamailio.admin.modules.carrierroute.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "carrierfailureroute")
public class CarrierFailureRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer carrier;
    @Column(nullable = false)
    private Integer domain;
    @Column(name = "scan_prefix", length = 64, nullable = false)
    private String scanPrefix;
    @Column(name = "host_name", length = 255, nullable = false)
    private String hostName;
    @Column(name = "reply_code", length = 3, nullable = false)
    private String replyCode;
    @Column
    private Integer flags = 0;
    @Column
    private Integer mask = 0;
    @Column(name = "next_domain", length = 64, nullable = false)
    private String nextDomain;
    @Column(length = 255)
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCarrier() { return carrier; }
    public void setCarrier(Integer carrier) { this.carrier = carrier; }
    public Integer getDomain() { return domain; }
    public void setDomain(Integer domain) { this.domain = domain; }
    public String getScanPrefix() { return scanPrefix; }
    public void setScanPrefix(String scanPrefix) { this.scanPrefix = scanPrefix; }
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
    public String getReplyCode() { return replyCode; }
    public void setReplyCode(String replyCode) { this.replyCode = replyCode; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getMask() { return mask; }
    public void setMask(Integer mask) { this.mask = mask; }
    public String getNextDomain() { return nextDomain; }
    public void setNextDomain(String nextDomain) { this.nextDomain = nextDomain; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
