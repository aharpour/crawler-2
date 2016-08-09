package nl.uva.hippo.crawler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlerUtil {
    private static Logger LOG = LoggerFactory.getLogger(CrawlerRunner.class);

    public static File createFileFromPath(Path current, Path root, String pageStorageLocation) {
        Path relative;
        if (root != null) {
            relative = root.relativize(current);
        } else {
            relative = current;
        }
        if ("".equals(relative.toString())) {
            relative = Paths.get("__startpath__");
        }
        File baseDir = new File(pageStorageLocation);
        File newFile = new File(baseDir, relative.toString());
        LOG.debug(String.format("relative [%s], newFile [%s]", baseDir, relative));

        // it is possible there is a clash between /abc as a file and /abc/def (abc as a folder)
        // every time a file ends without an extension , we use a fake extension which hopefully
        // isn't used in the site
        Pattern extensionPattern = Pattern.compile("\\.[^\\.]+$");
        Matcher extensionMatcher = extensionPattern.matcher(newFile.getPath());
        if (!extensionMatcher.matches()) {
            // make up an extension and add it to the file
            newFile = new File(newFile.getParentFile(), newFile.getName() + ".noextension");
        }

        return newFile;
    }

    public static String stripChangeableContent(String input) {
        // (?s) makes . match \n   , as content of script block is multi line
        input = input.replaceAll("(?s)<script type=\"application/ld\\+json\">.*?</script>", "");
        input = input.replaceAll("href=\"(.*?)\"", "");
        input = input.replaceAll("href=\'(.*?)\'", "");
        input = input.replaceAll("src=\"(.*?)\"", "");
        input = input.replaceAll("src=\'(.*?)\'", "");
        input = input.replaceAll("action=\"(.*?)\"", "");
        input = input.replaceAll("action=\'(.*?)\'", "");
        input = input.replaceAll("id=\"youtube-[^\"]+", "id=\"");
        input = input.replaceAll("'canonicalURI': '(.*?)',", "");
        input = input.replaceAll("data-main=\"(.*?)\"", "");
        input = input.replaceAll("'canonicalHostName': '(.*?)'", "");
        input = input.replaceAll("options.enableSearchboxOnly\\(\"(.*)?\"\\);", "");

        //<script type="application/ld+json">
        return input;
    }


    public static String hashBytes(byte[] bytes) {
        return DigestUtils.sha1Hex(bytes);
    }


}
