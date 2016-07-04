package nl.uva.hippo.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import nl.uva.hippo.crawler.entity.PageHit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlAndStore extends WebCrawler {

    @Override
    public void visit(Page page) {
        Path current = Paths.get(page.getWebURL().getURL());
        System.out.println(String.format("Processing page [%s]", current));

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String canonical = extractCanonical(htmlParseData.getHtml());
            if (canonical != null) {
                if (!page.getWebURL().getURL().equals(canonical)) {
                    System.out.println(String.format("Skipping page [%s] as canonical [%s] doesn't match url.", page.getWebURL().getURL(), canonical));
                    return;
                }
            }
        }

        CrawlCustomData customData = (CrawlCustomData) getMyController().getCustomData();
        Hasher hasher = customData.getHasher();
        Path root = Paths.get(customData.getSiteBaseUrl());

        Path relative = root.relativize(current);
        File baseDir = new File(customData.getPageStorageLocation());
        File newFile = new File(baseDir, relative.toString());
        newFile.getParentFile().mkdirs();

        // it is possible there is a clash between /abc as a file and /abc/def (abc as a folder)
        // every time a file ends without an extension , we use a fake extension which hopefully
        // isn't used in the site
        Pattern extensionPattern = Pattern.compile("\\.[^\\.]+$");
        Matcher extensionMatcher = extensionPattern.matcher(newFile.getPath());
        if (!extensionMatcher.matches()) {
            // make up an extension and add it to the file
            newFile = new File(newFile.getParentFile(), newFile.getName() + ".noextension");
        }

        String contentData = new String(page.getContentData(), StandardCharsets.UTF_8);

        contentData = contentData.replaceAll("href=\"(.*?)\"", "");
        contentData = contentData.replaceAll("href=\'(.*?)\'", "");
        contentData = contentData.replaceAll("src=\"(.*?)\"", "");
        contentData = contentData.replaceAll("src=\'(.*?)\'", "");

        try {
            Files.write(newFile.toPath(), contentData.getBytes());
            PageHitRepository repo = customData.getPageHitRepository();
            PageHit hit = new PageHit(relative.toString(), hasher.hashBytes(contentData.getBytes()));
            repo.save(hit);
        } catch (IOException io) {
            System.out.println("failed to write file to disk: " + newFile.toString());
            io.printStackTrace();
        }
    }

    public static void main(String[] argv) {
        File newFile = new File("/var/projects");
        Pattern extensionPattern = Pattern.compile("\\.[^\\.]+$");
        Matcher extensionMatcher = extensionPattern.matcher(newFile.getPath());
        if (!extensionMatcher.matches()) {
            // make up an extension and add it to the file
            newFile = new File(newFile.getParentFile(), newFile.getName() + ".noextension");
        }
        System.out.println(newFile);
    }

    public String extractCanonical(String html) {
        // <link rel="canonical" href="http://student.uva.nl/chem/events/events.html"/>
        Pattern extractCanonicalLink = Pattern.compile("(<link[^>]*rel=['\"]canonical['\"][^>]*/>)");
        Matcher linkMatcher = extractCanonicalLink.matcher(html);
        if (!linkMatcher.find()) {
            return null;
        }
        String canonLink = linkMatcher.group(1);
        Pattern extractHref = Pattern.compile("href=['\"]([^'\"]*)['\"]");
        Matcher hrefMatcher = extractHref.matcher(canonLink);
        if (!hrefMatcher.find()) {
            return null;
        }
        return hrefMatcher.group(1);
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        CrawlCustomData customData = (CrawlCustomData) getMyController().getCustomData();

        String href = url.getURL().toLowerCase();
        if (customData.getFilter() != null && customData.getFilter().matcher(href).matches()) {
            // if a filter is provided, use it to filter out unwanted resources.
            System.out.println(String.format("NOK to visit page [%s]", url));
            return false;
        }

        // only follow links that fall inside the website.
        if (href.startsWith(customData.getSiteBaseUrl())) {
            System.out.println(String.format("OK to visit page [%s]", url));
            return true;
        } else {
            System.out.println(String.format("NOK to visit page [%s]", url));
            return false;
        }
    }

}