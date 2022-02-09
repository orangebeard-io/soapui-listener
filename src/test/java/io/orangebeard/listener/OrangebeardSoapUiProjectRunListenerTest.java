package io.orangebeard.listener;

import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunner;
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
public class OrangebeardSoapUiProjectRunListenerTest {

    @Mock
    private ProjectRunner projectRunner;

    @Mock
    private ProjectRunContext projectRunContext;

    @InjectMocks
    private OrangebeardSoapUiProjectRunListener orangebeardSoapUiProjectRunListener;

    @Test
    void when_beforeRun_is_called_the_reporter_is_sent_to_the_runContext() {
        orangebeardSoapUiProjectRunListener.beforeRun(projectRunner, projectRunContext);
        verify(projectRunContext, times(1)).setProperty(eq("orangebeard"), any(OrangebeardReporter.class));
    }
}