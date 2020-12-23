package web.controllers;

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
import exceptions.UserAlreadyPresentNicknameException;
import exceptions.UserCredentialsException;
import exceptions.UserException;
import services.AccountService;
import services.OffensiveWordsService;

@WebServlet("/CheckNickname")
public class CheckChangeNickname extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/AccountService")
	private AccountService accountService;
	@EJB(name = "services/OffensiveWordService")
	private OffensiveWordsService offensiveWordService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "changeNickname";
		
		String errorMessage = "";
		boolean error = false;
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		String username = req.getParameter("usr");
		String password = req.getParameter("pwd");
		
		if (username == null || password == null) {
			error = true;
			errorMessage = lang.getString("changeNicknameIncompleteForm");
		} else {
			if (username.trim().length() == 0) {
				error = true;
				errorMessage = lang.getString("indexWrongNameNull");
			} else if (offensiveWordService.isPresent(username)) {
				error = true;
				errorMessage = lang.getString("indexWrongNameOffensive");
			} else {
				try {
					// try to change nickname
					accountService.changeNickname(user.getId(), username, password);
				} catch (UserAlreadyPresentNicknameException e) {
					error = true;
					errorMessage = lang.getString("indexNameAlreadyPresent");
				} catch (UserCredentialsException e) {
					error = true;
					errorMessage = lang.getString("indexWrongPassword");
				} catch (UserException e) {
					// the user exception is thrown when the user name does not exist, which is impossible since the value is taken from the session
					resp.sendRedirect("error?code=500");
				}
			}
		}
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("user", user);
		webContext.setVariable("errorMsg", error);
		webContext.setVariable("errorText", errorMessage);
		thymeleaf.process(page,webContext,resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
