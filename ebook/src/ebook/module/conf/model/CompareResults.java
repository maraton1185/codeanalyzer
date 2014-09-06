package ebook.module.conf.model;

import java.util.ArrayList;
import java.util.List;

import ebook.module.tree.item.ITreeItemInfo;

public class CompareResults {
	public List<ITreeItemInfo> equals = new ArrayList<ITreeItemInfo>();
	public List<ITreeItemInfo> added = new ArrayList<ITreeItemInfo>();
	public List<ITreeItemInfo> removed = new ArrayList<ITreeItemInfo>();
	public List<ITreeItemInfo> changed = new ArrayList<ITreeItemInfo>();
}
