package ebook.module.confLoad.interfaces;

import ebook.module.confLoad.services.CfGetService;
import ebook.module.confLoad.services.CfLoadService;
import ebook.module.confLoad.services.TextParser;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

	TextParser parse();

}