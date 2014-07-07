package ebook.module.book.views.section.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class BrowserComposite extends Composite {

	protected Browser browser;

	protected boolean loadCompleted = false;

	private final String url;

	public BrowserComposite(Composite blockComposite, String url) {
		super(blockComposite, SWT.BORDER);

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.Resize | SWT.MOZILLA);
		browser.setJavascriptEnabled(true);

		this.url = url;
		updateUrl();

	}

	public void updateUrl() {
		browser.setUrl(url);
	}

}
