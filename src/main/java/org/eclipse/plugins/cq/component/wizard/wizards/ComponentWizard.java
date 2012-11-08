package org.eclipse.plugins.cq.component.wizard.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import org.eclipse.plugins.cq.component.wizard.Activator;
import org.eclipse.plugins.cq.component.wizard.preferences.PreferenceConstants;

public class ComponentWizard extends Wizard implements INewWizard {

    private ComponentContentPage page;
    private DialogContentPage cfgPage;

    private ISelection selection;
    private IProgressMonitor monitor;

    private static final String PLUGIN_NAME = "Adobe CQ Component Wizard";
    private static final String CFG_PAGE_HEADER = "Select additional option";

    private static final String CLIENT_LIBS = "clientlibs";

    private Configurator cfg;

    /**
     * Constructor for ComponentWizard.
     */
    public ComponentWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * Adding the page to the wizard.
     */

    @Override
    public void addPages() {
        page = new ComponentContentPage(selection);
        cfgPage = new DialogContentPage(CFG_PAGE_HEADER);
        addPage(page);
        addPage(cfgPage);
    }

    private void collectConfiguration() throws CoreException {

        cfg = new Configurator();
        // from first page
        cfg.setContainerName(page.getContainerName());
        cfg.setComponentName(page.getFileName());
        cfg.setFolderName(page.getFileName());
        cfg.setSubFolder(page.getSubFolder());
        cfg.setDescription(page.getDescription());
        cfg.setJsFucntionName(page.getFileName());

        String innerPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_INNER_FOLDER_NAME);
        if (innerPath == null || innerPath.isEmpty()) {
        	throwCoreException("Set your inner path to the folder where component would be created in plugin's preferences");
        }
        cfg.setPathToComponent(innerPath + ((cfg.getSubFolder() != null && !cfg.getSubFolder().isEmpty()) ? cfg.getSubFolder() + "/" : "")
                + cfg.getFolderName() + "/");

        // from second page
        cfg.setUseMultiDialogTemplate(cfgPage.isDialogWithMulti());
        cfg.setDialogGroupName(cfgPage.getComponentGroupName());
        cfg.setUseCSS(cfgPage.isUseCss());
        cfg.setUseJS(cfgPage.isUseJs());
        
