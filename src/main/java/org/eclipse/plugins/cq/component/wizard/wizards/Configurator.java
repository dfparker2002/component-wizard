package org.eclipse.plugins.cq.component.wizard.wizards;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.runtime.Platform;

public class Configurator {

	private static final String DEFAULT_USER_NAME = "<!>Your username</!>";
	private String pathToComponent;
	private String componentName;
	private String containerName;
	private String folderName;
	private String subFolder;
	private String description;
	private boolean useMultiDialogTemplate;
	private String dialogGroupName;
	private boolean useCSS;
	private boolean useJS;
	private String jsFucntionName;
	
	private String authorName;

	private String jsCategoryName;
	private String taglibPrefix;
	private String taglibUrl;
	
	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getPathToComponent() {
		return pathToComponent;
	}
	
	public void setPathToComponent(String pathToComponent) {
		this.pathToComponent = pathToComponent;
	}
		
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName.toLowerCase().replace(" ", "-");
	}
	
	public String getSubFolder() {
		return subFolder;
	}
	public void setSubFolder(String subFolder) {
		this.subFolder = subFolder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUseMultiDialogTemplate() {
		return useMultiDialogTemplate;
	}
	public void setUseMultiDialogTemplate(boolean useMultiDialogTemplate) {
		this.useMultiDialogTemplate = useMultiDialogTemplate;
	}
	public String getDialogGroupName() {
		return dialogGroupName;
	}
	public void setDialogGroupName(String dialogGroupName) {
		this.dialogGroupName = dialogGroupName;
	}
	public boolean isUseCSS() {
		return useCSS;
	}
	public void setUseCSS(boolean useCSS) {
		this.useCSS = useCSS;
	}
	public boolean isUseJS() {
		return useJS;
	}
	public void setUseJS(boolean useJS) {
		this.useJS = useJS;
	}

	public String getJsFucntionName() {
		return jsFucntionName;
	}

	public void setJsFucntionName(String jsFucntionName) {
		if (jsFucntionName.matches(".*[0-9].*") && jsFucntionName.contains(" ")) {
			this.jsFucntionName = jsFucntionName.substring(jsFucntionName.indexOf(" ")).replace(" ", "");
		} else if (jsFucntionName.contains(" ")) {
			this.jsFucntionName = jsFucntionName.substring(jsFucntionName.indexOf(" ")).replace(" ", "");
		} else {
			this.jsFucntionName = jsFucntionName;
		}
	}

	public String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd"); 
		return sdf.format(Calendar.getInstance().getTime());
	}

	public String getAuthorName() {
		if ((authorName == null || authorName.isEmpty()) && Platform.getUserLocation() != null) {
			String[] str = Platform.getUserLocation().getURL().toString().split("\\/");
			for (String v : str) { 
				 if (v.contains("@")) {setAuthorName(v); break;}
			}
			if (authorName == null || authorName.isEmpty()) setAuthorName(DEFAULT_USER_NAME);
		}
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getJsCategoryName() {
		return jsCategoryName;
	}

	public void setJsCategoryName(String jsCategoryName) {
		this.jsCategoryName = jsCategoryName;
	}

	public String getTaglibPrefix() {
		return taglibPrefix;
	}

	public void setTaglibPrefix(String taglibPrefix) {
		this.taglibPrefix = taglibPrefix;
	}

	public String getTaglibUrl() {
		return taglibUrl;
	}

	public void setTaglibUrl(String taglibUrl) {
		this.taglibUrl = taglibUrl;
	}
}