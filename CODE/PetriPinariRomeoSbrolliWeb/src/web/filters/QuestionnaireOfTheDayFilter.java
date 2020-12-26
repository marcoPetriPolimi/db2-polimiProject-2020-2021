package web.filters;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Questionnaire;
import database.User;
import exceptions.QuestionnaireException;
import services.QuestionnaireOfTheDayService;
import services.SubmissionService;

public class QuestionnaireOfTheDayFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService questOfDay;
	@EJB(name = "services/SubmissionService")
	private SubmissionService submissionService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		
		Questionnaire quest;
		User user = (User) session.getAttribute("user");
		Date currDate = new Date();

		try {
			quest = questOfDay.getQuestionnaireByDate(currDate);
			if (submissionService.findSubmission(user.getId(), quest.getId()) != null) {
				resp.sendRedirect("homepage");
			} else {
				chain.doFilter(request,response);
			}
		} catch (QuestionnaireException e) {
			resp.sendRedirect("homepage");
		}
	}
}