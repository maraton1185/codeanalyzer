package ebook.module.conf.model;

import ebook.module.confLoad.model.ELevel;

public class AdditionalInfo {

	public String filter = "";
	public String itemTitle;
	public BuildType type;
	public boolean group2 = false;
	public boolean text = false;
	public boolean proc = false;
	public boolean comparison = false;

	public void setText(boolean text) {
		this.text = text;
		if (text)
			type = BuildType.text;
	}

	public void setProc(boolean proc) {
		this.proc = proc;
		if (proc)
			type = BuildType.proc;
	}

	// public void setComparison(boolean comparison) {
	// this.comparison = comparison;
	// if (comparison)
	// type = BuildType.comparison;
	// }

	public boolean getProc = false;
	public ELevel level;
	public boolean textWithoutLines = false;
	public boolean comparisonWithEquals = false;
	public boolean rootComparison = false;

}
