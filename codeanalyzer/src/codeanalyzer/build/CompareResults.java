package codeanalyzer.build;

import java.util.ArrayList;
import java.util.List;

public class CompareResults {
	public List<BuildInfo> equals = new ArrayList<BuildInfo>();
	public List<BuildInfo> added = new ArrayList<BuildInfo>();
	public List<BuildInfo> removed = new ArrayList<BuildInfo>();
	public List<BuildInfo> changed = new ArrayList<BuildInfo>();
}
