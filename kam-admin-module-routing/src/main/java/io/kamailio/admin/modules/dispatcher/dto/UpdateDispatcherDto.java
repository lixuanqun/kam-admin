package io.kamailio.admin.modules.dispatcher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

@Schema(description = "更新调度目标 DTO")
public class UpdateDispatcherDto {

    @Nullable
    @Min(0)
    @Schema(description = "调度组 ID")
    private Integer setid;

    @Nullable
    @Size(max = 256)
    @Schema(description = "目标地址")
    private String destination;

    @Nullable
    @Schema(description = "标志位")
    private Integer flags;

    @Nullable
    @Schema(description = "优先级")
    private Integer priority;

    @Nullable
    @Size(max = 128)
    @Schema(description = "属性")
    private String attrs;

    @Nullable
    @Size(max = 64)
    @Schema(description = "描述")
    private String description;

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
