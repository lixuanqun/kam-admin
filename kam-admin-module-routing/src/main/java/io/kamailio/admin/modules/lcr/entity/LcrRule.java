package io.kamailio.admin.modules.lcr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lcr_rule")
public class LcrRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "lcr_id")
    private Integer lcrId = 1;
    @Column(length = 16)
    private String prefix;
    @Column(name = "from_uri", length = 64)
    private String fromUri;
    @Column(name = "request_uri", length = 64)
    private String requestUri;
    @Column(name = "mt_tvalue", length = 128)
    private String mtTvalue;
    @Column
    private Integer stopper = 0;
    @Column
    private Integer enabled = 1;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLcrId() { return lcrId; }
    public void setLcrId(Integer lcrId) { this.lcrId = lcrId; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getFromUri() { return fromUri; }
    public void setFromUri(String fromUri) { this.fromUri = fromUri; }
    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }
    public String getMtTvalue() { return mtTvalue; }
    public void setMtTvalue(String mtTvalue) { this.mtTvalue = mtTvalue; }
    public Integer getStopper() { return stopper; }
    public void setStopper(Integer stopper) { this.stopper = stopper; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
}
