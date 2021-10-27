package com.example.registration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String template = getTemplate("login");
        template = replace(template, new HashMap<>());
        createResponse(response, template);
    }

    private void createResponse(HttpServletResponse response, String content) throws IOException {
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();
        out.println(content);
    }

    private String getTemplate(String htmlName) throws IOException {
        InputStream stream = getClass().getResourceAsStream("/templates/%s.html".formatted(htmlName));
        byte[] bytes = stream.readAllBytes();
        String template = new String(bytes);
        return template;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> replacements = new HashMap<>();

        String username = validate(req,
                                   replacements,
                                   "username",
                                   "usernameError",
                                   "Invalid username",
                                   u -> u == null || u.length() < 3);

        String password = validate(req,
                                   replacements,
                                   "password",
                                   "passwordError",
                                   "Invalid password",
                                   p -> p == null || p.length() < 4);

        if (replacements.size() > 0) {
            String html = getTemplate("login");
            html = replace(html, replacements);
            createResponse(resp, html);
        } else {
            resp.sendRedirect("/welcome");
        }
    }

    private String replace(String html, Map<String, Object> replacements) {
        for (String key : replacements.keySet()) {
            Object value = replacements.get(key);
            if (value == null) {
                value = "";
            }
            html = html.replace("#{" + key + "}", value.toString());
        }
        int from = html.indexOf("#{");
        while (from >= 0) {
            int to = html.indexOf("}", from);
            html = html.substring(0, from) + html.substring(to + 1);
            from = html.indexOf("#{", from);
        }
        return html;
    }

    private String validate(HttpServletRequest req,
                            Map<String, Object> replacements,
                            String parameterName,
                            String errorParameter,
                            String errorMessage,
                            Predicate<String> errorCondition) {
        String parameterValue = req.getParameter(parameterName);
        if (errorCondition.test(parameterValue)) {
            replacements.put(errorParameter, errorMessage);
        }
        return parameterValue;
    }
}
