package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.Comment;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.dao.CommentDAO;
import it.polimi.tiw.dao.ImageDAO;
import it.polimi.tiw.exceptions.ImageNotFoundException;
import it.polimi.tiw.utils.ControllerUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/image")
public class ImageController extends BaseTemplateServlet {
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
        int imageId;
        try {
            imageId = Integer.parseInt(req.getParameter("imagePk"));
        } catch (NumberFormatException e) {
            ControllerUtils.sendBadRequest(res, "Missing imageId");
            return;
        }

        Image image;
        try {
            image = imageDAO.getImage(imageId);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (ImageNotFoundException e) {
            ControllerUtils.sendNotFound(res, e.getMessage());
            return;
        }

        List<Comment> comments;
        try {
            comments = commentDAO.getComments(imageId);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        }

        WebContext ctx = createWebContext(req, res);

        ctx.setVariable("image", image);
        ctx.setVariable("comments", comments);

        processTemplate("WEB-INF/image.html", templateEngine, ctx, res);
    }
}
