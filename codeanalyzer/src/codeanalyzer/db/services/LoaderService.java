package codeanalyzer.db.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.ITextParser;
import codeanalyzer.core.interfaces.ITextParser.procEntity;
import codeanalyzer.utils.Const;

public class LoaderService {

	ITextParser parser = pico.get(ITextParser.class);
	
	DbService service = new DbService();
	
	public void loadTxtModuleFile(Connection con, File f) throws InvocationTargetException {
		
		procEntity proc = new procEntity();
		
		BufferedReader bufferedReader = null;
		try {

			parser.parseObject(f, proc);

			Integer object = service.addObject(con, proc);
			Integer module = service.addModule(con, proc, object);

			service.deleteProcs(con, object, module);

			Reader in = new InputStreamReader(new FileInputStream(f), "UTF-8");
			bufferedReader = new BufferedReader(in);

			ArrayList<String> buffer = new ArrayList<String>();
			ArrayList<String> vars =new ArrayList<String>();
			
			Boolean procWasFound = false;
			String file_line = null;


			while ((file_line = bufferedReader.readLine()) != null) {
				
				buffer.add(file_line + "\n");
				
				if (parser.findProcEnd(file_line)) {
				
					parser.getProcInfo(proc, buffer, vars);
					if(!procWasFound && !vars.isEmpty())
					{
						procEntity var = new procEntity();
						var.proc_name = Const.STRING_VARS;
						var.proc_title = Const.STRING_VARS_TITLE;
						var.text = new StringBuilder();
						for (String string : vars) {
							var.text.append(string);
						}
						var.export = false;
						service.addProcedure(con, var, object, module);
						
						
					}
					
					proc.text = new StringBuilder();
					for (String string : buffer) {
						proc.text.append(string);
					}
					
					service.addProcedure(con, proc, object, module);
					
					buffer.clear();
					procWasFound = true;
				}
								
			}
			
			if (!buffer.isEmpty()) {
				proc.proc_name = Const.STRING_INIT;
				proc.proc_title = Const.STRING_INIT_TITLE;
				proc.text = new StringBuilder();
				for (String string : buffer) {
					proc.text.append(string);
				}
				proc.export = false;
				service.addProcedure(con, proc, object, module);
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

}
