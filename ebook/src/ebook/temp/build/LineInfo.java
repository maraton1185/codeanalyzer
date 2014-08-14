package ebook.temp.build;

import java.lang.reflect.Field;

public class LineInfo{

	public LineInfo() {
		super();
	}
	
	public LineInfo(LineInfo info) {
		super();
		
		for (Field f : this.getClass().getDeclaredFields()) {				
			try {
				if (f.getType().isAssignableFrom(_BuildInfo.class))
					f.set(this, new _BuildInfo((_BuildInfo)f.get(info)));
				else
					f.set(this, f.get(info));
			} catch (Exception e) {
			}
		}
	}
	
	public int line;
	public int offset;
	public String title;
	public String name;
	public Boolean export;
	
	public _BuildInfo data;
	
	public LineInfo parent;
	
}