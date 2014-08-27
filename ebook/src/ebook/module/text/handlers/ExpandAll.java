package ebook.module.text.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.text.views.TextView;

public class ExpandAll {
	@Execute
	public void execute(@Active MPart part) {
		if (part.getObject() instanceof TextView)
			((TextView) part.getObject()).ExpandAll();
	}

}