package io.kamailio.admin.modules.secfilter.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "secfilter")
public class Secfilter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer action = 0;
    @Column
    private Integer type = 0;
    @Column(length = 64)
    private String data;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getAction() { return action; }
    public void setAction(Integer action) { this.action = action; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
