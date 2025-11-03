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
import java.util.List;
import java.util.Map;

@WebServlet("/liked")
public class LikedServlet extends HttpServlet {

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
        List<User> likedUsers = userDao.getLikedUsers(currentUser.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("profiles", likedUsers);

        Template template = cfg.getTemplate("liked.ftl");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            template.process(data, resp.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}