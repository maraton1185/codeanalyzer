package ebook.module.text.strategy;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

import ebook.core.App;
import ebook.core.pico;
import ebook.core.exceptions.ProcNotFoundException;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.procEntity;
import ebook.module.confLoad.services.TextParser;
import ebook.module.text.model.LineInfo;
import ebook.utils.Const;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_TEXT_DATA;

public class ReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {

	// final LinkedHashMap<String, LineInfo> model = new LinkedHashMap<String,
	// LineInfo>();

	TextParser parser = pico.get(ICfServices.class).parse();

	IDocument fDocument;

	ArrayList<LineInfo> fModel = new ArrayList<LineInfo>();
	ArrayList<Position> fMarkers = new ArrayList<Position>();

	private String fSearch = "";

	public ReconcilingStrategy() {
	}

	@Override
	public void initialReconcile() {

		try {
			fillModel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void fillModel() throws BadLocationException, ProcNotFoundException {

		fModel.clear();
		fMarkers.clear();

		int startLine = 0;
		ArrayList<String> buffer = new ArrayList<String>();
		ArrayList<String> vars = new ArrayList<String>();
		Boolean procWasFound = false;

		String source_line = null;
		String currentSection = "";

		for (int index = 0; index < fDocument.getNumberOfLines(); index++) {
			//
			IRegion r = fDocument.getLineInformation(index);
			source_line = fDocument.get(r.getOffset(), r.getLength());

			if (!fSearch.isEmpty()
					&& parser.findTextInLine(source_line, fSearch)) {
				fMarkers.add(new Position(r.getOffset(), 0));
			}
			buffer.add(source_line + "\n");

			if (parser.findProcEnd(source_line)) {

				procEntity proc = new procEntity(true);
				int lineOffset = parser.getProcInfo(proc, buffer, vars,
						currentSection);
				if (!procWasFound && !vars.isEmpty()) {
					LineInfo lineInfo = new LineInfo();
					lineInfo.line = 0;
					lineInfo.offset = 0;
					lineInfo.setTitle(Const.STRING_VARS_TITLE);
					lineInfo.name = Const.STRING_VARS;
					lineInfo.export = false;
					// lineInfo.data = data;
					fModel.add(lineInfo);

				}

				buffer.clear();
				procWasFound = true;

				startLine = startLine + lineOffset;

				IRegion reg = fDocument.getLineInformation(startLine);

				LineInfo lineInfo = new LineInfo();
				lineInfo.line = startLine;
				lineInfo.offset = reg.getOffset();
				lineInfo.setTitle(proc.proc_title);
				lineInfo.name = proc.proc_name;
				lineInfo.export = proc.export;
				lineInfo.length = reg.getLength();
				lineInfo.projection = new Position(reg.getOffset(),
						r.getOffset() - reg.getOffset() + r.getLength() + 1);
				fModel.add(lineInfo);

				// fPositions.add();

				startLine = index + 1;
			}
		}
		if (!buffer.isEmpty()) {
			String listString = "";
			for (String s : buffer)
				listString += s;
			if (!listString.trim().isEmpty()) {
				IRegion reg = fDocument.getLineInformation(startLine);
				LineInfo lineInfo = new LineInfo();
				lineInfo.line = startLine;
				lineInfo.offset = reg.getOffset();
				lineInfo.setTitle(Const.STRING_INIT_TITLE);
				lineInfo.name = Const.STRING_INIT;
				lineInfo.export = false;
				// lineInfo.data = data;
				fModel.add(lineInfo);
			}
		}

		App.br.post(Events.EVENT_UPDATE_TEXT_MODEL, new EVENT_TEXT_DATA(null,
				fDocument, fModel, fMarkers));
	}

	@Override
	public void setDocument(IDocument document) {
		this.fDocument = document;
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		initialReconcile();

	}

	@Override
	public void reconcile(IRegion partition) {
		initialReconcile();

	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {

	}

	public void setSearchText(String text) {
		fSearch = text.trim();

	}

}
