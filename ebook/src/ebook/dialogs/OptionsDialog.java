package ebook.dialogs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.views.tools.BrowserComposite;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OptionsDialog {

	private class Dialog extends PreferenceDialog {

		public Dialog(Shell parentShell, PreferenceManager manager) {
			super(parentShell, manager);
		}

		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(Strings.get("appTitle"));
			newShell.setImage(Utils.getImage("favicon.png"));
		}

	}

	public void open() {

		// Create the preference manager
		PreferenceManager mgr = new PreferenceManager();
		PreferenceNode p;
		// Create the nodes
		p = new PreferenceNode("", new FieldEditorPageCommon());
		mgr.addToRoot(p);
		p = new PreferenceNode("", new FieldEditorPageBrowser());
		mgr.addToRoot(p);
		// p = new PreferenceNode("", new FieldEditorPageTwo());
		// mgr.addToRoot(p);

		// Create the preferences dialog
		PreferenceDialog dlg = new Dialog(null, mgr);
		dlg.setPreferenceStore(PreferenceSupplier.getPreferenceStore());

		// Open the dialog
		dlg.open();

		PreferenceSupplier.save();

	}

	// *************************
	class FieldEditorPageCommon extends FieldEditorPreferencePage {
		public FieldEditorPageCommon() {
			// Use the "flat" layout
			super(GRID);
			setTitle("�����");
		}

		// Composite fonts;
		// List<FieldEditor> fontsFieldEditors = new ArrayList<FieldEditor>();
		//
		// protected FieldEditor addEditorToFontsGroup(FieldEditor editor) {
		// fontsFieldEditors.add(editor);
		// return editor;
		// }

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {

			FieldEditor f;
			Composite comp;
			Group group;
			comp = getFieldEditorParent();

			f = new BooleanFieldEditor(PreferenceSupplier.MINIMIZE_TO_TRAY,
					"����������� � ����", comp);
			addField(f);

			// f = new BooleanFieldEditor(
			// PreferenceSupplier.SHOW_ABOUT_ON_STARTUP,
			// "���������� \"� ���������\" ��� �������", comp);
			// addField(f);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP,
					"�������������� ��� �������", comp);
			addField(f);

			f = new StringFieldEditor(PreferenceSupplier.APP_BRAND,
					"��������� ���������", comp);
			addField(f);

			f = new BooleanFieldEditor(
					PreferenceSupplier.CHECK_UPDATE_ON_STARTUP,
					"��������� ���������� ��� �������", comp);
			addField(f);

			f = new StringFieldEditor(PreferenceSupplier.UPDATE_SITE,
					"����� ����� ����������", comp);
			addField(f);
			// BooleanFieldEditor f5 = new BooleanFieldEditor(
			// PreferenceSupplier.SHOW_BOOK_PERSPECTIVE,
			// "��������� ������ ���� ��� �������", comp);
			// addField(f5);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("��������� web-�������:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new IntegerFieldEditor(PreferenceSupplier.IMAGE_WIDTH,
					"������ �������� (� ��������)", comp);
			addField(f);

			f = new IntegerFieldEditor(PreferenceSupplier.SESSION_TIMEOUT,
					"������� ������ (� �������)", comp);
			addField(f);

			f = new BooleanFieldEditor(PreferenceSupplier.START_JETTY,
					"��������� web-������", comp);
			addField(f);

			f = new IntegerFieldEditor(PreferenceSupplier.REMOTE_PORT,
					"���� web-�������", comp);
			addField(f);

			f = new BooleanFieldEditor(PreferenceSupplier.EXTERNAL_JETTY_BASE,
					"������� ������� web-�������", comp);
			addField(f);

			f = new DirectoryFieldEditor(PreferenceSupplier.JETTY_BASE,
					"������� web-�������", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			//

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("�������� �� ���������:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_CONF_DIRECTORY,
					"��� ������������:", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_BOOK_DIRECTORY, "��� ����:",
					comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("��������� ����:");

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new BooleanFieldEditor(PreferenceSupplier.OPEN_BOOK_ON_STARTUP,
					"��������� ����� ��� �������", comp);
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new MyFileFieldEditor(PreferenceSupplier.BOOK_ON_STARTUP,
					"�����:", comp);
			((FileFieldEditor) f).setChangeButtonText("...");
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayout(new GridLayout(2, false));
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new FontFieldEditor(PreferenceSupplier.FONT, "�����:", comp);
			((FontFieldEditor) f).setChangeButtonText("...");
			addField(f);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			// comp.setLayout(new GridLayout(2, false));

			// BooleanFieldEditor f10 = new BooleanFieldEditor(
			// PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW,
			// "�� ��������� �������� \"��� �������� � ������\"", comp);
			// addField(f10);

			// comp = new Composite(group, SWT.BORDER);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			// comp.setLayout(new FillLayout());

		}
	}

	class FieldEditorPageBrowser extends FieldEditorPreferencePage {
		public FieldEditorPageBrowser() {
			// Use the "flat" layout
			super(FLAT);
			setTitle("�������");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {

			Composite body = getFieldEditorParent();
			body.setLayoutData(new GridData(GridData.FILL_BOTH));
			body.setLayout(new FillLayout());
			String url = "about:config";
			new BrowserComposite(body, url.toString());
			// Add a boolean field

			// BooleanFieldEditor bfe = new BooleanFieldEditor("myBoolean",
			// "Boolean", getFieldEditorParent());
			// addField(bfe);
			//
			// // Add a color field
			// ColorFieldEditor cfe = new ColorFieldEditor("myColor", "Color:",
			// getFieldEditorParent());
			// addField(cfe);
			//
			// // Add a directory field
			// DirectoryFieldEditor dfe = new
			// DirectoryFieldEditor("myDirectory",
			// "Directory:", getFieldEditorParent());
			// addField(dfe);
			//
			// // Add a file field
			// FileFieldEditor ffe = new FileFieldEditor("myFile", "File:",
			// getFieldEditorParent());
			// addField(ffe);
			//
			// // Add a font field
			// FontFieldEditor fontFe = new FontFieldEditor("myFont", "Font:",
			// getFieldEditorParent());
			// addField(fontFe);
			//
			// // Add a radio group field
			// RadioGroupFieldEditor rfe = new RadioGroupFieldEditor(
			// "myRadioGroup", "Radio Group", 2, new String[][] {
			// { "First Value", "first" },
			// { "Second Value", "second" },
			// { "Third Value", "third" },
			// { "Fourth Value", "fourth" } },
			// getFieldEditorParent(), true);
			// addField(rfe);
			//
			// // Add a path field
			// PathEditor pe = new PathEditor("myPath", "Path:",
			// "Choose a Path",
			// getFieldEditorParent());
			// addField(pe);
		}

		// @Override
		// protected Control createContents(Composite parent) {
		// Composite body = parent;
		// body.setLayout(new FillLayout());
		// String url = "about:config";
		// new BrowserComposite(body, url.toString());
		//
		// return super.createContents(body);
		// }

	}

	class FieldEditorPageTwo extends FieldEditorPreferencePage {
		public FieldEditorPageTwo() {
			// Use the "grid" layout
			super(GRID);
			setTitle("������");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {
			// Add an integer field
			IntegerFieldEditor ife = new IntegerFieldEditor("myInt", "Int:",
					getFieldEditorParent());
			addField(ife);

			// Add a scale field
			ScaleFieldEditor sfe = new ScaleFieldEditor("myScale", "Scale:",
					getFieldEditorParent(), 0, 100, 1, 10);
			addField(sfe);

			// Add a string field
			StringFieldEditor stringFe = new StringFieldEditor("myString",
					"String:", getFieldEditorParent());
			addField(stringFe);
		}
	}

}
