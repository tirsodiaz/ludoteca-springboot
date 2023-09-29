package com.ccsw.tutorial.common.pagination;

/**
 * @author ccsw
 *
 */
public class WrapperPageableRequest {

    private PageableRequest pageableRequest;

    public PageableRequest getPageableRequest() {
        return pageableRequest;
    }

    public void setPageableRequest(PageableRequest pageableRequest) {
        this.pageableRequest = pageableRequest;
    }
}
