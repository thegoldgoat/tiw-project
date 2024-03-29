package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.AllAlbums;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.utils.ControllerUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/home")
public class HomeController extends BaseTemplateServlet {
    private static final String templatePath = "WEB-INF/home.html";
    AlbumDAO albumDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        albumDAO = new AlbumDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int userId = (int) req.getSession().getAttribute("UserPk");

        AllAlbums allAlbums;
        try {
            allAlbums = albumDAO.getAllAlbums(userId);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res, "Unable to get albums");
            return;
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = createWebContext(req, res);
        ctx.setVariable("userAlbums", allAlbums.getUserAlbums());
        ctx.setVariable("otherUsersAlbums", allAlbums.getOtherUserAlbums());

        processTemplate(templatePath, templateEngine, ctx, res);
    }
}
