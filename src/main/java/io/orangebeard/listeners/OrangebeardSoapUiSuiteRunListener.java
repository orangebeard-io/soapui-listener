package io.orangebeard.listeners;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.support.TestSuiteRunListenerAdapter;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestSuiteRunContext;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import io.orangebeard.listeners.reporter.SoapUiReporter;
import io.orangebeard.listeners.reporter.SoapUiRunReporter;

public class OrangebeardSoapUiSuiteRunListener extends TestSuiteRunListenerAdapter {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    private static final String SINGLE_SUITE = "single_suite";
    SoapUiReporter orangebeardTestRun;

    @Override
    public void beforeRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
       orangebeardTestRun = (SoapUiRunReporter) runContext.getProperty(ORANGEBEARD_PROPERTY);
       if(orangebeardTestRun == null) {
           try {
               orangebeardTestRun = new SoapUiRunReporter(runContext.getTestSuite().getProject());
               orangebeardTestRun.start();
               orangebeardTestRun.startSuite(runContext.getTestSuite());
           } catch (Exception e) {
               SoapUI.logError(e, e.getMessage());
               orangebeardTestRun = SoapUiReporter.FAKE_ORANGEBEARD;
           }
           runContext.setProperty(SINGLE_SUITE, true);
       }
    }

    @Override
    public void afterRun(TestSuiteRunner testRunner, TestSuiteRunContext runContext) {
        if (runContext.getProperty(SINGLE_SUITE) != null) {
            orangebeardTestRun.finishSuite(testRunner);
            orangebeardTestRun.finish(testRunner.getStatus());
        }
    }

    @Override
    public void beforeTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCase testCase) {
        orangebeardTestRun.startTestCase(testCase, runContext);
    }

    @Override
    public void afterTestCase(TestSuiteRunner testRunner, TestSuiteRunContext runContext, TestCaseRunner testCaseRunner) {
        orangebeardTestRun.finishTestCase(testCaseRunner, runContext);
    }
}
