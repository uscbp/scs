package scs;
/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * Declaration - A class representing the declaration part of variables in a
 * module. A list of variables can be found in the Module class.
 *
 * @author Weifang Xie, Alexander
 * @version     %I%, %G%
 *
 * @since JDK1.1
 * The name of this class should really be called Variable!
 */

import scs.util.SCSUtility;

import java.io.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Declaration
{
    public String varName = "";
    //for variables not ports or submodules
    public String varAccess = "private";
    public String varScope = "local";
    public boolean varConstant = false;
    public String varCategoryType = "";
    public int varDimensions = 0;
    public String varType = "";
    public String varParams = "";
    public String varInits = "";
    public String varDialogType = "";
    //DialogTypes: InputPort, OutputPort, SubModule, BasicVariable
    public String varComment = "";
    public boolean portBuffering = true;
    // next two fields valid input is:
    //'R' for right to left, 'T' for top to bottom
    //'L' for left to right, 'B' for bottom to top
    public char portIconDirection = 'L';
    public char portSchDirection = 'L';
    // valid input is: 'E' for excitatory , 'I' for inhibitory, 'O' for other
    public char portSignalType = 'E';
    public String modLibNickName = "";                //for submodules
    public String modVersion = "";                    //for submodules

    public Declaration clone() throws CloneNotSupportedException
    {
        Declaration d=null;
        try
        {
            d = (Declaration)super.clone();
        }
        catch(Exception e)
        {
            d=new Declaration();
        }
        finally
        {
            if(d!=null)
            {
                d.varName = this.varName;
                d.varAccess = this.varAccess;
                d.varScope = this.varScope;
                d.varConstant = this.varConstant;
                d.varCategoryType = this.varCategoryType;
                d.varDimensions = this.varDimensions;
                d.varType = this.varType;
                d.varParams = this.varParams;
                d.varInits = this.varInits;
                d.varDialogType = this.varDialogType;
                d.varComment = this.varComment;
                d.portBuffering = this.portBuffering;
                d.portIconDirection = this.portIconDirection;
                d.portSchDirection = this.portSchDirection;
                d.portSignalType = this.portSignalType;
                d.modLibNickName = this.modLibNickName;
                d.modVersion = this.modVersion;
            }
        }
        return d;
    }

    /**
     * Constructor of this class with no parameters.
     */
    public Declaration()
    {
    }

    /**
     * Constructor of this class with one parameters.
     */
    public Declaration(String dialogType)
    {
        varDialogType = dialogType;
        if (varDialogType.equals("OutputPort"))
        {
            portSignalType = 'O';
        }
        if ((varDialogType.equals("InputPort")) || (varDialogType.equals("OutputPort")) || (varDialogType.equals("SubModule")))
        {
            varAccess = "public";
            varScope = "local";
        }
    }

    /**
     * Constructor of this class with two parameters.
     * this is the constructor to use with ports
     */
    public Declaration(String dialogType, String name)
    {
        varDialogType = dialogType;
        varName = name;
        if ((varDialogType.equals("InputPort")) || (varDialogType.equals("OutputPort")) || (varDialogType.equals("SubModule")))
        {
            varAccess = "public";
            varScope = "local";
        }
        portIconDirection = 'L';
        portSchDirection = 'L';
        if (varDialogType.equals("OutputPort"))
        {
            portSignalType = 'O';
        }
    }

    /**
     * Constructor of this class with another declaration
     */
    public Declaration(Declaration var)
    {
        varName = var.varName;
        varAccess = var.varAccess;
        varScope = var.varScope;
        varConstant = var.varConstant;
        varCategoryType = var.varCategoryType;
        this.varDimensions = var.varDimensions;
        varType = var.varType;
        varParams = var.varParams;
        varInits = var.varInits;
        varDialogType = var.varDialogType;
        varComment = var.varComment;
        this.portBuffering = var.portBuffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        this.portIconDirection = var.portIconDirection;
        this.portSchDirection = var.portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        this.portSignalType = var.portSignalType;
        this.modLibNickName = var.modLibNickName;
        this.modVersion = var.modVersion;
    }

    /**
     * Constructor of this class, using name, scope, type, param and init to set
     * to the corresponding fields of this class.
     *
     * @param name                 the name of this variable declaration
     * @param scope                the scope of this variable declaration
     * @param constant             whether or not variable is a constant
     * @param catagoryType         the type of this variable declaration without dim
     * @param varDimensions        number of dimensions
     * @param type                 the type of this variable declaration with dim
     * @param param                an string of parameters of this variable declaration
     * @param init                 initial value of this variable declaration
     * @param dialogType
     * @param comment
     * @param buffering
     * @param portIconDirection
     * @param portSchDirection
     * @param portSignalType
     * @param modLibNickName
     * @param modVersion
     * @since JDK1.1
     *        The name of this class should really be called Variable!
     */
    public Declaration(String name, String access, String scope, boolean constant, String catagoryType, String type,
                       int varDimensions, String param, String init, String dialogType, String comment, boolean buffering,
                       char portIconDirection, char portSchDirection, char portSignalType, String modLibNickName,
                       String modVersion)
    {
        varName = name;
        varAccess = access;
        varScope = scope;
        varConstant = constant;
        varCategoryType = catagoryType;
        this.varDimensions = varDimensions;
        varType = type;
        varParams = param;
        varInits = init;
        varDialogType = dialogType;
        varComment = comment;
        this.portBuffering = buffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        this.portIconDirection = portIconDirection;
        this.portSchDirection = portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        this.portSignalType = portSignalType;
        this.modLibNickName = modLibNickName;
        this.modVersion = modVersion;
    }

    //---------------------------------------------------------
    /**
     * duplicate or clone
     */
    public Declaration duplicate()
    {
        Declaration bob = new Declaration();
        bob.varName = this.varName;
        bob.varAccess = this.varAccess;
        bob.varScope = this.varScope;
        bob.varConstant = this.varConstant;
        bob.varCategoryType = this.varCategoryType;
        bob.varDimensions = this.varDimensions;
        bob.varType = this.varType;
        bob.varParams = this.varParams;
        bob.varInits = this.varInits;
        bob.varDialogType = this.varDialogType;
        bob.varComment = this.varComment;
        bob.portBuffering = this.portBuffering;
        // next two fields valid input is:
        //'R' for right to left, 'T' for top to bottom
        //'L' for left to right, 'B' for bottom to top
        bob.portIconDirection = this.portIconDirection;
        bob.portSchDirection = this.portSchDirection;
        // valid input is: 'E' for excitatory , 'I' for inhibitory
        bob.portSignalType = this.portSignalType;
        bob.modLibNickName = this.modLibNickName;
        bob.modVersion = this.modVersion;
        return (bob);
    }

    //-------------------------------------------
    /**
     * scopeIsOther
     * note: check for null before calling
     */
    public boolean scopeIsOther()
    {
        //the scope will say something otherthan private,public,protected
        if (varAccess == null)
        {
            return (true);
        }
        if ((varAccess.equals("private")) || (varAccess.equals("public")) || (varAccess.equals("protected")))
        {
            return (false);
        }
        return (true);
    }

    //-------------------------------------------
    /**
     * Equal - this actually compares the varNames not the whole
     * structure.
     * Put here for Vector use: indexOf, but the equals is
     * called by the incoming variable to indexOf.
     */
    public boolean equal(Object infoo)
    {
        if (infoo == null)
        {
            return false;
        }
        if (infoo instanceof String)
        {
            return varName.equals(infoo);
        }
        if (infoo instanceof Declaration)
        {
            return varName.equals(((Declaration) infoo).varName);
        }
        else
        {
            return false;
        }
    }

    public void readXML(Node variableNode)
    {
        varDialogType=variableNode.getAttributes().getNamedItem("type").getNodeValue();
        NodeList variableChildren=variableNode.getChildNodes();
        for(int i=0; i<variableChildren.getLength(); i++)
        {
            org.w3c.dom.Node variableChild=(org.w3c.dom.Node)variableChildren.item(i);
            if(variableChild.getNodeName().equals("name"))
                varName=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("access"))
                varAccess=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("scope"))
                varScope=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("constant"))
                varConstant=Boolean.parseBoolean(SCSUtility.getNodeValue(variableChild));
            else if(variableChild.getNodeName().equals("categoryType"))
                varCategoryType= SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("dimensions"))
                varDimensions=Integer.parseInt(SCSUtility.getNodeValue(variableChild));
            else if(variableChild.getNodeName().equals("type"))
                varType=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("params"))
                varParams=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("inits"))
                varInits=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("comment"))
                varComment=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("buffering"))
                portBuffering=Boolean.parseBoolean(SCSUtility.getNodeValue(variableChild));
            else if(variableChild.getNodeName().equals("iconDirection"))
                portIconDirection=SCSUtility.getNodeValue(variableChild).charAt(0);
            else if(variableChild.getNodeName().equals("schDirection"))
                portSchDirection=SCSUtility.getNodeValue(variableChild).charAt(0);
            else if(variableChild.getNodeName().equals("signalType"))
                portSignalType=SCSUtility.getNodeValue(variableChild).charAt(0);
            else if(variableChild.getNodeName().equals("library"))
                modLibNickName=SCSUtility.getNodeValue(variableChild);
            else if(variableChild.getNodeName().equals("version"))
                modVersion=SCSUtility.getNodeValue(variableChild);
        }
    }

    //-------------------------------------------
    /**
     * Read this variable declaration from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     */
    public void read(ObjectInputStream os, int sifVersionNumber) throws IOException
    {
        varName = os.readUTF();
        varAccess = os.readUTF();
        if(sifVersionNumber >= 8)
            varScope = os.readUTF();
        else
            varScope = "local";
        varConstant = os.readBoolean();
        varCategoryType = os.readUTF();
        varDimensions = os.readInt();
        varType = os.readUTF();

        varParams = os.readUTF();
        varInits = os.readUTF();
        varDialogType = os.readUTF();
        varComment = os.readUTF();
        portBuffering = os.readBoolean();
        portIconDirection = os.readChar();
        portSchDirection = os.readChar();
        portSignalType = os.readChar();
        modLibNickName = os.readUTF();
        modVersion = os.readUTF();
        /*modGetCurrentVersion = */os.readBoolean();
        /*inIcon = */os.readBoolean();
        /*inSch = */os.readBoolean();
        /*inNslm = */os.readBoolean();
    }

    /**
     * Write this variable declaration to the output stream os.
     *
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os) throws IOException
    {
        try
        {
            os.writeUTF(varName);
            os.writeUTF(varAccess);
            os.writeUTF(varScope);

            os.writeBoolean(varConstant);
            os.writeUTF(varCategoryType);
            os.writeInt(varDimensions);
            os.writeUTF(varType);

            os.writeUTF(varParams);
            os.writeUTF(varInits);
            os.writeUTF(varDialogType);
            os.writeUTF(varComment);
            os.writeBoolean(portBuffering);
            os.writeChar(portIconDirection);
            os.writeChar(portSchDirection);
            os.writeChar(portSignalType);
            os.writeUTF(modLibNickName);
            os.writeUTF(modVersion);
            os.writeBoolean(true);
            os.writeBoolean(true);
            os.writeBoolean(true);
            os.writeBoolean(true);
        }
        catch (IOException e)
        {
            throw new IOException("Declaration write IOException");
        }
    }

    //---------------------------------------------------------------	
    /**
     * writeAllChars this module to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {
        bw.write(prefix+"<variable type=\""+varDialogType+"\">\n");
        bw.write(prefix+"\t<name>"+varName+"</name>\n");
        bw.write(prefix+"\t<access>"+varAccess+"</access>\n");
        bw.write(prefix+"\t<scope>"+varScope+"</scope>\n");
        if(!varDialogType.equals("SubModule"))
        {
            bw.write(prefix+"\t<constant>"+varConstant+"</constant>\n");
            bw.write(prefix+"\t<categoryType>"+varCategoryType+"</categoryType>\n");
            bw.write(prefix+"\t<dimensions>"+varDimensions+"</dimensions>\n");
        }
        bw.write(prefix+"\t<type>"+varType+"</type>\n");
        bw.write(prefix+"\t<params>"+varParams+"</params>\n");
        bw.write(prefix+"\t<inits>"+varInits+"</inits>\n");
        bw.write(prefix+"\t<comment>"+SCSUtility.getXMLFormattedString(varComment)+"</comment>\n");
        if(varDialogType.equals("InputPort") || varDialogType.equals("OutputPort"))
        {
            bw.write(prefix+"\t<buffering>"+portBuffering+"</buffering>\n");
            bw.write(prefix+"\t<iconDirection>"+portIconDirection+"</iconDirection>\n");
            bw.write(prefix+"\t<schDirection>"+portSchDirection+"</schDirection>\n");
            bw.write(prefix+"\t<signalType>"+portSignalType+"</signalType>\n");
        }
        else if(varDialogType.equals("SubModule"))
        {
            bw.write(prefix+"\t<library>"+modLibNickName+"</library>\n");
            bw.write(prefix+"\t<version>"+modVersion+"</version>\n");
        }
        bw.write(prefix+"</variable>\n");
    }//end writeAllChars

    //--------------------------------------------------
    /**
     * writeImport - write Import statments for each submodule
     *
     * @exception IOException     if an IO error occurred
     * Note: to make the mod files portable, the paths
     * should not contain the names of the whole path to the libraries;
     * The libraries should instead be in the CLASSPATH in
     * the order listed in the LIBRARY_PATHS_LIST variable
     * minus the last directory
     */
    public void writeImport(PrintWriter pw) throws FileNotFoundException, IOException
    {
        //todo: need to also allow for NSLC type import statements
        String libPath;
        String libPathLast;
        String srcDirStr;
        String thisVersion;

        if (varDialogType.equals("SubModule") && modLibNickName.length() > 0 && varType.length() > 0 && modVersion.length() > 0)
        {
            try
            {
                libPath = SCSUtility.getLibPath(modLibNickName);
                libPathLast = SCSUtility.getLibPathLast(libPath);
                thisVersion = modVersion;
                srcDirStr = SCSUtility.getFullPathToSrc(libPathLast, varType, thisVersion);
                pw.print("nslImport " + srcDirStr.replace(File.separator, ".") + ".*;\n");
            }
            catch (FileNotFoundException e)
            {
                System.err.println("Declaration: current module src dir not found: " + varType);
                throw (new FileNotFoundException());
            }
            catch (IOException e)
            {
                System.err.println("Declaration: current module IOException while looking for: " + varType);
                throw (new IOException());
            }

        }
    }//end writeImport

    //---------------------------------------------------------------	
    /**
     * writeNSLM this module to the output stream os.
     *
     *
     */
    public void writeNSLM(String parentModuleName, PrintWriter pw)
    {
        if ((varDialogType.equals("InputPort")) || (varDialogType.equals("OutputPort")))
        {
            if (varAccess.length() == 0)
            {
                varAccess = "public";
            }
            pw.print(varAccess + ' ');
            pw.print(varType + ' ' + varName);
            pw.print('(' + varParams + ')');
            pw.print("; // " + varComment + '\n');

            return;
        }
        if (varDialogType.equals("SubModule"))
        {
            if (varAccess.length() == 0)
            {
                varAccess = "public";
            }
            pw.print(varAccess + ' ');
            if(!modLibNickName.equals(parentModuleName))
            {
                pw.print(modLibNickName+"."+varType+".");
                if(!modVersion.startsWith("v"))
                    pw.print("v");
                pw.print(modVersion+".src."+varType + ' ' + varName);
            }
            else
                pw.print(varType + ' ' + varName);
            pw.print('(' + varParams + ')');
            pw.print("; // " + varComment + '\n');
            return;
        }
        if (varDialogType.equals("BasicVariable"))
        {
            if (varAccess.length() == 0)
            {
                varAccess = "protected";
            }
            pw.print(varAccess + ' ');
            if (varConstant)
            {
                //todo: fix for NSLC
                pw.print("final "); //for java only
            }
            if (varScope.equals("static"))
            {
                pw.print("static ");
            }
            pw.print(varType);
            if(!varType.startsWith("Nsl"))
            {
                for(int i=0; i<varDimensions; i++)
                {
                    pw.print("[]");
                }
            }
            pw.print(' ' + varName);
            if (varParams.length() > 0)
            {
                //Nsl vs Native
                if (varCategoryType.startsWith("Nsl"))
                {
                    pw.print('(' + varParams + ')');
                }
            }
            if (varInits.length() > 0)
            {
                pw.print('=' + varInits); //todo: this may require { }
                //but user could put those in at time of editing
            }
            pw.print("; // " + varComment + '\n');
        }
    }//end writeNSLM
    //--------------------------------------------------------
    //-----------------------------------------------
} //end class Declaration
