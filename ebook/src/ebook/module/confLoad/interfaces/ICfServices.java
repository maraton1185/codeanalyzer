package ebook.module.confLoad.interfaces;

import ebook.module.confLoad.services.CfGetService;
import ebook.module.confLoad.services.CfLoadService;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

}