package scs.ui;
/* SCCS %W%  %G% %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project./
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * SchEditorFrame - A class representing the main GUI for Schematic Editor. It's
 * composed of four main parts:
 *      - a menu bar at the top
 *      - a schematic tool bar under the menu bar containing image buttons for 
 *	  immediate schematic editing operation
 *      - a schematic panel occupying the most area, where to draw schematics
 *      - a status panel at the bottom, used to print some status information if
 *        needed
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version     %W% , %G% , %U%
 *
 *
 * @since JDK1.1
 */

import scs.*;
import scs.graphics.*;
import scs.util.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import nslj.src.system.NslSystem;
import nslj.src.lang.NslHierarchy;
import nslj.src.cmd.*;
import org.xml.sax.SAXException;

/**
 * Main window
 */

public class SchEditorFrame extends JFrame implements ActionListener, ModuleDirtyListener, WindowListener
{
    MainToolBar maintoolbar;
    EditToolBar edittoolbar;
    DebugToolBar debugtoolbar;

    JMenuItem newModule, newModel, open, save, saveAs, close, print, exit, undo, redo, cut, copy, paste, clear, selectAll,
            group, unGroup, zoomIn, zoomOut, refresh, descend, ascend, insertSubmodule, insertConnection, insertInPort,
            insertOutPort, insertFreeText, optionsGraphics, optionsPortShapes, libraryManagement, /*libraryPathEditor,*/
            iconEditorMenuItem, nslmEditorMenuItem, viewNSLM, buildJava, simulateBuildJava, simulateJava, debugJava, debugBuildJava,
            /*buildC, simulateC, */ help, about;
    JMenu reopenMenu;
    JCheckBoxMenuItem snapGrid, showGrid;

    JPanel myToolBox = null;
    SchematicPanel mySchematicPanel = null;
    JScrollPane schematicScrollPane = null;
    String status = "";
    public Stack<Module> hierarchyStack = new Stack<Module>();
    private Component clipboardComponent;

    private String currentInstanceName = "";

    static boolean debugMode = false;

    NslmEditorFrame nslmEditor;
    IconEditorFrame iconEditor;

    public Module currModule = null;
    public String currDirStr = "";

    public StatusPanel myStatusPanel = null;

    static final int sizex = 800;
    static final int sizey = 600;
    static final int locationx = 50;
    static final int locationy = 50;

    /*
     * *var       myStatusPanel   	the panel for printing status
     *					information
     * *var       myToolBox       	the tool bar at the next to top
     *					containing image buttons for immediate
     *                              	schematic editing operation
     * *var       mySchematicPanel	the panel where to draw schematics
     * *var       currModule      	the current module that's being
     *					manipulated
     *
     * *var	status			a flag indicating whether it's at the
     *					phase of inserting a connection
     * *var	hierarchyStack			the stack of hierarchies of schematics
     *					such that the navigation among them is
     *					made possible

     *					when an unfinished function is asked for


     /**
      * The main function of the whole system, taking responsibility to be the
      * main window.
      */
    private static SchEditorFrame schEditorFrame;
    public static void main(String args[])
    {
        UserPref.init();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {}
        schEditorFrame = new SchEditorFrame();

        try
        {
            Thread.sleep(2000);
        }
        catch(InterruptedException e)
        {}

        schEditorFrame.setVisible(true);
        schEditorFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        schEditorFrame.mySchematicPanel.requestFocus();
    }

    public static SchEditorFrame getSingleton(){
        return schEditorFrame;
    }

    /**
     * Constructor of this class.
     */
    public SchEditorFrame()
    {
        initEditorFrame(sizex, sizey, locationx, locationy);

        setTitle("Schematic Editor");
        setJMenuBar(createMenuBar());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setSize(new Dimension(400, 100));

        mySchematicPanel = new SchematicPanel(this);


        JPanel bottomToolBarPanel = new JPanel();
        bottomToolBarPanel.setLayout(new GridLayout(1, 2, 2, 2));

        JPanel mainAndEditPanel = new JPanel(true);
        mainAndEditPanel.setLayout(new GridLayout(2, 1, 2, 2));
        maintoolbar = new MainToolBar(this);
        mainAndEditPanel.add(maintoolbar.returnToolbar());
        edittoolbar = new EditToolBar(this);
        bottomToolBarPanel.add(edittoolbar.returnToolbar());
        debugtoolbar = new DebugToolBar(mySchematicPanel);
        debugtoolbar.setVisible(false);
        bottomToolBarPanel.add(debugtoolbar);

        mainAndEditPanel.add(bottomToolBarPanel);

        getContentPane().add(mainAndEditPanel, BorderLayout.NORTH);

        mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);

        schematicScrollPane = new JScrollPane(mySchematicPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().add(schematicScrollPane, BorderLayout.CENTER);

        // add status panel
        myStatusPanel = new StatusPanel();
        getContentPane().add(myStatusPanel, BorderLayout.SOUTH);

        addComponentListener(new CLAdapter());

        myStatusPanel.setStatusMessage("Preferences are in: " + SCSUtility.scs_preferences_path);
    }

