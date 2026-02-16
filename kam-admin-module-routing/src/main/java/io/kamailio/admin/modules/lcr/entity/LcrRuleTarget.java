package io.kamailio.admin.modules.lcr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lcr_rule_target")
public class LcrRuleTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "lcr_id")
    private Integer lcrId = 1;
    @Column(name = "rule_id")
    private Integer ruleId;
    @Column(name = "gw_id")
    private Integer gwId;
    @Column
    private Integer priority = 1;
    @Column
    private Integer weight = 1;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLcrId() { return lcrId; }
    public void setLcrId(Integer lcrId) { this.lcrId = lcrId; }
    public Integer getRuleId() { return ruleId; }
    public void setRuleId(Integer ruleId) { this.ruleId = ruleId; }
    public Integer getGwId() { return gwId; }
    public void setGwId(Integer gwId) { this.gwId = gwId; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
