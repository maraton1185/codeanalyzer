 
package codesnippetapp.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import codesnippetapp.CodeSnippetAppConstants;
import codesnippetapp.data.SnippetData;
import codesnippetapp.data.SnippetRepository;

public class SnippetListView {
	
	TableViewer snippetsList;
	
	private static String SNIPPED_AT_MOUSE_CLICK = "snippet_at_mouse_click";
	private static int newSnippetCounter = 1;
//	private static Object selectedSnipped;
	
	@Inject
	public SnippetListView() {
		
	}
	
	@Inject
	IEventBroker broker;
	
	@Inject	@Optional
	public void onAddNewSnippet(@UIEventTopic(CodeSnippetAppConstants.NEW_SNIPPET_EVENT)Object data, SnippetRepository repo, Shell shell)
	{

		MessageDialog.openInformation(shell, "open", "hotfix");

//		SnippetData newSnippet = new SnippetData("Untitled" + (newSnippetCounter++));
//		repo.snippets.add(newSnippet);
//		snippetsList.refresh();
//		snippetsList.setSelection(new StructuredSelection(newSnippet));	
//		broker.send(CodeSnippetAppConstants.SELECT_SNIPPET_EVENT, newSnippet);
	}

	@Inject	@Optional
	public void onDeleteSnippet(@UIEventTopic(CodeSnippetAppConstants.DELETE_SNIPPET_EVENT)Object data, SnippetRepository repo)
	{
		for (TableItem item : snippetsList.getTable().getSelection()) {
			
			repo.snippets.remove(item.getData());
			
		}; 
		snippetsList.refresh();		
	}
	
	@Inject	@Optional
	public void onSaveSnippet(@UIEventTopic(CodeSnippetAppConstants.SAVE_SNIPPET_EVENT)Object data)
	{
		snippetsList.refresh();		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent, final IEclipseContext ctx, EMenuService menuService) {
		snippetsList = new TableViewer(parent);
		
		SnippetRepository repo = ctx.get(SnippetRepository.class);
		
		snippetsList.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if(inputElement instanceof SnippetRepository)
					return ((SnippetRepository)inputElement).snippets.toArray();			
				return new Object[]{};
			}
		});
		
		snippetsList.setInput(repo);
		
		menuService.registerContextMenu(snippetsList.getTable(), "codesnippetapp.snippetlist.popupmenu");
		
		snippetsList.getTable().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button ==1)
					return;
				TableItem itemAtClick = snippetsList.getTable().getItem(new Point(e.x, e.y));
				if(itemAtClick!=null){
					ctx.set(SNIPPED_AT_MOUSE_CLICK, itemAtClick.getData());
					//selectedSnipped = itemAtClick.getData();
				}else{
					ctx.remove(SNIPPED_AT_MOUSE_CLICK);
					//selectedSnipped = null;
				}
				super.mouseDown(e);
			}
			
		});
		
		snippetsList.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				SnippetData snippetData = (SnippetData)selection.getFirstElement();
				if (snippetData != null)
					broker.post(CodeSnippetAppConstants.SELECT_SNIPPET_EVENT,
							snippetData);
				
			}
		});
//		snippetsList.getTable().addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//				broker.send(CodeSnippetAppConstants.SELECT_SNIPPET_EVENT, e.item.getData());
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				
//				
//			}
//		});
	}
	
	
	
	
}
