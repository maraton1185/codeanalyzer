package codeanalyzer.module.books.web;

import java.net.MalformedURLException;

import org.eclipse.e4.core.di.annotations.Execute;

import com.caucho.hessian.client.HessianProxyFactory;

public class Test {
	@Execute
	public void execute() {
		String url = "http://localhost:8081/test1";

		HessianProxyFactory factory = new HessianProxyFactory();
		BasicAPI basic;
		try {

			basic = (BasicAPI) factory.create(BasicAPI.class, url);

			RequestData data = new RequestData();
			data.f1 = "hi";
			basic.setData(data);
			System.out.println("hello(): " + basic.hello());

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

	}

}