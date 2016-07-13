package nl.uva.hippo.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CrawlerRunner {
    private static Logger LOG = LoggerFactory.getLogger(CrawlerRunner.class);

    public CrawlerRunner() {
    }

    @Autowired
    CrawlerSettings settings;

    @Autowired
    private PageHitRepository pageHitRepository;

    public void runCrawler() throws Exception {
        // ensure the folder exists
        File resultsFolder = new File(settings.getResultsFolder());
        if (!resultsFolder.exists()) {
            LOG.error(String.format("no results folder [%s]", resultsFolder));
            return;
        }
//        javaDoesnothaveDeleteAllFiles(resultsFolder.toPath());
        LOG.error(String.format("ResultsFolder [%s]", resultsFolder.toString()));
        resultsFolder.mkdirs();

        CrawlConfig config = new CrawlConfig();
        if (settings.getMaxDepth() > 0) {
            config.setMaxDepthOfCrawling(settings.getMaxDepth());
        }
        config.setFollowRedirects(settings.isFollowRedirects());
        config.setCrawlStorageFolder(settings.getStorageFolder());
        config.setProcessBinaryContentInCrawling(true);
        config.setIncludeBinaryContentInCrawling(true);
        config.setConnectionTimeout(1000*60*4);
        config.setSocketTimeout(1000 * 1);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        CrawlCustomData customData = new CrawlCustomData();
//        customData.setFilter(Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$"));
        customData.setRestrictToUrl(settings.getSiteBaseUrl());
        customData.setPageStorageLocation(settings.getResultsFolder());
        customData.setMaxPageCount(settings.getMaxPageCount());
        customData.setPageHitRepository(pageHitRepository);
        controller.setCustomData(customData);


        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawlerRunner starts following links
         * which are found in these pages
         */
        controller.addSeed(settings.getStartPage());

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(CrawlAndStore.class, settings.getConcurrentThreads());
    }

}
