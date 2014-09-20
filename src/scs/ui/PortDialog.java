package scs.ui;

import scs.Module;
import scs.Declaration;
import scs.graphics.SchematicPort;
import scs.graphics.IconInst;
import scs.graphics.IconPort;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortDialog extends DeclarationDialog implements ListSelectionListener, ActionListener
{
    protected char iconDir;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel moduleNameLabelLabel;
    private JLabel moduleNameLabel;
    private JLabel varNameLabelLabel;
    private JLabel varNameLabel;
    private JLabel portVarTypeLabel;
    private JLabel portDirectionLabel;
    private JRadioButton leftRightRadioButton;
    private JRadioButton rightLeftRadioButton;
    private JRadioButton topBottomRadioButton;
    private JRadioButton bottomTopRadioButton;
    private JCheckBox portBufferingCheckBox;
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

    public PortDialog(JFrame fm, Module module, String dialogType)
    {
        super(fm, module, dialogType);
        setTitle("Port Dialog");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(this);
        buttonCancel.addActionListener(this);

        if (dialogType.equals("InputPort") || dialogType.equals("OutputPort"))
        {
            typeListModel = new DefaultListModel();
            if (dialogType.equals("InputPort"))
            {
                setTitle("Input Port Dialog");
                addPortCategoryTypes(typeListModel, "Din");
            }
            if (dialogType.equals("OutputPort"))
            {
                setTitle("Output Port Dialog");
                addPortCategoryTypes(typeListModel, "Dout");
            }
            varTypeList.setModel(typeListModel);
            varTypeList.setSelectedIndex(0);
            varTypeList.addListSelectionListener(this);

            leftRightRadioButton.addActionListener(this);
            rightLeftRadioButton.addActionListener(this);
            topBottomRadioButton.addActionListener(this);
            bottomTopRadioButton.addActionListener(this);

            zeroDimRadioButton.addActionListener(this);
            oneDimRadioButton.addActionListener(this);
            twoDimRadioButton.addActionListener(this);
            threeDimRadioButton.addActionListener(this);
            fourDimRadioButton.addActionListener(this);
            higherDimRadioButton.addActionListener(this);
        }
    }

    public static void addPortCategoryTypes(DefaultListModel model, String inOrOutStr)
    {
        String nameStr;
        String prefix = "Nsl" + inOrOutStr;
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

            if (inVar.varName != null)
                varNameLabel.setText(inVar.varName);

            String categoryType = inVar.varCategoryType;
            if ((inVar.varCategoryType == null) || (inVar.varCategoryType.length() == 0))
            {
                if (dialogType.equals("InputPort"))
                    categoryType = "NslDinDouble";
                if (dialogType.equals("OutputPort"))
                    categoryType = "NslDoutDouble";
            }
            setSelectionCategoryType(varTypeList, categoryType);

            dimensions = inVar.varDimensions;
            boolean[] dimensions = createDimensionsArray(this.dimensions);
            zeroDimRadioButton.setSelected(dimensions[0]);
            oneDimRadioButton.setSelected(dimensions[1]);
            twoDimRadioButton.setSelected(dimensions[2]);
            threeDimRadioButton.setSelected(dimensions[3]);
            fourDimRadioButton.setSelected(dimensions[4]);
            higherDimRadioButton.setSelected(dimensions[5]);

            if (inVar.varParams != null)
                varParametersTextField.setText(inVar.varParams);

            if (inVar.varInits != null)
                varInitializationTextField.setText(inVar.varInits);
            //no default
            if (inVar.varComment != null)
                commentsTextField.setText(inVar.varComment);
            if (dialogType.equals("OutputPort"))
                portBufferingCheckBox.setSelected(inVar.portBuffering);
            if (dialogType.equals("InputPort"))
                portBufferingCheckBox.setVisible(false);
            if ((dialogType.equals("OutputPort")) || (dialogType.equals("InputPort")))
            {
                //assuming that if IconDirection set then SchDirection is set
                iconDir = inVar.portIconDirection;
                boolean[] directionsArray = createDirectionsArray(inVar.portIconDirection);
                leftRightRadioButton.setSelected(directionsArray[0]);
                rightLeftRadioButton.setSelected(directionsArray[1]);
                topBottomRadioButton.setSelected(directionsArray[2]);
                bottomTopRadioButton.setSelected(directionsArray[3]);
            }
        }
    }

    protected static String figurePortIconDirectionStr(char direction)
    {
        String outstr = "";
        if (direction == 'L')
        { //("left->right")
            outstr = "left->right";
        }
        else if (direction == 'R')
        { //("right->left")
            outstr = "right->left";
        }
        else if (direction == 'T')
        { //("top->bottom")
            outstr = "top->bottom";
        }
        else if (direction == 'B')
        { //("bottom->top")
            outstr = "bottom->top";
        }
        return (outstr);
    }

    protected static char figurePortIconDirectionChar(String direction)
    {
        char outchar = '0'; //not found
        if (direction.equals("left->right"))
        {
            outchar = 'L';
        }
        else if (direction.equals("right->left"))
        {
            outchar = 'R';
        }
        else if (direction.equals("top->bottom"))
        {
            outchar = 'T';
        }
        else if (direction.equals("bottom->top"))
        {
            outchar = 'B';
        }
        return (outchar);
    }

    protected static boolean[] createDirectionsArray(char direction)
    {
        boolean[] blist = new boolean[4];

        blist[0] = (direction == 'L');
        blist[1] = (direction == 'R');
        blist[2] = (direction == 'T');
        blist[3] = (direction == 'B');

        return (blist);
    }


    /**
     * ----------------------------------------------
     * getDeclarationInfo
     * note1: updateCurrVar should have already been called in
     * actionPerformed
     * note2: must copy field by field so that the
     * variable passed in contains the new data.
     * just setting outVar=currVar will not work,
     * nor will will outVar=duplicate(currVar);
     * In both cases you try to change what outVar points
     * to which can only happen in the class calling DeclarationDialog.display
     */
    public Declaration fillDeclarationInfo(Declaration outVar)
    {
        outVar.varAccess = "public";
        outVar.varScope = "local";
        outVar.varConstant = false;
        outVar.varDimensions = dimensions;

        if (varTypeList.getSelectedValue() != null)
        {
            outVar.varCategoryType = varTypeList.getSelectedValue().toString();
            if (varTypeList.getSelectedValue().toString().startsWith("Nsl"))
                outVar.varType = varTypeList.getSelectedValue().toString() + outVar.varDimensions;
            else
                outVar.varType = varTypeList.getSelectedValue().toString();
        }

        outVar.varParams = varParametersTextField.getText();
        outVar.varInits = varInitializationTextField.getText();
        outVar.varDialogType = dialogType;
        outVar.varComment = commentsTextField.getText();
        outVar.portBuffering = portBufferingCheckBox.isSelected();
        outVar.portIconDirection = iconDir;
        outVar.portSchDirection = iconDir;
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
            else if (jradiobutton.equals(leftRightRadioButton) || jradiobutton.equals(rightLeftRadioButton) ||
                    jradiobutton.equals(topBottomRadioButton) || jradiobutton.equals(bottomTopRadioButton))
            {
                iconDir = figurePortIconDirectionChar(jradiobutton.getText());
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
        }  //end = JButton
    } //end actionPerformed

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
    }

    public void display(Declaration var)
    {
        init(var);
        setLocation(400, 400);
        pack();
        setSize(600, 350);
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
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleNameLabelLabel, gbc);
        moduleNameLabel = new JLabel();
        moduleNameLabel.setText("name");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleNameLabel, gbc);
        varNameLabelLabel = new JLabel();
        varNameLabelLabel.setText("Variable Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varNameLabelLabel, gbc);
        varNameLabel = new JLabel();
        varNameLabel.setText("name");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varNameLabel, gbc);
        portVarTypeLabel = new JLabel();
        portVarTypeLabel.setText("Port Variable Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(portVarTypeLabel, gbc);
        varTypeScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 9;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(varTypeScrollPane, gbc);
        varTypeList = new JList();
        varTypeScrollPane.setViewportView(varTypeList);
        dimensionsLabel = new JLabel();
        dimensionsLabel.setText("Dimensions:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(dimensionsLabel, gbc);
        zeroDimRadioButton = new JRadioButton();
        zeroDimRadioButton.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(zeroDimRadioButton, gbc);
        oneDimRadioButton = new JRadioButton();
        oneDimRadioButton.setText("1");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(oneDimRadioButton, gbc);
        threeDimRadioButton = new JRadioButton();
        threeDimRadioButton.setText("3");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(threeDimRadioButton, gbc);
        higherDimRadioButton = new JRadioButton();
        higherDimRadioButton.setText("higher");
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(higherDimRadioButton, gbc);
        varParametersLabel = new JLabel();
        varParametersLabel.setText("Port Variable Parameters:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varParametersLabel, gbc);
        varParametersTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varParametersTextField, gbc);
        varInitializationTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(varInitializationTextField, gbc);
        varInitializationLabel = new JLabel();
        varInitializationLabel.setText("Port Variable Initialization:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(varInitializationLabel, gbc);
        commentsLabel = new JLabel();
        commentsLabel.setText("Comments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(commentsLabel, gbc);
        commentsTextField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(commentsTextField, gbc);
        portDirectionLabel = new JLabel();
        portDirectionLabel.setText("Port Direction:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(portDirectionLabel, gbc);
        leftRightRadioButton = new JRadioButton();
        leftRightRadioButton.setText("left->right");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(leftRightRadioButton, gbc);
        rightLeftRadioButton = new JRadioButton();
        rightLeftRadioButton.setText("right->left");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(rightLeftRadioButton, gbc);
        topBottomRadioButton = new JRadioButton();
        topBottomRadioButton.setText("top->bottom");
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(topBottomRadioButton, gbc);
        bottomTopRadioButton = new JRadioButton();
        bottomTopRadioButton.setText("bottom->top");
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(bottomTopRadioButton, gbc);
        twoDimRadioButton = new JRadioButton();
        twoDimRadioButton.setText("2");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(twoDimRadioButton, gbc);
        fourDimRadioButton = new JRadioButton();
        fourDimRadioButton.setText("4");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(fourDimRadioButton, gbc);
        portBufferingCheckBox = new JCheckBox();
        portBufferingCheckBox.setText("Port Buffering");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(portBufferingCheckBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer1, gbc);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(zeroDimRadioButton);
        buttonGroup.add(oneDimRadioButton);
        buttonGroup.add(threeDimRadioButton);
        buttonGroup.add(higherDimRadioButton);
        buttonGroup.add(twoDimRadioButton);
        buttonGroup.add(fourDimRadioButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(leftRightRadioButton);
        buttonGroup.add(rightLeftRadioButton);
        buttonGroup.add(topBottomRadioButton);
        buttonGroup.add(bottomTopRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
