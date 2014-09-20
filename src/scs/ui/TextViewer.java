package scs.ui;
/*
 * @(#)TextViewer this adapted from Notepad.java	1.16 99/09/23
 *
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */


import scs.util.BrowserLauncher;
import scs.util.UserPref;

import javax.swing.*;
import javax.swing.text.*;
//import java.awt.*;
import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;

/**
 * Sample application using the simple text editor component that
 * supports only one font.
 *
 * @author Timothy Prinzing
 * @author Amanda Alexander
 * @version TextViewer 2.1
 */

public class TextViewer extends JFrame
{
    static final int notepadHeight = 50;
    static final int notepadWidth = 60;

    private static ResourceBundle resources;

    //------------------------------------------------------------------
    // Declarations
    //---------------------------------------------------------------
    public JFrame textViewerFrame = null; //this frame
    //this contains menubar, lowerPanel, statusbar
    private JPanel lowerPanel = null;  //contains toolbar, scroller

    //for loading a file
    private String lastViewedDirStr = "";
    private String lastViewedFileStr = "";

    // for the find/search utilities
    private String lastFindStr = "";
    private int lastFindIndex = -1;
    private int lastLastFindIndex = -1;

    public JTextComponent editor1; //actually a JTextArea
    public PlainDocument doc1; //aa - added - changed from Document to PlainDocument
    public JScrollPane scroller1;//aa - added
    public JViewport viewport1;//aa - added

    private Hashtable commands;
    private Hashtable menuItems;
    private JMenuBar menubar;
    private JToolBar toolbar;
    private JComponent status;

    protected JFileChooser fileDialog;

    protected FindAgainAction findAgainAction=new FindAgainAction();
    //-------------------------------------------------------------------------
    /**
     * Listener for the edits on the current document.
     */
    /*
    public UndoableEditListener undoHandler = new UndoHandler();
    //protected UndoableEditListener undoHandler = new UndoHandler();
    */

    /**
     * UndoManager that we add edits to.
     * Suffix applied to the key used in resource file
     * lookups for an image.
     * <p/>
     * Suffix applied to the key used in resource file
     * lookups for an image.
     * <p/>
     * Suffix applied to the key used in resource file
     * lookups for an image.
     */
    /*
    protected UndoManager undo = new UndoManager();
    */
    /**
     * Suffix applied to the key used in resource file
     * lookups for an image.
     */
    public static final String imageSuffix = "Image";

    /**
     * Suffix applied to the key used in resource file
     * lookups for a label.
     */
    public static final String labelSuffix = "Label";

    /**
     * Suffix applied to the key used in resource file
     * lookups for an action.
     */
    public static final String actionSuffix = "Action";

    /**
     * Suffix applied to the key used in resource file
     * lookups for tooltip text.
     */
    public static final String tipSuffix = "Tooltip";

    public static final String openAction = "open";
    public static final String newAction = "new";
    public static final String saveAction = "save";
    public static final String exitAction = "exit";
    public static final String helpAction = "help";

    static
    {
        try
        {
            resources = ResourceBundle.getBundle("resources.TextViewer", Locale.getDefault());
        }
        catch (MissingResourceException mre)
        {
            String errstr = "TextViewer:resources/TextViewer.properties not found";
            System.err.println(errstr);
        }
    }

