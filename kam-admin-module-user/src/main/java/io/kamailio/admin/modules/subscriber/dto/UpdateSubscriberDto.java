package io.kamailio.admin.modules.subscriber.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public class UpdateSubscriberDto {
    @Nullable @Size(min = 6, max = 64) private String password;
    @Nullable @Email @Size(max = 64) private String emailAddress;
    @Nullable @Size(max = 64) private String rpid;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getRpid() { return rpid; }
    public void setRpid(String rpid) { this.rpid = rpid; }
}
