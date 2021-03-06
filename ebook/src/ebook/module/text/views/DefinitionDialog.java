package ebook.module.text.views;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.module.tree.service.ArrayTreeService;
import ebook.module.tree.view.TreeViewComponent;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class DefinitionDialog extends Dialog {

	private TreeViewer viewer;
	private TreeViewComponent treeComponent;
	private ArrayTreeService service = new ArrayTreeService();

	ITreeItemInfo selected;
	private String definition;

	// boolean doSelection = false;

	public DefinitionDialog(Shell parentShell, String definition) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
		this.definition = definition;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Strings.title("DefinitionDialogTitle"));

		shell.setImage(Utils.getImage("source.png"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "�������", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "�������", false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 400);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite body = (Composite) super.createDialogArea(parent);

		body.setFont(new Font(Display.getCurrent(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		body.setLayout(new GridLayout(1, false));
		Composite cont;

		cont = new Composite(body, SWT.NONE);
		cont.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(cont);

		Label l = new Label(cont, SWT.NONE);
		l.setImage(Utils.getImage("search.png"));
		l = new Label(cont, SWT.NONE);
		l.setText(definition);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(l);

		cont = new Composite(body, SWT.NONE);
		cont.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(cont);

		treeComponent = new TreeViewComponent(cont, service, 3, false, false);

		viewer = treeComponent.getViewer();

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// if (!doSelection)
				// return;
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				selected = (ITreeItemInfo) selection.getFirstElement();
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {

				okPressed();
			}
		});

		treeComponent.setSelection();

		return body;
	}

	@Override
	protected void okPressed() {

		super.okPressed();

	}

	public void setData(List<ITreeItemInfo> defs) {
		service.setModel(defs);
		// doSelection = false;
		// treeComponent.updateInput();
		// treeComponent.setSelection();
		// doSelection = true;
	}

	public ContextInfo getItem() {
		return (ContextInfo) selected;
	}

}