    //------------------------------------------------------------------------
    public TextViewer()
    {
        textViewerFrame = this;
        lastViewedDirStr = "";
        lastViewedFileStr = "";

        setTitle(resources.getString("Title"));
        pack();
        setSize(500, 600);

        // Force SwingSet to come up in the Cross Platform L&F
        /*try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exc)
        {
            String errstr = "TextViewer:Error loading L&F: " + exc;
            warningPopup.display(errstr);
        }
*/

        Container cf = getContentPane();
        cf.setBackground(Color.lightGray);
        cf.setLayout(new BorderLayout());

        // create the embedded JTextComponent
        editor1 = createEditor();
        editor1.setFont(new Font("monospaced", Font.PLAIN, 12));
        // aa -added next line
        setPlainDocument((PlainDocument) editor1.getDocument()); //sets doc1

        // install the command table
        commands = new Hashtable();
        Action[] actions = getActions();
        for (int i = 0; i < actions.length; i++)
        {
            Action a = actions[i];
            commands.put(a.getValue(Action.NAME), a);
        }
        //editor1.setPreferredSize(new Dimension(,));
        //get setting from user preferences
        if (UserPref.keymapType.equals("Word"))
        {
            editor1 = updateKeymapForWord(editor1);
        }
        else
        {
            editor1 = updateKeymapForEmacs(editor1);
        }

        scroller1 = new JScrollPane();
        viewport1 = scroller1.getViewport();
        viewport1.add(editor1);
        scroller1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        try
        {
            String vpFlag = resources.getString("ViewportBackingStore");
            if (Boolean.parseBoolean(vpFlag))
            {
                viewport1.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
            }
            else
            {
                viewport1.setScrollMode(JViewport.BLIT_SCROLL_MODE);
            }
        }
        catch (MissingResourceException mre)
        {
            System.err.println("TextViewer:missing resource:" + mre.getMessage());
            // just use the viewport1 default
        }

        menuItems = new Hashtable();
        menubar = createMenubar();

        lowerPanel = new JPanel(true); //moved double buffering to here
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add("North", createToolbar());
        lowerPanel.add("Center", scroller1);

        cf.add("North", menubar);
        cf.add("Center", lowerPanel);
        cf.add("South", createStatusbar());
    }

    //--------------------------------------------------------------------------
    public void display(String currDirStr, String currFileStr)
    {
        if (currDirStr != null && currDirStr.length()>0)
        {
            lastViewedDirStr = currDirStr;
        }

        if (currFileStr != null && currFileStr.length()>0)
        {
            lastViewedFileStr = currFileStr;
        }

        if(openFile(lastViewedDirStr, lastViewedFileStr))
            setVisible(true);
        else
            dispose();
    }

    //--------------------------------------------------------------------------
    /**
     * Fetch the list of actions supported by this
     * editor.  It is implemented to return the list
     * of actions supported by the embedded JTextComponent
     * augmented with the actions defined locally.
     */
    public Action[] getActions()
    {
        return TextAction.augmentList(editor1.getActions(), defaultActions);
    }

    /**
     * Create an editor1 to represent the given document.
     */
    protected static JTextComponent createEditor()
    {
        JTextArea ed;
        ed = new JTextArea(notepadHeight, notepadWidth);
        ed.setCaretPosition(0); //aa
        return (ed);
    }

    /**
     * Fetch the editor1 contained in this file
     */
    public JTextComponent getEditor()
    {
        return editor1;
    }

    /**
     * set the document that this editor points to
     */
    public void setPlainDocument(PlainDocument doc)
    {
        doc1 = doc;
        // Add this as a listener for undoable edits.
        //doc1.addUndoableEditListener(undoHandler);//since viewer only
        editor1.setEditable(false);
        editor1.setDocument(doc1);
    }

    /**
     * Find the hosting frame, for the file-chooser dialog.
     */
    protected JFrame getFrame()
    {
        for (Container p = getParent(); p != null; p = p.getParent())
        {
            if (p instanceof JFrame)
            {
                return (JFrame) p;
            }
        }
        return null;
    }

