package io.kamailio.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "分页查询参数")
public class PaginationDto {

    @Schema(description = "页码", defaultValue = "1")
    @Min(1)
    private Integer page;

    @Schema(description = "每页数量", defaultValue = "20")
    @Min(1)
    @Max(100)
    private Integer limit;

    @Schema(description = "搜索关键词")
    private String keyword;

    public int pageOrDefault() {
        return page != null ? page : 1;
    }

    public int limitOrDefault() {
        return limit != null ? limit : 20;
    }

    public int offset() {
        return (pageOrDefault() - 1) * limitOrDefault();
    }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}
