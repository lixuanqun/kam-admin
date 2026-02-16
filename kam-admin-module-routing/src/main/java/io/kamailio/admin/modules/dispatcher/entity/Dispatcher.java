package io.kamailio.admin.modules.dispatcher.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "dispatcher")
@Schema(description = "调度器目标")
public class Dispatcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    private Integer id;

    @Column(name = "setid", nullable = false)
    @Schema(description = "调度组 ID")
    private Integer setid = 0;

    @Column(length = 256, nullable = false)
    @Schema(description = "目标地址")
    private String destination;

    @Column(nullable = false)
    @Schema(description = "标志位")
    private Integer flags = 0;

    @Column(nullable = false)
    @Schema(description = "优先级")
    private Integer priority = 0;

    @Column(length = 128)
    @Schema(description = "属性")
    private String attrs = "";

    @Column(length = 64)
    @Schema(description = "描述")
    private String description = "";

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSetid() { return setid; }
    public void setSetid(Integer setid) { this.setid = setid; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) { this.flags = flags; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getAttrs() { return attrs; }
    public void setAttrs(String attrs) { this.attrs = attrs; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
