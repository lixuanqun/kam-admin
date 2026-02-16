package io.kamailio.admin.modules.presence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "presentity")
public class Presentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64)
    private String username;
    @Column(length = 64)
    private String domain;
    @Column(length = 64)
    private String event;
    @Column(length = 128)
    private String etag;
    @Column
    private Integer expires;
    @Column(name = "received_time")
    private Integer receivedTime;
    @Column(columnDefinition = "blob")
    private byte[] body;
    @Column(length = 255)
    private String sender;
    @Column
    private Integer priority = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
    public String getEtag() { return etag; }
    public void setEtag(String etag) { this.etag = etag; }
    public Integer getExpires() { return expires; }
    public void setExpires(Integer expires) { this.expires = expires; }
    public Integer getReceivedTime() { return receivedTime; }
    public void setReceivedTime(Integer receivedTime) { this.receivedTime = receivedTime; }
    public byte[] getBody() { return body; }
    public void setBody(byte[] body) { this.body = body; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
