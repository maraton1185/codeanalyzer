package ebook.views;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ebook.module.book.views.tools._BrowserComposite;
import ebook.module.book.views.tools.TextEdit;
import ebook.utils.PreferenceSupplier;

public class AboutView {

	Composite body;
	_BrowserComposite browserComposite;

	@PostConstruct
	public void postConstruct(Composite parent, final EHandlerService hs,
			final ECommandService cs) {

		body = parent;
		body.setLayout(new FillLayout());
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		try {
			Bundle bundle = FrameworkUtil.getBundle(TextEdit.class);
			URL url_bundle = FileLocator.find(bundle, new Path("version.txt"),
					null);
			URL url = FileLocator.toFileURL(url_bundle);
			browserComposite = new _BrowserComposite(body, url.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}