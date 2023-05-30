package com.ccsw.tutorialauthor.author.model;

import com.ccsw.tutorialauthor.common.pagination.Pageable;

/**
 * @author ccsw
 *
 */
public class AuthorSearchDto {

    private Pageable pageable;

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
