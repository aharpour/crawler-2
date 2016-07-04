package nl.uva.hippo.crawler;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class Hasher {
    public String hashBytes(byte[] bytes) {
        return DigestUtils.sha1Hex(bytes);
    }
}
