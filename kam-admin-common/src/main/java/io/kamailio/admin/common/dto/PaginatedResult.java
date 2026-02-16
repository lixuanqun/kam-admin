package io.kamailio.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "分页结果")
public class PaginatedResult<T> {

    @Schema(description = "数据列表")
    private final List<T> items;

    @Schema(description = "总数量")
    private final long total;

    @Schema(description = "当前页")
    private final int page;

    @Schema(description = "每页数量")
    private final int limit;

    @Schema(description = "总页数")
    private final int totalPages;

    public PaginatedResult(List<T> items, long total, int page, int limit) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.totalPages = (int) Math.ceil((double) total / limit);
    }

    public List<T> getItems() { return items; }
    public long getTotal() { return total; }
    public int getPage() { return page; }
    public int getLimit() { return limit; }
    public int getTotalPages() { return totalPages; }
}
