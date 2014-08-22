package ebook.module.text.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.core.pico;
import ebook.module.conf.ConfService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.text.EditorConfiguration;
import ebook.module.text.TextConnection;
import ebook.module.text.model.DocumentPartitionScanner;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public class TextView {

	ICfServices cf = pico.get(ICfServices.class);
	ITreeItemInfo item;
	ITreeService srv;

	@Inject
	@Active
	TextConnection con;

	@PostConstruct
	public void postConstruct(Composite parent) {
		item = con.getItem();
		srv = con.getSrv();

		ProjectionViewer viewer = new ProjectionViewer(parent, null, null,
				true, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		EditorConfiguration viewerConfiguration = new EditorConfiguration();

		viewer.configure(viewerConfiguration);

		Document document = new Document();

		IDocumentPartitioner partitioner = new FastPartitioner(
				new DocumentPartitionScanner(), new String[] {
						DocumentPartitionScanner.STRING,
						DocumentPartitionScanner.COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		document.set(getItemText());
		viewer.setDocument(document);

	}

	private String getItemText() {

		// String text = "";
		Integer id = null;
		if (con.isConf()) {

			try {

				List<String> path = new ArrayList<String>();
				id = cf.build(srv.getConnection()).getProcId((ConfService) srv,
						(ContextInfo) item, path);

			} catch (Exception e) {
				return e.getMessage();
			}
		} else {
			id = item.getId();
		}

		return id == null ? "" : srv.getText(id);
	}
}
