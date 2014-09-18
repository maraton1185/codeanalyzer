package ebook.module.book.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ebook.module.book.BookConnection;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.ITextImagesView;
import ebook.module.book.views.tools.ImagesComposite;
import ebook.module.book.views.tools.TextEdit;
import ebook.utils.Events;
import ebook.utils.Utils;

public class SectionView implements ITextImagesView {

	@Inject
	@Active
	BookConnection book;
	@Inject
	private EHandlerService hService;
	@Inject
	private ECommandService comService;

	TextEdit text;

	FormToolkit toolkit;

	SectionInfo section;

	@Inject
	MDirtyable dirty;

	@Inject
	Shell shell;

	private ImagesComposite imagesComposite;

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

		book.srv().saveText(section, text.getText());
		dirty.setDirty(false);
		save_index = 2;
	}

	public void update() {

		imagesComposite.update(section);

		text.updateUrl();
	}

	@PostConstruct
	public void postConstruct(final Composite parent,
			@Active SectionInfo section) {

		dirty.setDirty(false);

		this.section = section;
		// this.window = window;
		toolkit = new FormToolkit(parent.getDisplay());

		// **************************************************************

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setBackground(sashForm.getDisplay().getSystemColor(
				SWT.COLOR_GRAY));
		imagesComposite = new ImagesComposite(sashForm, SWT.NONE, this);
		// Composite leftComposite = new Composite(sashForm, SWT.NONE);
		// leftComposite.setLayout(new FillLayout());
		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new FillLayout());

		text = new TextEdit(rightComposite, section, book.srv());
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		imagesComposite.update(section);
	}

	// ********************************************************************

	public Integer getId() {
		return section.getId();
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

	@Override
	public TextEdit getTextEditor() {
		return text;
	}

	@Override
	public boolean textEdit() {
		return true;
	}

	public void addImage() {
		imagesComposite.addImage(section);

	}

}
