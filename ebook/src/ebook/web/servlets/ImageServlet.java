package ebook.web.servlets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.Activator;
import ebook.core.App;
import ebook.module.book.BookConnection;

public class ImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43823400533628363L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// response.sendError(HttpServletResponse.SC_NOT_FOUND);

		// RequestDispatcher error_view = request
		// .getRequestDispatcher("bookNotFind.jsp");
		//
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

		boolean originSize = false;
		String size = request.getParameter("size");
		if (size == null)
			originSize = true;

		// error_view.forward(request, response);
		//
		BookConnection book = App.srv.bls().getBook(book_id);
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (originSize) {
			BufferedInputStream in = book.srv().getImage(image_id);
			if (in == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// InputStream resource = Activator.class
			// .getResourceAsStream("/icons/checked.png");
			//
			final byte[][] buffer = new byte[1][];
			// BufferedInputStream in = new BufferedInputStream(resource);
			response.setContentType("");
			ServletOutputStream out = response.getOutputStream();

			// ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			buffer[0] = new byte[1024];
			int n;
			while ((n = in.read(buffer[0])) > 0) {
				out.write(buffer[0], 0, n);
			}
			in.close();
			out.flush();
		} else {

			InputStream resource = Activator.class
					.getResourceAsStream("/icons/checked.png");

			// SectionImage image = book.srv().getSectionImage(image_id);
			// if (image == null) {
			// response.sendError(HttpServletResponse.SC_NOT_FOUND);
			// return;
			// }

			final byte[][] buffer = new byte[1][];
			BufferedInputStream in = new BufferedInputStream(resource);
			response.setContentType("");
			ServletOutputStream out = response.getOutputStream();

			// ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			buffer[0] = new byte[1024];
			int n;
			while ((n = in.read(buffer[0])) > 0) {
				out.write(buffer[0], 0, n);
			}
			in.close();
			out.flush();

		}
	}
}
