package ru.configviewer.core;

import org.eclipse.ui.IEditorPart;

import ru.configviewer.editor.EditorInput;

public interface IEditorFactory {

	public abstract IEditorPart open(LineInfo data);

	public abstract void deleteInput(EditorInput input);

}