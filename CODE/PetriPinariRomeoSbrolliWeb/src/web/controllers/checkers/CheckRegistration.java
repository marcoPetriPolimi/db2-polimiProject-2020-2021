package web.controllers.checkers;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import exceptions.UserAlreadyPresentEmailException;
import exceptions.UserAlreadyPresentNicknameException;
import services.AccountService;
import services.OffensiveWordsService;
import web.controllers.HttpThymeleafServlet;

/**
 * Servlet implementation class CheckRegistration
 */
@WebServlet("/CheckRegistration")
public class CheckRegistration extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/AccountService")
	private AccountService accountService;
	@EJB(name = "services/OffensiveWordService")
	private OffensiveWordsService offensiveWordService;

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
		
		String errorMessage = "";
		String registrationMessage = "";
		boolean error = false;
		boolean registration = false;

		String username = req.getParameter("usr");
		String email = req.getParameter("email");
		String password = req.getParameter("pwd");
		String repeatPassword = req.getParameter("pwd2");
		
		if (username == null || email == null || password == null || repeatPassword == null) {
			error = true;
			errorMessage = lang.getString("indexIncompleteForm");
		} else {
			if (!password.equals(repeatPassword)) {
				error = true;
				errorMessage = lang.getString("indexWrongRepeatPassword");
			} else if (!Pattern.matches("\\w+@\\w+\\.[a-z]{2,2}+",email)) {
				error = true;
				errorMessage = lang.getString("indexWrongEmailNotEmail");
			} else if (username.trim().length() == 0) {
				error = true;
				errorMessage = lang.getString("indexWrongNameNull");
			} else if (offensiveWordService.isPresent(username)) {
				error = true;
				errorMessage = lang.getString("indexWrongNameOffensive");
			} else {
				try {
					accountService.createNewUser(username,password,email,new Date());
					// if the code arrives at this step the user has been created since no exception is thrown
					registration = true;
					registrationMessage = lang.getString("indexRegistrationSuccess");
				} catch (UserAlreadyPresentNicknameException e) {
					error = true;
					errorMessage = lang.getString("indexNameAlreadyPresent");
				} catch (UserAlreadyPresentEmailException e) {
					error = true;
					errorMessage = lang.getString("indexWrongRepeatPassword");
				}
			}
		}
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("errorMsg", error);
		webContext.setVariable("errorText", errorMessage);
		webContext.setVariable("registration", registration);
		webContext.setVariable("registrationText", registrationMessage);
		thymeleaf.process(page,webContext,resp.getWriter());
	}

}
