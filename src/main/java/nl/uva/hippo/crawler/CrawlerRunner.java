package nl.uva.hippo.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import firstattempt.SingleDepthCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

@Component
public class CrawlerRunner {


    public CrawlerRunner() {

    }

    @Autowired
    CrawlerSettings settings;

    @Autowired
    private PageHitRepository pageHitRepository;

    @Autowired
    private Hasher hasher;

    private boolean javaDoesnothaveDeleteAllFiles(Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir,
                                                          IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }

            });
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    public void runCrawler() throws Exception {
        // ensure the folder exists
        File resultsFolder = new File(settings.getResultsFolder());
        if (!resultsFolder.exists()) {
            System.out.println(String.format("no results folder [%s]", resultsFolder));
            return;
        }
        javaDoesnothaveDeleteAllFiles(resultsFolder.toPath());
        resultsFolder.mkdirs();

        CrawlConfig config = new CrawlConfig();
        if (settings.getMaxDepth() > 0) {
            config.setMaxDepthOfCrawling(settings.getMaxDepth());
        }
        config.setFollowRedirects(settings.isFollowRedirects());
        config.setCrawlStorageFolder(settings.getStorageFolder());
        config.setProcessBinaryContentInCrawling(true);
        config.setIncludeBinaryContentInCrawling(true);

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
        customData.setPageHitRepository(pageHitRepository);
        customData.setHasher(hasher);
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
