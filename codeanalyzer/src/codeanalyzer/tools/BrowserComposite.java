package codeanalyzer.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class BrowserComposite extends Composite {

	protected Browser browser;

	// protected String editor_content;
	protected boolean loadCompleted = false;

	private ScrolledForm form;

	// private String buf;

	// BookSection section;

	public BrowserComposite(Composite parent, String buf, ScrolledForm form) {
		super(parent, SWT.None);

		this.form = form;

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.Resize);
		browser.setJavascriptEnabled(true);

		// Set content of editor after load completed
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
			}

			@Override
			public void completed(ProgressEvent event) {
				loadCompleted = true;
				getHeight();
			}
		});

		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				String text = event.text;
				// if (text.equals("loaded"))
				// getHeight();
				// else
				browser.setData(text);

			}
		});

		// Set url pointed to editor
		try {

			browser.setText(transform(buf));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String transform(String buf) {
		StringBuilder result = new StringBuilder();
		result.append("<style>");
		result.append("body{" + "overflow: hidden;" + "}");
		result.append("</style>");
		// result.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js\"></script>");
		result.append("<script type='text/javascript'>"
				+ "function getHeight() {"
				+ "return document.getElementById('body').offsetHeight;"
				// + "window.status = 'onHeightCalculated()'; "
				// + "window.status = 'done'; "
				// + "return $('#body').height();" +
				+ "};");

		// result.append("$(document).ready(function(){"
		// + "window.status = 'loaded'" + "});");
		result.append("</script>");

		result.append("<body>");
		result.append("<div id='body'>");

		result.append(buf);

		result.append("</div>");
		result.append("</body>");
		return result.toString();
	}

	public void getHeight() {
		String content = "";

		boolean executed = browser.execute("window.status=getHeight();");

		if (executed) {
			content = (String) browser.getData();
			// System.out.println(content);

			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = false;
			gd.widthHint = 50;
			gd.heightHint = Integer.parseInt(content) + 30;
			// gd.heightHint = browserComposite.getHeight();
			// gd.horizontalSpan = numColumns - 1;
			// browserComposite.setLayoutData(gd);
			// browser.setLayoutData(gd);

			this.setLayoutData(gd);

			form.reflow(true);

			// browser.execute("window.status='done'");
		}

		// return content;

	}

}
