package io.kamailio.admin.modules.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public class CreateDomainDto {
    @NotBlank @Size(max = 64) private String domain;
    @Nullable @Size(max = 64) private String did;

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getDid() { return did; }
    public void setDid(String did) { this.did = did; }
}
