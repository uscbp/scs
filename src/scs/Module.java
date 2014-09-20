package scs;
/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * Module - The class representing the module. A module is basically composed of
 * three parts, schematic part for its internal schematic module structure, icon
 * part for its icon representation, and parts used to generate its codes.
 *
 * @author Xie, Gupta, Alexander
 * @version     %W% , %G% - %U%
 *
 * *var       sifVersionNum   the version of the sif file
 *
 * *var       hasIcon   	does this module have an icon
 *
 * *var       hasSchematic   	does this module have a schematic
 *
 * *var       hasNslm   	does this module have NSLM
 *
 * *var       isModel   	is this a model
 *
 * *var 	getCurrentVersion	specifies whether the default for picking up icons
 *	and generating code should default to a specific version of an icon
 * 	or float to the most recent version of that module or icon.
 *
 * *var 	libNickName	specifies the name of the library to which the module belongs

 * *var       moduleName	                the name of the module template that this module
 *				is made an instance of
 *
 * *var	versionName		the version of the module
 *
 * *var      	moduleType		the type of this module, either serial or
 *				parallel
 * *var 	buffering 	specifies the type buffering for output ports
 *					true-double buffering for output ports for simulated
 *					     parallel processing
 * 					false-no buffering for sequential processing
 * *var	myIcon		The icon representation of this module

 * *var	mySchematic	The schematic representation of this module .
 *
 * *var	myNslm		The NSLM representation of this module .
 *
 * @since JDK1.1
 */

