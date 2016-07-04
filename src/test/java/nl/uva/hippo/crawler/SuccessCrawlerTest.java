package nl.uva.hippo.crawler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CrawlerApplication.class)
@TestPropertySource(locations = "classpath:successtest.properties")
public class SuccessCrawlerTest {
    @Autowired
    CrawlerRunner crawlerRunner;

    @Autowired
    SiteComparator siteComparator;

    @Before
    public void startUp() {
    }

    @Test
    public void testCrawlerRuns() throws Exception {
        crawlerRunner.runCrawler();

        // run the verifyer on /site2 , giving site1 as input
        Assert.assertTrue("sites were not identical", siteComparator.runComparator());
    }
}
