package scs.ui;

import scs.Module;
import scs.util.SCSUtility;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Iterator;

public class ModuleSelectorDialog extends JDialog implements ListSelectionListener
{
    protected String libraryPath = "";
    protected String libraryName = "";
    protected String moduleName = "";
    protected String versionName = "";
    protected String srcPath = "";
    protected String pushedBtn = "";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel libraryListLabel;
    private JLabel moduleListLabel;
    private JLabel versionListLabel;
    private JScrollPane libraryListScrollPane;
    private JScrollPane moduleListScrollPane;
    private JScrollPane versionListScrollPane;
    private JList libraryList;
    private JList moduleList;
    private JList versionList;

    public ModuleSelectorDialog(JFrame fm)
            throws IOException
    {
        super(fm, "Select Module");

        initGUI();

        initSelector("", null);
    }

    public ModuleSelectorDialog(JFrame fm, String inDirStr)
            throws IOException
    {
        super(fm, "Select Module From Table", true);

        initGUI();

        initSelector(inDirStr, null);
    }

    public ModuleSelectorDialog(JFrame fm, String inDirStr, Module inModule)
            throws IOException
    {
        super(fm, "Select Module From Table", true);

        initGUI();

        initSelector(inDirStr, inModule);
    }

    protected void initGUI()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.setEnabled(false);
        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void initSelector(String inDirStr, Module inModule) throws IOException
    {
        try
        {
            Vector libraries = SCSUtility.readLibraryNickAndPathsFile();
            String[] libraryNickNames = SCSUtility.vectorToNickNameArray(libraries);
            SortedSet<String> sortedLibraries = new TreeSet<String>();
            for (int i = 0; i < libraryNickNames.length; i++)
            {
                sortedLibraries.add(libraryNickNames[i]);
            }
            libraryList.setListData(sortedLibraries.toArray());
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:ModuleSelectorDialog:constructor:2 File not fould exception : " +
                    SCSUtility.scs_library_paths_file);
            throw (new FileNotFoundException());
        }
        catch (IOException e)
        {
            System.err.println("Error:ModuleSelectorDialog:constructor:3 io exception with : " +
                    SCSUtility.scs_library_paths_file);
            throw (new IOException());
        }

        libraryList.addListSelectionListener(this);
        //libraryList.setPreferredSize(new Dimension(300, (getFontMetrics(getFont()).getHeight()+5)*(libraryNickNames.length)));
        libraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //libraryList.setValueIsAdjusting(true);

