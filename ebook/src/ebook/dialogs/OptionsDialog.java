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
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ebook.module.book.views.tools._BrowserComposite;
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
			newShell.setText(Strings.title("appTitle"));
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
		p = new PreferenceNode("", new FieldEditorPageOthers());
		mgr.addToRoot(p);

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
			super(GRID);
			setTitle("Общие");
		}

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
					"Сворачивать в трей", comp);
			addField(f);

			// f = new BooleanFieldEditor(
			// PreferenceSupplier.SHOW_ABOUT_ON_STARTUP,
			// "Показывать \"О программе\" при запуске", comp);
			// addField(f);

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new BooleanFieldEditor(
					PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP,
					"Минимизировать при запуске", comp);
			addField(f);

			f = new StringFieldEditor(PreferenceSupplier.APP_BRAND,
					"Заголовок программы", comp);
			addField(f);

			f = new BooleanFieldEditor(
					PreferenceSupplier.CHECK_UPDATE_ON_STARTUP,
					"Проверять обновления при запуске", comp);
			addField(f);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("Настройки web-сервера:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new IntegerFieldEditor(PreferenceSupplier.IMAGE_WIDTH,
					"Ширина картинок (в пикселах)", comp);
			addField(f);

			f = new IntegerFieldEditor(PreferenceSupplier.SESSION_TIMEOUT,
					"Таймаут сессии (в минутах)", comp);
			addField(f);

			f = new BooleanFieldEditor(PreferenceSupplier.START_JETTY,
					"Запускать web-сервер", comp);
			addField(f);

			f = new IntegerFieldEditor(PreferenceSupplier.REMOTE_PORT,
					"Порт web-сервера", comp);
			addField(f);

			f = new BooleanFieldEditor(PreferenceSupplier.EXTERNAL_JETTY_BASE,
					"Внешний каталог web-сервера", comp);
			addField(f);

			f = new DirectoryFieldEditor(PreferenceSupplier.JETTY_BASE,
					"Каталог web-сервера", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("Каталоги по умолчанию:");

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_CONF_DIRECTORY,
					"Для конфигураций:", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_BOOK_DIRECTORY, "Для книг:",
					comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new DirectoryFieldEditor(
					PreferenceSupplier.DEFAULT_IMAGE_DIRECTORY,
					"Выбор картинок:", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

			group = new Group(getFieldEditorParent(), SWT.NULL);
			group.getParent().setLayout(new GridLayout(2, false));
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					2, 1));
			group.setText("Настройки книг:");

			// comp = new Composite(group, SWT.NULL);
			// comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new BooleanFieldEditor(PreferenceSupplier.OPEN_BOOK_ON_STARTUP,
					"Открывать книгу при запуске", comp);
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new MyFileFieldEditor(PreferenceSupplier.BOOK_ON_STARTUP,
					"Книга:", comp);
			((FileFieldEditor) f).setChangeButtonText("...");
			addField(f);

			comp = new Composite(group, SWT.NULL);
			comp.setLayout(new GridLayout(2, false));
			comp.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			f = new FontFieldEditor(PreferenceSupplier.FONT, "Шрифт:", comp);
			((FontFieldEditor) f).setChangeButtonText("...");
			addField(f);

		}
	}

	class FieldEditorPageBrowser extends FieldEditorPreferencePage {
		public FieldEditorPageBrowser() {
			// Use the "flat" layout
			super(FLAT);
			setTitle("Браузер");
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
			new _BrowserComposite(body, url.toString());

		}

	}

	class FieldEditorPageOthers extends FieldEditorPreferencePage {
		public FieldEditorPageOthers() {
			super(GRID);
			setTitle("Другие");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {

			FieldEditor f;
			Composite comp;
			// Group group;
			comp = getFieldEditorParent();

			f = new StringFieldEditor(PreferenceSupplier.APP_HOST,
					"Адрес сайта активации", comp);
			addField(f);

			f = new StringFieldEditor(PreferenceSupplier.UPDATE_SITE,
					"Адрес сайта обновления", comp);
			addField(f);

			f = new IntegerFieldEditor(PreferenceSupplier.BOOKMARK_LENGTH,
					"Длина текста закладки", comp);
			addField(f);

			f = new StringFieldEditor(PreferenceSupplier.IMAGE_TITLE,
					"Префикс имени картинки", comp);
			addField(f);

			f = new MultiLineTextFieldEditor(
					PreferenceSupplier.INIT_SECTION_HTML,
					"Начальный текст блока", comp);
			addField(f);

			f = new BooleanFieldEditor(
					PreferenceSupplier.LOAD_EDITOR_TEMPLATES_ON_GET,
					"Загружать шаблоны текста при каждом запросе к редактору",
					comp);
			addField(f);

			f = new DirectoryFieldEditor(
					PreferenceSupplier.EDITOR_TEMPLATES_FOLDER,
					"Каталог шаблонов текста:", comp);
			((DirectoryFieldEditor) f).setChangeButtonText("...");
			addField(f);

		}
	}

	class FieldEditorPageExample extends FieldEditorPreferencePage {
		private ScrolledComposite fScrolledComposite;
		private int fCompositeWidth = 100;
		private int fCompositeHeight = 100;
		private FormToolkit toolkit;
		private Form form;

		public FieldEditorPageExample() {
			super(GRID);
			setTitle("Начальный текст блока книги");
		}

		/**
		 * Creates the field editors
		 */
		@Override
		protected void createFieldEditors() {

			// Create the ScrolledComposite to scroll horizontally and
			// vertically
			fScrolledComposite = new ScrolledComposite(getFieldEditorParent(),
					SWT.H_SCROLL | SWT.V_SCROLL);
			// Displays the scrollbars when the window gets smaller
			fScrolledComposite.setAlwaysShowScrollBars(false);
			// Sets the minimum size for the composite to work for scrolling
			fScrolledComposite.setMinSize(fCompositeWidth, fCompositeHeight);
			fScrolledComposite.setExpandHorizontal(true);
			fScrolledComposite.setExpandVertical(true);

			Composite composite = new Composite(fScrolledComposite, SWT.NONE);
			composite.setLayout(new GridLayout());
			fScrolledComposite.setContent(composite);
			// Sets up the toolkit.
			Display display = composite.getDisplay();
			toolkit = new FormToolkit(display);

			// Creates a form instance.
			form = toolkit.createForm(composite);
			form.getBody().setLayout(new GridLayout());
			form.setBackground(display
					.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			// form.setText("Model: " + SignalGeneratorDevice.MODEL_ID);

			// Add the three main nodes to the preference page
			addNode1();

		}

		/**
		 * Adds the first node to the preference page
		 */

		private void addNode1() {
			ExpandableComposite expandableComposite = createExpandableComposite(
					"Signal Generator Device Host/Port:", true);
			Composite comp = createChildComposite(expandableComposite);

			FieldEditor f = new StringFieldEditor(
					PreferenceSupplier.IMAGE_TITLE, "Префикс имени картинки",
					comp);
			addField(f);
		}

		/**
		 * Creates an ExpandableComposite that will be added to the preference
		 * page
		 * 
		 * @param label
		 * @param expanded
		 * @return
		 */
		private ExpandableComposite createExpandableComposite(String label,
				boolean expanded) {
			ExpandableComposite expandableComposite = null;
			if (expanded) {
				expandableComposite = toolkit.createExpandableComposite(
						form.getBody(), ExpandableComposite.TWISTIE
								| ExpandableComposite.CLIENT_INDENT
								| ExpandableComposite.EXPANDED);
			} else {
				expandableComposite = toolkit.createExpandableComposite(
						form.getBody(), ExpandableComposite.TWISTIE
								| ExpandableComposite.CLIENT_INDENT);
			}

			expandableComposite.setText(label);
			expandableComposite.setBackground(form.getBackground());
			expandableComposite.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(ExpansionEvent e) {
					form.pack();
				}
			});

			GridData gd = new GridData();
			expandableComposite.setLayoutData(gd);

			return expandableComposite;
		}

		/**
		 * Creates a child composite for an ExpandableComposite
		 * 
		 * @param expandableComposite
		 * @return
		 */
		private Composite createChildComposite(
				ExpandableComposite expandableComposite) {
			Composite childComposite = new Composite(expandableComposite,
					SWT.None);

			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			// gd.horizontalAlignment = GridData.END;
			childComposite.setLayoutData(gd);

			expandableComposite.setClient(childComposite);

			return childComposite;
		}
	}

}