        cfg.setJsCategoryName(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_JS_CATEGORY_NAME));
        cfg.setTaglibPrefix(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TAGLIB_PREFIX));
        cfg.setTaglibUrl(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TAGLIB_URL));
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We
     * will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {

    	try {
    		collectConfiguration();
    	} catch (CoreException e) {
    		MessageDialog.openError(getShell(), "Error", e.getMessage());
    		return false;
    	}

        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };

        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            MessageDialog.openError(getShell(), "Error", e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    private void prepareFolder(IContainer folder) throws CoreException {
        IContainer parent = folder.getParent();
        if (parent instanceof IFolder) {
            prepareFolder(parent);
        }
        if (!folder.exists()) {
            ((IFolder) folder).create(true, false, monitor);
        }
    }

    @SuppressWarnings("serial")
    private Map<String, String> getTokens() {
        return new HashMap<String, String>() { {
        	//HEAD
            put("%head-title", cfg.getComponentName());
            put("%head-author-name", cfg.getAuthorName());
            put("%head-cur-date", cfg.getCurrentDate());
            put("%head-description", cfg.getDescription());
            //COMMON
            put("%description", cfg.getDescription());
            put("%group-name", cfg.getSubFolder().isEmpty() ? "" : cfg.getSubFolder().replaceFirst(cfg.getSubFolder().substring(0, 1), cfg.getSubFolder().substring(0, 1).toUpperCase()));
            //CSS
            put("%class-name", cfg.getFolderName());            
            put("%class-definition", cfg.isUseCSS() ? String.format("class=\"%s\"", cfg.getFolderName()) : "");
            put("%end-css-tag", cfg.isUseCSS() ? String.format("<!--END: .%s-->", cfg.getFolderName()) : "");
            put("%css-file-name", cfg.getFolderName() + ".css");
            //JS
            put("%js-definition", cfg.isUseJS() ? String.format("<ha:activationFunctionRequired functionName=\"%s\" />", cfg.getJsFucntionName()) : "");
            put("%js-function-name", cfg.isUseJS() ? cfg.getJsFucntionName() : "");
            put("%js-file-name", cfg.getFolderName() + ".js");
            
            put("%js-category-name", cfg.getJsCategoryName());
            
            if (!cfg.getTaglibPrefix().isEmpty() && !cfg.getTaglibPrefix().isEmpty()) {
            	put("%my-taglib", "<%@ " + String.format("taglib prefix=\"%s\" uri=\"%s\" ", cfg.getTaglibPrefix(), cfg.getTaglibUrl()) + "%>");
            } else {
            	put("%my-taglib", "");
            }
        }};
    }

    private IFile createFile(IContainer container, FileType type) throws CoreException, IOException {

    	String pathToTemplate = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATE_PATH);
    	//use build-in template
    	if (pathToTemplate.isEmpty()) pathToTemplate = "/templates/";
    	
        String path = pathToTemplate + type.getActualTemplateFileName();

        if (cfg.isUseMultiDialogTemplate() && type.equals(FileType.DLG)) {
            path = pathToTemplate + type.getTemplateFileName(1);
        }

        InputStream stream = null;
        stream = Activator.getDefault().getBundle().getEntry(path).openStream();
        String template = new Scanner(stream).useDelimiter("\\A").next();

        if (type.getActualTemplateFileName().contains(CLIENT_LIBS)) {
            final IFolder folder = container.getFolder(new Path(cfg.getPathToComponent() + "/" + CLIENT_LIBS));
            if (!folder.exists()) {
                folder.create(true, false, monitor);
            }
        }

        Path filePath = null;
        switch (type) {
            case JSP:
                filePath = type.getPath(cfg.getPathToComponent() + cfg.getFolderName() + ".jsp");
                stream = new ByteArrayInputStream(TemplateHelper.fillTemplate(template, getTokens()).getBytes());
                break;
            case JS:
                filePath = type.getPath(cfg.getPathToComponent() + CLIENT_LIBS + "/" + cfg.getFolderName() + ".js");
                stream = new ByteArrayInputStream(TemplateHelper.fillTemplate(template, getTokens()).getBytes());
                break;
            case CSS:
                filePath = type.getPath(cfg.getPathToComponent() + CLIENT_LIBS + "/" + cfg.getFolderName() + ".css");
                stream = new ByteArrayInputStream(TemplateHelper.fillTemplate(template, getTokens()).getBytes());
                break;
            case CQC:
            case DLG:
            case JS_TXT:
            case CSS_TXT:
                filePath = type.getPath(cfg.getPathToComponent());
                stream = new ByteArrayInputStream(TemplateHelper.fillTemplate(template, getTokens()).getBytes());
                break;
            case EDIT_CFG:
                filePath = type.getPath(cfg.getPathToComponent());
                stream = new ByteArrayInputStream(TemplateHelper.fillTemplate(template, getTokens()).getBytes());
                break;
            case JS_CQC:
                filePath = type.getPath(cfg.getPathToComponent());
                stream = new ByteArrayInputStream(template.getBytes());
                break;
            default:
                stream = new ByteArrayInputStream(template.getBytes());
        }

        if (filePath == null) {
            throwCoreException("Current type is not recognized, path unknown :" + type);
        }

        final IFile file = container.getFile(filePath);

        if (file.exists()) {
            file.setContents(stream, true, true, monitor);
        } else {
            file.create(stream, true, monitor);
        }
        stream.close();

        return file;
    }

    private void doFinish(IProgressMonitor monitor) throws CoreException {

        this.monitor = monitor;

        monitor.beginTask("Creating " + this.cfg.getComponentName(), 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        IResource resource = root.findMember(new Path(this.cfg.getContainerName()));

        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + this.cfg.getContainerName() + "\" does not exist.");
        }

        IContainer container = (IContainer) resource;

        Path componentPath = new Path(cfg.getPathToComponent());

        prepareFolder(container.getFolder(componentPath));
        
        try {
        	final IFile jspFile = createFile(container, FileType.JSP);

            final IFile dialogXMLFile = createFile(container, FileType.DLG);

            createFile(container, FileType.CQC);

            createFile(container, FileType.EDIT_CFG);

            if (cfg.isUseCSS() || cfg.isUseJS()) {
                createFile(container, FileType.JS_CQC);
            }

            if (cfg.isUseJS()) {
                createFile(container, FileType.JS_TXT);
                createFile(container, FileType.JS);
            }

            if (cfg.isUseCSS()) {
                createFile(container, FileType.CSS_TXT);
                createFile(container, FileType.CSS);
            }

            monitor.worked(1);
            monitor.setTaskName("Opening file for editing...");

            getShell().getDisplay().asyncExec(new Runnable() {
                public void run() {
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    try {
                        IDE.openEditor(page, jspFile, true);
                        IDE.openEditor(page, dialogXMLFile, true);
                    } catch (PartInitException e) {
                    }
                }
            });

        } catch (IOException ex) {
            throwCoreException("Failed to read template from the folder");
        }

        monitor.worked(1);
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, PLUGIN_NAME, IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize
     * from it.
     *
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}