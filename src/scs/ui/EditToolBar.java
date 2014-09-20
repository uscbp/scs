package scs.ui;/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import java.awt.*;

import scs.util.SCSUtility;

import javax.swing.*;
import java.awt.event.ActionListener;

public class EditToolBar
{
    JToolBar toolBar;
    JButton cutButton, copyButton, pasteButton;    

    public JToolBar returnToolbar()
    {
        return toolBar;
    }

    /**
     * Constructor of this class with no parameters.
     */
    public EditToolBar(ActionListener thislist)
    {
        toolBar = new JToolBar();
        toolBar.setFloatable(true);

        cutButton = new JButton(SCSUtility.getImageIcon("cut.png"));
        cutButton.setMargin(new Insets(2, 2, 2, 2));
        cutButton.addActionListener(thislist);
        cutButton.setActionCommand("Cut");
        cutButton.setToolTipText("Cut");
        cutButton.setEnabled(false);
        toolBar.add(cutButton);

        copyButton = new JButton(SCSUtility.getImageIcon("editcopy.png"));
        copyButton.setMargin(new Insets(2, 2, 2, 2));
        copyButton.setEnabled(false);
        copyButton.setActionCommand("Copy");
        copyButton.addActionListener(thislist);
        copyButton.setToolTipText("Copy");
        toolBar.add(copyButton);

        pasteButton = new JButton(SCSUtility.getImageIcon("editpaste.png"));
        pasteButton.setMargin(new Insets(2, 2, 2, 2));
        pasteButton.setEnabled(false);
        pasteButton.setActionCommand("Paste");
        pasteButton.addActionListener(thislist);
        pasteButton.setToolTipText("Paste");
        toolBar.add(pasteButton);
    }
    
    public void add(Component c)
    {
        toolBar.add(c);
    }
    
    public void updateUI()
    {
        toolBar.updateUI();
    }

    public void setCopyEnabled(boolean enabled)
    {
        copyButton.setEnabled(enabled);
    }

    public void setCutEnabled(boolean enabled)
    {
        cutButton.setEnabled(enabled);
    }

    public void setPasteEnabled(boolean enabled)
    {
        pasteButton.setEnabled(enabled);
    }
}




