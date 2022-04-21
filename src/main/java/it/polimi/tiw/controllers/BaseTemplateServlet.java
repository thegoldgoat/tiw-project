package it.polimi.tiw.controllers;

import org.thymeleaf.TemplateEngine;

public abstract class BaseTemplateServlet extends BaseServlet {
    protected TemplateEngine templateEngine;

    @Override
    protected void extended_init() {
        templateEngine = getTemplateEngine();
        extended_init_template();
    }

    protected void extended_init_template() {}
}
