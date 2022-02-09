package io.orangebeard.listener;

import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.model.testsuite.TestSuiteRunContext;
import com.eviware.soapui.model.testsuite.TestSuiteRunner;
import io.orangebeard.listener.reporter.OrangebeardReporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrangebeardSoapUiSuiteRunListenerTest {

    @Mock
    private TestSuiteRunner suiteRunner;

    @Mock
    private TestSuiteRunContext suiteRunContext;

    @InjectMocks
    private OrangebeardSoapUiSuiteRunListener orangebeardSoapUiSuiteRunListener;

    @Test
    void when_beforeRun_is_called_the_reporter_and_suite_only_attribute_are_sent_to_the_runContext() {
        orangebeardSoapUiSuiteRunListener.beforeRun(suiteRunner, suiteRunContext);
        verify(suiteRunContext, times(1)).setProperty(eq("orangebeard"), any(OrangebeardReporter.class));
        verify(suiteRunContext, times(1)).setProperty(eq("single_suite"), eq(true));
    }
}