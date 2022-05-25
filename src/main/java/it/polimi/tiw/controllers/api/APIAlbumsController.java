package it.polimi.tiw.controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.beans.AllAlbums;
import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/albums")
public class APIAlbumsController extends BaseServlet {

    private AlbumDAO albumDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        albumDAO = new AlbumDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute("UserPk");
        AllAlbums allAlbums;
        try {
            allAlbums = albumDAO.getAllAlbums(userId);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res, "Unable to get albums");
            return;
        }

        Gson gson = new GsonBuilder().create();


        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(gson.toJson(allAlbums));
    }
}
