package it.polimi.tiw.controllers;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

public abstract class BaseTemplateServlet extends BaseServlet {
    protected TemplateEngine templateEngine;

    @Override
    protected void extended_init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver =
                new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        extended_init_template();
    }

    protected void extended_init_template() {}
}
