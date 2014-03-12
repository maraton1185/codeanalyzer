package codeanalyzer.views;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.book.BookInfo;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class StartView {

	FormToolkit toolkit;
	ScrolledForm form;
	private Text DEFAULT_BOOK_DIRECTORY;
	private Text DEFAULT_DIRECTORY;
	private Section bookSection;
	Composite bookSectionClient;
	HyperlinkAdapter bookSectionHandler;
	IBookManager bm;
	private Shell shell;

	@Inject
	@Optional
	public void updateList(@UIEventTopic(Const.EVENT_UPDATE_BOOK_LIST) Object o,
			@Optional BookInfo book) {
		fillBooks();
	}

	@PostConstruct
	public void postConstruct(final Composite parent, EMenuService menuService,
			final ECommandService comService, final EHandlerService hService,
			final Shell shell, final IWorkbench wb, final IBookManager bm) {

		this.bm = bm;
		this.shell = shell;

		ImageHyperlink link;
		Hyperlink hlink;
		Button button;
		Label label;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setSize(448, 377);
		form.setLocation(0, 0);
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

		// IMAGEHYPERLINKS
		// *******************************************************

		link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		link.setImage(Utils.getImage("add_book.png"));
		link.setText("Создать книгу");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				hService.executeHandler(comService.createCommand(
						Strings.get("command.id.AddBook"),
						Collections.EMPTY_MAP));
				super.linkActivated(e);
			}

		});

		link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		link.setImage(Utils.getImage("open.png"));
		link.setText("Открыть книгу");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				hService.executeHandler(comService.createCommand(
						Strings.get("command.id.OpenBook"),
						Collections.EMPTY_MAP));
				super.linkActivated(e);
			}

		});

		link = toolkit.createImageHyperlink(form.getBody(), SWT.NULL);
		link.setImage(Utils.getImage("start.png"));
		link.setText("Открыть дерево объектов");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				hService.executeHandler(comService.createCommand(
						Strings.get("command.id.NewObjectTree"),
						Collections.EMPTY_MAP));
				super.linkActivated(e);
			}

		});

		link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		link.setImage(Utils.getImage("cf_add.png"));
		link.setText("Добавить конфигурацию");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				hService.executeHandler(comService.createCommand(
						Strings.get("command.id.Add"), Collections.EMPTY_MAP));
				super.linkActivated(e);
			}

		});

		link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		link.setImage(Utils.getImage("doc.png"));
		link.setText("Документация");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Program.launch(Const.URL_docLinkOpen);
				super.linkActivated(e);
			}

		});

		// СПИСОК КНИГ
		// **************************************************************

		bookSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR
				| Section.TWISTIE | Section.EXPANDED);

		bookSection.setText("Список книг");
		bookSectionClient = toolkit.createComposite(bookSection);
		bookSectionClient.setLayout(new GridLayout());
		bookSectionHandler = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				bm.openBook((BookInfo) e.getHref(), shell);
				super.linkActivated(e);
			}

		};

		bookSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (!bookSection.isExpanded())
					for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
							.getChildren()) {
						ctrl.dispose();
					}

				else {
					Utils.fillBooks(bm, bookSectionClient, toolkit, shell,
							bookSectionHandler);
					bookSection.setClient(bookSectionClient);
				}
				form.reflow(true);
			}
		});
		Utils.fillBooks(bm, bookSectionClient, toolkit, shell,
				bookSectionHandler);
		bookSection.setClient(bookSectionClient);

		// ПАРАМЕТРЫ
		// ****************************************************************

		Section prefSection = toolkit.createSection(form.getBody(),
		// Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
		// | Section.EXPANDED);
				Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
						| Section.EXPANDED);

		prefSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		prefSection.setText("Параметры");
		prefSection
				.setDescription("Перед началом работы заполните настройки по умолчанию.");
		Composite prefSectionClient = toolkit.createComposite(prefSection);
		GridLayout sectionLayout = new GridLayout();
		sectionLayout.numColumns = 3;
		prefSectionClient.setLayout(sectionLayout);

		GridData gd;

		gd = new GridData();
		gd.horizontalSpan = 3;
		label = toolkit.createLabel(prefSectionClient, "Каталог конфигураций:",
				SWT.LEFT);
		label.setLayoutData(gd);

		DEFAULT_DIRECTORY = toolkit.createText(prefSectionClient,
				PreferenceSupplier.get(PreferenceSupplier.DEFAULT_DIRECTORY),
				SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		DEFAULT_DIRECTORY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		button = toolkit.createButton(prefSectionClient, "...", SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.browseForPath(DEFAULT_DIRECTORY, shell);
				PreferenceSupplier.set(PreferenceSupplier.DEFAULT_DIRECTORY,
						DEFAULT_DIRECTORY.getText());
				PreferenceSupplier.save();
			}
		});

		button = toolkit.createButton(prefSectionClient, "", SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch(DEFAULT_DIRECTORY.getText());
			}
		});
		button.setImage(Utils.getImage("explore.png"));

		gd = new GridData();
		gd.horizontalSpan = 3;
		label = toolkit.createLabel(prefSectionClient, "Каталог книг:",
				SWT.LEFT);
		label.setLayoutData(gd);

		DEFAULT_BOOK_DIRECTORY = toolkit.createText(prefSectionClient,
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY),
				SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		DEFAULT_BOOK_DIRECTORY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		button = toolkit.createButton(prefSectionClient, "...", SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.browseForPath(DEFAULT_BOOK_DIRECTORY, shell);
				PreferenceSupplier.set(
						PreferenceSupplier.DEFAULT_BOOK_DIRECTORY,
						DEFAULT_BOOK_DIRECTORY.getText());
				PreferenceSupplier.save();
			}
		});

		button = toolkit.createButton(prefSectionClient, "", SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch(DEFAULT_BOOK_DIRECTORY.getText());
			}
		});
		button.setImage(Utils.getImage("explore.png"));

		hlink = toolkit.createHyperlink(prefSectionClient, "Другие...",
				SWT.WRAP);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				hService.executeHandler(comService.createCommand(
						Strings.get("command.id.Options"),
						Collections.EMPTY_MAP));
				super.linkActivated(e);
			}

		});
		gd = new GridData();
		gd.horizontalSpan = 3;
		hlink.setLayoutData(gd);

		final Button check1 = toolkit.createButton(prefSectionClient,
				"При запуске открывать список книг", SWT.CHECK);
		check1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferenceSupplier.set(
						PreferenceSupplier.SHOW_BOOK_PERSPECTIVE,
						check1.getSelection());
				PreferenceSupplier.save();
			}
		});
		gd = new GridData();
		gd.horizontalSpan = 3;
		check1.setLayoutData(gd);

		final Button check = toolkit.createButton(prefSectionClient,
				"Не показывать при запуске", SWT.CHECK);
		check.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferenceSupplier.set(PreferenceSupplier.SHOW_START_PAGE,
						!check.getSelection());
				PreferenceSupplier.save();
			}
		});
		gd = new GridData();
		gd.horizontalSpan = 3;
		check.setLayoutData(gd);

		prefSection.setClient(prefSectionClient);

	}

	protected void fillBooks() {

		for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
				.getChildren()) {
			ctrl.dispose();
		}

		Utils.fillBooks(bm, bookSectionClient, toolkit, shell,
				bookSectionHandler);
		bookSection.setClient(bookSectionClient);

		form.reflow(true);

	}

	@PreDestroy
	public void preDestroy() {
		toolkit.dispose();
	}

}
