package codeanalyzer.module.cf.interfaces;

import codeanalyzer.module.cf.services.CfGetService;
import codeanalyzer.module.cf.services.CfLoadService;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

}