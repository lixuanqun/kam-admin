package io.kamailio.admin.modules.subscriber.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "创建用户 DTO")
public class CreateSubscriberDto {
    @NotBlank @Size(max = 64) private String username;
    @NotBlank @Size(max = 64) private String domain;
    @NotBlank @Size(min = 6, max = 64) private String password;
    @Email @Size(max = 64) private String emailAddress;
    @Size(max = 64) private String rpid;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getRpid() { return rpid; }
    public void setRpid(String rpid) { this.rpid = rpid; }
}
