package com.ccsw.tutorialbooking.booking.model;

import com.ccsw.tutorialbooking.booking.PageableRequest;

/**
 * @author ccsw
 *
 */
public class BookingSearchDto {

    private PageableRequest pageable;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
