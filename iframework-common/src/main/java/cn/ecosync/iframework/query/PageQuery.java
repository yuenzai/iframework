package cn.ecosync.iframework.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class PageQuery {
    private Integer page;
    private Integer pagesize;

    public Sort toSort() {
        return Sort.unsorted();
    }

    public Pageable toPageable() {
        if (page != null && pagesize != null) {
            return PageRequest.of(page, pagesize, toSort());
        } else {
            return Pageable.unpaged(toSort());
        }
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "page=" + page +
                ", pagesize=" + pagesize +
                '}';
    }
}
