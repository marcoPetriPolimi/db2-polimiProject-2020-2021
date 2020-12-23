package web.controllers.checkers;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.context.WebContext;

import database.User;
import exceptions.UserException;
import services.AccountService;
import web.controllers.HttpThymeleafServlet;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/AccountService")
	private AccountService accountService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("index");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "index";
		
		User user;
		HttpSession session = req.getSession();
		
		String errorMessage = "";
		boolean error = false;
		boolean login = false;
		
		String username = req.getParameter("usr");
		String password = req.getParameter("pwd");
		
		if (username == null || password == null) {
			error = true;
			errorMessage = lang.getString("indexIncompleteForm");
		} else {
			try {
				user = accountService.findUser(username);
				if (accountService.login(username, password)) {
					cleanSession(session);
					
					// now there are no elements bound to this session, the newly are created
					session.setAttribute("user", user);
					login = true;
				} else {
					error = true;
					errorMessage = lang.getString("indexWrongPassword");
				}
			} catch (UserException e) {
				error = true;
				errorMessage = lang.getString("indexUnexistingUser");
			}
		}
		
		if (login) {
			resp.sendRedirect("homepage");
		} else {
			webContext.setVariable("lang", lang);
			webContext.setVariable("errorMsg", error);
			webContext.setVariable("errorText", errorMessage);
			webContext.setVariable("registration", false);
			webContext.setVariable("registrationText", "none");
			thymeleaf.process(page,webContext,resp.getWriter());
		}
	}
}
