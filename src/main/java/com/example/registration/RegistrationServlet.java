package com.example.registration;

import com.example.registration.users.User;
import com.example.registration.users.UserDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "registrationServlet", value = "/register")
public class RegistrationServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        createHtmlResponseFromTemplate(response, "registration", new HashMap<>());
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

        String password1 = validate(req,
                                    replacements,
                                    "password1",
                                    "passwordError",
                                    "Invalid password",
                                    p -> p != null && p.length() >= 4);
        validate(req,
                 replacements,
                 "password2",
                 "passwordsMismatchError",
                 "Passwords don't match",
                 p -> p != null && p.equals(password1));
        String age = validate(req,
                              replacements,
                              "age",
                              "ageError",
                              "Invalid age",
                              this::isValidAge);

        if (replacements.size() > 0) {
            createHtmlResponseFromTemplate(resp, "registration", replacements);
        } else {
            User user = new User(username, password1, Integer.valueOf(age));
            UserDao userDao = getUserDao();
            userDao.addUser(user);
            resp.sendRedirect("/login");
        }
    }

    private boolean isValidAge(String ageString) {
        try {
            int result = Integer.parseInt(ageString);
            return result >= 18 && result <= 120;
        } catch (Exception e) {
            return false;
        }
    }

}
