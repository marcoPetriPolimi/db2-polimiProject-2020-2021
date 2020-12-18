package web.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import database.Questionnaire;
import exceptions.QuestionnaireException;
import services.LeaderboardService;
import services.QuestionnaireOfTheDayService;


@WebServlet("/Leaderboard")
public class GetLeaderboard extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "LeaderboardService")
	private LeaderboardService ls;
	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService qds;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, Integer> leaderboard = null;
		String message = "Please, make a choice";
		if(req.getParameterMap().containsKey("selector")) {
			
			if(req.getParameter("selector").equals("1")){
				//Retrieving the leadearboard of all time
				leaderboard = ls.getGeneralLeaderboard();
				message = "This is the leaderboard of all time";
			}
			else if(req.getParameter("selector").equals("2")) {
		        try {
		        	//Retrieving the questionnaire of the day
					Questionnaire questionnaire = qds.getQuestionnaireByDate(Calendar.getInstance().getTime());
					//Retrieving the leaderboard of the users who submitted the questionnaire of the day
					//leaderboard = ls.getQuestionnaireLeaderboard(questionnaire.getId());
					message = "This is the leaderboard of the day";
				} catch (QuestionnaireException e) {
					e.printStackTrace();
					message = "Sorry, the questionnaire of the day is currently unavailable :(";
				}	        
			}
		}
		System.out.println(leaderboard);
		
		
		String path = "Leaderboard";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("leaderboard", leaderboard);
		ctx.setVariable("message", message);
		thymeleaf.process(path, ctx, resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
