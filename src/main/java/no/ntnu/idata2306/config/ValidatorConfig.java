package no.ntnu.idata2306.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for setting up the Validator.
 * This configuration ensures that all validation errors are collected and reported.
 */
@Configuration
public class ValidatorConfig {

    /**
     * Configures the LocalValidatorFactoryBean to collect all validation errors.
     *
     * @return the configured LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "false");
        return validatorFactoryBean;
    }
}
