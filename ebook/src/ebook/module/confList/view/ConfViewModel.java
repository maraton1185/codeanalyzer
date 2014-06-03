package ebook.module.confList.view;

import ebook.core.models.ModelObject;
import ebook.module.confList.tree.ListConfInfo;

public class ConfViewModel extends ModelObject {

	public ListConfInfo data;

	public String description = "";

	public ConfViewModel(ListConfInfo data) {
		super();
		this.data = data;

		// readDescription();

	}

	// private void readDescription() {
	// if (data.isGroup())
	// return;
	// if (!description.isEmpty())
	// return;
	// if (data.getPath() == null)
	// return;
	//
	// try {
	// File f = new File(data.getPath().addFileExtension("txt").toString());
	// if (!f.exists())
	// throw new FileNotFoundException();
	//
	// BufferedReader br = null;
	// StringBuffer sb = new StringBuffer();
	// try {
	// Reader in = new InputStreamReader(new FileInputStream(f));
	// br = new BufferedReader(in);
	// String source_line = null;
	// while ((source_line = br.readLine()) != null) {
	// sb.append(source_line + '\n');
	// }
	// } finally {
	// br.close();
	// }
	// description = sb.toString();
	//
	// } catch (Exception e) {
	// description = "Нет описания";
	// }
	//
	// }

	public ListConfInfo getData() {
		return data;
	}

	public String getPath() {
		return data.options.path;
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
		return data.options.description;
	}

	public void setDescription(String value) {

		fireIndexedPropertyChange("description", data.options.description,
				data.options.description = value);
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
	//
	// public boolean isShowRole() {
	// return data.getParent() == ITreeService.rootId;
	// }

}
