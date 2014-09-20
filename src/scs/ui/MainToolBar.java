package scs.ui;/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import scs.util.SCSUtility;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.*;

public class MainToolBar
{
    JToolBar toolBar;
    JButton newButton, openButton, closeButton, saveButton, printButton, exitButton, upButton, downButton,
            subModuleButton, connButton, inportButton, outportButton, textButton, helpButton;

    public JToolBar returnToolbar()
    {
        return toolBar;
    }

    /**
     * Constructor of this class with no parameters.
     */
    public MainToolBar(ActionListener thislist)
    {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        newButton = new JButton(SCSUtility.getImageIcon("filenew.png"));
        newButton.setMargin(new Insets(2, 2, 2, 2));
        newButton.addActionListener(thislist);
        newButton.setActionCommand("Module");
        newButton.setToolTipText("New Module");
        toolBar.add(newButton);

        openButton = new JButton(SCSUtility.getImageIcon("fileopen.png"));
        openButton.setMargin(new Insets(2, 2, 2, 2));
        toolBar.add(openButton);
        openButton.setActionCommand("Open");
        openButton.addActionListener(thislist);
        openButton.setToolTipText("Open Module");

        closeButton = new JButton(SCSUtility.getImageIcon("fileclose.png"));
        closeButton.setMargin(new Insets(2, 2, 2, 2));
        closeButton.setEnabled(false);
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(thislist);
        closeButton.setToolTipText("Close Module");
        toolBar.add(closeButton);

        saveButton = new JButton(SCSUtility.getImageIcon("filesave.png"));
        saveButton.setMargin(new Insets(2, 2, 2, 2));
        saveButton.setEnabled(false);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(thislist);
        saveButton.setToolTipText("Save Module");
        toolBar.add(saveButton);

        printButton = new JButton(SCSUtility.getImageIcon("printmgr.png"));
        printButton.setMargin(new Insets(2, 2, 2, 2));
        printButton.setEnabled(false);
        printButton.addActionListener(thislist);
        printButton.setActionCommand("Print");
        printButton.setToolTipText("Print");
        toolBar.add(printButton);

        exitButton = new JButton(SCSUtility.getImageIcon("exit.png"));
        exitButton.setMargin(new Insets(2, 2, 2, 2));
        exitButton.addActionListener(thislist);
        exitButton.setActionCommand("Exit");
        exitButton.setToolTipText("Exit");
        toolBar.add(exitButton);

        upButton = new JButton(SCSUtility.getImageIcon("up.png"));
        upButton.setMargin(new Insets(2, 2, 2, 2));
        upButton.setEnabled(false);
        upButton.addActionListener(thislist);
        upButton.setActionCommand("Ascend");
        upButton.setToolTipText("Ascend Hierarchy");
        toolBar.add(upButton);

        downButton = new JButton(SCSUtility.getImageIcon("down.png"));
        downButton.setMargin(new Insets(2, 2, 2, 2));
        downButton.setEnabled(false);
        downButton.addActionListener(thislist);
        downButton.setActionCommand("Descend");
        downButton.setToolTipText("Descend Hierarchy");
        toolBar.add(downButton);

        subModuleButton = new JButton(SCSUtility.getImageIcon("edit_add.png"));
        subModuleButton.setMargin(new Insets(2, 2, 2, 2));
        subModuleButton.setEnabled(false);
        subModuleButton.addActionListener(thislist);
        subModuleButton.setActionCommand("SubModule");
        subModuleButton.setToolTipText("Insert SubModule");
        toolBar.add(subModuleButton);

        connButton = new JButton(SCSUtility.getImageIcon("connect_creating.png"));
        connButton.setMargin(new Insets(2, 2, 2, 2));
        connButton.setEnabled(false);
        connButton.addActionListener(thislist);
        connButton.setActionCommand("Connection");
        connButton.setToolTipText("Insert Connection");
        toolBar.add(connButton);

        inportButton = new JButton(SCSUtility.getImageIcon("finish.png"));
        inportButton.setMargin(new Insets(2, 2, 2, 2));
        inportButton.setEnabled(false);
        inportButton.addActionListener(thislist);
        inportButton.setActionCommand("InPort");
        inportButton.setToolTipText("Insert Schematic InPort");
        toolBar.add(inportButton);

        outportButton = new JButton(SCSUtility.getImageIcon("start.png"));
        outportButton.setMargin(new Insets(2, 2, 2, 2));
        outportButton.setEnabled(false);
        outportButton.addActionListener(thislist);
        outportButton.setActionCommand("OutPort");
        outportButton.setToolTipText("Insert Schematic OutPort");
        toolBar.add(outportButton);

        textButton = new JButton(SCSUtility.getImageIcon("applix.png"));
        textButton.setMargin(new Insets(2, 2, 2, 2));
        textButton.setEnabled(false);
        textButton.addActionListener(thislist);
        textButton.setActionCommand("Free text");
        textButton.setToolTipText("Insert Text");
        textButton.setEnabled(false);
        toolBar.add(textButton);

        helpButton = new JButton(SCSUtility.getImageIcon("help.png"));
        helpButton.setMargin(new Insets(2, 2, 2, 2));
        toolBar.add(helpButton);
        helpButton.addActionListener(thislist);
        helpButton.setToolTipText("Help");
    }

    public void setModifyMenuEnabled(boolean enabled)
    {
        closeButton.setEnabled(enabled);
        printButton.setEnabled(enabled);
        subModuleButton.setEnabled(enabled);
        connButton.setEnabled(enabled);
        inportButton.setEnabled(enabled);
        outportButton.setEnabled(enabled);
        textButton.setEnabled(enabled);
    }
}
