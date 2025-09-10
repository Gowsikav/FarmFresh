package com.xworkz.farmfresh.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class FarmFreshInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public FarmFreshInitializer()
    {
        System.out.println("FarmFreshInitializer constructor");
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{FarmFreshConfiguration.class};
    }

}
