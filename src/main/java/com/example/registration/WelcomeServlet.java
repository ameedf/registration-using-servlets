package com.example.registration;

import com.example.registration.users.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "welcomeServlet", value = "/private/welcome")
public class WelcomeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
        } else {
            User user = (User)session.getAttribute("user");
            Map<String, Object> replacements = Map.of("username", user.getUsername());
            createHtmlResponseFromTemplate(response, "welcome", replacements);
        }
    }
}
