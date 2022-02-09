package io.orangebeard.listener;

import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunListener;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import io.orangebeard.listener.reporter.OrangebeardReporter;
import io.orangebeard.listener.reporter.OrangebeardSoapUiRunReporter;

public class OrangebeardSoapUiTestRunListener implements TestRunListener {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    private OrangebeardReporter orangebeardReporter;

    @Override
    public void beforeRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        orangebeardReporter = (OrangebeardSoapUiRunReporter) runContext.getProperty(ORANGEBEARD_PROPERTY);
        if (orangebeardReporter == null) {
            orangebeardReporter = OrangebeardReporter.FAKE_ORANGEBEARD;
        }
    }

    @Override
    public void afterRun(TestCaseRunner testRunner, TestCaseRunContext runContext) {
        // should not be used
    }

    @Override
    public void beforeStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStep testStep) {
        orangebeardReporter.startStep(testStep, runContext);
    }

    @Override
    public void afterStep(TestCaseRunner testRunner, TestCaseRunContext runContext, TestStepResult result) {
        orangebeardReporter.finishStep(result, runContext);
    }

    /**
     * @deprecated use {@link #beforeStep(TestCaseRunner, TestCaseRunContext, TestStep)} instead
     */
    @Deprecated
    @Override
    public void beforeStep(TestCaseRunner testCaseRunner, TestCaseRunContext testCaseRunContext) {
        throw new UnsupportedOperationException("Deprecated method! Use beforeStep(TestCaseRunner, TestCaseRunContext, TestStep) instead.");
    }

}
