package nl.uva.hippo.crawler;

import com.google.common.collect.Lists;
import nl.uva.hippo.crawler.entity.PageHit;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by jorishilhorst on 24/06/16.
 */
@Component
public class SiteComparator {
    private static Logger LOG = LoggerFactory.getLogger(SiteComparator.class);
    // class that receives a list of pagehit and verifies them against a specific site path

    @Autowired
    CrawlerSettings settings;

    @Autowired
    PageHitRepository pageHitRepository;

    public boolean runComparator() {
        LOG.error("Starting comparator");
        LOG.error(String.format("ComparePath: %s", settings.getComparePath()));

        List<PageHit> hits = Lists.newArrayList(pageHitRepository.findAll());
        LOG.error(String.format("Verifying %d hits", hits.size()));
        return verifyIdentical(hits, settings.getComparePath());
    }

    public synchronized boolean verifyIdentical(List<PageHit> hits, String basePath) {
        // for each hit (on original crawl)
        // paste path to basePath
        // verify that (following redirects) there is a page
        // verify that the hash of the found page is equal to hit.hash
        int errorCount = 0;
        boolean result = true;
        for (PageHit hit : hits) {
            boolean same = verifyPageHit(hit, basePath);

            if (!same) {
                errorCount++;

                File compareFile = CrawlerUtil.createFileFromPath(Paths.get(hit.getUrl()), null, settings.getCompareResultsFolder());
                File crawlFile = CrawlerUtil.createFileFromPath(Paths.get(hit.getUrl()), null, settings.getResultsFolder());
                LOG.error(String.format("difference found between [%s] and [%s]", crawlFile, compareFile));

                File diffFile = CrawlerUtil.createFileFromPath(Paths.get(hit.getUrl()), null, settings.getCompareDiffFolder());

                Process p;
                try {
                    diffFile.getParentFile().mkdirs();
                    diffFile.createNewFile();

                    LOG.error(String.format("diff %s %s > %s", crawlFile.getAbsolutePath(), compareFile.getAbsolutePath(), diffFile.getAbsolutePath()));
                    ProcessBuilder builder = new ProcessBuilder("diff", crawlFile.getAbsolutePath(), compareFile.getAbsolutePath());
                    builder.redirectOutput(diffFile);
                    p = builder.start();
                    p.waitFor();
                } catch (Exception e) {
                    LOG.error(String.format("Exception while running diff %s %s > %s", crawlFile.getAbsolutePath(), compareFile.getAbsolutePath(), diffFile.getAbsolutePath()));
                    LOG.error("Exception: ", e);
                }
            }
            result &= same;
        }

        LOG.error(String.format("Finished verification. result: [%s] total errors: [%d]", result, errorCount));
        return result;
    }

    public boolean verifyPageHit(PageHit hit, String basePath) {
        String fullUrl = basePath + hit.getUrl();

        byte[] responseBody;
        String contentData = "";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(fullUrl);

            HttpResponse response = client.execute(get);
            responseBody = EntityUtils.toByteArray(response.getEntity());

            contentData = new String(responseBody, StandardCharsets.UTF_8);
            contentData = CrawlerUtil.stripChangeableContent(contentData);
        } catch (IOException io) {
            LOG.error("IOException while verifying page hit");
            io.printStackTrace();
            return false;
        }

        String hash = CrawlerUtil.hashBytes(contentData.getBytes());
        boolean pagesSame = hash.equals(hit.getHash());

        if (pagesSame) {
            return true;
        } else {
            LOG.error(String.format("Hash does not match. [%s]", hit.getUrl()));
            File newFile = CrawlerUtil.createFileFromPath(Paths.get(hit.getUrl()), null, settings.getCompareResultsFolder());
            newFile.getParentFile().mkdirs();

            try {
                Files.write(newFile.toPath(), contentData.getBytes());
                LOG.error(String.format("writing file [%s]", newFile.getAbsolutePath()));
            } catch (IOException io) {
                LOG.error("IOException while writing compare data", io);
            }

            return false;
        }

    }

}
