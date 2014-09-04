package ebook.module.tree.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.adapter.Adapter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import ebook.module.tree.view.ICollapseView;

public class CollapseAll {
	@Inject
	private Adapter _adapter;

	@Execute
	public void execute(@Active MPart part) {
		final ICollapseView opTarget = _adapter.adapt(part.getObject(),
				ICollapseView.class);

		opTarget.CollapseAll();
	}

	@CanExecute
	public boolean canExecute(@Active final MPart part) {
		final ICollapseView opTarget = _adapter.adapt(part.getObject(),
				ICollapseView.class);
		if (opTarget == null)
			return false;

		return true;
	}

}