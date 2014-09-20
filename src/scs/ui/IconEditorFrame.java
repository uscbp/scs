package scs.ui;
/* SCCS %W% ---%G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * IconEditorFrame - A class representing the main GUI for Icon Editor. It's
 * composed of four main parts:
 * 	- a menu bar at the top
 *	- an icon tool box in the left containing buttons for immediate icon 
 *	  editing operation
 *	- an icon panel occupying the most area, where to draw icons
 *	- a status panel at the bottom, used to print some status information if
 *	  needed
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version     %I%, %G%

 *
 * @since JDK1.1
 */

import scs.*;
import scs.graphics.*;
import scs.util.SCSUtility;
import scs.util.BrowserLauncher;
import scs.util.UserPref;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


class IconEditorFrame extends JFrame implements ActionListener
{
    public StatusPanel myStatusPanel = null;
    IconPanel myIconPanel = null;
    IconToolBox myToolBox = null;
    GraphicPart clipboard = null;

    boolean deleteAfterPaste=false;

    public JMenuItem cut, copy, paste, delete;
    JCheckBoxMenuItem snapGrid, showGrid;

    SchEditorFrame parentFrame;

    //-------------------------------
    /**
     * Constructor of this class, with the parentFrame set to fm, and current module
     * set to module.
     * @param       fm            SchEditorFrame - pointing to the parentFrame--SchEditorFrame
     */
    public IconEditorFrame(SchEditorFrame fm)
    {
        super("Icon");
        parentFrame = fm;
        setLocation(new Point(175, 175));
        setSize(550, 550);

        setTitle("Icon Editor");

        getContentPane().setLayout(new BorderLayout());

        setJMenuBar(CreateMenuBar());

        // add toolbars
        JPanel toolbars = new JPanel();
        toolbars.setLayout(new BorderLayout());

        // add the open,close toolbar
        EditToolBar etb = new EditToolBar(this);  //99/4/15 aa

        getContentPane().add("North", etb.returnToolbar());

        // add status panel
        myStatusPanel = new StatusPanel();
        getContentPane().add("South", myStatusPanel);

        myToolBox = new IconToolBox(this);
        myToolBox.parentFrame = this;
        getContentPane().add("West", myToolBox);

        myIconPanel = new IconPanel(this);  //00/5/07

        JScrollPane scroller = new JScrollPane(myIconPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        getContentPane().add("Center", scroller);

        myIconPanel.setBackground(IconPanel.currBackgroundCol);

        if (parentFrame.currModule.myIcon == null)
        {
            finishNew();
            setTitle("Icon Editor: " + parentFrame.currModule.moduleName);
            return;
        }
        // A Icon view exists by that name
        finishOpen();
        setTitle("Icon Editor: " + parentFrame.currModule.moduleName);
    } //end constructor

    //------------------------------------------------------
    /**
     * Create the menu bar. 
     */
//------------------------------------------------------
    public JMenuBar CreateMenuBar()
    {
        JMenuItem mi;
        JMenu FileMenu;
        JMenu EditMenu;
        JMenu HelpMenu;
        JMenuBar myMenuBar = new JMenuBar();

        FileMenu = new JMenu("File");
        FileMenu.add(mi = new JMenuItem("Print"));
        mi.addActionListener(this);
        FileMenu.addSeparator();
        FileMenu.add(mi = new JMenuItem("Close"));
        mi.addActionListener(this);
        myMenuBar.add(FileMenu);

        // edit menu
        EditMenu = new JMenu("Edit");
        EditMenu.addSeparator();
        EditMenu.add(cut = new JMenuItem("Cut"));
        cut.addActionListener(this);
        cut.setEnabled(false);
        EditMenu.add(copy = new JMenuItem("Copy"));
        copy.addActionListener(this);
        copy.setEnabled(false);
        EditMenu.add(paste = new JMenuItem("Paste"));
        paste.addActionListener(this);
        paste.setEnabled(false);
        EditMenu.add(delete = new JMenuItem("Delete"));
        delete.addActionListener(this);
        delete.setEnabled(false);
        EditMenu.add(mi = new JMenuItem("Clear"));
        mi.addActionListener(this);
        myMenuBar.add(EditMenu);

        //===== options menu
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.addActionListener(this);
        optionsMenu.add(mi = new JMenuItem("Graphics"));
        mi.addActionListener(this);
        optionsMenu.add(showGrid = new JCheckBoxMenuItem("Show Grid"));
        showGrid.setSelected(false);
        showGrid.addActionListener(this);
        optionsMenu.add(snapGrid = new JCheckBoxMenuItem("Snap To Grid"));
        snapGrid.setSelected(true);
        snapGrid.addActionListener(this);
        myMenuBar.add(optionsMenu);

        // == help menu
        HelpMenu = new JMenu("Help");
        HelpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);
        myMenuBar.add(HelpMenu);

        return (myMenuBar);
    }

