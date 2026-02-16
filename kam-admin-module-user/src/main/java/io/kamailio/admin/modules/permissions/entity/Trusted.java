package io.kamailio.admin.modules.permissions.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trusted")
public class Trusted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "src_ip", length = 50, nullable = false)
    private String srcIp;
    @Column(length = 4)
    private String proto;
    @Column(name = "from_pattern", length = 64)
    private String fromPattern;
    @Column(name = "ruri_pattern", length = 64)
    private String ruriPattern;
    @Column(length = 64)
    private String tag;
    @Column(nullable = false)
    private Integer priority = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSrcIp() { return srcIp; }
    public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
    public String getProto() { return proto; }
    public void setProto(String proto) { this.proto = proto; }
    public String getFromPattern() { return fromPattern; }
    public void setFromPattern(String fromPattern) { this.fromPattern = fromPattern; }
    public String getRuriPattern() { return ruriPattern; }
    public void setRuriPattern(String ruriPattern) { this.ruriPattern = ruriPattern; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
