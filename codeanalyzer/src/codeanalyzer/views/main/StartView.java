package codeanalyzer.views.main;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.book.BookService;
import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.pico;
import codeanalyzer.core.components.TreeViewComponent;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_TREE_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class StartView {

	private Text DEFAULT_BOOK_DIRECTORY;
	private Text DEFAULT_DIRECTORY;

	FormToolkit toolkit;
	ScrolledForm form;
	// Section bookSection;
	Composite bookSectionClient;
	HyperlinkAdapter bookSectionHandler;
	IBookManager bm = pico.get(IBookManager.class);
	private TreeViewer viewer;

	// @Inject
	// @Optional
	// public void EVENT_EDIT_TITLE_BOOK_LIST(
	// @UIEventTopic(Const.EVENT_EDIT_TITLE_BOOK_LIST)
	// EVENT_UPDATE_BOOK_LIST_DATA data) {
	//
	// if (data.selected == null)
	// return;
	//
	// viewer.editElement(data.selected, 0);
	//
	// }

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_LIST(
			@UIEventTopic(Const.EVENT_UPDATE_BOOK_LIST) EVENT_UPDATE_TREE_DATA data) {

		if (data.parent == null)
			return;

		viewer.refresh(data.parent, true);

		if (data.selected != null)
			viewer.setSelection(new StructuredSelection(data.selected), true);

		form.reflow(true);
	}

	@PostConstruct
	public void postConstruct(final Composite parent, EMenuService menuService,
			final ECommandService comService, final EHandlerService hService,
			final Shell shell, final IWorkbench wb, final IBookManager bm) {

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setSize(448, 377);
		form.setLocation(0, 0);
		// GridLayout layout = new GridLayout();
		// layout.numColumns = 2;
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 3;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

		mainLinks(hService, comService);

		bookslist(shell, hService, comService);

		parameters(shell, hService, comService);

	}

	private void mainLinks(final EHandlerService hService,
			final ECommandService comService) {

		ImageHyperlink link;

		Section linksSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);

		// GridData gd = new GridData();
		// gd.verticalSpan = 2;
		// linksSection.setLayoutData(gd);
		linksSection.setText("");
		Composite linksSectionClient = toolkit.createComposite(linksSection);
		linksSectionClient.setLayout(new GridLayout());

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
		link.setImage(Utils.getImage("add_book.png"));
		link.setText("Создать книгу");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hService, comService,
						Strings.get("command.id.AddBook"));
				super.linkActivated(e);
			}

		});

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
		link.setImage(Utils.getImage("open_book.png"));
		link.setText("Открыть книгу");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hService, comService,
						Strings.get("command.id.OpenBook"));

				super.linkActivated(e);
			}

		});

		// link = toolkit.createImageHyperlink(linksSectionClient, SWT.NULL);
		// link.setImage(Utils.getImage("start.png"));
		// link.setText("Открыть дерево объектов");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// hService.executeHandler(comService.createCommand(
		// Strings.get("command.id.NewObjectTree"),
		// Collections.EMPTY_MAP));
		// super.linkActivated(e);
		// }
		//
		// });

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
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

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
		link.setImage(Utils.getImage("help.png"));
		link.setText("Документация");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Program.launch(Const.URL_docLinkOpen);
				super.linkActivated(e);
			}

		});

		linksSection.setClient(linksSectionClient);

	}

	private void parameters(final Shell shell, final EHandlerService hService,
			final ECommandService comService) {

		Hyperlink hlink;
		Button button;
		Label label;
		GridData gd;

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

	// @PreDestroy
	// public void preDestroy() {
	// toolkit.dispose();
	// }

	private void bookslist(final Shell shell, EHandlerService hService,
			ECommandService comService) {
		Section bookSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED
						| SWT.BORDER);

		bookSection.setText("Список книг");
		bookSection.setLayout(new GridLayout());
		bookSectionClient = toolkit.createComposite(bookSection);
		bookSectionClient.setLayout(new GridLayout());

		booksListCommands(hService, comService);

		bookSectionClient.setFont(new Font(Display.getCurrent(),
				PreferenceSupplier.getFontData(PreferenceSupplier.FONT)));

		TreeViewComponent booksList = new TreeViewComponent(bookSectionClient,
				new BookService(), 2);
		viewer = booksList.getViewer();
		toolkit.adapt(viewer.getTree());
		viewer.getTree().addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				form.reflow(true);
			}

		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				BookInfo selected = (BookInfo) selection.getFirstElement();

				bm.openBook(selected.getPath(), shell);
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();

				// BookInfoSelection sel = new BookInfoSelection();
				// @SuppressWarnings("unchecked")
				// Iterator<BookInfo> iterator = selection.iterator();
				// while (iterator.hasNext())
				// sel.add(iterator.next());
				//
				// AppManager.ctx.set(BookInfoSelection.class, sel);

				AppManager.ctx.set(BookInfo.class,
						(BookInfo) selection.getFirstElement());
			}
		});

		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.horizontalAlignment = SWT.RIGHT;
		viewer.getTree().setLayoutData(gd);
		bookSection.setClient(bookSectionClient);

	}

	private void booksListCommands(final EHandlerService hService,
			final ECommandService comService) {

		ImageHyperlink link;
		GridData gd;
		Hyperlink _link;

		Composite comp = toolkit.createComposite(bookSectionClient);
		comp.setLayout(new RowLayout());
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = SWT.RIGHT;
		comp.setLayoutData(gd);

		_link = toolkit.createHyperlink(comp, "Настроить", SWT.WRAP);
		_link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hService, comService,
						Strings.get("command.id.OpenBookList"));
			}

		});

		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("update.png"));
		link.setToolTipText("Обновить список");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				Utils.executeHandler(hService, comService,
						Strings.get("command.id.BookListUpdate"));
				// super.linkActivated(e);
			}

		});
		// link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// link.setImage(Utils.getImage("add.png"));
		// link.setToolTipText("Создать книгу");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.AddBook"));
		// // super.linkActivated(e);
		// }
		//
		// });
		// link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// link.setImage(Utils.getImage("add_section.png"));
		// link.setToolTipText("Добавить раздел");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.AddBooksGroup"));
		// }
		//
		// });
		// link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// link.setImage(Utils.getImage("add_sub_section.png"));
		// link.setToolTipText("Добавить подраздел");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.AddBooksSubGroup"));
		// }
		//
		// });
		// link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// link.setImage(Utils.getImage("edit.png"));
		// link.setToolTipText("Изменить заголовок");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.BookEditTitle"));
		// }
		//
		// });
		// link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// link.setImage(Utils.getImage("delete.png"));
		// link.setToolTipText("Удалить раздел");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hService, comService,
		// Strings.get("command.id.BookDelete"));
		// }
		//
		// });

	}

}