import scs.util.SCSUtility;
import scs.graphics.Schematic;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import java.io.*;
import javax.swing.text.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class Module extends Component
{
    int sifVersionNum = SCSUtility.sifVersionNum;
    boolean isModel = false;
    public String libNickName = "LIB1";
    public String moduleName; //template name
    public String versionName;   // version  4_4_4
    public String moduleType; //model,basic,input,output,purecode
    public String arguments = ""; // arguments are copied to NSLM code
    public boolean buffering = false; // 0=false for port buffering
    public Vector<Declaration> variables = null; // ports, submodules, and variables
    public Vector<String> imports = null;

    public scs.graphics.Icon myIcon = null;
    public Schematic mySchematic = null;
    public NSLM myNslm = null;
    
    public String instanceName = "";

    boolean hasIcon=true;
    private boolean dirty=false;
    private Vector<ModuleDirtyListener> dirtyListeners;

    public Module clone() throws CloneNotSupportedException
    {
        Module m=null;
        try
        {
            m = (Module)super.clone();
        }
        catch(Exception e)
        {
            m=new Module();
        }
        finally
        {
            if(m!=null)
            {
                m.sifVersionNum=this.sifVersionNum;
                m.isModel=this.isModel;
                m.libNickName=this.libNickName;
                m.moduleName=this.moduleName;
                m.versionName=this.versionName;
                m.moduleType=this.moduleType;
                m.arguments=this.arguments;
                m.buffering=this.buffering;
                m.imports=new Vector<String>();
                for(int i=0; i<this.imports.size(); i++)
                {
                    m.imports.add(this.imports.get(i));
                }
                m.variables=new Vector<Declaration>();
                for(int i=0; i<this.variables.size(); i++)
                {
                    m.variables.add(this.variables.get(i).clone());
                }
                if(this.myIcon!=null)
                    m.myIcon=this.myIcon.clone();
                m.mySchematic=this.mySchematic.clone();
                m.myNslm=this.myNslm.clone();
                m.dirty=this.dirty;
            }
        }
        return m;
    }

    /**
     * Constructor of this class with no parameters.
     */
    public Module()
    {
        super();
        initModule();
    }

    /**
     * Constructor of this class
     * @param moduleName - name of module
     * @param moduleType - type of module - NslModel, NslJavaModule, NslMatlabModule, or NslClass
     */
    public Module(String moduleName, String moduleType)
    {
        super();
        initModule();
        this.moduleName = moduleName;
        this.moduleType = moduleType;
        if (moduleType.equals("NslModel"))
        {
            isModel = true;
        }
    }

    /**
     * Contructor
     * used EditorFrame for checking if an editor already has that module open
     * @param libNickName2 - library name
     * @param moduleName2 - module name
     * @param versionName2 - version
     */
    public Module(String libNickName2, String moduleName2, String versionName2)
    {
        super();
        initModule();
        libNickName = libNickName2;
        moduleName = moduleName2;
        versionName = versionName2;
    }

    /**
     * Constructor of this class with
     * @param sifVersionNum - sif file version
     * @param libNickName - library nickname
     * @param moduleName - module name
     * @param moduleVersion - module version
     * @param moduleType - type of module - NslModel, NslJavaModule, NslMatlabModule, or NslClass
     * @param arguments - arguments
     * @param moduleBuffering - buffering
     */
    public Module(int sifVersionNum, String libNickName, String moduleName, String moduleVersion, String moduleType,
                  String arguments, boolean moduleBuffering)
    {
        super();
        this.sifVersionNum = sifVersionNum;
        isModel = false;
        this.libNickName = libNickName;
        this.moduleName = moduleName;
        this.versionName = moduleVersion;
        this.moduleType = moduleType;
        if (this.moduleType.equals("NslModel"))
        {
            isModel = true;
        }
        this.arguments = arguments;
        this.buffering = moduleBuffering;
        this.variables = new Vector<Declaration>();

        myIcon = null;
        mySchematic = null;
        myNslm = null;

        this.dirtyListeners=new Vector<ModuleDirtyListener>();
    }

    private void initModule()
    {
        sifVersionNum = SCSUtility.sifVersionNum;
        isModel = false;
        libNickName = "LIB1";
        moduleName = ""; // or template name
        versionName = "";
        moduleType = ""; //NslJavaModule, NslMatlabModule, NslModel, NslClass
        buffering = false;
        arguments = "";
        imports = new Vector<String>();
        variables = new Vector<Declaration>();
        dirty=true;

        myIcon = null;
        mySchematic = null;
        myNslm = null;

        dirtyListeners=new Vector<ModuleDirtyListener>();
    }

    /**
     * set the header of the module
     * @param libNickName String
     * @param moduleName String
     * @param versionName String
     * @param type String
     * @param arguments String
     * @param buffering boolean
     * @param imports Vector
     * @param variables Vector
     */
    public void setHeaderOfModule(String libNickName, String moduleName, String versionName, String type,
                                  String arguments, boolean buffering, Vector<String> imports,
                                  Vector<Declaration> variables)
    {
        this.libNickName= libNickName;
        this.moduleName = moduleName;
        this.versionName = versionName;
        this.moduleType = type;
        if (this.moduleType.equals("NslModel"))
        {
            isModel = true;
        }
        this.arguments = arguments;
        this.buffering = buffering;
        this.imports = imports;
        this.variables = variables;
        if (this.myIcon != null)
        {
            this.myIcon.libNickName = this.libNickName;
            this.myIcon.moduleName = this.moduleName;
            this.myIcon.versionName = this.versionName;
        }
        setDirty(true);
    }

    /**
     * equals
     * return false if not equal
     * @param module1 - module
     * @return    <code>eq</code>
     */
    public boolean equals(Module module1)
    {
        if (module1 == null)
            return false;
        String libNickName2 = module1.libNickName;
        String moduleName2 = module1.moduleName;
        String versionName2 = module1.versionName;

        return libNickName2 != null && moduleName2 != null && versionName2 != null &&
               (libNickName.equals(libNickName2)) && (moduleName.equals(moduleName2)) &&
               (versionName.equals(versionName2));
    }

    /**
     * findVar
     * return null if not found
     * @param name - variable name
     * @return    <code>declaration or variable</code>
     */
    public Declaration getVariable(String name)
    {
        int ix;
        for (ix = 0; ix < variables.size(); ix++)
        {
            if (variables.elementAt(ix).varName.equals(name))
            {
                return variables.elementAt(ix);
            }
        }
        return null;
    }

    /**
     * findVarIndex
     * return -1 if not found
     * @param name - variable name
     * @return    <code>int</code>
     */
    public int findVarIndex(String name)
    {
        int ix;
        for (ix = 0; ix < variables.size(); ix++)
        {
            if (variables.elementAt(ix).varName.equals(name))
            {
                return (ix);
            }
        }
        return (-1);

    }

    /**
     * addVariable
     * @param var - variable to add
     * @return worked - if added, return true
     */
    public boolean addVariable(Declaration var)
    {
        int foundi = findVarIndex(var.varName);
        //already in list
        if (foundi != -1)
        {
            return false;
        }
        else
        {
            //add to the variables list
            variables.addElement(var);
            setDirty(true);
            return true;
        }
    }

    /**
     * addVariable
     * @param index - index to add variable at
     * @param var - variable to add
     * @return worked - if added, return true
     */
    public boolean addVariableAt(int index, Declaration var)
    {
        //only one variable by that name
        int foundi = findVarIndex(var.varName);
        if (foundi != -1)
        {
            return false;  //already in list
        }
        else
        {
            //add to the variables list
            try
            {
                variables.add(index, var);
                setDirty(true);
            }
            catch (ArrayIndexOutOfBoundsException eee)
            {
                return false;
            }
            return true;
        }
    }

    /**
     * deleteVariable
     * @param var - variable to delete
     * @return worked - if deleted, return true
     */
    public boolean deleteVariable(Declaration var)
    {
        int foundi = findVarIndex(var.varName);
        if (foundi == -1)
        {//not found
            return false;
        }
        else
        {
            //delete the variable from the list
            variables.removeElementAt(foundi);
            mySchematic.deleteDrawableObj(var.varName);
            if(myIcon!=null)
                myIcon.deleteDrawablePart(var.varName);
            setDirty(true);
            return true;
        }
    }

    /**
     * deleteVariable
     * @param varName - name of variable to delete
     * @return worked - if deleted, return true
     */
    public boolean deleteVariable(String varName)
    {
        int foundi = findVarIndex(varName);
        if (foundi == -1)
        {
            return false;
        }
        else
        {
            //delete the variable from the list
            variables.removeElementAt(foundi);
            mySchematic.deleteDrawableObj(varName);
            if(myIcon!=null)
                myIcon.deleteDrawablePart(varName);
            setDirty(true);
            return true;
        }
    }

    /**
     * replaceVariable
     * @param varName - name of variable to replace
     * @param var - variable to replace it with
     * @return worked - true if success false otherwise
     */
    public boolean replaceVariable(String varName, Declaration var)
    {
        int foundi = findVarIndex(varName);
        if (foundi == -1)
        {
            return false;
        }
        else
        {
            //delete the variable from the list
            variables.setElementAt(var, foundi);
            setDirty(true);
            return true;
        }
    }

    /**
     * replaceVariable
     * @param foundi - index of variable to replace
     * @param var - variable to replace it with
     * @return worked - true if success false otherwise
     */
    public boolean replaceVariable(int foundi, Declaration var)
    {
        //delete the variable from the list
        variables.setElementAt(var, foundi);
        setDirty(true);
        return true;
    }

    /**
     * moveVariable
     * @param gotoi can be from 0 to the size of the variable list
     * @param varName - name of variable to move
     * @return worked - true if success false otherwise
     * The way this works is that the goto index is as if
     * we cloned the named variable, inserted it in the list
     * at the specified location (thus the list would be
     * the incoming size plus 1), and then deleted the original
     * variable.
     * Variables move down as others are inserted.
     */
    public boolean moveVariable(int gotoi, String varName)
    {
        int foundi = findVarIndex(varName);
        int vectorsize = variables.size();
        if (gotoi < 0 || gotoi > vectorsize || foundi == -1)
        {
            return false;
        }
        else
        {
            Declaration var = variables.elementAt(foundi);
            if (gotoi == vectorsize)
            { //put at end of list
                variables.removeElementAt(foundi);
                variables.add(var); //add at end
                setDirty(true);
                return true;
            }
            else
            {
                variables.removeElementAt(foundi);
                variables.add(gotoi, var); //add somewhere  from 0 to end-1
                setDirty(true);
                return true;
            }
        }
    }

    /**
     * fillVariableName
     * @param parentFrame - parent frame
     * @param dialogType - type of variable
     * @param message - message to show in dialog
     * @return worked
     */
    public Declaration fillVariableName(JFrame parentFrame, String dialogType, String message)
    {
        Declaration var;
        String namestr = JOptionPane.showInputDialog(null, message, "Variable Name", JOptionPane.QUESTION_MESSAGE);
        if (namestr == null)
        {
            return (null); //cancel
        }
        namestr = namestr.trim().replace(' ', '_');
        if (namestr.length()==0)
        {
            return (null); //blank
        }
        int index = findVarIndex(namestr);
        //not found
        if (index == -1)
        {
            var = new Declaration(dialogType, namestr);
        }
        //already in list
        else
        {
            var = variables.elementAt(index);
            if (!(var.varDialogType.equals(dialogType)))
            {
                String errstr = "Module:There is a : " + var.varDialogType + " with that name already.";
                JOptionPane.showMessageDialog(parentFrame, errstr, "Module Erorr", JOptionPane.ERROR_MESSAGE);
                return (null);
            }
        }
        return (var);
    }

    /**
     * Get specified module from the file system
     * @param libPath - library path
     * @param moduleName - name of module
     * @param versionName - module version
     * @throws IOException - if an IO error occurred
     * @throws ClassNotFoundException - if a class-not-found error occurred
     * @throws ParserConfigurationException - if an xml error occurred
     * @throws SAXException - if an xml error occurred
     */
    public void getModuleFromFile(String libPath, String moduleName, String versionName) throws ClassNotFoundException,
            IOException, ParserConfigurationException, SAXException
    {
        FileInputStream fis;
        DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);


        try
        {
            String libName=SCSUtility.getLibNickName(libPath);
            String srcPath=SCSUtility.getSrcPathUsingNick(libName, moduleName, versionName);
            String sifPath=SCSUtility.getFullPathToSif(srcPath, moduleName);
            String xmlPath=SCSUtility.getFullPathToXml(srcPath, moduleName);
            if((new File(xmlPath)).exists())
            {
                fis=new FileInputStream(xmlPath);
                DocumentBuilder builder=domFactory.newDocumentBuilder();
                org.w3c.dom.Document doc=builder.parse(fis);
                readXML(doc);
                fis.close();
            }
            else
            {
                fis=new FileInputStream(sifPath);
                ObjectInputStream ois = new ObjectInputStream(fis);
                read(ois);
                fis.close();
            }

            setDirty(false);
        }
        catch (IOException e)
        {
            System.err.println("Error:Module:getModuleFromFile IOException");
            throw new IOException("Module getModuleFromFile IOException");
        }
        catch (ParserConfigurationException e)
        {
            System.err.println("Error:Module:getModuleFromFile ParserConfigurationException");
            throw new ParserConfigurationException("Module getModuleFromFile ParserConfigurationException");
        }
        catch (SAXException e)
        {
            System.err.println("Error:Module:getModuleFromFile SAXException");
            throw new SAXException("Module getModuleFromFile SAXException");
        }
    }

    /**
     * Get specified module from the file system
     * @param libraryNickName - nickname of module's library
     * @param moduleName - name of module
     * @param versionName - module version
     * @throws IOException - if an IO error occurred
     * @throws ClassNotFoundException - if a class-not-found error occurred
     * @throws ParserConfigurationException - if an xml error occurred
     * @throws SAXException - if an xml error occurred
     */
    public void getModuleFromFileUsingNick(String libraryNickName, String moduleName, String versionName)
            throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException
    {
        FileInputStream fis;
        try
        {
            String src_path = SCSUtility.getSrcPathUsingNick(libraryNickName, moduleName, versionName);
            String testPathAndFile=SCSUtility.getFullPathToXml(src_path, moduleName);
            File tempfile = new File(testPathAndFile);
            if (tempfile.exists())   // does file exist
            {
                fis=new FileInputStream(tempfile.getAbsolutePath());
                DocumentBuilderFactory domFactory=DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);

                DocumentBuilder builder=domFactory.newDocumentBuilder();
                org.w3c.dom.Document doc=builder.parse(fis);
                readXML(doc);
                fis.close();
            }
            else
            {
                testPathAndFile = SCSUtility.getFullPathToSif(src_path, moduleName);
                tempfile=new File(testPathAndFile);
                if(tempfile.exists())
                {
                    fis = new FileInputStream(tempfile.getAbsolutePath());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    read(ois);
                    ois.close();
                    fis.close();
                }
            }
            setDirty(false);
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:Module:getModuleFromFileUsingNick  ClassNotFoundException");
            throw new ClassNotFoundException("Module getModuleFromFileUsingNick ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Error:Module:getModuleFromFileUsingNick IOException");
            throw new IOException("Module getModuleFromFileUsingNick IOException");
        }
        catch (ParserConfigurationException e)
        {
            System.err.println("Error:Module:getModuleFromFileUsingNick  ParserConfigurationException");
            throw new ParserConfigurationException("Module getModuleFromFileUsingNick ParserConfigurationException");
        }
        catch (SAXException e)
        {
            System.err.println("Error:Module:getModuleFromFileUsingNick SAXException");
            throw new SAXException("Module getModuleFromFileUsingNick SAXException");
        }
    }

    /**
     * Read this module from the input stream os.
     * @param ois - ObjectInputStream to read from
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(ObjectInputStream ois) throws ClassNotFoundException, IOException
    {
        try
        {
            sifVersionNum = ois.readInt();  // what software made this file
            hasIcon = ois.readBoolean();
            /*hasSchematic = */ois.readBoolean();
            /*hasNslm = */ois.readBoolean();
            isModel = ois.readBoolean();
            /*getCurrentVersion = */ois.readBoolean();//specific version of icons=1, float=0
            libNickName = ois.readUTF();  // what lib did this come out of
            moduleName = ois.readUTF();  // this is the module name
            versionName = ois.readUTF();
            moduleType = ois.readUTF();
            arguments = ois.readUTF();
            buffering = ois.readBoolean();
            //imports
            imports.removeAllElements();
            int n= ois.readInt();
            if(sifVersionNum>=7)
            {
                // read number of imports
                for(int i=0; i<n; i++)
                {
                    String im=ois.readUTF();
                    imports.add(im);
                }
                n= ois.readInt(); // read number of variables
            }
            for(int i=0; i<n; i++)
            {
                Declaration decl = new Declaration();
                decl.read(ois, sifVersionNum);
                variables.addElement(decl);
            }
            if(hasIcon)
            {
                myIcon = new scs.graphics.Icon();
                myIcon.read(ois);  //throws ClassNotFoundException
            }
            mySchematic = new Schematic();
            mySchematic.read(ois);  // not recursive
            myNslm = new NSLM();
            myNslm.read(ois,sifVersionNum);
            dirty=false;
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:Module: read ClassNotFoundException");
            throw new ClassNotFoundException("Module read ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Error:Module: read IOException");
            throw new IOException("Module read IOException");
        }
    }

    public void readXML(org.w3c.dom.Document doc)
    {
        imports=new Vector<String>();
        variables=new Vector<Declaration>();
        NodeList l=doc.getElementsByTagName("module");
        for(int i=0; i<l.getLength(); i++)
        {
            org.w3c.dom.Node module=l.item(i);
            NodeList moduleChildren=module.getChildNodes();
            for(int j=0; j<moduleChildren.getLength(); j++)
            {
                Node moduleChild=moduleChildren.item(j);
                if(moduleChild.getNodeName().equals("type"))
                {
                    moduleType=SCSUtility.getNodeValue(moduleChild);
                    isModel=false;
                    if(moduleType.equals("model"))
                    {
                        isModel=true;
                        moduleType="NslModel";
                    }
                }
                else if(moduleChild.getNodeName().equals("library"))
                    libNickName=SCSUtility.getNodeValue(moduleChild);
                else if(moduleChild.getNodeName().equals("name"))
                    moduleName=SCSUtility.getNodeValue(moduleChild);
                else if(moduleChild.getNodeName().equals("version"))
                    versionName=SCSUtility.getNodeValue(moduleChild);
                else if(moduleChild.getNodeName().equals("arguments"))
                    arguments=SCSUtility.getNodeValue(moduleChild);
                else if(moduleChild.getNodeName().equals("buffering"))
                    buffering=Boolean.parseBoolean(SCSUtility.getNodeValue(moduleChild));
                else if(moduleChild.getNodeName().equals("imports"))
                {
                    NodeList importChildren=moduleChild.getChildNodes();
                    for(int k=0; k<importChildren.getLength(); k++)
                    {
                        org.w3c.dom.Node importChild=importChildren.item(k);
                        if(importChild.getNodeName().equals("import"))
                            imports.add(SCSUtility.getNodeValue(importChild));
                    }
                }
                else if(moduleChild.getNodeName().equals("variables"))
                {
                    NodeList variablesChildren=moduleChild.getChildNodes();
                    for(int k=0; k<variablesChildren.getLength(); k++)
                    {
                        org.w3c.dom.Node variablesChild=variablesChildren.item(k);
                        if(variablesChild.getNodeName().equals("variable"))
                        {
                            Declaration decl=new Declaration();
                            decl.readXML(variablesChild);
                            variables.addElement(decl);
                        }
                    }
                }
                else if(moduleChild.getNodeName().equals("icon"))
                {
                    myIcon = new scs.graphics.Icon();
                    myIcon.readXML(moduleChild);
                }
                else if(moduleChild.getNodeName().equals("schematic"))
                {
                    mySchematic = new Schematic();
                    mySchematic.readXML(moduleChild);
                }
                else if(moduleChild.getNodeName().equals("nslm"))
                {
                    myNslm = new NSLM();
                    myNslm.readXML(moduleChild);
                }            
            }
        }
        dirty=false;
    }

    public boolean save(String dirStr, String sifFilePath) throws IOException, BadLocationException
    {
        File currDir = new File(dirStr);
        if (!currDir.isDirectory())
        {
            currDir.mkdirs();
        }
        File sifFile = new File(sifFilePath);
        BufferedWriter bw=new BufferedWriter(new FileWriter(sifFile));
        writeXML(bw);
        bw.close();
        setDirty(false);

        // Write NSLM
        String modFilePath=SCSUtility.getFullPathToSrc(SCSUtility.getLibPath(libNickName), moduleName, versionName)+
                File.separator+moduleName+".mod";
        File modFile=new File(modFilePath);
        FileOutputStream fos = new FileOutputStream(modFile);
        PrintWriter pw = new PrintWriter(fos);
        writeNSLM(pw);
        pw.flush();
        fos.close();

        // If Model write ant build file
        /*if(isModel)
        {
            StringBuilder buildFilePath=new StringBuilder(SCSUtility.getCurrLibPath());
            if(!buildFilePath.toString().endsWith(File.separator))
                buildFilePath.append(File.separatorChar);
            buildFilePath.append(moduleName);
            buildFilePath.append("_build.xml");
            fos=new FileOutputStream(new File(buildFilePath.toString()));
            pw=new PrintWriter(fos);
            writeAnt(pw);
            pw.flush();
            fos.close();
        }*/
        return true;
    } //end save

    public void writeXML(BufferedWriter bw) throws IOException
    {
        bw.write("<module>\n");
        bw.write("\t<library>"+libNickName+"</library>\n");
        bw.write("\t<name>"+moduleName+"</name>\n");
        bw.write("\t<version>"+versionName+"</version>\n");
        if(isModel)
            bw.write("\t<type>model</type>\n");
        else
            bw.write("\t<type>"+moduleType+"</type>\n");
        bw.write("\t<arguments>"+arguments+"</arguments>\n");
        bw.write("\t<buffering>"+buffering+"</buffering>\n");
        bw.write("\t<imports>\n");
        if(imports!=null)
        {
            for(int i=0; i<imports.size(); i++)
            {
                bw.write("\t\t<import>"+imports.elementAt(i)+"</import>\n");
            }
        }
        bw.write("\t</imports>\n");
        bw.write("\t<variables>\n");
        if(variables!=null)
        {
            for (int ix = 0; ix < variables.size(); ix++)
            {
                variables.elementAt(ix).writeXML(bw, "\t\t");
            }
        }
        bw.write("\t</variables>\n");
        if (myIcon != null)
            myIcon.writeXML(bw, true, "\t");    // write the icon  representation info for this particular module
        if (mySchematic != null)
            mySchematic.writeXML(bw, "\t");  // Write the schematic info
        if (myNslm != null)
            myNslm.writeXML(bw, "\t");  // Write the NSLM view of the module
        bw.write("</module>\n");
    }
    /**
     * Write this module to the output stream os.
     * @param os - ObjectOutputStream to write to
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os) throws IOException
    {
        try
        {
            os.writeInt(SCSUtility.sifVersionNum);
            // write flags
            os.writeBoolean(hasIcon);
            //os.writeBoolean(hasSchematic);
            os.writeBoolean(true);
            //os.writeBoolean(hasNslm);
            os.writeBoolean(true);
            os.writeBoolean(isModel);
            //os.writeBoolean(getCurrentVersion);
            os.writeBoolean(true);
            os.writeUTF(libNickName);
            os.writeUTF(moduleName);
            os.writeUTF(versionName);             // 99/4/27 aa
            os.writeUTF(moduleType);                  // module type:
            os.writeUTF(arguments);
            os.writeBoolean(buffering);
            //imports
            if(imports!=null)
            {
                os.writeInt(imports.size());
                for(int i=0; i<imports.size(); i++)
                {
                    os.writeUTF(imports.elementAt(i));
                }
            }
            else
                os.writeInt(0);
            //variables
            if(variables!=null)
            {
                os.writeInt(variables.size());
                for (int ix = 0; ix < variables.size(); ix++)
                {
                    variables.elementAt(ix).write(os);
                }
            }
            else
                os.writeInt(0);

            // current choices are : model, basic, input, output, pureNSLM
            if (myIcon != null)
            {
                boolean xminZeroed = true; //we zero the xmin,ymin before writing
                // when writing the icon template info.
                myIcon.write(os, xminZeroed);    // write the icon  representation info for this particular module
            }
            if (mySchematic != null)
            {
                mySchematic.write(os);  // Write the schematic info
            }
            if (myNslm != null)
            {
                myNslm.write(os);  // Write the NSLM view of the module
            }
        }
        catch (IOException e)
        {
            System.err.println("Error:Module: write IOException: " + e);
            throw new IOException("Module write IOException: " + e);
        }
    }

    /**
     * writeNSLM this module to the output stream os.
     * @param pw - PrintWriter to write to
     * @throws IOException     if an IO error occurred
     * @throws BadLocationException  if a BadLocation error occurred
     */
    public void writeNSLM(PrintWriter pw) throws IOException, BadLocationException
    {
        //if no NSLM, then return
        try
        {
            if (!(myNslm.comment1.length()==0))
            {
                pw.print("/** \n" + myNslm.comment1);
                pw.print("\n");
                pw.print("*/" + '\n');
            }
            if ((myNslm.verbatimNSLC) || (myNslm.verbatimNSLJ))
            {
                writeVerbatim(pw);
                return;
            }
            //do import statements
            pw.print("package " + libNickName + '.' + moduleName + ".v" + versionName + ".src;\n");
            Vector<String> pathsVector = new Vector<String>();

            if(imports!=null)
            {
                for(int i=0; i<imports.size(); i++)
                {
                    String imp=imports.get(i);
                    if(imp.endsWith(";"))
                        imp=imp.substring(0,imp.length()-1);
                    if(!imp.endsWith("*"))
                        imp=imp.substring(0,imp.lastIndexOf(".")+1)+"*";
                    pw.println("nslImport "+imp+";");
                }
            }
            if(variables!=null)
            {
                for (int ix = 0; ix < variables.size(); ix++)
                {
                    Declaration sid = variables.elementAt(ix);
                    if ((sid.varDialogType.equals("SubModule")) && (!(sid.modLibNickName.length()==0)) &&
                        (!(sid.varType.length()==0)) && (!(sid.modVersion.length()==0)))
                    {
                        String pseudoPath = sid.modLibNickName + '.' + sid.varType + '.';
                        if(!sid.modVersion.startsWith("v"))
                            pseudoPath += "v";    
                        pseudoPath += sid.modVersion;
                        if (!(pathsVector.contains(pseudoPath)))
                        {
                            pathsVector.addElement(pseudoPath);
                            sid.writeImport(pw);//can throw BadLocationException
                        }
                    }
                } //end for ix<variables.size()
            }
            pw.print("\n");

            if (isModel)
            {
                pw.print("nslModel");
            }
            else
            {
                String t1 = moduleType.substring(1, moduleType.length());
                pw.print('n' + t1);
            }
            pw.print(' ' + moduleName + '(' + arguments + ')');
            if (!(myNslm.extendsWhat.length()==0))
            {
                pw.print(" extends " + myNslm.extendsWhat + '(');
                pw.print(myNslm.whatsParams + ')');
            }
            if (!(myNslm.implementsWhat.length()==0))
            {
                pw.print(" implements " + myNslm.implementsWhat);
            }
            pw.print("{\n");
            pw.print("\n");
            pw.print("//NSL Version: " + SCSUtility.nslmVersionNum + '\n');
            pw.print("//Sif Version: " + SCSUtility.sifVersionNum + '\n');
            pw.print("//libNickName: " + libNickName + '\n');
            pw.print("//moduleName:  " + moduleName + '\n');
            pw.print("//versionName: " + versionName + '\n');
            pw.print("\n");

            if (!(myNslm.comment2.length()==0))
            {
                pw.print("/** \n" + myNslm.comment2);
                pw.print("\n");
                pw.print("*/" + '\n');
            }

            //variables
            pw.print("\n//variables \n");
            for (int ix = 0; ix < variables.size(); ix++)
            {
                variables.elementAt(ix).writeNSLM(moduleName, pw);
            }

            //no comment3 block since they can put this in the methods section
            pw.print("\n//methods \n");
            //todo: put the setBuffering command in initSys in methods

            if ((myNslm != null) && (myNslm.methods != null))
            {
                if (myNslm.methods != null)
                {
                    int strLength = myNslm.methods.getLength();
                    String methStr = (myNslm.methods.getText(0, strLength)).trim(); //badLocationException
                    pw.print(methStr); //not efficient
                    pw.print("\n");
                }
            }

            writeMakeConn(pw);

            pw.print("\n");
            pw.print("}//end " + moduleName + '\n');
            pw.print("\n"); //THIS IS THE LAST LINE
            pw.flush();
        }//end try 1
        catch (BadLocationException e)
        {
            String errstr = "Module: writeNSLM BadLocationException";
            System.err.println(errstr);
            throw (new BadLocationException(errstr, e.offsetRequested()));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Module: writeNSLM FileNotFoundException");
            throw (new FileNotFoundException("Module writeNSLM FileNotFoundException"));
        }
        catch (IOException e)
        {
            System.err.println("Error:Module: writeNSLM IOException");
            throw (new IOException("Module writeNSLM IOException"));
        }
    }

    public void writeVerbatim(PrintWriter pw) throws BadLocationException
    {
        if (myNslm == null)
        {
            return;
        }
        if (myNslm.verbatimNSLC)
        {
            pw.print("verbatim_NSLC;");
        }
        else
        {
            pw.print("verbatim_NSLJ;");
        }
        if (myNslm.methods != null)
        {
            try
            {
                int strLength = myNslm.methods.getLength();
                String methStr = (myNslm.methods.getText(0, strLength)).trim();//badLocationException
                pw.print(methStr); //not efficient
                pw.print("\n");
            }
            catch (BadLocationException e)
            {
                String errstr = "Module: writeVerbatim BadLocationException";
                System.err.println(errstr);
                throw (new BadLocationException(errstr, e.offsetRequested()));
            }
        }
        pw.print("verbatim_off;");
    }

    public void writeMakeConn(PrintWriter pw)
    {
        if (mySchematic == null)
        {
            return;
        }
        if (mySchematic.drawableObjs == null)
        {
            return;
        }

        pw.print("public void makeConn(){");
        mySchematic.writeNslm(pw);
        pw.print("}");

    }
    
    public void writeAnt(PrintWriter pw) throws IOException, ClassNotFoundException, ParserConfigurationException,
            SAXException
    {
        if(isModel)
        {
            pw.print("<project name=\"");
            pw.print(this.moduleName);
            pw.print("\" default=\"");
            pw.print(this.moduleName);
            pw.print("_compile\" basedir=\".\">\n");
            pw.println();
        }

        StringBuilder dependsPath=new StringBuilder("");
        int subModIdx=0;

        for(int i=0; i<variables.size(); i++)
        {
            if(variables.get(i).varDialogType.equals("SubModule"))
            {
                if(subModIdx>0)
                    dependsPath.append(",");
                dependsPath.append(variables.get(i).varType);
                dependsPath.append("_compile");
                subModIdx++;
                SCSUtility.openModule(variables.get(i).modLibNickName, variables.get(i).varType, 
                                      variables.get(i).modVersion).writeAnt(pw);
                pw.println();
            }
        }

        pw.print("\t<target name=\"");
        pw.print(this.moduleName);
        pw.print("_compile\"");
        if(dependsPath.toString().length()>0)
        {
            pw.print(" depends=\"");
            pw.print(dependsPath);
            pw.print("\"");
        }
        pw.print(">\n");
        String srcPath=SCSUtility.getSrcPathUsingNick(this.libNickName, this.moduleName, this.versionName);
        pw.print("\t\t<javac srcdir=\"");
        pw.print(srcPath);
        pw.print("\" destdir=\"");
        pw.print(srcPath);
        pw.print("\">\n");
        pw.print("\t\t</javac>\n");
        pw.print("\t</target>\n");
        if(isModel)
            pw.println("</project>");
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean d)
    {
        this.dirty=d;
        for(int i=0; i<dirtyListeners.size(); i++)
        {
            if(d)
                dirtyListeners.get(i).moduleDirtied();
            else
                dirtyListeners.get(i).moduleCleaned();
        }

    }

    public void addDirtyListener(ModuleDirtyListener listener)
    {
        dirtyListeners.add(listener);
    }

    public void removeDirtyListener(ModuleDirtyListener listener)
    {
        dirtyListeners.remove(listener);
    }
} //end class Module

