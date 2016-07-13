package nl.uva.hippo.crawler;

import nl.uva.hippo.crawler.entity.PageHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CrawlerApplication.class)
@TestPropertySource(locations = "classpath:successtest.properties")
public class ComparatorTest {
    @Autowired
    SiteComparator siteComparator;

    @Autowired
    CrawlerSettings settings;

    @Test
    public void testVerifyIdentical() {
        PageHit hit = new PageHit("/abc", "123");
        siteComparator.verifyIdentical(Arrays.asList(hit), settings.getComparePath());
    }
}
