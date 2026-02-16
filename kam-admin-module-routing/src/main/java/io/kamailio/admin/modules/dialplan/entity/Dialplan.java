package io.kamailio.admin.modules.dialplan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dialplan")
public class Dialplan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer dpid = 0;
    @Column(name = "pr")
    private Integer priority = 0;
    @Column(name = "match_op")
    private Integer matchOp = 1;
    @Column(name = "match_exp", length = 64, nullable = false)
    private String matchExp;
    @Column(name = "match_len")
    private Integer matchLen = 0;
    @Column(name = "subst_exp", length = 64, nullable = false)
    private String substExp;
    @Column(name = "repl_exp", length = 256, nullable = false)
    private String replExp;
    @Column(length = 64)
    private String attrs;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getDpid() { return dpid; }
    public void setDpid(Integer dpid) { this.dpid = dpid; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getMatchOp() { return matchOp; }
    public void setMatchOp(Integer matchOp) { this.matchOp = matchOp; }
    public String getMatchExp() { return matchExp; }
    public void setMatchExp(String matchExp) { this.matchExp = matchExp; }
    public Integer getMatchLen() { return matchLen; }
    public void setMatchLen(Integer matchLen) { this.matchLen = matchLen; }
    public String getSubstExp() { return substExp; }
    public void setSubstExp(String substExp) { this.substExp = substExp; }
    public String getReplExp() { return replExp; }
    public void setReplExp(String replExp) { this.replExp = replExp; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
}
