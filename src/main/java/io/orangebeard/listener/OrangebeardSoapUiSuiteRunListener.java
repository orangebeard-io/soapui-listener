package io.orangebeard.listener;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestSuiteRunContext;
import com.eviware.soapui.model.testsuite.TestSuiteRunListener;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import io.orangebeard.listener.reporter.OrangebeardReporter;
import io.orangebeard.listener.reporter.OrangebeardSoapUiRunReporter;

public class OrangebeardSoapUiSuiteRunListener implements TestSuiteRunListener {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    private static final String SINGLE_SUITE = "single_suite";
    private OrangebeardReporter orangebeardReporter;

    @Override
    public void beforeRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
       orangebeardReporter = (OrangebeardSoapUiRunReporter) runContext.getProperty(ORANGEBEARD_PROPERTY);
       if(orangebeardReporter == null) {
           try {
               orangebeardReporter = new OrangebeardSoapUiRunReporter(runContext.getTestSuite().getProject());
               orangebeardReporter.start();
               orangebeardReporter.startSuite(runContext.getTestSuite());
           } catch (Exception e) {
               SoapUI.log(e.getMessage());
               orangebeardReporter = OrangebeardReporter.FAKE_ORANGEBEARD;
           }
           runContext.setProperty(SINGLE_SUITE, true);
           runContext.setProperty(ORANGEBEARD_PROPERTY, orangebeardReporter);
       }
    }

    @Override
    public void afterRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
        if (runContext.getProperty(SINGLE_SUITE) != null) {
            orangebeardReporter.finishSuite(testRunner);
            orangebeardReporter.finish(testRunner.getStatus());
        }
    }

    @Override
    public void beforeTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCase testCase) {
        orangebeardReporter.startTestCase(testCase, runContext);
    }

    @Override
    public void afterTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCaseRunner testCaseRunner) {
        orangebeardReporter.finishTestCase(testCaseRunner, runContext);
    }
}
