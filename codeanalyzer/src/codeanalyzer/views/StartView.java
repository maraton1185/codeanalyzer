package codeanalyzer.views;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
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

import codeanalyzer.utils.Const;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class StartView {

	FormToolkit toolkit;
	private Text DEFAULT_BOOK_DIRECTORY;
	private Text DEFAULT_DIRECTORY;

	@PostConstruct
	public void postConstruct(final Composite parent, EMenuService menuService,
			final ECommandService comService, final EHandlerService hService,
			final Shell shell) {

		ImageHyperlink link;
		Hyperlink hlink;
		Button button;
		Label label;

		toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

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

		Section section = toolkit.createSection(form.getBody(), 0);
		// Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
		// | Section.EXPANDED);
		// Section.DESCRIPTION | Section.EXPANDED);
		// twd_link = new TableWrapData(TableWrapData.LEFT, TableWrapData.TOP,
		// 1,
		// 1);
		// twd_link.grabHorizontal = true;
		// twd_link.valign = TableWrapData.BOTTOM;
		// section.setLayoutData(twd_link);

		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Список файлов");
		// section.setDescription("This is the description that goes below the title");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		// Button btn = toolkit.createButton(sectionClient, "Radio 1",
		// SWT.RADIO);
		// btn = toolkit.createButton(sectionClient, "Radio 2", SWT.RADIO);
		section.setClient(sectionClient);

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
		sectionLayout.numColumns = 2;
		prefSectionClient.setLayout(sectionLayout);

		GridData gd;

		gd = new GridData();
		gd.horizontalSpan = 2;
		label = toolkit.createLabel(prefSectionClient, "Каталог конфигураций:",
				SWT.LEFT);
		label.setLayoutData(gd);

		DEFAULT_DIRECTORY = toolkit.createText(prefSectionClient,
				PreferenceSupplier.get(PreferenceSupplier.DEFAULT_DIRECTORY),
				SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		DEFAULT_DIRECTORY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		button = new Button(prefSectionClient, SWT.FLAT);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.browseForPath(DEFAULT_DIRECTORY, shell);
				PreferenceSupplier.set(PreferenceSupplier.DEFAULT_DIRECTORY,
						DEFAULT_DIRECTORY.getText());
				PreferenceSupplier.save();
			}
		});
		button.setText("...");

		gd = new GridData();
		gd.horizontalSpan = 2;
		label = toolkit.createLabel(prefSectionClient, "Каталог книг:",
				SWT.LEFT);
		label.setLayoutData(gd);

		DEFAULT_BOOK_DIRECTORY = toolkit.createText(prefSectionClient,
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_BOOK_DIRECTORY),
				SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		DEFAULT_BOOK_DIRECTORY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		button = new Button(prefSectionClient, SWT.FLAT);
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
		button.setText("...");

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
		gd.horizontalSpan = 2;
		hlink.setLayoutData(gd);

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
		gd.horizontalSpan = 2;
		check.setLayoutData(gd);

		prefSection.setClient(prefSectionClient);
		// button.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// PreferenceSupplier.set(PreferenceSupplier.SHOW_START_PAGE,
		// !button.getSelection());
		// PreferenceSupplier.save();
		// }
		// });

	}

	@PreDestroy
	public void preDestroy() {
		toolkit.dispose();
	}

}
