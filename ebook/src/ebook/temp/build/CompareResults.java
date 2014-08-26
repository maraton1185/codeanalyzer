package ebook.temp.build;

import java.util.ArrayList;
import java.util.List;

import ebook.module.text.model.LineInfo;

public class CompareResults {
	public List<LineInfo> equals = new ArrayList<LineInfo>();
	public List<LineInfo> added = new ArrayList<LineInfo>();
	public List<LineInfo> removed = new ArrayList<LineInfo>();
	public List<LineInfo> changed = new ArrayList<LineInfo>();
}
