package ebook.module.conf.model;

import java.util.ArrayList;
import java.util.List;

public class BuildInfo {

	public List<BuildInfo> children = new ArrayList<BuildInfo>();

	public boolean group = true;
	public String title;

}
