package ebook.module.confLoad.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ebook.core.pico;
import ebook.core.interfaces.ITextParser;
import ebook.core.interfaces.ITextParser.Entity;
import ebook.core.interfaces.ITextParser.ProcCall;
import ebook.core.interfaces.ITextParser.procEntity;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.utils.Const;

public class LoaderService {

	ITextParser parser = pico.get(ITextParser.class);

	ICfServices service = pico.get(ICfServices.class);

	public void loadTxtModuleFile(Connection con, File f)
			throws InvocationTargetException {

		Entity line = new Entity();

		BufferedReader bufferedReader = null;
		try {

			parser.parseTxtModuleName(f, line);

			Integer object = service.load().addObject(con, line);

			service.load().deleteProcs(con, object);

			Reader in = new InputStreamReader(new FileInputStream(f), "UTF-8");
			bufferedReader = new BufferedReader(in);

			ArrayList<String> buffer = new ArrayList<String>();
			ArrayList<String> vars = new ArrayList<String>();

			Boolean firstProcWasFound = false;
			String source_line = null;
			String currentSection = "";

			while ((source_line = bufferedReader.readLine()) != null) {

				buffer.add(source_line + "\n");

				if (parser.findProcEnd(source_line)) {

					procEntity proc = new procEntity(line);
					parser.getProcInfo(proc, buffer, vars, currentSection);
					if (!firstProcWasFound && !vars.isEmpty()) {
						procEntity var = new procEntity(line);
						var.proc_name = Const.STRING_VARS;
						var.proc_title = Const.STRING_VARS_TITLE;
						var.text = new StringBuilder();
						for (String string : vars) {
							var.text.append(string);
						}
						var.export = false;
						service.load().addProcedure(con, var, object);

					}

					proc.text = new StringBuilder();
					for (String string : buffer) {
						proc.text.append(string);
					}

					// parser.findCalls(proc);

					service.load().addProcedure(con, proc, object);

					currentSection = proc.section;

					buffer.clear();
					firstProcWasFound = true;
				}

			}

			if (!buffer.isEmpty()) {
				procEntity proc = new procEntity(line);
				proc.proc_name = Const.STRING_INIT;
				proc.proc_title = Const.STRING_INIT_TITLE;
				proc.text = new StringBuilder();
				for (String string : buffer) {
					proc.text.append(string);
				}
				proc.export = false;
				service.load().addProcedure(con, proc, object);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new InvocationTargetException(null,
					Const.ERROR_CONFIG_READFILE + f.getName());
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				throw new InvocationTargetException(null,
						Const.ERROR_CONFIG_READFILE + f.getName());
			}
		}

	}

	public void loadXmlModuleFile(Connection con, File f) {
		// TODO Auto-generated method stub

	}

	public void fillProcLinkTableDoWork(Connection con, IProgressMonitor monitor)
			throws Exception {

		monitor.beginTask(Const.MSG_CONFIG_FILL_LINK_TABLE, service
				.get().getProcCount(con));

		List<procEntity> procs = service.get().getProcs(con);

		ArrayList<String> buffer = new ArrayList<String>();

		for (procEntity proc : procs) {

			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

			monitor.subTask(proc.group1 + "." + proc.group2);

			String text = service.get().getProcText(con, proc.id);
			buffer = new ArrayList<String>(Arrays.asList(text.split("\n")));

			proc.calls = new ArrayList<ProcCall>();

			for (int line = 0; line < buffer.size(); line++) {

				String source_line = buffer.get(line);

				List<ProcCall> calls = parser.findProcsInString(source_line,
						proc.proc_name);

				for (ProcCall call : calls) {
					proc.calls.add(call);
				}

			}

			service.load().addProcCalls(con, proc, proc.id);

			monitor.worked(1);
		}

	}

	public boolean linkTableFilled(Connection con) throws Exception {
		return service.load().linkTableFilled(con);
	}

	public void clearLinkTable(Connection con) throws Exception {
		service.load().clearLinkTable(con);

	}
}
