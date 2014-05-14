package codeanalyzer.cf.interfaces;

import codeanalyzer.cf.services.CfGetService;
import codeanalyzer.cf.services.CfLoadService;

public interface ICfServices {

	CfGetService get();

	CfLoadService load();

}