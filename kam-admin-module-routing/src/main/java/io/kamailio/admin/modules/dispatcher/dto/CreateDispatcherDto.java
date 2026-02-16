package io.kamailio.admin.modules.dispatcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "创建调度目标 DTO")
public class CreateDispatcherDto {

    @Schema(description = "调度组 ID", example = "1")
    @Min(0)
    private int setid;

    @Schema(description = "目标地址", example = "sip:192.168.1.100:5060")
    @NotBlank
    @Size(max = 256)
    private String destination;

    @Schema(description = "标志位", defaultValue = "0")
    private Integer flags;

    @Schema(description = "优先级", defaultValue = "0")
    private Integer priority;

    @Schema(description = "属性")
    @Size(max = 128)
    private String attrs;

    @Schema(description = "描述")
    @Size(max = 64)
    private String description;

    public int flagsOrDefault() { return flags != null ? flags : 0; }
    public int priorityOrDefault() { return priority != null ? priority : 0; }
    public String attrsOrDefault() { return attrs != null ? attrs : ""; }
    public String descriptionOrDefault() { return description != null ? description : ""; }

    public int getSetid() { return setid; }
    public void setSetid(int setid) { this.setid = setid; }
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
