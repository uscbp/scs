package scs;
/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * NSLM - A class representing the NSLM code of the module - part of Module class
 *
 * @author Xie, Gupta, Alexander
 * @version     %I%, %G%
 *
 * @since JDK1.1
 *
 * Note: this class is part of and gets information from the Module.class.
 * To produce a .mod file it needs the Module class
 * except the schematic and icon info.
 */
import javax.swing.text.PlainDocument;
import scs.JavaDocument;
import scs.util.SCSUtility;
import org.syntax.jedit.*;
import org.w3c.dom.NodeList;

import javax.swing.text.BadLocationException;
import java.io.*;


public class NSLM
{
    public String extendsWhat = "";
    public String implementsWhat = "";
    public String whatsParams = "";
    public String comment1 = ""; //goes before module statement
    public boolean verbatimNSLC = false;
    public boolean verbatimNSLJ = false;
    public String comment2 = "";//goes after module statement
    public SyntaxDocument methods = null;

    public NSLM clone() throws CloneNotSupportedException
    {
        NSLM n=null;
        try
        {
            n=(NSLM)super.clone();
        }
        catch(Exception e)
        {
            n=new NSLM();
        }
        finally
        {
            if(n==null)
                n=new NSLM();
            n.extendsWhat=this.extendsWhat;
            n.implementsWhat=this.implementsWhat;
            n.whatsParams=this.whatsParams;
            n.comment1=this.comment1;
            n.verbatimNSLC=this.verbatimNSLC;
            n.verbatimNSLJ=this.verbatimNSLJ;
            n.comment2=this.comment2;
            n.methods=this.methods;
        }
        return n;
    }
    /**
     * Constructor of this class with no parameters.
     */
    public NSLM()
    {
        extendsWhat = "";

        whatsParams = "";

        comment1 = "";
        verbatimNSLC = false;
        verbatimNSLJ = false;
        comment2 = "";
        methods = null;
    }

    /**
     * Write this NSLM to the output stream os.
     * @param os - object output stream
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os) throws IOException
    {
        try
        {
            os.writeUTF(extendsWhat);
            os.writeUTF(whatsParams);
            os.writeUTF(implementsWhat);
            os.writeUTF(comment1);
            os.writeBoolean(verbatimNSLC);
            os.writeBoolean(verbatimNSLJ);
            os.writeUTF(comment2);
            
            PlainDocument tempPlainDoc = new PlainDocument();
            try
            {
                if(methods!=null)
                    tempPlainDoc.insertString(0, methods.getText(0,methods.getLength()),null);
            }
            catch(Exception e)
            {
                System.out.println("Exception in NSLM write method: " + e);
            }
            
            os.writeObject(tempPlainDoc);
        }
        catch (IOException e)
        {
            System.err.println("Error:NSLM: write IOException: " + e);
            throw new IOException("NSLM write IOException: " + e);
        }
    }

    //----------------------------------------------------
    /**
     * Write this NSLM to the BufferedWriter bw.
     * @param bw - buffered writer
     */
    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {

        bw.write(prefix+"<nslm>\n");
        bw.write(prefix+"\t<extends>"+extendsWhat+"</extends>\n");
        bw.write(prefix+"\t<extendsParams>"+whatsParams+"</extendsParams>\n");
        bw.write(prefix+"\t<implements>"+implementsWhat+"</implements>\n");
        bw.write(prefix+"\t<comment>"+SCSUtility.getXMLFormattedString(comment1)+"</comment>\n");
        String methodStr="";
        if(methods!=null && methods.getDocContent()!=null)
        {
            try
            {
                methodStr=SCSUtility.getXMLFormattedString(methods.getDocContent().getString(0,methods.getDocContent().length()));
                if(methodStr.endsWith("\n"))
                    methodStr=methodStr.substring(0, methodStr.length()-1);
            }
            catch(BadLocationException e)
            {}
        }
        bw.write(prefix+"\t<methods>"+methodStr+"</methods>\n");
        bw.write(prefix+"</nslm>\n");
    }

    public void readXML(org.w3c.dom.Node nslmNode)
    {
        NodeList nslmChildren=nslmNode.getChildNodes();
        for(int i=0; i<nslmChildren.getLength(); i++)
        {
            org.w3c.dom.Node nslmChild=(org.w3c.dom.Node)nslmChildren.item(i);
            if(nslmChild.getNodeName().equals("extends"))
                extendsWhat= SCSUtility.getNodeValue(nslmChild);
            else if(nslmChild.getNodeName().equals("extendsParams"))
                whatsParams=SCSUtility.getNodeValue(nslmChild);
            else if(nslmChild.getNodeName().equals("implements"))
                implementsWhat=SCSUtility.getNodeValue(nslmChild);
            else if(nslmChild.getNodeName().equals("comment"))
                comment1=SCSUtility.getNodeValue(nslmChild);
            else if(nslmChild.getNodeName().equals("methods"))
            {
                methods = new SyntaxDocument();
                try
                {
                    methods.insertString(0, SCSUtility.getNodeValue(nslmChild), null);
                }
                catch(BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //----------------------------------------------------
    /**
     * Read this NSLM from the input stream os.
     * @param os - object input stream
     * @param sifVersion - sif file version number
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(ObjectInputStream os, int sifVersion) throws IOException, ClassNotFoundException
    {
        try
        {
            extendsWhat = os.readUTF();
            whatsParams = os.readUTF();
            if(sifVersion>=9)
                implementsWhat = os.readUTF();
            comment1 = os.readUTF();
            verbatimNSLC = os.readBoolean();
            verbatimNSLJ = os.readBoolean();
            comment2 = os.readUTF();
            Object o=os.readObject();
            if(o instanceof SyntaxDocument)
                methods=(SyntaxDocument)o;
            else if(o instanceof JavaDocument)
            {
                JavaDocument p = (JavaDocument)o;
                methods = new SyntaxDocument();
                try
                {
                    methods.insertString(0, p.getText(0,p.getLength()),null);
                }
                catch(BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
            else if(o instanceof PlainDocument)
            {
                try
                {
                    PlainDocument j = (PlainDocument)o;
                    methods = new SyntaxDocument();
                    methods.insertString(0, j.getText(0,j.getLength()),null);
                }
                catch(BadLocationException e)
                {
                    e.printStackTrace();  
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error:NSLM read FileNotFoundException: " + e);
            throw new FileNotFoundException("NSLM read FileNotFoundException: " + e);
        }
        catch (IOException e)
        {
            System.err.println("Error: NSLM read IOException: " + e);
            throw new IOException("NSLM read IOException: " + e);
        }
    }
}  //end Class NSLM
