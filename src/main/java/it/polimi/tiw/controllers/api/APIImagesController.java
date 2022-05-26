package it.polimi.tiw.controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.beans.AllImages;
import it.polimi.tiw.controllers.AlbumController;
import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.exceptions.AlbumNotFoundException;
import it.polimi.tiw.exceptions.InvalidOperationException;
import it.polimi.tiw.exceptions.PageOutOfBoundException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/album")
public class APIImagesController extends BaseServlet {

    AlbumDAO albumDAO;

    @Override
    public void init() throws ServletException {
        super.init();

        albumDAO = new AlbumDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        AllImages allImages;
        try {
            allImages = AlbumController.getAllImagesFromReq(req, albumDAO);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (AlbumNotFoundException e) {
            ControllerUtils.sendBadRequest(res, "Album does not exist");
            return;
        } catch (PageOutOfBoundException e) {
            ControllerUtils.sendBadRequest(res, "Invalid page");
            return;
        } catch (InvalidOperationException e) {
            ControllerUtils.sendBadRequest(res, e.getMessage());
            return;
        }

        Gson gson = new GsonBuilder().create();

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(gson.toJson(allImages));
    }
}
