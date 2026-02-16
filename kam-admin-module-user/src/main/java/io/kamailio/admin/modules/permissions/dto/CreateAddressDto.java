package io.kamailio.admin.modules.permissions.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAddressDto {
    private Integer grp = 1;
    @NotBlank @Size(max = 50) private String ipAddr;
    private Integer mask = 32;
    private Integer port = 0;
    @Size(max = 64) private String tag;

    public Integer getGrp() { return grp; }
    public void setGrp(Integer grp) { this.grp = grp; }
    public String getIpAddr() { return ipAddr; }
    public void setIpAddr(String ipAddr) { this.ipAddr = ipAddr; }
    public Integer getMask() { return mask; }
    public void setMask(Integer mask) { this.mask = mask; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
