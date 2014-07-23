package ebook.web.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ebook.core.App;
import ebook.module.acl.ACLService.ACLResult;
import ebook.module.acl.AclViewModel;

public class BookFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		FilterHelper helper = new FilterHelper(request, response, chain);

		if (helper.swt())
			return;

		// get book parameter
		Integer book = helper.book();
		if (book == null)
			return;

		// get acl set
		ACLResult out = new ACLResult();
		List<AclViewModel> acl = App.srv.acl().get(book, out);

		helper.acl(acl);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

}
