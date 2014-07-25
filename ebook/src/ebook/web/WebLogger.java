package ebook.web;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jetty.util.log.Logger;

public class WebLogger implements Logger {

	@Override
	public void debug(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(String info, Object... arg1) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("debug.txt", true)))) {
			out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void debug(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Logger getLogger(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ignore(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void info(String info, Object... arg1) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("info.txt", true)))) {
			out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void info(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDebugEnabled(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn(String info, Object... arg1) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("warn.txt", true)))) {
			out.println(info);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub

	}

}
