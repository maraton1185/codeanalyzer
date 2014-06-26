package ebook.module.conf;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
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

import ebook.core.App;
import ebook.module.conf.interfaces.ILoaderManager.operationType;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions.DbState;
import ebook.utils.Events;
import ebook.utils.Utils;

@Creatable
public class LoadDialog extends Dialog {

	ConfManager dbManager = App.mng.cm();

	private Boolean dbPathModified = false;

	private Text name;
	private Text load_path;
	private Text db_full_path;
	// private Text sql_pathField;
	// private Text sql_userField;
	// private Text sql_passwordField;
	private Button btnCheckButton;
	// private Button btnUpdateName;

	private final ListConfInfo conf;

	HashMap<operationType, Button> radioBtns = new HashMap<operationType, Button>();

	private Button btnDeleteSourceFiles;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	@Inject
	public LoadDialog(Shell parentShell, ListConfInfo conf) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);

		this.conf = conf;

	}

	protected void updateName() {
		updateDb();
		name.setText(conf.getName());
		name.setEditable(!conf.getAutoName());
	}

	protected void updateDb() {
		conf.setName(name.getText());
		conf.setLoadPath(load_path.getText());
		conf.setDbFullPath(db_full_path.getText());
		// db.setSQL(sql_pathField.getText(), sql_userField.getText(),
		// sql_passwordField.getText());
		conf.setAutoName(btnCheckButton.getSelection());
		conf.setDeleteSourceFiles(btnDeleteSourceFiles.getSelection());
		if (dbPathModified)
			conf.setState(DbState.notLoaded);
	}

	protected void initContents() {
		name.setText(conf.getName());
		load_path.setText(conf.getLoadPath().toString());
		db_full_path.setText(conf.getDbFullPath().toString());
		btnCheckButton.setSelection(conf.getAutoName());
		radioBtns.get(conf.getType()).setSelection(true);

		// SQLConnection sql = db.getSQL();
		// sql_userField.setText(sql.user);
		// sql_passwordField.setText(sql.password);

		// sql_pathField.setText(sql.path);

		name.setEditable(!conf.getAutoName());

		btnDeleteSourceFiles.setSelection(conf.getDeleteSourceFiles());

		dbPathModified = false;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	@PostConstruct
	protected Control createDialogArea(Composite parent) {
		parent.setToolTipText("");
		parent.getShell().setImage(Utils.getImage("favicon.png"));
		GridData gridData;
		Label label;
		Button button;
		Group group;

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		// ************* Имя ************************

		label = new Label(container, SWT.RIGHT);
		label.setAlignment(SWT.RIGHT);
		label.setText("Имя конфигурации:");

		name = new Text(container, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 267;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		name.setLayoutData(gridData);

		btnCheckButton = new Button(container, SWT.FLAT | SWT.CHECK
				| SWT.CENTER);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateName();
			}
		});
		btnCheckButton.setToolTipText("Формировать имя автоматически");

		// ************** КАТАЛОГ ДЛЯ ЗАГРУЗКИ ***********************

		label = new Label(container, SWT.LEFT);
		label.setText("Каталог для загрузки:");

		load_path = new Text(container, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		load_path.setLayoutData(gridData);

		button = new Button(container, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForPath();
				updateName();
			}
		});
		button.setText("...");

		// ************** ФАЙЛ БАЗЫ ДАННЫХ ***********************

		label = new Label(container, SWT.LEFT);
		label.setText("Файл базы данных:");

		db_full_path = new Text(container, SWT.SINGLE | SWT.BORDER
				| SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		db_full_path.setLayoutData(gridData);

		button = new Button(container, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForFile();
				updateName();
				dbPathModified = true;
			}
		});
		button.setText("...");

		// ************** ПУТЬ К SQL ***********************

		// label = new Label(container, SWT.LEFT);
		// label.setText("Строка SQL-соединения:");
		//
		// sql_pathField = new Text(container, SWT.SINGLE | SWT.BORDER);
		// gridData = new GridData();
		// gridData.horizontalAlignment = GridData.FILL;
		// gridData.grabExcessHorizontalSpace = true;
		// gridData.horizontalSpan = 2;
		// sql_pathField.setLayoutData(gridData);
		//
		// btnUpdateName = new Button(container, SWT.FLAT);
		// btnUpdateName.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// updateName();
		// }
		// });
		// btnUpdateName.setToolTipText("Обновить имя");
		// btnUpdateName.setText("...");
		//
		// label = new Label(container, SWT.LEFT);
		// label.setText("Логин, пароль к базе MS SQL:");
		//
		// sql_userField = new Text(container, SWT.SINGLE | SWT.BORDER);
		// gridData = new GridData();
		// gridData.horizontalAlignment = GridData.FILL;
		// gridData.grabExcessHorizontalSpace = true;
		// gridData.horizontalSpan = 1;
		// sql_userField.setLayoutData(gridData);
		//
		// sql_passwordField = new Text(container, SWT.SINGLE | SWT.BORDER);
		// gridData = new GridData();
		// gridData.horizontalAlignment = GridData.FILL;
		// gridData.grabExcessHorizontalSpace = true;
		// gridData.horizontalSpan = 1;
		// sql_passwordField.setLayoutData(gridData);
		// sql_passwordField.setEchoChar('*');
		//
		// button = new Button(container, SWT.FLAT);
		// button.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// setDefaultSQL();
		// }
		// });
		// button.setText("<");

		// ************** ОПЕРАЦИИ ДЛЯ ВЫПОЛНЕНИЯ ***********************

		group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4,
				1));

		btnDeleteSourceFiles = new Button(container, SWT.CHECK);
		btnDeleteSourceFiles.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 4, 1));
		btnDeleteSourceFiles
				.setText("Удалять исходные файлы при загрузке/обновлении из каталога");

		for (final operationType key : operationType.values()) {

			String opName = App.mng.cm().getOperationName(key);

			if (opName == null) {
				continue;
			}

			button = new Button(group, SWT.RADIO);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					conf.setType(key);
					updateName();
				}
			});
			button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
					false, 1, 1));
			button.setText(opName);
			radioBtns.put(key, button);
		}

		initContents();

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Выполнить", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Закрыть", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(487, 396);
	}

	protected void setDefaultSQL() {
		// SQLConnection sql = db.getDefaultSQL();
		// sql_pathField.setText(sql.path);
		// sql_userField.setText(sql.user);
		// sql_passwordField.setText(sql.password);
	}

	protected void browseForFile() {
		Utils.browseForFile(db_full_path, getShell());
	}

	protected void browseForPath() {
		Utils.browseForPath(load_path, getShell());
	}

	@Override
	protected void okPressed() {

		updateDb();
		App.mng.clm().save(conf, getShell());
		dbManager.execute(conf, getShell());

		// initContents();
		App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);

		super.okPressed();

	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(conf.getName());
	}

	@Override
	public boolean close() {
		updateDb();
		App.mng.clm().save(conf, getShell());
		App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);
		return super.close();
	}
}
