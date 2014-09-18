package ebook.module.book.views.tools;

import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ebook.module.book.views.interfaces.IPictureTuneData;

public class SectionDropTargetListener implements DropTargetListener {

	private Composite parentComposite;
	private DropTarget target;
	private IPictureTuneData tune;

	public SectionDropTargetListener(IPictureTuneData tune,
			Composite parentComposite, DropTarget target) {
		this.parentComposite = parentComposite;
		this.target = target;
		this.tune = tune;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {

	}

	@Override
	public void dragLeave(DropTargetEvent event) {

	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {

	}

	@Override
	public void dragOver(DropTargetEvent event) {

	}

	@Override
	public void drop(DropTargetEvent event) {
		// retrieve the stored index
		int sourceIndex = Integer.valueOf(event.data.toString());

		// compute the index of target control
		Control targetControl = target.getControl();
		int targetIndex = -1;
		for (int i = 0; i < parentComposite.getChildren().length; i++) {
			if (parentComposite.getChildren()[i].equals(targetControl)) {
				targetIndex = i;
				break;
			}
		}

		Control sourceControl = parentComposite.getChildren()[sourceIndex];
		// do not do anything if the dragged photo is dropped at the same
		// position
		if (targetIndex == sourceIndex)
			return;

		// if dragged from left to right
		// shift the old picture to the left
		if (targetIndex > sourceIndex)
			sourceControl.moveBelow(targetControl);
		// if dragged from right to left
		// shift the old picture to the right
		else
			sourceControl.moveAbove(targetControl);

		tune.saveOrder();

		// repaint the parent composite
		parentComposite.layout();

	}

	@Override
	public void dropAccept(DropTargetEvent event) {

	}

}
