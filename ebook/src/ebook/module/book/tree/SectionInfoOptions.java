package ebook.module.book.tree;

import ebook.core.models.DbOptions;

public class SectionInfoOptions extends DbOptions {

	private static final long serialVersionUID = -8134048308726133820L;

	public static final int gridScaleMax = 11;
	public static final int gridScaleMin = 0;
	public static final int gridLength = 12;

	public static final int scaleIncrement = 1;

	private Integer bigImageCSS = 6;

	public int selectedContext;

	public Integer getBigImageCSS() {
		return bigImageCSS == null ? 0 : bigImageCSS;
	}

	public void setBigImageCSS(Integer bigImageCSS) {
		this.bigImageCSS = bigImageCSS;
	}

}
