package ebook.module.bookList.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import ebook.core.models.ModelObject;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeService;
import ebook.module.userList.tree.UserInfo;

public class BookViewModel extends ModelObject {

	public ListBookInfo data;

	public String description = "";

	private ListBookInfoOptions options;

	public BookViewModel(ListBookInfo data) {
		super();
		this.data = data;
		this.options = (ListBookInfoOptions) data.getOptions();
		readDescription();

	}

	private void readDescription() {
		if (data.isGroup())
			return;
		if (!description.isEmpty())
			return;
		if (data.getPath() == null)
			return;

		try {
			File f = new File(data.getPath().addFileExtension("txt").toString());
			if (!f.exists())
				throw new FileNotFoundException();

			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			try {
				Reader in = new InputStreamReader(new FileInputStream(f));
				br = new BufferedReader(in);
				String source_line = null;
				while ((source_line = br.readLine()) != null) {
					sb.append(source_line + '\n');
				}
			} finally {
				br.close();
			}
			description = sb.toString();

		} catch (Exception e) {
			description = "Нет описания";
		}

	}

	public ListBookInfo getData() {
		return data;
	}

	public String getPath() {
		return options.path;
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

	public String getBookDescription() {

		return description;
	}

	public UserInfo getRole() {
		return data.role;
	}

	public void setRole(UserInfo value) {

		fireIndexedPropertyChange("role", data.role, data.role = value);
	}

	public boolean isShowRole() {
		return data.getParent() == ITreeService.rootId;
	}

}
