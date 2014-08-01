package ebook.module.bookList.view;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.module.acl.ACLService.ACLResult;
import ebook.module.acl.AclRolesViewModel;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeService;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class BookViewModel extends AclRolesViewModel {

	public ListBookInfo info;
	private ListBookInfoOptions options;

	public BookViewModel(CheckboxTableViewer rolesViewer, ListBookInfo info) {
		super(rolesViewer, info);
		if (info != null) {
			this.info = info;
			this.options = (ListBookInfoOptions) info.getOptions();
		}
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

	public boolean getACL() {
		return options.ACL;
	}

	public void setACL(boolean value) {

		fireIndexedPropertyChange("ACL", options.ACL, options.ACL = value);
	}

	public boolean getContext() {
		return options.Context;
	}

	public void setContext(boolean value) {

		fireIndexedPropertyChange("context", options.Context,
				options.Context = value);
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

	@Override
	protected void loadActiveRoles(ACLResult out) {
		activeRoles = App.srv.acl().get(info.getId(), out);

	}

	@Override
	protected void saveActiveRoles(Object[] objects) {
		App.srv.acl().set(info.getId(), objects);

	}

	@Override
	protected void updateList() {
		App.br.post(Events.EVENT_UPDATE_LABELS_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(info, null));

	}

}
