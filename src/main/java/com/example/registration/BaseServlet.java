package com.example.registration;

import com.example.registration.users.UserDao;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Predicate;

public abstract class BaseServlet extends HttpServlet {

    protected void createResponse(HttpServletResponse response, String content) throws IOException {
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();
        out.println(content);
    }

    protected String getTemplate(String htmlName) throws IOException {
        InputStream stream = getClass().getResourceAsStream("/templates/%s.html".formatted(htmlName));
        byte[] bytes = stream.readAllBytes();
        String template = new String(bytes);
        return template;
    }

    protected String replace(String html, Map<String, Object> replacements) {
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

    protected String validate(HttpServletRequest req,
                              Map<String, Object> replacements,
                              String parameterName,
                              String errorParameter,
                              String errorMessage,
                              Predicate<String> okCondition) {
        String parameterValue = req.getParameter(parameterName);
        if (!okCondition.test(parameterValue)) {
            replacements.put(errorParameter, errorMessage);
        }
        return parameterValue;
    }

    protected void createHtmlResponseFromTemplate(HttpServletResponse response, String htmlTemplateName, Map<String, Object> replacements) throws IOException {
        String template = getTemplate(htmlTemplateName);
        template = replace(template, replacements);
        createResponse(response, template);
    }

    protected UserDao getUserDao() {
        ServletContext servletContext = getServletContext();
        UserDao userDao = (UserDao)servletContext.getAttribute(UserDao.class.getName());
        if (userDao == null) {
            userDao = new UserDao();
            servletContext.setAttribute(UserDao.class.getName(), userDao);
        }
        return userDao;
    }
}
