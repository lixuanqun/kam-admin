package io.kamailio.admin.modules.acc.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "acc")
public class Acc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 16, nullable = false)
    private String method;
    @Column(name = "from_tag", length = 64, nullable = false)
    private String fromTag;
    @Column(name = "to_tag", length = 64, nullable = false)
    private String toTag;
    @Column(name = "callid", length = 255, nullable = false)
    private String callid;
    @Column(name = "sip_code", length = 3, nullable = false)
    private String sipCode;
    @Column(name = "sip_reason", length = 128, nullable = false)
    private String sipReason;
    @Column(nullable = false)
    private java.time.Instant time;
    @Column(name = "src_user", length = 64)
    private String srcUser;
    @Column(name = "src_domain", length = 128)
    private String srcDomain;
    @Column(name = "src_ip", length = 64)
    private String srcIp;
    @Column(name = "dst_user", length = 64)
    private String dstUser;
    @Column(name = "dst_domain", length = 128)
    private String dstDomain;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getFromTag() { return fromTag; }
    public void setFromTag(String fromTag) { this.fromTag = fromTag; }
    public String getToTag() { return toTag; }
    public void setToTag(String toTag) { this.toTag = toTag; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public String getSipCode() { return sipCode; }
    public void setSipCode(String sipCode) { this.sipCode = sipCode; }
    public String getSipReason() { return sipReason; }
    public void setSipReason(String sipReason) { this.sipReason = sipReason; }
    public java.time.Instant getTime() { return time; }
    public void setTime(java.time.Instant time) { this.time = time; }
    public String getSrcUser() { return srcUser; }
    public void setSrcUser(String srcUser) { this.srcUser = srcUser; }
    public String getSrcDomain() { return srcDomain; }
    public void setSrcDomain(String srcDomain) { this.srcDomain = srcDomain; }
    public String getSrcIp() { return srcIp; }
    public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
    public String getDstUser() { return dstUser; }
    public void setDstUser(String dstUser) { this.dstUser = dstUser; }
    public String getDstDomain() { return dstDomain; }
    public void setDstDomain(String dstDomain) { this.dstDomain = dstDomain; }
}
