package codeanalyzer.books;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// NEXT Auto-generated method stub
		// super.doGet(req, resp);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>Hello SimpleServlet</h1>");
		// RequestDispatcher rd = request.getRequestDispatcher("test.jsp");
		// rd.forward(request, response);
	}

}
