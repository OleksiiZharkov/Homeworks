package ua.tinder.servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ua.tinder.config.FreeMarkerConfig;
import ua.tinder.dao.MessageDao;
import ua.tinder.dao.UserDao;
import ua.tinder.model.Message;
import ua.tinder.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/messages/*")
public class MessagesServlet extends HttpServlet {

    private MessageDao messageDao;
    private UserDao userDao;
    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        this.messageDao = new MessageDao();
        this.userDao = new UserDao();
        this.cfg = (Configuration) getServletContext().getAttribute(FreeMarkerConfig.TEMPLATE_CONFIG);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        int partnerId = getPartnerId(req);
        Optional<User> partnerOpt = userDao.getById(partnerId);

        if (partnerOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        List<Message> messages = messageDao.getMessages(currentUser.getId(), partnerId);
        Map<String, Object> data = new HashMap<>();
        data.put("partner", partnerOpt.get());
        data.put("messages", messages);
        data.put("currentUserId", currentUser.getId());

        Template template = cfg.getTemplate("messages.ftl");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            template.process(data, resp.getWriter());
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("user");
        int recipientId = getPartnerId(req);
        String content = req.getParameter("message");

        if (content != null && !content.trim().isEmpty()) {
            messageDao.saveMessage(currentUser.getId(), recipientId, content);
        }
        resp.sendRedirect("/messages/" + recipientId);
    }

    private int getPartnerId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // Буде "/{id}"
        return Integer.parseInt(pathInfo.substring(1)); // Видаляємо "/"
    }
}