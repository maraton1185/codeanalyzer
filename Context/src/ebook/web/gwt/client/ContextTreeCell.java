package ebook.web.gwt.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class ContextTreeCell extends AbstractCell<ContextTreeItem> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			ContextTreeItem value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		SafeHtml safeValue = SafeHtmlUtils.fromString(value.getTitle());
		sb.append(safeValue);

	}
}
