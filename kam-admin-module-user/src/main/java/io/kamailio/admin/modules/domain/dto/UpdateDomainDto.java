package io.kamailio.admin.modules.domain.dto;

import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public class UpdateDomainDto {
    @Nullable @Size(max = 64) private String did;

    public String getDid() { return did; }
    public void setDid(String did) { this.did = did; }
}
