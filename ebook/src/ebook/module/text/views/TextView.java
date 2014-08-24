package ebook.module.text.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextEvent;
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
import ebook.module.text.AnnotationSupport;
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
	MWindow window;

	@Inject
	@Active
	TextConnection con;

	ProjectionViewer viewer;
	Document document;
	EditorConfiguration viewerConfiguration;

	// private IVerticalRuler fVerticalRuler;
	// protected static final int VERTICAL_RULER_WIDTH = 12;

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

	@Inject
	MDirtyable dirty;
	private ITreeItemInfo parentItem;

	@Persist
	public void save() {
		saveItemText();
		dirty.setDirty(false);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		item = con.getItem();
		srv = con.getSrv();

		parentItem = srv.get(item.getParent());

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		int style = opt.type == BuildType.module ? SWT.READ_ONLY : SWT.NONE;

		AnnotationSupport annSupport = new AnnotationSupport();

		// fVerticalRuler = createVerticalRuler();

		viewer = new ProjectionViewer(parent, annSupport.getCompositeRuler(),
				annSupport.getOverviewRuler(), true, SWT.MULTI | SWT.V_SCROLL
						| SWT.H_SCROLL | style);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		annSupport.init(viewer);

		viewerConfiguration = new EditorConfiguration(viewer, annSupport);

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

		viewer.addTextListener(new ITextListener() {
			@Override
			public void textChanged(TextEvent event) {
				dirty.setDirty(true);

			}
		});

	}

	// private IVerticalRuler createVerticalRuler() {
	//
	// return new VerticalRuler(VERTICAL_RULER_WIDTH);
	//
	// }

	@Focus
	public void OnFocus(@Active @Optional ITreeItemInfo parent) {
		if (parent == parentItem)
			return;

		parentItem = srv.get(item.getParent());
		if (parentItem != null) {
			ContextInfoOptions opt1 = (ContextInfoOptions) parentItem
					.getOptions();
			if (opt1.type == BuildType.module)
				window.getContext().set(ITreeItemInfo.class, parentItem);
			else
				window.getContext().set(ITreeItemInfo.class, null);
		} else
			window.getContext().set(ITreeItemInfo.class, null);

	}

	private void saveItemText() {
		if (con.isConf())
			saveConfText();
		else
			srv.saveText(item.getId(), document.get());

	}

	private void saveConfText() {

		try {

			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			Integer id = build.getItemId((ConfService) srv, (ContextInfo) item,
					ELevel.proc, path);

			srv.saveText(id, document.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
