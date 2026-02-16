package io.kamailio.admin.modules.dialog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dialog")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "hash_entry")
    private Integer hashEntry;
    @Column(name = "hash_id")
    private Integer hashId;
    @Column(length = 255)
    private String callid;
    @Column(name = "from_uri", length = 255)
    private String fromUri;
    @Column(name = "from_tag", length = 128)
    private String fromTag;
    @Column(name = "to_uri", length = 255)
    private String toUri;
    @Column(name = "to_tag", length = 128)
    private String toTag;
    @Column(name = "caller_cseq", length = 20)
    private String callerCseq;
    @Column(name = "callee_cseq", length = 20)
    private String calleeCseq;
    @Column(name = "caller_contact", length = 255)
    private String callerContact;
    @Column(name = "callee_contact", length = 255)
    private String calleeContact;
    @Column(name = "caller_sock", length = 64)
    private String callerSock;
    @Column(name = "callee_sock", length = 64)
    private String calleeSock;
    @Column
    private Integer state;
    @Column(name = "start_time")
    private Integer startTime;
    @Column(nullable = false)
    private Integer timeout = 0;
    @Column(nullable = false)
    private Integer sflags = 0;
    @Column(name = "toroute_name", length = 32)
    private String torouteName;
    @Column(name = "req_uri", length = 255)
    private String reqUri;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getHashEntry() { return hashEntry; }
    public void setHashEntry(Integer hashEntry) { this.hashEntry = hashEntry; }
    public Integer getHashId() { return hashId; }
    public void setHashId(Integer hashId) { this.hashId = hashId; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public String getFromUri() { return fromUri; }
    public void setFromUri(String fromUri) { this.fromUri = fromUri; }
    public String getFromTag() { return fromTag; }
    public void setFromTag(String fromTag) { this.fromTag = fromTag; }
    public String getToUri() { return toUri; }
    public void setToUri(String toUri) { this.toUri = toUri; }
    public String getToTag() { return toTag; }
    public void setToTag(String toTag) { this.toTag = toTag; }
    public String getCallerCseq() { return callerCseq; }
    public void setCallerCseq(String callerCseq) { this.callerCseq = callerCseq; }
    public String getCalleeCseq() { return calleeCseq; }
    public void setCalleeCseq(String calleeCseq) { this.calleeCseq = calleeCseq; }
    public String getCallerContact() { return callerContact; }
    public void setCallerContact(String callerContact) { this.callerContact = callerContact; }
    public String getCalleeContact() { return calleeContact; }
    public void setCalleeContact(String calleeContact) { this.calleeContact = calleeContact; }
    public String getCallerSock() { return callerSock; }
    public void setCallerSock(String callerSock) { this.callerSock = callerSock; }
    public String getCalleeSock() { return calleeSock; }
    public void setCalleeSock(String calleeSock) { this.calleeSock = calleeSock; }
    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }
    public Integer getStartTime() { return startTime; }
    public void setStartTime(Integer startTime) { this.startTime = startTime; }
    public Integer getTimeout() { return timeout; }
    public void setTimeout(Integer timeout) { this.timeout = timeout; }
    public Integer getSflags() { return sflags; }
    public void setSflags(Integer sflags) { this.sflags = sflags; }
    public String getTorouteName() { return torouteName; }
    public void setTorouteName(String torouteName) { this.torouteName = torouteName; }
    public String getReqUri() { return reqUri; }
    public void setReqUri(String reqUri) { this.reqUri = reqUri; }
}
