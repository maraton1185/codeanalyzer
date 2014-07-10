package ebook.module.book.views;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.views.section.tools.BrowserComposite;
import ebook.utils.PreferenceSupplier;

public class StartView {

	FormToolkit toolkit;
	ScrolledForm form;

	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	BookConnection book;

	@Inject
	public StartView() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		// ImageHyperlink link;
		// Hyperlink hlink;
		// Button button;
		// Label label;
		//

		parent.setLayout(new FillLayout());

		parent.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		if (!App.getJetty().isStarted()) {
			toolkit = new FormToolkit(parent.getDisplay());
			form = toolkit.createScrolledForm(parent);
			form.setSize(448, 377);
			form.setLocation(0, 0);
			ColumnLayout layout = new ColumnLayout();
			layout.maxNumColumns = 2;
			form.getBody().setLayout(layout);

			form.setText("Не запущен web-сервер!");
			return;
		}

		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		URL url_bundle = FileLocator.find(bundle, new Path(
				"web/info/index.html"), null);
		try {
			URL url_file = FileLocator.toFileURL(url_bundle);
			new BrowserComposite(parent, url_file.toString());

		} catch (IOException e) {

			toolkit = new FormToolkit(parent.getDisplay());
			form = toolkit.createScrolledForm(parent);
			form.setSize(448, 377);
			form.setLocation(0, 0);
			ColumnLayout layout = new ColumnLayout();
			layout.maxNumColumns = 2;
			form.getBody().setLayout(layout);

			form.setText("Очень просто)");

		}

	}
}
