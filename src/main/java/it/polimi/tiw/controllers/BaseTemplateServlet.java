package it.polimi.tiw.controllers;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;

public abstract class BaseTemplateServlet extends BaseServlet {
    protected TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        super.init();
        templateEngine = getTemplateEngine();
    }
}
