package web.controllers;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import utils.Const;

public class HttpThymeleafServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected TemplateEngine thymeleaf;

	@Override
	public void init() throws ServletException {
		//getting the connection
		ServletContext context = getServletContext();

		//preparing thymeleaf template
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setPrefix("/WEB-INF/");
		templateResolver.setSuffix(".html");
		thymeleaf = new TemplateEngine();
		thymeleaf.setTemplateResolver(templateResolver);
	}
	
	public static ResourceBundle findLanguage(HttpServletRequest req) {
		ResourceBundle lang;

		if (Const.acceptedLangTags.contains(req.getLocale().getLanguage())) {
			String language = req.getLocale().getLanguage();
			String country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
		} else if (Const.acceptedOldIsoLangTags.contains(req.getLocale().getLanguage())) {
			String language = Const.oldIsoLangTagsToNew.get(req.getLocale().getLanguage());
			String country = Const.isoTagToCountry.get(language);
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(language,country));
		} else {
			lang = ResourceBundle.getBundle(Const.propertiesBaseName,new Locale(Const.defaultLanguage,Const.defaultCountry));
		}
		return lang;
	}
	
	/**
	 * This method gets a function and then eliminates every object related to it.
	 * @param session The session to modify
	 */
	public static final void cleanSession(HttpSession s) {
		// eliminates every object already bound to this session
		Enumeration<String> strings = s.getAttributeNames();
		while (strings.hasMoreElements()) {
			s.removeAttribute(strings.nextElement());
		}
	}
}
