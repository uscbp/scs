package scs.ui;

import scs.Module;
import scs.Declaration;
import scs.graphics.SchematicPort;
import scs.graphics.IconInst;
import scs.graphics.IconPort;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeclarationDialog extends JDialog implements ActionListener, ListSelectionListener
{
    public boolean okPressed = false;
    protected JFrame parentFrame;
    protected String dialogType;
    protected Module currentModule;
    protected DefaultListModel typeListModel;
    protected int dimensions = 0;
    private String varType;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel moduleNameLabelLabel;
    private JLabel moduleNameLabel;
    private JLabel varNameLabelLabel;
    private JLabel varNameLabel;
    private JLabel varAccessLabel;
    private JRadioButton privateRadioButton;
    private JRadioButton publicRadioButton;
    private JRadioButton protectedRadioButton;
    private JLabel varScopeLabel;
    private JRadioButton localRadioButton;
    private JRadioButton staticRadioButton;
    private JCheckBox constantCheckBox;
    private JLabel varTypeLabel;
    private JList varTypeList;
    private JLabel dimensionsLabel;
    private JRadioButton zeroDimRadioButton;
    private JRadioButton oneDimRadioButton;
    private JRadioButton twoDimRadioButton;
    private JRadioButton threeDimRadioButton;
    private JRadioButton fourDimRadioButton;
    private JRadioButton higherDimRadioButton;
    private JLabel varParametersLabel;
    private JTextField varParametersTextField;
    private JTextField varInitializationTextField;
    private JLabel varInitializationLabel;
    private JLabel commentsLabel;
    private JTextField commentsTextField;
    private JScrollPane varTypeScrollPane;

    public DeclarationDialog(JFrame fm, Module module, String dialogType)
    {
        super(fm, "Declaration Dialog", true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        this.parentFrame = fm;
        this.dialogType = dialogType;
        this.currentModule = module;

        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);

        typeListModel = new DefaultListModel();
        addBuiltInCategoryTypes(typeListModel);
        varTypeList.setModel(typeListModel);
        varTypeList.setSelectedIndex(0);
        varType = varTypeList.getSelectedValue().toString();
        varTypeList.addListSelectionListener(this);

        zeroDimRadioButton.addActionListener(this);
        oneDimRadioButton.addActionListener(this);
        twoDimRadioButton.addActionListener(this);
        threeDimRadioButton.addActionListener(this);
        fourDimRadioButton.addActionListener(this);
        higherDimRadioButton.addActionListener(this);
    }

    public void init(Declaration inVar)
    {
        if (inVar != null)
        {
            if (!dialogType.equals(inVar.varDialogType))
            {
                String errstr = "DialogType and input variable dialog type do not agree.";
                JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dialogType = inVar.varDialogType; //not displayed

            if (currentModule == null)
                moduleNameLabel.setText("");
            else
                moduleNameLabel.setText(currentModule.moduleName);

            if (inVar.varName != null)
                varNameLabel.setText(inVar.varName);

            String access = "private";
            if (inVar.varAccess != null)
                access = inVar.varAccess;
            boolean[] accessArray = createAccessArray(access);
            privateRadioButton.setSelected(accessArray[0]);
            publicRadioButton.setSelected(accessArray[1]);
            protectedRadioButton.setSelected(accessArray[2]);

            String scope = "local";
            if (inVar.varScope != null)
                scope = inVar.varScope;
            boolean[] scopeArray = createScopeArray(scope);
            localRadioButton.setSelected(scopeArray[0]);
            staticRadioButton.setSelected(scopeArray[1]);

            //constant
            constantCheckBox.setSelected(inVar.varConstant);

            String categoryType = "double";
            if (inVar.varCategoryType != null && inVar.varCategoryType.length() > 0)
                categoryType = inVar.varCategoryType;
            setSelectionCategoryType(varTypeList, categoryType);

            boolean[] dimensions = createDimensionsArray(inVar.varDimensions);
            zeroDimRadioButton.setSelected(dimensions[0]);
            oneDimRadioButton.setSelected(dimensions[1]);
            twoDimRadioButton.setSelected(dimensions[2]);
            threeDimRadioButton.setSelected(dimensions[3]);
            fourDimRadioButton.setSelected(dimensions[4]);
            higherDimRadioButton.setSelected(dimensions[5]);
            this.dimensions = inVar.varDimensions;
            if (inVar.varParams != null)
                varParametersTextField.setText(inVar.varParams);
            if (inVar.varInits != null)
                varInitializationTextField.setText(inVar.varInits);
            if (inVar.varComment != null)
                commentsTextField.setText(inVar.varComment);
        }
    }

    public void valueChanged(ListSelectionEvent event)
    {
        JList list;
        String selected;
        if (event.getSource() instanceof JList)
        {
            list = (JList) event.getSource();
            selected = list.getSelectedValue().toString();
            if (list == varTypeList)
            {
                if (selected.equals("other"))
                {
                    String otherTypeStr;
                    if (varType != null && varType.length() > 0)
                    {
                        otherTypeStr = JOptionPane.showInputDialog(null, "other: ", "Var Type", JOptionPane.QUESTION_MESSAGE,
                                null, null, varType).toString();
                    }
                    else
                    {
                        otherTypeStr = JOptionPane.showInputDialog(null, "other: ", "Var Type", JOptionPane.QUESTION_MESSAGE);
                    }
                    //cancel done
                    if (otherTypeStr != null && otherTypeStr.length() == 0)
                        varType = "";
                    else if (otherTypeStr != null)
                        varType = otherTypeStr.trim();
                }
                else
                    varType = varTypeList.getSelectedValue().toString();
            }
        }
    }

    public Declaration fillDeclarationInfo(Declaration outVar)
    {
        if (publicRadioButton.isSelected())
            outVar.varAccess = "public";
        else if (privateRadioButton.isSelected())
            outVar.varAccess = "private";
        else
            outVar.varAccess = "protected";

        if (localRadioButton.isSelected())
            outVar.varScope = "local";
        else
            outVar.varScope = "static";

        outVar.varConstant = constantCheckBox.isSelected();

        outVar.varCategoryType = varType;
        outVar.varDimensions = dimensions;
        if (outVar.varCategoryType.equals("NslDouble") || outVar.varCategoryType.equals("NslFloat") ||
                outVar.varCategoryType.equals("NslInt") || outVar.varCategoryType.equals("NslString") ||
                outVar.varCategoryType.equals("NslBoolean"))
            outVar.varType = outVar.varCategoryType + outVar.varDimensions;
        else if (!outVar.varCategoryType.equals("other"))
            outVar.varType = outVar.varCategoryType;

        outVar.varParams = varParametersTextField.getText();
        outVar.varInits = varInitializationTextField.getText();
        outVar.varDialogType = dialogType;
        outVar.varComment = commentsTextField.getText();
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

    protected static void addBuiltInCategoryTypes(DefaultListModel model)
    {
        String nameStr;
        String prefix = "Nsl";
        nameStr = "double";
        model.addElement(nameStr);
        nameStr = "float";
        model.addElement(nameStr);
        nameStr = "int";
        model.addElement(nameStr);
        nameStr = "boolean";
        model.addElement(nameStr);
        nameStr = "char";
        model.addElement(nameStr);
        nameStr = "charString";
        model.addElement(nameStr);
        nameStr = prefix + "Double";
        model.addElement(nameStr);
        nameStr = prefix + "Float";
        model.addElement(nameStr);
        nameStr = prefix + "Int";
        model.addElement(nameStr);
        nameStr = prefix + "Boolean";
        model.addElement(nameStr);
        nameStr = prefix + "String";
        model.addElement(nameStr);
        nameStr = "other";
        model.addElement(nameStr);
    }

    protected static boolean[] createAccessArray(String access)
    {
        boolean[] blist = new boolean[4];

        blist[0] = (access.equals("private"));
        blist[1] = (access.equals("public"));
        blist[2] = (access.equals("protected"));
        blist[3] = !((blist[0]) || (blist[1]) || (blist[2]));

        return (blist);
    }

    protected static boolean[] createScopeArray(String scope)
    {
        boolean[] blist = new boolean[2];

        blist[0] = (scope.equals("local"));
        blist[1] = (scope.equals("static"));

        return (blist);
    }

    protected static boolean[] createDimensionsArray(int varDimensions)
    {
        //0,1,2,3,4,higher
        boolean[] blist = new boolean[6];
        int i;
        for (i = 0; i < 5; i++)
        {
            blist[i] = (varDimensions == i);
        }
        blist[5] = (varDimensions > 4);
        return (blist);
    }

    protected static void setSelectionCategoryType(JList mylist, String categoryType)
    {
        mylist.setSelectedValue(categoryType, true);//should scroll
    }

    public void actionPerformed(ActionEvent event)
    {
        // now do buttons
        if (event.getSource() instanceof JRadioButton)
        {
            JRadioButton jradiobutton = (JRadioButton) event.getSource();
            if (jradiobutton.equals(zeroDimRadioButton))
                dimensions = 0;
            else if (jradiobutton.equals(oneDimRadioButton))
                dimensions = 1;
            else if (jradiobutton.equals(twoDimRadioButton))
                dimensions = 2;
            else if (jradiobutton.equals(threeDimRadioButton))
                dimensions = 3;
            else if (jradiobutton.equals(fourDimRadioButton))
                dimensions = 4;
            else if (jradiobutton.equals(higherDimRadioButton))
            {
                //center in middle due to the null frame first param
                String higherDimValue = (String) JOptionPane.showInputDialog(null, "higher dimension: ", "Dimension",
                        JOptionPane.QUESTION_MESSAGE, null, null, Integer.toString(dimensions));
                if (higherDimValue != null && higherDimValue.length() == 0)
                    dimensions = 0;
                else if (higherDimValue != null)
                    dimensions = Integer.parseInt(higherDimValue.trim());
            }
        }
        else if (event.getSource() instanceof JButton)
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
        }
    }

    protected boolean verifyInput()
    {
        //todo: check for bad letters
        // Only need to specify dimension parameters with Nsl types
        if (varTypeList.getSelectedValue().toString().startsWith("Nsl") && dimensions > 0 &&
                varParametersTextField.getText().length() == 0)
        {
            //this needs the tokenizer to check the parameters
            String errstr = "Please provide the parameters for the dimensions specified.";
            JOptionPane.showMessageDialog(parentFrame, errstr, "Declaration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    } //end verifyInput

    public void display(Declaration var)
    {
        init(var);
        setLocation(400, 400);
        pack();
        setSize(400, 500);
        setVisible(true);
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
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varNameLabel, gbc);
        varAccessLabel = new JLabel();
        varAccessLabel.setText("Variable Access:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varAccessLabel, gbc);
        privateRadioButton = new JRadioButton();
        privateRadioButton.setText("private");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(privateRadioButton, gbc);
        varScopeLabel = new JLabel();
        varScopeLabel.setText("Variable Scope:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varScopeLabel, gbc);
        localRadioButton = new JRadioButton();
        localRadioButton.setText("local");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(localRadioButton, gbc);
        constantCheckBox = new JCheckBox();
        constantCheckBox.setText("constant");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(constantCheckBox, gbc);
        varTypeLabel = new JLabel();
        varTypeLabel.setText("Variable Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(varTypeLabel, gbc);
        varTypeScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 6;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(varTypeScrollPane, gbc);
        varTypeList = new JList();
        varTypeScrollPane.setViewportView(varTypeList);
        dimensionsLabel = new JLabel();
        dimensionsLabel.setText("Dimensions:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(dimensionsLabel, gbc);
        zeroDimRadioButton = new JRadioButton();
        zeroDimRadioButton.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(zeroDimRadioButton, gbc);
        oneDimRadioButton = new JRadioButton();
        oneDimRadioButton.setText("1");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(oneDimRadioButton, gbc);
        twoDimRadioButton = new JRadioButton();
        twoDimRadioButton.setText("2");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(twoDimRadioButton, gbc);
        threeDimRadioButton = new JRadioButton();
        threeDimRadioButton.setText("3");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(threeDimRadioButton, gbc);
        fourDimRadioButton = new JRadioButton();
        fourDimRadioButton.setText("4");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(fourDimRadioButton, gbc);
        higherDimRadioButton = new JRadioButton();
        higherDimRadioButton.setText("higher");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(higherDimRadioButton, gbc);
        protectedRadioButton = new JRadioButton();
        protectedRadioButton.setText("protected");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(protectedRadioButton, gbc);
        publicRadioButton = new JRadioButton();
        publicRadioButton.setText("public");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(publicRadioButton, gbc);
        staticRadioButton = new JRadioButton();
        staticRadioButton.setText("static");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(staticRadioButton, gbc);
        varParametersLabel = new JLabel();
        varParametersLabel.setText("Variable Parameters:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varParametersLabel, gbc);
        varParametersTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varParametersTextField, gbc);
        varInitializationTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varInitializationTextField, gbc);
        varInitializationLabel = new JLabel();
        varInitializationLabel.setText("Variable Initialization:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varInitializationLabel, gbc);
        commentsLabel = new JLabel();
        commentsLabel.setText("Comments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(commentsLabel, gbc);
        commentsTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(commentsTextField, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(privateRadioButton);
        buttonGroup.add(publicRadioButton);
        buttonGroup.add(protectedRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(localRadioButton);
        buttonGroup.add(staticRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(zeroDimRadioButton);
        buttonGroup.add(oneDimRadioButton);
        buttonGroup.add(twoDimRadioButton);
        buttonGroup.add(threeDimRadioButton);
        buttonGroup.add(fourDimRadioButton);
        buttonGroup.add(higherDimRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
