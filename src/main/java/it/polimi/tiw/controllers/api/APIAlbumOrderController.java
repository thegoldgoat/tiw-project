package it.polimi.tiw.controllers.api;

import it.polimi.tiw.controllers.BaseServlet;
import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.exceptions.InvalidOperationException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/albumOrder")
public class APIAlbumOrderController extends BaseServlet {

    private AlbumDAO albumDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        albumDAO = new AlbumDAO(connection);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String rawOrder = req.getParameter("order");
        if (rawOrder == null) {
            ControllerUtils.sendBadRequest(res, "Missing order.");
            return;
        }

        String[] ordersString = rawOrder.split(",");
        List<Integer> orders = new ArrayList<>(ordersString.length);
        for (String order : ordersString) {
            try {
                orders.add(Integer.parseInt(order));
            } catch (NumberFormatException e) {
                ControllerUtils.sendBadRequest(res, "Invalid order number: " + order);
                return;
            }
        }

        int userId = (int) req.getSession().getAttribute("UserPk");
        try {
            albumDAO.reorderAlbum(userId, orders);
            res.setStatus(200);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
        } catch (InvalidOperationException e) {
            ControllerUtils.sendBadGateway(res, e.getMessage());
        }
    }
}
