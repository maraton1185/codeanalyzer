package codeanalyzer.dialogs;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.core.interfaces.ILoaderService.operationType;
import codeanalyzer.db.DbInfo.SQLConnection;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Utils;

@Creatable
public class EditDialog extends Dialog {

	IDbManager dbManager = pico.get(IDbManager.class);
	
	private Text nameField;
	private Text pathField;
	private Text db_pathField;
	private Text sql_pathField;
	private Text sql_userField;
	private Text sql_passwordField;
	
	private IDb db;	
	
	HashMap<operationType, Button> radioBtns = new HashMap<operationType, Button>();

	IEventBroker br; 
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public EditDialog(Shell parentShell, @Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db, IEventBroker br) {
		super(parentShell);		
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
 
		if(db==null)
		{
			db = pico.get(IDb.class);
			db.load(UUID.randomUUID().toString());
			dbManager.add(db);
		}
		this.db = db;
		this.br = br;
	}
	
	protected void setValues()
	{
		db.setName(nameField.getText());
		db.setPath(pathField.getText());
		db.setDbPath(db_pathField.getText());
		db.setSQL(sql_pathField.getText(), sql_userField.getText(), sql_passwordField.getText());
		db.save();	
	}
	
	@Override
	protected void okPressed() {

		setValues();

		dbManager.execute(db);

		initContents();

		super.okPressed();
	}

	protected void configureShell(Shell shell) {
	      super.configureShell(shell);
	      shell.setText(db.getName());
	   }
	
	@Override
	public boolean close() {
		setValues();
		br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);
		return super.close();
	}

	protected void initContents()
	{
		nameField.setText(db.getName());
		pathField.setText(db.getPath().toString());
		db_pathField.setText(db.getDbPath().toString());
		SQLConnection sql = db.getSQL();
		sql_pathField.setText(sql.path);
		sql_userField.setText(sql.user);
		sql_passwordField.setText(sql.password);
		
		radioBtns.get(db.getType()).setSelection(true);
		
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,
				"���������", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				"�������", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(487, 343);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	@PostConstruct
	protected Control createDialogArea(Composite parent) {
		parent.setToolTipText("");
		
		GridData gridData;
		Label label;
		Button button;
		Group group;
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//************* ��� ************************
		
		label = new Label(container, SWT.RIGHT);
		label.setAlignment(SWT.RIGHT);
		label.setText("��� ������������:");
		
		nameField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		nameField.setLayoutData(gridData);

	    //************** ������� ��� �������� ***********************
		
		label = new Label(container, SWT.LEFT);
		label.setText("������� ��� ��������:");
		
		pathField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();		
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		pathField.setLayoutData(gridData);
		
		button = new Button(container, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForPath();
			}
		});
		button.setText("...");

		//************** ���� ���� ������ ***********************
		
		label = new Label(container, SWT.LEFT);
		label.setText("���� ���� ������:");
				
		db_pathField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();		
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		db_pathField.setLayoutData(gridData);

		button = new Button(container, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForFile();
			}
		});
		button.setText("...");

		//************** ���� � SQL ***********************
		
		label = new Label(container, SWT.LEFT);
		label.setText("������ SQL-����������:");

		sql_pathField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 3;
		sql_pathField.setLayoutData(gridData);
		
		label = new Label(container, SWT.LEFT);
		label.setText("�����, ������ � ���� MS SQL:");
				
		sql_userField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();		
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		sql_userField.setLayoutData(gridData);
		
		sql_passwordField = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();		
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 1;
		sql_passwordField.setLayoutData(gridData);
		sql_passwordField.setEchoChar('*');
		
		button = new Button(container, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDefaultSQL();
			}
		});
		button.setText("<");
		
		
		//************** �������� ��� ���������� ***********************
		
		group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		for (final operationType key : operationType.values()) {
			button = new Button(group, SWT.RADIO);	
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					db.setType(key);
				}
			});
			button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			button.setText(pico.get(IDbManager.class).getOperationName(key));
			radioBtns.put(key, button);
		} 
				 		
		initContents();
		
		return area;
	}

	protected void setDefaultSQL() {
		SQLConnection sql = db.getDefaultSQL();
		sql_pathField.setText(sql.path);
		sql_userField.setText(sql.user);
		sql_passwordField.setText(sql.password);		
	}

	protected void browseForFile() {
		Utils.browseForFile(db_pathField, getShell());
	}

	protected void browseForPath() {
		Utils.browseForPath(pathField, getShell());				
	}
}
