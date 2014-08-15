package ebook.module.conf.model;

public class AdditionalInfo {

	public BuildType type;
	public boolean searchByGroup2 = false;
	public boolean searchByText = false;

	public void setSearchByText(boolean searchByText) {
		this.searchByText = searchByText;
		if (searchByText)
			type = BuildType.text;
	}

	public boolean getProc = false;

}
