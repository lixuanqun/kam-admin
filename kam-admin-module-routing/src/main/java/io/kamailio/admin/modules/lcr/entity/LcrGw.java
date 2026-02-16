package io.kamailio.admin.modules.lcr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lcr_gw")
public class LcrGw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "lcr_id")
    private Integer lcrId = 1;
    @Column(name = "gw_name", length = 128)
    private String gwName;
    @Column(name = "ip_addr", length = 50)
    private String ipAddr;
    @Column(length = 64)
    private String hostname;
    @Column
    private Integer port = 0;
    @Column(name = "uri_scheme")
    private Integer uriScheme = 1;
    @Column
    private Integer transport = 0;
    @Column(length = 64)
    private String params;
    @Column
    private Integer strip = 0;
    @Column(length = 16)
    private String prefix;
    @Column(length = 64)
    private String tag;
    @Column
    private Integer flags = 0;
    @Column
    private Integer defunct = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLcrId() { return lcrId; }
    public void setLcrId(Integer lcrId) { this.lcrId = lcrId; }
    public String getGwName() { return gwName; }
    public void setGwName(String gwName) { this.gwName = gwName; }
    public String getIpAddr() { return ipAddr; }
    public void setIpAddr(String ipAddr) { this.ipAddr = ipAddr; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public Integer getUriScheme() { return uriScheme; }
    public void setUriScheme(Integer uriScheme) { this.uriScheme = uriScheme; }
    public Integer getTransport() { return transport; }
    public void setTransport(Integer transport) { this.transport = transport; }
    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }
    public Integer getStrip() { return strip; }
    public void setStrip(Integer strip) { this.strip = strip; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getDefunct() { return defunct; }
    public void setDefunct(Integer defunct) { this.defunct = defunct; }
}
