package scs.ui;

import scs.util.SCSUtility;
import scs.util.UserPref;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GraphicsOptionsDialog extends JDialog implements ActionListener, ItemListener
{
    private SchEditorFrame parentFrame;

    public static boolean newPreferences = false;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel sampleColorLabel;
    private JLabel gridColorLabel;
    private JComboBox gridColorComboBox;
    private JLabel schematicGridSizeLabel;
    private JComboBox schematicGridSizeComboBox;
    private JLabel iconGridSizeLabel;
    private JComboBox iconGridSizeComboBox;
    private JLabel backgroundColorLabel;
    private JComboBox backgroundColorComboBox;
    private JLabel inPinColorLabel;
    private JComboBox inPinColorComboBox;
    private JLabel outPinColorLabel;
    private JComboBox outPinColorComboBox;
    private JLabel inPortFillColor;
    private JComboBox inPortFillColorComboBox;
    private JLabel outPortFillColorLabel;
    private JComboBox outPortFillColorComboBox;
    private JLabel connectionColorLabel;
    private JComboBox connectionColorComboBox;
    private JLabel connectionWidthLabel;
    private JComboBox connectionWidthComboBox;
    private JLabel lineColorLabel;
    private JComboBox lineColorComboBox;
    private JLabel rectangleColorLabel;
    private JComboBox rectangleColorComboBox;
    private JLabel ovalColorLabel;
    private JComboBox ovalColorComboBox;
    private JLabel freeTextColorLabel;
    private JComboBox freeTextColorComboBox;
    private JLabel freeTextFontLabel;
    private JComboBox freeTextFontComboBox;
    private JLabel freeTextSizeLabel;
    private JComboBox freeTextSizeComboBox;
    private JLabel moduleTextColorLabel;
    private JComboBox moduleTextColorComboBox;
    private JLabel moduleTextFontLabel;
    private JComboBox moduleTextFontComboBox;
    private JLabel moduleTextSizeLabel;
    private JComboBox moduleTextSizeComboBox;
    private JLabel moduleTextLocationLabel;
    private JComboBox moduleTextLocationComboBox;
    private JLabel instanceTextColorLabel;
    private JComboBox instanceTextColorComboBox;
    private JLabel instanceTextFontLabel;
    private JComboBox instanceTextFontComboBox;
    private JLabel instanceTextSize;
    private JComboBox instanceTextSizeComboBox;
    private JLabel instanceTextLocationLabel;
    private JComboBox instanceTextLocationComboBox;
    private JLabel highlightColorLabel;
    private JComboBox highlightColorComboBox;
    private ColorDemo colorDemo;

    public GraphicsOptionsDialog(SchEditorFrame efm)
    {
        super(efm, "Graphics Options", true);

        parentFrame = efm;
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        String[] fontlist = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        SCSUtility.setColorMenu(gridColorComboBox);
        gridColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.grid_col));
        gridColorComboBox.addItemListener(this);

        schematicGridSizeComboBox.addItem("2");
        schematicGridSizeComboBox.addItem("4");
        schematicGridSizeComboBox.addItem("6");
        schematicGridSizeComboBox.addItem("8");
        schematicGridSizeComboBox.addItem("10");
        schematicGridSizeComboBox.setSelectedItem((new Integer(UserPref.schematicGridSize)).toString());

        iconGridSizeComboBox.addItem("2");
        iconGridSizeComboBox.addItem("4");
        iconGridSizeComboBox.addItem("6");
        iconGridSizeComboBox.addItem("8");
        iconGridSizeComboBox.addItem("10");
        iconGridSizeComboBox.setSelectedItem((new Integer(UserPref.iconGridSize)).toString());

        SCSUtility.setColorMenu(backgroundColorComboBox);
        backgroundColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.drawBack_col));
        backgroundColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(inPinColorComboBox);
        inPinColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.inPin_col));
        inPinColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(outPinColorComboBox);
        outPinColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.outPin_col));
        outPinColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(inPortFillColorComboBox);
        inPortFillColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.inPortFill_col));
        inPortFillColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(outPortFillColorComboBox);
        outPortFillColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.outPortFill_col));
        outPortFillColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(connectionColorComboBox);
        connectionColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.connection_col));
        connectionColorComboBox.addItemListener(this);

        connectionWidthComboBox.addItem("1");
        connectionWidthComboBox.addItem("2");
        connectionWidthComboBox.addItem("3");
        connectionWidthComboBox.addItem("4");
        connectionWidthComboBox.setSelectedItem(UserPref.connectionWidth.toString());

        SCSUtility.setColorMenu(lineColorComboBox);
        lineColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.line_col));
        lineColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(rectangleColorComboBox);
        rectangleColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.rect_col));
        rectangleColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(ovalColorComboBox);
        ovalColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.oval_col));
        ovalColorComboBox.addItemListener(this);

        SCSUtility.setColorMenu(freeTextColorComboBox);
        freeTextColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.freeText_col));
        freeTextColorComboBox.addItemListener(this);

        for (int count = 0; count < fontlist.length; count++)
            freeTextFontComboBox.addItem(fontlist[count]);
        freeTextFontComboBox.setSelectedItem(UserPref.freeTextFontName);

        SCSUtility.setTextSizeMenu(freeTextSizeComboBox);
        freeTextSizeComboBox.setSelectedItem((new Integer(UserPref.freeTextSize)).toString());

        SCSUtility.setColorMenu(moduleTextColorComboBox);
        moduleTextColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.moduleText_col));
        moduleTextColorComboBox.addItemListener(this);

        for (int count = 0; count < fontlist.length; count++)
            moduleTextFontComboBox.addItem(fontlist[count]);
        moduleTextFontComboBox.setSelectedItem(UserPref.moduleTextFontName);

        SCSUtility.setTextSizeMenu(moduleTextSizeComboBox);
        moduleTextSizeComboBox.setSelectedItem((new Integer(UserPref.moduleTextSize)).toString());

        moduleTextLocationComboBox.addItem("BELOW");
        moduleTextLocationComboBox.addItem("CENTER");
        moduleTextLocationComboBox.addItem("ABOVE");
        moduleTextLocationComboBox.addItem("RIGHT");
        moduleTextLocationComboBox.addItem("LEFT");
        moduleTextLocationComboBox.setSelectedItem(UserPref.moduleTextLocation);

        SCSUtility.setColorMenu(instanceTextColorComboBox);
        instanceTextColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.instanceText_col));
        instanceTextColorComboBox.addItemListener(this);

        for (int count = 0; count < fontlist.length; count++)
            instanceTextFontComboBox.addItem(fontlist[count]);
        instanceTextFontComboBox.setSelectedItem(UserPref.instanceTextFontName);

        SCSUtility.setTextSizeMenu(instanceTextSizeComboBox);
        instanceTextSizeComboBox.setSelectedItem((new Integer(UserPref.instanceTextSize)).toString());

        instanceTextLocationComboBox.addItem("BELOW");
        instanceTextLocationComboBox.addItem("CENTER");
        instanceTextLocationComboBox.addItem("ABOVE");
        instanceTextLocationComboBox.addItem("RIGHT");
        instanceTextLocationComboBox.addItem("LEFT");
        instanceTextLocationComboBox.setSelectedItem(UserPref.instanceTextLocation);

        SCSUtility.setColorMenu(highlightColorComboBox);
        highlightColorComboBox.setSelectedItem(SCSUtility.returnColorNameString(UserPref.highlight_col));
        highlightColorComboBox.addItemListener(this);

        buttonOK.addActionListener(this);

        buttonCancel.addActionListener(this);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setOptions()
    {
        UserPref.highlight_col = SCSUtility.returnCol(highlightColorComboBox.getSelectedItem().toString());
        UserPref.grid_col = SCSUtility.returnCol(gridColorComboBox.getSelectedItem().toString());

        UserPref.drawBack_col = SCSUtility.returnCol(backgroundColorComboBox.getSelectedItem().toString());
        UserPref.inPin_col = SCSUtility.returnCol(inPinColorComboBox.getSelectedItem().toString());
        UserPref.outPin_col = SCSUtility.returnCol(outPinColorComboBox.getSelectedItem().toString());

        UserPref.inPortFill_col = SCSUtility.returnCol(inPortFillColorComboBox.getSelectedItem().toString());
        UserPref.outPortFill_col = SCSUtility.returnCol(outPortFillColorComboBox.getSelectedItem().toString());
        UserPref.connection_col = SCSUtility.returnCol(connectionColorComboBox.getSelectedItem().toString());
        UserPref.connectionWidth = new Integer(connectionWidthComboBox.getSelectedItem().toString());
        UserPref.line_col = SCSUtility.returnCol(lineColorComboBox.getSelectedItem().toString());
        UserPref.rect_col = SCSUtility.returnCol(rectangleColorComboBox.getSelectedItem().toString());
        UserPref.oval_col = SCSUtility.returnCol(ovalColorComboBox.getSelectedItem().toString());

        UserPref.freeText_col = SCSUtility.returnCol(freeTextColorComboBox.getSelectedItem().toString());
        UserPref.freeTextFontName = freeTextFontComboBox.getSelectedItem().toString();

        UserPref.freeTextSize = Integer.parseInt(freeTextSizeComboBox.getSelectedItem().toString());

        UserPref.freeTextFont = new Font(UserPref.freeTextFontName, Font.BOLD, UserPref.freeTextSize);

        UserPref.moduleText_col = SCSUtility.returnCol(moduleTextColorComboBox.getSelectedItem().toString());
        UserPref.moduleTextFontName = moduleTextFontComboBox.getSelectedItem().toString();
        UserPref.moduleTextSize = Integer.parseInt(moduleTextSizeComboBox.getSelectedItem().toString());
        UserPref.moduleTextFont = new Font(UserPref.moduleTextFontName, Font.BOLD, UserPref.moduleTextSize);
        UserPref.moduleTextLocation = moduleTextLocationComboBox.getSelectedItem().toString();

        UserPref.instanceText_col = SCSUtility.returnCol(instanceTextColorComboBox.getSelectedItem().toString());
        UserPref.instanceTextFontName = instanceTextFontComboBox.getSelectedItem().toString();
        UserPref.instanceTextSize = Integer.parseInt(instanceTextSizeComboBox.getSelectedItem().toString());
        UserPref.instanceTextFont = new Font(UserPref.instanceTextFontName, Font.BOLD, UserPref.instanceTextSize);
        UserPref.instanceTextLocation = instanceTextLocationComboBox.getSelectedItem().toString();
    }

    //------------------------------------------------------------
    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() instanceof JButton)
        {
            String cmdName = ((JButton) event.getSource()).getText();
            if (cmdName.equals("Cancel"))
            {
                dispose();
            }
            if (cmdName.equals("OK"))
            {
                setOptions();
                UserPref.save();
                newPreferences = true;

                IconPanel.setBackgroundColor(UserPref.drawBack_col);
                SchematicPanel.setBackgroundColor(UserPref.drawBack_col);
                parentFrame.repaint();

                if (parentFrame.currModule != null && parentFrame.currModule.mySchematic != null)
                    parentFrame.currModule.mySchematic.refresh();

                if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
                    parentFrame.iconEditor.myIconPanel.repaint();

                dispose();
            }
        }
    }

    //---------------------------------------------------------
    public void itemStateChanged(ItemEvent event)
    {
        JComboBox choice1;
        if (event.getSource() instanceof JComboBox)
        {
            choice1 = (JComboBox) event.getSource();

            //change sample only for color choices
            colorDemo.setColorFunc(choice1.getSelectedItem().toString());
            colorDemo.repaint();
            //if you change the backgroundCol, then change the pin color
            //when OK is pressed.
            if (choice1 == backgroundColorComboBox)
            {
                UserPref.inPin_col = SCSUtility.returnOppositeCol(backgroundColorComboBox.getSelectedItem().toString());
                inPinColorComboBox.setSelectedItem(UserPref.inPin_col);
                UserPref.outPin_col = SCSUtility.returnOppositeCol(backgroundColorComboBox.getSelectedItem().toString());
                outPinColorComboBox.setSelectedItem(UserPref.outPin_col);
            }
        }
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
        sampleColorLabel = new JLabel();
        sampleColorLabel.setText("Sample Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(sampleColorLabel, gbc);
        colorDemo = new ColorDemo();
        colorDemo.setMinimumSize(new Dimension(100, 30));
        colorDemo.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel3.add(colorDemo, gbc);
        gridColorLabel = new JLabel();
        gridColorLabel.setText("Grid Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(gridColorLabel, gbc);
        gridColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(gridColorComboBox, gbc);
        schematicGridSizeLabel = new JLabel();
        schematicGridSizeLabel.setText("Schematic Grid Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(schematicGridSizeLabel, gbc);
        schematicGridSizeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(schematicGridSizeComboBox, gbc);
        iconGridSizeLabel = new JLabel();
        iconGridSizeLabel.setText("Icon Grid Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(iconGridSizeLabel, gbc);
        iconGridSizeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(iconGridSizeComboBox, gbc);
        backgroundColorLabel = new JLabel();
        backgroundColorLabel.setText("Background Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(backgroundColorLabel, gbc);
        backgroundColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(backgroundColorComboBox, gbc);
        inPinColorLabel = new JLabel();
        inPinColorLabel.setText("Input Pin Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(inPinColorLabel, gbc);
        inPinColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(inPinColorComboBox, gbc);
        outPinColorLabel = new JLabel();
        outPinColorLabel.setText("Output Pin Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(outPinColorLabel, gbc);
        outPinColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(outPinColorComboBox, gbc);
        inPortFillColor = new JLabel();
        inPortFillColor.setText("Input Port Fill Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(inPortFillColor, gbc);
        inPortFillColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(inPortFillColorComboBox, gbc);
        outPortFillColorLabel = new JLabel();
        outPortFillColorLabel.setText("Output Port Fill Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(outPortFillColorLabel, gbc);
        outPortFillColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(outPortFillColorComboBox, gbc);
        instanceTextLocationLabel = new JLabel();
        instanceTextLocationLabel.setText("Instance Text Location");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(instanceTextLocationLabel, gbc);
        instanceTextLocationComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(instanceTextLocationComboBox, gbc);
        highlightColorLabel = new JLabel();
        highlightColorLabel.setText("Highlight Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(highlightColorLabel, gbc);
        highlightColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(highlightColorComboBox, gbc);
        instanceTextColorLabel = new JLabel();
        instanceTextColorLabel.setText("Instance Text Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(instanceTextColorLabel, gbc);
        instanceTextColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(instanceTextColorComboBox, gbc);
        instanceTextFontLabel = new JLabel();
        instanceTextFontLabel.setText("Instance Text Font");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(instanceTextFontLabel, gbc);
        instanceTextFontComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(instanceTextFontComboBox, gbc);
        instanceTextSize = new JLabel();
        instanceTextSize.setText("Instance Text Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(instanceTextSize, gbc);
        instanceTextSizeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(instanceTextSizeComboBox, gbc);
        moduleTextLocationComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 12;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(moduleTextLocationComboBox, gbc);
        moduleTextLocationLabel = new JLabel();
        moduleTextLocationLabel.setText("Module Text Location");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleTextLocationLabel, gbc);
        moduleTextSizeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(moduleTextSizeComboBox, gbc);
        moduleTextSizeLabel = new JLabel();
        moduleTextSizeLabel.setText("Module Text Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleTextSizeLabel, gbc);
        moduleTextFontLabel = new JLabel();
        moduleTextFontLabel.setText("Module Text Font");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleTextFontLabel, gbc);
        moduleTextFontComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(moduleTextFontComboBox, gbc);
        moduleTextColorLabel = new JLabel();
        moduleTextColorLabel.setText("Module Text Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(moduleTextColorLabel, gbc);
        moduleTextColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(moduleTextColorComboBox, gbc);
        freeTextSizeLabel = new JLabel();
        freeTextSizeLabel.setText("Free Text Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(freeTextSizeLabel, gbc);
        freeTextSizeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(freeTextSizeComboBox, gbc);
        freeTextFontLabel = new JLabel();
        freeTextFontLabel.setText("Free Text Font");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(freeTextFontLabel, gbc);
        freeTextFontComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(freeTextFontComboBox, gbc);
        freeTextColorLabel = new JLabel();
        freeTextColorLabel.setText("Free Text Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(freeTextColorLabel, gbc);
        freeTextColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(freeTextColorComboBox, gbc);
        ovalColorLabel = new JLabel();
        ovalColorLabel.setText("Oval Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(ovalColorLabel, gbc);
        ovalColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(ovalColorComboBox, gbc);
        rectangleColorLabel = new JLabel();
        rectangleColorLabel.setText("Rectangle Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(rectangleColorLabel, gbc);
        rectangleColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(rectangleColorComboBox, gbc);
        lineColorLabel = new JLabel();
        lineColorLabel.setText("Line Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lineColorLabel, gbc);
        lineColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(lineColorComboBox, gbc);
        connectionWidthLabel = new JLabel();
        connectionWidthLabel.setText("Connection Width");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(connectionWidthLabel, gbc);
        connectionWidthComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(connectionWidthComboBox, gbc);
        connectionColorLabel = new JLabel();
        connectionColorLabel.setText("Connection Color");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(connectionColorLabel, gbc);
        connectionColorComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(connectionColorComboBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }
}
