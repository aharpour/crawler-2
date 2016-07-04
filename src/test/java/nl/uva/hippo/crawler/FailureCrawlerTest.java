package nl.uva.hippo.crawler;

import com.google.common.collect.Lists;
import nl.uva.hippo.crawler.entity.PageHit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CrawlerApplication.class)
//@TestPropertySource(properties =
//        {
//            "crawler.startPage=http://localhost:9000/errorsite/startpoint",
//            "crawler.siteBaseUrl=http://localhost:9000/errorsite/",
//            "crawler.comparePath=http://localhost:9000/errorsite/"
//        })
@TestPropertySource(locations = "classpath:failuretest.properties")
public class FailureCrawlerTest {

    @Autowired
    CrawlerRunner runner;

    @Autowired
    SiteComparator siteComparator;

    @Before
    public void startUp() {
    }

    @Test
    public void testCrawlerRuns() throws Exception {
        runner.runCrawler();

        // run the verifyer on /site2 , giving site1 as input
        Assert.assertFalse("verification should find that the site is different", siteComparator.runComparator());
    }
}
