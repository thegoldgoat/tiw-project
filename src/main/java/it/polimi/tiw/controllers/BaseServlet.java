package it.polimi.tiw.controllers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseServlet extends HttpServlet {

    protected Connection connection;

    private String servletContextPath;

    @Override
    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }

        servletContextPath = getServletContext().getContextPath();

        extended_init();
    }

    protected void extended_init() {}

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        extended_destroy();
    }

    protected void extended_destroy() {}

    /**
     * @param suffixPath suffix of the path you are trying to generate
     * @return the full path, containing the servletContextPath prefix + your suffix
     */
    protected String getRedirectURL(String suffixPath) {
        return servletContextPath + suffixPath;
    }
}
