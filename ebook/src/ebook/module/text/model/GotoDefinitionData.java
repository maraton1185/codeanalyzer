package ebook.module.text.model;

import ebook.core.pico;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.services.TextParser;

public class GotoDefinitionData {

	TextParser parser = pico.get(ICfServices.class).parse();

	private String text;
	private int position;
	private ContextInfo item;

	public ContextInfo getItem() {
		return item;
	}

	public GotoDefinitionData(ContextInfo item, String text, int position) {
		this.item = item;
		this.text = text;
		this.position = position;
	}

	public String getProcInPosition() {

		return parser.getProcInPosition(position, text);
	}

}
