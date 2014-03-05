package codeanalyzer.core.interfaces;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import codeanalyzer.core.exceptions.ProcNotFoundException;
import codeanalyzer.core.interfaces.ICf.EContext;
import codeanalyzer.core.interfaces.ICf.EType;

public interface ITextParser {

	// public class ProcStartReader {
	// public ProcStartReader(BufferedReader bufferedReader) {
	// this.bufferedReader = bufferedReader;
	// }
	//
	// public BufferedReader bufferedReader;
	// public StringBuilder temp;
	// }

	public class ProcCall {
		public String context = "";
		public String name = "";
	}

	public class Entity {
		public String group1;
		public String group2;
		public String module;
		public EType type;
		public EContext context;
	}

	public class procEntity extends Entity {
		public String proc_name;
		public String proc_title;
		public String section;
		public StringBuilder text;
		public Boolean export;
		public String[] params;
		public List<ProcCall> calls;

		public procEntity(Entity line) {

			for (Field f : this.getClass().getFields()) {
				try {
					if (f.getType().isAssignableFrom(String.class))
						f.set(this, "");
					if (f.getType().isAssignableFrom(Boolean.class))
						f.set(this, false);
				} catch (Exception e) {
				}
			}

			for (Field f : line.getClass().getFields()) {
				try {
					f.set(this, f.get(line));

				} catch (Exception e) {
				}
			}
		}
	}

	// public class metaEntity extends Entity {
	// public String property;
	// public String value;
	// }

	public abstract void parseTxtModuleName(File f, Entity line)
			throws Exception;

	public abstract boolean isCommentOrDirective(String file_line);

	public abstract List<ProcCall> findProcsInString(String _line,
			String exclude_name);

	public abstract boolean findCallee(String line, String calleeName);

	public abstract boolean findProcEnd(String file_line);

	public abstract boolean findProcStart(String line, String name);

	public abstract String compare(String text, String text1);

	public abstract boolean findCompareMarker(String line);

	boolean findTextInLine(String line, String text);

	// int getProcInfo(procEntity proc, ArrayList<String> buffer,
	// ArrayList<String> vars) throws ProcNotFoundException;

	int getProcInfo(procEntity proc, ArrayList<String> buffer,
			ArrayList<String> vars, String currentSection)
			throws ProcNotFoundException;

	boolean isSection(String file_line);

	// public abstract void findCalls(procEntity proc);

}