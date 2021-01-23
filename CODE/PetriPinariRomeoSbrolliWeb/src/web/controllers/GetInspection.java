package web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.context.WebContext;

import database.Questionnaire;
import exceptions.QuestionnaireException;
import database.Product;
import database.Question;
import services.QuestionnaireAdminService;
import services.QuestionnaireOfTheDayService;

//@Contributor(s): Etion, Cristian Schrolle

@WebServlet("/inspection")
public class GetInspection extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;

	private QuestionnaireAdminService qas;

	@EJB(name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService QDS;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);

		 qas = (QuestionnaireAdminService) req.getSession().getAttribute("QuestionnaireAdminService");

		if(qas == null){
	          // EJB is not present in the HTTP session
	          // so let's fetch a new one from the container
	          try {
	            InitialContext ic = new InitialContext();
	            qas = (QuestionnaireAdminService)
	            ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireAdminService");
	            // put EJB in HTTP session for future servlet calls
	            req.getSession().setAttribute("QuestionnaireAdminService",qas);
	          } catch (NamingException e) {
	            throw new ServletException(e);
	          }
	    }



		String questionnaireId = req.getParameter("idQuestionnaire");

		String publicationDate = req.getParameter("publicationDate");

		String selectorAsString = req.getParameter("selector");
		
		String refresh_s= req.getParameter("refresh");
		Integer refresh = (refresh_s!=null)? Integer.parseInt(refresh_s) : 0;

		//if it is his first visit and the user has not input any number yet write answer and
		if(questionnaireId == null && publicationDate == null && selectorAsString == null) {
			System.out.println(questionnaireId + "  " + selectorAsString);
			wrongFormat(req, resp);
			return;
		};



		// selector is "1" if you are choosing id and "2" if you are choosing "publicationDate"
		int selector = Integer.parseInt(selectorAsString);

		Questionnaire questionnaire = null;
		List<Question> questions = null;
		Integer idQuestionnaire=null;
		if(selector == 1) {
			try {
				idQuestionnaire = Integer.parseInt(questionnaireId);
				questionnaire =QDS.getQuestionnaire(idQuestionnaire);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (selector == 2) {
			try {
				questionnaire =QDS.getQuestionnaireByDate(publicationDate);
				idQuestionnaire = questionnaire.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (selector == 3) {
			try {
				questionnaire = QDS.getQuestionnaireOfTheDay();
				idQuestionnaire = questionnaire.getId();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		//guard check if query is right
		if(questionnaire == null) {
			wrongFormat(req, resp);
			return;
		}

		try {
			questions = QDS.getQuestions(idQuestionnaire);
		} catch (QuestionnaireException e) {
			e.printStackTrace();
		}
		
		List<String> questionsString = new ArrayList<String>();
		for(Question q : questions) {
			questionsString.add(q.getQuestion());
		}

		String creatorName = questionnaire.getCreator().getNickname();
		Product product = questionnaire.getProduct();
		Date creationDate = questionnaire.getDate();
		Date presentationDate = questionnaire.getPresDate();

		String path = "QuestionnaireInspection";
		ServletContext servletContext = getServletContext();
		
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("lang", lang);
		ctx.setVariable("questionnaire", questionnaire.getId());
		ctx.setVariable("questionnaireName", questionnaire.getName());
		ctx.setVariable("questionsString", questionsString);
		ctx.setVariable("creatorName", creatorName);
		ctx.setVariable("product", Base64.encodeBase64String(product.getImage()));
		ctx.setVariable("creationDate", creationDate);
		ctx.setVariable("presentationDate", presentationDate);
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		setUserSubsCanc(ctx,idQuestionnaire,refresh);
		thymeleaf.process(path, ctx, resp.getWriter());
	}


	/**
	 * Creates a thymeleaf object that dynamically writes on the page if the questionnaire is found
	 * @param s questionnaire id
	 * @param req
	 * @param resp
	 * @throws IOException
	 */


	private void setUserSubsCanc(WebContext ctx, Integer idQuestionnaire,int refresh) {
		if (qas.getSelectedQuestionnaireId()==null || qas.getSelectedQuestionnaireId().intValue()!=idQuestionnaire.intValue()) {
			qas.setSelectedQuestionnaireId(idQuestionnaire);
		}
		if (refresh==1) qas.refreshAnswers();;
		ctx.setVariable("userSubmitted", qas.getUserSubmissionList());
		ctx.setVariable("userCanceled", qas.getUserCancelList());
	}


	/**
	 * checks if string s is null, namely no input has been put in
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	private boolean wrongFormat( HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ResourceBundle lang = findLanguage(req);
		String path = "QuestionnaireInspection";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("lang", lang);
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		thymeleaf.process(path, ctx, resp.getWriter());
		return true;
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
