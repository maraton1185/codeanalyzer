package codeanalyzer.module.books.views.section.tools;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
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

import codeanalyzer.module.books.WindowBookInfo;
import codeanalyzer.module.books.section.SectionImage;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.module.books.section.SectionOptions;
import codeanalyzer.module.books.views.section.interfaces.IBlockTune;
import codeanalyzer.module.books.views.section.interfaces.ISectionComposite;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionComposite implements ISectionComposite {

	Composite blockComposite;
	Composite groupsComposite;
	TinyTextEditor tinymce;
	List<SectionImage> imageList;

	@Override
	public String getText() {
		return tinymce.getText();
	}

	FormToolkit toolkit;
	ScrolledForm form;
	WindowBookInfo book;
	SectionInfo section;
	MDirtyable dirty;
	boolean blockView;

	Scale scaledImageWidthSlider;
	Spinner columnCountSpinner;

	@Override
	public void initSectionView(FormToolkit toolkit, ScrolledForm form,
			WindowBookInfo book, SectionInfo section) {

		this.toolkit = toolkit;
		// this.body = body;
		this.form = form;
		this.book = book;
		this.section = section;

		blockView = false;
		// blockComposite = form.getBody();
		blockComposite = toolkit.createComposite(form.getBody(), SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		blockComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		blockComposite.setLayout(layout);

	}

	@Override
	public void initBlockView(FormToolkit toolkit, ScrolledForm form,
			WindowBookInfo book, SectionInfo section, MDirtyable dirty) {

		this.toolkit = toolkit;
		this.form = form;
		this.book = book;
		this.section = section;
		this.dirty = dirty;
		blockView = true;

		blockComposite = toolkit.createComposite(form.getBody());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		blockComposite.setLayout(layout);
	}

	@Override
	public void render() {

		if (blockView)
			addTinyText();
		else
			addBrowserText();

		renderGroups();
	}

	private void addTinyText() {

		String buf = book.sections().getText(section);

		tinymce = new TinyTextEditor(blockComposite, section);
		toolkit.adapt(tinymce, true, true);
		tinymce.setText(buf);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 50;
		// gd.horizontalSpan = numColumns - 1;
		tinymce.setLayoutData(gd);

		groupsComposite = toolkit.createComposite(blockComposite);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		// gd.widthHint = section.options.getCompositeWidthHint();
		groupsComposite.setLayoutData(gd);

	}

	private void addBrowserText() {

		// String buf = book.sections().getText(section);
		//
		// BrowserComposite browserComposite = new BrowserComposite(
		// blockComposite, buf, form);
		// toolkit.adapt(browserComposite, true, true);
		//
		// GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.grabExcessHorizontalSpace = true;
		// gd.grabExcessVerticalSpace = false;
		// gd.widthHint = 50;
		// // gd.horizontalSpan = numColumns - 1;
		// browserComposite.setLayoutData(gd);
		//
		// groupsComposite = toolkit.createComposite(blockComposite,
		// SWT.BORDER);
		//
		// gd = new GridData();
		// gd.grabExcessHorizontalSpace = false;
		// gd.grabExcessVerticalSpace = false;
		// gd.verticalAlignment = SWT.TOP;
		// // gd.widthHint = section.options.getCompositeWidthHint();
		// // gd.widthHint = IBlockComposite.groupWidth;
		// groupsComposite.setLayoutData(gd);

	}

	@Override
	public void renderGroups() {

		for (Control ctrl : groupsComposite.getChildren()) {
			ctrl.dispose();
		}

		ColumnLayout layout = new ColumnLayout();
		if (blockView)
			layout.maxNumColumns = 1;
		else
			layout.maxNumColumns = section.options.columnCount;
		// layout.numColumns = 1;
		groupsComposite.setLayout(layout);

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

		if (blockView)
			addOptionsSection();

		blockComposite.layout(true);
		form.reflow(true);
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
						.setMaximum(SectionOptions.scaledImageMaxWidth);
				scaledImageWidthSlider
						.setMinimum(SectionOptions.scaledImageMinWidth);
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
		final Device display = groupsComposite.getDisplay();

		imageList = book.sections().getImages(display, section);

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

					if (blockView) {

						Composite panel = toolkit
								.createComposite(sectionClient);
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
				}
			};
			addSection(Strings.get("s.sectionBlockComposite.picture"),
					pictureTune);
		}
	}

	private void addSection(String title, IBlockTune opt) {
		Section section = toolkit.createSection(groupsComposite,
				Section.SHORT_TITLE_BAR | Section.TWISTIE);

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

	@Override
	public SectionOptions getSectionOptions() {

		SectionOptions result = new SectionOptions();
		result.scaledImageWidth = scaledImageWidthSlider.getSelection();
		result.columnCount = columnCountSpinner.getSelection();
		return result;
	}
}
