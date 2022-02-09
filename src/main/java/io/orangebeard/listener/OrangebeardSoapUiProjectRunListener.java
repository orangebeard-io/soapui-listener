package io.orangebeard.listener;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunListener;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import io.orangebeard.listener.reporter.OrangebeardReporter;
import io.orangebeard.listener.reporter.OrangebeardSoapUiRunReporter;

public class OrangebeardSoapUiProjectRunListener implements ProjectRunListener {

    private static final String ORANGEBEARD_PROPERTY = "orangebeard";
    private OrangebeardReporter orangebeardReporter;

    @Override
    public void beforeRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
        try {
             orangebeardReporter = new OrangebeardSoapUiRunReporter(runContext.getProject());
        } catch (Exception e) {
            SoapUI.log(e.getMessage());
            orangebeardReporter = OrangebeardReporter.FAKE_ORANGEBEARD;
        }
        orangebeardReporter.start();
        runContext.setProperty(ORANGEBEARD_PROPERTY, orangebeardReporter);
    }

    @Override
    public void afterRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
        orangebeardReporter.finish(projectRunner.getStatus());
    }

    @Override
    public void beforeTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuite testRunnable) {
        orangebeardReporter.startSuite(testRunnable);
    }

    @Override
    public void afterTestSuite(ProjectRunner projectRunner, ProjectRunContext runContext, TestSuiteRunner testRunner) {
        orangebeardReporter.finishSuite(testRunner);
    }
}
