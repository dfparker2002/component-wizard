CQWizard
========

Eclipse wizard plugin for create CQ component in simple and easy way from template.

The project contains template folder with common structure for Adobe Day CQ component.
The path to the template folder: "src\main\resources\templates";

 	\clientlibs
                .content.xml
		.css.txt
		index.js
		js.txt
	.content.xml
	_cq_editConfig.xml
	dialog.xml
	dialog_m.xml (alternative version)
	index.jsp

This structure fully cover all you need to create a custom component.
The variable I used in template is starting with % sign, and replaced a value from wizard dialog or from preference page.

How to use it: 

1. "mvn clean install" to make a jar file. 
2. put it inside eclipse/plugin directory. 
3. Run Eclipse
4. Change a settings "Windows/Preferences/CQ5 Wizard Preferences";
5. File -> New -> Other -> CQ5 Component Wizard.
6. Follow the step. 
