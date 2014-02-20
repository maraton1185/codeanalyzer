package ru.configviewer;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ru.configviewer.views.MainView;

public class Perspective implements IPerspectiveFactory {

	
	public static final String ID = "ru.configviewer.perspective";

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
//		layout.setEditorAreaVisible(false);
		layout.addView(MainView.ID, IPageLayout.LEFT,
				0.3f, layout.getEditorArea());
//		String editorArea = layout.getEditorArea();
		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
	}

	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
//		layout.addFastView("ru.configviewer.views.main");
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
//		layout.addShowViewShortcut("ru.configviewer.views.main");
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}

}
