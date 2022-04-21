package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.Image;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.exceptions.AlbumNotExistsException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/album")
public class AlbumController extends BaseTemplateServlet {

    AlbumDAO albumDAO;

    @Override
    public void init() throws ServletException {
        super.init();

        albumDAO = new AlbumDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int albumPk;
        int page;
        try {
            albumPk = Integer.parseInt(req.getParameter("albumPk"));
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtils.sendBadRequest(res, "Error while parsing");
            return;
        }
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (Exception e) {
            page = 0;
        }

        List<Image> images;
        try {
            images = albumDAO.getImages(albumPk, page);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (AlbumNotExistsException e) {
            ControllerUtils.sendBadRequest(res, "Album does not exist");
            return;
        }

        var ctx = createWebContext(req, res);

        ctx.setVariable("images", images);

        processTemplate("WEB-INF/album.html", templateEngine, ctx, res);
    }
}
