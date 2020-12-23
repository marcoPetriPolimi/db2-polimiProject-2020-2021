package web.controllers.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import web.controllers.HttpThymeleafServlet;

//linked from homepage, do not change name of webServlet -- Etion
@WebServlet("/questionnaireResponse")
public class GetQuestionnaireResponse extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// insert the code for the get
		PrintWriter out = resp.getWriter();
		out.println("This page has still to be implemented. Soon...");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
