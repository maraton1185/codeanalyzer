package ebook.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import ebook.auth.ActivationInfo;
import ebook.auth.interfaces.IAuthorize;
import ebook.core.pico;
import ebook.utils.Const;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

@Creatable
public class ActivateDialog extends Dialog {

	private Text loginField;
	private Text passwordField;

	private Text serialField;
	private Text statusField;
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
	public ActivateDialog(Shell parentShell) {
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
		Button button;

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		// ************* ПОЧТА ************************

		label = new Label(container, SWT.LEFT);
		label.setText("Почта:");

		loginField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		loginField.setLayoutData(gridData);

		// ************** ПАРОЛЬ ***********************

		label = new Label(container, SWT.LEFT);
		label.setText("Пароль:");

		passwordField = new Text(container, SWT.SINGLE | SWT.BORDER);
		passwordField.setEchoChar('*');
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		passwordField.setLayoutData(gridData);

		// ************* NTP-server ************************

		// label = new Label(container, SWT.LEFT);
		// label.setText("NTP-сервер:");
		//
		// ntpField = new Text(container, SWT.SINGLE | SWT.BORDER);
		// gridData = new GridData();
		// gridData.horizontalAlignment = GridData.FILL;
		// gridData.horizontalSpan = 2;
		// gridData.grabExcessHorizontalSpace = true;
		// ntpField.setLayoutData(gridData);
		// ntpField.setEditable(false);

		// ************** СЕРИЙНИК ***********************

		label = new Label(container, SWT.LEFT);
		label.setText("Серийный номер:");
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);

		serialField = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		GridData gridData_1 = new GridData();
		gridData_1.heightHint = 100;
		gridData_1.horizontalAlignment = GridData.FILL;
		gridData_1.grabExcessHorizontalSpace = true;
		gridData_1.horizontalSpan = 2;
		serialField.setLayoutData(gridData_1);

		// **************** СТАТУС *********************

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
				1, 1));

		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCompUUIDOnClick();
			}
		});
		button.setText("UUID");

		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnActivateOnClick();
			}
		});
		// button.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.BOLD));
		button.setText("Активировать");

		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckOnClick();
			}
		});
		button.setText("Проверить");

		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRegisterOnClick();
			}
		});
		button.setText("Продлить");

		statusField = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		// statusField.setFont(SWTResourceManager.getFont("Tahoma", 10,
		// SWT.BOLD));
		// statusField.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		statusField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 1));
		statusField.setEditable(false);

		initContents();

		return area;
	}

	protected void btnRegisterOnClick() {

		Program.launch(Const.URL_proLinkOpen);

	}

	protected void btnCheckOnClick() {
		setValues();
		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {

				ActivationInfo info = pico.get(IAuthorize.class).getInfo();
				statusField.setText(info.FullMessage());
			}
		});

	}

	protected void btnActivateOnClick() {

		final String login = loginField.getText();
		final String password = passwordField.getText();

		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {

				try {
					ActivationInfo info = pico.get(IAuthorize.class).Activate(
							login, password);

					statusField.setText(info.message);
					if (!info.serial.isEmpty()) {
						serialField.setText(info.serial);
						setValues();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					statusField.setText(e1.getMessage());
				}
			}
		});

	}

	protected void btnCompUUIDOnClick() {
		BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
			@Override
			public void run() {
				try {
					statusField.setText(ActivationInfo.getComputerSerial());
				} catch (Exception e) {
					statusField.setText(Const.MSG_GETID);
				}
			}
		});
	}

	protected void initContents() {
		Preferences preferences = PreferenceSupplier.getScoupNode();
		loginField.setText(preferences.get("P_LOGIN", Strings.pref("P_LOGIN")));
		passwordField.setText(preferences.get("P_PASSWORD",
				Strings.pref("P_PASSWORD")));
		serialField.setText(preferences.get("P_SERIAL",
				Strings.pref("P_SERIAL")));

		// ntpField.setText(PreferenceSupplier.get(PreferenceSupplier.NTPSERVER));

	}

	protected void setValues() {
		Preferences preferences = PreferenceSupplier.getScoupNode();
		preferences.put("P_LOGIN", loginField.getText());
		preferences.put("P_PASSWORD", passwordField.getText());
		preferences.put("P_SERIAL", serialField.getText());
		try {
			// forces the application to save the preferences
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "Закрыть", true);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.title("ActivateDialogTitle"));
		shell.setImage(Utils.getImage("favicon.png"));
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(673, 464);
	}

	@Override
	public boolean close() {
		setValues();
		br.post(Events.EVENT_UPDATE_STATUS, null);
		return super.close();
	}

}
