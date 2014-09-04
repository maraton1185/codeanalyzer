package ebook.module.tree.service;

import java.util.ArrayList;
import java.util.List;

import ebook.module.tree.item.ITreeItemInfo;

public class ArrayTreeService implements ITreeService2 {

	private List<ITreeItemInfo> model = new ArrayList<ITreeItemInfo>();
	private ITreeItemInfo selected;

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

	public void setModel(List<ITreeItemInfo> model) {
		this.model = model;

	}

	public void setSelection(ITreeItemInfo selected) {
		this.selected = selected;

	}

	@Override
	public ITreeItemInfo get(int item) {
		return null;
	}

	@Override
	public void saveTitle(ITreeItemInfo object) {
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {
		return null;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {
		return null;
	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		return null;
	}

}
