package ru.configviewer.core;

import java.util.HashMap;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import ru.configviewer.editor.Editor;
import ru.configviewer.editor.EditorInput;

public class EditorFactory implements IEditorFactory {

	HashMap<String, EditorInput> inputs = new HashMap<String, EditorInput>();
	
	/* (non-Javadoc)
	 * @see ru.configviewer.editor.IEditorFactory#open(ru.configviewer.core.LineInfo)
	 */
	@Override
	public IEditorPart open(LineInfo data)
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		if (page == null)
			return null;
		IEditorPart editor = null;
		try {
			
			EditorInput input = getInput(data);
			page.openEditor(input, Editor.ID, true);		
			
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}

		return editor;
	}
	
	/* (non-Javadoc)
	 * @see ru.configviewer.editor.IEditorFactory#deleteInput(ru.configviewer.editor.EditorInput)
	 */
	@Override
	public void deleteInput(EditorInput input) {
		String key = input.getData().title;
		inputs.remove(key);
	}
	
	private EditorInput getInput(LineInfo data)
	{
		String key = data.title;
		
		EditorInput input = inputs.get(key);
		if (input == null) {
			input = new EditorInput(data);
			inputs.put(key, input);
		} else
			input.setData(data);
		return input;
		
	}






	


	
}
