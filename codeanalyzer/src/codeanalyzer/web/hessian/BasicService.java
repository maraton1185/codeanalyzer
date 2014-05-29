package codeanalyzer.web.hessian;

import com.caucho.hessian.server.HessianServlet;

public class BasicService extends HessianServlet implements BasicAPI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String _greeting = "Hello, world";

	@Override
	public String hello() {
		return _greeting;
	}

	// @Override
	// public void setGreeting(String greeting) {
	// _greeting = greeting;
	// }

	@Override
	public void setData(RequestData data) {
		_greeting = data.f1;
	}
}
