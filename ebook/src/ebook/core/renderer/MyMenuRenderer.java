package ebook.core.renderer;

import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;

public class MyMenuRenderer extends MenuManagerRenderer {

	@Override
	public void cleanUp(MMenu menuModel) {

		if (menuModel instanceof MPopupMenu)
			unlinkMenu(menuModel);

		super.cleanUp(menuModel);
	}

	private void unlinkMenu(MMenu menu) {

		List<MMenuElement> children = menu.getChildren();
		for (MMenuElement child : children) {
			if (child instanceof MMenu)
				unlinkMenu((MMenu) child);
			else {
				IContributionItem contribution = getContribution(child);
				clearModelToContribution(child, contribution);
			}
		}
		MenuManager mm = getManager(menu);
		clearModelToManager(menu, mm);
	}
}
