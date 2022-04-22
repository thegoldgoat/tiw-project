package it.polimi.tiw.controllers;

import it.polimi.tiw.beans.Image;
import it.polimi.tiw.dao.ImageDAO;
import it.polimi.tiw.exceptions.ImageNotFoundException;
import it.polimi.tiw.utils.ControllerUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

@WebServlet("/imageraw")
public class ImageRawController extends BaseServlet {
    ImageDAO imageDAO;

    String imagesBasePath;

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext context = getServletContext();
        imagesBasePath = context.getInitParameter("imagesBasePath");

        imageDAO = new ImageDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int imageId;
        try {
            imageId = Integer.parseInt(req.getParameter("imageId"));
        } catch (NumberFormatException e) {
            ControllerUtils.sendBadRequest(res, "Missing image ID in request");
            return;
        }

        Image image;
        try {
            image = imageDAO.getImage(imageId);
        } catch (SQLException e) {
            ControllerUtils.sendBadGateway(res);
            return;
        } catch (ImageNotFoundException e) {
            ControllerUtils.sendNotFound(res, "Image not found");
            return;
        }

        try {
            File inputFile = new File(imagesBasePath + image.getPath());
            FileInputStream fileStream = new FileInputStream(inputFile);

            OutputStream os = res.getOutputStream();

            res.setContentType("image/image");

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            fileStream.close();
        } catch (IOException e) {
            ControllerUtils.sendBadGateway(res, "Error while retrieving file");
        }
    }
}
