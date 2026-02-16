package io.kamailio.admin.modules.presence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "active_watchers")
public class ActiveWatchers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "presentity_uri", length = 255)
    private String presentityUri;
    @Column(name = "watcher_username", length = 64)
    private String watcherUsername;
    @Column(name = "watcher_domain", length = 64)
    private String watcherDomain;
    @Column(name = "to_user", length = 64)
    private String toUser;
    @Column(name = "to_domain", length = 64)
    private String toDomain;
    @Column(length = 64)
    private String event;
    @Column(name = "event_id", length = 64)
    private String eventId;
    @Column(name = "to_tag", length = 128)
    private String toTag;
    @Column(name = "from_tag", length = 128)
    private String fromTag;
    @Column(length = 255)
    private String callid;
    @Column(name = "local_cseq")
    private Integer localCseq;
    @Column(name = "remote_cseq")
    private Integer remoteCseq;
    @Column(length = 255)
    private String contact;
    @Column(name = "record_route", columnDefinition = "text")
    private String recordRoute;
    @Column
    private Integer expires;
    @Column
    private Integer status = 0;
    @Column(length = 64)
    private String reason;
    @Column
    private Integer version = 0;
    @Column(name = "socket_info", length = 64)
    private String socketInfo;
    @Column(name = "local_contact", length = 255)
    private String localContact;
    @Column(name = "from_user", length = 64)
    private String fromUser;
    @Column(name = "from_domain", length = 64)
    private String fromDomain;
    @Column
    private Integer updated;
    @Column(name = "updated_winfo")
    private Integer updatedWinfo;
    @Column
    private Integer flags = 0;
    @Column(name = "user_agent", length = 255)
    private String userAgent;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getPresentityUri() { return presentityUri; }
    public void setPresentityUri(String presentityUri) { this.presentityUri = presentityUri; }
    public String getWatcherUsername() { return watcherUsername; }
    public void setWatcherUsername(String watcherUsername) { this.watcherUsername = watcherUsername; }
    public String getWatcherDomain() { return watcherDomain; }
    public void setWatcherDomain(String watcherDomain) { this.watcherDomain = watcherDomain; }
    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }
    public String getToDomain() { return toDomain; }
    public void setToDomain(String toDomain) { this.toDomain = toDomain; }
    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getToTag() { return toTag; }
    public void setToTag(String toTag) { this.toTag = toTag; }
    public String getFromTag() { return fromTag; }
    public void setFromTag(String fromTag) { this.fromTag = fromTag; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public Integer getLocalCseq() { return localCseq; }
    public void setLocalCseq(Integer localCseq) { this.localCseq = localCseq; }
    public Integer getRemoteCseq() { return remoteCseq; }
    public void setRemoteCseq(Integer remoteCseq) { this.remoteCseq = remoteCseq; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getRecordRoute() { return recordRoute; }
    public void setRecordRoute(String recordRoute) { this.recordRoute = recordRoute; }
    public Integer getExpires() { return expires; }
    public void setExpires(Integer expires) { this.expires = expires; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getSocketInfo() { return socketInfo; }
    public void setSocketInfo(String socketInfo) { this.socketInfo = socketInfo; }
    public String getLocalContact() { return localContact; }
    public void setLocalContact(String localContact) { this.localContact = localContact; }
    public String getFromUser() { return fromUser; }
    public void setFromUser(String fromUser) { this.fromUser = fromUser; }
    public String getFromDomain() { return fromDomain; }
    public void setFromDomain(String fromDomain) { this.fromDomain = fromDomain; }
    public Integer getUpdated() { return updated; }
    public void setUpdated(Integer updated) { this.updated = updated; }
    public Integer getUpdatedWinfo() { return updatedWinfo; }
    public void setUpdatedWinfo(Integer updatedWinfo) { this.updatedWinfo = updatedWinfo; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}
