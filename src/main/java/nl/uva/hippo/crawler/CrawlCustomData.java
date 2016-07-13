package nl.uva.hippo.crawler;

import java.util.regex.Pattern;

public class CrawlCustomData {
    private Pattern filter;
    private String restrictToUrl;
    private String pageStorageLocation;
    private PageHitRepository pageHitRepository;
    private int maxPageCount;

    public Pattern getFilter() {
        return filter;
    }

    public void setFilter(Pattern filter) {
        this.filter = filter;
    }

    public String getSiteBaseUrl() {
        return restrictToUrl;
    }

    public void setRestrictToUrl(String restrictToUrl) {
        this.restrictToUrl = restrictToUrl;
    }

    public String getPageStorageLocation() {
        return pageStorageLocation;
    }

    public void setPageStorageLocation(String pageStorageLocation) {
        this.pageStorageLocation = pageStorageLocation;
    }

    public PageHitRepository getPageHitRepository() {
        return pageHitRepository;
    }

    public void setPageHitRepository(PageHitRepository pageHitRepository) {
        this.pageHitRepository = pageHitRepository;
    }

    public String getRestrictToUrl() {
        return restrictToUrl;
    }

    public int getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
    }
}
