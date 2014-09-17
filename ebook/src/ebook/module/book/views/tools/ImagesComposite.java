package ebook.module.book.views.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.IBlockTune;
import ebook.module.book.views.interfaces.IPictureTuneData;
import ebook.module.book.views.interfaces.ITextImagesView;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class ImagesComposite extends Composite implements IPictureTuneData {

	ScrolledForm form;
	Composite body;
	List<SectionImage> imageList;

	HashMap<Composite, Listener> listeners = new HashMap<Composite, Listener>();
	private FormToolkit toolkit;

	private ITextImagesView view;

	public ImagesComposite(Composite parent, int style, ITextImagesView view) {
		super(parent, style);
		setLayout(new FillLayout());

		this.view = view;
		toolkit = view.getToolkit();

		form = toolkit.createScrolledForm(this);

		setBackground(form.getBackground());
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

	}

	public void update(SectionInfo data) {

		setRedraw(false);
		for (Listener list : listeners.values()) {
			removeListener(SWT.Resize, list);
		}
		listeners.clear();
		for (Control ctrl : body.getChildren()) {

			ctrl.dispose();
		}

		addSection(Strings.value("options"), new OptionsTune(this, data));

		addImageSections(data);

		reflow();

		setRedraw(true);

	}

	private void addImageSections(final SectionInfo section) {

		if (section == null)
			return;

		imageList = view.srv().getImages(section.getId());

		for (SectionImage sectionImage : imageList) {

			PictureTune pictureTune = new PictureTune(this, sectionImage,
					section);

			addSection(Strings.value("picture"), pictureTune);
		}
	}

	@Override
	public void addImage(SectionInfo section, int id) {

		SectionImage img = view.srv().getImage(id);

		PictureTune pictureTune = new PictureTune(this, img, section);

		addSection(Strings.value("picture"), pictureTune);

		reflow();

	}

	public void addSection(String title, IBlockTune opt) {
		Section section = toolkit.createSection(body, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);

		section.setLayout(new FillLayout());

		section.setText(title);

		DragSource source = new DragSource(section, DND.DROP_NONE);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new SectionDragSourceListener(body, source));
		DropTarget target = new DropTarget(section, DND.DROP_NONE);
		target.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		target.addDropListener(new SectionDropTargetListener(this, body, target));

		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout(1, false));

		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});

		opt.tune(toolkit, section, sectionClient);
		section.setClient(sectionClient);
	}

	@Override
	public void reflow() {

		setRedraw(false);
		form.reflow(true);
		setRedraw(true);

	}

	@Override
	public Composite getImagesComposite() {
		return this;
	}

	@Override
	public HashMap<Composite, Listener> getListeners() {
		return listeners;
	}

	@Override
	public FormToolkit getToolkit() {
		// TODO Auto-generated method stub
		return toolkit;
	}

	@Override
	public BookService srv() {
		return view.srv();
	}

	@Override
	public void expand() {
		setRedraw(false);

		for (int i = 0; i < body.getChildren().length; i++) {

			Section _section = (Section) body.getChildren()[i];

			SectionImage img = (SectionImage) _section.getData();
			if (img == null)
				continue;

			_section.setExpanded(true);
		}

		reflow();

		setRedraw(true);

	}

	@Override
	public void collapse() {
		setRedraw(false);

		for (int i = 0; i < body.getChildren().length; i++) {

			Section _section = (Section) body.getChildren()[i];

			SectionImage img = (SectionImage) _section.getData();
			if (img == null)
				continue;

			_section.setExpanded(false);
		}

		reflow();

		setRedraw(true);

	}

	@Override
	public void reorder(SectionInfo section) {
		int index = 1;
		for (int i = 0; i < body.getChildren().length; i++) {

			Section _section = (Section) body.getChildren()[i];

			SectionImage img = (SectionImage) _section.getData();
			if (img == null)
				continue;

			String title = PreferenceSupplier
					.get(PreferenceSupplier.IMAGE_TITLE);
			title = title + "" + index;
			index++;
			view.srv().save_image_title(section, img, title);
			_section.setText(title);

		}

	}

	@Override
	public void moveUp(ImageHyperlink imageHyperlink) {
		Section section = (Section) imageHyperlink.getParent().getParent()
				.getParent();
		List<Control> list = Arrays.asList(body.getChildren());
		int i = list.indexOf(section);
		if (i == 1)
			body.getChildren()[i]
					.moveBelow(body.getChildren()[list.size() - 1]);
		else
			body.getChildren()[i].moveAbove(body.getChildren()[i - 1]);
		body.layout();
		reflow();

	}

	@Override
	public void moveDown(ImageHyperlink imageHyperlink) {
		Section section = (Section) imageHyperlink.getParent().getParent()
				.getParent();
		List<Control> list = Arrays.asList(body.getChildren());
		int i = list.indexOf(section);
		if (i == list.size() - 1)
			body.getChildren()[i].moveBelow(body.getChildren()[0]);
		else
			body.getChildren()[i].moveBelow(body.getChildren()[i + 1]);
		body.layout();
		reflow();
	}

	@Override
	public void reorder() {
		List<SectionImage> items = new ArrayList<SectionImage>();

		for (int i = 0; i < body.getChildren().length; i++) {

			Section _section = (Section) body.getChildren()[i];

			SectionImage img = (SectionImage) _section.getData();
			if (img == null)
				continue;

			items.add(img);

		}

		try {
			view.srv().updateImagesOrder(items);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
