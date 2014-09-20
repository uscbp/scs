package scs.ui;/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import scs.util.SCSUtility;

import javax.swing.*;
import java.awt.event.ActionListener;

public class IconEditorMainToolBar
{
    JToolBar toolBar;

    public JToolBar returnToolbar()
    {
        return toolBar;
    }

    /**
     * Constructor of this class with no parameters.
     */
    public IconEditorMainToolBar(ActionListener thislist)
    {
        toolBar = new JToolBar();
        JButton printButton = new JButton(SCSUtility.getImageIcon("printmgr.png"));
        toolBar.add(printButton);
        printButton.addActionListener(thislist);
        printButton.setActionCommand("Print");
        printButton.setToolTipText("Print");

        JButton exitButton = new JButton(SCSUtility.getImageIcon("exit.png"));
        toolBar.add(exitButton);
        exitButton.addActionListener(thislist);
        exitButton.setActionCommand("Exit");
        exitButton.setToolTipText("Exit");

        JButton helpButton = new JButton(SCSUtility.getImageIcon("help.png"));
        toolBar.add(helpButton);
        helpButton.addActionListener(thislist);
        helpButton.setToolTipText("Help documentation");

    }
}
