package no.ntnu.idata2306.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class provides static access to the Spring ApplicationContext.
 * It's used to resolve circular dependencies and access beans programmatically.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * Gets a bean of the specified type from the application context.
     *
     * @param beanClass the type of bean to retrieve
     * @param <T> the bean type
     * @return the bean instance
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * Gets a bean with the specified name and type from the application context.
     *
     * @param name the name of the bean
     * @param beanClass the type of bean to retrieve
     * @param <T> the bean type
     * @return the bean instance
     */
    public static <T> T getBean(String name, Class<T> beanClass) {
        return applicationContext.getBean(name, beanClass);
    }
}