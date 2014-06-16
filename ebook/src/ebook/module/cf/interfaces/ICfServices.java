package ebook.module.cf.interfaces;

import ebook.module.cf.services.CfGetService;
import ebook.module.cf.services.CfLoadService;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

}