    //--------------------------------------------------
    public void finishNew()
    {
        if (parentFrame.currModule.myIcon==null)
        {
            parentFrame.currModule.myIcon = new scs.graphics.Icon(parentFrame.currModule.libNickName,
                    parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
            parentFrame.currModule.setDirty(true);
        }
        IconPanel.currBackgroundCol = UserPref.drawBack_col;
        myIconPanel.setBackground(IconPanel.currBackgroundCol);
        myIconPanel.repaint();
    }

    //--------------------------------------------------
    public void finishOpen()
    {
        if (parentFrame.currModule.myIcon==null)
        {
            parentFrame.currModule.myIcon = new scs.graphics.Icon(parentFrame.currModule.libNickName,
                    parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
        }
        // since we zero the icon's coordinates, this just moves it
        // away from the edge
        //if created with schEd or nslmEd
        parentFrame.currModule.myIcon.moveobj(SCSUtility.gridT2, SCSUtility.gridT2);
        parentFrame.currModule.myIcon.resetLabel();
        IconPanel.currBackgroundCol = UserPref.drawBack_col;
        myIconPanel.setBackground(IconPanel.currBackgroundCol);
        myIconPanel.repaint();
    }

    //--------------------------------------------------
    /**
     * Perform menu functions according to action events. 
     */
    public void actionPerformed(ActionEvent event)
    {
        JMenuItem mi;
        myStatusPanel.setStatusMessage("");

        String actionLabel = "";

        if (event.getSource() instanceof JButton)
        {
            JButton dmi = (JButton) event.getSource();
            actionLabel = dmi.getActionCommand();
        }
        else if (event.getSource() instanceof JMenuItem)
        {
            mi = (JMenuItem) event.getSource();
            actionLabel = mi.getText();
        }
        // --------------------------------------------------
        //  File Menu
        // --------------------------------------------------
        //---------------------------------------------------
        if (actionLabel.equals("Print"))
        {
            PrintJob pjob = getToolkit().getPrintJob(this, "Printing Icon", null);
            if (pjob != null)
            {
                Graphics pg = pjob.getGraphics();
                if (pg != null)
                {
                    myIconPanel.paintChildren(pg);
                    pg.dispose();
                }
                pjob.end();
            }
        }
        //---------------------------------------------------
        else if (actionLabel.equals("CloseIconEditor"))
        {
            if (!(closeModule()))
            {
                return;
            }
            dispose();
            parentFrame.mySchematicPanel.requestFocus();
        }
        //---------------------------------------------------
        // Options Menu
        //---------------------------------------------------
        else if (actionLabel.equals("Graphics"))
        {
            GraphicsOptionsDialog opt=new GraphicsOptionsDialog(parentFrame);
            opt.setSize(600, 450);
            opt.setLocation(200, 200);
            opt.setVisible(true);
            myIconPanel.setBackground(IconPanel.currBackgroundCol);
            myIconPanel.repaint();
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Show Grid"))
        {
            myIconPanel.showGrid= showGrid.getState();
            myIconPanel.repaint();
        }
        else if (actionLabel.equals("Snap To Grid"))
        {
            myIconPanel.snapToGrid=snapGrid.getState();
        }
        //---------------------------------------------------
        // Edit Menu
        //---------------------------------------------------
        //--------------------------------------------
        else if (actionLabel.equals("Cut"))
        {
            //dirty=true;
            clipboard=getSelectedComponent();
            if(clipboard!=null)
            {
                deleteAfterPaste=true;
                paste.setEnabled(true);
            }
        }
        //--------------------------------------------
        else if (actionLabel.equals("Delete"))
        {
            //dirty=true;
            removeDrawablePart();
        }
        //--------------------------------------------
        else if (actionLabel.equals("Clear"))
        {
            if (parentFrame.currModule != null && parentFrame.currModule.myIcon != null)
            {
                String errstr="Remove all visible items on screen?";
                int selected = JOptionPane.showConfirmDialog(null, errstr, "Warning", JOptionPane.OK_CANCEL_OPTION,
                                                     JOptionPane.WARNING_MESSAGE);
                if (selected==JOptionPane.OK_OPTION)
                {
                    parentFrame.currModule.myIcon.drawableParts.removeAllElements();
                    parentFrame.currModule.setDirty(true);
                    myIconPanel.repaint();
                }
            }
        }
        //--------------------------------------------
        else if (actionLabel.equals("Copy"))
        {
            clipboard=getSelectedComponent();
            deleteAfterPaste=false;
        }
        //--------------------------------------------
        else if (actionLabel.equals("Paste"))
        {
            clipboard.moveobj(10,10);
            try
            {
                GraphicPart pasting = clipboard.clone();
                if(deleteAfterPaste)
                {
                    removeDrawablePart();
                }
                for(int i=0; i<parentFrame.currModule.myIcon.drawableParts.size(); i++)
                {
                    ((GraphicPart)parentFrame.currModule.myIcon.drawableParts.get(i)).select=0;
                }
                parentFrame.currModule.myIcon.addDrawablePart(pasting);
                parentFrame.currModule.setDirty(true);
                pasting.select=1;
            }
            catch(Exception e)
            {}
            repaint();
        }
        else if(actionLabel.equals("Help"))
        {
            BrowserLauncher.openUrl("http://neuroinformatics.usc.edu/mediawiki/index.php/Icon_Editor");
        }
    }

    private GraphicPart getSelectedComponent()
    {
        GraphicPart gobj=null;
        for (int i = 0; i < parentFrame.currModule.myIcon.drawableParts.size(); i++)
        {
            if (((GraphicPart) parentFrame.currModule.myIcon.drawableParts.elementAt(i)).select == 1)
            {
                try
                {
                    gobj=((GraphicPart) parentFrame.currModule.myIcon.drawableParts.elementAt(i)).clone();
                    paste.setEnabled(true);
                    break;
                }
                catch(Exception e)
                {}
            }
        }
        return gobj;
    }

    //---------------------------------------------
    /**
     * closeModule
     */
    //---------------------------------------------
    public boolean closeModule()
    {
        this.setTitle("Icon Editor");
        this.myStatusPanel.clearWarningMessage();
        return true;
    } //end close

    /**
     * Remove part of the icon
     * @param name - name of the part to remove
     */
    public void removeDrawablePart(String name)
    {
        parentFrame.currModule.myIcon.deleteDrawablePart(name);
        myIconPanel.repaint();
    }

    /**
     * Remove selected part of the icon
     */
    public void removeDrawablePart()
    {
        if (parentFrame.currModule == null || parentFrame.currModule.myIcon == null || parentFrame.currModule.myIcon.drawableParts == null)
        {
            return;
        }

        for (int i = 0; i < parentFrame.currModule.myIcon.drawableParts.size(); i++)
        {
            Object gobj = parentFrame.currModule.myIcon.drawableParts.get(i);
            if (gobj instanceof IconInport && ((IconInport)gobj).select==1)
            {
                IconInport ip = (IconInport) gobj;

                String errstr="Delete inport from module too?";
                int selected = JOptionPane.showConfirmDialog(null, errstr, "Warning", JOptionPane.OK_CANCEL_OPTION,
                                                     JOptionPane.WARNING_MESSAGE);
                if(selected==JOptionPane.OK_OPTION)
                {
                    // Remove port from NslmEditor if module is open in it
                    if(parentFrame.nslmEditor!=null && parentFrame.nslmEditor.isVisible())
                        parentFrame.nslmEditor.removeVariable(parentFrame.currModule.getVariable(ip.Name));

                    if (parentFrame.currModule.variables != null)
                    {
                        parentFrame.currModule.deleteVariable(ip.Name);
                        parentFrame.mySchematicPanel.repaint();
                    }
                }
                break;
            }
            else if (gobj instanceof IconOutport && ((IconOutport)gobj).select==1)
            {
                IconOutport op = (IconOutport) gobj;
                String errstr="Delete outport from module too?";
                int selected = JOptionPane.showConfirmDialog(null, errstr, "Warning", JOptionPane.OK_CANCEL_OPTION,
                                                     JOptionPane.WARNING_MESSAGE);
                if(selected==JOptionPane.OK_OPTION)
                {
                    // Remove port from NslmEditor if module is open in it
                    if(parentFrame.nslmEditor!=null && parentFrame.nslmEditor.isVisible())
                        parentFrame.nslmEditor.removeVariable(parentFrame.currModule.getVariable(op.Name));

                    if (parentFrame.currModule.variables != null)
                    {
                        parentFrame.currModule.deleteVariable(op.Name);
                        parentFrame.mySchematicPanel.repaint();
                    }
                }
                break;
            }// end if gobj instanceof IconOutput
            else if(gobj instanceof GraphicPart && ((GraphicPart)gobj).select==1)
            {
                parentFrame.currModule.myIcon.deleteDrawablePart(i);
                parentFrame.currModule.setDirty(true);
                break;
            }
        } //for all drawableParts
        myIconPanel.repaint();
    }//end removeDrawablePart

    /**
     * Handle windowClosing event.
     */
    public void windowClosing(WindowEvent event)
    {
        Window w = event.getWindow();
        w.dispose();
    } //windowClosing

    //--------------------------------------------------
    /**
     * Handle component events. This is added to correct the problem of losing
     * focus for keyboard input events if the window is setSized. 
     */

    class CLAdapter extends ComponentAdapter
    {
        /**
         * Handle componentResized event.
         */
        public void componentResized(ComponentEvent event)
        {
            myIconPanel.requestFocus();
        }
    } //end class CLAdapter

} //end class IconEditorFrame