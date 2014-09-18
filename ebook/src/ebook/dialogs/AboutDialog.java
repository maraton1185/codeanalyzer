package ebook.dialogs;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ebook.module.book.views.tools._BrowserComposite;
import ebook.module.book.views.tools.TextEdit;
import ebook.utils.Strings;
import ebook.utils.Utils;

@Creatable
public class AboutDialog extends Dialog {

	@Inject
	public AboutDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Закрыть", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite body = (Composite) super.createDialogArea(parent);
		body.setLayout(new FillLayout());
		try {
			Bundle bundle = FrameworkUtil.getBundle(TextEdit.class);
			URL url_bundle = FileLocator.find(bundle, new Path("version.txt"),
					null);
			URL url = FileLocator.toFileURL(url_bundle);
			new _BrowserComposite(body, url.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.title("AboutDialogTitle"));
		shell.setImage(Utils.getImage("favicon.png"));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(673, 464);
	}
}
