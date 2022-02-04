package io.orangebeard.listeners.reporter;

import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.model.testsuite.TestSuiteRunContext;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;

public interface SoapUiReporter {

    void start();
    void finish(TestRunner.Status status);
    void startSuite(TestSuite suite);
    void finishSuite(TestSuiteRunner suiteInfo);
    void startTestCase(TestCase test, TestSuiteRunContext context);
    void finishTestCase(TestCaseRunner runner, TestSuiteRunContext context);
    void startStep(TestStep step, TestCaseRunContext context);
    void finishStep(TestStepResult stepResult, TestCaseRunContext context);

    SoapUiReporter FAKE_ORANGEBEARD = new SoapUiReporter() {
        @Override
        public void start() {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void finish(TestRunner.Status status) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void startSuite(TestSuite suite) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void finishSuite(TestSuiteRunner suiteInfo) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void startTestCase(TestCase test, TestSuiteRunContext context) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void finishTestCase(TestCaseRunner runner, TestSuiteRunContext context) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void startStep(TestStep step, TestCaseRunContext context) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }

        @Override
        public void finishStep(TestStepResult stepResult, TestCaseRunContext context) {
            // intentionally left empty to prevent breaking test runs when there is an issue with orangebeard
        }
    };

}
