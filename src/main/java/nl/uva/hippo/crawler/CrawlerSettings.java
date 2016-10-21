package nl.uva.hippo.crawler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="crawler")
public class CrawlerSettings {
    private String storageFolder;
    private String resultsFolder;
    private int concurrentThreads;
    private int maxDepth;
    private boolean followRedirects;
    private String startPage;
    private String siteBaseUrl;
    private String comparePath;
    private int maxPageCount;
    private String compareResultsFolder;
    private String compareDiffFolder;

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    public String getResultsFolder() {
        return resultsFolder;
    }

    public void setResultsFolder(String resultsFolder) {
        this.resultsFolder = resultsFolder;
    }

    public int getConcurrentThreads() {
        return concurrentThreads;
    }

    public void setConcurrentThreads(int concurrentThreads) {
        this.concurrentThreads = concurrentThreads;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getSiteBaseUrl() {
        return siteBaseUrl;
    }

    public void setSiteBaseUrl(String siteBaseUrl) {
        this.siteBaseUrl = siteBaseUrl;
    }

    public String getComparePath() {
        return comparePath + (!comparePath.endsWith("/") ? "/" : "");
    }

    public void setComparePath(String comparePath) {
        this.comparePath = comparePath;
    }

    public int getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    public String getCompareResultsFolder() {
        return compareResultsFolder;
    }

    public void setCompareResultsFolder(String compareResultsFolder) {
        this.compareResultsFolder = compareResultsFolder;
    }

    public String getCompareDiffFolder() {
        return compareDiffFolder;
    }

    public void setCompareDiffFolder(String compareDiffFolder) {
        this.compareDiffFolder = compareDiffFolder;
    }
}
