package scs.ui;

import scs.util.SCSUtility;
import scs.Module;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;

public class NewModuleDialog extends JDialog
{
    protected SchEditorFrame parent;
    protected boolean libSelect;
    protected String pushedBtn = "";
    protected String libraryName;
    protected String libraryPath;
    protected String moduleName;
    protected String versionName;
    protected String type;
    protected String arguments;
    protected boolean buffering = true;
    protected String sourcePath;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel libraryLabel;
    private JComboBox libraryComboBox;
    private JLabel moduleNameLabel;
    private JTextField moduleNameTextField;
    private JLabel versionLabel;
    private JTextField versionTextField;
    private JLabel typeLabel;
    private JComboBox typeComboBox;
    private JCheckBox bufferingCheckBox;
    private JLabel argumentsLabel;
    private JTextField argumentsTextField;

    public NewModuleDialog(SchEditorFrame parent, String title, String modelOrModule, boolean libSelect)
    {
        super(parent, title);
        this.parent = parent;
        this.libSelect = libSelect;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        String[] libList = null;
        try
        {
            libList = SCSUtility.vectorToNickNameArray(SCSUtility.readLibraryNickAndPathsFile());
            Arrays.sort(libList);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:NewModuleDialog:constructor: please fix the library file : " + SCSUtility.scs_library_paths_file);
        }
        catch (IOException e)
        {
            System.err.println("Error:NewModuleDialog:constructor: io exception with : " + SCSUtility.scs_library_paths_file);
        }
        if (libList != null)
        {
            for (int i = 0; i < libList.length; i++)
                libraryComboBox.addItem(libList[i]);
        }
        if (!libSelect)
            libraryComboBox.setEnabled(false);
        if (modelOrModule.equals("Module"))
        {
            typeComboBox.addItem("NslJavaModule");
            typeComboBox.addItem("NslMatlabModule");
            typeComboBox.addItem("NslClass");
        }
        else
        {
            typeComboBox.addItem("NslModel");
        }
    }

    private void onOK()
    {
        if (libSelect)
        {
            if (libraryComboBox.getSelectedIndex() < 0)
            {
                JOptionPane.showMessageDialog(parent, "Library must be selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            libraryName = libraryComboBox.getSelectedItem().toString();
        }
        try
        {
            libraryPath = SCSUtility.getLibPath(libraryName);
        }
        catch (IOException e)
        {
            System.err.println("Error:NewModuleDialog: library list is corrupt.");
        }

        moduleName = moduleNameTextField.getText();
        if (moduleName == null || moduleName.length() == 0)
        {
            JOptionPane.showMessageDialog(parent, "Module name must be specified", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Character.isUpperCase(moduleName.charAt(0)))
        {
            JOptionPane.showMessageDialog(parent, "First letter in module name must be capitalized", "Error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        versionName = versionTextField.getText();
        if (versionName == null || versionName.length() == 0)
        {
            JOptionPane.showMessageDialog(parent, "Version name must be specified", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //now to check the module within the file
        String srcPath = SCSUtility.getFullPathToSrc(libraryPath, moduleName, versionName);
        //Check if src dir already exists
        File srcFile = new File(srcPath);
        sourcePath = srcFile.getAbsolutePath();
        if (srcFile.isDirectory())
        {
            //else if the directory exists
            Module tempModule;
            try
            {
                String sifPath = SCSUtility.getFullPathToSif(srcPath, moduleName);
                String xmlPath = SCSUtility.getFullPathToXml(srcPath, moduleName);
                tempModule = new Module();
                if ((new File(xmlPath)).exists())
                {
                    FileInputStream fis = new FileInputStream(xmlPath);
                    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                    domFactory.setNamespaceAware(true);

                    DocumentBuilder builder = domFactory.newDocumentBuilder();
                    Document doc = builder.parse(fis);
                    tempModule.readXML(doc);
                    fis.close();
                }
                else
                {
                    FileInputStream fis = new FileInputStream(sifPath);
                    ObjectInputStream mois = new ObjectInputStream(fis); //lots of exceptions
                    tempModule.read(mois); //must catch exceptions
                    fis.close();
                }
            }
            catch (IOException e)
            {
                String errstr = "IOException";
                JOptionPane.showMessageDialog(parent, errstr, "New Module Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            catch (ParserConfigurationException e)
            {
                String errstr = "ParserConfigurationException";
                JOptionPane.showMessageDialog(parent, errstr, "New Module Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            catch (SAXException e)
            {
                String errstr = "SAXException";
                JOptionPane.showMessageDialog(parent, errstr, "New Module Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            catch (ClassNotFoundException e)
            {
                String errstr = "ClassNotFoundException";
                JOptionPane.showMessageDialog(parent, errstr, "New Module Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tempModule.mySchematic != null)
            {
                String errstr = "Cannot create a new module  by that name. Use -open- instead.";
                JOptionPane.showMessageDialog(parent, errstr, "New Module Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        type = typeComboBox.getSelectedItem().toString();
        buffering = bufferingCheckBox.isSelected();
        arguments = argumentsTextField.getText();
        pushedBtn = "ok";
        dispose();
    }

    private void onCancel()
    {
        pushedBtn = "cancel";
        dispose();
    }

    public String getPushedBtn()
    {
        return pushedBtn;
    }

    public String getLibraryName()
    {
        return libraryName;
    }

    public void setLibraryName(String l)
    {
        libraryName = l;
        libraryComboBox.setSelectedItem(libraryName);
    }

    public String getLibraryPath()
    {
        return libraryPath;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public String getArguments()
    {
        return arguments;
    }

    public void setArguments(String a)
    {
        this.arguments = a;
        argumentsTextField.setText(arguments);
    }

    public boolean isBuffering()
    {
        return buffering;
    }

    public void setBuffering(boolean b)
    {
        this.buffering = b;
        bufferingCheckBox.setSelected(buffering);
    }

    public String getSourcePath()
    {
        return sourcePath;
    }

    public String getType()
    {
        return type;
    }

    public void addType(String t)
    {
        typeComboBox.addItem(t);
    }

    public void setType(String t)
    {
        this.type = t;
        boolean contains = false;
        for (int i = 0; i < typeComboBox.getModel().getSize(); i++)
        {
            if (typeComboBox.getModel().getElementAt(i).equals(type))
            {
                contains = true;
                break;
            }
        }
        if (!contains)
            typeComboBox.addItem(type);
        typeComboBox.setSelectedItem(type);
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
        libraryLabel = new JLabel();
        libraryLabel.setText("Library:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(libraryLabel, gbc);
        libraryComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(libraryComboBox, gbc);
        moduleNameLabel = new JLabel();
        moduleNameLabel.setText("Module Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleNameLabel, gbc);
        moduleNameTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(moduleNameTextField, gbc);
        versionLabel = new JLabel();
        versionLabel.setText("Version:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(versionLabel, gbc);
        versionTextField = new JTextField();
        versionTextField.setText("1_1_1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(versionTextField, gbc);
        typeLabel = new JLabel();
        typeLabel.setText("Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(typeLabel, gbc);
        typeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(typeComboBox, gbc);
        argumentsLabel = new JLabel();
        argumentsLabel.setText("Arguments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(argumentsLabel, gbc);
        argumentsTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(argumentsTextField, gbc);
        bufferingCheckBox = new JCheckBox();
        bufferingCheckBox.setSelected(true);
        bufferingCheckBox.setText("Double Buffering");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(bufferingCheckBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer1, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
