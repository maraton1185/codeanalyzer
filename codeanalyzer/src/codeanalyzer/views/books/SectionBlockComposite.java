package codeanalyzer.views.books;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.book.BookSectionImage;
import codeanalyzer.tools.BrowserComposite;
import codeanalyzer.tools.TinyTextEditor;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionBlockComposite implements ISectionBlockComposite {

	FormText ft;
	FormToolkit toolkit;
	Composite body;
	Boolean blockView;
	TinyTextEditor tinymce;
	List<BookSectionImage> imageList;
	Composite comp;

	@Override
	public TinyTextEditor getTinymce() {
		return tinymce;
	}

	private ScrolledForm form;
	private BookInfo book;
	private BookSection section;

	@Override
	public void render(BookSection section) {

		this.section = section;

		if (blockView)
			addTinyText();
		else
			addBrowserText();
	}

	private void addTinyText() {

		String buf = book.sections().getText(section);

		tinymce = new TinyTextEditor(body, section);
		toolkit.adapt(tinymce, true, true);
		tinymce.setText(buf);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 50;
		gd.horizontalSpan = numColumns - 1;
		tinymce.setLayoutData(gd);

		comp = toolkit.createComposite(body);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		gd.widthHint = 200;
		comp.setLayoutData(gd);

		renderGroups(section);
	}

	private void addBrowserText() {

		String buf = book.sections().getText(section);

		BrowserComposite browserComposite = new BrowserComposite(body, buf,
				form);
		toolkit.adapt(browserComposite, true, true);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = 50;
		gd.horizontalSpan = numColumns - 1;
		browserComposite.setLayoutData(gd);

		comp = toolkit.createComposite(body);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = false;
		gd.verticalAlignment = SWT.TOP;
		gd.widthHint = 200;
		comp.setLayoutData(gd);

		renderGroups(section);
	}

	@Override
	public void renderGroups(BookSection section) {

		this.section = section;

		for (Control ctrl : comp.getChildren()) {
			ctrl.dispose();
		}

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		comp.setLayout(layout);

		addImageSections(comp);

		// ****************************************************************

		ISectionBlockOptions defaultTune = new ISectionBlockOptions() {
			@Override
			public void tune(FormToolkit toolkit, Section section,
					Composite sectionClient) {
				toolkit.createLabel(sectionClient, "test");
			}
		};
		addSection(comp, Strings.get("s.sectionBlockComposite.code"),
				defaultTune);

		// ****************************************************************

		ISectionBlockOptions bookmarkTune = new ISectionBlockOptions() {

			@Override
			public void tune(FormToolkit toolkit, Section section,
					Composite sectionClient) {

				toolkit.createLabel(sectionClient, "bookmark");

			}
		};

		addSection(comp, Strings.get("s.sectionBlockComposite.bookmark"),
				bookmarkTune);

	}

	private void addImageSections(Composite comp) {
		imageList = book.sections().getImages(comp.getDisplay(), section);

		for (final BookSectionImage sectionImage : imageList) {

			ISectionBlockOptions pictureTune = new ISectionBlockOptions() {
				@Override
				public void tune(FormToolkit toolkit, Section group,
						Composite sectionClient) {

					group.setText(sectionImage.title);
					group.setExpanded(sectionImage.expanded);

					ImageHyperlink hlink = toolkit.createImageHyperlink(
							sectionClient, SWT.WRAP);
					hlink.setImage(sectionImage.image);
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

					if (section.block) {
						GridData gd = new GridData();
						gd.grabExcessVerticalSpace = true;
						gd.grabExcessHorizontalSpace = true;
						gd.horizontalAlignment = SWT.CENTER;
						hlink.setLayoutData(gd);

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
			addSection(comp, Strings.get("s.sectionBlockComposite.picture"),
					pictureTune);
		}
	}

	private void addSection(Composite comp, String title,
			ISectionBlockOptions opt) {
		Section section = toolkit.createSection(comp, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		section.setLayoutData(gd);

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
	public void init(FormToolkit toolkit, Composite body, ScrolledForm form,
			BookInfo book) {

		this.toolkit = toolkit;
		this.body = body;
		this.form = form;
		this.book = book;

		blockView = false;

	}

	@Override
	public void setBlockView(Boolean blockView) {
		this.blockView = blockView;
	}

}
