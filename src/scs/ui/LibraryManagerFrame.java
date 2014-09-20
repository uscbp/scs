package scs.ui;

import scs.util.SCSUtility;
import scs.util.BrowserLauncher;
import scs.Module;
import scs.Declaration;
import scs.graphics.IconInst;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Vector;
import java.util.Arrays;

public class LibraryManagerFrame extends JFrame implements ListSelectionListener, ActionListener
{
    JMenuItem deleteLibrary, deleteModule, deleteVersion, copyModule, pasteModule, cutModule, copyVersion, pasteVersion,
            cutVersion;

    String clipboardLibraryName, clipboardLibraryPath, clipboardModulePath, clipboardModuleVersion, clipboardModuleVersionPath;
    String lastSelectedLib, lastSelectedMod, lastSelectedVer, lastSelectedDir;
    boolean deleteAfterPaste = false;

    String[] libraryNames, libraryPaths;

    SchEditorFrame parentFrame;

    private JLabel libraryLabel;
    private JList libraryList;
    private JScrollPane libraryScrollPane;
    private JLabel moduleLabel;
    private JList moduleList;
    private JScrollPane moduleScrollPane;
    private JLabel versionLabel;
    private JList versionList;
    private JScrollPane versionScrollPane;
    private JLabel srcDocIoLabel;
    private JList srcDocIoList;
    private JScrollPane srcDocIoScrollPane;
    private JLabel filesLabel;
    private JList fileList;
    private JButton closeButton;
    private JScrollPane fileScrollPane;
    private JPanel mainPanel;
    private JPanel buttonPanel;

    public LibraryManagerFrame(SchEditorFrame parent)
    {
        super("Library Manager");
        parentFrame = parent;

        setJMenuBar(CreateMenuBar());

        add(mainPanel);

        reloadLibPaths();

        libraryList.addListSelectionListener(this);

        moduleList.addListSelectionListener(this);

        versionList.addListSelectionListener(this);

        srcDocIoList.addListSelectionListener(this);

        fileList.addListSelectionListener(this);

        closeButton.addActionListener(this);

    }

