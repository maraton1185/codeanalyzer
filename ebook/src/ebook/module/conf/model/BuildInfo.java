package ebook.module.conf.model;

public class BuildInfo implements Comparable<BuildInfo> {

	public SortedArrayList<BuildInfo> children = new SortedArrayList<BuildInfo>();

	public boolean group = true;
	public String title = "";
	public BuildType type;
	public Integer parent = 0;
	public Integer id;
	public Integer sort = 0;

	public String proc;
	public Integer start_offset;
	public boolean openInComparison = false;

	@Override
	public int compareTo(BuildInfo o) {
		if (sort - o.sort != 0)
			return sort - o.sort;
		return title.compareTo(o.title);
	}

}
