package codeanalyzer.views;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import codeanalyzer.utils.Strings;

public class TreeView {

	TableViewer objectsTree;

	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {

		objectsTree = new TableViewer(parent);
		menuService.registerContextMenu(objectsTree.getTable(),
				Strings.get("objectsTree_popupmenu"));

	}

}
