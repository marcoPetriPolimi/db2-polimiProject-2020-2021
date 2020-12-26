package web.controllers.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.registry.InvalidRequestException;

import org.thymeleaf.context.WebContext;

import database.Question;
import database.Questionnaire;
import database.User;
import exceptions.QuestionnaireException;
import services.AccountService;
import services.QuestionnaireOfTheDayService;
import services.QuestionnaireSubmissionService;
import utils.Const;
import utils.forms.FormPersonalAnswer;
import utils.forms.FormProductAnswer;
import web.controllers.HttpThymeleafServlet;

@WebServlet("/questionnaireResponse")
public class GetQuestionnaireResponse extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService questOfDay;
	@EJB(name = "services/AccountService")
	private AccountService accountService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		ServletContext context = getServletContext();
		WebContext webContext = new WebContext(req,resp,context);
		String page = "questionnaireResponse";
		
		User user = (User) req.getSession().getAttribute("user");
		Date currDate = new Date();
		Questionnaire questionnaire = null;
		
		String counter = req.getParameter("sec");
		
		try {
			questionnaire = questOfDay.getQuestionnaireByDate(currDate);
		} catch (QuestionnaireException e) {
			// the questionnaire exception is never thrown since there is the filter veryfing that today there is a questionnaire
			resp.sendRedirect("error?code=500");
		}
		
		webContext.setVariable("lang", lang);
		webContext.setVariable("user", user);
		webContext.setVariable("section", counter != null ? (counter.equals("1") ? 1 : 2) : 1);
		webContext.setVariable("questions", questionnaire.getQuestions());
		webContext.setVariable("questionnaireName", questionnaire.getName());
		thymeleaf.process(page,webContext,resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		
		String firstSection = req.getParameter("product");
		String secondSection = req.getParameter("sub");
		
		User user = (User) session.getAttribute("user");
		QuestionnaireSubmissionService qss = (QuestionnaireSubmissionService) session.getAttribute("questSubmissService");
		Date currDate = new Date();
		Questionnaire questionnaire = null;
		
		boolean error500 = false;
		
		try {
			questionnaire = questOfDay.getQuestionnaireByDate(currDate);
		} catch (QuestionnaireException e) {
			// the questionnaire exception is never thrown since there is the filter veryfing that today there is a questionnaire
			error500 = true;
		}
		
		// check the request is not extremely manipulated
		if ((firstSection == null && secondSection == null) || (firstSection != null && secondSection != null)) {
			error500 = true;
		}
		
		if (!error500) {
			if (firstSection != null) {
				if (qss == null) {
					qss = createQuest();
					session.setAttribute("questSubmissService", qss);
				}
				
				try {
					qss.setQuestionnaire(questOfDay.getQuestionnaireByDate(new Date()).getId());
				} catch (QuestionnaireException e) {
					// impossible since there is the filter blocking access if not exist
				}
				
				try {
					qss.modifyProductAnswers(parseAnswers(req,questionnaire),user.getId());
					resp.sendRedirect("questionnaireResponse?sec=2");
				} catch (InvalidRequestException e) {
					// the request has been manipulated or user has not inserted comments/strings
					error500 = true;
				}
			} else if (secondSection.equals("b")) {
				// the user clicks the button back, so he must go back
				resp.sendRedirect("questionnaireResponse?sec=1");
			} else if (secondSection.equals("c")) {
				// the user want to cancel the questionnaire, so it is deleted
				qss.cancelQuestionnaire(user.getId());
				resp.sendRedirect("homepage");
			} else if (secondSection.equals("s")) {
				if (qss == null || !qss.isSubmissionWellFormed()) {
					// submission is valid only if the data are valid
					if (qss.getOffensiveWords()) {
						accountService.banUser(user.getId());
						cleanSession(session);
						session.invalidate();
						resp.sendRedirect("index");
					} else {
						resp.sendRedirect("questionnaireResponse?sec=1");
					}
				} else {
					try {
						qss.modifyPersonalAnswers(parsePersonalAnswers(req), user.getId());
						qss.submitQuestionnaire(user.getId());
						session.removeAttribute("questSubmissService");
						resp.sendRedirect("greetings");
					} catch (InvalidRequestException e) {
						// the request has been manipulated and so the page is reloaded
						resp.sendRedirect("questionnaireResponse?sec=2");
					}
				}
			} else {
				// the request has been manipulated
				error500 = true;
			}
		}
		
		if (error500) {
			resp.sendRedirect("error?code=500");
		}
	}
	
	private FormPersonalAnswer parsePersonalAnswers(HttpServletRequest req) throws InvalidRequestException {
		String sex = req.getParameter("sex");
		String age = req.getParameter("age");
		String expertise = req.getParameter("expertise");
		int ageNum;
		FormPersonalAnswer givenAnswers = new FormPersonalAnswer();
		
		if (sex != null) {
			if (!sex.equals("none") && !sex.equals("M") && !sex.equals("F") && !sex.equals("U")) {
				throw new InvalidRequestException();
			}
			
			// sex is valid
			if (sex.equals("M") || sex.equals("F") || sex.equals("U")) {
				try {
					givenAnswers.setSex(sex.charAt(0));
				} catch (IllegalArgumentException e) {
					// impossible to reach
				}
			}
		}
		if (age != null) {
			try {
				ageNum = Integer.parseInt(age);
				if (ageNum < 18 || ageNum > 130) {
					throw new InvalidRequestException();
				}
			} catch (NumberFormatException e) {
				throw new InvalidRequestException();
			}
			
			// age is valid
			try {
				givenAnswers.setAge(ageNum);
			} catch (IllegalArgumentException e) {
				// impossible to reach
			}
		}
		if (expertise != null) {
			if (!expertise.equals("none") && !expertise.equals("L") && !expertise.equals("M") && !expertise.equals("H")) {
				throw new InvalidRequestException();
			}
			
			// expertise is valid
			if (expertise.equals("L") || expertise.equals("M") || expertise.equals("H")) {
				try {
					givenAnswers.setExpertise(expertise.charAt(0) == 'L' ? 1 : expertise.charAt(0) == 'M' ? 2 : expertise.charAt(0) == 'H' ? 3 : 0);
				} catch (IllegalArgumentException e) {
					// impossible to reach
				}
			}
		}
		
		return givenAnswers;
	}
	
	private List<FormProductAnswer> parseAnswers(HttpServletRequest req, Questionnaire questionnaire) throws InvalidRequestException {
		int questionCounter = 0;
		List<FormProductAnswer> answersParsed = new ArrayList<>();
		
		for (Question q: questionnaire.getQuestions()) {
			List<String> possibleAnswers;
			String currParameter;
			String[] currCheckbox;
			FormProductAnswer currAnswer = new FormProductAnswer();
			
			currAnswer.setQuestion(q.getId());
			currAnswer.setType(q.getType());
			
			switch (q.getType()) {
				case 1:
					// checkbox
					possibleAnswers = q.getQuestionAnswers().stream().map((e) -> e.getAnswerText()).collect(Collectors.toList());
					currCheckbox = req.getParameterValues("check_"+questionCounter);
					if (currCheckbox == null || currCheckbox.length == 0 || !possibleAnswers.containsAll(Arrays.asList(currCheckbox))) {
						throw new InvalidRequestException();
					}
					
					for (String s : currCheckbox) {
						currAnswer.addResponse(s);
					}
					answersParsed.add(currAnswer);
				break;
				
				case 2:
					// selection
					possibleAnswers = q.getQuestionAnswers().stream().map((e) -> e.getAnswerText()).collect(Collectors.toList());
					currParameter = req.getParameter("sele_"+questionCounter);
					if (currParameter == null || !possibleAnswers.contains(currParameter)) {
						throw new InvalidRequestException();
					}
					
					currAnswer.setResponse(currParameter);
					answersParsed.add(currAnswer);
				break;
					
				case 3:
					// string
					currParameter = req.getParameter("text_"+questionCounter);
					if (currParameter == null || currParameter.trim().length() == 0 || currParameter.length() > Const.answersMaxLength) {
						throw new InvalidRequestException();
					}
					
					currAnswer.setResponse(currParameter);
					answersParsed.add(currAnswer);
				break;
				
				case 4:
					// comment
					currParameter = req.getParameter("comm_"+questionCounter);
					if (currParameter == null || currParameter.trim().length() == 0 || currParameter.length() > Const.answersMaxLength) {
						throw new InvalidRequestException();
					}
					
					currAnswer.setResponse(currParameter);
					answersParsed.add(currAnswer);
				break;
			}
			
			questionCounter++;
		}
		
		return answersParsed;
	}
	
	private QuestionnaireSubmissionService createQuest() throws ServletException {
		try {
			InitialContext ic = new InitialContext();
			return (QuestionnaireSubmissionService) ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireSubmissionService");
		} catch (NamingException e) {
			throw new ServletException(e);
		}
	}
}
