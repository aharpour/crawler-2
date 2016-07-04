package firstattempt;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;

public class OneDepth {
//    final Logger LOGGER = LoggerFactory.getLogger(OneDepth.class);

    public static void mainx(String[] argv) throws Exception {
//        JdbcConnectionPool cp = JdbcConnectionPool.
//                create("jdbc:h2:~/test", "sa", "sa");
//        Connection conn = cp.getConnection();
//        conn.close();

        String crawlStorageFolder = "/var/projects/crawler";
        int numberOfCrawlers = 1;

//        CrawlerSettings crawlerSettings = new CrawlerSettings();
//        crawlerSettings.setConnectionPool(cp);
//        crawlerSettings.setRestrictTo("http://localhost:8080/site/");
//        crawlerSettings.setIgnoreExtensions("");

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(2);
        config.setFollowRedirects(true);
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
//        controller.setCustomData(crawlerSettings);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://localhost:8080/site/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(SingleDepthCrawler.class, numberOfCrawlers);

//        cp.dispose();
    }
}
