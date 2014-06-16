package codeanalyzer.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BooksListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher error_view = request
				.getRequestDispatcher("error.jsp");

		String c = request.getParameter("book");
		if (c == null) {
			error_view.forward(request, response);
			return;
		}

		request.setAttribute("data", c);
		RequestDispatcher view = request.getRequestDispatcher("list.jsp");
		view.forward(request, response);
	}
}

// protected void doGet1(HttpServletRequest req, HttpServletResponse resp)
// throws ServletException, IOException {
// // NEXT Auto-generated method stub
// // super.doGet(req, resp);
// // response.setContentType("text/html");
// // response.setStatus(HttpServletResponse.SC_OK);
// // response.getWriter().println("<h1>Hello SimpleServlet</h1>");
// // RequestDispatcher rd = request.getRequestDispatcher("test.jsp");
// // rd.forward(request, response);
//
// String name = "param";
// String value = req.getParameter(name);
// if (value == null) {
// // The request parameter 'param' was not present in the query string
// // e.g. http://hostname.com?a=b
// } else if ("".equals(value)) {
// // The request parameter 'param' was present in the query string but
// // has no value
// // e.g. http://hostname.com?param=&a=b
// }
//
// // The following generates a page showing all the request parameters
// PrintWriter out = resp.getWriter();
// resp.setContentType("text/plain");
//
// // Get the values of all request parameters
// Enumeration<String> enum1 = req.getParameterNames();
// for (; enum1.hasMoreElements();) {
// // Get the name of the request parameter
// name = enum1.nextElement();
// out.println(name);
//
// // Get the value of the request parameter
// value = req.getParameter(name);
//
// // If the request parameter can appear more than once in the query
// // string, get all values
// String[] values = req.getParameterValues(name);
//
// for (int i = 0; i < values.length; i++) {
// out.println("    " + values[i]);
// }
// }
// out.close();
// }