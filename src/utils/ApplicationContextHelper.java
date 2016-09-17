package utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext appCtx;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		appCtx = arg0;

	}

	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}
}
