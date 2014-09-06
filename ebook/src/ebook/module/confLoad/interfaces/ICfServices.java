package ebook.module.confLoad.interfaces;

import ebook.module.confLoad.services.CfGetService;
import ebook.module.confLoad.services.CfLoadService;
import ebook.module.confLoad.services.TextBuffer;
import ebook.module.confLoad.services.TextParser;

public interface ICfServices {

	// CfBuildService build(IBuildConnection connection);

	CfGetService get();

	CfLoadService load();

	TextParser parse();

	TextBuffer buffer();

}