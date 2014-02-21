package codeanalyzer.dialogs;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import codeanalyzer.utils.Strings;

//import ru.codeanalyzer.core.model.ActivationInfo;
//import ru.codeanalyzer.interfaces.IAuthorize;
//import ru.codeanalyzer.interfaces.pico;
//import ru.codeanalyzer.preferences.PreferenceConstants;

@Creatable
public class ActivateDialog extends Dialog {

	private Text loginField;
	private Text passwordField;

	private Text serialField;
	private Text statusField;
	private Text ntpField;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public ActivateDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		parent.setToolTipText("");
		
		GridData gridData;
		Label label;
		Button button;
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//************* ����� ************************
		
		label = new Label(container, SWT.LEFT);
		label.setText("�����:");
		
		loginField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		loginField.setLayoutData(gridData);

	    //************** ������ ***********************
		
		label = new Label(container, SWT.LEFT);
		label.setText("������:");
		
		passwordField = new Text(container, SWT.SINGLE | SWT.BORDER);
		passwordField.setEchoChar('*');
		gridData = new GridData();		
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		passwordField.setLayoutData(gridData);

	    //************* NTP-server ************************
		
		label = new Label(container, SWT.LEFT);
		label.setText("NTP-������:");
		
		ntpField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		ntpField.setLayoutData(gridData);
		ntpField.setEditable(false);

	    //************** �������� ***********************
		
		label = new Label(container, SWT.LEFT);
		label.setText("�������� �����:");
		gridData = new GridData();
	    gridData.horizontalSpan = 3;
	    label.setLayoutData(gridData);
	    
	    serialField = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
	    GridData gridData_1 = new GridData();
		gridData_1.heightHint = 100;
		gridData_1.horizontalAlignment = GridData.FILL;
		gridData_1.grabExcessHorizontalSpace = true;
		gridData_1.horizontalSpan = 2;
		serialField.setLayoutData(gridData_1);
		
	    //**************** ������ *********************
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
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
		//button.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.BOLD));
		button.setText("������������");
		
		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCheckOnClick();	
			}
		});
		button.setText("���������");
		
		button = new Button(composite, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnRegisterOnClick();
			}
		});
		button.setText("��������");
		
		statusField = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
//		statusField.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
//		statusField.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		statusField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		statusField.setEditable(false);
		
		initContents();
		
		return area;
	}

	
	protected void btnRegisterOnClick() {
		// TODO Auto-generated method stub
		
	}

	protected void btnCheckOnClick() {
		// TODO Auto-generated method stub
		
	}

	protected void btnActivateOnClick() {
		// TODO Auto-generated method stub
		
	}

	protected void btnCompUUIDOnClick() {
		// TODO Auto-generated method stub
		
	}

	protected void initContents()
	{
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
		loginField.setText(preferences.get("P_LOGIN", Strings.get("P_LOGIN")));
		passwordField.setText(preferences.get("P_PASSWORD", Strings.get("P_PASSWORD")));
		ntpField.setText(preferences.get("P_NTPSERVER", Strings.get("P_NTPSERVER")));
		serialField.setText(preferences.get("P_SERIAL", Strings.get("P_SERIAL")));
		
	}
	
	protected void setValues()
	{
		
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Strings.get("P_NODE"));
		preferences.put("P_LOGIN", loginField.getText());
		preferences.put("P_PASSWORD", passwordField.getText());
		preferences.put("P_SERIAL", serialField.getText());
		
		try {
			  // forces the application to save the preferences
			  preferences.flush();
		  } catch (BackingStoreException e) {
			    e.printStackTrace();
			  }; 
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID,
				"�������", true);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.get("ActivateDialogTitle"));
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
		return super.close();
	}

}
