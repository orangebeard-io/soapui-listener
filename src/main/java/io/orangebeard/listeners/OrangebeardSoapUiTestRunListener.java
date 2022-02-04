package io.orangebeard.listeners;

import com.eviware.soapui.model.support.TestRunListenerAdapter;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.plugins.ListenerConfiguration;
import io.orangebeard.listeners.reporter.SoapUiReporter;
import io.orangebeard.listeners.reporter.SoapUiRunReporter;

@ListenerConfiguration
public class OrangebeardSoapUiTestRunListener extends TestRunListenerAdapter {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    SoapUiReporter orangebeardTestRun;

    @Override
    public void beforeRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        orangebeardTestRun = (SoapUiRunReporter) runContext.getProperty(ORANGEBEARD_PROPERTY);
        if (orangebeardTestRun == null) {
            orangebeardTestRun = SoapUiReporter.FAKE_ORANGEBEARD;
        }
    }

    @Override
    public void afterRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        // should not be used
    }

    @Override
    public void beforeStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStep testStep) {
        orangebeardTestRun.startStep(testStep, runContext);
    }

    @Override
    public void afterStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStepResult result) {
        orangebeardTestRun.finishStep(result, runContext);
    }
}
