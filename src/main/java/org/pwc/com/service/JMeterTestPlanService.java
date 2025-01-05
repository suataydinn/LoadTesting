package org.pwc.com.service;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jorphan.collections.ListedHashTree;
import org.springframework.stereotype.Service;
import org.pwc.com.model.TestConfig;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class JMeterTestPlanService {

    private static final String RESULTS_DIR = "test-results";

    public void createAndRunTestPlan(TestConfig config) {
        try {
            StandardJMeterEngine jmeter = new StandardJMeterEngine();

            TestPlan testPlan = new TestPlan(config.getTestName());
            testPlan.setEnabled(true);
            testPlan.setUserDefinedVariables(new Arguments());

            HTTPSamplerProxy httpSampler = createHttpSampler(config);

            if (config.isUseHeader()) {
                HeaderManager headerManager = createHeaderManager();
                httpSampler.setHeaderManager(headerManager);
            }

            ThreadGroup threadGroup = createThreadGroup(config);

            ListedHashTree testPlanTree = new ListedHashTree();
            ListedHashTree threadGroupHashTree = (ListedHashTree) testPlanTree.add(testPlan, threadGroup);
            threadGroupHashTree.add(httpSampler);

            setupResultCollector(testPlanTree, config.getTestName());

            jmeter.configure(testPlanTree);
            jmeter.run();

        } catch (Exception e) {
            throw new RuntimeException("Test execution failed: " + e.getMessage(), e);
        }
    }

    private HTTPSamplerProxy createHttpSampler(TestConfig config) {
        HTTPSamplerProxy httpSampler = new HTTPSamplerProxy();
        httpSampler.setDomain(config.getDomain());
        httpSampler.setProtocol(config.getProtocol());
        httpSampler.setPath(config.getPath());
        httpSampler.setMethod(config.getMethod());

        if (config.getSearchTerm() != null && !config.getSearchTerm().isEmpty()) {
            httpSampler.addArgument("q", config.getSearchTerm());
        }

        return httpSampler;
    }

    private HeaderManager createHeaderManager() {
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"));
        headerManager.add(new Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
        headerManager.add(new Header("Accept-Language", "en-US,en;q=0.5"));
        return headerManager;
    }

    private ThreadGroup createThreadGroup(TestConfig config) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName(config.getTestName() + " Thread Group");
        threadGroup.setNumThreads(config.getThreads());
        threadGroup.setRampUp(config.getRampUp());
        threadGroup.setDuration(config.getDuration());
        threadGroup.setScheduler(true);

        LoopController loopController = new LoopController();
        loopController.setLoops(-1);
        loopController.setContinueForever(false);
        threadGroup.setSamplerController(loopController);

        return threadGroup;
    }

    private void setupResultCollector(ListedHashTree testPlanTree, String testName) {
        File resultsDir = new File(RESULTS_DIR);
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String resultFile = RESULTS_DIR + "/" + testName.replaceAll("\\s+", "_") + "_" + timestamp + ".jtl";

        Summariser summer = new Summariser("Summary");
        ResultCollector resultCollector = new ResultCollector(summer);
        resultCollector.setFilename(resultFile);
        testPlanTree.add(testPlanTree.getArray()[0], resultCollector);
    }
}
