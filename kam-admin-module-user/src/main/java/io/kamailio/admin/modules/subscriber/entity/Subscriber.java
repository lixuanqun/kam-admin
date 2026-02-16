package io.kamailio.admin.modules.subscriber.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "subscriber")
@Schema(description = "SIP 用户/订阅者")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String username;
    @Column(length = 64, nullable = false)
    private String domain;
    @Column(length = 64, nullable = false)
    private String password;
    @Column(length = 128, nullable = false)
    private String ha1;
    @Column(length = 128, nullable = false)
    private String ha1b;
    @Column(name = "email_address", length = 64)
    private String emailAddress;
    @Column(name = "rpid", length = 64)
    private String rpid;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getHa1() { return ha1; }
    public void setHa1(String ha1) { this.ha1 = ha1; }
    public String getHa1b() { return ha1b; }
    public void setHa1b(String ha1b) { this.ha1b = ha1b; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getRpid() { return rpid; }
    public void setRpid(String rpid) { this.rpid = rpid; }
}
