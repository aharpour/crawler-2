FROM isuper/java-oracle:jdk_7
VOLUME /crawler_work
VOLUME /crawler_result
ENV crawler.resultsFolder /crawler_result/
ENV crawler.storageFolder /crawler_work/
ADD javacrawler.jar app.jar
CMD ["java","-jar","/app.jar"]