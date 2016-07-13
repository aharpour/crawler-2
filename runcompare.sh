CRAWL_FOLDER=/var/projects/crawler
CRAWL_OUTPUT=/var/projects/crawler_output
CRAWL_COMPARE=/var/projects/crawler_compare
CRAWL_DIFF=/var/projects/crawler_diff

rm -r $CRAWL_COMPARE/*;
rm -r $CRAWL_DIFF/*;

java -jar   -Dcrawler.storageFolder=$CRAWL_FOLDER \
            -Dcrawler.resultsFolder=$CRAWL_OUTPUT \
            -Dcrawler.compareFolder=$CRAWL_COMPARE \
            -Dcrawler.compareDiffFolder=$CRAWL_DIFF \
            -Dcrawler.startPage=http://sys4.open-web.nl:8880/site/ \
            -Dcrawler.siteBaseUrl=http://sys4.open-web.nl:8880/site/ \
            -Dcrawler.comparePath=http://sys4.open-web.nl:8880/site/ \
            -Dcrawler.compareResultsFolder=/var/projects/crawler_compare \
            -Dspring.datasource.url=jdbc:mysql://127.0.0.1/crawler \
            -Dspring.datasource.username=crawlerUsername \
            -Dspring.datasource.password=crawlerPassword \
            -Dspring.datasource.driver-class-name=com.mysql.jdbc.Driver \
    target/javacrawler.jar compare
