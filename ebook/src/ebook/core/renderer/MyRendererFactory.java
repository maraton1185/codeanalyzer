package ebook.core.renderer;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class MyRendererFactory extends WorkbenchRendererFactory {

	private MyMenuRenderer menuRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
		if (uiElement instanceof MPopupMenu) {
			if (menuRenderer == null) {
				menuRenderer = new MyMenuRenderer();
				super.initRenderer(menuRenderer);
			}
			return menuRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}
}