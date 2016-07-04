package nl.uva.hippo.crawler;

import com.google.common.collect.Lists;
import nl.uva.hippo.crawler.entity.PageHit;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by jorishilhorst on 24/06/16.
 */
@Component
public class SiteComparator {
    // class that receives a list of pagehit and verifies them against a specific site path

    @Autowired
    private Hasher hasher;

    @Autowired
    CrawlerSettings settings;

    @Autowired
    PageHitRepository pageHitRepository;

    public boolean runComparator() {
        List<PageHit> hits = Lists.newArrayList(pageHitRepository.findAll());
        return verifyIdentical(hits, settings.getComparePath());
    }


    public boolean verifyIdentical(List<PageHit> hits, String basePath) {
        // for each hit (on original crawl)
        // paste path to basePath
        // verify that (following redirects) there is a page
        // verify that the hash of the found page is equal to hit.hash
        boolean result = true;
        for (PageHit hit : hits) {
            boolean same = verifyPageHit(hit, basePath);
            if (!same) {
                System.out.println(String.format("difference found. Original [%s] not the same in site [%s]", hit.getUrl(), basePath));
            }
            result &= same;
        }
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

            contentData = contentData.replaceAll("href=\"(.*?)\"", "");
            contentData = contentData.replaceAll("href=\'(.*?)\'", "");
            contentData = contentData.replaceAll("src=\"(.*?)\"", "");
            contentData = contentData.replaceAll("src=\'(.*?)\'", "");

        } catch (IOException io) {
            System.out.println("IOException while verifying page hit");
            io.printStackTrace();
            return false;
        }

        String hash = hasher.hashBytes(contentData.getBytes());

        if (hash.equals(hit.getHash())) {
            return true;
        } else {
            System.out.println("Hash does not match. ");
            System.out.println(new String(contentData));
            return false;
        }

    }
}
