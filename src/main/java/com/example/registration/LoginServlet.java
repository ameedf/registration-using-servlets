package com.example.registration;

import com.example.registration.users.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        createHtmlResponseFromTemplate(response, "login", new HashMap<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> replacements = new HashMap<>();

        String username = validate(req,
                                   replacements,
                                   "username",
                                   "usernameError",
                                   "Invalid username",
                                   u -> u != null && u.length() >= 3);

        String password = validate(req,
                                   replacements,
                                   "password",
                                   "passwordError",
                                   "Invalid password",
                                   p -> p != null && p.length() >= 4);

        if (replacements.size() > 0) {
            createHtmlResponseFromTemplate(resp, "login", replacements);
        } else {
            User user = getUserDao().findByName(username);
            if (user == null || !password.equals(user.getPassword())) {
                replacements.put("credentialsError", "Invalid username or password");
                createHtmlResponseFromTemplate(resp, "login", replacements);
            } else {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect("/private/welcome");
            }
        }
    }
}
