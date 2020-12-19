package web.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.QuestionnaireCancellationException;
import services.QuestionnaireOfTheDayService;

/*
 * @author Giorgio
 */
@WebServlet("/DeleteQuestionnaire")
public class GetDeleteQuestionnaire extends HttpThymeleafServlet {

	private static final long serialVersionUID = 1L;
	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService qod;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		int toRemove= Integer.parseInt(req.getParameter("questionnaireId"));
		try {
			qod.deleteQuestionnaire(toRemove);
		} catch (QuestionnaireCancellationException e) {
			e.printStackTrace();
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/deletion";
		resp.sendRedirect(path);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
	}
	
}

