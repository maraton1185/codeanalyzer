package ebook.module.conf.model;

public class BuildInfo implements Comparable<BuildInfo> {

	public SortedArrayList<BuildInfo> children = new SortedArrayList<BuildInfo>();

	public boolean group = true;
	public String title;
	public BuildType type;
	public Integer parent;
	public Integer id;
	public Integer sort = 0;

	@Override
	public int compareTo(BuildInfo o) {
		if (sort - o.sort != 0)
			return sort - o.sort;
		return title.compareTo(o.title);
	}

}
