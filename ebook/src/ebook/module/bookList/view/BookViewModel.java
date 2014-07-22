package ebook.module.bookList.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;

import ebook.core.App;
import ebook.core.models.ModelObject;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeService;
import ebook.module.userList.tree.UserInfo;
import ebook.module.userList.views.RoleViewModel;

public class BookViewModel extends ModelObject {

	public ListBookInfo data;
	private ListBookInfoOptions options;

	// public String description = "";

	public void setData(ListBookInfo data) {
		this.data = data;
		this.options = (ListBookInfoOptions) data.getOptions();
	}

	// public BookViewModel(ListBookInfo data) {
	// super();
	// this.data = data;
	// this.options = (ListBookInfoOptions) data.getOptions();
	// // readDescription();
	//
	// }

	public ListBookInfo getData() {
		return data;
	}

	public String getPath() {
		IPath path = data.getPath();
		return path == null ? "" : path.toString();
	}

	public String getTitle() {
		return data.getTitle();
	}

	public boolean isGroup() {
		return data.isGroup();
	}

	public boolean isItem() {
		return !data.isGroup();
	}

	public String getDescription() {
		return options.description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", options.description,
				options.description = value);
	}

	public Image getImage() {
		return data.getImage();
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
		return data.getParent() == ITreeService.rootId;
	}

	public Integer getId() {
		return data.getId();
	}

	List<RoleViewModel> roles = new ArrayList<RoleViewModel>();

	public List<RoleViewModel> getRoles() {

		return roles;
	}

	public void setRoles() {
		List<RoleViewModel> result = new ArrayList<RoleViewModel>();

		List<UserInfo> input = App.srv.us().getRoles();
		for (UserInfo info : input) {

			RoleViewModel item = new RoleViewModel();
			item.title = info.getTitle();
			item.id = info.getId();
			result.add(item);

		}
		roles = result;
		firePropertyChange("roles", null, null);

		Set<RoleViewModel> result1 = new HashSet<RoleViewModel>();

		List<UserInfo> input1 = App.srv.acl().get(data.getId());

		for (UserInfo info : input1) {

			RoleViewModel item = new RoleViewModel();
			item.title = info.getTitle();
			item.id = info.getId();
			result1.add(item);
		}

		activeRoles = result1;

		firePropertyChange("activeRoles", null, null);
	}

	private Set<RoleViewModel> activeRoles = new HashSet<RoleViewModel>();

	public Set<RoleViewModel> getActiveRoles() {
		return new HashSet<RoleViewModel>(activeRoles);
	}

	public void setActiveRoles(Set<RoleViewModel> value) {

		// set to db
		firePropertyChange("activeRoles", this.activeRoles,
				this.activeRoles = new HashSet<RoleViewModel>(value));
	}
}
