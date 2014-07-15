package ebook.dialogs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

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
		// p = new PreferenceNode("", new FieldEditorPageOne());
		// mgr.addToRoot(p);
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
			setTitle("Общие");
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

			Composite comp;
			Group group;
			// StringFieldEditor f1 = new StringFieldEditor(
			// PreferenceSupplier.NTPSERVER, "NTP-сервер:",
			// getFieldEditorParent());
			// addField(f1);

			// group = new Group(getFieldEditorParent(), SWT.NULL);
			// group.getParent().setLayout(new GridLayout(2, false));
			// group.setLayout(new GridLayout(1, false));
			// group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
			// 4, 1));
			// group.setText("Действия при запуске:");
			//
			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f11 = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY, "Сворачивать в трей",
					getFieldEditorParent());
			addField(f11);

			BooleanFieldEditor f14 = new BooleanFieldEditor(
					PreferenceSupplier.START_JETTY, "Запускать web-сервер",
					getFieldEditorParent());
			addField(f14);

			IntegerFieldEditor f13 = new IntegerFieldEditor(
					PreferenceSupplier.REMOTE_PORT, "Порт web-сервера",
					getFieldEditorParent());
			addField(f13);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f4 = new BooleanFieldEditor(
					PreferenceSupplier.INIT_EXECUTION,
					"Подключать конфигурации при запуске",
					getFieldEditorParent());
			addField(f4);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f12 = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP,
					"Минимизировать при запуске", getFieldEditorParent());
			addField(f12);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("Каталоги по умолчанию:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			DirectoryFieldEditor f2 = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_CONF_DIRECTORY,
					"Для конфигураций:", comp);
			f2.setChangeButtonText("...");
			addField(f2);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			DirectoryFieldEditor f3 = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_BOOK_DIRECTORY, "Для книг:",
					comp);
			f3.setChangeButtonText("...");
			addField(f3);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("Настройки книг:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f5 = new BooleanFieldEditor(
					PreferenceSupplier.SHOW_BOOK_PERSPECTIVE,
					"Открывать список книг при запуске", comp);
			addField(f5);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f7 = new BooleanFieldEditor(
					PreferenceSupplier.OPEN_BOOK_ON_STARTUP,
					"Открывать книгу при запуске", comp);
			addField(f7);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			FileFieldEditor f8 = new FileFieldEditor(
					PreferenceSupplier.BOOK_ON_STARTUP, "Книга:", comp);
			f8.setChangeButtonText("...");
			addField(f8);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			FontFieldEditor f9 = new FontFieldEditor(PreferenceSupplier.FONT,
					"Шрифт:", comp);
			f9.setChangeButtonText("...");
			addField(f9);

			// Label l = new Label();

			IntegerFieldEditor f15 = new IntegerFieldEditor(
					PreferenceSupplier.IMAGE_WIDTH,
					"Ширина картинок (в пикселах)", comp);
			addField(f15);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			BooleanFieldEditor f10 = new BooleanFieldEditor(
					PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW,
					"Не открывать страницу \"Как работать с книгой\"", comp);
			addField(f10);

			// group = new Group(getFieldEditorParent(), SWT.NULL);
			// group.getParent().setLayout(new GridLayout(2, false));
			// group.setLayout(new GridLayout(1, false));
			// group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
			// 2, 1));
			// group.setText("Другое:");

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		}
	}

	class FieldEditorPageOne extends FieldEditorPreferencePage {
		public FieldEditorPageOne() {
			// Use the "flat" layout
			super(FLAT);
			setTitle("one");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {
			// Add a boolean field
			BooleanFieldEditor bfe = new BooleanFieldEditor("myBoolean",
					"Boolean", getFieldEditorParent());
			addField(bfe);

			// Add a color field
			ColorFieldEditor cfe = new ColorFieldEditor("myColor", "Color:",
					getFieldEditorParent());
			addField(cfe);

			// Add a directory field
			DirectoryFieldEditor dfe = new DirectoryFieldEditor("myDirectory",
					"Directory:", getFieldEditorParent());
			addField(dfe);

			// Add a file field
			FileFieldEditor ffe = new FileFieldEditor("myFile", "File:",
					getFieldEditorParent());
			addField(ffe);

			// Add a font field
			FontFieldEditor fontFe = new FontFieldEditor("myFont", "Font:",
					getFieldEditorParent());
			addField(fontFe);

			// Add a radio group field
			RadioGroupFieldEditor rfe = new RadioGroupFieldEditor(
					"myRadioGroup", "Radio Group", 2, new String[][] {
							{ "First Value", "first" },
							{ "Second Value", "second" },
							{ "Third Value", "third" },
							{ "Fourth Value", "fourth" } },
					getFieldEditorParent(), true);
			addField(rfe);

			// Add a path field
			PathEditor pe = new PathEditor("myPath", "Path:", "Choose a Path",
					getFieldEditorParent());
			addField(pe);
		}
	}

	class FieldEditorPageTwo extends FieldEditorPreferencePage {
		public FieldEditorPageTwo() {
			// Use the "grid" layout
			super(GRID);
			setTitle("Другое");
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
