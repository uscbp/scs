package scs.ui;

import scs.Module;
import scs.Declaration;
import scs.graphics.SchematicPort;
import scs.graphics.IconInst;
import scs.graphics.IconPort;
import scs.util.SCSUtility;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.StringTokenizer;

import org.xml.sax.SAXException;

public class SubmoduleDialog extends DeclarationDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel moduleNameLabelLabel;
    private JLabel moduleNameLabel;
    private JLabel varNameLabelLabel;
    private JLabel varNameLabel;
    private JLabel varParametersLabel;
    private JTextField varParametersTextField;
    private JTextField varInitializationTextField;
    private JLabel varInitializationLabel;
    private JLabel commentsLabel;
    private JTextField commentsTextField;
    private JButton chooseFileButton;
    private JLabel chooseLabel;
    private JLabel libraryNameLabel;
    private JTextField libraryNameTextField;
    private JLabel libraryPathLabel;
    private JTextField libraryPathTextField;
    private JLabel submoduleNameLabel;
    private JTextField submoduleNameTextField;
    private JLabel submoduleVersionLabel;
    private JTextField submoduleVersionTextField;

    public SubmoduleDialog(JFrame fm, Module module, String dialogType)
    {
        super(fm, module, dialogType);
        setTitle("Submodule Dialog");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);
        chooseFileButton.addActionListener(this);
    }

    public void init(Declaration inVar)
    {
        if (inVar != null)
        {
            if (!(dialogType.equals(inVar.varDialogType)))
            {
                String errstr = "DialogType and input variable dialog type do not agree.";
                JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            }
            dialogType = inVar.varDialogType; //not displayed

            if (currentModule == null)
                moduleNameLabel.setText("");
            else
                moduleNameLabel.setText(currentModule.moduleName);

            if (inVar.varName != null)
                varNameLabel.setText(inVar.varName);

            if (inVar.varParams != null)
                varParametersTextField.setText(inVar.varParams);

            if (inVar.varInits != null)
                varInitializationTextField.setText(inVar.varInits);

            if (inVar.varComment != null)
                commentsTextField.setText(inVar.varComment);

            submoduleNameTextField.setText(inVar.varType);

            libraryNameTextField.setText(inVar.modLibNickName);

            submoduleVersionTextField.setText(inVar.modVersion);

            if (inVar.modLibNickName.length() > 0)
            {
                try
                {
                    libraryPathTextField.setText(SCSUtility.getLibPath(inVar.modLibNickName));
                }
                catch (IOException ioe)
                {
                    String errstr = "IO error while looking for " + inVar.modLibNickName;
                    JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public Declaration fillDeclarationInfo(Declaration outVar)
    {
        outVar.varAccess = "public";
        outVar.varScope = "local";
        outVar.varConstant = false;
        outVar.varCategoryType = submoduleNameTextField.getText();
        outVar.varDimensions = 0;
        outVar.varType = submoduleNameTextField.getText();
        outVar.varParams = varParametersTextField.getText();
        outVar.varInits = varInitializationTextField.getText();
        outVar.varDialogType = dialogType;
        outVar.varComment = commentsTextField.getText();
        //path does not get copied to the variable
        outVar.modLibNickName = libraryNameTextField.getText();
        outVar.modVersion = submoduleVersionTextField.getText();

        Component comp = currentModule.mySchematic.getDrawableObject(outVar.varName);
        if (comp != null)
        {
            if (comp instanceof SchematicPort)
            {
                ((SchematicPort) comp).Type = outVar.varType;
            }
            else if (comp instanceof IconInst)
            {
                ((IconInst) comp).moduleName = outVar.varType;
            }
        }
        if (currentModule.myIcon != null)
        {
            IconPort port = currentModule.myIcon.getDrawableObject(outVar.varName);
            if (port != null)
            {
                port.Type = outVar.varType;
            }
        }
        return outVar;
    }

    public void actionPerformed(ActionEvent event)
    {
        // now do buttons
        if (event.getSource() instanceof JButton)
        {
            JButton jbutton = (JButton) event.getSource();

            if (jbutton.equals(buttonOK))
            {
                if (verifyInput())
                {
                    okPressed = true;
                    dispose();
                }
            }
            else if (jbutton.equals(buttonCancel))
            {
                okPressed = false;
                dispose();
            }
            else if (jbutton.equals(chooseFileButton))
            {
                try
                {
                    ModuleSelectorDialog selectorDialog = new ModuleSelectorDialog(parentFrame);
                    selectorDialog.setLocation(new Point(300, 200));
                    selectorDialog.pack();
                    selectorDialog.setSize(700, 300);
                    selectorDialog.setVisible(true);
                    if (selectorDialog.getPushedBtn().equals("ok"))
                    {
                        libraryNameTextField.setText(selectorDialog.getLibraryName());
                        libraryPathTextField.setText(selectorDialog.getLibraryPath());
                        submoduleNameTextField.setText(selectorDialog.getModuleName());
                        submoduleVersionTextField.setText(selectorDialog.getVersionName());
                    }
                }
                catch (IOException e)
                {
                    String errstr = "Input/Output exception";
                    JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    protected boolean verifyInput()
    {
        Module varMod = new Module();
        String libPath = libraryPathTextField.getText();
        String modType = submoduleNameTextField.getText();
        String modVersion = submoduleVersionTextField.getText();
        String params = varParametersTextField.getText();

        try
        {
            if (libPath != null && libPath.length() > 0 && modType != null && modType.length() > 0 &&
                    modVersion != null && modVersion.length() > 0)
            {
                varMod.getModuleFromFile(libPath, modType, modVersion);
                StringTokenizer requiredArgs = new StringTokenizer(varMod.arguments.replace(", ", ","), ",");
                StringTokenizer givenArgs = new StringTokenizer(params.replace(", ", ","), ",");
                if (requiredArgs.countTokens() != givenArgs.countTokens())
                {
                    String errstr = varMod.moduleName + " requires (" + varMod.arguments + ") as parameters";
                    JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            String errstr = "Class Not Found: " + modType;
            JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch (IOException e)
        {
            String errstr = "IOException on " + modType;
            JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch (ParserConfigurationException e)
        {
            String errstr = "ParserConfigurationException on : " + modType;
            JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        catch (SAXException e)
        {
            String errstr = "SAXException on " + modType;
            JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void display(Declaration var)
    {
        init(var);
        setLocation(400, 400);
        pack();
        setSize(400, 325);
        setVisible(true);
    }

    public String getLibraryPath()
    {
        return libraryPathTextField.getText();
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
        moduleNameLabelLabel = new JLabel();
        moduleNameLabelLabel.setText("Module Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleNameLabelLabel, gbc);
        moduleNameLabel = new JLabel();
        moduleNameLabel.setText("name");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleNameLabel, gbc);
        varNameLabelLabel = new JLabel();
        varNameLabelLabel.setText("Variable Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varNameLabelLabel, gbc);
        varNameLabel = new JLabel();
        varNameLabel.setText("name");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varNameLabel, gbc);
        varParametersLabel = new JLabel();
        varParametersLabel.setText("Variable Parameters:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varParametersLabel, gbc);
        varParametersTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varParametersTextField, gbc);
        varInitializationTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varInitializationTextField, gbc);
        varInitializationLabel = new JLabel();
        varInitializationLabel.setText("Variable Initialization:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varInitializationLabel, gbc);
        commentsLabel = new JLabel();
        commentsLabel.setText("Comments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(commentsLabel, gbc);
        commentsTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(commentsTextField, gbc);
        chooseFileButton = new JButton();
        chooseFileButton.setText("Choose File");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        panel3.add(chooseFileButton, gbc);
        chooseLabel = new JLabel();
        chooseLabel.setText("Choose file or fill in below:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(chooseLabel, gbc);
        libraryNameLabel = new JLabel();
        libraryNameLabel.setText("Library Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(libraryNameLabel, gbc);
        libraryNameTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(libraryNameTextField, gbc);
        libraryPathLabel = new JLabel();
        libraryPathLabel.setText("Library Path:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(libraryPathLabel, gbc);
        libraryPathTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 7;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(libraryPathTextField, gbc);
        submoduleNameLabel = new JLabel();
        submoduleNameLabel.setText("Submodule Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(submoduleNameLabel, gbc);
        submoduleNameTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 8;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(submoduleNameTextField, gbc);
        submoduleVersionLabel = new JLabel();
        submoduleVersionLabel.setText("Submodule Version:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(submoduleVersionLabel, gbc);
        submoduleVersionTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 9;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(submoduleVersionTextField, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
