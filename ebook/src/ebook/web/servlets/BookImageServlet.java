package ebook.web.servlets;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.module.book.BookConnection;

public class BookImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String book_id = request.getParameter("book");
		if (book_id == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String image_id = request.getParameter("id");
		if (image_id == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		BookConnection book = App.srv.bls().getBook(book_id);
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		BufferedInputStream in = book.srv().getImage(image_id);
		if (in == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		final byte[][] buffer = new byte[1][];
		response.setContentType("");
		ServletOutputStream out = response.getOutputStream();

		buffer[0] = new byte[1024];
		int n;
		while ((n = in.read(buffer[0])) > 0) {
			out.write(buffer[0], 0, n);
		}
		in.close();
		out.flush();

	}
}
