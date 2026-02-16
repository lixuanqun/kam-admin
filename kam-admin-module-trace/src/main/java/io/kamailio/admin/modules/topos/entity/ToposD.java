package io.kamailio.admin.modules.topos.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "topos_d")
public class ToposD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Instant rectime;
    @Column(name = "x_context", length = 64)
    private String xContext;
    @Column(name = "s_method", length = 64)
    private String sMethod;
    @Column(name = "s_cseq", length = 64)
    private String sCseq;
    @Column(name = "a_callid", length = 255)
    private String aCallid;
    @Column(name = "a_uuid", length = 255)
    private String aUuid;
    @Column(name = "b_uuid", length = 255)
    private String bUuid;
    @Column(name = "a_contact", length = 512)
    private String aContact;
    @Column(name = "b_contact", length = 512)
    private String bContact;
    @Column(name = "as_contact", length = 512)
    private String asContact;
    @Column(name = "bs_contact", length = 512)
    private String bsContact;
    @Column(name = "a_tag", length = 255)
    private String aTag;
    @Column(name = "b_tag", length = 255)
    private String bTag;
    @Column(name = "a_rr", columnDefinition = "mediumtext")
    private String aRr;
    @Column(name = "b_rr", columnDefinition = "mediumtext")
    private String bRr;
    @Column(name = "s_rr", columnDefinition = "mediumtext")
    private String sRr;
    @Column(name = "iflags", length = 255)
    private String iflags;
    @Column(name = "a_socket", length = 128)
    private String aSocket;
    @Column(name = "b_socket", length = 128)
    private String bSocket;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Instant getRectime() { return rectime; }
    public void setRectime(Instant rectime) { this.rectime = rectime; }
    public String getXContext() { return xContext; }
    public void setXContext(String xContext) { this.xContext = xContext; }
    public String getsMethod() { return sMethod; }
    public void setsMethod(String sMethod) { this.sMethod = sMethod; }
    public String getsCseq() { return sCseq; }
    public void setsCseq(String sCseq) { this.sCseq = sCseq; }
    public String getaCallid() { return aCallid; }
    public void setaCallid(String aCallid) { this.aCallid = aCallid; }
    public String getaUuid() { return aUuid; }
    public void setaUuid(String aUuid) { this.aUuid = aUuid; }
    public String getbUuid() { return bUuid; }
    public void setbUuid(String bUuid) { this.bUuid = bUuid; }
    public String getaContact() { return aContact; }
    public void setaContact(String aContact) { this.aContact = aContact; }
    public String getbContact() { return bContact; }
    public void setbContact(String bContact) { this.bContact = bContact; }
    public String getAsContact() { return asContact; }
    public void setAsContact(String asContact) { this.asContact = asContact; }
    public String getBsContact() { return bsContact; }
    public void setBsContact(String bsContact) { this.bsContact = bsContact; }
    public String getaTag() { return aTag; }
    public void setaTag(String aTag) { this.aTag = aTag; }
    public String getbTag() { return bTag; }
    public void setbTag(String bTag) { this.bTag = bTag; }
    public String getaRr() { return aRr; }
    public void setaRr(String aRr) { this.aRr = aRr; }
    public String getbRr() { return bRr; }
    public void setbRr(String bRr) { this.bRr = bRr; }
    public String getsRr() { return sRr; }
    public void setsRr(String sRr) { this.sRr = sRr; }
    public String getIflags() { return iflags; }
    public void setIflags(String iflags) { this.iflags = iflags; }
    public String getaSocket() { return aSocket; }
    public void setaSocket(String aSocket) { this.aSocket = aSocket; }
    public String getbSocket() { return bSocket; }
    public void setbSocket(String bSocket) { this.bSocket = bSocket; }
}
