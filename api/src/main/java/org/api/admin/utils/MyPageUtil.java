package org.api.admin.utils;

import org.api.admin.model.common.PageCommon;

import java.util.Optional;

public class MyPageUtil {
    private MyPageUtil() {
    }
    
    public static PageCommon checkPage(PageCommon page) {
        page = Optional.ofNullable(page).orElse(new PageCommon());
        page.setPageIndex(Optional.ofNullable(page.getPageIndex()).orElse(0));
        page.setPageNum(Optional.ofNullable(page.getPageNum()).orElse(50));
        return page;
    }
    
    
}
