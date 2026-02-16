package io.kamailio.admin.modules.drouting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dr_rules")
public class DrRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruleid")
    private Integer ruleid;
    @Column(length = 255, nullable = false)
    private String groupid;
    @Column(length = 64, nullable = false)
    private String prefix;
    @Column(length = 255)
    private String timerec;
    @Column(nullable = false)
    private Integer priority = 0;
    @Column(length = 255)
    private String routeid;
    @Column(length = 255, nullable = false)
    private String gwlist;
    @Column(name = "sort_alg")
    private Integer sortAlg = 0;
    @Column(name = "sort_profile", length = 255)
    private String sortProfile;
    @Column(length = 255)
    private String attrs;
    @Column(length = 128)
    private String description;

    public Integer getRuleid() { return ruleid; }
    public void setRuleid(Integer ruleid) { this.ruleid = ruleid; }
    public String getGroupid() { return groupid; }
    public void setGroupid(String groupid) { this.groupid = groupid; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getTimerec() { return timerec; }
    public void setTimerec(String timerec) { this.timerec = timerec; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getRouteid() { return routeid; }
    public void setRouteid(String routeid) { this.routeid = routeid; }
    public String getGwlist() { return gwlist; }
    public void setGwlist(String gwlist) { this.gwlist = gwlist; }
    public Integer getSortAlg() { return sortAlg; }
    public void setSortAlg(Integer sortAlg) { this.sortAlg = sortAlg; }
    public String getSortProfile() { return sortProfile; }
    public void setSortProfile(String sortProfile) { this.sortProfile = sortProfile; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
