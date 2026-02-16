package io.kamailio.admin.modules.mtree.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mtree")
public class Mtree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false)
    private String tname;
    @Column(length = 32, nullable = false)
    private String tprefix;
    @Column(length = 128, nullable = false)
    private String tvalue;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }
    public String getTprefix() { return tprefix; }
    public void setTprefix(String tprefix) { this.tprefix = tprefix; }
    public String getTvalue() { return tvalue; }
    public void setTvalue(String tvalue) { this.tvalue = tvalue; }
}
