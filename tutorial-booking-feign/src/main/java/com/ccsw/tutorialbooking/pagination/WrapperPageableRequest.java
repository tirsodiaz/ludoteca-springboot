package com.ccsw.tutorialbooking.pagination;

/**
 * @author ccsw
 *
 */
public class WrapperPageableRequest {

    private PageableRequest pageableRequest;

    public PageableRequest getPageableRequest() {
        return pageableRequest;
    }

    public void setPageableRequest(PageableRequest pageable) {
        this.pageableRequest = pageable;
    }
}
