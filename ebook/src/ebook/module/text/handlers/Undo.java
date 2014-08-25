package ebook.module.text.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.adapter.Adapter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.text.ITextOperationTarget;

public class Undo {
	@Inject
	private Adapter _adapter;

	@Execute
	public void execute(@Active MPart part) {
		final ITextOperationTarget opTarget = _adapter.adapt(part.getObject(),
				ITextOperationTarget.class);

		opTarget.doOperation(ITextOperationTarget.UNDO);
	}

	@CanExecute
	public boolean canExecute(@Active final MPart part) {
		final ITextOperationTarget opTarget = _adapter.adapt(part.getObject(),
				ITextOperationTarget.class);
		if (opTarget == null)
			return false;

		return opTarget.canDoOperation(ITextOperationTarget.UNDO);
	}

}