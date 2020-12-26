package web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import services.QuestionnaireSubmissionService;

public class EJBQuestionnaireFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();

		QuestionnaireSubmissionService qss = (QuestionnaireSubmissionService) session.getAttribute("questSubmissService");
		
		if(qss!=null) {
			qss.remove();
		}
		session.removeAttribute("questSubmissService");
		chain.doFilter(request,response);
	}
}
