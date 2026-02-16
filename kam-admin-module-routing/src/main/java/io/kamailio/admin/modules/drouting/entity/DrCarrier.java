package io.kamailio.admin.modules.drouting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dr_carriers")
public class DrCarrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 64, nullable = false)
    private String carrierid;
    @Column(length = 255, nullable = false)
    private String gwlist;
    @Column(nullable = false)
    private Integer flags = 0;
    @Column(name = "sort_alg", nullable = false)
    private Integer sortAlg = 0;
    @Column(nullable = false)
    private Integer state = 0;
    @Column(length = 255)
    private String attrs;
    @Column(length = 128)
    private String description;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCarrierid() { return carrierid; }
    public void setCarrierid(String carrierid) { this.carrierid = carrierid; }
    public String getGwlist() { return gwlist; }
    public void setGwlist(String gwlist) { this.gwlist = gwlist; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getSortAlg() { return sortAlg; }
    public void setSortAlg(Integer sortAlg) { this.sortAlg = sortAlg; }
    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
