package firstattempt;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by jorishilhorst on 20/06/16.
 */
public class SingleDepthCrawler extends WebCrawler {
//    final Logger LOGGER = LoggerFactory.getLogger(SingleDepthCrawler.class);
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(ico|css|json|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    @Override
    public void visit(Page page) {
        if (page.isRedirect()) {
//            LOGGER.debug(String.format("[%s] is redirected to [%s]", page.getWebURL(), page.getRedirectedToUrl()));
        } else {
//            LOGGER.debug(String.format("[%s] is loaded with code [%s]", page.getWebURL(), page.getStatusCode()));
        }

        super.visit(page);
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://localhost:8080/");
    }
}
