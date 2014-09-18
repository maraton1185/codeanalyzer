package ebook.module.book.views.tools;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.book.views.interfaces.IBlockTune;
import ebook.module.book.views.interfaces.IPictureTuneData;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OptionsTune implements IBlockTune {

	private IPictureTuneData tune;
	private SectionInfo section;
	Scale scaledImageWidthSlider;
	private FormToolkit toolkit;

	public OptionsTune(IPictureTuneData tuneData, SectionInfo section) {
		this.tune = tuneData;
		this.section = section;
		toolkit = tuneData.getToolkit();
		// comp = tuneData.getImagesComposite();

	}

	@Override
	public void tune(FormToolkit toolkit, Section group, Composite sectionClient) {

		group.setExpanded(true);

		// toolkit.createLabel(sectionClient,
		// Strings.value("scaledImageWidthSlider"));

		scaledImageWidthSlider = new Scale(sectionClient, SWT.HORIZONTAL);
		toolkit.adapt(scaledImageWidthSlider, true, true);
		scaledImageWidthSlider.setToolTipText(Strings
				.value("scaledImageWidthSlider"));
		scaledImageWidthSlider.setMaximum(SectionInfoOptions.gridScaleMax);
		scaledImageWidthSlider.setMinimum(SectionInfoOptions.gridScaleMin);
		// scaledImageWidthSlider.setIncrement(20);
		scaledImageWidthSlider
				.setPageIncrement(SectionInfoOptions.scaleIncrement);
		scaledImageWidthSlider.setSelection(section.getOptions()
				.getBigImageCSS());
		scaledImageWidthSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				SectionInfoOptions opt = section.getOptions();
				opt.setBigImageCSS(scaledImageWidthSlider.getSelection());
				tune.srv().saveOptions(section, opt);

			}

		});

		scaledImageWidthSlider.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		addPanel(sectionClient);

	}

	private void addPanel(final Composite sectionClient) {
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

				tune.addImage(section);

			}

		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("expand.png"));
		hlink.setToolTipText("Развернуть");
		hlink.setUnderlined(false);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				tune.expand();
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("collapse.png"));
		hlink.setToolTipText("Свернуть");
		hlink.setUnderlined(false);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				tune.collapse();
			}
		});

		hlink = toolkit.createImageHyperlink(panel, SWT.WRAP);
		hlink.setImage(Utils.getImage("order.png"));
		hlink.setToolTipText("Перенумеровать");
		hlink.setUnderlined(false);
		hlink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				if (!MessageDialog.openConfirm(sectionClient.getShell(),
						Strings.title("appTitle"), "Переименовать по порядку?"))
					return;

				tune.rename(section);
			}
		});

	}

}
