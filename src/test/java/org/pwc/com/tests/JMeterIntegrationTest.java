package org.pwc.com.tests;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.pwc.com.service.JMeterTestPlanService;
import org.pwc.com.model.TestConfig;
import org.apache.jmeter.util.JMeterUtils;
import java.io.File;

@SpringBootTest(classes = org.pwc.com.Application.class)
public class JMeterIntegrationTest {

    @Autowired
    private JMeterTestPlanService jMeterTestPlanService;

    private static final String JMETER_HOME = System.getenv("JMETER_HOME");


    @BeforeAll
    public static void setup() {
        if (JMETER_HOME == null || JMETER_HOME.isEmpty()) {
            throw new RuntimeException("JMETER_HOME environment variable is not set");
        }

        // JMeter özellikleri yükle
        File jmeterProperties = new File(JMETER_HOME + "/bin/jmeter.properties");
        if (!jmeterProperties.exists()) {
            throw new RuntimeException("JMeter properties file not found at: " + jmeterProperties.getAbsolutePath());
        }

        JMeterUtils.loadJMeterProperties(jmeterProperties.getAbsolutePath());
        JMeterUtils.setJMeterHome(JMETER_HOME);
        JMeterUtils.initLocale();
        new File("test-results").mkdirs();

    }

    @Test
    public void testCategoryScenarios() {
        TestConfig config = new TestConfig();
        config.setTestName("N11 Search Test");
        config.setDomain("www.n11.com");
        config.setPath("/bilgisayar/bilgisayar-bilesenleri");
        config.setProtocol("https");
        config.setThreads(1);
        config.setRampUp(1);
        config.setDuration(5);
        config.setMethod("GET");

        jMeterTestPlanService.createAndRunTestPlan(config);
    }

    @Test
    public void testSearchScenarios() {
        TestConfig config1 = new TestConfig("Basic Search Test", "/arama?q=laptop", 60);
        jMeterTestPlanService.createAndRunTestPlan(config1);

        TestConfig config2 = new TestConfig("Special Character Search Test", "/arama?q=gaming+laptop", 60);
        jMeterTestPlanService.createAndRunTestPlan(config2);

        TestConfig config3 = new TestConfig("Empty Search Test", "/arama?q=", 30);
        jMeterTestPlanService.createAndRunTestPlan(config3);
    }


}