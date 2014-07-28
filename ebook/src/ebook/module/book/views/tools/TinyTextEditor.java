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
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;

public class TinyTextEditor extends Composite {

	protected Browser browser;
	protected String editor_content;
	protected boolean loadCompleted = false;

	SectionInfo section;

	public TinyTextEditor(Composite parent, SectionInfo section) {
		super(parent, SWT.None);

		this.section = section;

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
				browser.execute("setContent('" + editor_content + "');");
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
				if (text.equals("tinymce:onInit()"))
					setText(editor_content);
				else if (text.equals("tinymce:onChange()"))
					setDirty();
				else
					browser.setData(text);

			}
		});

		// Set url pointed to editor
		// try {
		// String url = App.getJetty().host()
		// + App.getJetty().section(book.getTreeItem().getId(),
		// section.getId());
		//
		// Bundle bundle = FrameworkUtil.getBundle(TinyTextEditor.class);
		// URL url_bundle = FileLocator.find(bundle, new Path(
		// "web/tinyMCE/index.html"), null);
		// URL url_file = FileLocator.toFileURL(url_bundle);

		browser.setUrl(App.getJetty().editor());

		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	protected void setDirty() {
		App.br.post(Events.EVENT_SET_SECTIONVIEW_DIRTY, section);
	}

	public void setText(String text) {
		editor_content = text == null ? "" : text.replace("\n", "").replace(
				"'", "\\'");

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
		String link = "<a href='#' class='picture-link image$id' >$text</a>";
		link = link.replace("$id", id.toString());
		link = link.replace("$text", text);
		browser.execute("tinyMCE.activeEditor.execCommand('mceInsertContent', false, \""
				+ link + "\");");
	}
}
