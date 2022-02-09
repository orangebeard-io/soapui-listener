package io.orangebeard.listener.reporter;

import com.eviware.soapui.model.TestPropertyHolder;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrangebeardSoapUiRunReporterTest {

    @Mock
    private TestStepResult result;

    @Mock
    private TestCaseRunContext context;

    @Mock
    private TestStep step;

    @Mock
    private TestPropertyHolder project;


    @Test
    public void when_a_test_fails_the_errors_are_extracted_from_step_results() {
        when(project.getPropertyNames()).thenReturn(new String[] {});

        OrangebeardSoapUiRunReporter reporter = Mockito.spy(new OrangebeardSoapUiRunReporter(project));

        when(result.getTestStep()).thenReturn(step);
        when(step.getId()).thenReturn("testStepId");
        when(result.getMessages()).thenReturn(new String[] {});
        when(result.getStatus()).thenReturn(TestStepResult.TestStepStatus.FAILED);

        reporter.finishStep(result, context);

        verify(reporter, times(1)).getErrorsFromStepResult(result);

    }
}