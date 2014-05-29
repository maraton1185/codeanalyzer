package jettycustom;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

public class JettyErrorHandler extends ErrorHandler {
	// @Override
	// protected void handleErrorPage(HttpServletRequest request, Writer writer,
	// int code, String message) throws IOException {
	// // if (code == 404) {
	// // writer.write("No,no,no!!! This page does not exist!");
	// // return;
	// // }
	// RequestDispatcher error_view = request
	// .getRequestDispatcher("error.jsp");
	// // error_view.forward(req, resp);
	// super.handleErrorPage(request, writer, code, message);
	// }

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		RequestDispatcher error_view = request
				.getRequestDispatcher("error.jsp");
		try {
			error_view.forward(request, response);
		} catch (ServletException e) {
			super.handle(target, baseRequest, request, response);
		}
		// super.handle(target, baseRequest, request, response);
	}
}
