package ebook.web.gwt.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContextTreeItem implements IsSerializable {

	public ContextTreeItem() {
	}

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String title = "title";
	private boolean leaf = false;

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public boolean isLeaf() {
		return leaf;
	}

}
