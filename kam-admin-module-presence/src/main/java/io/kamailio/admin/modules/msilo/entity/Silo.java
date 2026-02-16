package io.kamailio.admin.modules.msilo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "silo")
public class Silo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "src_addr", length = 255)
    private String srcAddr;
    @Column(name = "dst_addr", length = 255)
    private String dstAddr;
    @Column(length = 64)
    private String username;
    @Column(length = 64)
    private String domain;
    @Column(name = "inc_time")
    private Integer incTime;
    @Column(name = "exp_time")
    private Integer expTime;
    @Column(name = "snd_time")
    private Integer sndTime;
    @Column(length = 255)
    private String callid;
    @Column(columnDefinition = "blob")
    private byte[] body;
    @Column(name = "extra_hdrs", columnDefinition = "text")
    private String extraHdrs;
    @Column(name = "content_type", length = 128)
    private String contentType;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSrcAddr() { return srcAddr; }
    public void setSrcAddr(String srcAddr) { this.srcAddr = srcAddr; }
    public String getDstAddr() { return dstAddr; }
    public void setDstAddr(String dstAddr) { this.dstAddr = dstAddr; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public Integer getIncTime() { return incTime; }
    public void setIncTime(Integer incTime) { this.incTime = incTime; }
    public Integer getExpTime() { return expTime; }
    public void setExpTime(Integer expTime) { this.expTime = expTime; }
    public Integer getSndTime() { return sndTime; }
    public void setSndTime(Integer sndTime) { this.sndTime = sndTime; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public byte[] getBody() { return body; }
    public void setBody(byte[] body) { this.body = body; }
    public String getExtraHdrs() { return extraHdrs; }
    public void setExtraHdrs(String extraHdrs) { this.extraHdrs = extraHdrs; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
}
