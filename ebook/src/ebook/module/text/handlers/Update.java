package ebook.module.text.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.text.views.TextView;
import ebook.utils.Events;

public class Update {
	@Execute
	public void execute(
			@Active MPart part,
			@Active @Optional @Named(Events.TEXT_VIEW_ACTIVE_PROCEDURE) LineInfo item,
			@Active TextConnection con) {
		if (part.getObject() instanceof TextView) {
			if (item != null)
				con.setLine(item);

			((TextView) part.getObject()).updateText();
		}
	}

}