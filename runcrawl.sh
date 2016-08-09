CRAWL_FOLDER=/var/projects/crawler
CRAWL_OUTPUT=/var/projects/crawler_output
mkdir -p $CRAWL_FOLDER
mkdir -p $CRAWL_OUTPUT

rm -r $CRAWL_OUTPUT/*;

java -jar   -Dcrawler.storageFolder=$CRAWL_FOLDER \
            -Dcrawler.resultsFolder=$CRAWL_OUTPUT \
            -Dcrawler.startPage=http://sys3.open-web.nl:9080/site/hippo/ \
            -Dcrawler.siteBaseUrl=http://sys3.open-web.nl:9080/site/hippo/ \
            -Dspring.datasource.url=jdbc:mysql://127.0.0.1/crawler \
            -Dspring.datasource.username=crawlerUsername \
            -Dspring.datasource.password=crawlerPassword \
            -Dspring.datasource.driver-class-name=com.mysql.jdbc.Driver \
            -Dspring.jpa.hibernate.ddl-auto=create \
    target/javacrawler.jar crawl

