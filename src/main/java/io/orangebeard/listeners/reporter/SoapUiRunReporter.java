package io.orangebeard.listeners.reporter;

import com.eviware.soapui.model.TestPropertyHolder;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.model.testsuite.TestSuiteRunContext;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import com.google.common.base.Strings;
import io.orangebeard.client.OrangebeardClient;
import io.orangebeard.client.OrangebeardProperties;
import io.orangebeard.client.OrangebeardV2Client;
import io.orangebeard.client.entity.FinishTestItem;
import io.orangebeard.client.entity.FinishTestRun;
import io.orangebeard.client.entity.Log;
import io.orangebeard.client.entity.LogLevel;
import io.orangebeard.client.entity.StartTestItem;
import io.orangebeard.client.entity.StartTestRun;
import io.orangebeard.client.entity.Status;
import io.orangebeard.client.entity.TestItemType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Character.LINE_SEPARATOR;

public class SoapUiRunReporter implements SoapUiReporter {
    private final OrangebeardClient orangebeardClient;
    private final OrangebeardProperties properties;
    private UUID testRunUUID;

    private final Map<String, UUID> testItemMap = new HashMap<>();
    private final Map<String, UUID> suiteMap = new HashMap<>();

    public SoapUiRunReporter(TestPropertyHolder soapUiProperties) {
        for (String propertyName : soapUiProperties.getPropertyNames()){
            //set soapui project properties as system properties
            System.setProperty(propertyName, soapUiProperties.getPropertyValue(propertyName));
        }

        this.properties = new OrangebeardProperties();
        this.orangebeardClient = new OrangebeardV2Client(
                properties.getEndpoint(),
                properties.getAccessToken(),
                properties.getProjectName(),
                properties.requiredValuesArePresent());
    }

    @Override
    public void start() {
        this.testRunUUID = orangebeardClient.startTestRun(new StartTestRun(properties.getTestSetName(),
                properties.getDescription(), properties.getAttributes()));
    }

    @Override
    public void finish(TestRunner.Status status) {
        orangebeardClient.finishTestRun(testRunUUID, new FinishTestRun(mapStatus(status)));
    }

    @Override
    public void startSuite(TestSuite suite) {
        UUID suiteUUID = orangebeardClient.startTestItem(null, new StartTestItem(testRunUUID, suite.getName(),
                TestItemType.SUITE));
        suiteMap.put(suite.getId(), suiteUUID);
    }

    @Override
    public void finishSuite(TestSuiteRunner suiteInfo) {
        orangebeardClient.finishTestItem(suiteMap.get(suiteInfo.getTestSuite().getId()), new FinishTestItem(testRunUUID,
                mapStatus(suiteInfo.getStatus())));
    }

    @Override
    public void startTestCase(TestCase test, TestSuiteRunContext context) {
        UUID testUUID = orangebeardClient.startTestItem(suiteMap.get(test.getTestSuite().getId()),
                new StartTestItem(testRunUUID, test.getName(), TestItemType.TEST));
        testItemMap.put(test.getId(), testUUID);
    }

    @Override
    public void finishTestCase(TestCaseRunner runner, TestSuiteRunContext context) {
        orangebeardClient.finishTestItem(testItemMap.get(runner.getTestCase().getId()), new FinishTestItem(testRunUUID,
                mapStatus(runner.getStatus())));
    }

    @Override
    public void startStep(TestStep step, TestCaseRunContext context) {
        UUID testUUID = orangebeardClient.startTestItem(testItemMap.get(step.getTestCase().getId()),
                new StartTestItem(testRunUUID, step.getName(), TestItemType.STEP));
        testItemMap.put(step.getId(), testUUID);
    }

    @Override
    public void finishStep(TestStepResult stepResult, TestCaseRunContext context) {
        UUID stepUUID = testItemMap.get(stepResult.getTestStep().getId());
        String stepLogData = getLogDataFromStepResult(stepResult);
        if (!Strings.isNullOrEmpty(stepLogData)) {
            orangebeardClient.log(new Log(testRunUUID, stepUUID, LogLevel.info, stepLogData));
        }

        if(mapStepStatus(stepResult.getStatus()) == Status.FAILED) {
                orangebeardClient.log(new Log(testRunUUID, stepUUID, LogLevel.error, getErrorsFromStepResult(stepResult)));
        }

        orangebeardClient.finishTestItem(stepUUID, new FinishTestItem(testRunUUID, mapStepStatus(stepResult.getStatus())));
    }

    protected String getLogDataFromStepResult(TestStepResult result) {
        final StringWriter logData = new StringWriter();
        PrintWriter logWriter = new PrintWriter(logData);
        result.writeTo(logWriter);
        return logData.toString();
    }

    protected String getErrorsFromStepResult(TestStepResult result) {
        String message;
        if (result.getError() != null) {
            message = "Exception: " + result.getError().getMessage() + LINE_SEPARATOR
                    + formatStacktrace(result.getError());
        } else {
            StringBuilder messages = new StringBuilder();
            for (String messageLog : result.getMessages()) {
                messages.append(messageLog);
                messages.append(LINE_SEPARATOR);
            }
            message = messages.toString();
        }
        return message;
    }

    protected String formatStacktrace(Throwable e) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            result.append(e.getStackTrace()[i]);
            result.append(LINE_SEPARATOR);
        }
        return result.toString();
    }

    private Status mapStatus(TestRunner.Status status) {
        switch (status) {
            case FAILED:
                return Status.FAILED;
            case CANCELED:
                return Status.SKIPPED;
            default:
                return Status.PASSED;
        }
    }

    private Status mapStepStatus(TestStepResult.TestStepStatus status) {
        switch (status) {
            case FAILED:
                return Status.FAILED;
            case CANCELED:
                return Status.SKIPPED;
            default:
                return Status.PASSED;
        }
    }
}
