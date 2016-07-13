package nl.uva.hippo.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
public class CrawlerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext appContext = SpringApplication.run(CrawlerApplication.class, args);
    }

    @Autowired
    CrawlerRunner crawlerRunner;

    @Autowired
    SiteComparator siteComparator;


    @Override
    public void run(String... strings) throws Exception {
        for (String arg : strings) {
            switch (arg) {
                case "crawl":
                    // application.properties contains settings to be used
                    crawlerRunner.runCrawler();
                    break;
                case "compare":
                    // application.properties contains settings to be used
                    siteComparator.runComparator();
                    break;
                default:
                    System.out.println("Usage: crawl|compare");
                    return;
            }
        }
    }
}

