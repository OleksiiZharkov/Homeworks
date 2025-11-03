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

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private UserDao userDao;
    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        this.userDao = new UserDao();
        this.cfg = (Configuration) getServletContext().getAttribute(FreeMarkerConfig.TEMPLATE_CONFIG);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        Integer lastViewedId = (Integer) session.getAttribute("lastViewedId");
        if (lastViewedId == null) {
            lastViewedId = 0;
        }

        Optional<User> nextUser = userDao.getNextUser(currentUser.getId(), lastViewedId);

        if (nextUser.isPresent()) {
            session.setAttribute("lastViewedId", nextUser.get().getId());
            Map<String, Object> data = new HashMap<>();
            data.put("profile", nextUser.get());
            Template template = cfg.getTemplate("users.ftl");
            resp.setContentType("text/html; charset=UTF-8");
            try {
                template.process(data, resp.getWriter());
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        } else {
            session.setAttribute("lastViewedId", 0);
            resp.sendRedirect("/liked");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String choice = req.getParameter("choice");
        int profileId = Integer.parseInt(req.getParameter("profileId"));
        User currentUser = (User) req.getSession().getAttribute("user");

        if ("yes".equals(choice)) {
            userDao.addLike(currentUser.getId(), profileId);
        }
        resp.sendRedirect("/users");
    }
}