    public void addNewLibrary(String libNickName, String libraryName)
    {
        File tempFile = new File(libraryName);
        String myLibStr = libNickName + '=' + libraryName;

        if (tempFile.exists() && !tempFile.isDirectory())
            // create the module
            JOptionPane.showMessageDialog(parentFrame, "LibraryManagerFrame:file exists with same name at library path.",
                                          "Library File Error", JOptionPane.ERROR_MESSAGE);
        else if (!tempFile.exists() && !tempFile.mkdirs())
        {
            String errstr = "LibraryManagerFrame:could not create the library. Please check path " + libraryName;
            JOptionPane.showMessageDialog(parentFrame, errstr, "Library File Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            try
            {
                //open SCS LIBRARY PATHS FILE, and write nick and libPath name to it.
                SCSUtility.appendToFile(SCSUtility.scs_library_paths_path, myLibStr);
                reloadLibPaths();
            }
            catch (FileNotFoundException e)
            {
            }
            catch (IOException e)
            {
            }
        }
    }

    /*--------------------*/
    /* *
    * Perform menu functions according to action events.
    */
    public void actionPerformed(ActionEvent event)
    {
        JMenuItem mi;
        String actionLabel = "none";

        if (event.getSource() instanceof JMenuItem)
        {
            mi = (JMenuItem) event.getSource();
            actionLabel = mi.getText();
        }
        else if (event.getSource() instanceof JButton)
        {
            JButton dmi = (JButton) event.getSource();
            actionLabel = dmi.getText();
        }
        if (actionLabel.equals("Close"))
        {
            dispose();
        }
        else if (actionLabel.equals("Delete Library"))
        {
            if (libraryList.getSelectedValue() != null)
            {
                try
                {
                    String delLibName = libraryList.getSelectedValue().toString();
                    String delLibPath = returnLibraryPath();

                    // get the selected Library path.. Give warning...
                    String errstr = "Delete library : " + delLibPath + "?";
                    Object[] options = {"Ok", "Cancel"};
                    int n = JOptionPane.showOptionDialog(parentFrame, errstr, "Library Delete Warning", JOptionPane.OK_CANCEL_OPTION,
                                                         JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (n == JOptionPane.OK_OPTION)
                    {
                        // Delete library
                        if ((new File(delLibPath)).exists())
                            SCSUtility.deleteFile(delLibPath, this);
                        // Delete libraries files
                        SCSUtility.deleteFile(SCSUtility.scs_library_paths_path, this);
                        // Re-create libraries files
                        File f = new File(SCSUtility.scs_library_paths_path);
                        if (f.createNewFile())
                        {
                            // Re-add libraries that weren't deleted
                            for (int i = 0; i < libraryNames.length; i++)
                            {
                                if (!libraryNames[i].equals(delLibName))
                                {
                                    String libString = libraryNames[i] + '=' + libraryPaths[i];
                                    SCSUtility.appendToFile(SCSUtility.scs_library_paths_path, libString);
                                }
                            }
                        }
                        reloadLibPaths();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (actionLabel.equals("Delete Module"))
        {
            if (returnModulePath() != null)
            {
                // get the selected module path.. Give warning...
                String errstr = "Delete Module and Versions: " + returnModulePath() + "?";
                Object[] options = {"Ok", "Cancel"};
                int n = JOptionPane.showOptionDialog(parentFrame, errstr, "Module Delete Warning",
                                                     JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                                     options, options[0]);
                if (n == JOptionPane.OK_OPTION)
                {
                    SCSUtility.deleteFile(returnModulePath(), this);
                    SCSUtility.deleteFile(returnModuleMatlabPath(), this);
                    libraryList.setSelectedValue(lastSelectedLib, true);
                    updateLibraryLists();
                }
            }
        }
        else if (actionLabel.equals("Copy Module"))
        {
            // get the selected module path
            if (returnLibraryPath() != null && returnModulePath() != null)
            {
                // save library and module paths to clipboard
                clipboardLibraryPath = returnLibraryPath();
                try
                {
                    clipboardLibraryName = SCSUtility.getLibNickName(clipboardLibraryPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                clipboardModulePath = returnModulePath();

                // set flag to not delete after paste
                deleteAfterPaste = false;

                // enable paste
                pasteModule.setEnabled(true);
            }
        }
        else if (actionLabel.equals("Cut Module"))
        {
            // get the selected module path
            if (returnLibraryPath() != null && returnModulePath() != null)
            {
                // save library and module paths to clipboard
                clipboardLibraryPath = returnLibraryPath();
                try
                {
                    clipboardLibraryName = SCSUtility.getLibNickName(clipboardLibraryPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                clipboardModulePath = returnModulePath();

                // set flag to delete after paste
                deleteAfterPaste = true;

                // enable paste
                pasteModule.setEnabled(true);
            }
        }
        else if (actionLabel.equals("Paste Module"))
        {
            // get selected library path
            if (returnLibraryPath() != null)
            {
                String libPath = returnLibraryPath();
                try
                {
                    // get library nickname
                    String libNick = SCSUtility.getLibNickName(libPath);

                    // open each version of the clipboard module
                    String clipboardModName = clipboardModulePath.substring(clipboardModulePath.lastIndexOf(File.separatorChar) + 1);
                    recursivePasteModule(libPath, libNick, clipboardLibraryName, clipboardModName, clipboardModulePath);
                    libraryList.setSelectedValue(lastSelectedLib, true);
                    updateLibraryLists();
                }
                catch (Exception e)
                {

                }
            }
        }
        if (actionLabel.equals("Delete Version"))
        {
            if (returnVersionPath() != null)
            {
                // get the selected  version   path.. Give warning...
                String errstr = "Delete version : " + returnVersionPath() + "?";
                Object[] options = {"Ok", "Cancel"};
                int n = JOptionPane.showOptionDialog(parentFrame, errstr, "Version Delete Warning",
                                                     JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                                     options, options[0]);
                if (n == JOptionPane.OK_OPTION)
                {
                    SCSUtility.deleteFile(returnVersionPath(), this);
                    libraryList.setSelectedValue(lastSelectedLib, true);
                    moduleList.setSelectedValue(lastSelectedMod, true);
                    updateModuleLists();
                }
            }
        }
        else if (actionLabel.equals("Copy Version"))
        {
            // get the selected module path
            if (returnLibraryPath() != null && returnModulePath() != null && returnVersionPath() != null)
            {
                // save library and module paths to clipboard
                clipboardLibraryPath = returnLibraryPath();
                try
                {
                    clipboardLibraryName = SCSUtility.getLibNickName(clipboardLibraryPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                clipboardModulePath = returnModulePath();
                clipboardModuleVersionPath = returnVersionPath();
                clipboardModuleVersion = clipboardModuleVersionPath.substring(clipboardModuleVersionPath.lastIndexOf(File.separatorChar) + 1);

                // set flag to not delete after paste
                deleteAfterPaste = false;

                // enable paste
                pasteModule.setEnabled(true);
            }
        }
        else if (actionLabel.equals("Cut Version"))
        {
            // get the selected module path
            if (returnLibraryPath() != null && returnModulePath() != null)
            {
                // save library and module paths to clipboard
                clipboardLibraryPath = returnLibraryPath();
                try
                {
                    clipboardLibraryName = SCSUtility.getLibNickName(clipboardLibraryPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                clipboardModulePath = returnModulePath();
                clipboardModuleVersionPath = returnVersionPath();
                clipboardModuleVersion = clipboardModuleVersionPath.substring(clipboardModuleVersionPath.lastIndexOf(File.separatorChar) + 1);

                // set flag to delete after paste
                deleteAfterPaste = true;

                // enable paste
                pasteModule.setEnabled(true);
            }
        }
        else if (actionLabel.equals("Paste Version"))
        {
            // get selected library path
            if (returnLibraryPath() != null)
            {
                String libPath = returnLibraryPath();

                try
                {
                    // get library nickname
                    String libNick = SCSUtility.getLibNickName(libPath);

                    // open each version of the clipboard module
                    String clipboardModName = clipboardModulePath.substring(clipboardModulePath.lastIndexOf(File.separatorChar) + 1);

                    recursivePasteVersion(libPath, libNick, clipboardLibraryName, clipboardModName, clipboardModuleVersion, clipboardModuleVersionPath);
                    libraryList.setSelectedValue(lastSelectedLib, true);
                    moduleList.setSelectedValue(lastSelectedMod, true);
                    updateModuleLists();
                }
                catch (Exception e)
                {
                }
            }
        }
        if (actionLabel.equals("New Library"))
        {
            String libraryName = null, nickName = null;
            JFileChooser libPathChooser = new JFileChooser();
            libPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            libPathChooser.showOpenDialog(this);
            if (libPathChooser.getSelectedFile() != null)
            {
                libraryName = libPathChooser.getSelectedFile().getAbsolutePath();
                nickName = libraryName.substring(libraryName.lastIndexOf(File.separatorChar) + 1);
                boolean valid = false;
                while (!valid)
                {
                    nickName = (String) JOptionPane.showInputDialog(this, "Enter Name", "Library Name",
                                                                    JOptionPane.PLAIN_MESSAGE, null, null, nickName);
                    if (nickName != null)
                    {
                        if (nickName.length() == 0)
                            JOptionPane.showMessageDialog(parentFrame, "LibraryManagerFrame:Please enter a library nickname",
                                                          "Library Nickname Error", JOptionPane.ERROR_MESSAGE);
                        else if (nickName.contains(" "))
                            JOptionPane.showMessageDialog(parentFrame,
                                                          "LibraryManagerFrame:Library nickname must not contain spaces",
                                                          "Library Nickname Error", JOptionPane.ERROR_MESSAGE);
                        else
                        {
                            boolean unique = true;
                            for (int i = 0; i < libraryNames.length; i++)
                            {
                                if (libraryNames[i].equalsIgnoreCase(nickName))
                                {
                                    unique = false;
                                    break;
                                }
                            }
                            if (!unique)
                                JOptionPane.showMessageDialog(parentFrame,
                                                              "LibraryManagerFrame:Library nickname must be unique",
                                                              "Library Nickname Error", JOptionPane.ERROR_MESSAGE);
                            else
                                valid = true;
                        }
                    }
                    else
                        valid = true;
                }
            }

            if (libraryName != null && nickName != null)
                addNewLibrary(nickName, libraryName);
        }
        if (actionLabel.equals("Help"))
            BrowserLauncher.openUrl("http://neuroinformatics.usc.edu/mediawiki/index.php/Library_Manager");
    }

    protected void recursivePasteModule(String newLibPath, String newLibName, String oldLibName, String moduleName, String oldModulePath)
    {
        // create new module directory in selected library
        File newModDir = new File(newLibPath + File.separatorChar + moduleName);
        if (!newModDir.exists())
        {
            newModDir.mkdirs();

            // Go through each version in the clipboard module
            File modDir = new File(oldModulePath);
            if (modDir.exists() && modDir.isDirectory())
            {
                File[] files = modDir.listFiles();
                for (int i = 0; i < files.length; i++)
                {
                    File f = files[i];
                    if (f.isDirectory() && f.getName().charAt(0) == 'v')
                    {
                        String modVerPath = f.getAbsolutePath();
                        String modVer = modVerPath.substring(modVerPath.lastIndexOf(File.separatorChar) + 1);

                        recursivePasteVersion(newLibPath, newLibName, oldLibName, moduleName, modVer, modVerPath);
                    }
                }
                if (deleteAfterPaste)
                    SCSUtility.deleteFile(oldModulePath, this);
                reloadLibPaths();
            }
        }
    }

    protected void recursivePasteVersion(String newLibPath, String newLibName, String oldLibName, String moduleName,
                                         String moduleVersion, String moduleVersionPath)
    {
        // create new module directory in selected library
        File newModDir = new File(newLibPath + File.separatorChar + moduleName);
        if (!newModDir.exists())
            newModDir.mkdirs();

        // Make path for this version in selected library
        File newModVerDir = new File(newLibPath + File.separatorChar + moduleName + File.separatorChar +
                moduleVersion);
        if (!newModVerDir.exists())
        {
            newModVerDir.mkdirs();

            // Open this version
            try
            {
                Module mod = SCSUtility.openModule(oldLibName, moduleName, moduleVersion);

                // change library
                mod.libNickName = newLibName;
                if (mod.myIcon != null)
                    mod.myIcon.libNickName = newLibName;

                for (int j = 0; j < mod.variables.size(); j++)
                {
                    Declaration var = mod.variables.get(j);
                    if (var.varDialogType.equals("SubModule"))
                    {
                        String verPath = SCSUtility.getLibPath(var.modLibNickName) + File.separator + var.varType + File.separator + var.modVersion;
                        recursivePasteVersion(newLibPath, newLibName, var.modLibNickName, var.varType, var.modVersion, verPath);

                        var.modLibNickName = newLibName;
                    }
                }
                for (int j = 0; j < mod.mySchematic.drawableObjs.size(); j++)
                {
                    if (mod.mySchematic.drawableObjs.get(j) instanceof IconInst)
                        ((IconInst) mod.mySchematic.drawableObjs.get(j)).libNickName = newLibName;
                }

                // save to selected library path
                String newSrcPath = SCSUtility.getSrcPathUsingNick(newLibName, moduleName, moduleVersion);
                String newXmlFilePath = SCSUtility.getFullPathToXml(newSrcPath, moduleName);
                mod.save(newSrcPath, newXmlFilePath);

                String matlabDirPath = SCSUtility.getMatlabPathUsingNick(oldLibName, moduleName);
                File matlabDir = new File(matlabDirPath);
                if (matlabDir.exists() && matlabDir.isDirectory())
                {
                    String newMatlabDirPath = SCSUtility.getMatlabPathUsingNick(newLibName, moduleName);
                    File newMatlabDir = new File(newMatlabDirPath);
                    if (!newMatlabDir.exists() || !newMatlabDir.isDirectory())
                    {
                        newMatlabDir.mkdirs();
                        SCSUtility.copyFolder(matlabDir, newMatlabDir);
                    }
                    String matlabNslModuleDirPath = SCSUtility.getMatlabPathUsingNick(oldLibName, "NslModule");
                    File matlabNslModuleDir = new File(matlabNslModuleDirPath);
                    if (matlabNslModuleDir.exists() && matlabNslModuleDir.isDirectory())
                    {
                        String newMatlabNslModuleDirPath = SCSUtility.getMatlabPathUsingNick(newLibName, "NslModule");
                        File newMatlabNslModuleDir = new File(newMatlabNslModuleDirPath);
                        if (!newMatlabNslModuleDir.exists())
                        {
                            newMatlabNslModuleDir.mkdirs();
                            SCSUtility.copyFolder(matlabNslModuleDir, newMatlabNslModuleDir);
                        }
                    }
                }
                if (deleteAfterPaste)
                {
                    SCSUtility.deleteFile(moduleVersionPath, this);
                }
                reloadLibPaths();
            }
            catch (Exception e)
            {

            }
        }
    }

    // --------------------------------------------------
    /**
     * Create the menu bar.
     */
    public JMenuBar CreateMenuBar()
    {
        JMenuItem mi;

        JMenu FileMenu;
        JMenu HelpMenu;
        JMenu EditMenu;
        JMenuBar MyMenuBar = new JMenuBar();

        // File main menu
        FileMenu = new JMenu("File");
        FileMenu.add(mi = new JMenuItem("New Library"));
        mi.addActionListener(this);
        FileMenu.add(deleteLibrary = new JMenuItem("Delete Library"));
        deleteLibrary.addActionListener(this);
        deleteLibrary.setEnabled(false);
        FileMenu.add(deleteModule = new JMenuItem("Delete Module"));
        deleteModule.addActionListener(this);
        deleteModule.setEnabled(false);
        FileMenu.add(deleteVersion = new JMenuItem("Delete Version"));
        deleteVersion.addActionListener(this);
        deleteVersion.setEnabled(false);
        MyMenuBar.add(FileMenu);

        // Edit main menu
        EditMenu = new JMenu("Edit");
        EditMenu.add(copyModule = new JMenuItem("Copy Module"));
        copyModule.addActionListener(this);
        copyModule.setEnabled(false);
        EditMenu.add(cutModule = new JMenuItem("Cut Module"));
        cutModule.addActionListener(this);
        cutModule.setEnabled(false);
        EditMenu.add(pasteModule = new JMenuItem("Paste Module"));
        pasteModule.addActionListener(this);
        pasteModule.setEnabled(false);
        EditMenu.addSeparator();
        EditMenu.add(copyVersion = new JMenuItem("Copy Version"));
        copyVersion.addActionListener(this);
        copyVersion.setEnabled(false);
        EditMenu.add(cutVersion = new JMenuItem("Cut Version"));
        cutVersion.addActionListener(this);
        cutVersion.setEnabled(false);
        EditMenu.add(pasteVersion = new JMenuItem("Paste Version"));
        pasteVersion.addActionListener(this);
        pasteVersion.setEnabled(false);
        MyMenuBar.add(EditMenu);

        //help menu
        HelpMenu = new JMenu("Help");
        HelpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);
        HelpMenu.add(mi = new JMenuItem("About"));
        mi.addActionListener(this);
        MyMenuBar.add(HelpMenu);
        //MyMenuBar.setHelpMenu(HelpMenu);

        return (MyMenuBar);
    }

    //--------------------------------------------------------------------
    public void reloadLibPaths()
    {
        try
        {
            Vector nickAndLibPathEntries = SCSUtility.readLibraryNickAndPathsFile();
            if (nickAndLibPathEntries == null)
                System.err.println("Error:LibraryManagerFrame:reloadLibPaths: please fix the library file : " +
                        SCSUtility.scs_library_paths_file);
            else
            {
                libraryPaths = SCSUtility.vectorToPathArray(nickAndLibPathEntries);
                libraryNames = SCSUtility.vectorToNickNameArray(nickAndLibPathEntries);
                String[] sortedLibraryNames = new String[libraryNames.length];
                System.arraycopy(libraryNames, 0, sortedLibraryNames, 0, libraryNames.length);
                Arrays.sort(sortedLibraryNames);
                libraryList.setListData(sortedLibraryNames);
                libraryList.repaint();
                moduleList.setListData(new String[]{});
                moduleList.repaint();
                moduleList.validate();
                versionList.setListData(new String[]{});
                versionList.repaint();
                versionList.validate();
                srcDocIoList.setListData(new String[]{});
                srcDocIoList.repaint();
                srcDocIoList.validate();
                fileList.setListData(new String[]{});
                fileList.repaint();
                fileList.validate();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:LibraryManagerFrame:reloadLibPaths: please fix the library file : " +
                    SCSUtility.scs_library_paths_file);

        }
        catch (IOException e)
        {
            System.err.println("Error:LibraryManagerFrame:reloadLibPaths: io exception with : " +
                    SCSUtility.scs_library_paths_file);
        }
    }

    private String returnLibraryPath()
    {
        if (libraryList.getSelectedValue() != null)
        {
            try
            {
                return SCSUtility.getLibPath(libraryList.getSelectedValue().toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String returnModulePath()
    {
        String tempA = returnLibraryPath();
        if (tempA != null && moduleList.getSelectedValue() != null)
            return tempA + File.separator + moduleList.getSelectedValue();
        else
            return null;
    }

    private String returnModuleMatlabPath()
    {
        String tempA = returnLibraryPath();
        if ((tempA != null) && (libraryList.getSelectedValue() != null))
            return (tempA + File.separator + '@' + moduleList.getSelectedValue());
        else
            return (null);
    }

    private String returnVersionPath()
    {
        String tempB = returnModulePath();
        if ((tempB != null) && (versionList.getSelectedValue() != null))
            return (tempB + File.separator + versionList.getSelectedValue());
        else
            return null;
    }

    private String returnSrcDocIOPath()
    {
        String tempC = returnVersionPath();
        if ((tempC != null) && (srcDocIoList.getSelectedValue() != null))
            return (tempC + File.separator + srcDocIoList.getSelectedValue());
        else
            return null;
    }

    public void valueChanged(ListSelectionEvent event)
    {
        if (event.getSource() instanceof JList)
        {
            JList templist = (JList) event.getSource();

            // library selected -- handle module pane
            if (templist.equals(libraryList))
            {
                if (libraryList.getSelectedValue() == null)
                {
                    deleteLibrary.setEnabled(false);
                    pasteModule.setEnabled(false);
                    return;
                }
                else
                    updateLibraryLists();
            }

            // module selected --  handle version pane
            if (templist.equals(moduleList))
            {
                if (moduleList.getSelectedValue() == null)
                {
                    deleteModule.setEnabled(false);
                    cutModule.setEnabled(false);
                    copyModule.setEnabled(false);
                    return;
                }
                else
                {
                    updateModuleLists();
                    versionList.setSelectedIndex(versionList.getModel().getSize() - 1);
                }
            }
            // version selected -- handle file pane
            if (templist.equals(versionList))
            {
                if (versionList.getSelectedValue() == null)
                {
                    deleteVersion.setEnabled(false);
                    copyVersion.setEnabled(false);
                    cutVersion.setEnabled(false);
                    return;
                }
                else
                    updateVersionLists();
            }

            // srcDocIO selected -- handle file pane
            if (templist.equals(srcDocIoList))
            {
                if (srcDocIoList.getSelectedValue() != null)
                    updateDirList();
            }
        }
    }

    private void updateDirList()
    {
        lastSelectedDir = srcDocIoList.getSelectedValue().toString();

        fileScrollPane.getVerticalScrollBar().setValue(fileScrollPane.getVerticalScrollBar().getMinimum());

        File srcDocIOPath = new File(returnSrcDocIOPath());

        fileList.setListData(getVisibleFiles(srcDocIOPath));

        fileList.repaint();
        validate();
    }

    private void updateVersionLists()
    {
        lastSelectedVer = versionList.getSelectedValue().toString();

        srcDocIoScrollPane.getVerticalScrollBar().setValue(srcDocIoScrollPane.getVerticalScrollBar().getMinimum());
        fileScrollPane.getVerticalScrollBar().setValue(fileScrollPane.getVerticalScrollBar().getMinimum());

        deleteVersion.setEnabled(true);
        copyVersion.setEnabled(true);
        cutVersion.setEnabled(true);
        File verPath = new File(returnVersionPath());
        srcDocIoList.setListData(getVisibleDirectories(verPath));
        srcDocIoList.repaint();
        srcDocIoList.validate();
        fileList.setListData(new String[]{});
        fileList.validate();
        fileList.repaint();
    }

    private void updateModuleLists()
    {
        lastSelectedMod = moduleList.getSelectedValue().toString();

        versionScrollPane.getVerticalScrollBar().setValue(versionScrollPane.getVerticalScrollBar().getMinimum());
        srcDocIoScrollPane.getVerticalScrollBar().setValue(srcDocIoScrollPane.getVerticalScrollBar().getMinimum());
        fileScrollPane.getVerticalScrollBar().setValue(fileScrollPane.getVerticalScrollBar().getMinimum());

        deleteModule.setEnabled(true);
        cutModule.setEnabled(true);
        copyModule.setEnabled(true);
        pasteModule.setEnabled(false);
        File modPath = new File(returnModulePath());
        versionList.setListData(getVisibleDirectories(modPath));
        versionList.validate();
        versionList.repaint();
        srcDocIoList.setListData(new String[]{});
        srcDocIoList.repaint();
        srcDocIoList.validate();
        fileList.setListData(new String[]{});
        fileList.validate();
        fileList.repaint();
    }

    private void updateLibraryLists()
    {
        lastSelectedLib = libraryList.getSelectedValue().toString();

        moduleScrollPane.getVerticalScrollBar().setValue(moduleScrollPane.getVerticalScrollBar().getMinimum());
        versionScrollPane.getVerticalScrollBar().setValue(versionScrollPane.getVerticalScrollBar().getMinimum());
        srcDocIoScrollPane.getVerticalScrollBar().setValue(srcDocIoScrollPane.getVerticalScrollBar().getMinimum());
        fileScrollPane.getVerticalScrollBar().setValue(fileScrollPane.getVerticalScrollBar().getMinimum());

        deleteLibrary.setEnabled(true);

        File albumdir = new File(returnLibraryPath());

        //enable paste if module clipboard is not empty
        if (clipboardModulePath != null && clipboardModulePath.length() > 0 && !clipboardLibraryPath.equals(albumdir))
            pasteModule.setEnabled(true);

        if (clipboardModuleVersionPath != null && clipboardModuleVersionPath.length() > 0 && !clipboardLibraryPath.equals(albumdir))
            pasteVersion.setEnabled(true);

        String[] dirs = new String[]{};
        if (albumdir.exists())
            dirs = getVisibleDirectories(albumdir);

        moduleList.setListData(dirs);
        moduleList.repaint();
        moduleList.validate();
        versionList.setListData(new String[]{});
        versionList.repaint();
        versionList.validate();
        srcDocIoList.setListData(new String[]{});
        srcDocIoList.repaint();
        srcDocIoList.validate();
        fileList.setListData(new String[]{});
        fileList.repaint();
        fileList.validate();
    }

    protected String[] getVisibleFiles(File dir)
    {
        String[] files = null;
        if (dir.isDirectory() && dir.exists())
        {
            File[] allFiles = dir.listFiles();
            Vector<String> visibleFiles = new Vector<String>();
            for (int i = 0; i < allFiles.length; i++)
            {
                if (!allFiles[i].isHidden())
                    visibleFiles.add(allFiles[i].getName());
            }
            files = new String[visibleFiles.size()];
            for (int i = 0; i < visibleFiles.size(); i++)
                files[i] = visibleFiles.get(i);
            Arrays.sort(files);
            return files;
        }
        return files;
    }

    protected String[] getVisibleDirectories(File dir)
    {
        String[] files = null;
        if (dir.isDirectory() && dir.exists())
        {
            File[] allFiles = dir.listFiles();
            Vector<String> visibleFiles = new Vector<String>();
            for (int i = 0; i < allFiles.length; i++)
            {
                if (!allFiles[i].isHidden() && allFiles[i].isDirectory() && !allFiles[i].getName().startsWith("@"))
                    visibleFiles.add(allFiles[i].getName());
            }
            files = new String[visibleFiles.size()];
            for (int i = 0; i < visibleFiles.size(); i++)
                files[i] = visibleFiles.get(i);
            Arrays.sort(files);
            return files;
        }
        return files;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        libraryLabel = new JLabel();
        libraryLabel.setText("Library");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(libraryLabel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        libraryScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(libraryScrollPane, gbc);
        libraryList = new JList();
        libraryScrollPane.setViewportView(libraryList);
        moduleLabel = new JLabel();
        moduleLabel.setText("Module");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(moduleLabel, gbc);
        moduleScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(moduleScrollPane, gbc);
        moduleList = new JList();
        moduleScrollPane.setViewportView(moduleList);
        versionLabel = new JLabel();
        versionLabel.setText("Version");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(versionLabel, gbc);
        versionScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.025;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(versionScrollPane, gbc);
        versionList = new JList();
        versionScrollPane.setViewportView(versionList);
        srcDocIoLabel = new JLabel();
        srcDocIoLabel.setText("Src / Doc / Io");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(srcDocIoLabel, gbc);
        srcDocIoScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.025;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(srcDocIoScrollPane, gbc);
        srcDocIoList = new JList();
        srcDocIoScrollPane.setViewportView(srcDocIoList);
        filesLabel = new JLabel();
        filesLabel.setText("Files");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(filesLabel, gbc);
        fileScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(fileScrollPane, gbc);
        fileList = new JList();
        fileScrollPane.setViewportView(fileList);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(buttonPanel, gbc);
        closeButton = new JButton();
        closeButton.setText("Close");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(closeButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
