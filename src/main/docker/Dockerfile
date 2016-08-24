FROM isuper/java-oracle:jdk_7
VOLUME ["/crawler_work", "/crawler_result", "/crawler_compare_result", "/crawler_diff"]

ENV crawler.resultsFolder=/crawler_result/ \
 crawler.storageFolder=/crawler_work/ \
 crawler.compareResultsFolder=/crawler_compare_result/ \
 crawler.compareDiffFolder=/crawler_diff/
ADD javacrawler.jar app.jar
CMD ["java","-jar","/app.jar"]