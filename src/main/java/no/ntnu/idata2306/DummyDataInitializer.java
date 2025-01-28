package no.ntnu.idata2306;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Responsible for populating database with dummy data for testing
 */
@Slf4j
@Component
public class DummyDataInitializer implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("Importing test data...");


    }
}
