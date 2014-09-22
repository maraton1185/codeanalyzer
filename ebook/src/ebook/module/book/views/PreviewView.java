package ebook.module.book.views;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.module.book.BookConnection;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.IBrowserBridgeView;
import ebook.module.book.views.interfaces.ITextImagesView;
import ebook.module.book.views.tools.ImagesComposite;
import ebook.module.book.views.tools.TextEdit;
import ebook.module.book.views.tools.TextPreview;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class PreviewView implements ITextImagesView, IBrowserBridgeView {

	@Inject
	@Active
	BookConnection book;
	@Inject
	private EHandlerService hService;
	@Inject
	private ECommandService comService;
	// @Inject
	// @Active
	// @Optional
	// private SectionInfo section;

	// private MWindow window;
	// private MPart part;

	TextPreview text;

	FormToolkit toolkit;

	Composite stack;
	StackLayout stackLayout;
	Composite groupComp;
	Composite itemComp;
	Composite nullComp;

	ScrolledForm groupForm;
	Composite groupBody;

	@Inject
	MDirtyable dirty;
	@Inject
	Shell shell;

	@Inject
	@Active
	@Optional
	MPart part;

	private ImagesComposite imagesComposite;
	private MWindow window;
	private boolean blockUpdate = false;
	private SectionInfo section;

	@Focus
	public void OnFocus(@Active @Optional SectionInfo data) {
		update(data);
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_INFO) EVENT_UPDATE_VIEW_DATA data,
			@Active @Optional SectionInfo section) {

		if (data == null)
			return;
		if (data.con != book)
			return;
		// TODO: не обновлять, если форма не активна
		// if (part != null && !part.isOnTop())
		// return;

		update(section);

	}

	public void update(SectionInfo data) {

		if (blockUpdate)
			return;

		this.section = data;

		if (data == null) {
			stackLayout.topControl = nullComp;

		} else if (data.isGroup()) {

			stackLayout.topControl = groupComp;
			updateGroup(data);

		} else if (!data.isGroup()) {

			stackLayout.topControl = itemComp;
			text.setText(book.srv().getText(data.getId()));
			imagesComposite.update(data);
		}

		updateLayout();
	}

	private void updateLayout() {

		stack.layout();
		itemComp.layout(true);
		groupComp.layout(true);
		nullComp.layout(true);
		imagesComposite.reflow();

	}

	@PostConstruct
	public void postConstruct(Composite parent, @Active final MWindow window,
			@Active MPart part) {

		this.window = window;
		// this.part = part;

		stack = parent;
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);
		toolkit = new FormToolkit(stack.getDisplay());

		// **************************************************************
		itemComp = new Composite(stack, SWT.NONE);
		itemComp.setLayout(new FillLayout());

		SashForm sashForm = new SashForm(itemComp, SWT.HORIZONTAL);
		sashForm.setBackground(sashForm.getDisplay().getSystemColor(
				SWT.COLOR_GRAY));

		imagesComposite = new ImagesComposite(sashForm, SWT.NONE, this);
		// leftComposite = new Composite(sashForm, SWT.NONE);
		// leftComposite.setLayout(new FillLayout());
		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new FillLayout());

		text = new TextPreview(rightComposite);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		// renderGroups();

		GridData gd;

		// **************************************************************
		nullComp = new Composite(stack, SWT.NONE);
		nullComp.setLayout(new FillLayout());

		ScrolledForm fnull = toolkit.createScrolledForm(nullComp);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		fnull.setLayoutData(gd);
		fnull.setText("Нет данных для просмотра");

		// **************************************************************
		groupComp = new Composite(stack, SWT.NONE);
		groupComp.setLayout(new FillLayout());

		groupForm = toolkit.createScrolledForm(groupComp);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		groupForm.setLayoutData(gd);
		groupForm.setText("Раздел");

		toolkit.decorateFormHeading(groupForm.getForm());

		groupBody = groupForm.getBody();
		toolkit.paintBordersFor(groupBody);

		GridLayout layout1 = new GridLayout();
		layout1.numColumns = 1;
		groupBody.setLayout(layout1);
		groupBody.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		// groupBody.setForeground();

	}

	public void updateGroup(SectionInfo data) {

		groupForm.setText(data.getTitle());

		groupComp.setRedraw(false);

		for (Control ctrl : groupBody.getChildren()) {

			ctrl.dispose();
		}

		ImageHyperlink hlink = null;

		List<ITreeItemInfo> list = book.srv().getChildren(data.getId());
		for (ITreeItemInfo item : list) {

			hlink = toolkit.createImageHyperlink(groupBody, SWT.RIGHT);
			hlink.setText(item.getTitle());
			hlink.setForeground(groupBody.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));

			// hlink.setImage(Utils.getImage("markers/module.png"));

			FontData fontDatas[] = groupBody.getFont().getFontData();
			FontData fd = fontDatas[0];
			Font font;
			int height;
			if (item.isGroup()) {

				height = 20;// fd.getHeight();
				// height = (int) (height - 0.2 * height);
				font = new Font(Display.getCurrent(), fd.getName(), height,
						SWT.BOLD);
			} else {
				height = 20;// fd.getHeight();
				height = (int) (height - 0.2 * height);
				font = new Font(Display.getCurrent(), fd.getName(), height,
						SWT.NORMAL);
				// hlink.setImage(Utils.getImage("markers/object.png"));
			}
			hlink.setFont(font);

			// hlink.setToolTipText("Перейти к разделу");
			hlink.setHref(item);
			hlink.setUnderlined(false);
			hlink.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {

					SectionInfo item = (SectionInfo) e.getHref();

					window.getContext().set(SectionInfo.class, item);

					executeHandler(Strings.model("command.id.ShowSection"));

				}
			});

			// hlink.setBackground(groupForm.getDisplay().getSystemColor(
			// SWT.COLOR_BLACK));

			GridDataFactory.fillDefaults().grab(true, false)
					.align(SWT.CENTER, SWT.CENTER).applyTo(hlink);

			// hlink.setFocus();
		}

		if (hlink != null)
			hlink.setFocus();

		groupForm.reflow(true);
		groupComp.setRedraw(true);

	}

	@Override
	public FormToolkit getToolkit() {
		return toolkit;
	}

	@Override
	public BookService srv() {
		return book.srv();
	}

	@Override
	public void setDirty() {
		// dirty.setDirty(true);
	}

	@Override
	public void executeHandler(String id) {
		Utils.executeHandler(hService, comService, id);

	}

	@Override
	public TextEdit getTextEditor() {
		return null;
	}

	@Override
	public boolean textEdit() {
		return false;
	}

	public void triggerBlock() {
		blockUpdate = !blockUpdate;

		// MDirectToolItem data = (MDirectToolItem) App.model.find(
		// Strings.model("ebook.directtoolitem.blockPreview"), App.app);
		//
		// data.setSelected(blockUpdate);

	}

	@Override
	public SectionInfo getSection() {
		return section;
	}

}