package ru.configviewer.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import ru.configviewer.core.IService;
import ru.configviewer.core.LineInfo;
import ru.configviewer.core.pico;
import ru.configviewer.utils.Const;

public class EditorStorage implements IStorage{

//	IDbManager dbManager = pico.get(IDbManager.class);
//	IEvents events = pico.get(IEvents.class);
	
	public EditorStorage(LineInfo data) {
		super();
		this.data = data;				
	}

	LineInfo data;
		
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public InputStream getContents() throws CoreException {
		
		try {

			
//			if (!events.activeConfigLoadedCheck()) return new ByteArrayInputStream(
//					"".getBytes(Const.DEFAULT_CHARACTER_ENCODING)); 
//			
//			IDb db = dbManager.getActive();
//			this.config_name = db.getName();
//			
			String text = pico.get(IService.class).getText(data.title);
//			if (data.onlyProc)
//			{
//				text = db.getProcText(data);
//				if(data.compare)
//				{
//					if (!events.nonActiveConfigLoadedCheck()) return new ByteArrayInputStream(
//							"".getBytes(Const.DEFAULT_CHARACTER_ENCODING)); 
//					IDb db1 = dbManager.getNonActive();
//					String text1 = db1.getProcText(data);
//					text = pico.get(ITextParser.class).compare(text, text1);
//				}
//			}else
//				text = db.getModuleText(data);

			return new ByteArrayInputStream(
					text.getBytes(Const.DEFAULT_CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new CoreException(null);
		}

	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

}
