package ebook.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Context implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";

	public static native void init()/*-{
									$doc.init();
									}-*/;

	public static native void setContent(String value)/*-{
														$doc.setContent(value);
														}-*/;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final static TreeServiceAsync treeService = GWT
			.create(TreeService.class);

	private SingleSelectionModel<ContextTreeItem> selectionModel = new SingleSelectionModel<ContextTreeItem>();

	/**
	 * The model that defines the nodes in the tree.
	 */
	private class ContextTreeModel implements TreeViewModel {

		protected AsyncDataProvider<ContextTreeItem> getDataProvider(
				final ContextTreeItem node) {
			return new AsyncDataProvider<ContextTreeItem>() {
				@Override
				protected void onRangeChanged(
						final HasData<ContextTreeItem> display) {
					treeService.getChild(node,
							new AsyncCallback<List<ContextTreeItem>>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert(caught.toString());
								}

								@Override
								public void onSuccess(
										List<ContextTreeItem> result) {

									final int start = display.getVisibleRange()
											.getStart();
									updateRowData(display, start, result);

								}
							});

				}
			};
		}

		@Override
		public <T> NodeInfo<?> getNodeInfo(T value) {

			return new DefaultNodeInfo<ContextTreeItem>(
					getDataProvider((ContextTreeItem) value),
					new ContextTreeCell(), selectionModel, null);
		}

		@Override
		public boolean isLeaf(Object value) {
			if (value instanceof ContextTreeItem)
				return ((ContextTreeItem) value).isLeaf();
			return false;
		}

	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		SplitLayoutPanel p = new SplitLayoutPanel();
		p.setSize("100%", "100%");

		// Create a model for the tree.
		TreeViewModel model = new ContextTreeModel();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {

						@SuppressWarnings("unchecked")
						ContextTreeItem item = ((SingleSelectionModel<ContextTreeItem>) event
								.getSource()).getSelectedObject();
						if (item == null)
							return;

						treeService.getText(item, new AsyncCallback<String>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert(caught.toString());
							}

							@Override
							public void onSuccess(String result) {
								setContent(result);
							}
						});

					}
				});

		/*
		 * Create the tree using the model. We specify the default value of the
		 * hidden root node as "Item 1".
		 */
		ContextTreeItem item = new ContextTreeItem();
		CellTree tree = new CellTree(model, item);

		p.addWest(tree, 300);
		p.add(new HTML(
				"<textarea id=\"content\" name=\"content\" style=\"width:100%; height:100% !important\"></textarea>"));

		// Add it to the root panel.
		RootPanel.get("root").add(p);

		init();

	}
}
