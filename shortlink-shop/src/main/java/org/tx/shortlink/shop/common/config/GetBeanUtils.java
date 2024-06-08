package org.tx.shortlink.shop.common.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GetBeanUtils implements ApplicationContextAware {
    private static ApplicationContext context;
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return context;
    }
    /**
     * 根据类名
     */
    public static Object getBeans(String name) {
    	ApplicationContext applicationContext = getApplicationContext();
        return applicationContext.getBean(name);
    }
    
    /**
     * 根据class
     */
    public static <T> T getBean(Class<T> tClass){
    	return context.getBean(tClass);
    }
}