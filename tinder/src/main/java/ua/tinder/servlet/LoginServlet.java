package ua.tinder.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ua.tinder.config.FreeMarkerConfig;
import ua.tinder.dao.UserDao;
import ua.tinder.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao;
    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        this.userDao = new UserDao();
        this.cfg = (Configuration) getServletContext().getAttribute(FreeMarkerConfig.TEMPLATE_CONFIG);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Template template = cfg.getTemplate("login.ftl");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            template.process(new HashMap<>(), resp.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Optional<User> userOpt = userDao.getByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            HttpSession session = req.getSession();
            session.setAttribute("user", userOpt.get());
            Cookie cookie = new Cookie("userId", String.valueOf(userOpt.get().getId()));
            cookie.setMaxAge(60 * 60 * 24 * 30);
            resp.addCookie(cookie);
            resp.sendRedirect("/users");
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("error", "Невірний email або пароль");
            Template template = cfg.getTemplate("login.ftl");
            resp.setContentType("text/html; charset=UTF-8");
            try {
                template.process(data, resp.getWriter());
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }
}