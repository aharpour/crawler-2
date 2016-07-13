package nl.uva.hippo.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CrawlerApplication.class)
@TestPropertySource(locations = "classpath:successtest.properties")
public class ShowEnvVar {
    @Test
    public void testPrintEnv() {
        Properties props = System.getProperties();;
        for (Object key : props.keySet()) {
            System.out.printf("Property [%s] value [%s]\n", key.toString(), props.get(key));
        }


        Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            System.out.printf("Environment [%s] value[%s]\n", key, env.get(key));
        }
    }

}
