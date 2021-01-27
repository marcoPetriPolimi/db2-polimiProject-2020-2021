package web.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import database.User;
import web.controllers.HttpThymeleafServlet;


@WebServlet("/greetings")
public class GetGreetings extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// insert the code for the get
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "greetings";
		
		User user = (User) req.getSession().getAttribute("user");
		String name = user.getNickname();
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("user", user);
		webContext.setVariable("errorMsg", false);
		webContext.setVariable("errorText", "none");
		webContext.setVariable("nickName", name);
		thymeleaf.process(page,webContext,resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
