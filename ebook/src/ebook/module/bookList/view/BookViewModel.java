package ebook.module.bookList.view;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;

import ebook.core.models.ModelObject;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeService;
import ebook.module.userList.tree.UserInfo;

public class BookViewModel extends ModelObject {

	public ListBookInfo data;

	// public String description = "";

	private final ListBookInfoOptions options;

	public BookViewModel(ListBookInfo data) {
		super();
		this.data = data;
		this.options = (ListBookInfoOptions) data.getOptions();
		// readDescription();

	}

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

	public UserInfo getRole() {
		return data.role;
	}

	public void setRole(UserInfo value) {

		fireIndexedPropertyChange("role", data.role, data.role = value);
	}

	public boolean isShowRole() {
		return data.getParent() == ITreeService.rootId;
	}

	public Integer getId() {
		return data.getId();
	}

}
