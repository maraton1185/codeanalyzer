package ebook.module.text.scanner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import ebook.core.pico;
import ebook.core.interfaces.IColorManager;

public class DocumentPartitionScanner extends RuleBasedPartitionScanner {

	public final static String STRING = "__string___";
	public final static String COMMENT = "__comment___";

	IColorManager provider = pico.get(IColorManager.class);

	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public DocumentPartitionScanner() {
		super();

		List<IRule> rules = new ArrayList<IRule>();

		IToken string = new Token(STRING);
		rules.add(new ScannerStringRule("\"", "\"", string));
		// rules.add(new MultiLineRule("\"", "\"", string));

		IToken comment = new Token(COMMENT);
		rules.add(new EndOfLineRule("//", comment));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);

	}
}
