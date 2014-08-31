package ebook.module.text.views;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.TextConnection;
import ebook.utils.Strings;
import ebook.utils.Utils;

@Creatable
public class FindDialog extends Dialog {

	Text text;
	Button btn1, btn2, btn3;
	private String line = "";
	private TextConnection con;
	private ContextInfo item;

	@Inject
	public FindDialog(Shell parentShell, TextConnection con, ContextInfo item) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
		this.con = con;
		this.item = item;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Найти", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Закрыть", false);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.title("FindDialogTitle"));

		shell.setImage(Utils.getImage("source.png"));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(330, 150);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite body = (Composite) super.createDialogArea(parent);

		body.setLayout(new FillLayout(SWT.VERTICAL));
		Composite cont;

		cont = new Composite(body, SWT.NONE);
		cont.setLayout(new GridLayout(2, false));
		Label l = new Label(cont, SWT.NONE);
		l.setImage(Utils.getImage("search.png"));
		text = new Text(cont, SWT.SINGLE | SWT.BORDER);
		text.setMessage("Введите текст для поиска");

		// if (!line.isEmpty())
		text.setText(line);

		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);

		cont = new Composite(body, SWT.NONE);
		cont.setLayout(new RowLayout());

		btn1 = new Button(cont, SWT.RADIO);
		btn1.setText("Конфигурация");
		btn1.setImage(Utils.getImage("markers/root.png"));
		btn2 = new Button(cont, SWT.RADIO);
		btn2.setText("Объект");
		btn2.setImage(Utils.getImage("markers/object.png"));
		btn3 = new Button(cont, SWT.RADIO);
		btn3.setImage(Utils.getImage("markers/module.png"));
		btn3.setText("Модуль");
		btn3.setSelection(true);

		// if (!line.isEmpty())
		// btn3.setFocus();
		return body;
	}

	@Override
	protected void okPressed() {

		if (btn1.getSelection()) {
			con.srv().buildText(item, line, false);
		} else if (btn2.getSelection()) {
			con.srv().buildText(item, line, true);
		} else if (btn3.getSelection()) {

		}
		;

		super.okPressed();

	}

	public void setText(String line) {
		this.line = line;
	}

}
