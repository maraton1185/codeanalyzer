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

public class TextPreview extends Composite {

	protected Browser browser;
	protected String editor_content;
	protected boolean loadCompleted = false;

	public TextPreview(Composite parent) {
		super(parent, SWT.None);

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
				// String text = event.text;
				// if (text.equals("editor:onInit()"))
				// setText();
				// else if (text.equals("editor:onChange()"))
				// setDirty();
				// else
				// browser.setData(text);

			}
		});

		// browser.setUrl(App.getJetty().editor());

	}

	protected void setDirty() {
		// App.br.post(Events.EVENT_SET_SECTIONVIEW_DIRTY, section);
	}

	public void setText(String text) {

		text = ""
				+ "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>"
				+ text + "</body></html>";
		browser.setText(text);
	}
	// public String getText() {
	// String content = "";
	//
	// boolean executed = browser.execute("window.status=getContent();");
	//
	// if (executed) {
	// content = (String) browser.getData();
	// }
	//
	// return content;
	// }
	//
	// public void addLink(Integer id, String text) {
	// String link = " <a href='#' class='picture-link image$id' >$text</a> ";
	// link = link.replace("$id", id.toString());
	// link = link.replace("$text", text);
	// //
	// browser.execute("tinyMCE.activeEditor.execCommand('mceInsertContent', false, \""
	// // + link + "\");");
	// browser.execute("CKEDITOR.instances.editor1.insertHtml(\" " + link
	// + "\");");
	// // + link + "\");");
	//
	// }
	//
	// public void updateUrl() {
	//
	// browser.setUrl(App.getJetty().editor());
	// }
}
