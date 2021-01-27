package web.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;

import database.Product;
import database.User;
import exceptions.ProductException;
import exceptions.QuestionnaireException;
import exceptions.ReviewAlreadyPresentException;
import services.QuestionnaireOfTheDayService;
import services.ReviewService;

@WebServlet("/productReview")
public class GetProductReview extends HttpThymeleafServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "ReviewService")
	private ReviewService rs;
	
	@EJB (name = "QuestionnaireOfTheDayService")
	private QuestionnaireOfTheDayService qds;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle lang = findLanguage(req);
		
		String message = null;
		String path = "ReviewSubmission";
		Product dailyProduct = null;
		try {
			dailyProduct = qds.getQuestionnaireOfTheDay().getProduct();
		} catch (QuestionnaireException | ParseException e) {
			e.printStackTrace();
		}
		
		String productReview = req.getParameter("productReview");
		if (productReview != null) {
			try {
				rs.addProductReview((User) req.getSession().getAttribute("user"), dailyProduct.getId(), productReview);
				resp.sendRedirect("homepage");
			} catch (ProductException e) {
				message = "Sorry, currently there is no product available";
				e.printStackTrace();
			}catch(ReviewAlreadyPresentException e) {
				message = "Sorry, you have already reviewed this product";
			}
		}			
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("lang", lang);
		ctx.setVariable("user", req.getSession().getAttribute("user"));
		ctx.setVariable("message", message);
		thymeleaf.process(path, ctx, resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
