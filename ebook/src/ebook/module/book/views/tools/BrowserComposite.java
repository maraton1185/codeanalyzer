package ebook.module.book.views.tools;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class BrowserComposite extends Composite {

	protected Browser browser;

	protected boolean loadCompleted = false;

	private final String url;

	private BookConnection book;

	MWindow window;
	EHandlerService hs;
	ECommandService cs;

	public BrowserComposite(Composite blockComposite, String url) {
		super(blockComposite, SWT.BORDER);

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.Resize | SWT.MOZILLA);
		browser.setJavascriptEnabled(true);

		this.url = url;
		updateUrl("");

	}

	public BrowserComposite(Composite blockComposite, String url, String tag,
			BookConnection book, MWindow window, EHandlerService hs,
			ECommandService cs) {
		super(blockComposite, SWT.BORDER);

		this.book = book;
		this.window = window;
		this.hs = hs;
		this.cs = cs;

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.Resize | SWT.MOZILLA);
		browser.setJavascriptEnabled(true);

		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				String text = event.text;
				if (text.contains("event:openSection()"))
					openSection(text.replace("event:openSection()=", ""));
				// MessageDialog.openInformation(getShell(),
				// Strings.get("appTitle"), "event:changeBlock():"
				// + text.replace("event:changeBlock()=", ""));
				else
					browser.setData(text);

			}
		});

		this.url = url;
		updateUrl(tag);

	}

	protected void openSection(String section_id) {
		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		SectionInfo info = (SectionInfo) book.srv().get(id);

		if (info == null)
			return;

		window.getContext().set(SectionInfo.class, info);
		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));

	}

	public void updateUrl(String tag) {
		browser.setUrl(url + "&swt=" + App.getJetty().swt()
				+ (tag.isEmpty() ? "" : "#" + tag));
		// browser.refresh();
	}

}
