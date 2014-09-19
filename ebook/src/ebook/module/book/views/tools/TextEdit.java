package ebook.module.book.views.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;

public class TextEdit extends Composite {

	protected Browser browser;
	protected String editor_content;
	protected boolean loadCompleted = false;

	SectionInfo section;
	private BookService srv;

	public TextEdit(Composite parent, SectionInfo section, BookService srv) {
		super(parent, SWT.None);

		this.section = section;
		this.srv = srv;

		setLayout(new FillLayout());

		browser = new Browser(this, SWT.Resize);
		browser.setJavascriptEnabled(true);

		browser.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
			}

			@Override
			public void completed(ProgressEvent event) {
				loadCompleted = true;
				// browser.execute("setContent('" + editor_content + "');");
				// browser.execute("tinymce.activeEditor.execCommand(\"mceRepaint\");");
				// browser.execute("$.cookie(\"tinyEditor\", true);");
			}
		});

		browser.addOpenWindowListener(new OpenWindowListener() {
			@Override
			public void open(WindowEvent event) {

			}
		});

		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				String text = event.text;
				if (text.equals("editor:onInit()"))
					setText();
				else if (text.equals("editor:onChange()"))
					setDirty();
				else
					browser.setData(text);

			}
		});

		browser.setUrl(App.getJetty().editor());

	}

	protected void setDirty() {
		App.br.post(Events.EVENT_SET_SECTIONVIEW_DIRTY, section);
	}

	private void setText() {

		String text = srv.getText(section.getId());

		editor_content = text == null ? "" : text.replace("\n", "\\n")
				.replace("\r", "\\r").replace("\t", "\\t").replace("'", "\\'");

		if (loadCompleted)
			browser.execute("setContent('" + editor_content + "');");

	}

	public String getText() {
		String content = "";

		boolean executed = browser.execute("window.status=getContent();");

		if (executed) {
			content = (String) browser.getData();
		}

		return content;
	}

	public void addLink(Integer id, String text) {
		String link = " <a href='#' class='picture-link image$id' >$text</a> ";
		link = link.replace("$id", id.toString());
		link = link.replace("$text", text);
		// browser.execute("tinyMCE.activeEditor.execCommand('mceInsertContent', false, \""
		// + link + "\");");
		browser.execute("CKEDITOR.instances.editor1.insertHtml(\" " + link
				+ "\");");
		// + link + "\");");

	}

	public void updateUrl() {

		browser.setUrl(App.getJetty().editor());
	}
}
