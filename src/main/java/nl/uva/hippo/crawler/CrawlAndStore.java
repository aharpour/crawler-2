package nl.uva.hippo.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import nl.uva.hippo.crawler.entity.PageHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class CrawlAndStore extends WebCrawler {
    private static Logger LOG = LoggerFactory.getLogger(CrawlerRunner.class);
    private static AtomicInteger pageCount = new AtomicInteger();

    @Override
    public void visit(Page page) {
        int count = pageCount.incrementAndGet();
        Path current = Paths.get(page.getWebURL().getURL());
        LOG.error(String.format("Processing page [%s] [%d]", current, count));

        CrawlCustomData customData = (CrawlCustomData) getMyController().getCustomData();
        Path root = Paths.get(customData.getSiteBaseUrl());
        Path relative = root.relativize(current);
        File newFile = CrawlerUtil.createFileFromPath(current, root, customData.getPageStorageLocation());

        newFile.getParentFile().mkdirs();

        String contentData = new String(page.getContentData(), StandardCharsets.UTF_8);

        contentData = CrawlerUtil.stripChangeableContent(contentData);

        try {
            Files.write(newFile.toPath(), contentData.getBytes());
            LOG.error(String.format("writing file [%s]", newFile.getAbsolutePath()));
            PageHitRepository repo = customData.getPageHitRepository();
            PageHit hit = new PageHit(relative.toString(), CrawlerUtil.hashBytes(contentData.getBytes()));
            repo.save(hit);
        } catch (IOException io) {
            LOG.error(String.format("failed to write file to disk: %s", newFile.toString()), io);
        }
    }


    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        CrawlCustomData customData = (CrawlCustomData) getMyController().getCustomData();
        if (pageCount.get() >= customData.getMaxPageCount()) {
            return false;
        }

        String href = url.getURL().toLowerCase();
        if (customData.getFilter() != null && customData.getFilter().matcher(href).matches()) {
            // if a filter is provided, use it to filter out unwanted resources.
            LOG.debug(String.format("NOK to visit page [%s]", url));
            return false;
        }

        if (!href.startsWith(customData.getSiteBaseUrl())) {
            LOG.debug(String.format("NOK to visit page [%s]", url));
            return false;
        }

        LOG.error(String.format("OK to visit page [%s]", url));
        return true;
    }

}