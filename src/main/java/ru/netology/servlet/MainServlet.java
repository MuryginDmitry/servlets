package ru.netology.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {
    private PostController controller;
    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
    }

    private static final String ALL_POSTS = "/api/posts";
    private static final String SINGLE_POST = "/api/posts/\\d+";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String path = req.getRequestURI();
        final String method = req.getMethod();

        if (method.equals("GET") && path.equals(ALL_POSTS)) {
            controller.all(resp);
        } else if (method.equals("GET") && path.matches(SINGLE_POST)) {
            final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
        } else if (method.equals("POST") && path.equals(ALL_POSTS)) {
            controller.save(req.getReader(), resp);
        } else if (method.equals("DELETE") && path.matches(SINGLE_POST)) {
            final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.removeById(id, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