    /**
     * init
     * @param sizexx - width
     * @param sizeyy - height
     * @param locationxx - x location
     * @param locationyy - y location
     */
    public void initEditorFrame(int sizexx, int sizeyy, int locationxx, int locationyy)
    {
        this.setSize(sizexx, sizeyy);
        this.setLocation(new Point(locationxx, locationyy));

        addWindowListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    // --------------------------------------------------
    /**
     * Create the menu bar.
     * @return JMenuBar
     */
    public JMenuBar createMenuBar()
    {
        //JMenuItem mi;

        JMenu FileMenu;

        JMenu EditMenu;
        JMenu HierarchyMenu;
        JMenu InsertMenu;
        JMenu OptionsMenu;
        JMenu ToolsMenu;
        JMenu HelpMenu;
        //JMenu DbtoolsMenu;
        JMenuBar myMenuBar = new JMenuBar();
        //JMenu BmwMenu;
        //JMenu ForeignMenu;

        // File main menu
        FileMenu = new JMenu("File");

        JMenu newMenu = new JMenu("New");
        newMenu.add(newModule = new JMenuItem("Module"));
        newModule.addActionListener(this);
        newMenu.add(newModel = new JMenuItem("Model"));
        newModel.addActionListener(this);
        FileMenu.add(newMenu);

        FileMenu.add(open = new JMenuItem("Open"));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.addActionListener(this);

        reopenMenu = new JMenu("Reopen");
        refreshReopenMenu();
        FileMenu.add(reopenMenu);

        FileMenu.add(save = new JMenuItem("Save"));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.addActionListener(this);
        save.setEnabled(false);
        FileMenu.add(saveAs = new JMenuItem("Save as"));
        saveAs.addActionListener(this);
        saveAs.setEnabled(false);
        FileMenu.add(close = new JMenuItem("Close"));
        close.addActionListener(this);
        close.setEnabled(false);

        FileMenu.addSeparator();

        FileMenu.add(print = new JMenuItem("Print"));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        print.addActionListener(this);
        print.setEnabled(false);

        FileMenu.addSeparator();

        FileMenu.add(exit = new JMenuItem("Quit"));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exit.addActionListener(this);
        myMenuBar.add(FileMenu);

        // Edit Main Menu
        EditMenu = new JMenu("Edit");
        EditMenu.add(undo = new JMenuItem("Undo"));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undo.addActionListener(this);
        undo.setEnabled(false);
        EditMenu.add(redo = new JMenuItem("Redo"));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redo.addActionListener(this);
        redo.setEnabled(false);

        EditMenu.addSeparator();

        EditMenu.add(cut = new JMenuItem("Cut"));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cut.addActionListener(this);
        cut.setEnabled(false);
        EditMenu.add(copy = new JMenuItem("Copy"));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copy.addActionListener(this);
        copy.setEnabled(false);
        EditMenu.add(paste = new JMenuItem("Paste"));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        paste.addActionListener(this);
        paste.setEnabled(false);
        EditMenu.add(clear = new JMenuItem("Clear"));
        clear.addActionListener(this);
        clear.setEnabled(false);
        //EditMenu.add(selectAll = new JMenuItem("Select all"));
        //selectAll.addActionListener(this);
        //selectAll.setEnabled(false);

        EditMenu.addSeparator();

        /*EditMenu.add(group = new JMenuItem("Group"));
        group.addActionListener(this);
        group.setEnabled(false);
        EditMenu.add(unGroup = new JMenuItem("Ungroup"));
        unGroup.addActionListener(this);
        unGroup.setEnabled(false);

        EditMenu.addSeparator();

        EditMenu.add(zoomIn = new JMenuItem("Zoom in"));
        zoomIn.addActionListener(this);
        zoomIn.setEnabled(false);
        EditMenu.add(zoomOut = new JMenuItem("Zoom out"));
        zoomOut.addActionListener(this);
        zoomOut.setEnabled(false);*/
        EditMenu.add(refresh = new JMenuItem("Refresh"));
        refresh.addActionListener(this);
        refresh.setEnabled(false);
        myMenuBar.add(EditMenu);

        // hier menu
        HierarchyMenu = new JMenu("Hierarchy");

        HierarchyMenu.add(descend = new JMenuItem("Descend"));
        descend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
        descend.addActionListener(this);
        descend.setEnabled(false);
        HierarchyMenu.add(ascend = new JMenuItem("Ascend"));
        ascend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
        ascend.addActionListener(this);
        ascend.setEnabled(false);

        myMenuBar.add(HierarchyMenu);

        // Insert Main  Menu
        InsertMenu = new JMenu("Insert");
        InsertMenu.add(insertSubmodule = new JMenuItem("SubModule"));
        insertSubmodule.addActionListener(this);
        insertSubmodule.setEnabled(false);
        InsertMenu.add(insertConnection = new JMenuItem("Connection"));
        insertConnection.addActionListener(this);
        insertConnection.setEnabled(false);
        InsertMenu.add(insertInPort = new JMenuItem("InPort"));
        insertInPort.addActionListener(this);
        insertInPort.setEnabled(false);
        InsertMenu.add(insertOutPort = new JMenuItem("OutPort"));
        insertOutPort.addActionListener(this);
        insertOutPort.setEnabled(false);
        InsertMenu.add(insertFreeText = new JMenuItem("Free text"));
        insertFreeText.addActionListener(this);
        insertFreeText.setEnabled(false);
        myMenuBar.add(InsertMenu);

        //options menu
        OptionsMenu = new JMenu("Options");
        OptionsMenu.add(optionsGraphics = new JMenuItem("Graphics"));
        optionsGraphics.addActionListener(this);
        OptionsMenu.add(optionsPortShapes = new JMenuItem("Port Shapes"));
        optionsPortShapes.addActionListener(this);
        OptionsMenu.add(showGrid = new JCheckBoxMenuItem("Show Grid"));
        showGrid.setEnabled(false);
        showGrid.setSelected(false);
        showGrid.addActionListener(this);
        OptionsMenu.add(snapGrid = new JCheckBoxMenuItem("Snap To Grid"));
        snapGrid.setEnabled(false);
        snapGrid.setSelected(true);
        snapGrid.addActionListener(this);

        myMenuBar.add(OptionsMenu);

        // Tools Main Menu
        ToolsMenu = new JMenu("Tools");
        ToolsMenu.add(libraryManagement = new JMenuItem("Library Management"));
        libraryManagement.addActionListener(this);
        ToolsMenu.addSeparator();

        ToolsMenu.add(iconEditorMenuItem = new JMenuItem("Icon Editor"));
        iconEditorMenuItem.addActionListener(this);
        iconEditorMenuItem.setEnabled(false);
        ToolsMenu.add(nslmEditorMenuItem = new JMenuItem("NSLM Editor"));
        nslmEditorMenuItem.addActionListener(this);
        nslmEditorMenuItem.setEnabled(false);
        ToolsMenu.addSeparator();
        ToolsMenu.add(viewNSLM = new JMenuItem("View NSLM"));
        viewNSLM.addActionListener(this);
        ToolsMenu.addSeparator();
        ToolsMenu.add(buildJava = new JMenuItem("Build"));
        buildJava.addActionListener(this);
        buildJava.setEnabled(false);
        ToolsMenu.add(simulateBuildJava = new JMenuItem("Build and Simulate"));
        simulateBuildJava.addActionListener(this);
        simulateBuildJava.setEnabled(false);
        ToolsMenu.add(simulateJava = new JMenuItem("Simulate"));
        simulateJava.addActionListener(this);
        simulateJava.setEnabled(false);
        ToolsMenu.add(debugBuildJava = new JMenuItem("Build and Debug"));
        debugBuildJava.addActionListener(this);
        debugBuildJava.setEnabled(false);
        ToolsMenu.add(debugJava = new JMenuItem("Debug"));
        debugJava.addActionListener(this);
        debugJava.setEnabled(false);
        /*ToolsMenu.addSeparator();
        ToolsMenu.add(buildC = new JMenuItem("Build C++ Version"));
        buildC.addActionListener(this);
        buildC.setEnabled(false);
        ToolsMenu.add(simulateC = new JMenuItem("Simulate Using C++"));
        simulateC.addActionListener(this);
        simulateC.setEnabled(false);*/

        myMenuBar.add(ToolsMenu);

        /*DbtoolsMenu = new JMenu("DBTools");
        DbtoolsMenu.setEnabled(false);

        BmwMenu = new JMenu("BMW");
        BmwMenu.add(mi = new JMenuItem("BMW Browser&Download"));
        mi.addActionListener(this);
        BmwMenu.add(mi = new JMenuItem("BMW Upload"));
        mi.addActionListener(this);
        DbtoolsMenu.add(BmwMenu);

        ForeignMenu = new JMenu("Foreign DB");
        ForeignMenu.add(mi = new JMenuItem("Foreign DB Manager"));
        mi.addActionListener(this);
        ForeignMenu.add(mi = new JMenuItem("Schematic Linker"));
        mi.addActionListener(this);
        ForeignMenu.add(mi = new JMenuItem("Schematic Viewer"));
        mi.addActionListener(this);
        ForeignMenu.add(mi = new JMenuItem("NSLM Linker"));
        mi.addActionListener(this);
        ForeignMenu.add(mi = new JMenuItem("NSLM Viewer"));
        mi.addActionListener(this);
        DbtoolsMenu.add(ForeignMenu);

        myMenuBar.add(DbtoolsMenu);*/

        HelpMenu = new JMenu("Help");
        HelpMenu.add(help = new JMenuItem("Help"));
        help.addActionListener(this);
        HelpMenu.add(about = new JMenuItem("About"));
        about.addActionListener(this);

        myMenuBar.add(HelpMenu);
        return (myMenuBar);
    }

    private void refreshReopenMenu()
    {
        reopenMenu.removeAll();
        for(int i=0; i< UserPref.recentlyOpened.size(); i++)
        {
            JMenuItem item=new JMenuItem(UserPref.recentlyOpened.get(i));
            item.addActionListener(this);
            reopenMenu.add(item);
        }
    }

    public void resetScrollbarLimits()
    {
        if(currModule!=null && currModule.mySchematic!=null)
        {
            int horizScrollMax = schematicScrollPane.getHorizontalScrollBar().getMaximum()*schematicScrollPane.getHorizontalScrollBar().getUnitIncrement();
            int vertScrollMax = schematicScrollPane.getVerticalScrollBar().getMaximum()*schematicScrollPane.getVerticalScrollBar().getUnitIncrement();
            int maxX=horizScrollMax;
            int minX=0;
            int maxY=vertScrollMax;
            int minY=0;
            for(int i=0; i<currModule.mySchematic.drawableObjs.size(); i++)
            {
                Component comp = currModule.mySchematic.drawableObjs.get(i);
                if(comp instanceof IconInst)
                {
                    if(((IconInst)comp).getXmax()>maxX)
                    {
                        maxX=((IconInst)comp).getXmax()+20;
                    }
                    else if(((IconInst)comp).getXmin()<minX)
                    {
                        minX=((IconInst)comp).getXmin()-20;
                    }
                    if(((IconInst)comp).getYmax()>maxY)
                    {
                        maxY=((IconInst)comp).getYmax()+20;
                    }
                    else if(((IconInst)comp).getYmin()<minY)
                    {
                        minY=((IconInst)comp).getYmin()-20;
                    }
                }
                else if(comp instanceof GraphicPart)
                {
                    if(((GraphicPart)comp).getXmax()>maxX)
                    {
                        maxX=((GraphicPart)comp).getXmax()+20;
                    }
                    else if(((GraphicPart)comp).getXmin()<minX)
                    {
                        minX=((GraphicPart)comp).getXmin()-20;
                    }
                    if(((GraphicPart)comp).getYmax()>maxY)
                    {
                        maxY=((GraphicPart)comp).getYmax()+20;
                    }
                    else if(((GraphicPart)comp).getYmin()<minY)
                    {
                        minY=((GraphicPart)comp).getYmin()-20;
                    }
                }
                else if(comp instanceof Connection)
                {
                    for(int j=0; j<((Connection)comp).numVerticies; j++)
                    {
                        if(((Connection)comp).x[j]>maxX)
                        {
                            maxX=((Connection)comp).x[j]+20;
                        }
                        else if(((Connection)comp).x[j]<minX)
                        {
                            minX=((Connection)comp).x[j]-20;
                        }
                        if(((Connection)comp).y[j]>maxY)
                        {
                            maxY=((Connection)comp).y[j]+20;
                        }
                        else if(((Connection)comp).y[j]<minY)
                        {
                            minY=((Connection)comp).y[j]-20;
                        }
                    }
                }
            }
            if(minX<0 || minY<0)
            {
                currModule.mySchematic.shift(-1*minX, -1*minY);
                maxX+=(-1*minX);
                maxY+=(-1*minY);
            }
            mySchematicPanel.setSize(maxX, maxY);
            mySchematicPanel.setPreferredSize(maxX, maxY);
            mySchematicPanel.repaint();
            schematicScrollPane.repaint();
        }

    }
    /**
     * Perform menu functions according to action events.
     */
    public void actionPerformed(ActionEvent event)
    {
        JMenuItem mi;
        IconInst selIconInst;
        Module selModule;

        String actionLabel = "", parentLabel = "";

        myStatusPanel.setStatusMessage("");

        if (event.getSource() instanceof JButton)
        {
            JButton dmi = (JButton) event.getSource();
            actionLabel = dmi.getActionCommand();
        }
        else if (event.getSource() instanceof JMenuItem)
        {
            mi = (JMenuItem) event.getSource();
            actionLabel = mi.getText();
            if(mi.getParent() instanceof JPopupMenu && ((JPopupMenu)mi.getParent()).getInvoker() instanceof JMenu)
                parentLabel=((JMenu)((JPopupMenu)mi.getParent()).getInvoker()).getText();
        }
        // --------------------------------------------------
        //  File Menu
        // --------------------------------------------------
        // NEW or New
        // --------------------------------------------------
        if ((actionLabel.equals("Module")) || (actionLabel.equals("Model")))
        {
            Module tempModule = newModule(actionLabel);//modifies currModule
            if (tempModule != null)
            {
                currModule.mySchematic = new Schematic();
                while (!hierarchyStack.empty())
                {
                    hierarchyStack.pop();
                }
                tempModule.addDirtyListener(this);
                setModifyMenuEnabled(true);
                save.setEnabled(true);
                maintoolbar.saveButton.setEnabled(true);
                setSimulateJavaAccessibility();
                SchematicPanel.currBackgroundCol = UserPref.drawBack_col;
                mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                mySchematicPanel.repaint();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Open") || parentLabel.equals("Reopen"))
        {
            Module m;
            if(actionLabel.equals("Open"))
                m=openModule();
            else
            {
                StringTokenizer tokenizer=new StringTokenizer(actionLabel, ".");
                String libraryName=tokenizer.nextToken();
                String moduleName=tokenizer.nextToken();
                String version=tokenizer.nextToken();
                m=openModule(libraryName, moduleName, version);
            }
            if (m != null)
            {
                setModifyMenuEnabled(true);
                currModule.addDirtyListener(this);
                save.setEnabled(false);
                maintoolbar.saveButton.setEnabled(false);
                setSimulateJavaAccessibility();
                //start new hierarchy stack
                while (!hierarchyStack.empty())
                {
                    hierarchyStack.pop();
                }
                SchematicPanel.currBackgroundCol = UserPref.drawBack_col;
                mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                if(!currModule.mySchematic.getMethodsSet())
                {
                	new NslmEditorFrame(this).loadMethodParts();
                }
                
                mySchematicPanel.repaint();
                resetScrollbarLimits();
                if(currModule.moduleType.equals("NslMatlabModule"))
                    saveAs.setEnabled(false);
                else
                    saveAs.setEnabled(true);
                UserPref.addToRecentlyOpened(currModule.libNickName, currModule.moduleName, currModule.versionName);
                refreshReopenMenu();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Save"))
        {
            //error messages given from saveModule
            if (currModule == null)
            {
                String errstr = "There is no module opened yet.";
                JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                saveModule();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Save as"))
        {
            //error messages given from saveAsModule
            if (saveAsModule())
            {
                currModule.addDirtyListener(this);
                // Disable save options since already saved
                save.setEnabled(false);
                maintoolbar.saveButton.setEnabled(false);
                SchematicPanel.currBackgroundCol = UserPref.drawBack_col;
                mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Close"))
        {
            if (closeModule(true))
            {
                currModule = null;

                //if there is a sch on the stack, go there
                if (!hierarchyStack.empty())
                {
                    currModule = hierarchyStack.pop();
                    if(currModule.mySchematic!=null)
                        currModule.mySchematic.refresh();
                    if (hierarchyStack.empty())
                    {
                        ascend.setEnabled(false);
                        maintoolbar.upButton.setEnabled(false);
                    }
                    try
                    {
                        currDirStr = SCSUtility.getSrcPathUsingNick(currModule.libNickName, currModule.moduleName, currModule.versionName);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    setSimulateJavaAccessibility();
                    mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                    mySchematicPanel.repaint();
                    if(currModule.isDirty())
                    {
                        save.setEnabled(true);
                        maintoolbar.saveButton.setEnabled(true);
                    }
                    else
                    {
                        save.setEnabled(false);
                        maintoolbar.saveButton.setEnabled(false);
                    }
                }
                else
                {
                    currDirStr=null;
                    setModifyMenuEnabled(false);
                    setSimulateJavaAccessibility();
                    save.setEnabled(false);
                    maintoolbar.saveButton.setEnabled(false);
                    mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
                    mySchematicPanel.repaint();
                }
            }
        }
        //------------------------------------------------------
        else if (actionLabel.equals("Print"))
        {
            PrintJob pjob = getToolkit().getPrintJob(this, "Printing Test", null);
            if (pjob != null)
            {
                Graphics pg = pjob.getGraphics();
                if (pg != null)
                {
                    mySchematicPanel.paintChildren(pg);
                    pg.dispose();
                }
                pjob.end();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Quit"))
        {
            if (exitTool())
                exitForGood(this);
        }
        // --------------------------------------------------
        // Edit Menu
        // --------------------------------------------------
        // --------------------------------------------------
        else if (actionLabel.equals("Undo"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Redo"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Cut"))
        {
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("There's no open module.");
            }
            else
            {
                //dirty=true;
                // SAVE THE DELETED COMPONENT (icon,text,or port) however
                // interconnect not saved
                clipboardComponent = mySchematicPanel.deleteComp();
                setPasteEnabled(true);
                mySchematicPanel.repaint();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Copy"))
        {
            if (currModule != null && currModule.mySchematic.selComponent != null)
            {
                if (currModule.mySchematic.selComponent instanceof scs.graphics.Icon)
                {
                    try
                    {
                        clipboardComponent = ((scs.graphics.Icon) currModule.mySchematic.selComponent).clone();
                        setPasteEnabled(true);
                    }
                    catch(Exception e)
                    {}
                }
                else if (currModule.mySchematic.selComponent instanceof GraphicPart_text)
                {
                    try
                    {
                        clipboardComponent = ((GraphicPart_text) currModule.mySchematic.selComponent).clone();
                        setPasteEnabled(true);
                    }
                    catch(Exception e)
                    {}
                }
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Paste"))
        {
            // check for the type of clipboard object
            // paste only if an icon or text
            if (clipboardComponent instanceof scs.graphics.Icon)
            {
                try
                {
                    currModule.mySchematic.addDrawableObj(((scs.graphics.Icon) clipboardComponent).clone());
                    currModule.setDirty(true);
                }
                catch(Exception e)
                {
                }
                repaint();
            }
            else if (clipboardComponent instanceof GraphicPart_text)
            {
                try
                {
                    currModule.mySchematic.addDrawableObj(((GraphicPart_text) clipboardComponent).clone());
                    currModule.setDirty(true);
                    repaint();
                }
                catch(Exception e)
                {}
            }
            else
            {
                System.err.println("Error:SchEditorFrame:Cannot  paste  this component ");
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Clear"))
        {
            if (currModule == null)
            {
                String errstr="Nothing to clear.";
                JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                currModule.mySchematic.deleteAllDrawableObjs();
                currModule.setDirty(true);
                mySchematicPanel.repaint();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Select all"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Group"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Ungroup"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Zoom in"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Zoom out"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Refresh"))
        {
            mySchematicPanel.repaint();
        }

        // --------------------------------------------------
        // Hierarchy Menu
        // --------------------------------------------------
        // --------------------------------------------------
        else if (actionLabel.equals("Descend"))
        {
            descend();
        }

        // --------------------------------------------------
        else if (actionLabel.equals("Ascend"))
        {
            boolean ascend=true;
            if(currModule.isDirty())
            {
                if (!closingOpenModule())
                    ascend=false;
            }
            if(ascend)
                ascend();
        }

        // --------------------------------------------------
        // Insert Menu
        // --------------------------------------------------
        else if (actionLabel.equals("SubModule"))
        {
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
            }
            else
            {
                myStatusPanel.setStatusMessage("Insert Submodule mode ");
                insertSubmodule();
            }
        }

        // --------------------------------------------------
        else if (actionLabel.equals("InPort"))
        {
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
            }
            else
            {
                //dirty=true;
                myStatusPanel.setStatusMessage("Insert Port mode ");
                insertPort("InputPort");
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("OutPort"))
        {
            //dirty=true;
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
            }
            else
            {
                myStatusPanel.setStatusMessage("Insert Port mode ");
                insertPort("OutputPort");
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Connection"))
        {
            //dirty=true;
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
            }
            else
            {
                if(status.equals("Connection"))
                {
                    status="";
                }
                else
                {
                    myStatusPanel.setStatusMessage("Insert Connection  mode ");
                    status = "Connection";
                }
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Free text"))
        {
            //dirty=true;
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }
            myStatusPanel.setStatusMessage("Insert Free Text mode ");

            String text = (String)JOptionPane.showInputDialog(this, "Enter Text", "Free Text",
                                                                   JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (text!=null)
            {
                status = "insert_text";
                SchematicPanel.freeTextString = text;
            }
            else
            {
                status = "nothing";
            }
        }
        // --------------------------------------------------
        // Options Menu
        // --------------------------------------------------

        else if (actionLabel.equals("Graphics"))
        {
            GraphicsOptionsDialog opt=new GraphicsOptionsDialog(this);
            opt.setSize(600, 450);
            opt.setLocation(200, 200);
            opt.setVisible(true);
            mySchematicPanel.setBackground(SchematicPanel.currBackgroundCol);
            mySchematicPanel.repaint();
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Port Shapes"))
        {
            PortOptionsDialog opt=new PortOptionsDialog(this);
            opt.setSize(300, 125);
            opt.setLocation(400, 200);
            opt.setVisible(true);
            mySchematicPanel.repaint();
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Show Grid"))
        {
            mySchematicPanel.showGrid= showGrid.getState();
            mySchematicPanel.repaint();
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Snap To Grid"))
        {
            mySchematicPanel.snapToGrid= snapGrid.getState();
        }

        // --------------------------------------------------
        //  Tools Menu
        // --------------------------------------------------
        else if (actionLabel.equals("Library Management"))
        {
            LibraryManagerFrame frame = new LibraryManagerFrame(this);
            frame.pack();
            frame.setSize(900, 300);
            frame.setVisible(true);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Icon Editor"))
        {
            IconEditorFrame iconEF;
            selIconInst = null;

            if (currModule != null && currModule.mySchematic != null &&
                    currModule.mySchematic.selComponent instanceof IconInst)
            {
                selIconInst = (IconInst) currModule.mySchematic.selComponent;
            }
            if (selIconInst != null)
            {
                try
                {
                    selModule = selIconInst.getModuleFromFile();
                }
                catch (IOException e)
                {
                    String errstr="Cannot find module for " + selIconInst.moduleName;
                    JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (ClassNotFoundException e)
                {
                    String errstr="IOException for file corresponding to " + selIconInst.moduleName;
                    JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (SAXException e)
                {
                    String errstr="SAXException for file corresponding to " + selIconInst.moduleName;
                    JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (ParserConfigurationException e)
                {
                    String errstr="ParserConfigurationException for file corresponding to " + selIconInst.moduleName;
                    JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else
            {
                selModule = currModule;
            }
            if(selModule!=null)
            {
                iconEditor = new IconEditorFrame(this);
                iconEditor.finishOpen();
                iconEditor.setSize(800, 700);
                iconEditor.setVisible(true);
                iconEditor.myIconPanel.requestFocus();
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("NSLM Editor"))
        {
            selModule = getSelectedModule();

            if(selModule!=null)
            {
                nslmEditor = new NslmEditorFrame(this);
                nslmEditor.setVisible(true);
            }

        }
        // --------------------------------------------------
        //note:we also have NSLM Viewer
        else if (actionLabel.equals("View NSLM"))
        {
            String currFileStr = "";
            if (currModule != null && currModule.moduleName != null &&
                    currModule.moduleName.length() != 0)
            {
                currFileStr = currModule.moduleName + ".mod";
            }
            TextViewer myTextViewer = new TextViewer();
            if ((currDirStr == null || currDirStr.length()==0) && currModule!=null)
            {
                try
                {
                    currDirStr = SCSUtility.getLibPath(currModule.libNickName);
                }
                catch(IOException e)
                {}
            }
            if (currDirStr.length() == 0)
            {
                currDirStr = SCSUtility.user_home;
            }
            myTextViewer.display(currDirStr, currFileStr);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Build"))
        {
            if (currModule == null)
            {
                myStatusPanel.setWarningMessage("Current module is still empty yet.");
                return;
            }

            selModule=getSelectedModule();

            try
            {
                String srcPath=SCSUtility.getSrcPathUsingNick(selModule.libNickName, selModule.moduleName,
                                                              selModule.versionName);
                String xmlPath=SCSUtility.getFullPathToXml(srcPath,selModule.moduleName);
                selModule.save(srcPath, xmlPath);
            }
            catch(Exception e)
            {}
            if(selModule!=null)
                NslJavaInterface.build(selModule, this);

            setSimulateJavaAccessibility();
        }
        else if (actionLabel.equals("Build and Simulate"))
        {
            if (currModule!=null && currModule.moduleType.equals("NslModel"))
            {
                if(currModule.isDirty())
                {
                    String xmlPath=SCSUtility.getFullPathToXml(currDirStr,currModule.moduleName);
                    try
                    {
                        currModule.save(currDirStr,xmlPath);
                    }
                    catch(Exception e)
                    {}
                }
                NslJavaInterface.build(currModule, this);
                NslJavaInterface.simulateUsingJava(currModule.libNickName, currModule.moduleName,
                                                   currModule.versionName, currDirStr, 0);
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Simulate"))
        {
            if (currModule!=null && currModule.moduleType.equals("NslModel"))
            {
                if(currModule.isDirty())
                {
                    String xmlPath=SCSUtility.getFullPathToXml(currDirStr,currModule.moduleName);
                    try
                    {
                        currModule.save(currDirStr,xmlPath);
                    }
                    catch(Exception e)
                    {}
                }
                NslJavaInterface.simulateUsingJava(currModule.libNickName, currModule.moduleName,
                                                   currModule.versionName, currDirStr, 0);
            }
        }
        else if (actionLabel.equals("Build and Debug"))
        {
            startedStep = false;
            if (currModule!=null && currModule.moduleType.equals("NslModel"))
            {
                if(currModule.isDirty())
                {
                    String xmlPath=SCSUtility.getFullPathToXml(currDirStr,currModule.moduleName);
                    try
                    {
                        currModule.save(currDirStr,xmlPath);
                    }
                    catch(Exception e)
                    {}
                }
                NslJavaInterface.build(currModule, this);
                NslJavaInterface.simulateUsingJava(currModule.libNickName, currModule.moduleName,
                                                   currModule.versionName, currDirStr, 1);

                if(debugtoolbar == null)
                {
                    debugtoolbar = new DebugToolBar(mySchematicPanel);
                    edittoolbar.add(debugtoolbar);
                    edittoolbar.updateUI();
                }
                else
                {
                    debugtoolbar.setVisible(true);
                }
                debugMode = true;
            }
        }
        else if (actionLabel.equals("Debug"))
        {
            startedStep = false;
            if (currModule!=null && currModule.moduleType.equals("NslModel"))
            {
                if(currModule.isDirty())
                {
                    String xmlPath=SCSUtility.getFullPathToXml(currDirStr,currModule.moduleName);
                    try
                    {
                        currModule.save(currDirStr,xmlPath);
                    }
                    catch(Exception e)
                    {}
                }
                NslJavaInterface.simulateUsingJava(currModule.libNickName, currModule.moduleName,
                                                   currModule.versionName, currDirStr, 1);

                if(debugtoolbar == null)
                {
                    debugtoolbar = new DebugToolBar(mySchematicPanel);
                    edittoolbar.add(debugtoolbar);
                    edittoolbar.updateUI();
                }
                else
                {
                    debugtoolbar.setVisible(true);
                }
                debugMode = true;
            }
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Build C++ Version"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Simulate Using C++"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("BMW Browser&Download"))
        {
            try
            {
                BrowserLauncher.openURL("http://java.usc.edu/bmw_dev/apb/webdriver?MIval=/index.html");
            }
            catch (IOException ioe)
            {
                String errstr="Browser failed to launch.";
                JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // --------------------------------------------------
        // --------------------------------------------------
        else if (actionLabel.equals("Foreign DB Manager"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Schematic Linker"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("Schematic Viewer"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("NSLM Linker"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        else if (actionLabel.equals("NSLM Viewer"))
        {
            String errstr="Command not implemented yet:  " + actionLabel;
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        // --------------------------------------------------
        // Help Menu
        // --------------------------------------------------
        else if (actionLabel.equals("Help"))
        {
            BrowserLauncher.openUrl("http://neuroinformatics.usc.edu/mediawiki/index.php/Schematic_Editor");
        }
    } //end action Performed

    public void ascend()
    {
        if (hierarchyStack.empty())
        {
            myStatusPanel.setWarningMessage("You have reached the top most level.");
            return;
        }
        closeEditors();
        currModule = hierarchyStack.pop();
        if(currModule.mySchematic!=null)
        {
            currModule.mySchematic.refresh();
        }
        if (hierarchyStack.empty())
        {
            ascend.setEnabled(false);
            maintoolbar.upButton.setEnabled(false);
        }
        if(currModule.isDirty())
        {
            save.setEnabled(true);
            maintoolbar.saveButton.setEnabled(true);
        }
        else
        {
            save.setEnabled(false);
            maintoolbar.saveButton.setEnabled(false);
        }
        try
        {
            currDirStr = SCSUtility.getSrcPathUsingNick(currModule.libNickName, currModule.moduleName, currModule.versionName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setTitle("Schematic Editor: Module " + currModule.moduleName);
        currentInstanceName = currModule.instanceName;
        setSimulateJavaAccessibility();
        mySchematicPanel.repaint();
    }

    public void descend()
    {
        IconInst selIconInst;
        if (currModule == null)
        {
            myStatusPanel.setWarningMessage("There's no module opened yet.");
            return;
        }
        if (currModule.mySchematic.selComponent == null || !(currModule.mySchematic.selComponent instanceof IconInst))
        {
            myStatusPanel.setWarningMessage("No module selected yet.");
            return;
        }
        hierarchyStack.push(currModule);
        ascend.setEnabled(true);
        maintoolbar.upButton.setEnabled(true);
        if (!(currModule.mySchematic.selComponent instanceof IconInst))
        {
            //if port
            myStatusPanel.setWarningMessage("Can only descend into modules.");
            return;
        }
        closeEditors();
        selIconInst = (IconInst) currModule.mySchematic.selComponent;
        String oldInstanceName = currentInstanceName;
        currentInstanceName = selIconInst.instanceName;
        Module tempModule = new Module();
        try
        {
            tempModule.getModuleFromFileUsingNick(selIconInst.libNickName, selIconInst.moduleName, selIconInst.versionName);
        }
        catch (ClassNotFoundException e)
        {
            String errStr = "ClassNotFoundException: " + selIconInst.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            currentInstanceName = oldInstanceName;
            return;
        }
        catch (FileNotFoundException e)
        {
            String errStr = "FileNotFoundException: " + selIconInst.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            currentInstanceName = oldInstanceName;
            return;
        }
        catch (IOException e)
        {
            String errStr = "IOException: " + selIconInst.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            currentInstanceName = oldInstanceName;
            return;
        }
        catch (ParserConfigurationException e)
        {
            String errStr = "ParserConfigurationException: " + selIconInst.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            currentInstanceName = oldInstanceName;
            return;
        }
        catch (SAXException e)
        {
            String errStr = "SAXException: " + selIconInst.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            currentInstanceName = oldInstanceName;
            return;
        }
        currModule = tempModule;
        currModule.instanceName = currentInstanceName;
        currModule.addDirtyListener(this);
        if(currModule.isDirty())
        {
            save.setEnabled(true);
            maintoolbar.saveButton.setEnabled(true);
        }
        else
        {
            save.setEnabled(false);
            maintoolbar.saveButton.setEnabled(false);
        }
        try
        {
            currDirStr = SCSUtility.getSrcPathUsingNick(currModule.libNickName, currModule.moduleName, currModule.versionName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setTitle("Schematic Editor: Module " + tempModule.moduleName);
        setSimulateJavaAccessibility();
        if(!currModule.mySchematic.getMethodsSet())
        {
        	new NslmEditorFrame(this).loadMethodParts();
        }
        mySchematicPanel.repaint();
    }

    protected void closeEditors()
    {
        if(nslmEditor!=null && nslmEditor.isVisible())
            nslmEditor.dispose();
        if(iconEditor!=null && iconEditor.isVisible())
            iconEditor.dispose();
    }

    public Module getSelectedModule()
    {
        IconInst selIconInst;
        Module selModule;
        selIconInst = null;

        if (currModule != null && currModule.mySchematic != null &&
                currModule.mySchematic.selComponent instanceof IconInst)
        {
            selIconInst = (IconInst) currModule.mySchematic.selComponent;
        }
        if (selIconInst != null)
        {
            try
            {
                selModule = selIconInst.getModuleFromFile();
            }
            catch (IOException e)
            {
                String errStr = "Cannot find module for " + selIconInst.moduleName;
                JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                selModule=null;
            }
            catch (ClassNotFoundException e)
            {
                String errStr = "IOException " + selIconInst.moduleName;
                JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                selModule=null;
            }
            catch (SAXException e)
            {
                String errStr = "SAXExceptionr " + selIconInst.moduleName;
                JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                selModule=null;
            }
            catch (ParserConfigurationException e)
            {
                String errStr = "ParserConfigurationException " + selIconInst.moduleName;
                JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                selModule=null;
            }
        }
        else
        {
            selModule = currModule;
        }
        return selModule;
    }

    /**
     * Called whenever a module has been modified and the changes havent yet been saved
     */
    public void moduleDirtied()
    {
        save.setEnabled(true);
        maintoolbar.saveButton.setEnabled(true);
    }

    /**
     * Called whenever a module has been cleaned
     */
    public void moduleCleaned()
    {
        save.setEnabled(false);
        maintoolbar.saveButton.setEnabled(false);
    }

    private void setModifyMenuEnabled(boolean enabled)
    {
        saveAs.setEnabled(enabled);
        close.setEnabled(enabled);
        print.setEnabled(enabled);
        clear.setEnabled(enabled);
        refresh.setEnabled(enabled);
        insertSubmodule.setEnabled(enabled);
        insertConnection.setEnabled(enabled);
        insertInPort.setEnabled(enabled);
        insertOutPort.setEnabled(enabled);
        insertFreeText.setEnabled(enabled);
        nslmEditorMenuItem.setEnabled(enabled);
        iconEditorMenuItem.setEnabled(enabled);
        buildJava.setEnabled(enabled);
        snapGrid.setEnabled(enabled);
        showGrid.setEnabled(enabled);
        maintoolbar.setModifyMenuEnabled(enabled);
    }

    public void setDescendEnabled(boolean enabled)
    {
        descend.setEnabled(enabled);
        maintoolbar.downButton.setEnabled(enabled);
    }

    public void setCopyEnabled(boolean enabled)
    {
        copy.setEnabled(enabled);
        edittoolbar.setCopyEnabled(enabled);
    }

    public void setCutEnabled(boolean enabled)
    {
        cut.setEnabled(enabled);
        edittoolbar.setCutEnabled(enabled);
    }

    public void setPasteEnabled(boolean enabled)
    {
        paste.setEnabled(enabled);
        edittoolbar.setPasteEnabled(enabled);
    }

    private void setSimulateJavaAccessibility()
    {
        if (currModule == null)
        {
            simulateJava.setEnabled(false);
            simulateBuildJava.setEnabled(false);
            debugJava.setEnabled(false);
            debugBuildJava.setEnabled(false);
        }
        else if (currModule.moduleType.equals("NslModel"))
        {
            simulateJava.setEnabled(true);
            simulateBuildJava.setEnabled(true);
            debugJava.setEnabled(true);
            debugBuildJava.setEnabled(true);
        }
        else
        {
            simulateJava.setEnabled(false);
            simulateBuildJava.setEnabled(false);
            debugJava.setEnabled(false);
            debugBuildJava.setEnabled(false);
        }
    }

    private boolean startedStep = false;
    public void step(int st)
    {

        if(!startedStep)
        {
            NslCmdInit x = new NslCmdInit();
            x.execute();
            startedStep = true;
        }

        //NslScheduler scheduler = nslj.src.cmd.NslCmd.system.getScheduler();
        //scheduler.stepModule(steps);
        //NslSystem ns = NslHierarchy.nslGetSystem();
        NslSystem.runSemaphore++;
        if(NslHierarchy.system.getCurTime() < NslHierarchy.system.getEndTime())
        {
            NslCmdStepCycle.execute(st);
            // CURRENTLY DOES BUSY WAITING
            // NSL should be updated with a static synchronized method
            // for managing the semaphore
            // and then the wait() function can be used to prevent busy waiting
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {

            }
            while(NslSystem.runSemaphore > 0)
            {
//            try {
//                if(ns.runSemaphore > 0)
//                   // SEMAPHORE.UP METHOD should be added
//            } catch (InterruptedException ex) {
//                System.err.println("unexpected interruption in SchEditorFrame");
//                ex.printStackTrace();
//            }
            }

            //JOptionPane.showMessageDialog(this, "system.runSemaphore: " + ns.runSemaphore);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Simulation has reached end.");

        }
        this.requestFocus();
        this.mySchematicPanel.requestFocus();
    }


    //------------------------------------
    // insertPort
    // very similary to the one in IconToolBox
    //------------------------------------
    public void insertPort(String dialogType)
    {
        Declaration var;
        PortDialog dialog = new PortDialog(this, currModule, dialogType);

        var = currModule.fillVariableName(this, dialogType, "Port Instance Name(first letter lower case): ");

        if (var == null)
            return;

        dialog.display(var);
        if (dialog.okPressed)
        {
            dialog.fillDeclarationInfo(var);
            // add to nsl
            currModule.addVariable(var);
            if(currModule.myIcon==null)
            {
                this.currModule.myIcon=new scs.graphics.Icon(currModule.libNickName, currModule.moduleName, currModule.versionName);
                GraphicPart_rect mainIcon=new GraphicPart_rect(50, 50, UserPref.rect_col);
                this.currModule.myIcon.addDrawablePart(mainIcon);
                mainIcon.movepoint(100,100);
            }

            if (dialogType.equals("InputPort"))
            {
                // add to icon
                IconInport inport=new IconInport(var, 0, 0, Color.green);
                currModule.myIcon.addDrawablePart(inport);
                currModule.myIcon.organizePorts();
                // add to schematic
                currModule.mySchematic.addDrawableObj(new SchematicInport(var));
            }
            else if (dialogType.equals("OutputPort"))
            {
                // add to icon
                IconOutport outport=new IconOutport(var, 0, 0, Color.green);
                currModule.myIcon.addDrawablePart(outport);
                currModule.myIcon.organizePorts();
                // add to schematic
                currModule.mySchematic.addDrawableObj(new SchematicOutport(var));
            }

            // Add port to NslmEditor if module is open in it
            if(nslmEditor!=null && nslmEditor.isVisible())
                nslmEditor.addVariable(var);
            // Add port to IconEditor if module is open in it
            if(iconEditor!=null && iconEditor.isVisible())
                iconEditor.myIconPanel.repaint();
            this.mySchematicPanel.repaint();
        }
    } //end insertPort

    //------------------------------------
    // insertSubmodule
    // very similary to the one in NslmEditorFrame
    //------------------------------------
    public void insertSubmodule()
    {
        Module iconModule;
        IconInst tempIconInst;
        Declaration var;

        var = currModule.fillVariableName(this, "SubModule", "SubModule Instance Name (first letter lower case): ");
        if (var == null)
            return;

        SubmoduleDialog dialog=new SubmoduleDialog(this, currModule, "SubModule");
        dialog.display(var);
        if (!dialog.okPressed)
        {
            return;
        }
        dialog.fillDeclarationInfo(var);

        //everything is in var except libraryPath
        String libraryPath = dialog.getLibraryPath();

        // Now Create the scs.graphics.Icon Inst
        iconModule = new Module();
        try
        {
            iconModule.getModuleFromFile(libraryPath, var.varType, var.modVersion);
        }
        catch (ClassNotFoundException e)
        {
            String errStr = "Class Not Found: " + libraryPath;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (IOException e)
        {
            String errStr = "IOException on " + libraryPath;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (ParserConfigurationException e)
        {
            String errStr="ParserConfigurationException on "+libraryPath;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        catch (SAXException e)
        {
            String errStr="SAXException on "+libraryPath;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (iconModule.myIcon == null)
        {
            String errStr = "Module's Icon is null in module: " + iconModule.moduleName;
            JOptionPane.showMessageDialog(null, errStr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (iconModule.myIcon.moduleName == null)
        {
            String errstr="Module's Icon name is null";
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tempIconInst = new IconInst(var.varName, iconModule.myIcon, var.varParams);

        if (iconModule.moduleName == null)
        {
            return;
        }
        if (this.currModule == null)
        {
            return;
        }

        int vari = currModule.findVarIndex(var.varName);
        int iconi = currModule.mySchematic.findDrawableIndex(var.varName);
        // all new
        if ((vari == -1) && (iconi == -1))
        {
            this.currModule.mySchematic.addIconInst(tempIconInst);
            this.mySchematicPanel.repaint();

            currModule.addVariable(var);

            // Add port to NslmEditor if module is open in it
            if(nslmEditor!=null && nslmEditor.isVisible())
                nslmEditor.addVariable(var);
        }
        // already in variable list
        else if ((vari != -1) && (iconi == -1))
        {
            if (var.varDialogType.equals("SubModule"))
            {
                this.currModule.mySchematic.addIconInst(tempIconInst);
                this.mySchematicPanel.repaint();
                this.currModule.setDirty(true);
            }
        }
        // already in icon list
        else if (vari == -1)
        {
            //should have already been in variable list
            currModule.addVariable(var);

            // Add port to NslmEditor if module is open in it
            if(nslmEditor!=null && nslmEditor.isVisible())
                nslmEditor.addVariable(var);
        }
        // already in both lists
        else
        {
            String errstr="Same name. Cannot add.";
            JOptionPane.showMessageDialog(null, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
    }//end insertSubmodule

    public String getCurrModuleName()
    {
        return currModule.libNickName;
        //return currModule.moduleType; "NslModule"
        //return currModule.versionName; "1_1_1"
        //return currModule.getName(); "null"
        //return currModule.moduleName;
    }

    public String getCurrentInstanceName()
    {
        if(currentInstanceName.length() == 0)
            return currModule.moduleName;

        return currentInstanceName;
    }

    /**
     * Handle windowClosing event.
     */
    public void windowClosing(WindowEvent event)
    {
        Window w = event.getWindow();
        if (currModule == null)
        {
            exitForGood(w);
            return;
        }
        //if you are exiting the Schematic editor
        //everything else is going down too!
        if(currModule.isDirty())
        {
            if (!closingOpenModule())
                return;
        }
        exitForGood(w);
    }

    // --------------------------------------------------
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
            mySchematicPanel.requestFocus();
        }
    }


    public boolean exitForGood(Window w)
    {
        this.mySchematicPanel.debugClearAll();
        w.dispose();
        System.exit(0);
        return false;
    }

    /**
     * newModule
     * @param modelOrModuleStr - "Model" or "Module"
     * @return - new module
     */
    public Module newModule(String modelOrModuleStr)
    {
        Module returnModule;
        if (this.currModule != null && this.currModule.isDirty())
        {
            if (!closingOpenModule())
                return (null);
        }
        NewModuleDialog nmDialog=new NewModuleDialog(this, "Create New "+modelOrModuleStr, modelOrModuleStr, true);
        nmDialog.setLocation(new Point(200, 200));
        nmDialog.pack();
        nmDialog.setSize(500, 200);
        nmDialog.setVisible(true);
        if (nmDialog.getPushedBtn().equals("ok"))
        {
            if(nmDialog.getType().equals("NslMatlabModule"))
            {
                returnModule = new MatlabModule(SCSUtility.sifVersionNum, nmDialog.getLibraryName(),
                    nmDialog.getModuleName(), nmDialog.getVersionName(), nmDialog.getType(),
                    nmDialog.getArguments(), nmDialog.isBuffering());
            }
            else
            {
                returnModule = new Module(SCSUtility.sifVersionNum, nmDialog.getLibraryName(),
                    nmDialog.getModuleName(), nmDialog.getVersionName(), nmDialog.getType(),
                    nmDialog.getArguments(), nmDialog.isBuffering());
            }
            currModule = endNewModule(returnModule, nmDialog.getSourcePath());
            return currModule;
        }
        return null;
    }

    private Module endNewModule(Module returnModule, String sourcePath)
    {
        this.currModule = returnModule;
        this.currModule.myNslm = new NSLM();
        this.currModule.myIcon = new scs.graphics.Icon(currModule.libNickName, currModule.moduleName, currModule.versionName);
        GraphicPart_rect mainIcon = new GraphicPart_rect(50, 50, UserPref.rect_col);
        this.currModule.myIcon.addDrawablePart(mainIcon);
        mainIcon.movepoint(100, 100);
        this.currDirStr = sourcePath;
        this.setTitle("Schematic Editor: " + this.currModule.moduleName);
        this.myStatusPanel.setStatusMessage("creating " + currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
        return (currModule);
    }

    /**
     * openModule
     * @return a Module
     */
    public Module openModule()
    {
        if (this.currModule != null && this.currModule.isDirty())
        {
            if (!closingOpenModule())
                return (null);
            
        }
        try
        {
            ModuleSelectorDialog selectorDialog = new ModuleSelectorDialog(this);
            selectorDialog.setLocation(new Point(200, 200));
            selectorDialog.pack();
            selectorDialog.setSize(700, 275);
            selectorDialog.setVisible(true);
            if(selectorDialog.getPushedBtn().equals("ok"))
            {
                String moduleName = selectorDialog.getModuleName();
                String sourcePath = selectorDialog.getSourcePath();
                return endOpenModule(SCSUtility.openModule(sourcePath, moduleName), sourcePath);
            }
        }
        catch (ClassNotFoundException e)
        {
            String errstr = "openModule: problem with lib - ClassNotFoundException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch (FileNotFoundException e)
        {
            String errstr = "openModule: problem with lib - FileNotFoundException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch (IOException e)
        {
            String errstr = "openModule: problem with lib - IOException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch (ParserConfigurationException e)
        {
            String errstr = "openModule: problem with lib - ParserConfigurationException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch (SAXException e)
        {
            String errstr = "openModule: problem with lib - SAXException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return null;
    }

    /**
     * Check whether to save and close, close without saving or cancel
     * @return - true if the module was closed
     */
    protected boolean closingOpenModule()
    {
        String errstr = "Current module is not saved.";
        Object[] options = {"Save and Close",
                            "Close Without Saving",
                            "Cancel"};
        int n = JOptionPane.showOptionDialog(this,
            errstr,
            "Closing module",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if(n==JOptionPane.YES_OPTION)
        {
            saveModule();
            return closeModule(false);
        }
        else if(n==JOptionPane.NO_OPTION)
        {
            return closeModule(false);
        }
        return false;
    }


    public Module openModule(String libraryName, String moduleName, String version)
    {
        if (this.currModule != null && this.currModule.isDirty())
        {
            if (!closingOpenModule())
                return (null);
        }
        try
        {
            String sourcePath=SCSUtility.getSrcPathUsingNick(libraryName, moduleName, version);
            return endOpenModule(SCSUtility.openModule(sourcePath, moduleName), sourcePath);
        }
        catch (ClassNotFoundException e)
        {
            String errstr = "openModule: problem with lib - ClassNotFoundException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (FileNotFoundException e)
        {
            String errstr = "openModule: problem with lib - FileNotFoundException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e)
        {
            String errstr = "openModule: problem with lib - IOException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (ParserConfigurationException e)
        {
            String errstr = "openModule: problem with lib - ParserConfigurationException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (SAXException e)
        {
            String errstr = "openModule: problem with lib - SAXException";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * End open module
     * @param returnModule - opened module
     * @param sourcePath - path to module source
     * @return - opened module
     */
    protected Module endOpenModule(Module returnModule, String sourcePath)
    {
        this.currModule = returnModule;
        this.currDirStr = sourcePath;

        this.setTitle("Schematic Editor: " + this.currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
        this.myStatusPanel.setStatusMessage("Opened " + this.currModule.moduleName);
        return (currModule);
    }

    /**
     * saveModule
     */
    public void saveModule()
    {
        String xmlFilePath= SCSUtility.getFullPathToXml(currDirStr, currModule.moduleName);
        try
        {
            currModule.save(currDirStr,xmlFilePath);
        }
        catch (Exception e)
        {
            String errstr = "save:Exception: " + xmlFilePath;
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * saveAsModule
     * @return successful
     */
    public boolean saveAsModule()
    {
        String xmlFilePath;
        Module saveToModule;

        if (this.currModule == null)
        {
            String errstr="saveAsModule:There's no module opened yet.";
            JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        NewModuleDialog nmDialog=new NewModuleDialog(this, "Save As", "Module", false);
        nmDialog.setLocation(new Point(200, 200));
        nmDialog.pack();
        nmDialog.setSize(500, 200);
        nmDialog.setLibraryName(currModule.libNickName);
        nmDialog.setArguments(currModule.arguments);
        nmDialog.setBuffering(currModule.buffering);
        nmDialog.addType("NslModel");
        nmDialog.setType(currModule.moduleType);
        nmDialog.setVisible(true);

        if (nmDialog.getPushedBtn().equals("ok"))
        {

            if (nmDialog.getModuleName() == null)
            {
                String errstr = "New module name is null ";
                JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            xmlFilePath = SCSUtility.getFullPathToXml(nmDialog.getSourcePath(), nmDialog.getModuleName());
            //try and merge the modules
            try
            {
                File newSrcDir = new File(nmDialog.getSourcePath());
                if (!newSrcDir.isDirectory())
                {
                    newSrcDir.mkdirs();
                }
                //New Module
                currModule.setHeaderOfModule(nmDialog.getLibraryName(), nmDialog.getModuleName(),
                        nmDialog.getVersionName(), nmDialog.getType(), nmDialog.getArguments(),
                        nmDialog.isBuffering(), currModule.imports, currModule.variables);
                saveToModule = currModule; //because of endSaveAs
                this.currModule.save(nmDialog.getSourcePath(), xmlFilePath);
            }
            catch (IOException e)
            {
                String errstr="saveAsModule:IOException: " + xmlFilePath;
                JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            catch (BadLocationException e)
            {
                String errstr="saveAsModule:BadLocationException " + xmlFilePath;
                JOptionPane.showMessageDialog(this, errstr, "SchEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            endSaveAs(saveToModule);
            return true;
        }
        return false;
    }

    /**
     * End Save As
     * @param returnModule - saved module
     */
    private void endSaveAs(Module returnModule)
    {
        this.currModule = returnModule;
        this.setTitle("Schematic Editor: " + this.currModule.moduleName);
        this.myStatusPanel.clearWarningMessage();
    }

    /**
     * closeModule
     * @param savePrompt - prompt to save module first
     * @return successfully closed
     */
    public boolean closeModule(boolean savePrompt)
    {
        if (this.currModule == null)
        {
            //could be null if they tried to quit without saving first
            // and ok-close anyway was selected
            endCloseTool();
            return true;
        }
        else if(!this.currModule.isDirty())
        {
            endCloseTool();
            return true;
        }
        else if(savePrompt)
        {
            return closingOpenModule();
        }
        endCloseTool();

        return true;
    }

    /**
     * endCloseTool
     */
    public void endCloseTool()
    {
        this.setTitle("Schematic Editor");
        this.myStatusPanel.clearWarningMessage();
        this.currDirStr = null;
        this.currModule = null;
    }

    /**
     * exitTool
     * @return boolean - successfully closed module
     */
    public boolean exitTool()
    {
        return closeModule(true);
    }

    /**
     * Handle windowClosing event.
     */
    public void windowIconified(WindowEvent e)
    {
    }

    public void windowDeiconified(WindowEvent e)
    {
    }

    public void windowDeactivated(WindowEvent e)
    {
    }

    public void windowClosed(WindowEvent e)
    {
    }

    public void windowOpened(WindowEvent e)
    {
    }

    public void windowActivated(WindowEvent e)
    {
    }
    // --------------------------------------------------
} //end Class SchEditorFrame
