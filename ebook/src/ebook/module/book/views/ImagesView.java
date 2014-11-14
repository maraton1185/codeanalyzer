package ebook.module.book.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import ebook.module.book.BookConnection;
import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.ITextEditor;
import ebook.module.book.views.interfaces.ITextImagesView;
import ebook.module.book.views.tools.ImagesComposite;

public class ImagesView implements ITextImagesView {

	@Inject
	@Active
	BookConnection book;
	@Inject
	private EHandlerService hService;
	@Inject
	private ECommandService comService;

	@Inject
	Shell shell;

	private ImagesComposite imagesComposite;

	private MWindow window;

	FormToolkit toolkit;

	SectionInfo section;

	@PostConstruct
	public void postConstruct(final Composite parent,
			@Active SectionInfo section, @Active final MWindow window) {

		this.window = window;
		this.section = section;

		imagesComposite = new ImagesComposite(parent, SWT.NONE, this);

		imagesComposite.update(section);

	}

	@Override
	public FormToolkit getToolkit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookService srv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDirty() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeHandler(String model) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITextEditor getTextEditor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean textEdit() {
		// TODO Auto-generated method stub
		return false;
	}

}