package scs.ui;

import scs.graphics.*;
import scs.graphics.Icon;
import scs.util.*;
import scs.Module;
import scs.NSLM;
import scs.Declaration;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import javax.swing.event.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
import org.xml.sax.SAXException;

public class NslmEditorFrame extends JFrame implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
        MouseListener, WindowListener
{
    private SchEditorFrame parentFrame;

    private JPanel mainPanel;
    private JLabel libNameLabel;
    private JLabel libPathLabel;
    private JLabel classNameLabel;
    private JLabel classVersionLabel;
    private JComboBox classTypeSelect;
    private JTextArea classArgumentsText;
    private JTextField superClassText;
    private JTextArea superclassParametersText;
    private JTextField interfaceText;
    private JTextArea commentsText;
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton selectAllButton;
    private JPanel methodsMenuPanel;
    private JButton addImportButton;
    private JButton removeImportButton;
    private JScrollPane importsScrollPane;
    private JButton addInportButton;
    private JButton addOutputPortButton;
    private JButton addSubmoduleButton;
    private JButton addVariableButton;
    private JButton modifyButton;
    private JButton renameButton;
    private JButton copyVariableButton;
    private JButton deleteButton;
    private JButton upButton;
    private JButton downButton;
    private JButton topButton;
    private JButton bottomButton;
    private JScrollPane declarationsScrollPane;
    private JEditTextArea methodsTextArea;
    private JScrollPane classArgsScrollPane;
    private JScrollPane superclassParametersScrollPane;
    private JScrollPane libPathScrollPane;
    private JScrollPane commentsScrollPane;
    private JCheckBox bufferingCheckBox;
    private Hashtable<String, Action> methodsCommands;

    /**
     * Listener for the edits on the current document.
     */
    public UndoableEditListener undoHandler = new UndoHandler();
    /**
     * UndoManager that we add edits to.
     */
    protected UndoManager undoManager = new UndoManager();
    private UndoAction methodsUndoAction = new UndoAction();
    private RedoAction methodsRedoAction = new RedoAction();
    private FindAction methodsFindAction = new FindAction();
    private FindAgainAction methodsFindAgainAction = new FindAgainAction();

    private boolean methodTab = false;
    private String methodName = "";
    
    private ReplaceAction methodsReplaceAction = new ReplaceAction();
    private Action[] methodsDefaultActions = {
            methodsUndoAction,
            methodsRedoAction,
            new CopyAction(), //MM
            new PasteAction(), //MM
            new CutAction(),
            new SelectAllAction(),
            methodsFindAction,
            methodsFindAgainAction,
            methodsReplaceAction,
            new InitSysAction(),
            new CallFromConstructorTopAction(),
            new CallFromConstructorBottomAction(),
            new MakeConnAction(),
            new InitModuleAction(),
            new InitTrainEpochsAction(),
            new InitTrainAction(),
            new SimTrainAction(),
            new EndTrainAction(),
            new EndTrainEpochsAction(),
            new InitRunEpochsAction(),
            new InitRunAction(),
            new SimRunAction(),
            new EndRunAction(),
            new EndRunEpochsAction(),
            new EndModuleAction(),
            new EndSysAction()
    };

    private DefaultListModel importListModel;
    private JList importList;
    private DefaultListModel declListModel;
    private JList declList;

    String lastFindStr = "";
    int lastFindIndex = -1;
    int lastLastFindIndex = -1;

    String lastExistingString = "";
    String lastReplacementString = "";
    int lastReplaceIndex = -1;
    int lastLastReplaceIndex = -1;
    public static ResourceBundle resources;

    static
    {
        try
        {
            resources = ResourceBundle.getBundle("resources.NslmEditor", Locale.getDefault());
        }
        catch (MissingResourceException mre)
        {
            System.err.println("NslmEditor:resources/NslmEditor.properties not found");
            System.exit(1);
        }
    }

    public NslmEditorFrame(SchEditorFrame fm)
    {
        super("NSLM Editor");

        this.parentFrame = fm;
        setupLayout();
        initUI();
    }

    public NslmEditorFrame(SchEditorFrame fm, String methodName)
    {

        super("NSLM Editor");
    	this.methodTab = true;
    	this.methodName = methodName;
        this.parentFrame = fm;
        setupLayout();
        initUI();
    }
    
    protected void setupLayout()
    {
        methodsCommands = new Hashtable<String, Action>();
        Action[] actions = getActions();
        for (int i = 0; i < actions.length; i++)
        {
            Action a = actions[i];
            methodsCommands.put(a.getValue(Action.NAME).toString(), a);
        }

        JMenuBar nslmMenuBar = createNslmMenuBar();
        setJMenuBar(nslmMenuBar);

        JMenuBar methodsMenuBar = createMethodsMenuBar();

        methodsMenuPanel.setLayout(new BoxLayout(methodsMenuPanel, BoxLayout.X_AXIS));
        methodsMenuPanel.add(methodsMenuBar);

        addImportButton.addActionListener(this);
        removeImportButton.addActionListener(this);

        classArgumentsText.addKeyListener(this);
        superClassText.addKeyListener(this);
        superclassParametersText.addKeyListener(this);
        interfaceText.addKeyListener(this);
        commentsText.addKeyListener(this);
        methodsTextArea.addKeyListener(this);

        addInportButton.addActionListener(this);
        addOutputPortButton.addActionListener(this);
        addSubmoduleButton.addActionListener(this);
        addVariableButton.addActionListener(this);

        modifyButton.addActionListener(this);
        renameButton.addActionListener(this);
        copyVariableButton.addActionListener(this);
        deleteButton.addActionListener(this);
        upButton.addActionListener(this);
        downButton.addActionListener(this);
        topButton.addActionListener(this);
        bottomButton.addActionListener(this);

        cutButton.addActionListener(getAction("cut-to-clipboard"));
        copyButton.addActionListener(getAction("copy-to-clipboard"));
        pasteButton.addActionListener(getAction("paste-from-clipboard"));
        selectAllButton.addActionListener(getAction("select-all"));

        addWindowListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setSize(800, 600);
        add(mainPanel);
        
    }

    protected void initUI()
    {
        if (parentFrame.currModule != null)
        {
            setTitle("NSLM Editor: Module " + parentFrame.currModule.moduleName);

            libNameLabel.setText(parentFrame.currModule.libNickName);
            try
            {
                libPathLabel.setText(SCSUtility.getLibPath(parentFrame.currModule.libNickName));
            }
            catch (IOException e)
            {
                libPathLabel.setText("");
            }

            importListModel = new DefaultListModel();
            if (parentFrame.currModule.imports != null)
            {
                importListModel.clear();
                for (int i = 0; i < parentFrame.currModule.imports.size(); i++)
                {
                    importListModel.addElement(parentFrame.currModule.imports.elementAt(i));
                }
            }
            importList = new JList(importListModel);
            importList.addListSelectionListener(this);
            importsScrollPane.getViewport().setView(importList);

            classNameLabel.setText(parentFrame.currModule.moduleName);

            classVersionLabel.setText(parentFrame.currModule.versionName);

            if (parentFrame.currModule.moduleType.equals("NslModel"))
            {
                classTypeSelect.addItem("NslModel");
                classTypeSelect.setSelectedIndex(0);
            }
            else
            {
                classTypeSelect.addItem("NslJavaModule");
                classTypeSelect.addItem("NslMatlabModule");
                classTypeSelect.addItem("NslClass");
                classTypeSelect.setSelectedItem(parentFrame.currModule.moduleType);
            }
            classTypeSelect.addItemListener(this);

            classArgumentsText.setText(parentFrame.currModule.arguments);

            superClassText.setText(parentFrame.currModule.myNslm.extendsWhat);

            superclassParametersText.setText(parentFrame.currModule.myNslm.whatsParams);

            interfaceText.setText(parentFrame.currModule.myNslm.implementsWhat);

            commentsText.setText(parentFrame.currModule.myNslm.comment1);

            bufferingCheckBox.setSelected(parentFrame.currModule.buffering);

            declListModel = new DefaultListModel();
            initDeclListModel();

            declList = new JList(declListModel);
            declList.addListSelectionListener(this);
            declList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            declarationsScrollPane.getViewport().setView(declList);
            declList.addMouseListener(this);
            declList.addKeyListener(this);

            methodsTextArea.setTokenMarker(new JavaTokenMarker());
            if (parentFrame.currModule.myNslm == null)
                parentFrame.currModule.myNslm = new NSLM();
            if (parentFrame.currModule.myNslm.methods == null)
                parentFrame.currModule.myNslm.methods = new SyntaxDocument();
            parentFrame.currModule.myNslm.methods.addUndoableEditListener(undoHandler);
            methodsTextArea.setDocument(parentFrame.currModule.myNslm.methods);

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem m = new JMenuItem("Cut");
            m.setIcon(new ImageIcon("resources/cut.png"));
            m.addActionListener(getAction("cut-to-clipboard"));
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
            popupMenu.add(m);

            m = new JMenuItem("Copy");
            m.setIcon(new ImageIcon("resources/editcopy.png"));
            m.addActionListener(getAction("copy-to-clipboard"));
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
            popupMenu.add(m);

            m = new JMenuItem("Paste");
            m.setIcon(new ImageIcon("resources/editpaste.png"));
            m.addActionListener(getAction("paste-from-clipboard"));
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
            popupMenu.add(m);

            m = new JMenuItem("Select All");
            m.setIcon(new ImageIcon("resources/edit.png"));
            m.addActionListener(getAction("select-all"));
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
            popupMenu.add(m);

            popupMenu.add(new JSeparator());
            JMenu jm = createMenu("insertMethod");
            popupMenu.add(jm);

            methodsTextArea.setRightClickPopup(popupMenu);
            methodsTextArea.getInputHandler().addKeyBinding("C+Z", methodsUndoAction);
            methodsTextArea.getInputHandler().addKeyBinding("CS+Z", methodsRedoAction);
            methodsTextArea.getInputHandler().addKeyBinding("C+F", methodsFindAction);
            methodsTextArea.getInputHandler().addKeyBinding("C+G", methodsFindAgainAction);
            methodsTextArea.getInputHandler().addKeyBinding("C+R", methodsReplaceAction);
            if(methodTab) 
        	{
        		((JTabbedPane)mainPanel.getComponent(0)).setSelectedIndex(2);
        		try {
        			methodsTextArea.setCaretPosition(methodsTextArea.getDocument().getText(0, methodsTextArea.getDocument().getLength()).indexOf("public void " + methodName));
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
        	}
        }
        else
            setTitle("NSLM Editor");
    }

    private void initDeclListModel()
    {
        declListModel.clear();
        if (parentFrame.currModule.variables != null)
        {
            int sizeOfVarList = parentFrame.currModule.variables.size();
            for (int i = 0; i < sizeOfVarList; i++)
            {
                Declaration var = parentFrame.currModule.variables.elementAt(i);

                declListModel.addElement(getVarString(var));
            }
        }
    }

    /**
     * Create the NSLM menu bar.
     *
     * @return - new menu bar
     */
    public JMenuBar createNslmMenuBar()
    {
        JMenuItem mi;
        JMenu fileMenu;
        JMenu helpMenu;
        JMenuBar myMenuBar;

        myMenuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        fileMenu.add(mi = new JMenuItem("View NSLM"));
        mi.addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(mi = new JMenuItem("Close NSLM Editor"));
        mi.addActionListener(this);

        myMenuBar.add(fileMenu);

        helpMenu = new JMenu("Help");
        helpMenu.addActionListener(this);
        helpMenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(this);

        myMenuBar.add(helpMenu);

        return (myMenuBar);
    }

    /**
     * Create the menubar for the app.  By default this pulls the
     * definition of the menu from the associated resource file.
     *
     * @return - new menubar
     */
    protected JMenuBar createMethodsMenuBar()
    {
        JMenuBar mb = new JMenuBar();

        StringTokenizer t = new StringTokenizer(resources.getString("menubar"));
        while (t.hasMoreTokens())
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
     *
     * @param key - menu name
     * @return new menu
     */
    protected JMenu createMenu(String key)
    {
        StringTokenizer t = new StringTokenizer(resources.getString(key));
        JMenu menu = new JMenu(resources.getString(key + "Label"));
        while (t.hasMoreTokens())
        {
            String itemKey = t.nextToken();
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

    /**
     * This is the hook through which all menu items are
     * created.  It registers the result with the menuitem
     * hashtable so that it can be fetched with getMenuItem().
     *
     * @param cmd - menu command
     * @return new menu item
     */
    protected JMenuItem createMenuItem(String cmd)
    {
        JMenuItem mi = new JMenuItem(resources.getString(cmd + SCSUtility.labelSuffix));
        if (resources.containsKey(cmd + SCSUtility.imageSuffix))
        {
            URL url = ClassLoader.getSystemResource(resources.getString(cmd + SCSUtility.imageSuffix));
            if (url != null)
            {
                mi.setHorizontalTextPosition(JButton.RIGHT);
                mi.setIcon(new ImageIcon(url));
            }
        }
        String astr = cmd;
        if (resources.containsKey(cmd + SCSUtility.actionSuffix))
            astr = resources.getString(cmd + SCSUtility.actionSuffix);
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
            //causes the item to be greyed out
            mi.setEnabled(false);
        }
        if (resources.containsKey(cmd + SCSUtility.keyShortcutSuffix))
            mi.setAccelerator(KeyStroke.getKeyStroke(resources.getString(cmd + SCSUtility.keyShortcutSuffix)));

        return mi;
    }

    protected Action getAction(String cmd)
    {
        return methodsCommands.get(cmd); //commands is Hashtable
    }

    /**
     * Fetch the list of actions supported by this
     * editor.  It is implemented to return the list
     * of actions supported by the embedded JTextComponent
     * augmented with the actions defined locally.
     *
     * @return array of actions
     */
    public Action[] getActions()
    {
        return methodsDefaultActions;
    }

    protected static PropertyChangeListener createActionChangeListener(JMenuItem b)
    {
        return new ActionChangedListener(b);
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tabbedPane1, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Main", panel1);
        final JLabel label1 = new JLabel();
        label1.setText("Library Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Library Path:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Imports:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label3, gbc);
        addImportButton = new JButton();
        addImportButton.setText("Add Import");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(addImportButton, gbc);
        removeImportButton = new JButton();
        removeImportButton.setEnabled(false);
        removeImportButton.setText("Remove Import");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(removeImportButton, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Class Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label4, gbc);
        classNameLabel = new JLabel();
        classNameLabel.setText("Class Name");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(classNameLabel, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Class Version:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label5, gbc);
        classVersionLabel = new JLabel();
        classVersionLabel.setText("Class Version");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(classVersionLabel, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Class Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label6, gbc);
        classTypeSelect = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(classTypeSelect, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Class Arguments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label7, gbc);
        importsScrollPane = new JScrollPane();
        importsScrollPane.setHorizontalScrollBarPolicy(32);
        importsScrollPane.setVerticalScrollBarPolicy(22);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.gridheight = 3;
        gbc.weightx = 0.01;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(importsScrollPane, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Superclass:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label8, gbc);
        superClassText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(superClassText, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Superclass Parameters:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label9, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Interface:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label10, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        interfaceText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(interfaceText, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Comments:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel1.add(label11, gbc);
        libNameLabel = new JLabel();
        libNameLabel.setText("Lib Name");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(libNameLabel, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        classArgsScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(classArgsScrollPane, gbc);
        classArgumentsText = new JTextArea();
        classArgumentsText.setRows(2);
        classArgumentsText.setText("");
        classArgsScrollPane.setViewportView(classArgumentsText);
        superclassParametersScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(superclassParametersScrollPane, gbc);
        superclassParametersText = new JTextArea();
        superclassParametersText.setRows(2);
        superclassParametersScrollPane.setViewportView(superclassParametersText);
        commentsScrollPane = new JScrollPane();
        commentsScrollPane.setVerticalScrollBarPolicy(22);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.gridwidth = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(commentsScrollPane, gbc);
        commentsText = new JTextArea();
        commentsText.setRows(5);
        commentsText.setText("");
        commentsScrollPane.setViewportView(commentsText);
        libPathScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = 2;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(libPathScrollPane, gbc);
        libPathLabel = new JLabel();
        libPathLabel.setText("Lib Path");
        libPathScrollPane.setViewportView(libPathLabel);
        bufferingCheckBox = new JCheckBox();
        bufferingCheckBox.setText("Double Buffering");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(bufferingCheckBox, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Declarations", panel2);
        declarationsScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.01;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(declarationsScrollPane, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(toolBar1, gbc);
        addInportButton = new JButton();
        addInportButton.setText("Add Input Port");
        toolBar1.add(addInportButton);
        addOutputPortButton = new JButton();
        addOutputPortButton.setText("Add Output Port");
        toolBar1.add(addOutputPortButton);
        addSubmoduleButton = new JButton();
        addSubmoduleButton.setText("Add Submodule");
        toolBar1.add(addSubmoduleButton);
        addVariableButton = new JButton();
        addVariableButton.setText("Add Variable");
        toolBar1.add(addVariableButton);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setFloatable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(toolBar2, gbc);
        modifyButton = new JButton();
        modifyButton.setEnabled(false);
        modifyButton.setText("Modify");
        toolBar2.add(modifyButton);
        renameButton = new JButton();
        renameButton.setEnabled(false);
        renameButton.setText("Rename");
        toolBar2.add(renameButton);
        copyVariableButton = new JButton();
        copyVariableButton.setEnabled(false);
        copyVariableButton.setText("Copy");
        toolBar2.add(copyVariableButton);
        deleteButton = new JButton();
        deleteButton.setEnabled(false);
        deleteButton.setText("Delete");
        toolBar2.add(deleteButton);
        upButton = new JButton();
        upButton.setEnabled(false);
        upButton.setText("Up");
        toolBar2.add(upButton);
        downButton = new JButton();
        downButton.setEnabled(false);
        downButton.setText("Down");
        toolBar2.add(downButton);
        topButton = new JButton();
        topButton.setEnabled(false);
        topButton.setText("Top");
        toolBar2.add(topButton);
        bottomButton = new JButton();
        bottomButton.setEnabled(false);
        bottomButton.setText("Bottom");
        toolBar2.add(bottomButton);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Methods", panel3);
        final JToolBar toolBar3 = new JToolBar();
        toolBar3.setFloatable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.01;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(toolBar3, gbc);
        toolBar3.setBorder(BorderFactory.createTitledBorder(""));
        cutButton = new JButton();
        cutButton.setIcon(new ImageIcon(getClass().getResource("/resources/cut.png")));
        cutButton.setText("");
        toolBar3.add(cutButton);
        copyButton = new JButton();
        copyButton.setIcon(new ImageIcon(getClass().getResource("/resources/editcopy.png")));
        copyButton.setText("");
        toolBar3.add(copyButton);
        pasteButton = new JButton();
        pasteButton.setIcon(new ImageIcon(getClass().getResource("/resources/editpaste.png")));
        pasteButton.setText("");
        toolBar3.add(pasteButton);
        selectAllButton = new JButton();
        selectAllButton.setIcon(new ImageIcon(getClass().getResource("/resources/edit.png")));
        selectAllButton.setText("");
        toolBar3.add(selectAllButton);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer7, gbc);
        methodsMenuPanel = new JPanel();
        methodsMenuPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(methodsMenuPanel, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(scrollPane1, gbc);
        methodsTextArea = new JEditTextArea();
        scrollPane1.setViewportView(methodsTextArea);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer10, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
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
                menuItem.setEnabled((Boolean) e.getNewValue());
            }
        }
    }

    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() instanceof JMenuItem)
        {
            JMenuItem jmi = (JMenuItem) event.getSource();
            String selectedStr = jmi.getText(); //Swing
            //###########----------------
            // file actions
            //###########--------------------------
            if (selectedStr.equals("View NSLM"))
            {
                String lastViewedFileStr = "";
                if ((parentFrame.currModule != null) && (parentFrame.currModule.moduleName != null) && (!(parentFrame.currModule.moduleName.length() == 0)))
                {
                    lastViewedFileStr = parentFrame.currModule.moduleName + ".mod";
                }
                TextViewer myTextViewer = new TextViewer();
                String currDirStr = "";
                try
                {
                    currDirStr = SCSUtility.getLibPath(parentFrame.currModule.libNickName);
                }
                catch (IOException e)
                {
                }
                if (currDirStr.length() == 0)
                {
                    currDirStr = SCSUtility.user_home;
                }
                myTextViewer.display(currDirStr, lastViewedFileStr);
            }
            //###########-------
            else if (selectedStr.equals("Close NSLM Editor"))
            {

                if (closeModule())
                {
                    declListModel.clear();
                    parentFrame.currModule = null;
                    dispose();
                    parentFrame.mySchematicPanel.requestFocus();
                }
            }
            else if (selectedStr.equals("Help"))
            {
                BrowserLauncher.openUrl("http://neuroinformatics.usc.edu/mediawiki/index.php/NSLM_Editor");
            }
        }
        else if (event.getSource() instanceof JCheckBox)
        {
            JRadioButton rbn = (JRadioButton) event.getSource(); //actually a radio button
            String bntext = rbn.getText();

            if (bntext.equals("Double Buffering"))
            {
                parentFrame.currModule.buffering = bufferingCheckBox.isSelected();
                parentFrame.currModule.setDirty(true);
            }
        }
        else if (event.getSource() instanceof JButton)
        {
            JButton bn = (JButton) event.getSource(); //actually a radio button
            //String bntext = bn.getText();
            if (bn.equals(addImportButton))
            {
                finishAdd("Import");
            }
            else if (bn.equals(removeImportButton))
            {
                finishDelete("Import");
            }
            else if (bn.equals(addInportButton)/*bntext.equals("Add Input Port")*/)
            {
                finishAdd("InputPort");
            }
            else if (bn.equals(addOutputPortButton)/*bntext.equals("Add Output Port")*/)
            {
                finishAdd("OutputPort");
            }
            else if (bn.equals(addSubmoduleButton)/*bntext.equals("Add Submodule")*/)
            {
                finishAdd("SubModule");
            }
            else if (bn.equals(addVariableButton)/*bntext.equals("Add Variable")*/)
            {
                finishAdd("BasicVariable");
            }
            //====All buttons============================
            else if (bn.equals(modifyButton) || bn.equals(renameButton) || bn.equals(copyVariableButton) ||
                    bn.equals(deleteButton) || bn.equals(topButton) || bn.equals(bottomButton) || bn.equals(upButton) ||
                    bn.equals(downButton))
            {
                int listIndex = declList.getSelectedIndex();
                if (listIndex == -1)
                {
                    String errstr = "NslmEditorFrame:Must select a variable first.";
                    JOptionPane.showMessageDialog(null, errstr, "Warning", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    String selectedVarStr = parentFrame.currModule.variables.get(listIndex).varName;
                    //====Modify============================
                    if (bn.equals(modifyButton))
                    {
                        finishModify(listIndex);
                    }
                    //====ChangeName============================
                    else if (bn.equals(renameButton))
                    {
                        finishRename(listIndex);
                    }
                    //====Copy============================
                    else if (bn.equals(copyVariableButton))
                    {
                        finishCopy(listIndex);
                    }
                    //================delete variable===================
                    else if (bn.equals(deleteButton))
                    {
                        finishDelete("Variable");
                    }
                    //================Move to Top ===================
                    else if (bn.equals(topButton))
                    {
                        moveTop(selectedVarStr, listIndex);
                    }
                    //================Move to Bottom ===================
                    else if (bn.equals(bottomButton))
                    {
                        moveBottom(selectedVarStr, listIndex);
                    }
                    //================Move Up ===================
                    else if (bn.equals(upButton))
                    {
                        moveUp(selectedVarStr, listIndex);
                    }
                    //================Move Down ===================
                    else if (bn.equals(downButton))
                    {
                        moveDown(selectedVarStr, listIndex);
                    }
                }
            }
            //====Change============================
        }
    }

    private void moveTop(String selectedVarStr, int listIndex)
    {
        parentFrame.currModule.moveVariable(0, selectedVarStr);
        initDeclListModel();
        declList.setSelectedIndex(0);
    }

    private void moveBottom(String selectedVarStr, int listIndex)
    {
        int insertAt = parentFrame.currModule.variables.size();
        parentFrame.currModule.moveVariable(insertAt, selectedVarStr);
        initDeclListModel();
        declList.setSelectedIndex(insertAt - 1);
    }

    private void moveUp(String selectedVarStr, int listIndex)
    {
        int insertAt = listIndex - 1;
        if (insertAt < 0)
            insertAt = 0;
        parentFrame.currModule.moveVariable(insertAt, selectedVarStr);
        initDeclListModel();
        declList.setSelectedIndex(insertAt);
    }

    private void moveDown(String selectedVarStr, int listIndex)
    {
        int insertAt = listIndex + 1;
        if (insertAt > parentFrame.currModule.variables.size())
            insertAt = parentFrame.currModule.variables.size();
        parentFrame.currModule.moveVariable(insertAt, selectedVarStr);
        initDeclListModel();
        declList.setSelectedIndex(insertAt);
    }

    private String getVarString(Declaration var)
    {
        String varString = var.varAccess + " " + var.varType;
        if (!var.varType.startsWith("Nsl"))
        {
            for (int i = 0; i < var.varDimensions; i++)
                varString = varString + "[]";
        }
        varString = varString + " " + var.varName;
        if (var.varParams != null && var.varParams.length() > 0)
            varString = varString + "(" + var.varParams + ")";
        return varString;
    }

    private String getVariableName(String selectedVarStr)
    {
        String selectedVarName = selectedVarStr.substring(selectedVarStr.indexOf(" ", selectedVarStr.indexOf(" ") + 1) + 1);
        if (selectedVarName.contains("("))
            selectedVarName = selectedVarName.substring(0, selectedVarName.indexOf("("));
        return selectedVarName;
    }

    private void finishModify(int ilist)
    {
        Declaration var = parentFrame.currModule.variables.elementAt(ilist);
        DeclarationDialog dialog = getDialogWindow(var.varDialogType);

        //note: ilist and ivector should be the same
        if (dialog != null)
        {
            dialog.display(var);
            if (dialog.okPressed)
            {
                dialog.fillDeclarationInfo(var);
                declListModel.setElementAt(getVarString(var), ilist);
                parentFrame.currModule.setDirty(true);
                if (parentFrame.currModule.myIcon != null && var.varDialogType.equals("InputPort") || var.varDialogType.equals("OutputPort"))
                {
                    if (parentFrame.currModule.myIcon.getDrawableObject(var.varName) != null)
                    {
                        IconPort port = parentFrame.currModule.myIcon.getDrawableObject(var.varName);
                        port.Type = var.varType;
                        port.Parameters = var.varParams;

                        if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
                            parentFrame.iconEditor.myIconPanel.repaint();
                    }
                }
                if (parentFrame.currModule.mySchematic != null && var.varDialogType.equals("SubModule"))
                {
                    if (parentFrame.currModule.mySchematic.getDrawableObject(var.varName) != null)
                    {
                        IconInst icon = (IconInst) parentFrame.currModule.mySchematic.getDrawableObject(var.varName);
                        icon.moduleName = var.varType;
                        icon.libNickName = var.modLibNickName;
                        icon.versionName = var.modVersion;
                        icon.parameters = var.varParams;

                        parentFrame.mySchematicPanel.repaint();
                    }
                }
            }
        }
    }

    //--------------------------------------------------
    public void finishRename(int ilist)
    {
        Declaration var = parentFrame.currModule.variables.elementAt(ilist);
        String namestr = (String) JOptionPane.showInputDialog(null, "Variable Name: ", "Variable Name",
                JOptionPane.QUESTION_MESSAGE, null, null, var.varName);
        if (namestr == null)
        {
            return; //cancel
        }
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.length() == 0)
        {
            return; //blank
        }
        if (namestr.equals(var.varName))
        {
            return; //same name
        }
        int index = parentFrame.currModule.findVarIndex(namestr);
        if (index == -1) //if not found, change name
        {
            String oldName = var.varName;
            var.varName = namestr;
            declListModel.setElementAt(getVarString(var), ilist);
            parentFrame.currModule.setDirty(true);
            Component sch = parentFrame.currModule.mySchematic.getDrawableObject(oldName);
            if (sch != null)
            {
                if (sch instanceof SchematicPort)
                    ((SchematicPort) sch).Name = namestr;
                else if (sch instanceof IconInst)
                    ((IconInst) sch).instanceName = namestr;

                parentFrame.mySchematicPanel.repaint();
            }
            if (parentFrame.currModule.myIcon != null)
            {
                IconPort port = parentFrame.currModule.myIcon.getDrawableObject(oldName);
                if (port != null)
                    port.Name = namestr;
                if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
                    parentFrame.iconEditor.myIconPanel.repaint();
            }
        }
        else //another variable by that name - cannot change
        {
            String errstr = "Duplicate names - cannot do.";
            JOptionPane.showMessageDialog(parentFrame, errstr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void finishCopy(int ilist)
    {
        Declaration var = parentFrame.currModule.variables.elementAt(ilist);
        String namestr;
        namestr = JOptionPane.showInputDialog(null, "Copies Name: ", var.varName, JOptionPane.QUESTION_MESSAGE);
        if (namestr == null)
        {
            return; //cancel
        }
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.length() == 0)
        {
            return; //blank
        }
        if (namestr.equals(var.varName))
        {
            return; //same name
        }
        int index = parentFrame.currModule.findVarIndex(namestr);
        if (index == -1) //if not found, make copy
        {
            Declaration newvar = var.duplicate();
            newvar.varName = namestr;
            declListModel.addElement(getVarString(newvar));
            parentFrame.currModule.addVariable(newvar);
            if (var.varDialogType.equals("InputPort"))
            {
                // add to schematic
                parentFrame.currModule.mySchematic.addDrawableObj(new SchematicInport(newvar));
                parentFrame.myStatusPanel.repaint();
                parentFrame.repaint();
                // add to icon
                if (parentFrame.currModule.myIcon == null)
                {
                    this.parentFrame.currModule.myIcon = new Icon(parentFrame.currModule.libNickName, parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
                    GraphicPart_rect mainIcon = new GraphicPart_rect(50, 50, UserPref.rect_col);
                    this.parentFrame.currModule.myIcon.addDrawablePart(mainIcon);
                    mainIcon.movepoint(100, 100);
                }
                IconInport inport = new IconInport(newvar, 0, 0, Color.green);
                parentFrame.currModule.myIcon.addDrawablePart(inport);
                parentFrame.currModule.myIcon.organizePorts();
            }
            else if (var.varDialogType.equals("OutputPort"))
            {
                // add to schematic
                parentFrame.currModule.mySchematic.addDrawableObj(new SchematicOutport(newvar));
                parentFrame.myStatusPanel.repaint();
                parentFrame.repaint();
                // add to icon
                if (parentFrame.currModule.myIcon == null)
                {
                    this.parentFrame.currModule.myIcon = new Icon(parentFrame.currModule.libNickName, parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
                    GraphicPart_rect mainIcon = new GraphicPart_rect(50, 50, UserPref.rect_col);
                    this.parentFrame.currModule.myIcon.addDrawablePart(mainIcon);
                    mainIcon.movepoint(100, 100);
                }
                IconOutport outport = new IconOutport(newvar, 0, 0, Color.green);
                parentFrame.currModule.myIcon.addDrawablePart(outport);
                parentFrame.currModule.myIcon.organizePorts();
            }
            else if (var.varDialogType.equals("SubModule"))
            {
                // add to schematic
                // Now Create the scs.graphics.Icon Inst
                Module iconModule = new Module();
                try
                {
                    iconModule.getModuleFromFile(SCSUtility.getLibPath(newvar.modLibNickName), newvar.varType,
                            newvar.modVersion);
                }
                catch (ClassNotFoundException e)
                {
                    String errStr = "Class Not Found: " + newvar.modLibNickName;
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (IOException e)
                {
                    String errStr = "IOException on " + newvar.modLibNickName;
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (ParserConfigurationException e)
                {
                    String errStr = "ParserConfigurationException on " + newvar.modLibNickName;
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                catch (SAXException e)
                {
                    String errStr = "SAXException on " + newvar.modLibNickName;
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (iconModule.myIcon == null)
                {
                    String errStr = "Module's Icon is null in module: " + iconModule.moduleName;
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (iconModule.myIcon.moduleName == null)
                {
                    String errStr = "Module's Icon name is null";
                    JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                IconInst icon = new IconInst(newvar.varName, iconModule.myIcon, newvar.varParams);
                parentFrame.currModule.mySchematic.addDrawableObj(icon);
                parentFrame.mySchematicPanel.repaint();
                parentFrame.repaint();
            }
            parentFrame.currModule.setDirty(true);
        }
        else //another variable by that name - cannot change
        {
            String errstr = "Duplicate names - cannot do.";
            JOptionPane.showMessageDialog(parentFrame, errstr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finishDelete(String type)
    {
        if (type.equals("Import"))
        {
            removeImport();
        }
        else if (type.equals("Variable"))
        {
            removeVariable();
        }
    }

    private void removeImport()
    {
        if (importList.getSelectedValue() != null)
        {
            if (parentFrame.currModule.imports != null && parentFrame.currModule.imports.contains(importList.getSelectedValue()))
                parentFrame.currModule.imports.remove(importList.getSelectedValue());
            importListModel.remove(importList.getSelectedIndex());
            importList.validate();
            parentFrame.currModule.setDirty(true);
        }
    }

    /**
     * Remove variable
     *
     * @param var - variable to remove
     */
    public void removeVariable(Declaration var)
    {
        String selectedVarStr = getVarString(var);
        if (declListModel.contains(selectedVarStr))
            declListModel.removeElement(selectedVarStr);
    }

    /**
     * Remove selected variable
     */
    private void removeVariable()
    {
        String selectedVarStr = declList.getSelectedValue().toString();
        String selectedVarName = getVariableName(selectedVarStr);
        String errstr = "Do you really want to remove the variable: " + selectedVarName + '?';
        int selected = JOptionPane.showConfirmDialog(null, errstr, "Warning", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (selected == JOptionPane.OK_OPTION)
        {
            boolean worked = parentFrame.currModule.deleteVariable(selectedVarName);
            if (!worked)
            {
                String errstr2 = "Variable:" + selectedVarName + " not found.";
                JOptionPane.showMessageDialog(parentFrame, errstr2, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                declListModel.removeElement(selectedVarStr);
                parentFrame.mySchematicPanel.repaint();
                parentFrame.repaint();
                // Remove port from IconEditor if module is open in it
                if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
                    parentFrame.iconEditor.removeDrawablePart(selectedVarName);
            }
        }
    }

    public void finishAdd(String dialogType)
    {
        if (dialogType.equals("Import"))
        {
            addImport();
        }
        else
        {
            DeclarationDialog dialog = getDialogWindow(dialogType);
            Declaration var = parentFrame.currModule.fillVariableName(this, dialogType, "Variable Instance Name(first letter lower case): ");
            if (var != null)
            {
                dialog.display(var);
                if (dialog.okPressed)
                {
                    dialog.fillDeclarationInfo(var);
                    //now check if a duplicate
                    int foundi = parentFrame.currModule.findVarIndex(var.varName);
                    if (foundi == -1)
                    {
                        // add to nsl
                        parentFrame.currModule.addVariable(var);
                        if (dialogType.equals("InputPort"))
                        {
                            addInport(var);
                        }
                        else if (dialogType.equals("OutputPort"))
                        {
                            addOutport(var);
                        }
                        else if (dialogType.equals("SubModule"))
                        {
                            if (addSubModule(var)) return;

                        }
                        String varString = getVarString(var);
                        declListModel.addElement(varString);
                        declList.setSelectedValue(varString, true);//should scroll
                        parentFrame.currModule.setDirty(true);
                    } //otherwise it has been added already
                }
            }
        }
    }

    /**
     * Add a variable to the list
     *
     * @param var - variable to add
     */
    public void addVariable(Declaration var)
    {
        String varString = getVarString(var);
        declListModel.addElement(varString);
        declList.setSelectedValue(varString, true);
    }

    private boolean addSubModule(Declaration var)
    {
        // add to schematic
        // Now Create the scs.graphics.Icon Inst
        Module iconModule = new Module();
        try
        {
            iconModule.getModuleFromFile(SCSUtility.getLibPath(var.modLibNickName), var.varType, var.modVersion);
        }
        catch (ClassNotFoundException e)
        {
            String errStr = "Class Not Found: " + var.modLibNickName;
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        catch (IOException e)
        {
            String errStr = "IOException on " + var.modLibNickName;
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        catch (ParserConfigurationException e)
        {
            String errStr = "ParserConfigurationException on " + var.modLibNickName;
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        catch (SAXException e)
        {
            String errStr = "SAXException on " + var.modLibNickName;
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        if (iconModule.myIcon == null)
        {
            String errStr = "Module's Icon is null in module: " + iconModule.moduleName;
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        if (iconModule.myIcon.moduleName == null)
        {
            String errStr = "Module's Icon name is null";
            JOptionPane.showMessageDialog(parentFrame, errStr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        IconInst icon = new IconInst(var.varName, iconModule.myIcon, var.varParams);
        parentFrame.currModule.mySchematic.addDrawableObj(icon);
        parentFrame.mySchematicPanel.repaint();
        parentFrame.repaint();
        return false;
    }

    private void addOutport(Declaration var)
    {
        // add to schematic
        parentFrame.currModule.mySchematic.addDrawableObj(new SchematicOutport(var));
        parentFrame.myStatusPanel.repaint();
        parentFrame.repaint();
        // add to icon
        if (parentFrame.currModule.myIcon == null)
        {
            this.parentFrame.currModule.myIcon = new Icon(parentFrame.currModule.libNickName, parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
            GraphicPart_rect mainIcon = new GraphicPart_rect(50, 50, UserPref.rect_col);
            this.parentFrame.currModule.myIcon.addDrawablePart(mainIcon);
            mainIcon.movepoint(100, 100);
        }
        IconOutport outport = new IconOutport(var, 0, 0, Color.green);
        parentFrame.currModule.myIcon.addDrawablePart(outport);
        parentFrame.currModule.myIcon.organizePorts();

        // Add port to IconEditor if module is open in it
        if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
            parentFrame.iconEditor.myIconPanel.repaint();
    }

    private void addImport()
    {
        String msg = "Import Name (package name, can contain * wildcards): ";
        String importStr = JOptionPane.showInputDialog(null, msg, "Import", JOptionPane.QUESTION_MESSAGE);
        if (importStr != null)
        {
            if (parentFrame.currModule.imports == null)
                parentFrame.currModule.imports = new Vector<String>();
            parentFrame.currModule.imports.add(importStr);
            parentFrame.currModule.setDirty(true);
            importListModel.addElement(importStr);
            importList.validate();
        }
    }

    private void addInport(Declaration var)
    {
        // add to schematic
        parentFrame.currModule.mySchematic.addDrawableObj(new SchematicInport(var));
        parentFrame.myStatusPanel.repaint();
        parentFrame.repaint();
        // add to icon
        if (parentFrame.currModule.myIcon == null)
        {
            this.parentFrame.currModule.myIcon = new Icon(parentFrame.currModule.libNickName, parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
            GraphicPart_rect mainIcon = new GraphicPart_rect(50, 50, UserPref.rect_col);
            this.parentFrame.currModule.myIcon.addDrawablePart(mainIcon);
            mainIcon.movepoint(100, 100);
        }
        IconInport inport = new IconInport(var, 0, 0, Color.green);
        parentFrame.currModule.myIcon.addDrawablePart(inport);
        parentFrame.currModule.myIcon.organizePorts();

        // Add port to IconEditor if module is open in it
        if (parentFrame.iconEditor != null && parentFrame.iconEditor.isVisible())
            parentFrame.iconEditor.myIconPanel.repaint();
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getSource().equals(importList))
        {
            removeImportButton.setEnabled((importList.getSelectedValue() != null));
        }
        else if (e.getSource().equals(declList))
        {
            modifyButton.setEnabled((declList.getSelectedValue() != null));
            renameButton.setEnabled((declList.getSelectedValue() != null));
            copyVariableButton.setEnabled((declList.getSelectedValue() != null));
            deleteButton.setEnabled((declList.getSelectedValue() != null));
            upButton.setEnabled((declList.getSelectedValue() != null));
            downButton.setEnabled((declList.getSelectedValue() != null));
            topButton.setEnabled((declList.getSelectedValue() != null));
            bottomButton.setEnabled((declList.getSelectedValue() != null));
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource().equals(classTypeSelect))
            parentFrame.currModule.setDirty(true);
    }

    public void keyPressed(KeyEvent event)
    {
    }

    public void keyTyped(KeyEvent event)
    {
        if (event.getSource().equals(declList))
        {
            if (event.getKeyChar() == KeyEvent.VK_ENTER)
            {
                int listIndex = declList.getSelectedIndex();
                if (listIndex > -1)
                    finishModify(listIndex);
            }

        }
        else
        {
            if (!event.isActionKey())
                parentFrame.currModule.setDirty(true);
        }
    }

    public void keyReleased(KeyEvent event)
    {
    }

    public void mousePressed(MouseEvent evt)
    {
    }

    public void mouseClicked(MouseEvent evt)
    {
        if (evt.getClickCount() == 2)
        {
            int listIndex = declList.getSelectedIndex();
            if (listIndex > -1)
                finishModify(listIndex);
        }
    }

    public void mouseReleased(MouseEvent evt)
    {
    }

    public void mouseEntered(MouseEvent evt)
    {
    }

    public void mouseExited(MouseEvent evt)
    {
    }

    public DeclarationDialog getDialogWindow(String dialogType)
    {
        if (dialogType.equals("SubModule"))
            return new SubmoduleDialog(this, parentFrame.currModule, dialogType);
        else if (dialogType.equals("InputPort") || dialogType.equals("OutputPort"))
            return new PortDialog(this, parentFrame.currModule, dialogType);
        else
            return new DeclarationDialog(this, parentFrame.currModule, dialogType);
    }

    public boolean closeModule()
    {

        if (this.parentFrame.currModule == null)
        {
            //could be null if they tried to quit without saving first
            // and ok-close anyway was selected
            endCloseTool();
            return true;
        }
        onClose();
        endCloseTool();
        return true;

    } //end close

    public void loadMethodParts()
    {
    	try {
            IconMethod myIcon;
            String nslCode = methodsTextArea.getDocument().getText(0, methodsTextArea.getDocument().getLength());
            
        	for(int i = 0; i < SCSUtility.methods.length; i++)
        	{
        		if(nslCode.contains("public void " + SCSUtility.methods[i]) )
        		{
        			myIcon = new IconMethod(parentFrame.currModule.libNickName, SCSUtility.methods[i], parentFrame.currModule.versionName);
        			parentFrame.currModule.mySchematic.addMethod(myIcon);
        			parentFrame.currModule.setDirty(true);
        		}
        		else if(parentFrame.currModule.mySchematic.findDrawableIndex(SCSUtility.methods[i]) != -1)
        		{
        			parentFrame.currModule.mySchematic.deleteDrawableObj(SCSUtility.methods[i]);
        			parentFrame.currModule.setDirty(true);
        		}
     
        	}
            parentFrame.mySchematicPanel.repaint();
            
    		} catch (BadLocationException e) {
    			
    			e.printStackTrace();
    		}
    }
    
    private boolean onClose()
    {
    	loadMethodParts();
        if (parentFrame.currModule.isDirty())
        {
        	parentFrame.currModule.moduleType = classTypeSelect.getSelectedItem().toString();
            parentFrame.currModule.arguments = classArgumentsText.getText();
            parentFrame.currModule.myNslm.extendsWhat = superClassText.getText();
            parentFrame.currModule.myNslm.whatsParams = superclassParametersText.getText();
            parentFrame.currModule.myNslm.implementsWhat = interfaceText.getText();
            parentFrame.currModule.myNslm.comment1 = commentsText.getText();
            parentFrame.currModule.myNslm.methods = methodsTextArea.getDocument();
            if (!parentFrame.currModule.moduleName.equals(parentFrame.currModule.moduleName))
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

                if (n == JOptionPane.YES_OPTION)
                {
                    save();
                    return true;
                }
                else if (n == JOptionPane.NO_OPTION)
                {
                    return true;
                }
                return false;
            }
            else
            {
                parentFrame.currModule.moduleType = classTypeSelect.getSelectedItem().toString();
                parentFrame.currModule.arguments = classArgumentsText.getText();
                parentFrame.currModule.myNslm.extendsWhat = superClassText.getText();
                parentFrame.currModule.myNslm.whatsParams = superclassParametersText.getText();
                parentFrame.currModule.myNslm.implementsWhat = interfaceText.getText();
                parentFrame.currModule.myNslm.comment1 = commentsText.getText();
                parentFrame.currModule.myNslm.methods = methodsTextArea.getDocument();
               return true;
            }
        }
        return true;
    }

    public void endCloseTool()
    {
        this.setTitle("NslmEditorFrame");
        this.parentFrame.currModule = null;
    }

    /**
     * Handle windowClosing event.
     */
    public void windowClosing(WindowEvent event)
    {
        Window w = event.getWindow();
        if (parentFrame.currModule == null)
        {
            w.dispose();
            return;
        }
        if (onClose())
            w.dispose();
    }

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

    public boolean save()
    {
        try
        {
            String srcPath = SCSUtility.getSrcPathUsingNick(parentFrame.currModule.libNickName, parentFrame.currModule.moduleName, parentFrame.currModule.versionName);
            String xmlPath = SCSUtility.getFullPathToXml(srcPath, parentFrame.currModule.moduleName);
            parentFrame.currModule.save(srcPath, xmlPath);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    class CopyAction extends AbstractAction
    {
        public CopyAction()
        {
            super("copy-to-clipboard");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            methodsTextArea.copy();
        }
    }

    class CutAction extends AbstractAction
    {
        public CutAction()
        {
            super("cut-to-clipboard");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            methodsTextArea.cut();
        }
    }

    class PasteAction extends AbstractAction
    {
        public PasteAction()
        {
            super("paste-from-clipboard");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            methodsTextArea.paste();
        }
    }

    class SelectAllAction extends AbstractAction
    {
        public SelectAllAction()
        {
            super("select-all");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            methodsTextArea.selectAll();
        }

    }

    //-------------------------------------------------------------
    class UndoAction extends AbstractAction
    {
        public UndoAction()
        {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                undoManager.undo();
            }
            catch (CannotUndoException ex)
            {
            }
            update();
            methodsRedoAction.update();
        }

        protected void update()
        {
            if (undoManager.canUndo())
            {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getUndoPresentationName());
            }
            else
            {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction
    {
        public RedoAction()
        {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                undoManager.redo();
            }
            catch (CannotRedoException ex)
            {
            }
            update();
            methodsUndoAction.update();
        }

        protected void update()
        {
            if (undoManager.canRedo())
            {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());
            }
            else
            {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    class FindAction extends AbstractAction
    {
        FindAction()
        {
            super("find");
            setEnabled(true);
        }

        FindAction(String name, boolean enabled)
        {
            super(name);
            setEnabled(enabled);
        }

        public void actionPerformed(ActionEvent event)
        {
            if ((methodsTextArea == null) || (parentFrame.currModule.myNslm.methods == null))
            {
                String errstr = "NslmEditorFrame:editor1 or document is null";
                JOptionPane.showMessageDialog(parentFrame, errstr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
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

                methodsFindAgainAction.setEnabled(true);
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
            int carPos = methodsTextArea.getCaretPosition();
            int strLength = methodsTextArea.getText().length();
            //search Forward
            String chunk1 = methodsTextArea.getText(carPos, (strLength - carPos)); //offset,length

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
                methodsTextArea.setCaretPosition(lastFindIndex); //ready to type in body
                methodsTextArea.select(lastFindIndex, lastFindIndex + lastFindStr.length());
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
                    methodsTextArea.setCaretPosition(foo);
                }
            }

            int carPos = methodsTextArea.getCaretPosition();
            //search backward
            String chunk1 = methodsTextArea.getText(0, carPos);
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
                methodsTextArea.setCaretPosition(lastFindIndex); //ready to type in body
                methodsTextArea.select(lastFindIndex, lastFindIndex + lastFindStr.length());
            }
            return (lastFindIndex);
        }
    } //end FindAction

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

    class ReplaceAction extends AbstractAction
    {
        ReplaceAction()
        {
            super("replace");
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event)
        {
            if ((methodsTextArea == null) || (parentFrame.currModule.myNslm.methods == null))
            {
                String errstr = "editor1 or document is null";
                JOptionPane.showMessageDialog(parentFrame, errstr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ReplaceDialog replace = initReplaceDialog();
            replace.setVisible(true);
            if (replace.okPressed)
            {
                lastExistingString = replace.getFindString();
                lastReplacementString = replace.getReplaceString();

                if (replace.isForward())
                    lastReplaceIndex = replaceForward(lastExistingString, lastReplacementString, replace.isGlobal());
                else
                    lastReplaceIndex = replaceBackward(lastExistingString, lastReplacementString, replace.isGlobal());
            }
        }

        protected ReplaceDialog initReplaceDialog()
        {
            ReplaceDialog replace = new ReplaceDialog(null, lastFindStr, lastReplacementString);
            replace.setLocation(400, 400);
            replace.pack();
            replace.setSize(300, 175);
            return replace;
        }

        //----------------------
        //return last location found
        public int replaceForward(String lastExistingString, String lastReplacementString, boolean globalReplace)
        {
            int numFound = 0;
            while (true)
            {
                int carPos = methodsTextArea.getCaretPosition();
                //int strLength = doc1.getLength();
                int strLength = methodsTextArea.getText().length();
                //replace Forward
                String chunk1 = methodsTextArea.getText(carPos, (strLength - carPos)); //offset,length

                lastReplaceIndex = chunk1.indexOf(lastExistingString);
                if (lastReplaceIndex == -1)
                {
                    if (numFound > 0)
                    { //must be global replace
                        //using this instead of warningPopup because warning prints to status window
                        String errStr = "Found " + numFound + " instances.";
                        JOptionPane.showMessageDialog(parentFrame, errStr);
                        return (lastReplaceIndex);
                    }
                    else
                    {
                        int selected = JOptionPane.showConfirmDialog(null, "String not found. Try backward?", "Warning",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        //handle backward
                        if (selected == JOptionPane.OK_OPTION)
                        {
                            lastReplaceIndex = replaceBackward(lastExistingString, lastReplacementString, globalReplace);
                        }
                        else
                        { //cancel
                            return (lastReplaceIndex);
                        }
                    }
                }
                else
                { //found a string
                    lastReplaceIndex = lastReplaceIndex + carPos;
                    lastLastReplaceIndex = lastReplaceIndex;
                    methodsTextArea.setCaretPosition(lastReplaceIndex); //ready to type in body
                    methodsTextArea.select(lastReplaceIndex, lastReplaceIndex + lastExistingString.length());
                    numFound++;
                    if (globalReplace)
                    {
                        methodsTextArea.replaceSelection(lastReplacementString);//replace currently selected text
                        parentFrame.currModule.setDirty(true);
                    }
                    else
                    {
                        int selected = JOptionPane.showConfirmDialog(null, "Replace?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (selected == JOptionPane.YES_OPTION)
                        {
                            methodsTextArea.replaceSelection(lastReplacementString);//replace currently selected text
                            parentFrame.currModule.setDirty(true);
                        }
                    }
                } //end found
                if (!globalReplace)
                    break;
            } //end while
            return (lastReplaceIndex);
        } //end replaceForward

        //----------------------------------------------------
        //return last location found
        public int replaceBackward(String lastExistingString, String lastReplacementString, boolean globalReplace)
        {
            int numFound = 0;
            while (true)
            {
                if ((lastFindStr != null && lastFindStr.length() > 0) || (globalReplace))
                {//otherwise you replace same string
                    int foo = lastLastReplaceIndex; //begining of word
                    if (foo >= 0)
                    {
                        methodsTextArea.setCaretPosition(foo);
                    }
                    System.out.println("Debug:NslmEditorFrame:replaceBackward: foo: " + foo);
                }

                int carPos = methodsTextArea.getCaretPosition();
                //replace backward
                //todo: should we use the getText(pos,len,segment);
                //String chunk1 = doc1.getText(0, carPos);
                String chunk1 = methodsTextArea.getText(0, carPos);
                lastReplaceIndex = chunk1.lastIndexOf(lastExistingString);
                if (lastReplaceIndex == -1)
                {
                    if (numFound > 0)
                    { //must be global replace
                        //using this instead of warningPopup because warning prints to status window
                        String errstr = "Found " + numFound + " instances.";
                        JOptionPane.showMessageDialog(parentFrame, errstr);
                        return (lastReplaceIndex);
                    }
                    else
                    { //numFound==0
                        int selected = JOptionPane.showConfirmDialog(null, "String not found. Try forward?", "Warning",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                        //handle forward
                        if (selected == JOptionPane.OK_OPTION)
                        {
                            lastReplaceIndex = replaceForward(lastExistingString, lastReplacementString, globalReplace);
                            parentFrame.currModule.setDirty(true);
                        }
                        else
                        { //cancel
                            return (lastReplaceIndex);
                        }
                    } //end numFound==0
                }
                else
                { //found
                    lastLastReplaceIndex = lastReplaceIndex;
                    methodsTextArea.setCaretPosition(lastReplaceIndex); //ready to type in body
                    methodsTextArea.select(lastReplaceIndex, lastReplaceIndex + lastExistingString.length());
                    numFound++;
                    if (globalReplace)
                    {
                        methodsTextArea.replaceSelection(lastReplacementString);//replace currently selected text
                        parentFrame.currModule.setDirty(true);
                    }
                    else
                    {
                        int selected = JOptionPane.showConfirmDialog(null, "Replace?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (selected == JOptionPane.YES_OPTION)
                        {
                            methodsTextArea.replaceSelection(lastReplacementString);//replace currently selected text
                            parentFrame.currModule.setDirty(true);
                        }
                    }
                } //end found
                if (!globalReplace)
                    break;
            } //end while
            return (lastReplaceIndex);
        }
    }//end Replace

    class InsertMethodNameAction extends AbstractAction
    {
        String comment = "";
        String methodName = "X";
        String functionPhrase = "public void ";
        String args = "";

        InsertMethodNameAction()
        {
            super("insertMethodNameAction");
            setEnabled(true);
        }

        InsertMethodNameAction(String nm, String cm)
        {
            super(nm);
            methodName = nm;
            comment = cm;
            setEnabled(true);
        }

        InsertMethodNameAction(String nm, String arguments, String cm)
        {
            super(nm);
            args = arguments;
            methodName = nm;
            comment = cm;
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            if ((methodsTextArea == null) || (parentFrame.currModule.myNslm.methods == null))
            {
                String errstr = "Error:editor1 or document is null";
                JOptionPane.showMessageDialog(parentFrame, errstr, "NslmEditorFrame Error", JOptionPane.ERROR_MESSAGE);
            }
            functionPhrase = "/**\n * " + comment + "\n */\n" + functionPhrase + methodName + "(" + args + ")\n{\n\t\n}\n" + '\n';

            int carPos;
            carPos = methodsTextArea.getCaretPosition();
            methodsTextArea.insertText(functionPhrase, carPos);
            parentFrame.currModule.setDirty(true);
            carPos = methodsTextArea.getCaretPosition(); //new location after insert
            if (carPos > 4)
            {
                methodsTextArea.setCaretPosition(carPos - 4); //ready to type in body
            }
        }
    }//end InsertMethodName

    //---------------------------------------------------------------
    class InitSysAction extends InsertMethodNameAction
    {
        InitSysAction()
        {
            super("initSys", "Executed when NSL starts");
        }
    }

    class CallFromConstructorTopAction extends InsertMethodNameAction
    {
        CallFromConstructorTopAction()
        {
            super("callFromConstructorTop", "Object[] args", "Executed at the start of module constructor");
        }
    }

    class CallFromConstructorBottomAction extends InsertMethodNameAction
    {
        CallFromConstructorBottomAction()
        {
            super("callFromConstructorBottom", "Executed at the end of module constructor");
        }
    }

    class MakeConnAction extends InsertMethodNameAction
    {
        MakeConnAction()
        {
            super("makeConn", "Connects input and output ports and submodule ports");
        }
    }

    class InitModuleAction extends InsertMethodNameAction
    {
        InitModuleAction()
        {
            super("initModule", "Executed when the module is initialized");
        }
    }

    class InitTrainEpochsAction extends InsertMethodNameAction
    {
        InitTrainEpochsAction()
        {
            super("initTrainEpochs", "Executed at the start of a set of training epochs");
        }
    }

    class InitTrainAction extends InsertMethodNameAction
    {
        InitTrainAction()
        {
            super("initTrain", "Executed before each training epoch");
        }
    }

    class SimTrainAction extends InsertMethodNameAction
    {
        SimTrainAction()
        {
            super("simTrain", "Executed during each training cycle");
        }
    }

    class EndTrainAction extends InsertMethodNameAction
    {
        EndTrainAction()
        {
            super("endTrain", "Executed at the end of each training epoch");
        }
    }

    class EndTrainEpochsAction extends InsertMethodNameAction
    {
        EndTrainEpochsAction()
        {
            super("endTrainEpochs", "Executed at the end of a set of training epochs");
        }
    }

    class InitRunEpochsAction extends InsertMethodNameAction
    {
        InitRunEpochsAction()
        {
            super("initRunEpochs", "Executed at the start of a set of run epochs");
        }
    }

    class InitRunAction extends InsertMethodNameAction
    {
        InitRunAction()
        {
            super("initRun", "Executed at the start of each run epoch");
        }
    }

    class SimRunAction extends InsertMethodNameAction
    {
        SimRunAction()
        {
            super("simRun", "Executed during each run cycle");
        }
    }

    class EndRunAction extends InsertMethodNameAction
    {
        EndRunAction()
        {
            super("endRun", "Executed at the end of each run epoch");
        }
    }

    class EndRunEpochsAction extends InsertMethodNameAction
    {
        EndRunEpochsAction()
        {
            super("endRunEpochs", "Executed at the end of a set of run epochs");
        }
    }

    class EndModuleAction extends InsertMethodNameAction
    {
        EndModuleAction()
        {
            super("endModule", "Executed when the module is destroyed");
        }
    }

    class EndSysAction extends InsertMethodNameAction
    {
        EndSysAction()
        {
            super("endSys", "Executed when NSL is closed");
        }
    }

    class UndoHandler implements UndoableEditListener
    {
        /**
         * Messaged when the Document has created an edit, the edit is
         * added to <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e)
        {
            undoManager.addEdit(e.getEdit());
            methodsUndoAction.update();
            methodsRedoAction.update();
        }
    }
}
