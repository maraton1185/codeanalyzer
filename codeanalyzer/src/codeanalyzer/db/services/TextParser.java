package codeanalyzer.db.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codeanalyzer.core.exceptions.ProcNotFoundException;
import codeanalyzer.core.interfaces.ICf.EContext;
import codeanalyzer.core.interfaces.ICf.EType;
import codeanalyzer.core.interfaces.ITextParser;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class TextParser implements ITextParser {

	@Override
	public void parseTxtModuleName(File f, Entity line) throws Exception {
		String name = f.getName();
		String s[] = name.split("\\.");
		if (s.length < 3)
			throw new Exception();

		switch (s[0].toLowerCase()) {
		case "конфигурация":

			line.group1 = "Конфигурация";
			line.group2 = "МодулиКонфигурации";
			line.module = s[1];
			line.context = EContext.Config;

			break;

		default:

			line.group1 = s[0];
			line.group2 = s[1];

			switch (s[0].toLowerCase()) {
			case "общиймодуль":
				line.context = EContext.CommonModule;
				break;
			case "общаяформа":
				line.context = EContext.CommonForm;
				break;
			default:

				switch (s[2].toLowerCase()) {
				case "форма":
					line.context = EContext.Form;
					break;
				case "команда":
					line.context = EContext.Command;
					break;
				case "модульменеджера":
					line.context = EContext.ManagerModule;
					break;
				default:
					line.context = EContext.Module;
					break;
				}
				break;
			}

			if (s[2].equalsIgnoreCase("Форма")
					|| s[2].equalsIgnoreCase("Команда"))
				line.module = s.length > 3 ? s[3] : "";
			else
				line.module = s[2];

			break;
		}

		line.type = EType.Module;
	}

	@Override
	public boolean findProcEnd(String source_line) {

		String upper_line = source_line.toUpperCase();

		String pattern = "(КОНЕЦПРОЦЕДУРЫ|КОНЕЦФУНКЦИИ)";
		Pattern endProcPattern = Pattern.compile(pattern);

		String p;
		Pattern r;
		Matcher m;

		Matcher endProcMatcher = endProcPattern.matcher(upper_line);
		if (endProcMatcher.find()) {
			String endProcResult = endProcMatcher.group();

			// комментарий
			p = "/{2,}.*".concat(endProcResult);
			r = Pattern.compile(p);
			m = r.matcher(upper_line);
			if (m.find())
				return false;

			// строка
			p = "\".*".concat(endProcResult);
			r = Pattern.compile(p);
			m = r.matcher(upper_line);
			if (m.find())
				return false;

			return true;
		}

		return false;
	}

	@Override
	public int getProcInfo(procEntity proc, ArrayList<String> buffer,
			ArrayList<String> vars, String currentSection)
			throws ProcNotFoundException {

		boolean founded = false;

		vars.clear();
		String pattern = Const.PATTERN_PROCEDURE;

		Pattern procPattern = Pattern.compile(pattern);
		String param_list = "";
		String p;
		Pattern r;
		Matcher m;

		for (int line = 0; line < buffer.size(); line++) {

			String source_line = buffer.get(line);

			String upper_line = source_line.toUpperCase();

			Matcher procMatcher = procPattern.matcher(upper_line);
			if (!procMatcher.find()) {

				vars.add(source_line);

			} else {

				String procResult = procMatcher.group().replace("(", "");

				// комментарий
				p = "/{2,}.*".concat(procResult);
				r = Pattern.compile(p);
				m = r.matcher(upper_line);
				if (m.find()) {
					vars.add(source_line);
					continue;
				}

				founded = true;

				// имя
				proc.proc_name = procResult.replace("ФУНКЦИЯ", "")
						.replace("ПРОЦЕДУРА", "").trim();
				// представление
				int i = source_line.indexOf(" ");
				i = i < 0 ? source_line.indexOf("\t") : i;
				int j = source_line.indexOf("(");
				try {
					proc.proc_title = source_line.substring(i, j)
							.concat("(...)").trim();
				} catch (Exception e) {
					proc.proc_title = source_line;
				}

				// если в текущей строке нет ')', то читаем до него
				p = pattern.concat(".*\\)");
				r = Pattern.compile(p);
				m = r.matcher(upper_line);

				param_list = source_line;
				if (!m.find()) {

					for (int q = line + 1; q < buffer.size(); q++) {

						source_line = buffer.get(q);

						// комментарий
						p = ",.*/{2,}";
						r = Pattern.compile(p);
						m = r.matcher(source_line);
						if (m.find()) {
							int k = source_line.indexOf(m.group());
							source_line = source_line.substring(0, k).trim()
									.concat(",");
							// file_line = __m.group().replace("/", "").trim();
						} else
							source_line = source_line.trim();

						param_list = param_list.concat(source_line);

						upper_line = source_line.toUpperCase();
						p = ".*\\)";
						r = Pattern.compile(p);
						m = r.matcher(upper_line);
						if (m.find())
							break;
					}
				}

				proc.export = (upper_line.indexOf("ЭКСПОРТ") >= 0);

				i = param_list.indexOf("(");
				j = param_list.indexOf(")");
				if (i >= 0 && j >= 0)
					param_list = param_list.substring(i + 1, j);

				proc.params = param_list.split(",");

				break;

			}
		}

		if (!founded)
			throw new ProcNotFoundException();

		// уменьшаем буфер на раздел описания переменных
		for (int k = 0; k < vars.size(); k++) {
			buffer.remove(0);
		}

		int lineOffset = vars.size();

		// вычисляем секцию процедуры и отделяем комментарий к процедуре от vars
		StringBuilder section = new StringBuilder();
		Boolean sectionStarts = false;

		int _k = 0;
		for (int k = vars.size() - 1; k >= 0; k--) {
			String s = vars.get(k);

			if (isCommentOrDirective(s) && !sectionStarts) {
				buffer.add(0, s);
			} else if (isSection(s) && sectionStarts) {
				section.insert(0, s);
				buffer.add(0, s);
			} else if (s.trim().equalsIgnoreCase("") && !sectionStarts) {
				sectionStarts = true;
				buffer.add(0, s);
			} else
				break;

			_k++;
		}
		for (int k = 0; k < _k; k++) {
			vars.remove(vars.size() - 1);
		}

		String _section = section.toString();
		_section = _section.replace("//", "").replace("*", "").replace("-", "")
				.trim();

		proc.section = section.length() == 0 ? currentSection : _section;

		// proc.calls = new ArrayList<ProcCall>();
		// // в буфере собран текст процедуры - заполняем таблицу LINK
		// for (int line = 0; line < buffer.size(); line++) {
		//
		// String source_line = buffer.get(line);
		//
		// List<ProcCall> calls = findProcsInString(source_line,
		// proc.proc_name);
		//
		// for (ProcCall call : calls) {
		// proc.calls.add(call);
		// }
		// }

		return lineOffset;
	}

	// @Override
	// public void findCalls(procEntity proc) {
	//
	// proc.calls = new ArrayList<ProcCall>();
	//
	// List<ProcCall> calls = findProcsInString(proc.text.toString(),
	// proc.proc_name);
	//
	// for (ProcCall call : calls) {
	// proc.calls.add(call);
	// }
	//
	// }

	// public Boolean target(Matcher procInStringMatcher) {
	// return procInStringMatcher.find();
	// }

	// public Boolean target3(String procInStringResult, CharSequence
	// upper_line) {
	// String p;
	// Pattern r;
	// Matcher m;
	// // комментарии
	// p = "/{2,}.*".concat(procInStringResult);
	// r = Pattern.compile(p);
	// m = r.matcher(upper_line);
	// if (m.find())
	// return true;
	//
	// // строка
	// p = "\\|[^\"]*.*".concat(procInStringResult);
	// r = Pattern.compile(p);
	// m = r.matcher(upper_line);
	// if (m.find())
	// return true;
	//
	// // новый
	// p = "НОВЫЙ\\s+".concat(procInStringResult);
	// r = Pattern.compile(p);
	// m = r.matcher(upper_line);
	// if (m.find())
	// return true;
	//
	// return false;
	// }

	@Override
	public List<ProcCall> findProcsInString(String source_line,
			String exclude_name) {

		List<ProcCall> result = new ArrayList<ProcCall>();

		if (!source_line.contains("("))
			return result;

		String upper_line = source_line.toUpperCase();

		// return result;

		String pattern = Const.PATTERN_PROCEDURE_IN_STRING;
		Pattern procInStringPattern = Pattern.compile(pattern);

		String p;
		Pattern r;
		Matcher m;

		Matcher procInStringMatcher = procInStringPattern.matcher(upper_line);

		while (procInStringMatcher.find()) {
			String procInStringResult = procInStringMatcher.group().replace(
					"(", "");
			if (procInStringResult.equalsIgnoreCase(exclude_name))
				continue;
			// }

			// if (target3(procInStringResult, upper_line))
			// continue;
			// комментарий
			p = "/{2,}.*".concat(procInStringResult);
			r = Pattern.compile(p);
			m = r.matcher(upper_line);
			if (m.find())
				continue;

			// // строка
			// p = "\\|[^\"]*.*".concat(procInStringResult);
			// r = Pattern.compile(p);
			// m = r.matcher(upper_line);
			// if (m.find())
			// continue;

			// новый
			p = "НОВЫЙ\\s+".concat(procInStringResult);
			r = Pattern.compile(p);
			m = r.matcher(upper_line);
			if (m.find())
				continue;

			ProcCall call = new ProcCall();

			// ищем модуль
			if (procInStringResult.contains(".")) {

				p = "[^\\s\\(]+".concat(procInStringResult);
				r = Pattern.compile(p);
				m = r.matcher(upper_line);
				if (m.find()) {
					procInStringResult = m.group();
					String s[] = procInStringResult.split("\\.");
					call.name = s[s.length - 1];
					call.context = procInStringResult.replace(call.name, "");

				}
			} else {
				if (Strings.standart_call(procInStringResult.trim()))
					continue;

				call.name = procInStringResult.trim();
			}

			result.add(call);
			// System.out.println("Found value: " + v);
		}

		return result;
	}

	@Override
	public boolean isCommentOrDirective(String file_line) {

		if (file_line.isEmpty())
			return true;
		file_line = file_line.replace(Character.toChars(65279)[0], ' ');
		String _line = file_line.toUpperCase();

		String pattern = "^\\s*(/{2,}.*|#|&)";
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(_line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSection(String file_line) {

		if (file_line.isEmpty())
			return true;
		file_line = file_line.replace(Character.toChars(65279)[0], ' ');
		String _line = file_line.toUpperCase();

		String pattern = "^\\s*(/{2,}.*[-*/]{2,}.*|#|&)";
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(_line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	// ************************************************************************************

	// public boolean findProcStart(String file_line, String proc_name,
	// procEntity line, ProcStartReader procStart) {
	//
	// if (procStart != null)
	// procStart.temp = new StringBuilder();
	//
	// String _line = file_line.toUpperCase();
	//
	// String pattern;
	// if (proc_name == null)
	// pattern = Const.PATTERN_PROCEDURE;
	// else
	// pattern = "(ФУНКЦИЯ|ПРОЦЕДУРА)\\s+" + proc_name.toUpperCase()
	// + "\\(";
	//
	// Pattern r = Pattern.compile(pattern);
	//
	// String param_list = "";
	//
	// String _p, __p;
	// Pattern _r, __r;
	// Matcher _m, __m;
	//
	// Matcher m = r.matcher(_line);
	// if (m.find()) {
	//
	// String v = m.group().replace("(", "");
	//
	// // комментарий
	// _p = "/{2,}.*".concat(v);
	// _r = Pattern.compile(_p);
	// _m = _r.matcher(_line);
	// if (_m.find())
	// return false;
	//
	// // имя
	// line.proc_name = v.replace("ФУНКЦИЯ", "").replace("ПРОЦЕДУРА", "")
	// .trim();
	// // представление
	// int i = file_line.indexOf(" ");
	// i = i < 0 ? file_line.indexOf("\t") : i;
	// int j = file_line.indexOf("(");
	// try {
	// line.proc_title = file_line.substring(i, j).concat("(...)")
	// .trim();
	// } catch (Exception e) {
	// line.proc_title = file_line;
	// }
	// // DONE загрузка признака экспортной процедуры
	// // признак экспорта
	// line.export = (_line.indexOf("ЭКСПОРТ") >= 0);
	//
	// // если в текущей строке нет ')', то читаем до него
	// _p = pattern.concat(".*\\)");
	// _r = Pattern.compile(_p);
	// _m = _r.matcher(_line);
	//
	// // DONE обработка списка параметров
	//
	// param_list = file_line;
	// while (!_m.find()) {
	// if (procStart != null) {
	// try {
	// file_line = procStart.bufferedReader.readLine();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return false;
	// }
	// if (file_line == null)
	// return false;
	//
	// procStart.temp.append(file_line + "\n");
	// }
	// // комментарий
	// __p = ",.*/{2,}";
	// __r = Pattern.compile(__p);
	// __m = __r.matcher(file_line);
	// if (__m.find()) {
	// int k = file_line.indexOf(__m.group());
	// file_line = file_line.substring(0, k).trim().concat(",");
	// // file_line = __m.group().replace("/", "").trim();
	// } else
	// file_line = file_line.trim();
	//
	// param_list = param_list.concat(file_line);
	//
	// _line = file_line.toUpperCase();
	// _p = ".*\\)";
	// _r = Pattern.compile(_p);
	// _m = _r.matcher(_line);
	// }
	//
	// line.export = (_line.indexOf("ЭКСПОРТ") >= 0);
	//
	// i = param_list.indexOf("(");
	// j = param_list.indexOf(")");
	// if (i >= 0 && j >= 0)
	// param_list = param_list.substring(i + 1, j);
	//
	// line.params = param_list.split(",");
	//
	// return true;
	// }
	//
	// return false;
	//
	// }

	@Override
	public boolean findCallee(String _line, String _calleeName) {

		if (_line.isEmpty() || _calleeName.isEmpty())
			return false;

		if (isCommentOrDirective(_line))
			return false;
		// _line = _line.replace(Character.toChars(65279)[0],' ');
		String line = _line.toUpperCase();
		String calleeName = _calleeName.toUpperCase();

		String pattern = calleeName + "\\(";
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean findProcStart(String line, String name) {

		if (name == null)
			return false;

		String _line = line.toUpperCase();

		String pattern = "(ФУНКЦИЯ|ПРОЦЕДУРА)\\s+" + name.toUpperCase()
				+ "\\s*\\(";

		Pattern r = Pattern.compile(pattern);

		String _p;
		Pattern _r;
		Matcher _m;

		Matcher m = r.matcher(_line);
		if (m.find()) {
			String v = m.group();
			v = v.replace("(", "\\(");
			// комментарий
			_p = "/{2,}.*".concat(v);
			_r = Pattern.compile(_p);
			_m = _r.matcher(_line);
			if (_m.find())
				return false;

			return true;
		}

		return false;
	}

	@Override
	public String compare(String text, String text1) {
		// String[] t1 = text.split("\n");
		// String[] t2 = text1.split("\n");
		//
		// List<String> original = Arrays.asList(t1);
		// List<String> revised = Arrays.asList(t2);
		//
		// StringBuilder sb = new StringBuilder();
		//
		// DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
		// DiffRowGenerator dfg = builder.build();
		// List<DiffRow> rows = dfg.generateDiffRows(original, revised);
		// for (DiffRow diffRow : rows) {
		// DiffRow.Tag tag = diffRow.getTag();
		//
		// switch (tag) {
		// case CHANGE:
		//
		// sb.append(Const.COMPARE_CHANGED_MARKER + diffRow.getOldLine() +
		// "\n");
		//
		// break;
		// case DELETE:
		//
		// sb.append(Const.COMPARE_REMOVED_MARKER + diffRow.getOldLine() +
		// "\n");
		// break;
		//
		// case INSERT:
		//
		// sb.append(Const.COMPARE_ADDED_MARKER + diffRow.getNewLine() + "\n");
		// break;
		//
		// default:
		// sb.append(diffRow.getOldLine() + "\n");
		// break;
		// }
		//
		// }
		//
		// return sb.toString()
		// .replace("&lt;", "<")
		// .replace("&gt;", ">")
		// .replace("<br>", "");
		return "";

	}

	@Override
	public boolean findCompareMarker(String line) {
		String _line = line.toUpperCase();

		return _line.contains(Const.COMPARE_TEXT_MARKER)
				|| _line.contains(Const.COMPARE_CHANGED_MARKER)
				|| _line.contains(Const.COMPARE_REMOVED_MARKER)
				|| _line.contains(Const.COMPARE_ADDED_MARKER);

		// String pattern = ".*(" +
		// Const.COMPARE_TEXT_MARKER + "|" +
		// Const.COMPARE_CHANGED_MARKER + "|" +
		// Const.COMPARE_REMOVED_MARKER + "|" +
		// Const.COMPARE_ADDED_MARKER +
		// ").*";
		//
		// Pattern r = Pattern.compile(pattern);
		// Matcher m = r.matcher(_line);
		// return m.find();
	}

	@Override
	public boolean findTextInLine(String line, String text) {
		if (text.trim().isEmpty())
			return false;

		// String _line = line.toUpperCase();
		//
		// String pattern = ".*" + text.toUpperCase() +".*";
		//
		// Pattern r = Pattern.compile(pattern);
		// Matcher m = r.matcher(_line);
		// return m.find();
		return line.toUpperCase().contains(text.toUpperCase());
	}

	// *****************************************************

}
