package io.kamailio.admin.modules.siptrace.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sip_trace")
public class SipTrace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "time_stamp")
    private Instant timeStamp;
    @Column(name = "time_us")
    private Integer timeUs = 0;
    @Column(length = 255)
    private String callid;
    @Column(name = "traced_user", length = 255)
    private String tracedUser;
    @Column(columnDefinition = "mediumtext")
    private String msg;
    @Column(length = 50)
    private String method;
    @Column(length = 255)
    private String status;
    @Column(name = "fromip", length = 64)
    private String fromIp;
    @Column(name = "toip", length = 64)
    private String toIp;
    @Column(name = "fromtag", length = 128)
    private String fromTag;
    @Column(name = "totag", length = 128)
    private String toTag;
    @Column(length = 4)
    private String direction;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Instant getTimeStamp() { return timeStamp; }
    public void setTimeStamp(Instant timeStamp) { this.timeStamp = timeStamp; }
    public Integer getTimeUs() { return timeUs; }
    public void setTimeUs(Integer timeUs) { this.timeUs = timeUs; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public String getTracedUser() { return tracedUser; }
    public void setTracedUser(String tracedUser) { this.tracedUser = tracedUser; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFromIp() { return fromIp; }
    public void setFromIp(String fromIp) { this.fromIp = fromIp; }
    public String getToIp() { return toIp; }
    public void setToIp(String toIp) { this.toIp = toIp; }
    public String getFromTag() { return fromTag; }
    public void setFromTag(String fromTag) { this.fromTag = fromTag; }
    public String getToTag() { return toTag; }
    public void setToTag(String toTag) { this.toTag = toTag; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
