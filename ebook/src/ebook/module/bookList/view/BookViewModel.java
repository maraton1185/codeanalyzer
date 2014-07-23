package ebook.module.bookList.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.core.models.ModelObject;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.db.ACLService.ACLResult;
import ebook.module.tree.ITreeService;
import ebook.module.userList.tree.UserInfo;
import ebook.module.userList.views.RoleViewModel;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class BookViewModel extends ModelObject {

	public ListBookInfo info;
	private ListBookInfoOptions options;

	// public String description = "";

	public void setData(ListBookInfo data, CheckboxTableViewer rolesViewer) {
		this.info = data;
		this.options = (ListBookInfoOptions) data.getOptions();
		this.rolesViewer = rolesViewer;
	}

	// public BookViewModel(ListBookInfo data) {
	// super();
	// this.data = data;
	// this.options = (ListBookInfoOptions) data.getOptions();
	// // readDescription();
	//
	// }

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

	// public String getBookDescription() {
	//
	// return description;
	// }

	// public UserInfo getRole() {
	// return data.role;
	// }
	//
	// public void setRole(UserInfo value) {
	//
	// fireIndexedPropertyChange("role", data.role, data.role = value);
	// }

	public boolean isShowRole() {
		return info.getParent() == ITreeService.rootId;
	}

	public Integer getId() {
		return info.getId();
	}

	List<RoleViewModel> roles = new ArrayList<RoleViewModel>();
	private CheckboxTableViewer rolesViewer;

	public List<RoleViewModel> getRoles() {

		return roles;
	}

	public void setRoles() {

		if (!info.isGroup()) {

			roles.clear();
			activeRoles.clear();

			return;
		}

		List<RoleViewModel> result = new ArrayList<RoleViewModel>();

		List<UserInfo> input = App.srv.us().getRoles();
		for (UserInfo info : input) {

			RoleViewModel item = new RoleViewModel(info.getId());
			item.setTitle(info.getTitle());
			result.add(item);

		}
		roles = result;
		firePropertyChange("roles", null, null);

		ACLResult out = new ACLResult();
		activeRoles = App.srv.acl().get(info.getId(), out);

		firePropertyChange("activeRoles", null, null);

		if (out.inherited)
			rolesViewer.getTable().setBackground(
					Display.getCurrent().getSystemColor(
							SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			rolesViewer.getTable().setBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

	}

	private Set<RoleViewModel> activeRoles = new HashSet<RoleViewModel>();

	public Set<RoleViewModel> getActiveRoles() {
		return new HashSet<RoleViewModel>(activeRoles);
	}

	public void setActiveRoles(Object[] objects) {

		App.srv.acl().set(info.getId(), objects);

		rolesViewer.getTable().setBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		info.setACL();

		App.br.post(Events.EVENT_UPDATE_LABELS_BOOK_LIST,
				new EVENT_UPDATE_TREE_DATA(info, null));

	}
}
