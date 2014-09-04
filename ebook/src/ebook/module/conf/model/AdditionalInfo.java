package ebook.module.conf.model;

import ebook.module.confLoad.model.ELevel;

public class AdditionalInfo {

	public String filter = "";
	public String itemTitle;
	public BuildType type;
	public boolean searchByGroup2 = false;
	public boolean searchByText = false;
	public boolean searchByProc = false;

	public void setSearchByText(boolean searchByText) {
		this.searchByText = searchByText;
		if (searchByText)
			type = BuildType.text;
	}

	public void setSearchByProc(boolean searchByProc) {
		this.searchByProc = searchByProc;
		if (searchByProc)
			type = BuildType.proc;
	}

	public boolean getProc = false;
	public ELevel level;
	public boolean textSearchWithoutLines = false;

}
