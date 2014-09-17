package ebook.module.book.views.tools;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.IBlockTune;
import ebook.module.book.views.interfaces.IPictureTuneData;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class PictureTune implements IBlockTune {

	ImageHyperlink blockImage;
	Section blockGroup;

	private SectionImage sectionImage;

	private Composite comp;

	IPictureTuneData tune;
	private SectionInfo section;

	public PictureTune(IPictureTuneData tuneData, SectionImage sectionImage,
			SectionInfo section) {
		this.tune = tuneData;
		this.sectionImage = sectionImage;
		this.section = section;
		comp = tuneData.getImagesComposite();

	}

	@Override
	public void tune(FormToolkit toolkit, Section group,
			final Composite sectionClient) {

		group.setText(sectionImage.getTitle());
		group.setExpanded(true);
		group.setData(sectionImage);

		blockGroup = group;

		blockImage = toolkit.createImageHyperlink(sectionClient, SWT.WRAP);

		blockImage.setImage(sectionImage.getScaled(comp));
		blockImage.setHref(sectionImage);
		blockImage.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				try {
					File temp = File.createTempFile("temp",
							"." + sectionImage.getMime());
					Image image = sectionImage.image;

					ImageLoader saver = new ImageLoader();
					saver.data = new ImageData[] { image.getImageData() };

					saver.save(temp.getAbsolutePath(), sectionImage.getFormat());

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
		blockImage.setLayoutData(gd);

		Listener list = new Listener() {
			@Override
			public void handleEvent(Event e) {
				blockImage.setImage(sectionImage.getScaled(comp));
				update(sectionClient);
			}
		};

		comp.addListener(SWT.Resize, list);
		tune.getListeners().put(sectionClient, list);

		addPanel(sectionClient);

	}

	private void addPanel(final Composite sectionClient) {

		FormToolkit toolkit = tune.getToolkit();
		Composite panel = toolkit.createComposite(sectionClient);
		panel.setLayout(new RowLayout());
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.grabExcessHorizontalSpace = true;
		panel.setLayoutData(gd);

		ImageHyperlink hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("edit.png"));
		hlink.setToolTipText("Изменить заголовок");
		hlink.setHref(sectionImage);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				SectionImage image = (SectionImage) e.getHref();

				InputDialog dlg = new InputDialog(sectionClient.getShell(),
						ebook.utils.Strings.title("appTitle"),
						"Введите заголовок картинки:", image.getTitle(), null);
				if (dlg.open() == Window.OK) {
					tune.srv().save_image_title(section, image, dlg.getValue());
					blockGroup.setText(image.getTitle());

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

				IPath p = Utils.browseFile(
						new Path(
								PreferenceSupplier
										.get(PreferenceSupplier.DEFAULT_IMAGE_DIRECTORY)),
						sectionClient.getShell(), Strings.title("appTitle"),
						SectionImage.getFilters());
				if (p == null)
					return;

				tune.srv().edit_image(section, image, p);
				blockImage.setImage(image.getScaled(comp));
				update(sectionClient);

			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("delete.png"));
		hlink.setToolTipText("Удалить");
		hlink.setHref(sectionImage);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if (!MessageDialog.openConfirm(sectionClient.getShell(),
						Strings.title("appTitle"), "Удалить картинку?"))
					return;

				SectionImage image = (SectionImage) e.getHref();
				tune.srv().delete_image(section, image);
				tune.getListeners().remove(sectionClient);
				blockGroup.dispose();
				update(sectionClient);
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("up.png"));
		hlink.setHref(sectionImage);
		hlink.setToolTipText("Переместить вверх");
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// SectionImage image = (SectionImage) e.getHref();
				// tune.srv().move_image(section, image, true);
				tune.moveUp((ImageHyperlink) e.getSource());
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("down.png"));
		hlink.setHref(sectionImage);
		hlink.setToolTipText("Переместить вниз");
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				// SectionImage image = (SectionImage) e.getHref();
				// tune.srv().move_image(section, image, false);
				tune.moveDown((ImageHyperlink) e.getSource());
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("add.png"));
		hlink.setToolTipText("Добавить картинку");
		hlink.setUnderlined(false);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				IPath p = Utils.browseFile(
						new Path(
								PreferenceSupplier
										.get(PreferenceSupplier.DEFAULT_IMAGE_DIRECTORY)),
						sectionClient.getShell(), Strings.title("appTitle"),
						SectionImage.getFilters());
				if (p == null)
					return;

				int id = tune.srv().add_image(section, p, null);
				tune.addImage(section, id);
			}
		});

	}

	protected void update(Composite sectionClient) {
		comp.setRedraw(false);
		if (!sectionClient.isDisposed())
			sectionClient.layout();
		tune.reflow();
		comp.setRedraw(true);
	}

}
