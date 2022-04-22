package it.polimi.tiw.controllers;

import it.polimi.tiw.dao.CommentDAO;
import it.polimi.tiw.exceptions.UserNotFoundException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/comment")
public class CommentController extends BaseServlet {
    CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        commentDAO = new CommentDAO(connection);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int userId;
        int imageId;
        try {
            userId = (int) req.getSession().getAttribute("UserPk");
            imageId = Integer.parseInt(req.getParameter("imagePk"));
        } catch (Exception e) {
            ControllerUtils.sendBadGateway(res);
            return;
        }

        String text = req.getParameter("text");

        try {
            commentDAO.addComment(userId, imageId, text);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            ControllerUtils.sendNotFound(res, "User not found. (?)");
            return;
        }

        res.sendRedirect(getRedirectURL("/image?imagePk=" + imageId));
    }
}
