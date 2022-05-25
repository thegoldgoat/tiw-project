package it.polimi.tiw.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerUtils {
    public static boolean stringInvalid(String val) {
        return val == null || val.isEmpty();
    }

    private static void send(HttpServletResponse res, int status, String msg) throws IOException {
        res.setStatus(status);
        var writer = res.getWriter();

        writer.print(msg);
    }

    private static void send(HttpServletResponse res, int status) throws IOException {
        send(res, status, "");
    }

    public static void sendBadRequest(HttpServletResponse res, String msg) throws IOException {
        send(res, HttpServletResponse.SC_BAD_REQUEST, msg);
    }

    public static void sendBadRequest(HttpServletResponse res) throws IOException {
        send(res, HttpServletResponse.SC_BAD_REQUEST);
    }

    public static void sendBadGateway(HttpServletResponse res, String msg) throws IOException {
        send(res, HttpServletResponse.SC_BAD_GATEWAY, msg);
    }

    public static void sendBadGateway(HttpServletResponse res) throws IOException {
        send(res, HttpServletResponse.SC_BAD_GATEWAY);
    }

    public static void sendUnauthorized(HttpServletResponse res, String msg) throws IOException {
        send(res, HttpServletResponse.SC_UNAUTHORIZED, msg);
    }

    public static void sendUnauthorized(HttpServletResponse res) throws IOException {
        send(res, HttpServletResponse.SC_UNAUTHORIZED);
    }

    public static void sendNotFound(HttpServletResponse res, String msg) throws IOException {
        send(res, HttpServletResponse.SC_NOT_FOUND, msg);
    }

    public static void sendNotFound(HttpServletResponse res) throws IOException {
        send(res, HttpServletResponse.SC_NOT_FOUND);
    }
}
