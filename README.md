# crawler
Mini project, crawl and compare to other site. (refactoring integration verifyer). 

To run tests, a local site is needed with some specific paths/files. Disable integration tests or run with -DskipTests

rawler: site crawler and diff generator

needs:
mysql username/password/database

CRAWL_FOLDER : work area for crawler
CRAWL_OUTPUT : each crawled site is stored here for comparison
CRAWL_COMPARE: each compared site is stored here for comparison
CRAWL_DIFF   : diff between output and compare , when there is a difference

CRAWL_STARTPAGE : first page to start crawling from
CRAWL_SITE_BASE : url to use as a base. 
                        e.g.   
                        www.domain.com/a/b/c   
                        this would mean only sites under c (so not a or a/b) will be crawled
CRAWL_COMP_BASE : url to use as a base for comparison.


internally each page is stripped of any SRC= ACTION= LINK= reference to ensure 
no variable content or changed url is used for comparison. 
The page is stored and a database table is filled with the page url + hash of the page

usage: runcrawl.sh  , runcompare.sh

crawl should be done before compare. both crawl and compare can be rerun multiple times.
