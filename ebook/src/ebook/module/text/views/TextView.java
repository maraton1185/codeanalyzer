package ebook.module.text.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import ebook.core.pico;
import ebook.module.conf.ConfService;
import ebook.module.conf.model.BuildInfo;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.ELevel;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.text.EditorConfiguration;
import ebook.module.text.TextConnection;
import ebook.module.text.scanner.DocumentPartitionScanner;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;
import ebook.utils.Events;
import ebook.utils.Strings;

public class TextView {

	ICfServices cf = pico.get(ICfServices.class);
	ITreeItemInfo item;
	ITreeService srv;

	@Inject
	@Active
	TextConnection con;

	ProjectionViewer viewer;
	Document document;
	EditorConfiguration viewerConfiguration;

	@Inject
	@Optional
	public void EVENT_TEXT_VIEW_DOUBLE_CLICK(
			@UIEventTopic(Events.EVENT_TEXT_VIEW_DOUBLE_CLICK) Object o) {

		ITextSelection textSelection = (ITextSelection) viewer
				.getSelectionProvider().getSelection();
		String _line;
		try {
			_line = document.get(textSelection.getOffset(),
					textSelection.getLength());
			viewerConfiguration.lightWord(_line);
		} catch (BadLocationException e) {

			e.printStackTrace();
		}

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		item = con.getItem();
		srv = con.getSrv();

		viewer = new ProjectionViewer(parent, null, null, true, SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		viewerConfiguration = new EditorConfiguration(viewer);

		viewer.configure(viewerConfiguration);

		document = new Document();

		IDocumentPartitioner partitioner = new FastPartitioner(
				new DocumentPartitionScanner(), new String[] {
						DocumentPartitionScanner.STRING,
						DocumentPartitionScanner.COMMENT });
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		String text = getItemText();
		if (text == null)
			text = Strings.msg("TextView.errorGetText");
		document.set(text);
		viewer.setDocument(document);

	}

	private String getItemText() {

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		if (opt.type == BuildType.module)
			if (con.isConf())
				return getConfModuleText();
			else
				return getBookModuleText();
		else

		if (con.isConf())
			return getConfText();
		else
			return srv.getText(item.getId());

	}

	private String getBookModuleText() {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	private String getConfModuleText() {
		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.module, path);

			if (id == null)
				return null;

			List<BuildInfo> proposals = new ArrayList<BuildInfo>();
			build.getProcs(null, id, proposals);

			if (proposals.isEmpty())
				return null;

			StringBuilder result = new StringBuilder();

			for (BuildInfo buildInfo : proposals) {

				String text = srv.getText(buildInfo.id);

				result.append(text);
			}

			return result.toString();

		} catch (Exception e) {
			return e.getMessage();
		}
		// return null;
	}

	private String getConfText() {

		Integer id = null;
		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.proc, path);

		} catch (Exception e) {
			return e.getMessage();
		}
		return id == null ? null : srv.getText(id);
	}

}
