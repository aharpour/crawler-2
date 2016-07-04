package nl.uva.hippo.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by jorishilhorst on 29/06/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CrawlAndStoreTest {
    @Test
    public void testExtractCanonical() {
        CrawlAndStore store = new CrawlAndStore();
        assertEquals("http://somesite.site.nl/page.html", store.extractCanonical("<html><head><link rel=\"canonical\" href=\"http://somesite.site.nl/page.html\" />"));
        assertEquals("http://somesite.site.nl/page.html", store.extractCanonical("<html><head><link href=\"http://somesite.site.nl/page.html\" rel=\"canonical\"  />"));
        assertEquals("http://somesite.site.nl/page.html", store.extractCanonical("<html><head><link    rel=\"canonical\"    href=\"http://somesite.site.nl/page.html\"    />"));
    }
}
