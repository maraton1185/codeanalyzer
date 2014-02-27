package codeanalyzer.core.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.core.exceptions.ProcNotFoundException;

public interface ITextParser {

	public class ProcStartReader{
		public ProcStartReader(BufferedReader bufferedReader) {
			this.bufferedReader = bufferedReader;
		}
		public BufferedReader bufferedReader;
		public StringBuilder temp;
	} 
	
	public abstract class Entity {
		public String group1;
		public String group2;
		public String module;
	}

	public class procEntity extends Entity {
		public String proc_name;
		public String proc_title;
		public StringBuilder text;
		public Boolean export;
		public String[] params;
	}

	public class metaEntity extends Entity {
		public String property;
		public String value;
	}
	
	public abstract void parseObject(File f, Entity line) throws Exception;

	public abstract boolean isCommentOrDirective(String file_line);

	public abstract List<String> findProcsInString(String _line, String exclude_name);

	public abstract boolean findCallee(String line, String calleeName);

	public abstract boolean findProcEnd(String file_line);
	
	public abstract boolean findProcStart(String line, String name);

	public abstract String compare(String text, String text1);

	public abstract boolean findCompareMarker(String line);

	boolean findTextInLine(String line, String text);

	int getProcInfo(procEntity proc, ArrayList<String> buffer,
			ArrayList<String> vars) throws ProcNotFoundException;
	

}