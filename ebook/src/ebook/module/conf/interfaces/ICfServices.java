package ebook.module.conf.interfaces;

import ebook.module.conf.services.CfGetService;
import ebook.module.conf.services.CfLoadService;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

}