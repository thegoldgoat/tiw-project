package it.polimi.tiw.controllers.api;

import it.polimi.tiw.controllers.CommentController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/comment")
public class APICommentController extends CommentController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (tryAddComment(req, res)) {
            res.setStatus(200);
        }
    }
}