    /**
     * This is the hook through which all menu items are
     * created.  It registers the result with the menuitem
     * hashtable so that it can be fetched with getMenuItem().
     *
     * @see #getMenuItem
     */
    protected JMenuItem createMenuItem(String cmd)
    {
        JMenuItem mi = new JMenuItem(getResourceString(cmd + labelSuffix));
        URL url = getResource(cmd + imageSuffix);
        if (url != null)
        {
            mi.setHorizontalTextPosition(JButton.RIGHT);
            mi.setIcon(new ImageIcon(url));
        }
        String astr = getResourceString(cmd + actionSuffix);
        if (astr == null)
        {
            astr = cmd;
        }
        mi.setActionCommand(astr);
        Action myaction = getAction(astr); //if this is a known action
        if (myaction != null)
        {
            mi.addActionListener(myaction);
            myaction.addPropertyChangeListener(createActionChangeListener(mi));
            mi.setEnabled(myaction.isEnabled());
        }
        else
        {
            System.err.println("Error:TextViewer: createMenuItem: myaction is null: astr:" + astr);
            //causes the item to be greyed out
            mi.setEnabled(false);
        }
        menuItems.put(cmd, mi);
        return mi;
    }

    /**
     * Fetch the menu item that was created for the given
     * command.
     *
     * @param cmd Name of the action.
     * @return item created for the given command or null
     *         if one wasn't created.
     */
    protected JMenuItem getMenuItem(String cmd)
    {
        return (JMenuItem) menuItems.get(cmd);
    }

    protected Action getAction(String cmd)
    {
        return (Action) commands.get(cmd); //commands is Hashtable
    }

    protected static String getResourceString(String nm)
    {
        String str;
        try
        {
            str = resources.getString(nm);
        }
        catch (MissingResourceException mre)
        {
            str = null;
        }
        return str;
    }

    protected static URL getResource(String key)
    {
        String name = getResourceString(key);
        if (name != null)
        {
            return ClassLoader.getSystemResource(name);
        }
        return null;
    }

    protected Container getToolbar()
    {
        return toolbar;
    }

    protected JMenuBar getMenubar()
    {
        return menubar;
    }

    /**
     * Create a status bar
     */
    protected Component createStatusbar()
    {
        // need to do something reasonable here
        status = new StatusBar();
        return status;
    }

    /**
     * Resets the undo manager.
     * <p/>
     * Create the toolbar.  By default this reads the
     * resource file for the definition of the toolbar.
     * <p/>
     * Create the toolbar.  By default this reads the
     * resource file for the definition of the toolbar.
     * <p/>
     * Create the toolbar.  By default this reads the
     * resource file for the definition of the toolbar.
     */
    /*
    protected void resetUndoManager() {
	undo.discardAllEdits();
	undoAction.update();
	redoAction.update();
    }
    */

    /**
     * Create the toolbar.  By default this reads the 
     * resource file for the definition of the toolbar.
     */
    private Component createToolbar()
    {
        toolbar = new JToolBar();
        StringTokenizer t=new StringTokenizer(getResourceString("toolbar"));
        while(t.hasMoreTokens())
        {
            String toolKey=t.nextToken();
            if (toolKey.equals("-"))
            {
                toolbar.add(Box.createHorizontalStrut(5));
            }
            else
            {
                toolbar.add(createTool(toolKey));
            }
        }
        toolbar.add(Box.createHorizontalGlue());
        return toolbar;
    }

    /**
     * Hook through which every toolbar item is created.
     */
    protected Component createTool(String key)
    {
        return createToolbarButton(key);
    }

