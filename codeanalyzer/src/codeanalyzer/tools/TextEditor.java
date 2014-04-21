package codeanalyzer.tools;

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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class TextEditor extends Composite {

	protected Browser browser;
	protected String editor_content;
	protected boolean loadCompleted = false;

	public TextEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, true));

		browser = new Browser(this, SWT.NONE);
		browser.setJavascriptEnabled(true);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Listen to control resized
		browser.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if (loadCompleted) {
					browser.execute("tinyMCE.activeEditor.getContentAreaContainer().height="
							+ (browser.getClientArea().height - 70));

					super.controlResized(e);
				}
			}
		});

		// Set url pointed to editor
		try {
			Bundle bundle = FrameworkUtil.getBundle(TextEditor.class);
			URL url_bundle = FileLocator.find(bundle, new Path(
					"web_editor/tinymce/index.html"), null);
			URL url_file = FileLocator.toFileURL(url_bundle);

			browser.setUrl(url_file.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set content of editor after load completed
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
			}

			@Override
			public void completed(ProgressEvent event) {
				loadCompleted = true;
				setText(editor_content);
			}
		});

		// Listen to status change event
		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				browser.setData("leet-content", event.text);
			}
		});
	}

	/**
	 * Set the content of the HTML editor.
	 * 
	 * @param String
	 *            text
	 */
	public void setText(String text) {
		editor_content = text == null ? "" : text.replace("\n", "").replace(
				"'", "\\'");

		if (loadCompleted) {
			/**
			 * [TimPietrusky] 20120416 - tinyMCE might not yet been "completely"
			 * initialized
			 */
			browser.execute("setContent('" + editor_content + "');");
		}
	}

	/**
	 * Returns the content of the HTML editor.
	 * 
	 * @return String
	 */
	public String getText() {
		String content = "";

		boolean executed = browser.execute("window.status=getContent();");

		if (executed) {
			content = (String) browser.getData("leet-content");
		}

		return content;
	}

}
