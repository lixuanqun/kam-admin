package io.kamailio.admin.modules.uac.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "uacreg")
public class UacReg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "l_uuid", length = 64)
    private String lUuid;
    @Column(name = "l_username", length = 64)
    private String lUsername;
    @Column(name = "l_domain", length = 64)
    private String lDomain;
    @Column(name = "r_username", length = 64)
    private String rUsername;
    @Column(name = "r_domain", length = 64)
    private String rDomain;
    @Column(length = 64)
    private String realm;
    @Column(name = "auth_username", length = 64)
    private String authUsername;
    @Column(name = "auth_password", length = 64)
    private String authPassword;
    @Column(name = "auth_proxy", length = 255)
    private String authProxy;
    @Column
    private Integer expires = 0;
    @Column
    private Integer flags = 0;
    @Column(name = "reg_delay")
    private Integer regDelay = 0;
    @Column(name = "contact_addr", length = 255)
    private String contactAddr;
    @Column(length = 128)
    private String socket;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLUuid() { return lUuid; }
    public void setLUuid(String lUuid) { this.lUuid = lUuid; }
    public String getLUsername() { return lUsername; }
    public void setLUsername(String lUsername) { this.lUsername = lUsername; }
    public String getLDomain() { return lDomain; }
    public void setLDomain(String lDomain) { this.lDomain = lDomain; }
    public String getRUsername() { return rUsername; }
    public void setRUsername(String rUsername) { this.rUsername = rUsername; }
    public String getRDomain() { return rDomain; }
    public void setRDomain(String rDomain) { this.rDomain = rDomain; }
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getAuthUsername() { return authUsername; }
    public void setAuthUsername(String authUsername) { this.authUsername = authUsername; }
    public String getAuthPassword() { return authPassword; }
    public void setAuthPassword(String authPassword) { this.authPassword = authPassword; }
    public String getAuthProxy() { return authProxy; }
    public void setAuthProxy(String authProxy) { this.authProxy = authProxy; }
    public Integer getExpires() { return expires; }
    public void setExpires(Integer expires) { this.expires = expires; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getRegDelay() { return regDelay; }
    public void setRegDelay(Integer regDelay) { this.regDelay = regDelay; }
    public String getContactAddr() { return contactAddr; }
    public void setContactAddr(String contactAddr) { this.contactAddr = contactAddr; }
    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }
}
