package codeanalyzer.module.books.views.section;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.section.SectionImage;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.module.books.section.SectionInfoOptions;
import codeanalyzer.module.books.section.SectionSaveData;
import codeanalyzer.module.books.views.section.interfaces.IBlockTune;
import codeanalyzer.module.books.views.section.tools.TinyTextEditor;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;
import codeanalyzer.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class BlockView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;

	@Inject
	@Active
	BookConnection book;

	SectionInfo section;

	@Inject
	MDirtyable dirty;

	// ISectionComposite sectionComposite;

	private MWindow window;

	// Composite blockComposite;
	// Composite groupsComposite;
	TinyTextEditor tinymce;
	List<SectionImage> imageList;
	Scale scaledImageWidthSlider;
	Spinner columnCountSpinner;

	@Inject
	public BlockView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_SET_SECTIONVIEW_DIRTY(
			@UIEventTopic(Events.EVENT_SET_SECTIONVIEW_DIRTY) Object section) {
		if (section == this.section)
			dirty.setDirty(true);
	}

	@Persist
	public void save() {

		SectionSaveData data = new SectionSaveData();
		data.text = getText();
		data.options = getSectionOptions();
		book.srv().saveBlock(section, data);
		dirty.setDirty(false);
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_BLOCK_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs) {

		if (book != data.book)
			return;

		// if (!data.onlySectionView)
		// return;

		if (data.parent == null)
			return;

		if (!data.parent.equals(section))
			return;

		// part.setLabel(data.parent.title);
		// part.setLabel(section.title);
		renderGroups();
		// sectionComposite.renderGroups();
	}

	@Focus
	public void OnFocus() {
		window.getContext().set(Events.CONTEXT_ACTIVE_VIEW_SECTION, section);
	}

	@PostConstruct
	public void postConstruct(final Composite parent, SectionInfo section,
			@Active MWindow window) {

		this.section = section;
		this.window = window;

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setBackground(sashForm.getDisplay().getSystemColor(
				SWT.COLOR_GRAY));
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new FillLayout());
		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new FillLayout());

		String buf = book.srv().getText(section);
		tinymce = new TinyTextEditor(leftComposite, section);
		tinymce.setText(buf);
		tinymce.setLayoutData(new GridData(GridData.FILL_BOTH));

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(rightComposite);

		parent.setBackground(form.getBackground());
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		form.setLayoutData(gd);

		// form.setText("text");
		body = form.getBody();
		toolkit.paintBordersFor(body);

		ColumnLayout layout1 = new ColumnLayout();
		layout1.maxNumColumns = 1;
		body.setLayout(layout1);
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		renderGroups();

	}

	// ********************************************************************

	public void renderGroups() {

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}

		addImageSections();

		// ****************************************************************

		IBlockTune defaultTune = new IBlockTune() {
			@Override
			public void tune(FormToolkit toolkit, Section section,
					Composite sectionClient) {
				toolkit.createLabel(sectionClient, "test");
			}
		};
		addSection(Strings.get("s.sectionBlockComposite.code"), defaultTune);

		// ****************************************************************

		IBlockTune bookmarkTune = new IBlockTune() {

			@Override
			public void tune(FormToolkit toolkit, Section section,
					Composite sectionClient) {

				toolkit.createLabel(sectionClient, "bookmark");

			}
		};

		addSection(Strings.get("s.sectionBlockComposite.bookmark"),
				bookmarkTune);

		// ****************************************************************

		addOptionsSection();

		// bo.layout(true);
		form.reflow(true);
	}

	public String getText() {
		return tinymce.getText();
	}

	public SectionInfoOptions getSectionOptions() {

		SectionInfoOptions result = new SectionInfoOptions();
		result.scaledImageWidth = scaledImageWidthSlider.getSelection();
		result.columnCount = columnCountSpinner.getSelection();
		return result;
	}

	private void addOptionsSection() {
		IBlockTune optionsTune = new IBlockTune() {

			@Override
			public void tune(FormToolkit toolkit, Section group,
					Composite sectionClient) {

				group.setExpanded(true);

				toolkit.createLabel(sectionClient, Strings
						.get("s.sectionBlockComposite.scaledImageWidthSlider"));

				scaledImageWidthSlider = new Scale(sectionClient,
						SWT.HORIZONTAL);
				toolkit.adapt(scaledImageWidthSlider, true, true);
				scaledImageWidthSlider
						.setMaximum(SectionInfoOptions.scaledImageMaxWidth);
				scaledImageWidthSlider
						.setMinimum(SectionInfoOptions.scaledImageMinWidth);
				// scaledImageWidthSlider.setIncrement(20);
				scaledImageWidthSlider.setPageIncrement(50);
				scaledImageWidthSlider
						.setSelection(section.options.scaledImageWidth);
				scaledImageWidthSlider
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								super.widgetSelected(e);
								dirty.setDirty(true);
								// System.out.println(slider.getSelection());
							}

						});

				scaledImageWidthSlider.setLayoutData(new GridData(
						GridData.FILL_HORIZONTAL));

				columnCountSpinner = new Spinner(sectionClient, SWT.BORDER);
				toolkit.adapt(columnCountSpinner, true, true);
				columnCountSpinner.setMinimum(1);
				columnCountSpinner.setMaximum(5);
				columnCountSpinner.setSelection(section.options.columnCount);
				columnCountSpinner.setIncrement(1);
				columnCountSpinner.setPageIncrement(1);
				columnCountSpinner.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						super.widgetSelected(e);
						dirty.setDirty(true);
					}

				});

			}
		};

		addSection(Strings.get("s.sectionBlockComposite.options"), optionsTune);

	}

	private void addImageSections() {
		final Device display = body.getDisplay();

		imageList = book.srv().getImages(display, section);

		for (final SectionImage sectionImage : imageList) {

			IBlockTune pictureTune = new IBlockTune() {
				@Override
				public void tune(FormToolkit toolkit, Section group,
						Composite sectionClient) {

					group.setText(sectionImage.title);
					group.setExpanded(sectionImage.expanded);

					ImageHyperlink hlink = toolkit.createImageHyperlink(
							sectionClient, SWT.WRAP);

					hlink.setImage(sectionImage.getScaled(display,
							section.options));
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {

							try {
								File temp = File.createTempFile("temp", ".png");
								Image image = sectionImage.image;

								ImageLoader saver = new ImageLoader();
								saver.data = new ImageData[] { image
										.getImageData() };
								saver.save(temp.getAbsolutePath(),
										SWT.IMAGE_PNG);

								Desktop.getDesktop().open(temp);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});

					GridData gd = new GridData();
					gd.grabExcessVerticalSpace = true;
					gd.grabExcessHorizontalSpace = true;
					gd.horizontalAlignment = SWT.CENTER;
					hlink.setLayoutData(gd);

					Composite panel = toolkit.createComposite(sectionClient);
					panel.setLayout(new RowLayout());
					gd = new GridData();
					gd.horizontalAlignment = SWT.RIGHT;
					gd.grabExcessHorizontalSpace = true;
					panel.setLayoutData(gd);

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("edit.png"));
					hlink.setToolTipText("Изменить");

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("delete.png"));
					hlink.setToolTipText("Удалить");

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("up.png"));
					hlink.setToolTipText("Переместить вверх");

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("down.png"));
					hlink.setToolTipText("Переместить вниз");

				}
			};
			addSection(Strings.get("s.sectionBlockComposite.picture"),
					pictureTune);
		}
	}

	private void addSection(String title, IBlockTune opt) {
		Section section = toolkit.createSection(body, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);

		// GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.grabExcessHorizontalSpace = true;
		// section.setLayoutData(gd);

		section.setLayout(new FillLayout());

		section.setText(title);
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout(1, false));

		// gd = new GridData(GridData.FILL_BOTH);
		// // gd.horizontalAlignment = SWT.RIGHT;
		// gd.grabExcessHorizontalSpace = true;
		// sectionClient.setLayoutData(gd);

		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});

		// GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.horizontalSpan = 2;
		// section.setLayoutData(gd);

		opt.tune(toolkit, section, sectionClient);
		section.setClient(sectionClient);
	}

}
