package ebook.module.book.views.tools;

import java.util.List;

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
import ebook.module.book.service.ContextService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.conf.tree.ContextInfoSelection;
import ebook.module.tree.ITreeItemInfo;
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
		browser.setUrl(url);

		// updateUrl("");

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
				else if (text.contains("event:openContext()"))
					openContext(text.replace("event:openContext()=", ""));
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
		Utils.executeHandler(hs, cs, Strings.model("command.id.ShowSection"));

	}

	protected void openContext(String section_id) {
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

		ContextService service = book.ctxsrv(info);
		List<ITreeItemInfo> input = service.getRoot();
		if (input.isEmpty())
			return;

		ContextInfoSelection sel = new ContextInfoSelection();
		sel.add(input.get(0));

		window.getContext().set(SectionInfo.class, info);
		window.getContext().set(ContextInfoSelection.class, sel);

		Utils.executeHandler(hs, cs, Strings.model("ContextView.OpenContext"));

	}

	public void updateUrl(String tag) {
		browser.setUrl(url + "&swt=" + App.getJetty().swt()
				+ (tag.isEmpty() ? "" : "#" + tag));
		// browser.refresh();
	}

}
