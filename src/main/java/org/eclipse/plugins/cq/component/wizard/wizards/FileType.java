package org.eclipse.plugins.cq.component.wizard.wizards;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Path;

public enum FileType {
	
	JSP(Arrays.asList("index.jsp")),
	DLG("dialog.xml", Arrays.asList("dialog.xml", "dialog_m.xml")), 
	
	CQC(".content.xml", Arrays.asList(".content.xml")), 
	
	EDIT_CFG("_cq_editConfig.xml", Arrays.asList("_cq_editConfig.xml")), 
	
	JS_CQC("clientlibs/.content.xml", Arrays.asList("clientlibs/.content.xml")),
	
	JS(Arrays.asList("clientlibs/index.js")), 
	JS_TXT("clientlibs/js.txt", Arrays.asList("clientlibs/js.txt")),
	
	CSS(Arrays.asList("clientlibs/index.css")),
	CSS_TXT("clientlibs/css.txt", Arrays.asList("clientlibs/css.txt"));
	
	private String resultFileName;
	private final List<String> templateFileNames;

	FileType(String name, List<String> names) {
		resultFileName = name;
		templateFileNames = names;
	}

	FileType(List<String> names) {
		resultFileName = "";
		templateFileNames = names;
	}
	
	public Path getPath(String mainPath) {
		return getResultFileName().isEmpty() ? new Path(mainPath) : new Path(mainPath + "/" + getResultFileName());
	}
	
	public String getResultFileName() {
		return resultFileName;
	}
	
	public String getActualTemplateFileName() {
		return this.templateFileNames.get(0);
	}

	public String getTemplateFileName(int idx) {
		return this.templateFileNames.get(idx);
	}

	public List<String> getTemplateFileNames() {
		return this.templateFileNames;
	}
}