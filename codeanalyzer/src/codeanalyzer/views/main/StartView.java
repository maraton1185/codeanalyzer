package codeanalyzer.views.main;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.TextStyle;
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

import codeanalyzer.books.interfaces.IBookManager;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.pico;
import codeanalyzer.core.db.DbManager;
import codeanalyzer.core.db.interfaces.IDbManager;
import codeanalyzer.core.db.model.BookInfo;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_BOOK_LIST_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class StartView {

	private Text DEFAULT_BOOK_DIRECTORY;
	private Text DEFAULT_DIRECTORY;

	FormToolkit toolkit;
	ScrolledForm form;
	Section bookSection;
	Composite bookSectionClient;
	HyperlinkAdapter bookSectionHandler;
	private IDbManager dbManager = pico.get(DbManager.class);
	private TreeViewer viewer;

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_LIST(
			@UIEventTopic(Const.EVENT_UPDATE_BOOK_LIST) EVENT_UPDATE_BOOK_LIST_DATA data) {

		// for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
		// .getChildren()) {
		// ctrl.dispose();
		// }
		//
		// Utils.fillBooks(bookSectionClient, toolkit, shell,
		// bookSectionHandler);
		// bookSection.setClient(bookSectionClient);
		//
		if (data.parent != null)
			viewer.refresh(data.parent);
		else
			viewer.refresh();

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
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 3;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

		mainLinks(hService, comService);

		bookslist();

		parameters(shell, hService, comService);

	}

	private void mainLinks(final EHandlerService hService,
			final ECommandService comService) {

		ImageHyperlink link;

		Section linksSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);
		linksSection.setText("");
		Composite linksSectionClient = toolkit.createComposite(linksSection);
		linksSectionClient.setLayout(new GridLayout());

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
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

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.WRAP);
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

		link = toolkit.createImageHyperlink(linksSectionClient, SWT.NULL);
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
		link.setImage(Utils.getImage("doc.png"));
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

	private void bookslist() {
		bookSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR
				| Section.TWISTIE | Section.EXPANDED);

		bookSection.setText("Список книг");
		bookSectionClient = toolkit.createComposite(bookSection);
		bookSectionClient.setLayout(new GridLayout());
		// bookSectionHandler = new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// bm.openBook((CurrentBookInfo) e.getHref(), shell);
		// super.linkActivated(e);
		// }
		//
		// };
		booksListCommands();

		bookSectionClient.setFont(new Font(Display.getCurrent(),
				PreferenceSupplier.getFontData(PreferenceSupplier.FONT)));
		viewer = new TreeViewer(bookSectionClient, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				AppManager.ctx.set(BookInfo.class,
						(BookInfo) selection.getFirstElement());
			}
		});

		toolkit.adapt(viewer.getTree());

		List<BookInfo> input = dbManager.getBooks();
		// root = input.size() == 0 ? null : input.get(0);
		viewer.setInput(input);
		// Utils.fillBooks(bookSectionClient, toolkit, shell,
		// bookSectionHandler);
		bookSection.setClient(bookSectionClient);

	}

	private void booksListCommands() {

		ImageHyperlink link;
		GridData gd;

		Composite comp = toolkit.createComposite(bookSectionClient);
		comp.setLayout(new RowLayout());
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = SWT.RIGHT;
		comp.setLayoutData(gd);

		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("add.png"));
		link.setToolTipText("Создать книгу");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// dbManager.addBookGroup();
			}

		});
		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("add_section.png"));
		link.setToolTipText("Добавить раздел");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// dbManager.addBookGroup();
			}

		});
		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("add_sub_section.png"));
		link.setToolTipText("Добавить подраздел");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// dbManager.addBookGroup();
			}

		});
		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("edit.png"));
		link.setToolTipText("Изменить заголовок");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// dbManager.addBookGroup();
			}

		});
		link = toolkit.createImageHyperlink(comp, SWT.WRAP);
		link.setImage(Utils.getImage("delete.png"));
		link.setToolTipText("Удалить раздел");
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// dbManager.addBookGroup();
			}

		});

	}

	class ViewContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((Collection<BookInfo>) inputElement).toArray();
			// return (BookSection[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// return book.sections().getChildren((SectionInfo) parentElement)
			// .toArray();
			return null;
		}

		@Override
		public Object getParent(Object element) {
			// return book.sections().getParent((SectionInfo) element);
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			// return book.sections().hasChildren((SectionInfo) element);
			return false;
		}
	}

	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			BookInfo item = (BookInfo) element;

			if (item.title != null) {
				if (item.isGroup) {

					FontData fontDatas[] = bookSectionClient.getFont()
							.getFontData();
					FontData data = fontDatas[0];
					int height = data.getHeight();
					height = (int) (height - 0.2 * height);
					final Font font = new Font(Display.getCurrent(),
							data.getName(), height, SWT.BOLD);

					text.append(item.title, new Styler() {
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.font = font;
						}
					});

				} else {
					text.append(item.title + " : ");
					text.append(item.path, new Styler() {
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.foreground = Display.getCurrent()
									.getSystemColor(SWT.COLOR_DARK_GRAY);
						}
					});
					// text.append(" " + section.id);
				}

			}

			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);

		}
	}
}
