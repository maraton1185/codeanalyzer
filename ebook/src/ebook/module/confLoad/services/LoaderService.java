package ebook.module.confLoad.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import ebook.core.pico;
import ebook.module.confLoad.LogFormatter;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.Entity;
import ebook.module.confLoad.model.procEntity;
import ebook.utils.Const;

public class LoaderService {

	// ITextParser parser = pico.get(ITextParser.class);

	public static final String LogFile = "log.txt";

	public ICfServices srv = pico.get(ICfServices.class);

	public void loadTxtModuleFile(Connection con, File f, boolean log)
			throws InvocationTargetException {

		Entity line = new Entity();

		Logger LOG = Logger.getLogger(LoaderService.class.getName());
		Handler handler = null;

		BufferedReader bufferedReader = null;
		try {

			if (log) {
				handler = new FileHandler(LogFile);
				handler.setFormatter(new LogFormatter());
				// handler.setLevel(Level.FINE);
				LOG.addHandler(handler);
				LOG.setLevel(Level.FINE);
			}

			LOG.info("parse file: " + f.toString());
			srv.parse().parseTxtModuleName(f, line);

			LOG.fine("add object");
			Integer module = srv.load().addEntity(con, line);

			LOG.fine("delete procs");
			srv.load().deleteProcs(con, module);

			Reader in = new InputStreamReader(new FileInputStream(f), "UTF-8");
			bufferedReader = new BufferedReader(in);

			ArrayList<String> buffer = new ArrayList<String>();
			ArrayList<String> vars = new ArrayList<String>();

			Boolean firstProcWasFound = false;
			String source_line = null;
			String currentSection = "";

			LOG.fine("read file ---------------------------------");

			while ((source_line = bufferedReader.readLine()) != null) {

				LOG.fine(source_line);

				buffer.add(source_line + "\n");

				if (srv.parse().findProcEnd(source_line)) {

					LOG.fine("find proc end: " + source_line);

					procEntity proc = new procEntity(line);
					srv.parse().getProcInfo(proc, buffer, vars, currentSection);
					if (!firstProcWasFound && !vars.isEmpty()) {
						procEntity var = new procEntity(line);
						var.proc_name = Const.STRING_VARS;
						var.proc_title = Const.STRING_VARS_TITLE;
						var.text = new StringBuilder();
						for (String string : vars) {
							var.text.append(string);
						}
						var.export = false;

						LOG.fine("add proc: " + var.proc_name);
						srv.load().addProcedure(con, var, module);

					}

					proc.text = new StringBuilder();
					for (String string : buffer) {
						proc.text.append(string);
					}

					// parser.findCalls(proc);

					LOG.fine("add proc: " + proc.proc_name);
					srv.load().addProcedure(con, proc, module);

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

				LOG.fine("add proc: " + proc.proc_name);
				srv.load().addProcedure(con, proc, module);
			}

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception: ", e);

			e.printStackTrace();
			throw new InvocationTargetException(null,
					Const.ERROR_CONFIG_READFILE + f.getName());
		} finally {
			try {
				bufferedReader.close();
				if (log)
					handler.close();
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Exception: ", e);
				throw new InvocationTargetException(null,
						Const.ERROR_CONFIG_READFILE + f.getName());
			}
		}

	}

	public void loadXmlModuleFile(Connection con, File f, boolean log) {
		// TODO Auto-generated method stub

	}

	// public void fillProcLinkTableDoWork(Connection con, IProgressMonitor
	// monitor)
	// throws Exception {
	//
	// monitor.beginTask(Const.MSG_CONFIG_FILL_LINK_TABLE, srv.get()
	// .getProcCount(con));
	//
	// List<procEntity> procs = srv.get().getProcs(con);
	//
	// ArrayList<String> buffer = new ArrayList<String>();
	//
	// for (procEntity proc : procs) {
	//
	// if (monitor.isCanceled()) {
	// throw new InterruptedException();
	// }
	//
	// monitor.subTask(proc.group1 + "." + proc.group2);
	//
	// String text = srv.get().getProcText(con, proc.id);
	// buffer = new ArrayList<String>(Arrays.asList(text.split("\n")));
	//
	// proc.calls = new ArrayList<procCall>();
	//
	// for (int line = 0; line < buffer.size(); line++) {
	//
	// String source_line = buffer.get(line);
	//
	// List<procCall> calls = srv.parse().findProcsInString(
	// source_line, proc.proc_name);
	//
	// for (procCall call : calls) {
	// proc.calls.add(call);
	// }
	//
	// }
	//
	// srv.load().addProcCalls(con, proc, proc.id);
	//
	// monitor.worked(1);
	// }
	//
	// }

	// public boolean linkTableFilled(Connection con) throws Exception {
	// return srv.load().linkTableFilled(con);
	// }
	//
	// public void clearLinkTable(Connection con) throws Exception {
	// srv.load().clearLinkTable(con);
	//
	// }
	//
	// public void clearTables(Connection con) throws Exception {
	// srv.load().clearTables(con);
	//
	// }
}
