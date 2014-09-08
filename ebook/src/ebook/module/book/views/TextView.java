package ebook.module.book.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.book.tree.SectionSaveData;
import ebook.module.book.views.interfaces.IBlockTune;
import ebook.module.book.views.tools.TinyTextEditor;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class TextView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;

	@Inject
	@Active
	BookConnection book;

	@Inject
	private EHandlerService hService;

	@Inject
	private ECommandService comService;

	@Inject
	Shell shell;

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

	// Spinner columnCountSpinner;

	@Inject
	public TextView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_SET_SECTIONVIEW_DIRTY(
			@UIEventTopic(Events.EVENT_SET_SECTIONVIEW_DIRTY) Object section) {
		if (section == this.section) {
			save_index--;
			if (save_index <= 0)
				dirty.setDirty(true);
		}
	}

	int save_index = 0;

	@Persist
	public void save() {

		SectionSaveData data = new SectionSaveData();
		data.text = getText();
		data.options = getSectionOptions();
		book.srv().saveBlock(section, data);
		dirty.setDirty(false);
		save_index = 2;
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (book != data.con)
			return;

		if (!data.parent.equals(section))
			return;

		tinymce.updateUrl();

	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_BLOCK_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_BLOCK_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs) {

		if (book != data.con)
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

		dirty.setDirty(false);

		this.section = section;
		this.window = window;

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setBackground(sashForm.getDisplay().getSystemColor(
				SWT.COLOR_GRAY));
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new FillLayout());
		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new FillLayout());

		String buf = book.srv().getText(section.getId());
		tinymce = new TinyTextEditor(rightComposite, section);
		tinymce.setText(buf);
		tinymce.setLayoutData(new GridData(GridData.FILL_BOTH));

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(leftComposite);

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

		OnFocus();
	}

	// ********************************************************************

	public void renderGroups() {

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}

		addOptionsSection();

		addImageSections();

		// ****************************************************************

		// IBlockTune defaultTune = new IBlockTune() {
		// @Override
		// public void tune(FormToolkit toolkit, Section section,
		// Composite sectionClient) {
		// toolkit.createLabel(sectionClient, "test");
		// }
		// };
		// addSection(Strings.get("s.sectionBlockComposite.code"), defaultTune);

		// ****************************************************************

		// IBlockTune bookmarkTune = new IBlockTune() {
		//
		// @Override
		// public void tune(FormToolkit toolkit, Section section,
		// Composite sectionClient) {
		//
		// toolkit.createLabel(sectionClient, "bookmark");
		//
		// }
		// };
		//
		// addSection(Strings.get("s.sectionBlockComposite.bookmark"),
		// bookmarkTune);

		// ****************************************************************

		// bo.layout(true);
		form.reflow(true);
	}

	public String getText() {
		return tinymce.getText();
	}

	public SectionInfoOptions getSectionOptions() {

		SectionInfoOptions result = new SectionInfoOptions();
		result.setBigImageCSS(scaledImageWidthSlider.getSelection());
		// result.scaledImageWidth = scaledImageWidthSlider.getSelection();
		// result.columnCount = columnCountSpinner.getSelection();
		return result;
	}

	private void addOptionsSection() {
		IBlockTune optionsTune = new IBlockTune() {

			@Override
			public void tune(FormToolkit toolkit, Section group,
					Composite sectionClient) {

				group.setExpanded(true);

				toolkit.createLabel(sectionClient,
						Strings.value("scaledImageWidthSlider"));

				scaledImageWidthSlider = new Scale(sectionClient,
						SWT.HORIZONTAL);
				toolkit.adapt(scaledImageWidthSlider, true, true);
				scaledImageWidthSlider
						.setMaximum(SectionInfoOptions.gridScaleMax);
				scaledImageWidthSlider
						.setMinimum(SectionInfoOptions.gridScaleMin);
				// scaledImageWidthSlider.setIncrement(20);
				scaledImageWidthSlider
						.setPageIncrement(SectionInfoOptions.scaleIncrement);
				scaledImageWidthSlider.setSelection(section.getOptions()
						.getBigImageCSS());
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

				Composite panel = toolkit.createComposite(sectionClient);
				panel.setLayout(new RowLayout());
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.RIGHT;
				gd.grabExcessHorizontalSpace = true;
				panel.setLayoutData(gd);

				ImageHyperlink hlink;

				hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
				hlink.setImage(Utils.getImage("add.png"));
				hlink.setText("Добавить картинку");
				hlink.setUnderlined(false);
				hlink.addHyperlinkListener(new HyperlinkAdapter() {
					@Override
					public void linkActivated(HyperlinkEvent e) {
						Utils.executeHandler(hService, comService,
								Strings.model("ebook.command.2"));
					}
				});

				hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
				hlink.setImage(Utils.getImage("save.png"));
				hlink.setText("Сохранить");
				hlink.setUnderlined(false);
				hlink.addHyperlinkListener(new HyperlinkAdapter() {
					@Override
					public void linkActivated(HyperlinkEvent e) {
						Utils.executeHandler(hService, comService,
								Strings.model("ebook.command.Save"));
					}
				});
				// columnCountSpinner = new Spinner(sectionClient, SWT.BORDER);
				// toolkit.adapt(columnCountSpinner, true, true);
				// columnCountSpinner.setMinimum(1);
				// columnCountSpinner.setMaximum(5);
				// columnCountSpinner
				// .setSelection(section.getOptions().columnCount);
				// columnCountSpinner.setIncrement(1);
				// columnCountSpinner.setPageIncrement(1);
				// columnCountSpinner.addSelectionListener(new
				// SelectionAdapter() {
				// @Override
				// public void widgetSelected(SelectionEvent e) {
				// super.widgetSelected(e);
				// dirty.setDirty(true);
				// }
				//
				// });

			}
		};

		addSection(Strings.value("options"), optionsTune);

	}

	private void addImageSections() {
		final Device display = body.getDisplay();

		imageList = book.srv().getImages(section.getId());

		for (final SectionImage sectionImage : imageList) {

			IBlockTune pictureTune = new IBlockTune() {
				@Override
				public void tune(FormToolkit toolkit, Section group,
						Composite sectionClient) {

					group.setText(sectionImage.getTitle());
					group.setExpanded(true);

					ImageHyperlink hlink = toolkit.createImageHyperlink(
							sectionClient, SWT.WRAP);

					hlink.setImage(sectionImage.getScaled(display,
							section.getOptions()));
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {

							try {
								File temp = File.createTempFile("temp", "."
										+ sectionImage.getMime());
								Image image = sectionImage.image;

								ImageLoader saver = new ImageLoader();
								saver.data = new ImageData[] { image
										.getImageData() };

								saver.save(temp.getAbsolutePath(),
										sectionImage.getFormat());

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
					hlink.setImage(Utils.getImage("link.png"));
					hlink.setText("Вставить в текст");
					hlink.setToolTipText("Вставить ссылку в текст");
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {

						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();
							tinymce.addLink(image.getId(), image.getTitle());
						}
					});

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("edit.png"));
					hlink.setToolTipText("Изменить заголовок");
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {

						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();

							InputDialog dlg = new InputDialog(shell,
									ebook.utils.Strings.title("appTitle"),
									"Введите заголовок картинки:", image
											.getTitle(), null);
							if (dlg.open() == Window.OK) {
								book.srv().save_image_title(section, image,
										dlg.getValue());
							}
						}
					});

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("import.png"));
					hlink.setToolTipText("Изменить");
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {

						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();

							IPath p = Utils.browseFile(book.getFullPath(),
									shell, Strings.title("appTitle"),
									SectionImage.getFilters());
							if (p == null)
								return;

							book.srv().edit_image(section, image, p);
						}
					});

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("delete.png"));
					hlink.setToolTipText("Удалить");
					hlink.setHref(sectionImage);
					hlink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();
							book.srv().delete_image(section, image);
						}
					});

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("up.png"));
					hlink.setHref(sectionImage);
					hlink.setToolTipText("Переместить вверх");
					hlink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();
							book.srv().move_image(section, image, true);
						}
					});

					hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
					hlink.setImage(Utils.getImage("down.png"));
					hlink.setHref(sectionImage);
					hlink.setToolTipText("Переместить вниз");
					hlink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {
							SectionImage image = (SectionImage) e.getHref();
							book.srv().move_image(section, image, false);
						}
					});
				}
			};
			addSection(Strings.value("picture"), pictureTune);
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

	public Integer getId() {
		return section.getId();
	}

}
