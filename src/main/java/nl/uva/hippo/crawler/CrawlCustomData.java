package nl.uva.hippo.crawler;

import java.util.regex.Pattern;

public class CrawlCustomData {
    private Pattern filter;
    private String restrictToUrl;
    private String pageStorageLocation;
    private PageHitRepository pageHitRepository;
    private Hasher hasher;

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

    public Hasher getHasher() {
        return hasher;
    }

    public void setHasher(Hasher hasher) {
        this.hasher = hasher;
    }
}
