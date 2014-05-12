package codeanalyzer.views.books.tools;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import codeanalyzer.books.section.SectionInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;

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
		try {
			Bundle bundle = FrameworkUtil.getBundle(TinyTextEditor.class);
			URL url_bundle = FileLocator.find(bundle, new Path(
					"web_editor/index.html"), null);
			URL url_file = FileLocator.toFileURL(url_bundle);

			browser.setUrl(url_file.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void setDirty() {
		AppManager.br.post(Const.EVENT_SET_SECTIONVIEW_DIRTY, section);
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

}
