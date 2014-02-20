package ru.configviewer.editor;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import ru.configviewer.core.LineInfo;

public class EditorInput implements IStorageEditorInput{

	LineInfo data;
	
	public EditorInput(LineInfo data) {
		super();
		this.data = data;
	}

	public LineInfo getData() {
		return data;
	}
	
	public void setData(LineInfo data) {
		this.data = data;
	}
	
	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return data.title;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		
		return data.title;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
//		if (IResource.class.equals(adapter)) {
//			return ResourcesPlugin.getWorkspace().getRoot();
//		}
		return null;
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return new EditorStorage(data);
	}

}
