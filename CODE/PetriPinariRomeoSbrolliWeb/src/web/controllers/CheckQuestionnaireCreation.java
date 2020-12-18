package web.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import services.QuestionnaireCreationService;

/**
 * Servlet implementation class CheckQuestionnaireCreation
 */
@WebServlet("/CreateQuestionnaire")
public class CheckQuestionnaireCreation extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	private QuestionnaireCreationService qcs;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		
		 qcs = (QuestionnaireCreationService) req.getSession().getAttribute("QuestionnaireCreationService");
			
			if(qcs == null){
		          // EJB is not present in the HTTP session
		          // so let's fetch a new one from the container
		          try {
		            InitialContext ic = new InitialContext();
		            qcs = (QuestionnaireCreationService) 
		             ic.lookup("java:global/PetriPinariRomeoSbrolliWeb/QuestionnaireCreationService");

		            // put EJB in HTTP session for future servlet calls
		            req.getSession().setAttribute(
		              "QuestionnaireCreationService", 
		              qcs);

		          } catch (NamingException e) {
		            throw new ServletException(e);
		          }
		    }
		

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date presDate= null;
			try {
				presDate = (Date) sdf.parse(req.getParameter("date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//FIXME appena viene implementata la session qua al posto di 1 va messo l'id dello user, da aggiungere la data di presentazione
			qcs.createQuestionnaire(1, StringEscapeUtils.escapeJava(req.getParameter("name")),presDate,(Integer)Integer.parseInt(req.getParameter("chosenProd")));
		
		String ctxpath = getServletContext().getContextPath();
		req.getSession().removeAttribute("QuestionnaireCreationService");
		qcs.remove();
		String path = ctxpath + "/ServletBrutta";
		resp.sendRedirect(path);
	}

}
