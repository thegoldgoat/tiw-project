package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.AllImages;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.exceptions.AlbumNotFoundException;
import it.polimi.tiw.exceptions.PageOutOfBoundException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.IntStream;

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
        } catch (NumberFormatException e) {
            page = 0;
        }

        AllImages allImages;
        try {
            allImages = albumDAO.getImages(albumPk, page);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (AlbumNotFoundException e) {
            ControllerUtils.sendBadRequest(res, "Album does not exist");
            return;
        } catch (PageOutOfBoundException e) {
            // Instead of sending an error message, just redirect to the first page
            res.sendRedirect(getRedirectURL("/album?albumPk=" + albumPk));
            return;
        }

        var ctx = createWebContext(req, res);

        ctx.setVariable("images", allImages.getImages());
        ctx.setVariable("currentPage", allImages.getCurrentPage());
        ctx.setVariable("pageCount", allImages.getPageCount());
        ctx.setVariable("pageIterator", IntStream.range(0, allImages.getPageCount()).iterator());
        ctx.setVariable("albumPk", albumPk);

        processTemplate("WEB-INF/album.html", templateEngine, ctx, res);
    }
}
