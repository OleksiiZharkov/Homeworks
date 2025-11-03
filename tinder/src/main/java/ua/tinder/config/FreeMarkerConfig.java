package ua.tinder.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;

@WebListener
public class FreeMarkerConfig implements ServletContextListener {

    public static final String TEMPLATE_CONFIG = "freemarkerConfig";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

        try {
            String templateDir = context.getRealPath("/WEB-INF/templates");
            cfg.setDirectoryForTemplateLoading(new File(templateDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not set template directory", e);
        }

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);

        context.setAttribute(TEMPLATE_CONFIG, cfg);
    }
}