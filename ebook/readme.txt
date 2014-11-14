
http://download.eclipse.org/egit/github/updates - mylyn github connector

http://marketplace.eclipse.org/content/properties-editor#.U6ls6EAe9f8

e4 tools: http://blog.vogella.com/2014/01/21/eclipse-e4-tools-update-site-for-kepler-and-luna/
http://download.vogella.com/kepler/e4tools

редактор jsp, html, css, js:
Kepler - http://download.eclipse.org/releases/kepler
что бы избежать запроса про xText
pref - general-editor-files assot - css удалить default-редактор

для mozilla:
http://wiki.eclipse.org/ATF/Installing
http://download.eclipse.org/tools/atf/updates/0.3.0

зависимости для jetty:
https://github.com/jetty-project/embedded-jetty-jsp/blob/master/pom.xml

coffee script:
Download and run Node.js installer http://nodejs.org/download/.
Run on the command line: npm install -g coffee-script
That's all. You can now run coffee from any directory.
install http://www.sublimetext.com/
install sublime package control https://sublime.wbond.net/installation#st2
install package CoffeeScript, CoffeeScript build system in sublime
in terminal:
coffee -wc test.coffee

jetty start error 
Issues during startup	
Check the log file in the workspace folder of your exported application to see the error messages during startup. 
Alternatively add the "-consoleLog" parameter to the ".ini" file in folder of the exported application.

http://mahichir.wordpress.com/2012/08/07/eclipse-rcp-and-p2-headless-update-on-startup/

GWT
-------
install jdk (window - prefs - java - installed jre - add)

download and unpack gwt sdk http://www.gwtproject.org/download.html
webAppCreator -out MyWebApp com.mycompany.mywebapp.MyWebApp
import to eclipse
launch in dev mode = run MyWebApp.launch
compile = build.xml with ant (ant build - jre = jdk) 

to jetty
---
compile
copy war\mywebapp
add gwt-servlet.jar to build-path (and runtime path)
packages of rcp interface must be identical
web.xml - path to servlet (<module rename-to='mywebapp'>)

rename package
---
debug configuration - arguments
build.xml
MyWebApp.gwt.xml (<module rename-to='mywebapp'>) in same package with entrypoint

xulRunner
---
должен быть в папке установки ("..\workspace")
версия - 10



