package ebook.module.bookList.view;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.graphics.Image;

import ebook.module.acl.AclRolesViewModel;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeService;
import ebook.utils.Events;

public class BookViewModel extends AclRolesViewModel {

	public ListBookInfo info;
	private ListBookInfoOptions options;

	public BookViewModel(CheckboxTableViewer rolesViewer, ListBookInfo info) {
		super(rolesViewer, info, Events.EVENT_UPDATE_LABELS_BOOK_LIST);
		this.info = info;
		this.options = (ListBookInfoOptions) info.getOptions();
	}

	public ListBookInfo getData() {
		return info;
	}

	public String getPath() {
		IPath path = info.getPath();
		return path == null ? "" : path.toString();
	}

	public String getTitle() {
		return info.getTitle();
	}

	public boolean isGroup() {
		return info.isGroup();
	}

	public boolean isItem() {
		return !info.isGroup();
	}

	public String getDescription() {
		return options.description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", options.description,
				options.description = value);
	}

	public Image getImage() {
		return info.getImage();
	}

	public boolean isShowRole() {
		return info.getParent() == ITreeService.rootId;
	}

	public Integer getId() {
		return info.getId();
	}

}
