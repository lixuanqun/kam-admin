package io.kamailio.admin.modules.permissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTrustedDto {
    @NotBlank @Size(max = 50) private String srcIp;
    @Size(max = 4) private String proto;
    @Size(max = 64) private String fromPattern;
    @Size(max = 64) private String ruriPattern;
    @Size(max = 64) private String tag;
    private Integer priority = 0;

    public String getSrcIp() { return srcIp; }
    public void setSrcIp(String srcIp) { this.srcIp = srcIp; }
    public String getProto() { return proto; }
    public void setProto(String proto) { this.proto = proto; }
    public String getFromPattern() { return fromPattern; }
    public void setFromPattern(String fromPattern) { this.fromPattern = fromPattern; }
    public String getRuriPattern() { return ruriPattern; }
    public void setRuriPattern(String ruriPattern) { this.ruriPattern = ruriPattern; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}
