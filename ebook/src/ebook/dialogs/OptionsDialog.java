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

import ebook.utils.PreferenceSupplier;

public class OptionsDialog {

	public void open() {

		// Create the preference manager
		PreferenceManager mgr = new PreferenceManager();
		PreferenceNode p;
		// Create the nodes
		p = new PreferenceNode("", new FieldEditorPageCommon());
		mgr.addToRoot(p);
		p = new PreferenceNode("", new FieldEditorPageOne());
		mgr.addToRoot(p);
		p = new PreferenceNode("", new FieldEditorPageTwo());
		mgr.addToRoot(p);

		// Create the preferences dialog
		PreferenceDialog dlg = new PreferenceDialog(null, mgr);

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
			setTitle("Общие настройки");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {

			// StringFieldEditor f1 = new StringFieldEditor(
			// PreferenceSupplier.NTPSERVER, "NTP-сервер:",
			// getFieldEditorParent());
			// addField(f1);

			DirectoryFieldEditor f2 = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_DIRECTORY,
					"Каталог конфигураций:", getFieldEditorParent());
			f2.setChangeButtonText("...");
			addField(f2);

			DirectoryFieldEditor f3 = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_BOOK_DIRECTORY, "Каталог книг:",
					getFieldEditorParent());
			f3.setChangeButtonText("...");
			addField(f3);

			BooleanFieldEditor f4 = new BooleanFieldEditor(
					PreferenceSupplier.INIT_EXECUTION,
					"Подключать конфигурации при запуске",
					getFieldEditorParent());
			addField(f4);

			BooleanFieldEditor f5 = new BooleanFieldEditor(
					PreferenceSupplier.SHOW_BOOK_PERSPECTIVE,
					"При запуске открывать список книг", getFieldEditorParent());
			addField(f5);

			// BooleanFieldEditor f6 = new BooleanFieldEditor(
			// PreferenceSupplier.SHOW_START_PAGE,
			// "Показывать страницу приветствия", getFieldEditorParent());
			// addField(f6);

			BooleanFieldEditor f7 = new BooleanFieldEditor(
					PreferenceSupplier.OPEN_BOOK_ON_STARTUP,
					"Открывать книгу при запуске", getFieldEditorParent());
			addField(f7);

			FileFieldEditor f8 = new FileFieldEditor(
					PreferenceSupplier.BOOK_ON_STARTUP, "Книга:",
					getFieldEditorParent());
			f8.setChangeButtonText("...");
			addField(f8);

			FontFieldEditor f9 = new FontFieldEditor(PreferenceSupplier.FONT,
					"Шрифт разделов книги:", getFieldEditorParent());
			f9.setChangeButtonText("...");
			addField(f9);

			BooleanFieldEditor f10 = new BooleanFieldEditor(
					PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW,
					"Не открывать страницу книги \"Как работать с книгой\"",
					getFieldEditorParent());
			addField(f10);

			BooleanFieldEditor f11 = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY, "Сворачивать в трей",
					getFieldEditorParent());
			addField(f11);
			BooleanFieldEditor f12 = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP,
					"Минимизировать при запуске", getFieldEditorParent());
			addField(f12);

			IntegerFieldEditor f13 = new IntegerFieldEditor(
					PreferenceSupplier.REMOTE_PORT, "Порт web-сервера",
					getFieldEditorParent());
			addField(f13);
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
