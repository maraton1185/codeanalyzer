package ebook.module.text.strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import ebook.temp.build.LineInfo;
import ebook.utils.Const;
import ebook.utils.Events;

public class ReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {

	final LinkedHashMap<String, LineInfo> model = new LinkedHashMap<String, LineInfo>();

	TextParser parser = pico.get(ICfServices.class).parse();

	IDocument fDocument;

	// final Map<String, Position> fPositions = new HashMap<String, Position>();
	final ArrayList<Position> fMarkers = new ArrayList<Position>();

	public ReconcilingStrategy() {
	}

	@Override
	public void initialReconcile() {

		try {
			fillFoldingStructure();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Display.getDefault().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// // editor.updateFoldingStructure(fPositions);
		// // LineInfo line = getLineInfo();
		// // if (line!=null)
		// // editor.highlightLine(line.line);
		// // if (outlineView != null)
		// // outlineView.update(editor, model);
		// }
		// });
		//
		// updateCurrentLine();
	}

	// LineInfo getLineInfo() {
	// BuildInfo data = ((EditorInput) editor.getEditorInput()).getData();
	// return model.get(data.name);
	// }

	// public void updateCurrentLine() {

	// BuildInfo data = ((EditorInput) editor.getEditorInput()).getData();
	//
	// fMarkers.clear();
	// if ((!data.getCalleeName().isEmpty()) || data.compare
	// || (!data.getSearch().isEmpty())) {
	// fillMarkers();
	// }
	//
	// Display.getDefault().asyncExec(new Runnable() {
	// @Override
	// public void run() {
	// editor.updateMarkers(fMarkers);
	// LineInfo line = getLineInfo();
	// if (line != null)
	// editor.highlightLine(line.line);
	// }
	// });
	// }

	private void fillFoldingStructure() throws BadLocationException,
			ProcNotFoundException {

		List<Position> fPositions = new ArrayList<Position>();
		model.clear();
		//
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

			buffer.add(source_line + "\n");

			if (parser.findProcEnd(source_line)) {

				procEntity proc = new procEntity(true);
				int lineOffset = parser.getProcInfo(proc, buffer, vars,
						currentSection);
				if (!procWasFound && !vars.isEmpty()) {
					LineInfo lineInfo = new LineInfo();
					lineInfo.line = 0;
					lineInfo.offset = 0;
					lineInfo.title = Const.STRING_VARS_TITLE;
					lineInfo.name = Const.STRING_VARS;
					lineInfo.export = false;
					// lineInfo.data = data;
					model.put(lineInfo.name, lineInfo);

				}

				buffer.clear();
				procWasFound = true;

				startLine = startLine + lineOffset;

				IRegion reg = fDocument.getLineInformation(startLine);

				LineInfo lineInfo = new LineInfo();
				lineInfo.line = startLine;
				lineInfo.offset = reg.getOffset();
				lineInfo.title = proc.proc_title;
				lineInfo.name = proc.proc_name;
				lineInfo.export = proc.export;
				// lineInfo.data = data;
				model.put(proc.proc_name, lineInfo);

				fPositions.add(new Position(reg.getOffset(), r.getOffset()
						- reg.getOffset() + r.getLength() + 1));

				startLine = index + 1;
			}

			if (!buffer.isEmpty()) {
				IRegion reg = fDocument.getLineInformation(startLine);
				LineInfo lineInfo = new LineInfo();
				lineInfo.line = startLine;
				lineInfo.offset = reg.getOffset();
				lineInfo.title = Const.STRING_INIT_TITLE;
				lineInfo.name = Const.STRING_INIT;
				lineInfo.export = false;
				// lineInfo.data = data;
				model.put(lineInfo.name, lineInfo);
			}
		}

		App.br.post(Events.EVENT_TEXT_VIEW_UPDATE_FOLDING, fPositions);
	}

	private void fillMarkers() {
		// try {
		//
		// BuildInfo data = ((EditorInput) editor.getEditorInput()).getData();
		//
		// fMarkers.clear();
		// boolean mProcStart = false;
		//
		// for (int index = 0; index < fDocument.getNumberOfLines(); index++) {
		//
		// IRegion r = fDocument.getLineInformation(index);
		// String line = fDocument.get(r.getOffset(), r.getLength());
		//
		// if (parser.findTextInLine(line, data.getSearch())) {
		//
		// fMarkers.add(new Position(r.getOffset(), line.length()));
		// }
		//
		// if (parser.findCompareMarker(line)) {
		//
		// fMarkers.add(new Position(r.getOffset(), line.length()));
		// }
		//
		// if (parser.findProcStart(line, data.name)) {
		// mProcStart = true;
		// }
		//
		// if (parser.findProcEnd(line)) {
		//
		// mProcStart = false;
		// }
		//
		// if (mProcStart)
		// for (String callee : data.getCalleeName()) {
		// if (parser.findCallee(line, callee)) {
		// fMarkers.add(new Position(r.getOffset(), line
		// .length()));
		// }
		// }
		//
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }
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

}
