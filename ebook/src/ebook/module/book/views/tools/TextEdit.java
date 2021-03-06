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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.ITextEditor;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;

public class TextEdit extends Composite implements ITextEditor {

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

		setRedraw(false);
		browser = new Browser(this, SWT.Resize | SWT.MOZILLA);
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
				if (text.equals("editor:onInit()")) {
					setText();

				} else if (text.equals("editor:onChange()"))
					setDirty();
				else
					browser.setData(text);

			}
		});

		// try {
		// Bundle bundle = FrameworkUtil.getBundle(TextEdit.class);
		// URL url_bundle = FileLocator.find(bundle, new Path(
		// "webroot/tmpl/cleditor/index.jsp"), null);
		// URL url = FileLocator.toFileURL(url_bundle);
		// browser.setUrl(url.toString());
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		browser.setUrl(App.getJetty().editor());

	}

	protected void setDirty() {
		App.br.post(Events.EVENT_SET_SECTIONVIEW_DIRTY, section);
	}

	private void setText() {

		String text = srv.getText(section.getId());

		editor_content = text == null ? "" : text.replace("\n", "\\n")
				.replace("\r", "\\r").replace("\t", "\\t").replace("'", "\\'");

		if (loadCompleted) {
			browser.execute("setContent('" + editor_content + "');");
			setRedraw(true);
		}

	}

	@Override
	public String getText() {
		String content = "";

		boolean executed = browser.execute("window.status=getContent();");

		if (executed) {
			content = (String) browser.getData();
		}

		return content;
	}

	@Override
	public void addLink(Integer id, String text) {
		String link = " <a href='#' class='picture-link image$id' >$text</a> ";
		link = link.replace("$id", id.toString());
		link = link.replace("$text", text);
		browser.execute("CKEDITOR.instances.editor1.insertHtml(\" " + link
				+ "\");");

	}

	@Override
	public void updateUrl() {

		setRedraw(false);
		browser.setUrl(App.getJetty().editor());
		// setRedraw(true);
	}

	@Override
	public void addSectionLink(Integer book, Integer id, String tag,
			String title) {
		String optTitle = PreferenceSupplier
				.get(PreferenceSupplier.SECTION_REF_TITLE);
		Boolean optTarget = PreferenceSupplier
				.getBoolean(PreferenceSupplier.SECTION_REF_TARGET);
		String link = " <a href='$link#$tag' $target>$text</a> ";
		link = link.replace("$link", App.getJetty().section(book, id));
		link = link.replace("$tag", tag);
		if (optTarget)
			link = link.replace("$target", "target=_blank");
		link = link.replace("$text", optTitle.isEmpty() ? title : optTitle);
		browser.execute("CKEDITOR.instances.editor1.insertHtml(\" " + link
				+ "\");");

	}

	@Override
	public void setLayoutData(GridData gridData) {
		super.setLayoutData(gridData);

	}
}
