package com.app.brandadmin.Model;

public class Links {
    private String firstPage;
    private String lastPageUrl;
    private String nextPageUrl;
    private String prevPageUrl;
    private String totalStr;

    public String getFirstPage() {
        return firstPage;
    }

    public Links setFirstPage(String firstPage) {
        this.firstPage = firstPage;
        return this;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public Links setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
        return this;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public Links setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
        return this;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public Links setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
        return this;
    }

    public String getTotalStr() {
        return totalStr;
    }

    public Links setTotalStr(String totalStr) {
        this.totalStr = totalStr;
        return this;
    }


     /* "first_page_url": "http://queryfinders.com/brandmania_uat/public/api/getImageCategory?page=1",
        "from": 1,
        "last_page": 5,
        "last_page_url": "http://queryfinders.com/brandmania_uat/public/api/getImageCategory?page=5",
        "next_page_url": "http://queryfinders.com/brandmania_uat/public/api/getImageCategory?page=2",
        "path": "http://queryfinders.com/brandmania_uat/public/api/getImageCategory",
        "per_page": 2,
        "prev_page_url": null,
        "to": 2,
        "total": 9*/
}
