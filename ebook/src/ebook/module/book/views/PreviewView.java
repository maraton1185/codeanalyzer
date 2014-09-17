package ebook.module.book.views;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.module.book.BookConnection;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.ITextImagesView;
import ebook.module.book.views.tools.ImagesComposite;
import ebook.module.book.views.tools.TextPreview;
import ebook.utils.Events;
import ebook.utils.Utils;

public class PreviewView implements ITextImagesView {

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

	@Inject
	MDirtyable dirty;
	@Inject
	Shell shell;

	private ImagesComposite imagesComposite;

	@Focus
	public void OnFocus(@Active @Optional SectionInfo data) {
		update(data);
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_INFO(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_INFO) Object o,
			@Active @Optional SectionInfo data) {

		update(data);

	}

	public void update(SectionInfo data) {
		if (data != null && !data.isGroup()) {
			text.setText(book.srv().getText(data.getId()));
			imagesComposite.update(data);
		}

		updateLayout(data);
	}

	private void updateLayout(SectionInfo data) {
		if (data == null || data.isGroup())
			stackLayout.topControl = groupComp;
		else
			stackLayout.topControl = itemComp;
		stack.layout();
		itemComp.layout(true);
		groupComp.layout(true);
		imagesComposite.reflow();

	}

	@PostConstruct
	public void postConstruct(Composite parent, @Active final MWindow window,
			@Active MPart part) {

		// this.window = window;
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

		// **************************************************************
		groupComp = new Composite(stack, SWT.NONE);
		groupComp.setLayout(new FillLayout());

		ScrolledForm f = toolkit.createScrolledForm(groupComp);
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		gd.grabExcessVerticalSpace = true;
		f.setLayoutData(gd);
		f.setText("group");

		// **************************************************************
		// updateLayout(section);

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
		dirty.setDirty(true);

	}

	@Override
	public void executeHandler(String id) {
		Utils.executeHandler(hService, comService, id);

	}

}