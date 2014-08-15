package ebook.module.conf.model;

import java.util.ArrayList;
import java.util.List;

public class BuildInfo {

	public List<BuildInfo> children = new ArrayList<BuildInfo>();

	public boolean group = true;
	public String title;
	public BuildType type;
	public Integer parent;
	public Integer id;

	// @Override
	// public boolean equals(Object obj) {
	// if (obj instanceof BuildInfo)
	// return ((BuildInfo) obj).id.equals(id);
	// else
	// return super.equals(obj);
	// }
}
