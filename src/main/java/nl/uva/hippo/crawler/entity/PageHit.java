package nl.uva.hippo.crawler.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PageHit {
    @Id
    private String url;
    private String hash;

    protected PageHit() {

    }

    public PageHit(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
