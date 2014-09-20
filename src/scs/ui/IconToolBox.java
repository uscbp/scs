package scs.ui;
/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

import scs.Declaration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * IconToolBox - A class representing the icon tool bar in the left of icon
 * editor containing buttons for immediate icon editing operations.
 *
 * @author Weifang Xie, Gupta, Alexander
 * @version %I%, %G%
 *          <p/>
 *          parentFrame   pointing to the parentFrame--IconEditorFrame
 *          shapeMenu	    a popup menu containing different choices of
 *          shapes to be inserted to the icon panel
 *          status		a flag indicating the current status of
 *          operations, like what component will be inserted
 *          next
 * @since JDK1.1
 */

//todo: eliminate this class and move to a tool bar.
public class IconToolBox extends JPanel implements ActionListener
{
    IconEditorFrame parentFrame;
    JPopupMenu shapeMenu;
    String status = null;

    Declaration var = null;

    /**
     * Constructor of this class with no parameters.
     * @param parentFrame - parent frame
     */
    public IconToolBox(IconEditorFrame parentFrame)
    {
        JButton jbn;

        this.parentFrame = parentFrame;
        setLayout(new FlowLayout());
        Dimension dim = new Dimension(100, 30);

        // Rectangle
        jbn = new JButton("Rectangle");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Line
        jbn = new JButton("Line");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Oval
        jbn = new JButton("Oval");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);
        // Polygon
        /*jbn = new JButton("Polygon");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        this.add(jbn);*/
        // Text
        jbn = new JButton("Text");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
        // Inport
        jbn = new JButton("InPort");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
        // Outport
        jbn = new JButton("OutPort");
        jbn.addActionListener(this);
        jbn.setPreferredSize(dim);
        add(jbn);
    }
    //-------------------------------------------------------

    /**
     * Handle action events.
     */
    public void actionPerformed(ActionEvent event)
    {
        JButton jbn;
        String somestr;

        if (parentFrame.parentFrame.currModule == null)
        {
            String errStr="Must use File Menu to create Icon first.";
            JOptionPane.showMessageDialog(parentFrame, errStr, "IconToolbox Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (event.getSource() instanceof JButton)
        {
            jbn = (JButton) event.getSource();
            somestr = jbn.getText();
            //--------------------------------------
            if (somestr.equals("InPort"))
            {
                doPort("InputPort");
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //-----------------------------------
            if (somestr.equals("OutPort"))
            {
                doPort("OutputPort");
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //-----------------------------------
            if (somestr.equals("Text"))
            {
                String text = (String)JOptionPane.showInputDialog(this, "Enter Text", "Text",
                                                                   JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (text!=null)
                {
                    status = "insert_text";
                    parentFrame.myIconPanel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                    IconPanel.text_string = text;
                }
                else
                {
                    IconPanel.text_string = "";
                    status = "nothing";
                }
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Line"))
            {
                status = "insert_line";
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Oval"))
            {
                status = "insert_oval";
                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            if (somestr.equals("Rectangle"))
            {
                status = "insert_rect";

                parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                parentFrame.myIconPanel.newstatus();
                return;
            }
            //--------------------
            /*if (somestr.equals("Polygon"))
            {
                if(status!=null && status.equals("insert_poly"))
                {
                    status=null;
                    parentFrame.myIconPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                else
                {
                    status = "insert_poly";
                    parentFrame.myIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    parentFrame.myIconPanel.newstatus();
                }
                return;
            }*/
            //--------------------
            String errStr="JButton but no one claimed it.";
            JOptionPane.showMessageDialog(parentFrame, errStr, "IconToolbox Error", JOptionPane.ERROR_MESSAGE);
            return;
        } //end if JButton
        String errStr="Event but no button or menu item claimed it.";
        JOptionPane.showMessageDialog(parentFrame, errStr, "IconToolbox Error", JOptionPane.ERROR_MESSAGE);
    }

    //----------------------------------------------------------
    public void doPort(String dialogType)
    {
        parentFrame.myIconPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        PortDialog dialog = new PortDialog(parentFrame, parentFrame.parentFrame.currModule, dialogType);

        var = parentFrame.parentFrame.currModule.fillVariableName(parentFrame, dialogType, "Port Instance Name(first letter lower case): ");
        if (var == null)
            return;

        dialog.display(var);
        if (dialog.okPressed)
        {
            dialog.fillDeclarationInfo(var);
            //everything is in var
            if (parentFrame.parentFrame.currModule.findVarIndex(var.varName)<0)
            {
                if (dialogType.equals("InputPort"))
                {
                    status = "insert_inport";
                }
                if (dialogType.equals("OutputPort"))
                {
                    status = "insert_outport";
                }
                parentFrame.myIconPanel.newstatus();
                parentFrame.myIconPanel.setPortInfo(var);//add port
                //free to add
            }
        }
    }

    /**
     * Return the preferred size of this icon tool box.
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(150, getSize().height);
    }
}

