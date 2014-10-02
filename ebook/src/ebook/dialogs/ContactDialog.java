package ebook.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

import ebook.auth.Request;
import ebook.utils.Const;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

@Creatable
public class ContactDialog extends Dialog {

	private Combo typeField;
	private Text loginField;
	// private Text passwordField;

	private Text messageField;
	// private Text statusField;
	// private Text ntpField;

	Shell shell;

	// @Inject UISynchronize sync;
	@Inject
	IEventBroker br;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	@Inject
	public ContactDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		shell = getShell();

		parent.setToolTipText("");

		GridData gridData;
		Label label;

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		// ************* ПОЧТА ************************

		label = new Label(container, SWT.LEFT);
		label.setText("Email:");

		loginField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		loginField.setLayoutData(gridData);

		// ************* ТИП СООБЩЕНИЯ ************************

		label = new Label(container, SWT.LEFT);
		label.setText("Тип сообщения:");

		typeField = new Combo(container, SWT.READ_ONLY | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		typeField.setLayoutData(gridData);
		typeField.setItems(new String[] { "Ошибка", "Вопрос", "Предложение",
				"Другое" });
		typeField.setText("Ошибка");

		// ************** СООБЩЕНИЕ ***********************

		label = new Label(container, SWT.LEFT);
		label.setText("Сообщение:");
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);

		messageField = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		GridData gridData_1 = new GridData();
		gridData_1.heightHint = 100;
		gridData_1.horizontalAlignment = GridData.FILL;
		gridData_1.verticalAlignment = GridData.FILL;
		gridData_1.grabExcessHorizontalSpace = true;
		gridData_1.grabExcessVerticalSpace = true;
		gridData_1.horizontalSpan = 2;
		messageField.setLayoutData(gridData_1);

		initContents();

		messageField.setFocus();

		return area;
	}

	protected void initContents() {
		Preferences preferences = PreferenceSupplier.getScoupNode();
		loginField.setText(preferences.get("P_LOGIN", Strings.pref("P_LOGIN")));

		// serialField.setText(preferences.get("P_SERIAL",
		// Strings.pref("P_SERIAL")));

		// ntpField.setText(PreferenceSupplier.get(PreferenceSupplier.NTPSERVER));

	}

	@Override
	protected void okPressed() {

		if (loginField.getText().trim().isEmpty()) {
			MessageDialog.openWarning(shell, Strings.title("appTitle"),
					"Не указана электронная почта!");
			return;
		}

		if (messageField.getText().trim().isEmpty()) {
			MessageDialog.openWarning(shell, Strings.title("appTitle"),
					"Не заполнено сообщение!");
			return;
		}

		Request msg = new Request();

		msg.name = loginField.getText().trim();
		msg.activationRequest = false;
		msg.msg_type = typeField.getText().trim();
		msg.msg_text = messageField.getText().trim();

		Request response = new Request();
		try {
			response = msg.send(Const.URL_CONTACT());
		} catch (Exception e) {

			MessageDialog.openError(shell, Strings.title("appTitle"),
					Const.MSG_CONTACT_FAIL + msg.getError(e));
			return;
		}

		if (!response.error.isEmpty()) {
			MessageDialog.openError(shell, Strings.title("appTitle"),
					Const.MSG_CONTACT_FAIL + msg.getError(response.error));
			return;
		}

		MessageDialog
				.openInformation(
						shell,
						Strings.title("appTitle"),
						"Сообщение отправлено.\n"
								+ "На указанную почту придёт письмо-подтверждение и ответ.\n"
								+ "Спасибо за внимание к проекту)");
		return;
		// if (!MessageDialog.openConfirm(shell, Strings.title("appTitle"),
		// "Отправить сообщение в support?"))
		// return;

		// super.okPressed();

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Отправить", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Закрыть", false);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.title("ContactDialogTitle"));
		shell.setImage(Utils.getImage("favicon.png"));
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(673, 464);
	}

}
