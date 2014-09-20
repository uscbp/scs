package scs.ui;/* SCCS  %W% -- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import javax.swing.*;
import java.awt.*;

class StatusPanel extends JPanel
{
    protected Font StatusFont;
    protected FontMetrics StatusFontMetrics;
    protected static final int STATUSPANEL_HEIGHT = 50;
    protected static final Color BackgroundColor = Color.lightGray;
    protected static final Color TextColor = Color.black;
    protected String statusMessage = "";
    protected String warningMessage = "";

    JPanel statusPanel = null;
    JPanel warningPanel = null;
    JLabel statusLabel1 = null;
    JLabel warningLabel1 = null;

    JLabel statusLabel = null;
    JLabel warningLabel = null;

    /**
     * Constructor of this class.
     */
    public StatusPanel()
    {
        //default Flow
        setBackground(BackgroundColor);
        BoxLayout boxlay = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxlay);

        statusPanel = new JPanel();
        FlowLayout flowlay1 = new FlowLayout(FlowLayout.LEFT);
        statusPanel.setLayout(flowlay1);
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel1 = new JLabel("Status:  ");
        statusPanel.add(statusLabel1);
        statusLabel = new JLabel(statusMessage);
        statusPanel.add(statusLabel);

        warningPanel = new JPanel();  //todo: this should be a scrollabel window
        FlowLayout flowlay2 = new FlowLayout(FlowLayout.LEFT);
        warningPanel.setLayout(flowlay2);
        warningPanel.setBorder(BorderFactory.createEtchedBorder());
        warningLabel1 = new JLabel("Warning: ");
        warningPanel.add(warningLabel1);
        warningLabel = new JLabel(warningMessage);
        warningPanel.add(warningLabel);

        //vertical box layout
        add(statusPanel);
        add(warningPanel);
    }

    //-------------------------------------------------
    /**
     * Display the message in this status panel.
     */
    //-------------------------------------------------
    public void setStatusMessage(String msg)
    {
        statusMessage = msg;
        statusLabel.setText(statusMessage);
    }

    //-------------------------------------------------
    public String getStatusMessage()
    {
        return (statusMessage);
    }

    //-------------------------------------------------
    public void clearStatusMessage()
    {
        statusMessage = "";
        statusLabel.setText(statusMessage);
    }

    //-------------------------------------------------
    /**
     /* clear warning message
     */
    //------------------------------------------
    public void clearWarningMessage()
    {
        warningMessage = "";
        warningLabel.setText(warningMessage);
    }

    //-------------------------------------------------
    /**
     /* displays warning
     */
    //------------------------------------------
    public void setWarningMessage(String msg)
    {
        System.err.flush();

        warningMessage = msg;
        warningLabel.setText(warningMessage);
    }

    //-------------------------------------------------
    public String getWarningMessage()
    {
        return (warningMessage);
    }
}//end StatusPanel
