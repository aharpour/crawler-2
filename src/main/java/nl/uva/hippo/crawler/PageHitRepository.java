package nl.uva.hippo.crawler;

import nl.uva.hippo.crawler.entity.PageHit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageHitRepository extends CrudRepository<PageHit, String> {
    List<PageHit> findByUrl(String url);
}
