package web.controllers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class HttpThymeleafServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected TemplateEngine thymeleaf;

	@Override
	public void init() throws ServletException {
		// getting the connection
		ServletContext context = getServletContext();

		// preparing thymeleaf template
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setPrefix("/WEB-INF/");
		templateResolver.setSuffix(".html");
		thymeleaf = new TemplateEngine();
		thymeleaf.setTemplateResolver(templateResolver);
	}
}
