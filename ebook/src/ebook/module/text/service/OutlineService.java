package ebook.module.text.service;

import java.util.ArrayList;
import java.util.List;

import ebook.module.text.model.LineInfo;
import ebook.module.tree.AbstractTreeService;
import ebook.module.tree.ITreeItemInfo;

public class OutlineService extends AbstractTreeService {

	private ArrayList<LineInfo> model = new ArrayList<LineInfo>();
	private LineInfo selected;

	@Override
	public List<ITreeItemInfo> getRoot() {

		return new ArrayList<ITreeItemInfo>(model);
	}

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {
		return new ArrayList<ITreeItemInfo>();
	}

	@Override
	public boolean hasChildren(int parent) {

		return false;
	}

	@Override
	public ITreeItemInfo getSelected() {

		return selected;
	}

	public void setModel(ArrayList<LineInfo> model) {
		this.model = model;

	}

	public void setSelection(LineInfo selected) {
		this.selected = selected;

	}

}
