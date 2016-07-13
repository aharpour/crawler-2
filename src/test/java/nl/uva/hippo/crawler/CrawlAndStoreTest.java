package nl.uva.hippo.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by jorishilhorst on 29/06/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CrawlAndStoreTest {
    @Test
    public void testCreateStoragePath_normal() {
        CrawlAndStore store = new CrawlAndStore();
        assertEquals("/tmp/abc.noextension", CrawlerUtil.createFileFromPath(Paths.get("http://test/abc"), Paths.get("http://test/"), "/tmp").toString());
    }

    @Test
    public void testCreateStoragePath_emptyComparable() {
        CrawlAndStore store = new CrawlAndStore();
        assertEquals("/tmp/__startpath__.noextension", CrawlerUtil.createFileFromPath(Paths.get("http://test/abc"), Paths.get("http://test/abc"), "/tmp").toString());
    }
}
