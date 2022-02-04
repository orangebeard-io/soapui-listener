package io.orangebeard.listeners;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.support.ProjectRunListenerAdapter;
import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import io.orangebeard.listeners.reporter.SoapUiReporter;
import io.orangebeard.listeners.reporter.SoapUiRunReporter;

public class OrangebeardSoapUiProjectRunListener extends ProjectRunListenerAdapter {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    SoapUiReporter orangebeardTestRun;

    @Override
    public void beforeRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
        try {
             orangebeardTestRun = new SoapUiRunReporter(runContext.getProject());
        } catch (Exception e) {
            SoapUI.logError(e, e.getMessage());
            orangebeardTestRun = SoapUiReporter.FAKE_ORANGEBEARD;
        }
        orangebeardTestRun.start();
        runContext.setProperty(ORANGEBEARD_PROPERTY, orangebeardTestRun);
    }

    @Override
    public void afterRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
        orangebeardTestRun.finish(projectRunner.getStatus());
    }

    @Override
    public void beforeTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuite testRunnable) {
        orangebeardTestRun.startSuite(testRunnable);
    }

    @Override
    public void afterTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuiteRunner testRunner) {
        orangebeardTestRun.finishSuite(testRunner);
    }
}
