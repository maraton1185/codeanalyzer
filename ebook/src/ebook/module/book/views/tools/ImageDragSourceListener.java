package ebook.module.book.views.tools;

import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Composite;

public class ImageDragSourceListener implements DragSourceListener {
	private Composite parentComposite;
	private DragSource source;

	public ImageDragSourceListener(Composite parentComposite,
			DragSource source) {
		this.parentComposite = parentComposite;
		this.source = source;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		for (int i = 0; i < parentComposite.getChildren().length; i++) {
			if (parentComposite.getChildren()[i].equals(source.getControl())) {
				event.data = new Integer(i).toString();
				break;
			}
		}
	}

}
