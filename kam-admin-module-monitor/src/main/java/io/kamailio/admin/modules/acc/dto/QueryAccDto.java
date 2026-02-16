package io.kamailio.admin.modules.acc.dto;

import io.kamailio.admin.common.dto.PaginationDto;
import org.springframework.lang.Nullable;

public class QueryAccDto extends PaginationDto {
    @Nullable private String srcUser;
    @Nullable private String dstUser;
    @Nullable private String callid;
    @Nullable private String startTime;
    @Nullable private String endTime;
    @Nullable private String sipCode;

    public String getSrcUser() { return srcUser; }
    public void setSrcUser(String srcUser) { this.srcUser = srcUser; }
    public String getDstUser() { return dstUser; }
    public void setDstUser(String dstUser) { this.dstUser = dstUser; }
    public String getCallid() { return callid; }
    public void setCallid(String callid) { this.callid = callid; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getSipCode() { return sipCode; }
    public void setSipCode(String sipCode) { this.sipCode = sipCode; }
}