    //------------------------------------------------------------
    /**
     * Create a button to go inside of the toolbar.  By default this
     * will load an image resource.  The image filename is relative to
     * the classpath (including the '.' directory if its a part of the
     * classpath), and may either be in a JAR file or a separate file.
     * 
     * @param key The key in the resource file to serve as the basis
     *  of lookups.
     */
    protected JButton createToolbarButton(String key)
    {
        URL url = getResource(key + imageSuffix);
        JButton b = new JButton(new ImageIcon(url))
        {
            public float getAlignmentY()
            {
                return 0.5f;
            }
        };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1, 1, 1, 1));

        String astr = getResourceString(key + actionSuffix);
        if (astr == null)
        {
            astr = key;
        }
        Action a = getAction(astr);
        if (a != null)
        {
            b.setActionCommand(astr);
            b.addActionListener(a);
        }
        else
        {
            b.setEnabled(false);
        }

        String tip = getResourceString(key + tipSuffix);
        if (tip != null)
        {
            b.setToolTipText(tip);
        }

        return b;
    }
    //------------------------------------------------------------

    /**
     * Create the menubar for the app.  By default this pulls the
     * definition of the menu from the associated resource file. 
     */
    protected JMenuBar createMenubar()
    {
        JMenuBar mb = new JMenuBar();

        StringTokenizer t=new StringTokenizer(getResourceString("menubar"));
        while(t.hasMoreTokens())
        {
            JMenu m = createMenu(t.nextToken());
            if (m != null)
            {
                mb.add(m);
            }
        }
        return mb;
    }

    //------------------------------------------------------------
    /**
     * Create a menu for the app.  By default this pulls the
     * definition of the menu from the associated resource file.
     */
    protected JMenu createMenu(String key)
    {
        StringTokenizer t=new StringTokenizer(getResourceString(key));
        JMenu menu = new JMenu(getResourceString(key + "Label"));
        while(t.hasMoreTokens())
        {
            String itemKey=t.nextToken();
            if (itemKey.equals("-"))
            {
                menu.addSeparator();
            }
            else
            {
                JMenuItem mi = createMenuItem(itemKey);
                menu.add(mi);
            }
        }
        return menu;
    }

    //------------------------------------------------------------------
    // Yarked from JMenu, ideally this would be public.
    protected static PropertyChangeListener createActionChangeListener(JMenuItem b)
    {
        return new ActionChangedListener(b);
    }

    // Yarked from JMenu, ideally this would be public.
    private static class ActionChangedListener implements PropertyChangeListener
    {
        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi)
        {
            super();
            this.menuItem = mi;
        }

        public void propertyChange(PropertyChangeEvent e)
        {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME))
            {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            }
            else if (propertyName.equals("enabled"))
            {
                menuItem.setEnabled(Boolean.parseBoolean(e.getNewValue().toString()));
            }
        }
    }

    //------------------------------------------------------------------
    //from Java Swing 1.2 Orielly - Robert Eckstein
    //------------------------------------------------------------------
    protected JTextComponent updateKeymapForWord(JTextComponent textComp)
    {
        //create a new child keymap
        Keymap map = JTextComponent.addKeymap("NslmMap", textComp.getKeymap());

        //define the keystrokeds to be added
        KeyStroke next = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK, false);
        //add the new mappings used DefaultEditorKit actions
        map.addActionForKeyStroke(next, getAction(DefaultEditorKit.nextWordAction));

        KeyStroke prev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(prev, getAction(DefaultEditorKit.previousWordAction));

        KeyStroke selNext = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selNext, getAction(DefaultEditorKit.selectionNextWordAction));
        KeyStroke selPrev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selPrev, getAction(DefaultEditorKit.selectionPreviousWordAction));

        KeyStroke find = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(find, getAction("find"));

        KeyStroke findAgain = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(findAgain, getAction("findAgain"));

        //set the keymap for the text component
        textComp.setKeymap(map);
        return (textComp);
    }//end updateKeymapForWord

    //------------------------------------------------------------------
    //from Java Swing 1.2 Orielly - Robert Eckstein
    //------------------------------------------------------------------
    protected JTextComponent updateKeymapForEmacs(JTextComponent textComp)
    {
        //note: it does not look like a key can do more than one action
        // thus no modes.
        //todo: not all of these are correct. such as ctrlK
        //todo: add saving - ctrlXS

        //create a new child keymap
        Keymap map = JTextComponent.addKeymap("NslmMap", textComp.getKeymap());

        KeyStroke selNext = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selNext, getAction(DefaultEditorKit.selectionNextWordAction));

        KeyStroke selPrev = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(selPrev, getAction(DefaultEditorKit.selectionPreviousWordAction));

        KeyStroke next = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(next, getAction(DefaultEditorKit.forwardAction));
        KeyStroke prev = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(prev, getAction(DefaultEditorKit.backwardAction));

        KeyStroke selectionDown = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selectionDown, getAction(DefaultEditorKit.downAction));
        KeyStroke selectionUp = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selectionUp, getAction(DefaultEditorKit.upAction));

        KeyStroke pageDown = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(pageDown, getAction(DefaultEditorKit.pageDownAction));

        KeyStroke pageUp = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(pageUp, getAction(DefaultEditorKit.pageUpAction));

        KeyStroke endDoc = KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, InputEvent.META_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(endDoc, getAction(DefaultEditorKit.endAction));
        KeyStroke beginingDoc = KeyStroke.getKeyStroke(KeyEvent.VK_LESS, InputEvent.META_MASK | InputEvent.SHIFT_MASK, false);
        map.addActionForKeyStroke(beginingDoc, getAction(DefaultEditorKit.beginAction));

        // the VK_SPACE and VK_W not working as in Emacs - space deleting
        //KeyStroke selectionStart=KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,InputEvent.CTRL_MASK,false);
        //map.addActionForKeyStroke(selectionStart,getAction(DefaultEditorKit.selectionForwardAction)); //todo: setCharPosAction
        //this is doing nothing because only one char to can be assigned to cut
        //KeyStroke cut1=KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_MASK,false);
        //map.addActionForKeyStroke(cut1,getAction(DefaultEditorKit.cutAction));

        //if we do save as XS, this will have to change
        KeyStroke cut = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(cut, getAction(DefaultEditorKit.cutAction));

        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(paste, getAction(DefaultEditorKit.pasteAction));

        KeyStroke moveToEndLine = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(moveToEndLine, getAction(DefaultEditorKit.endLineAction));

        //not emacs like
        KeyStroke selWord = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selWord, getAction(DefaultEditorKit.selectWordAction));

        KeyStroke selLine = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(selLine, getAction(DefaultEditorKit.selectLineAction));

        KeyStroke delNext = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(delNext, getAction(DefaultEditorKit.deleteNextCharAction));

        KeyStroke insertLine = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(insertLine, getAction(DefaultEditorKit.insertBreakAction));

        KeyStroke searchBackward = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(searchBackward, getAction("findAgain"));

        KeyStroke searchForward = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK, false);
        map.addActionForKeyStroke(searchForward, getAction("findAgain"));

        //set the keymap for the text component
        textComp.setKeymap(map);
        return (textComp);
    }//end updateKeymapForEmacs

    //-------------------------------------------------------------
    public boolean openFile(String currDirStr, String currFileStr)
    {

        if (fileDialog == null)
        {
            fileDialog = new JFileChooser();
        }
        fileDialog.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        if (currDirStr.length()>0)
        {
            fileDialog.setCurrentDirectory(new File(currDirStr));
        }
        if (currFileStr.length()>0)
        {
            fileDialog.setSelectedFile(new File(currFileStr));
        }
        fileDialog.showOpenDialog(this);

        File file = fileDialog.getSelectedFile(); //cancel pushed
        if (file!= null && file.exists())
        {
            Document oldDoc = editor1.getDocument();
            if (oldDoc != null)
                editor1.setDocument(new PlainDocument());
            fileDialog.setName(file.getName());
            Thread loader = new FileLoader(file, editor1.getDocument());
            loader.start();
            return true;
        }
        else
        {
            setVisible(false);
            dispose();
            return false;
        }
    }

    //-------------------------------------------------------------
    /*
    class UndoHandler implements UndoableEditListener {
*/

    /**
     * Messaged when the Document has created an edit, the edit is
     * added to <code>undo</code>, an instance of UndoManager.
     * <p/>
     * FIXME - I'm not very useful yet
     * <p/>
     * FIXME - I'm not very useful yet
     * <p/>
     * FIXME - I'm not very useful yet
     */
    /*
        public void undoableEditHappened(UndoableEditEvent e) {
	    undo.addEdit(e.getEdit());
	    undoAction.update();
	    redoAction.update();
	}
    }
    */
    //-------------------------------------------------------------
    /**
     * FIXME - I'm not very useful yet
     */
    static class StatusBar extends JComponent
    {
        public StatusBar()
        {
            super();
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        public void paint(Graphics g)
        {
            super.paint(g);
        }

    }

    // --- action supporting viewer ----------------------------
    /**
     * Thread to load a file into the text storage model
     */
    class FileLoader extends Thread
    {
        Document doc2;
        File f2;

        FileLoader(File f, Document doc)
        {
            setPriority(4);
            this.f2 = f;
            this.doc2 = doc;
        }

        public void run()
        {
            try
            {
                // initialize the statusbar
                status.removeAll();
                JProgressBar progress = new JProgressBar();
                progress.setMinimum(0);
                progress.setMaximum((int) f2.length());
                status.add(progress);
                status.revalidate();

                // try to start reading
                Reader in = new FileReader(f2);
                char[] buff = new char[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1)
                {
                    doc2.insertString(doc2.getLength(), new String(buff, 0, nch), null);
                    progress.setValue(progress.getValue() + nch);
                }

                // we are done... get rid of progressbar
                status.removeAll();
                status.revalidate();
            }
            catch (IOException e)
            {
                System.err.println("TextViewer:FileLoader " + e.toString());
            }
            catch (BadLocationException e)
            {
                System.err.println("TextViewer:FileLoader " + e.getMessage());
            }
        }
    }

    // action implementations ----------------------------------
    /*
    private UndoAction undoAction = new UndoAction();
    private RedoAction redoAction = new RedoAction();
    */
    /**
     * Actions defined by the TextViewer class
     */
    private Action[] defaultActions = {
            new NewAction(),
            new OpenAction(),
            new PrintAction(),
            new ExitAction(),
            new FindAction(),
            findAgainAction,
            new HelpAction(),
    };

    //-------------------------------------------------------------

    class OpenAction extends NewAction
    {
        OpenAction()
        {
            super(openAction);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            openFile(lastViewedDirStr, lastViewedFileStr);
        }
    }

    //----------------------------------------------------------
    class NewAction extends AbstractAction
    {
        NewAction()
        {
            super(newAction);
            setEnabled(true);
        }

        NewAction(String nm)
        {
            super(nm);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            getEditor().setDocument(new PlainDocument()); //aa - PlainDocument
            lowerPanel.revalidate();
        }
    }//end class

    //---------------------------------------------------
    class PrintAction extends AbstractAction
    {
        PrintAction()
        {
            super("print");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            PrintJob pjob = getToolkit().getPrintJob(textViewerFrame, "Printing Nslm", null);
            if (pjob != null)
            {
                Graphics pg = pjob.getGraphics();
                if (pg != null)
                {
                    //todo: this should print from
                    // the file not from the screen.
                    //if (editor1!=null) {
                    //    editor1.print(pg); //print crashes, must use printAll
                    //}
                    //if (scroller1!=null) {
                    //    scroller1.printAll(pg); //is clipping on left
                    //}
                    if (scroller1 != null)
                    {
                        JViewport jvp = scroller1.getViewport();
                        jvp.printAll(pg);
                    }
                    pg.dispose();
                }
                pjob.end();
            }
        }
    }//end class

    class HelpAction extends AbstractAction
    {
        HelpAction()
        {
            super(helpAction);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            BrowserLauncher.openUrl("http://neuroinformatics.usc.edu/mediawiki/index.php/NSLM_Viewer");
        }
    }

    //----------------------------------------------------------
    /**
     * 
     */
    class ExitAction extends AbstractAction
    {
        ExitAction()
        {
            super(exitAction);
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
        }
    }

    //----------------------------------------------------
    class FindAction extends AbstractAction
    {
        FindAction()
        {  //only called by findAction
            super("find");
            setEnabled(true);
        }

        FindAction(String nm, boolean enabled)
        { //called by findAgainAction
            super(nm);
            setEnabled(enabled);
        }

        public void actionPerformed(ActionEvent event)
        {
            JMenuItem mi;
            String label;

            if ((editor1 == null) || (editor1.getDocument() == null))
            {
                return;
            }
            String actionStr = event.getActionCommand(); //is not makeing anysense
            //when keystrokes typed
            if ((event.getSource() instanceof JMenuItem))
            {
                mi = (JMenuItem) event.getSource();
                label = mi.getText();
            }
            else if ((event.getSource() instanceof JTextArea))
            {  //keystroke
                label = "FindAgain"; //just set it to findagain
            }
            else
            {
                System.err.println("Debug:TextViewer:" + actionStr);
                System.err.println("Debug:TextViewer:" + event.getSource().toString());
                return;
            }

            FindDialog find = initFindDialog();
            find.setVisible(true);
            if (find.okPressed)
            {
                lastFindStr = find.getString();

                if (find.isForward())
                    lastFindIndex = searchForward(lastFindStr);
                else
                    lastFindIndex = searchBackward(lastFindStr);

                findAgainAction.setEnabled(true);
            }
        }

        protected FindDialog initFindDialog()
        {
            FindDialog find = new FindDialog(null);
            find.setLocation(400, 400);
            find.pack();
            find.setSize(300, 125);
            return find;
        }

        //----------------------
        //return last location found
        public int searchForward(String lastFindStr)
        {
            int carPos = editor1.getCaretPosition();
            int strLength = editor1.getText().length();
            //search Forward
            try
            {
                String chunk1 = editor1.getText(carPos, (strLength - carPos)); //offset,length

                lastFindIndex = chunk1.indexOf(lastFindStr);
                if (lastFindIndex == -1)
                {
                    int selected = JOptionPane.showConfirmDialog(null, "String not found. Try backward?", "Warning",
                                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    //handle backward
                    if (selected == JOptionPane.OK_OPTION)
                        lastFindIndex = searchBackward(lastFindStr);
                }
                else
                {
                    lastFindIndex = lastFindIndex + carPos;
                    lastLastFindIndex = lastFindIndex;
                    editor1.setCaretPosition(lastFindIndex); //ready to type in body
                    editor1.select(lastFindIndex, lastFindIndex + lastFindStr.length());
                }
            }
            catch(BadLocationException e)
            {
                lastFindIndex=-1;
            }
            return (lastFindIndex);
        }

        //----------------------------------------------------
        //return last location found
        public int searchBackward(String lastFindStr)
        {
            if (lastFindStr != null && lastFindStr.length() > 0)
            {
                int foo = lastLastFindIndex; //begining of word
                if (foo >= 0)
                {
                    editor1.setCaretPosition(foo);
                }
            }

            int carPos = editor1.getCaretPosition();
            //search backward
            try
            {
                String chunk1 = editor1.getText(0, carPos);
                lastFindIndex = chunk1.lastIndexOf(lastFindStr);
                if (lastFindIndex == -1)
                {
                    int selected = JOptionPane.showConfirmDialog(null, "String not found. Try forward?", "Warning",
                                                                 JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    //handle forward
                    if (selected == JOptionPane.OK_OPTION)
                        lastFindIndex = searchForward(lastFindStr);
                }
                else if (lastFindStr != null)
                {
                    lastLastFindIndex = lastFindIndex;
                    editor1.setCaretPosition(lastFindIndex); //ready to type in body
                    editor1.select(lastFindIndex, lastFindIndex + lastFindStr.length());
                }
            }
            catch(BadLocationException e)
            {
                lastFindIndex=-1;
            }
            return (lastFindIndex);
        }
    } //end FindAction

    //----------------------------------------------------
    class FindAgainAction extends FindAction
    {
        FindAgainAction()
        {
            super("findAgain", false);
        }

        protected FindDialog initFindDialog()
        {
            FindDialog find = new FindDialog(null, lastFindStr);
            find.setLocation(400, 400);
            find.pack();
            find.setSize(300, 125);
            return find;
        }
    }

    //---------------------------------------------------------------
} //end TextViewer Class