        moduleList.addListSelectionListener(this);
        //moduleList.setPreferredSize(new Dimension(200, 900));
        moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        versionList.addListSelectionListener(this);
        //versionList.setPreferredSize(new Dimension(100, 400));
        versionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //now set the JList values
        if (inDirStr != null && inDirStr.length() > 0)
            libraryList.setSelectedValue(inDirStr, true);//scroll
        if (inModule != null && inModule.moduleName.length() > 0)
            moduleList.setSelectedValue(inModule.moduleName, true);//scroll
        if (inModule != null && inModule.versionName.length() > 0)
            versionList.setSelectedValue(inModule.versionName, true);//scroll
    }

    public void valueChanged(ListSelectionEvent event)
    {
        if (event.getSource() instanceof JList)
        {
            JList templist = (JList) event.getSource();

            // library selected -- handle module pane
            if (templist.equals(libraryList))
            {
                if (libraryList.getSelectedValue() != null)
                {
                    try
                    {
                        Vector<String> moduleData=returnDirList(SCSUtility.getLibPath(libraryList.getSelectedValue().toString()));
                        moduleList.setListData(moduleData);
                        moduleList.setPreferredSize(new Dimension(200, (getFontMetrics(moduleList.getFont()).getHeight()+10)*moduleData.size()));
                    }
                    catch (IOException e)
                    {
                    }
                    //moduleList.setVisibleRowCount(maxElementsInDir / 3);
                    moduleList.repaint();
                    moduleList.validate();
                    moduleListScrollPane.getViewport().setView(moduleList);

                    versionList.setListData(new String[]{});
                    versionList.repaint();
                    versionList.validate();

                    buttonOK.setEnabled(false);
                }
            }

            // module selected --  handle version pane
            else if (templist.equals(moduleList))
            {
                if (moduleList.getSelectedValue() != null)
                {
                    try
                    {
                        Vector<String> versiondata = returnDirList(new File(SCSUtility.getLibPath(libraryList.getSelectedValue().toString()),
                                                                            moduleList.getSelectedValue().toString()).getAbsolutePath());
                        for (int i = 0; i < versiondata.size(); i++)
                        {
                            if (versiondata.get(i) != null && versiondata.get(i).length() > 0 &&
                                    versiondata.get(i).charAt(0) == 'v')
                            {
                                versiondata.set(i, versiondata.get(i).substring(1));
                            }
                        }
                        versionList.setListData(versiondata);
                        if (versiondata.size() > 0)
                        {
                            versionList.setSelectedIndex(versiondata.size() - 1);
                            buttonOK.setEnabled(true);
                        }
                    }
                    catch (IOException e)
                    {
                    }
                    //versionList.setPreferredSize(new Dimension(100, (getFontMetrics(getFont()).getHeight()+5)*versiondata.length));
                    //versionList.setVisibleRowCount(maxElementsInDir / 3);
                    versionList.validate();
                    versionList.repaint();
                    versionListScrollPane.getViewport().setView(versionList);
                }
            }
            else if (templist.equals(versionList))
                buttonOK.setEnabled(true);
        }
    }

    private static Vector<String> returnDirList(String parentdirpath)
    {
        // --- convert to function
        Vector<String> resultList = new Vector<String>();
        resultList.add("");
        File albumdir = new File(parentdirpath);

        if (albumdir.isDirectory())
        {
            String fileList[] = albumdir.list();
            SortedSet<String> sortedList = new TreeSet<String>();
            JFileChooser chooser = new JFileChooser();
            for (int i = 0; i < fileList.length; i++)
            {
                File tempfile = new File(parentdirpath, fileList[i]);
                if (tempfile.isDirectory() && !chooser.getFileSystemView().isHiddenFile(tempfile) &&
                        albumdir.getName().length() > 0 && tempfile.getName().indexOf('@') != 0)
                    sortedList.add(fileList[i]);
            }
            Iterator iter = sortedList.iterator();
            while (iter.hasNext())
                resultList.add(iter.next().toString());
        }
        return resultList;
    }

    private void onOK()
    {
        libraryName = libraryList.getSelectedValue().toString();
        try
        {
            libraryPath = SCSUtility.getLibPath(libraryName);
        }
        catch (IOException e)
        {
            System.err.println("Error:ModuleSelectorDialog: library list is corrupt.");
        }
        moduleName = moduleList.getSelectedValue().toString();
        versionName = versionList.getSelectedValue().toString();
        srcPath = libraryPath + File.separator + moduleName + File.separator + "v" + versionName + File.separator + "src";
        setVisible(false);
        pushedBtn = "ok";
        dispose();
    }

    private void onCancel()
    {
        pushedBtn = "cancel";
        dispose();
    }

    public String getLibraryPath()
    {
        return libraryPath;
    }

    public String getSourcePath()
    {
        return srcPath;
    }

    public String getPushedBtn()
    {
        return pushedBtn;
    }

    public String getLibraryName()
    {
        return libraryName;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public String getSrcPath()
    {
        return srcPath;
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        buttonOK = new JButton();
        buttonOK.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonCancel, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel3, gbc);
        libraryListScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(libraryListScrollPane, gbc);
        libraryList = new JList();
        libraryListScrollPane.setViewportView(libraryList);
        moduleListScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(moduleListScrollPane, gbc);
        moduleList = new JList();
        moduleListScrollPane.setViewportView(moduleList);
        versionListScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(versionListScrollPane, gbc);
        versionList = new JList();
        versionListScrollPane.setViewportView(versionList);
        libraryListLabel = new JLabel();
        libraryListLabel.setText("Library");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(libraryListLabel, gbc);
        moduleListLabel = new JLabel();
        moduleListLabel.setText("Module");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleListLabel, gbc);
        versionListLabel = new JLabel();
        versionListLabel.setText("Version");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(versionListLabel, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
