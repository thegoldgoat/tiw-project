package it.polimi.tiw.controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tiw.beans.Comment;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.controllers.ImageController;
import it.polimi.tiw.dao.CommentDAO;
import it.polimi.tiw.dao.ImageDAO;
import it.polimi.tiw.exceptions.ImageNotFoundException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/image")
public class APIImageController extends BaseServlet {
    private ImageDAO imageDAO;
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        imageDAO = new ImageDAO(connection);
        commentDAO = new CommentDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ImageController.ImageAndComments imageAndComments = ImageController.getImageAndComment(req, res, imageDAO, commentDAO);

        Gson gson = new GsonBuilder().create();

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(gson.toJson(imageAndComments));

    }


}
