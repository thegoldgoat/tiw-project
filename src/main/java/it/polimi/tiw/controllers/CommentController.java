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
    protected CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        commentDAO = new CommentDAO(connection);
    }

    protected boolean tryAddComment(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int userId;
        int imageId;
        try {
            userId = (int) req.getSession().getAttribute("UserPk");
            imageId = Integer.parseInt(req.getParameter("imagePk"));
        } catch (Exception e) {
            ControllerUtils.sendBadRequest(res);
            return false;
        }

        String text = req.getParameter("text");

        if (text.length() == 0) {
            ControllerUtils.sendBadRequest(res, "Comment Must Contain Text");
            return false;
        }

        try {
            commentDAO.addComment(userId, imageId, text);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return false;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            ControllerUtils.sendNotFound(res, "User not found. (?)");
            return false;
        }

        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (this.tryAddComment(req, res)) {
            int imageId = Integer.parseInt(req.getParameter("imagePk"));
            res.sendRedirect(getRedirectURL("/image?imagePk=" + imageId));
        }
    }
}
