package scs.graphics;
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.  
// Copyright: This software may be freely copied provided the toplevel 
// Copyright: COPYRIGHT file is included with each such copy.  
// Copyright: Email nsl@java.usc.edu.

/**
 * Schematic - A class representing the schematic structure of a module. It's
 * composed of all those drawableObjs of a schematic representation, including
 * modules, schematic inports, schematic outports, and connections.
 *
 * @author Xie, Gupta, Alexander
 * @version     %I%, %G%
 *
 * @param    drawableObjs   an array of drawableObjs of this schematic, each component being either a IconInst,
 *				            SchematicInport, SchematicOutport, or a Connection
 *
 * @param    selComponent   pointing to the selected component or interconnect of this schematic
 *
 * @since JDK1.1
 *
 * Note: in version 1.4 we have tried to make the schematic a stand alone entity
 * which contains a list of drawable objects some of which are IconInsts which
 * point to Icons within the schematics Icon list.
 */

import scs.util.SCSUtility;

import java.awt.*;
import java.io.*;
import java.util.Vector;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class Schematic
{
    // protected Vector icons; add if trying to be efficient on space
    public Vector<Component> drawableObjs;
    boolean schModified = false;
    public Component selComponent = null;
    
    //Determines if the method icons are in the schematic
    private boolean methodsSet = false;
    
    public Schematic clone() throws CloneNotSupportedException
    {
        Schematic s=null;
        try
        {
            s=(Schematic)super.clone();
        }
        catch(Exception e)
        {
            s=new Schematic();
        }
        finally
        {
            s.drawableObjs=new Vector<Component>();
            for(int i=0; i<drawableObjs.size(); i++)
            {
                try
                {
                    s.drawableObjs.add(((GraphicPart)drawableObjs.get(i)).clone());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            s.schModified=this.schModified;
            s.selComponent=this.selComponent;
        }
        return s;
    }
    /**
     * Constructor of this class with no parameters.
     */
    public Schematic()
    {
        drawableObjs = new Vector<Component>();
    }

    /**
     * Constructor of this class, which is constructed out of an existing
     * schematic.
     * @param schematic - schematic to copy
     */
    public Schematic(Schematic schematic)
    {
        drawableObjs = new Vector<Component>();

        if (schematic == null)
        {
            System.err.println("Error:Schematic:constructor: schematic to copy is null.");
            return;
        }

        if (schematic.drawableObjs != null)
        {
            for (int ix = 0; ix < schematic.drawableObjs.size(); ix++)
            {
                drawableObjs.addElement(schematic.drawableObjs.get(ix));
            }
        }
    }

    /**
     * Add a drawableObj to this schematic.
     * @param newComponent - the component to be added to this schematic
     */
    public void addDrawableObj(Component newComponent)
    {
        schModified = true;
        drawableObjs.addElement(newComponent);
    }

    /**
     * findDrawableIndex
     * @param name - the iconInst selected
     * @return index of the drawable object
     */
    public int findDrawableIndex(String name)
    {
        for (int i = 0; i < drawableObjs.size(); i++)
        {
            if (drawableObjs.get(i) instanceof IconInst)
            {
                if (((IconInst)drawableObjs.get(i)).instanceName.equals(name))
                    return i;
            }
            if (drawableObjs.get(i) instanceof SchematicInport)
            {
                if (((SchematicInport) drawableObjs.get(i)).Name.equals(name))
                    return i;
            }
            if (drawableObjs.get(i) instanceof SchematicOutport)
            {
                if (((SchematicOutport) drawableObjs.get(i)).Name.equals(name))
                    return i;
            }
            if (drawableObjs.get(i) instanceof IconMethod)
            {
                if (((IconMethod)drawableObjs.get(i)).methodName.equals(name))
                    return i;
            }
        }
        return -1;
    }

    /**
     * Add a new IconInst to this schematic, and add a new icon
     * to the list of Icons if necessary.
     * @param newIconInst - the iconInst selected
     * @return whether or not successful in adding the icon
     */
    public boolean addIconInst(IconInst newIconInst)
    {
        boolean worked = true;

        if (newIconInst == null)
        {
            System.err.println("Error:Schematic:addIconInst: newIconInst is null ");
            return (!worked);
        }
        if (newIconInst.instanceName.length()==0)
        {
            System.err.println("Error:Schematic:addIconInst: instance name is blank");
            return (!worked);
        }
        int dup = findDrawableIndex(newIconInst.instanceName);
        if (dup != -1)
        {
            return (!worked);
        }
        else
        {
            addDrawableObj(newIconInst);
            return (worked);
        }
    }

    /**
     * Add a new IconInst to this schematic, and add a new icon
     * to the list of Icons if necessary.
     * @param newIconInst - the iconInst selected
     * @return whether or not successful in adding the icon
     */
    public boolean addMethod(IconMethod newIconMethodInst)
    {
        boolean worked = true;

        if (newIconMethodInst == null)
        {
            System.err.println("Error:Schematic:addIconInst: newIconInst is null ");
            return (!worked);
        }
        if (newIconMethodInst.methodName.length()==0)
        {
            System.err.println("Error:Schematic:addIconInst: instance name is blank");
            return (!worked);
        }
        int dup = findDrawableIndex(newIconMethodInst.methodName);
        if (dup != -1)
        {
            return (!worked);
        }
        else
        {
            addDrawableObj(newIconMethodInst);
            return (worked);
        }
    }
    
    public Component getDrawableObject(String name)
    {
        int idx=findDrawableIndex(name);
        if(idx>-1 && idx<drawableObjs.size())
            return drawableObjs.get(idx);
        else
            return null;
    }

    public boolean deleteDrawableObj(String name)
    {
        int idx=findDrawableIndex(name);
        return idx>-1 && idx<drawableObjs.size() && deleteDrawableObj(drawableObjs.get(idx));
    }

    /**
     * Delete a drawableObj from this schematic.
     * @param    deletedComponent    the component to be deleted from this schematic
     * @return wether or not successful in deleting the object
     */
    public boolean deleteDrawableObj(Component deletedComponent)
    {
        schModified = true;
        Connection conn;

        // connection
        if (deletedComponent instanceof Connection)
        {
            conn = (Connection) deletedComponent;

            if (conn.src_iconOrSchPort!=null)
            {
                if(conn.src_iconOrSchPort instanceof IconInst && conn.src_port!=null)
                {
                    conn.src_port.disconnect(conn);
                }
                if (conn.src_iconOrSchPort instanceof SchematicInport)
                {
                    ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);
                }
            }

            if (conn.dest_iconOrSchPort != null)
            {
                if (conn.dest_iconOrSchPort instanceof IconInst && conn.dest_port!=null)
                {
                    conn.dest_port.disconnect();
                }
                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                {
                    ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                }
            }

            drawableObjs.removeElement(deletedComponent);
        }
        // SchematicInport
        else if (deletedComponent instanceof SchematicInport)
        {
            SchematicInport sip = (SchematicInport) deletedComponent;

            for (int i = 0; i < sip.links.size(); i++)
            {
                conn = sip.links.get(i);

                if (conn.dest_iconOrSchPort != null)
                {
                    if (conn.dest_iconOrSchPort instanceof IconInst && conn.dest_port!=null)
                    {
                        conn.dest_port.disconnect();
                    }
                    if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                    {
                        ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                    }
                }
                sip.disconnect(conn);
                i--;
                drawableObjs.removeElement(conn);
            }

            drawableObjs.removeElement(deletedComponent);
        }
        // SchematicOutport
        else if (deletedComponent instanceof SchematicOutport)
        {
            SchematicOutport sop = (SchematicOutport) deletedComponent;

            if (sop.link != null)
            {
                conn = sop.link;

                if (conn.src_iconOrSchPort!=null)
                {
                    if (conn.src_iconOrSchPort instanceof IconInst && conn.src_port!=null)
                    {
                        conn.src_port.disconnect(conn);
                    }
                    if (conn.src_iconOrSchPort instanceof SchematicInport)
                    {
                        ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);
                    }
                }

                sop.disconnect();
                drawableObjs.removeElement(conn);
            }

            drawableObjs.removeElement(deletedComponent);
        }
        // Text
        else if (deletedComponent instanceof GraphicPart_text)
        {
            drawableObjs.removeElement(deletedComponent);
        }

     // Text
        else if (deletedComponent instanceof IconMethod)
        {
            drawableObjs.removeElement(deletedComponent);
        }
        // IconInst
        else if (deletedComponent instanceof IconInst)
        {
            Vector iconsDrawables = ((IconInst) deletedComponent).drawableParts;
            if (iconsDrawables != null)
            {
                for (int i = 0; i < iconsDrawables.size(); i++)
                {
                    GraphicPart gobj = (GraphicPart) iconsDrawables.get(i);
                    if (gobj instanceof IconInport)
                    {
                        IconInport ip = (IconInport) gobj;
                        conn = ip.link;
                        if (conn != null)
                        {
                            if (conn.src_iconOrSchPort!=null)
                            {
                                if (conn.src_iconOrSchPort instanceof IconInst && conn.src_port!=null)
                                {
                                    conn.src_port.disconnect(conn);
                                }
                                if (conn.src_iconOrSchPort instanceof SchematicInport)
                                {
                                    ((SchematicInport) conn.src_iconOrSchPort).disconnect(conn);
                                }
                            }
                            if (conn.dest_port != null)
                            {
                                conn.dest_port.disconnect();
                            }
                            drawableObjs.removeElement(conn);
                        }
                    }
                    if (gobj instanceof IconOutport)
                    {
                        IconOutport op = (IconOutport) gobj;
                        for (int j = 0; j < op.links.size(); j++)
                        {
                            conn = op.links.get(j);
                            if (conn.dest_iconOrSchPort != null)
                            {
                                if (conn.dest_iconOrSchPort instanceof IconInst && conn.dest_port!=null)
                                {
                                    conn.dest_port.disconnect();
                                }
                                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                                {
                                    ((SchematicOutport) conn.dest_iconOrSchPort).disconnect();
                                }
                            }
                            if (conn.src_port != null)
                            {
                                conn.src_port.disconnect(conn);
                                j--;
                            }
                            drawableObjs.removeElement(conn);
                        }
                    }
                } //end for (int i=0; i<iconsDrawables.size(); i++)
                drawableObjs.removeElement(deletedComponent);

            } //end if iconsDrawables!=null
        } //end if instanceof graphics.IconInst
        else
            return false;
        return true;
    } //end method
//----------------------------------------------------------------------------

    /**
     * Delete all drawableObjs from this schematic.
     */
    public void deleteAllDrawableObjs()
    {
        drawableObjs.removeAllElements();
    }
//----------------------------------------------------------------------------

    /**
     * Push the ix'th component of this schematic to the last of the drawableObjs
     * array. Since we are drawing the drawableObjs in the order that they're in
     * the array, so being the last component means it will be the last to be
     * painted, so appears in the top position.
     *
     * @param ix the index of the component in the array that's gonna
     *           be pushed to the top
     */
    public void pushtop(int ix)
    {
        Component tempComponent;
        tempComponent = drawableObjs.get(ix);
        drawableObjs.removeElementAt(ix);
        drawableObjs.addElement(tempComponent);
    }

    public void shift(int dx, int dy)
    {
        for(int i=0; i<drawableObjs.size(); i++)
        {
            if(drawableObjs.get(i) instanceof IconInst)
            {
                ((IconInst)drawableObjs.get(i)).moveobj(dx, dy);
            }
            else if(drawableObjs.get(i) instanceof GraphicPart)
            {
                ((GraphicPart)drawableObjs.get(i)).moveobj(dx, dy);
            }
            else if(drawableObjs.get(i) instanceof Connection)
            {
                for(int j=1; j<((Connection)drawableObjs.get(i)).numVerticies-1; j++)
                {
                    ((Connection)drawableObjs.get(i)).x[j]+=dx;
                    ((Connection)drawableObjs.get(i)).y[j]+=dy;
                }
            }
        }
    }

    /**
     * Paint this schematic.
     * @param g - Graphics object to paint to
     */
    public void paint(Graphics g)
    { //use to be called paint
        if (drawableObjs != null)
        {
            for (int ix = 0; ix < drawableObjs.size(); ix++)
            {
                drawableObjs.get(ix).paint(g);
            }
        }
    }

    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {
        bw.write(prefix+"<schematic>\n");
        bw.write(prefix+"\t<inports>\n");
        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);

                if (comp instanceof SchematicInport)
                {
                    ((SchematicInport)comp).writeXML(bw,prefix+"\t\t");
                }
            }
        }
        bw.write(prefix+"\t</inports>\n");
        bw.write(prefix+"\t<outports>\n");
        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);

                if (comp instanceof SchematicOutport)
                {
                    ((SchematicOutport)comp).writeXML(bw, prefix+"\t\t");
                }
            }
        }
        bw.write(prefix+"\t</outports>\n");
        bw.write(prefix+"\t<textObjects>\n");
        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);

                if (comp instanceof GraphicPart_text)
                {
                    ((GraphicPart_text)comp).writeXML(bw, 0, 0, prefix+"\t\t");
                }
            }
        }
        bw.write(prefix+"\t</textObjects>\n");
        bw.write(prefix+"\t<methodIcons>\n");
        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);

                if (comp instanceof IconMethod)
                {
                    ((IconMethod)comp).writeXML(bw, false, prefix+"\t\t");
                }
            }
        }
        bw.write(prefix+"\t</methodIcons>\n");
        bw.write(prefix+"\t<icons>\n");
        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);

                if (comp instanceof IconInst)
                {
                    ((IconInst)comp).writeXML(bw, prefix+"\t\t");
                }
            }
        }
        bw.write(prefix+"\t</icons>\n");
        bw.write(prefix+"\t<connections>\n");
        for (int i = 0; i < drawableObjs.size(); i++)
        {
            Component comp = drawableObjs.get(i);
            if (comp instanceof Connection)
            {
                Connection conn = (Connection) comp;
                if(conn.src_iconOrSchPort!=null)
                {
                    bw.write(prefix+"\t\t<connection>\n");
                    conn.writeXML(bw, "\t\t\t");
                    writePortSrcConnectionXML(bw, conn, "\t\t\t");
                    writePortDestConnectionXML(bw, conn, "\t\t\t");
                    bw.write(prefix+"\t\t</connection>\n");
                }
            } //end if
        } //end for
        bw.write(prefix+"\t</connections>\n");
        bw.write(prefix+"</schematic>\n");
    }
    /**
     * Write this schematic to the output stream oos.
     * @param oos - ObjectOutputStream to write to
     * @throws FileNotFoundException if a file-not-found error occurred
     * @throws IOException           if an IO error occurred
     */
    public void write(ObjectOutputStream oos) throws IOException
    {
        int compCount = 0;
        try
        {
            if (drawableObjs != null)
            {
                // count the number of drawableObjs that are not connections
                for (int i = 0; i < drawableObjs.size(); i++)
                {
                    Component comp = drawableObjs.get(i);

                    if (!(comp instanceof Connection))
                        compCount++;
                }
            }

            oos.writeInt(compCount); //  write down the component count

            if (drawableObjs != null)
            {
                for (int i = 0; i < drawableObjs.size(); i++)
                {
                    Component comp = drawableObjs.get(i);

                    if (comp instanceof SchematicInport)
                    {
                        oos.writeInt(1);  //SchematicInport
                        ((SchematicInport) comp).write(oos);
                    }
                    if (comp instanceof SchematicOutport)
                    {
                        oos.writeInt(2);  //SchematicOutport
                        ((SchematicOutport) comp).write(oos);
                    }
                    if (comp instanceof GraphicPart_text)
                    {
                        oos.writeInt(4);  //Graphic text

                        ((GraphicPart_text) comp).write(oos, 0, 0);
                    }
                    if (comp instanceof IconMethod)
                    {
                        oos.writeInt(5);  //Graphic text

                        ((IconMethod) comp).write(oos, false);
                    }
                    // IconInst
                    if (comp instanceof IconInst)
                    {
                        oos.writeInt(3);  //IconInst  code
                        ((IconInst) comp).write(oos);
                    }
                }
            }

            //begin to write Connection
            if (drawableObjs == null)
            {
                oos.writeInt(0);
            }
            else
            {
                //begin to write number of Connection
                oos.writeInt(drawableObjs.size() - compCount);

                for (int i = 0; i < drawableObjs.size(); i++)
                {
                    Component comp = drawableObjs.get(i);
                    if (comp instanceof Connection)
                    {
                        Connection conn = (Connection) comp;
                        conn.write(oos);
                        writePortSrcConnection(oos, conn);
                        writePortDestConnection(oos, conn);
                    } //end if
                } //end for
            } //end else
        } //end try
        catch (FileNotFoundException e)
        {
            System.err.println("Error:Schematic:write: FileNotFoundException");
            throw new FileNotFoundException("Schematic IconInst write: FileNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Error:Schematic:write: IOException");
            throw new IOException("Schematic write: IOException");
        }
    }

    public static void writePortSrcConnectionXML(BufferedWriter bw, Connection conn, String prefix) throws IOException
    {
        if(conn!=null)
        {
            if (conn.src_iconOrSchPort instanceof SchematicInport)
            {
                bw.write(prefix+"<sourcePort type=\"inport\">\n");
                bw.write(prefix+"\t<name>"+((SchematicInport) conn.src_iconOrSchPort).Name+"</name>\n");
            }
            else if (conn.src_iconOrSchPort instanceof IconInst)
            {
                bw.write(prefix+"<sourcePort type=\"icon\">\n");
                bw.write(prefix+"\t<name>"+((IconInst) conn.src_iconOrSchPort).instanceName+"</name>\n");
                Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
                if (iconsDrawables != null)
                {
                    for (int j = 0; j < iconsDrawables.size(); j++)
                    {
                        Component icon_comp = (Component) iconsDrawables.get(j);

                        if (icon_comp instanceof IconOutport)
                        {
                            if (((IconOutport) icon_comp).Name.equals(conn.src_port.Name))
                            {
                                bw.write(prefix+"\t<portIndex>"+j+"</portIndex>\n");
                                bw.write(prefix+"\t<portName>"+conn.src_port.Name+"</portName>\n");
                                break;
                            }
                        }//end for
                    }
                }
            }
            bw.write(prefix+"</sourcePort>\n");
        }
    }
    
    /**
     * Write Port Source Connection List
     * @param oos - ObjectOutputStream to write to
     * @param conn - Connection whose source port to write
     * @throws IOException in case of failure
     */
    public static void writePortSrcConnection(ObjectOutputStream oos, Connection conn) throws IOException
    {
        try
        {
            if (conn.src_iconOrSchPort == null)
            {
                oos.writeInt(0);
                return;
            }
            oos.writeInt(1); // exists a src
            if (conn.src_iconOrSchPort instanceof SchematicInport)
            {
                oos.writeInt(1);
                oos.writeUTF(((SchematicInport) conn.src_iconOrSchPort).Name);
                return;
            }
            if (conn.src_iconOrSchPort instanceof IconInst)
            {
                oos.writeInt(2);
                oos.writeUTF(((IconInst) conn.src_iconOrSchPort).instanceName);

                //write src_port's index
                Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
                if (iconsDrawables != null)
                {
                    for (int j = 0; j < iconsDrawables.size(); j++)
                    {
                        Component icon_comp = (Component) iconsDrawables.get(j);

                        if (icon_comp instanceof IconOutport)
                        {
                            if (((IconOutport) icon_comp).Name.equals(conn.src_port.Name))
                            {
                                oos.writeInt(j);
                                oos.writeUTF(conn.src_port.Name);
                                break;
                            }
                        }
                    }//end for
                }
            }
        } //end try
        catch (IOException e)
        {
            System.err.println("Error:Schematic:writePortSrcConnection: IOException");
            throw new IOException("Schematic writePortSrcConnection: IOException");
        }
    } //end writePortSrcConnection

    public static void writePortDestConnectionXML(BufferedWriter bw, Connection conn, String prefix) throws IOException
    {
        if(conn!=null)
        {
            if (conn.dest_iconOrSchPort instanceof SchematicOutport)
            {
                bw.write(prefix+"<destinationPort type=\"outport\">\n"); //type
                bw.write(prefix+"\t<name>"+((SchematicOutport) conn.dest_iconOrSchPort).Name+"</name>\n");
                bw.write(prefix+"</destinationPort>\n");
            }
            else if (conn.dest_iconOrSchPort instanceof IconInst)
            {
                bw.write(prefix+"<destinationPort type=\"icon\">\n"); //type
                bw.write(prefix+"\t<name>"+((IconInst) conn.dest_iconOrSchPort).instanceName+"</name>\n");

                //write dest_port's index
                Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
                if (iconsDrawables != null)
                {
                    for (int j = 0; j < iconsDrawables.size(); j++)
                    {
                        Component icon_comp = (Component) iconsDrawables.get(j);

                        if (icon_comp instanceof IconInport)
                        {
                            if(((IconInport)icon_comp).Name!=null && conn.dest_port!=null)
                            {
                                if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name))
                                {
                                    bw.write(prefix+"\t<portIndex>"+j+"</portIndex>\n");
                                    bw.write(prefix+"\t<portName>"+conn.dest_port.Name+"</portName>\n");
                                    break;
                                }
                            }
                        }
                    }
                }//end iconsDrawables!=null
                bw.write(prefix+"</destinationPort>\n");
            } //IconInst
        }
    }
    /**
     * Write Port Destination Connection List
     * @param oos - ObjectOutputStream to write to
     * @param conn - Connection whose port destination to write
     * @throws IOException in case of failure
     */
    public static void writePortDestConnection(ObjectOutputStream oos, Connection conn) throws IOException
    {
        try
        {
            if(conn!=null)
            {
                if (conn.dest_iconOrSchPort == null)
                {
                    oos.writeInt(0); //no destination exists
                    return;
                }
                oos.writeInt(1); //yes destination does exists
                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                {
                    oos.writeInt(1); //type
                    oos.writeUTF(((SchematicOutport) conn.dest_iconOrSchPort).Name);
                    return;
                }
                if (conn.dest_iconOrSchPort instanceof IconInst)
                {
                    oos.writeInt(2); //type
                    oos.writeUTF(((IconInst) conn.dest_iconOrSchPort).instanceName);

                    //write dest_port's index
                    Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
                    if (iconsDrawables != null)
                    {
                        for (int j = 0; j < iconsDrawables.size(); j++)
                        {
                            Component icon_comp = (Component) iconsDrawables.get(j);

                            if (icon_comp instanceof IconInport)
                            {
                                if(((IconInport)icon_comp).Name!=null && conn.dest_port!=null)
                                {
                                    if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name))
                                    {
                                        oos.writeInt(j);
                                        oos.writeUTF(conn.dest_port.Name);
                                        break;
                                    }
                                }
                            }
                        }
                    }//end iconsDrawables!=null
                } //IconInst
            }
        } //end try
        catch (IOException e)
        {
            System.err.println("Error:Schematic:writePortDestConnection: IOException");
            throw new IOException("Schematic writePortDestConnection: IOException");
        }
    } //end writePortDestConnection

//    /**
//     * WriteAllChars format for dup of schematic to the PrintWriter pw.
//     * @param pw - PrintWriter to write to
//     */
    /*public void writeAllChars(PrintWriter pw)
    {
        int compCount = 0;
        pw.print("\n");
        if (drawableObjs != null)
        {
            // count the number of drawableObjs that are not connections
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);
                if (!(comp instanceof Connection))
                    compCount++;
            }
        }
        pw.print("Num Drawable Objs: "); //  write down the num of icons
        pw.print(drawableObjs.size()); //  write down the num of drawableObjs
        pw.print("\n"); //
        pw.print("Num Drawable Components: "); //
        pw.print(compCount); //
        pw.print("\n"); //

        if (drawableObjs != null)
        {
            for (int i = 0; i < drawableObjs.size(); i++)
            {
                pw.print("DrawableObj Num: "); //  write down type of port
                pw.print(i);
                pw.print("\n");
                Component comp = drawableObjs.get(i);

                if (comp instanceof SchematicInport)
                {
                    pw.print("SchInPort: "); //  write down type of port
                    ((SchematicInport) comp).writeAllChars(pw);
                }

                if (comp instanceof SchematicOutport)
                {
                    pw.print("SchOutPort: "); //  write down type of port
                    ((SchematicOutport) comp).writeAllChars(pw);
                }

                if (comp instanceof GraphicPart_text)
                { // nitgupta
                    pw.print("GraphicText: "); //  write down type of drawable
                    ((GraphicPart_text) comp).writeAllChars(pw, 0, 0);
                }

                // IconInst
                if (comp instanceof IconInst)
                {
                    pw.print("IconInst: "); //  write down type of drawable
                    ((IconInst) comp).writeAllChars(pw);
                }

            }
        }
        pw.print('\n');
        pw.print("Num Drawable Connections: "); //  write down type of drawable
        //begin to write Connection
        if (drawableObjs == null)
        {
            pw.print("0");
            pw.print("\n"); //  write down type of connections
        }
        else
        {
            pw.print((drawableObjs.size() - compCount)); //  write down the num of drawableObjs
            pw.print("\n"); //  write down type of connections

            for (int i = 0; i < drawableObjs.size(); i++)
            {
                Component comp = drawableObjs.get(i);
                if (comp instanceof Connection)
                {
                    pw.print("\n");
                    pw.print("Connection Num:");
                    pw.print(i);
                    pw.print("\n");
                    Connection conn = (Connection) comp;
                    conn.writeAllChars(pw);
                    writeAllCharsPortSrcConnection(pw, conn);
                    writeAllCharsPortDestConnection(pw, conn);
                } //end if
            } //end for
        } //end else
    } //end writeAllChars*/

//    /**
//     * WriteAllChars Port Source Connection List
//     * @param pw - PrintWriter to write to
//     * @param conn - Connection whose source port to write
//     */
    /*public static void writeAllCharsPortSrcConnection(PrintWriter pw, Connection conn)
    {
        pw.print("PortSrcConnection num:");
        if (conn.src_iconOrSchPort == null)
        {
            pw.print("0");
            pw.print("\n");
            return;
        }
        pw.print("1");
        pw.print("\n");
        if (conn.src_iconOrSchPort instanceof SchematicInport)
        {
            pw.print("SchInport: ");
            pw.print(((SchematicInport) conn.src_iconOrSchPort).Name);
            pw.print("\n");
        }

        if (conn.src_iconOrSchPort instanceof IconInst)
        {
            pw.print("IconInst: ");
            pw.print(((IconInst) conn.src_iconOrSchPort).instanceName);
            pw.print("\n");
            //write src_port's index
            Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
            if (iconsDrawables != null)
            {
                for (int j = 0; j < iconsDrawables.size(); j++)
                {
                    Component icon_comp = (Component) iconsDrawables.get(j);

                    if (icon_comp instanceof IconOutport)
                    {
                        if (((IconOutport) icon_comp).Name.
                                equals(conn.src_port.Name))
                        {
                            pw.print("pin num: ");
                            pw.print(j); //  write down the num of drawableObjs
                            pw.print(" ");
                            pw.print("conn.src_port.Name:");
                            pw.print(conn.src_port.Name);
                            pw.print("\n");
                            break;
                        }
                    }
                }
            }
        }
    } //end writeAllCharsPortSrcConnection*/

//    /**
//     * WriteAllChars Port Destination Connection List
//     * @param pw - PrintWriter to write to
//     * @param conn - Connection whose destination port to write
//     */
    /*public static void writeAllCharsPortDestConnection(PrintWriter pw, Connection conn)
    {
        pw.print("PortDestConnection num:");

        if (conn.dest_iconOrSchPort == null)
        {
            pw.print("0");
            pw.print("\n");
            return;
        }
        pw.print("1");
        pw.print("\n");
        if (conn.dest_iconOrSchPort instanceof SchematicOutport)
        {
            pw.print("SchOutPort: ");
            pw.print(((SchematicOutport) conn.dest_iconOrSchPort).Name);
            pw.print("\n");
            return;
        }
        if (conn.dest_iconOrSchPort instanceof IconInst)
        {
            pw.print("IconInst: ");
            pw.print(((IconInst) conn.dest_iconOrSchPort).instanceName);
            pw.print("\n");

            //write dest_port's index
            Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
            if (iconsDrawables != null)
            {
                for (int j = 0; j < iconsDrawables.size(); j++)
                {
                    Component icon_comp = (Component) iconsDrawables.get(j);

                    if (icon_comp instanceof IconInport)
                    {
                        if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name))
                        {
                            pw.print("pin num: ");
                            pw.print(j);
                            pw.print(" ");
                            pw.print("conn.dest_port.Name:");
                            pw.print(conn.dest_port.Name);
                            pw.print("\n");
                            break;
                        }
                    }
                }
            } //end if (iconsDrawables)
            pw.print("\n");
        } //end if (conn.des_conOrSchPort)
    } //end writeAllCharsPortDestConnection*/

    public void readXML(org.w3c.dom.Node schematicNode)
    {
        selComponent = null;

        if ((drawableObjs != null) && (drawableObjs.size() != 0))
        {
            drawableObjs.removeAllElements();
        }

        NodeList schematicChildren=schematicNode.getChildNodes();
        for(int i=0; i<schematicChildren.getLength(); i++)
        {
            Node schematicChild=schematicChildren.item(i);
            if(schematicChild.getNodeName().equals("inports"))
            {
                NodeList inportsChildren=schematicChild.getChildNodes();
                for(int j=0; j<inportsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node inportsChild=(org.w3c.dom.Node)inportsChildren.item(j);
                    if(inportsChild.getNodeName().equals("inport"))
                    {
                        SchematicInport sip = new SchematicInport();
                        sip.readXML(inportsChild);  //throws ClassNotFoundException
                        drawableObjs.addElement(sip);
                    }
                }
            }
            else if(schematicChild.getNodeName().equals("outports"))
            {
                NodeList outportsChildren=schematicChild.getChildNodes();
                for(int j=0; j<outportsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node outportsChild=(org.w3c.dom.Node)outportsChildren.item(j);
                    if(outportsChild.getNodeName().equals("outport"))
                    {
                        SchematicOutport sop=new SchematicOutport();
                        sop.readXML(outportsChild);
                        drawableObjs.addElement(sop);
                    }
                }
            }
            else if(schematicChild.getNodeName().equals("textObjects"))
            {
                NodeList textChildren=schematicChild.getChildNodes();
                for(int j=0; j<textChildren.getLength(); j++)
                {
                    org.w3c.dom.Node textChild=(org.w3c.dom.Node)textChildren.item(j);
                    if(textChild.getNodeName().equals("part") &&
                            textChild.getAttributes().getNamedItem("type").getNodeValue().equals("text"))
                    {
                        GraphicPart_text text=new GraphicPart_text();
                        text.readXML(textChild);
                        drawableObjs.addElement(text);
                    }
                }
            }
            else if(schematicChild.getNodeName().equals("methodIcons"))
            {
                NodeList methodChildren=schematicChild.getChildNodes();
                for(int j=0; j<methodChildren.getLength(); j++)
                {
                    org.w3c.dom.Node methodChild=(org.w3c.dom.Node)methodChildren.item(j);
                    if(methodChild.getNodeName().equals("method"))
                    {
                        IconMethod method=new IconMethod();
                        method.readXML(methodChild);
                        drawableObjs.addElement(method);
                    }
                }
                methodsSet = true;
            }
            else if(schematicChild.getNodeName().equals("icons"))
            {
                NodeList iconsChildren=schematicChild.getChildNodes();
                for(int j=0; j<iconsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node iconsChild=(org.w3c.dom.Node)iconsChildren.item(j);
                    if(iconsChild.getNodeName().equals("iconInstance"))
                    {
                        IconInst icon=new IconInst();
                        icon.readXML(this, iconsChild);
                        drawableObjs.addElement(icon);
                    }
                }
            }
            else if(schematicChild.getNodeName().equals("connections"))
            {
                NodeList connectionsChildren=schematicChild.getChildNodes();
                for(int j=0; j<connectionsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node connectionsChild=(org.w3c.dom.Node)connectionsChildren.item(j);
                    if(connectionsChild.getNodeName().equals("connection"))
                    {
                        Connection conn=new Connection();
                        conn.readXML(connectionsChild);
                        boolean sourcePort=false, destPort=false;
                        NodeList connectionChildren=connectionsChild.getChildNodes();
                        for(int k=0; k<connectionChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node connectionChild=(org.w3c.dom.Node)connectionChildren.item(k);
                            if(connectionChild.getNodeName().equals("sourcePort"))
                                sourcePort=readPortSrcConnectionXml(connectionChild, conn);
                            else if(connectionChild.getNodeName().equals("destinationPort"))
                                destPort=readPortDestConnectionXml(connectionChild, conn);
                        }
                        if(sourcePort && destPort)
                        {
                            drawableObjs.add(conn);
                        }
                    }
                }
            }
        }
    }

    /**
     * Read this schematic from the input stream oos.
     * @param ois - ObjectInputStream to read from
     * @throws IOException - if an IO error occurred
     * @throws ClassNotFoundException - if a class-not-found error occurred
     */
    public void read(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        try
        {
            selComponent = null;

            if ((drawableObjs != null) && (drawableObjs.size() != 0))
            {
                drawableObjs.removeAllElements();
            }

            // DRAWABLES
            int numDrawables = ois.readInt();

            for (int i = 0; i < numDrawables; i++)
            {
                int typeOfObj = ois.readInt();

                switch (typeOfObj)
                {
                    case 1: //SchematicInport
                        SchematicInport sip = new SchematicInport();
                        sip.read(ois);  //throws ClassNotFoundException
                        drawableObjs.addElement(sip);
                        break;
                    case 2: //SchematicOutport
                        SchematicOutport sop = new SchematicOutport();
                        sop.read(ois);
                        drawableObjs.addElement(sop);
                        break;
                    case 4: // text string
                        GraphicPart_text text = new GraphicPart_text();
                        text.read(ois);
                        drawableObjs.addElement(text);
                        break;
                    case 3: //graphics.IconInst -
                        IconInst iconInst = new IconInst();
                        iconInst.read(ois);  //should catch exceptions
                        drawableObjs.addElement(iconInst);
                        break;
                    default: //error
                        System.err.println("Error:Schematic:read unknown type of object");
                        break;
                }//end switch
            }

            //begin to read Connections
            int numConnections = ois.readInt();
            for (int ic = 0; ic < numConnections; ic++)
            {
                Connection conn = new Connection();
                conn.read(ois);
                //todo: eventually want to have multiple destinations on a connection.
                //todo: Right now you have to start over from the src and lay the
                //todo: interconnect on top of another interconnect.
                if(readPortSrcConnection(ois, conn) && readPortDestConnection(ois, conn))
                {
                    drawableObjs.addElement(conn);
                }
            }
        } //end try
        catch (IOException e)
        {
            System.err.println("Error:Schematic:read: IOException");
            throw new IOException("Schematic read IOException");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:Schematic:Read: ClassNotFoundException");
            throw new ClassNotFoundException("Schematic read ClassNotFoundException");
        }
    } //end read

    public boolean readPortSrcConnectionXml(org.w3c.dom.Node node, Connection conn)
    {
        String type=node.getAttributes().getNamedItem("type").getNodeValue();
        if(type.equals("inport"))
        {
            NodeList children=node.getChildNodes();
            for(int i=0; i<children.getLength(); i++)
            {
                org.w3c.dom.Node child=(org.w3c.dom.Node)children.item(i);
                if(child.getNodeName().equals("name"))
                {
                    String name= SCSUtility.getNodeValue(child);
                    for (int j = 0; j < drawableObjs.size(); j++)
                    {
                        Component comp = drawableObjs.get(j);
                        if (comp instanceof SchematicInport)
                        {
                            SchematicInport sip = (SchematicInport) comp;
                            if (sip.Name.equals(name))
                            {
                                conn.src_iconOrSchPort = sip;
                                conn.src_port = null;
                                sip.links.addElement(conn);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        else if(type.equals("icon"))
        {
            String name="", portName="";
            int portIdx=-1;
            NodeList children=node.getChildNodes();
            for(int i=0; i<children.getLength(); i++)
            {
                org.w3c.dom.Node child=(org.w3c.dom.Node)children.item(i);
                if(child.getNodeName().equals("name"))
                    name= SCSUtility.getNodeValue(child);
                else if(child.getNodeName().equals("portName"))
                    portName=SCSUtility.getNodeValue(child);
                else if(child.getNodeName().equals("portIndex"))
                    portIdx=Integer.parseInt(SCSUtility.getNodeValue(child));
            }
            int foundIconInst = -1;
            for (int j = 0; j < drawableObjs.size(); j++)
            {
                Component comp = drawableObjs.get(j);
                if (comp instanceof IconInst)
                {
                    IconInst iconInst = (IconInst) comp;
                    if (iconInst.instanceName.equals(name))
                    {
                        foundIconInst = j;
                        conn.src_iconOrSchPort = iconInst;
                        if(portIdx>-1 && portIdx<iconInst.drawableParts.size())
                        {
                            Component realPin = iconInst.drawableParts.get(portIdx);
                            //should be connecting to IconOutport
                            IconOutport realOutPin;
                            if (realPin instanceof IconOutport)
                            {
                                realOutPin = (IconOutport) realPin;
                                //check if name the same as before
                                if (portName.equals(realOutPin.Name))
                                {
                                    fixConnSrc(conn, realOutPin);
                                    conn.src_port = realOutPin;
                                    realOutPin.links.addElement(conn);
                                    break;
                                }
                                else if (frozenIconOutport(conn, realOutPin))
                                {
                                    //assumes pin has been renamed
                                    fixConnSrc(conn, realOutPin); //not needed
                                    conn.src_port = realOutPin;
                                    realOutPin.links.addElement(conn);
                                    break;
                                }
                                //IconInport but pin moved
                                else
                                {
                                    //find by Name as second option
                                    findAndFixIconOutport(conn, portName, iconInst);
                                    break;
                                }
                            } //end if IconOutport
                            else
                            {
                                //find by Name as second option
                                findAndFixIconOutport(conn, portName, iconInst);
                                break;
                            }
                        }
                        else
                        {
                            //find by Name as second option
                            findAndFixIconOutport(conn, portName, iconInst);
                            break;
                        }
                    } //instance name matches
                }//iconinst
            } //end goThruDrawableObjs
            if (foundIconInst == -1)
            {
                System.err.println("Error:Schematic:readPortSrcConnection: cannot find: " + name + " in instance list.");
                return false;
            }
        }
        return true;
    }

    /**
     * readPortSrcConnection
     * There is only one input source (SchematicInport or IconOutport) per connection.
     * @param ois - Object input stream to read from
     * @param conn - Connection whose source port to read
     * @throws IOException - if an IO error occurs
     * @return whether or not successful in reading the port
     */
    public boolean readPortSrcConnection(ObjectInputStream ois, Connection conn) throws IOException
    {
        String name;

        //read source IconInst and port information
        int exist = ois.readInt();
        if (exist == 0)
        {
            conn.src_iconOrSchPort = null;
            conn.src_port = null;
            return false;
        }
        if (exist == 1)
        {
            int typeOfConnector = ois.readInt(); //1=SchematicInport 2=graphics.IconInst
            switch (typeOfConnector)
            {
                case 1:  //SchematicInport
                    name = ois.readUTF();
                    for (int j = 0; j < drawableObjs.size(); j++)
                    {
                        Component comp = drawableObjs.get(j);
                        if (comp instanceof SchematicInport)
                        {
                            SchematicInport sip = (SchematicInport) comp;
                            if (sip.Name.equals(name))
                            {
                                conn.src_iconOrSchPort = sip;
                                conn.src_port = null;
                                sip.links.addElement(conn);
                                break;
                            }
                        }
                    }
                    break;
                case 2:  //IconInst
                    name = ois.readUTF();
                    int foundIconInst = -1;
                    for (int j = 0; j < drawableObjs.size(); j++)
                    {
                        Component comp = drawableObjs.get(j);
                        if (comp instanceof IconInst)
                        {
                            IconInst iconInst = (IconInst) comp;
                            if (iconInst.instanceName.equals(name))
                            {
                                foundIconInst = j;
                                conn.src_iconOrSchPort = iconInst;
                                int connPinIndex = ois.readInt();
                                String connPinName = ois.readUTF();
                                if(connPinIndex>-1 && connPinIndex<iconInst.drawableParts.size())
                                {
                                    Component realPin = iconInst.drawableParts.get(connPinIndex);
                                    //should be connecting to IconOutport
                                    IconOutport realOutPin;
                                    if (realPin instanceof IconOutport)
                                    {
                                        realOutPin = (IconOutport) realPin;
                                        //check if name the same as before
                                        if (connPinName.equals(realOutPin.Name))
                                        {
                                            fixConnSrc(conn, realOutPin);
                                            conn.src_port = realOutPin;
                                            realOutPin.links.addElement(conn);
                                            break;
                                        }
                                        else if (frozenIconOutport(conn, realOutPin))
                                        {
                                            //assumes pin has been renamed
                                            fixConnSrc(conn, realOutPin); //not needed
                                            conn.src_port = realOutPin;
                                            realOutPin.links.addElement(conn);
                                            break;
                                        }
                                        //IconInport but pin moved
                                        else
                                        {
                                            //find by Name as second option
                                            findAndFixIconOutport(conn, connPinName, iconInst);
                                            break;
                                        }
                                    } //end if IconOutport
                                    else
                                    {
                                        //find by Name as second option
                                        findAndFixIconOutport(conn, connPinName, iconInst);
                                        break;
                                    }
                                }
                                else
                                {
                                    //find by Name as second option
                                    findAndFixIconOutport(conn, connPinName, iconInst);
                                    break;
                                }
                            } //instance name matches
                        }//iconinst
                    } //end goThruDrawableObjs
                    if (foundIconInst == -1)
                    {
                        return false;
                    }
                    break; //case 2
                default:
                    return false;
            }//end switch
        }
        return true;
    }//end readPortSrcConnection

    public boolean readPortDestConnectionXml(org.w3c.dom.Node node, Connection conn)
    {
        String type=node.getAttributes().getNamedItem("type").getNodeValue();
        if(type.equals("outport"))
        {
            NodeList children=node.getChildNodes();
            for(int i=0; i<children.getLength(); i++)
            {
                org.w3c.dom.Node child=(org.w3c.dom.Node)children.item(i);
                if(child.getNodeName().equals("name"))
                {
                    String name= SCSUtility.getNodeValue(child);
                    for (int j = 0; j < drawableObjs.size(); j++)
                    {
                        Component comp = drawableObjs.get(j);
                        if (comp instanceof SchematicOutport)
                        {
                            SchematicOutport sop = (SchematicOutport) comp;
                            if (sop.Name.equals(name))
                            {
                                conn.dest_iconOrSchPort = sop;
                                conn.dest_port = null;
                                sop.link = conn;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        else if(type.equals("icon"))
        {
            String name="", portName="";
            int portIdx=-1;
            NodeList children=node.getChildNodes();
            for(int i=0; i<children.getLength(); i++)
            {
                org.w3c.dom.Node child=(org.w3c.dom.Node)children.item(i);
                if(child.getNodeName().equals("name"))
                    name= SCSUtility.getNodeValue(child);
                else if(child.getNodeName().equals("portName"))
                    portName=SCSUtility.getNodeValue(child);
                else if(child.getNodeName().equals("portIndex"))
                    portIdx=Integer.parseInt(SCSUtility.getNodeValue(child));
            }
            int foundIconInst = -1;
            for (int j = 0; j < drawableObjs.size(); j++)
            {
                Component comp = drawableObjs.get(j);
                if (comp instanceof IconInst)
                {
                    IconInst iconInst = (IconInst) comp;
                    if (iconInst.instanceName.equals(name))
                    {
                        foundIconInst = j;
                        conn.dest_iconOrSchPort = iconInst;
                        if(portIdx>-1 && portIdx<iconInst.drawableParts.size())
                        {
                            Component realPin = iconInst.drawableParts.get(portIdx);
                            IconInport realInPin;
                            if (realPin instanceof IconInport)
                            {
                                realInPin = (IconInport) realPin;
                                //check if name the same as before
                                if (portName.equals(realInPin.Name))
                                {
                                    fixConnDest(conn, realInPin);
                                    conn.dest_port = realInPin;
                                    conn.dest_port.link = conn;
                                    break;
                                }
                                //if pin did not move, then assume pin has been renamed
                                else if (frozenIconInport(conn, realInPin))
                                {
                                    fixConnDest(conn, realInPin); //dont need here
                                    conn.dest_port = realInPin;
                                    conn.dest_port.link = conn;
                                    break;
                                }
                                //IconInport but not correct pin
                                else
                                {
                                    //find by Name as second option
                                    findAndFixIconInport(conn, portName, iconInst);
                                    break;
                                }
                            }
                            //else not IconInport thus not right pin
                            else
                            {
                                findAndFixIconInport(conn, portName, iconInst);
                                break;
                            }
                        }
                    }
                } //if iconInst
            }//end goThruDrawableObjs2
            if (foundIconInst == -1)
            {
                System.err.println("Error:Schematic:readPortDestConnection: cannot find: " + name + " in instance list.");
                return false;
            }
        }
        return true;
    }

    /**
     * readPortDestConnection
     * @param ois - ObjectInputStream to read from
     * @param conn - connection whose destination port to read
     * @return whether or not successful in reading the destination port
     * @throws IOException - when an I/O error occurs
     */
    public boolean readPortDestConnection(ObjectInputStream ois, Connection conn) throws IOException
    {
        //read destination IconInst and port information
        int exist = ois.readInt();
        if (exist == 0)
        {
            conn.dest_iconOrSchPort = null;
            conn.dest_port = null;
            return false;
        }
        if (exist == 1)
        {
            String name;
            int typeOfConnector2 = ois.readInt();
            switch (typeOfConnector2)
            {
                case 1:  //SchematicOutport
                    try
                    {
                        name = ois.readUTF();
                        for (int j = 0; j < drawableObjs.size(); j++)
                        {
                            Component comp = drawableObjs.get(j);
                            if (comp instanceof SchematicOutport)
                            {
                                SchematicOutport sop = (SchematicOutport) comp;
                                if (sop.Name.equals(name))
                                {
                                    conn.dest_iconOrSchPort = sop;
                                    conn.dest_port = null;
                                    sop.link = conn;
                                    break;
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println("Error:Schematic:readPortDestConnection");
                        return false;
                    }
                    break;

                case 2:  //IconInst
                    try
                    {
                        name = ois.readUTF();
                        int foundIconInst = -1;
                        for (int j = 0; j < drawableObjs.size(); j++)
                        {
                            Component comp = drawableObjs.get(j);
                            if (comp instanceof IconInst)
                            {
                                IconInst iconInst = (IconInst) comp;
                                if (iconInst.instanceName.equals(name))
                                {
                                    foundIconInst = j;
                                    conn.dest_iconOrSchPort = iconInst;
                                    int connPinIndex = ois.readInt();
                                    String connPinName = ois.readUTF();
                                    if(connPinIndex>-1 && connPinIndex<iconInst.drawableParts.size())
                                    {
                                        Component realPin = iconInst.drawableParts.get(connPinIndex);
                                        IconInport realInPin;
                                        if (realPin instanceof IconInport)
                                        {
                                            realInPin = (IconInport) realPin;
                                            //check if name the same as before
                                            if (connPinName.equals(realInPin.Name))
                                            {
                                                fixConnDest(conn, realInPin);
                                                conn.dest_port = realInPin;
                                                conn.dest_port.link = conn;
                                                break;
                                            }
                                            //if pin did not move, then assume pin has been renamed
                                            else if (frozenIconInport(conn, realInPin))
                                            {
                                                fixConnDest(conn, realInPin); //dont need here
                                                conn.dest_port = realInPin;
                                                conn.dest_port.link = conn;
                                                break;
                                            }
                                            //IconInport but not correct pin
                                            else
                                            {
                                                //find by Name as second option
                                                findAndFixIconInport(conn, connPinName, iconInst);
                                                break;
                                            }
                                        }
                                        //else not IconInport thus not right pin
                                        else
                                        {
                                            findAndFixIconInport(conn, connPinName, iconInst);
                                            break;
                                        }
                                    }
                                }
                            } //if iconInst
                        }//end goThruDrawableObjs2
                        if (foundIconInst == -1)
                        {
                            System.err.println("Error:Schematic:readPortDestConnection: cannot find: " + name + " in instance list.");
                            return false;
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println("Error:Schematic:readPortDestConnection");
                        return false;
                    }
                    break; //end case 2
                default:
                    System.err.println("Error:Schematic:readPortDestConnection: unknown type of connection on interconnect.");
                    return false;
            }//end switch
        } //end if (exist==1)
        return true;
    }//end readPortDestConnection

    //---------------------------------------------
    public static boolean frozenIconInport(Connection conn, IconInport realPin)
    {
        //did the pin move?  yes=not frozen no=frozen
        boolean frozen = true;
        int x = conn.x[conn.numVerticies - 1];
        int y = conn.y[conn.numVerticies - 1];
        if ((x != realPin.x0) || (y != realPin.y0))
        {
            frozen = false;
        }
        return (frozen);
    }

    //---------------------------------------------
    public static boolean frozenIconOutport(Connection conn, IconOutport realPin)
    {
        //did the pin move?  yes=not frozen no=frozen
        boolean frozen = true;
        int x = conn.x[0];
        int y = conn.y[0];
        if ((x != realPin.x1) || (y != realPin.y1))
        {
            frozen = false;
        }
        return (frozen);
    }

    //---------------------------------------------
    public static boolean fixConnSrc(Connection conn, IconOutport realPin)
    {
        boolean frozen = frozenIconOutport(conn, realPin);
        //if pin moved, then move interconnect
        if (! frozen)
        {
            conn.moveSrcAbsolute(realPin.x1, realPin.y1);
        }
        return (!frozen);
    }

    //---------------------------------------------
    public static boolean fixConnDest(Connection conn, IconInport realPin)
    {
        boolean frozen = frozenIconInport(conn, realPin);
        //if pin moved, then move interconnect
        if (! frozen)
        {
            conn.moveDestAbsolute(realPin.x0, realPin.y0);
        }
        return (!frozen);
    }

    //---------------------------------------------
    public static boolean findAndFixIconInport(Connection conn, String connPinName, IconInst iconInst)
    {
        boolean found;
        int foundi = iconInst.findInport(connPinName);
        if (foundi >= 0)
        {
            IconInport realInPin = (IconInport) iconInst.drawableParts.get(foundi);
            fixConnDest(conn, realInPin);
            conn.dest_port = realInPin;
            conn.dest_port.link = conn;//only one
            found = true;
        }
        //error conn not pointing to an IconInport and cannot find pin elsewhere on iconinst
        else
        {
            //todo: call IconInst.selectPort to find a IconInport near by the end of the interconnect and connect to it.
            conn.dest_iconOrSchPort = null;
            conn.dest_port = null;

            found = false;
        }
        return (found);
    }

    //---------------------------------------------
    public static boolean findAndFixIconOutport(Connection conn, String connPinName, IconInst iconInst)
    {
        boolean found;
        int foundi = iconInst.findOutport(connPinName);
        if (foundi >= 0)
        {
            IconOutport realOutPin = (IconOutport) iconInst.drawableParts.get(foundi);
            fixConnSrc(conn, realOutPin);
            conn.src_port = realOutPin;
            conn.src_port.links.addElement(conn); //more than one
            found = true;
        }
        //error conn not pointing to an IconOutport and cannot find pin elsewhere on iconinst
        else
        {
            //todo: call IconInst.selectPort to find a IconOutport near by the end of the interconnect and connect to it.
            conn.src_iconOrSchPort = null;
            conn.src_port = null;
            found = false;
        }
        return (found);
    }

    //---------------------------------------------
    /**
     * writeNslm from these connections
     *
     * @param    pw PrintWriter
     */
    public void writeNslm(PrintWriter pw)
    {
        Connection conn;

        if (drawableObjs == null)
        {
            return;
        }
        if (drawableObjs.size() == 0)
        {
            return;
        }
        pw.print("\n");

        // go thru drawables, when you find either a SchPort or a icon
        // then go thru its output ports and write the connections
        // note: It looks like NSL only allows one src and one dest per nslConnect
        // or nslRelabel statement currently.

        for (int i = 0; i < drawableObjs.size(); i++)
        {
            StringBuilder tempstr;
            Component tempComponent = drawableObjs.get(i);

            // SchematicInport
            if (tempComponent instanceof SchematicInport)
            {
                SchematicInport sip = (SchematicInport) tempComponent;
                //for each link
                for (int j = 0; j < sip.links.size(); j++)
                {
                    conn = sip.links.get(j);
                    if (conn.dest_iconOrSchPort != null)
                    {
                        if (conn.dest_iconOrSchPort instanceof IconInst)
                        {
                            tempstr=new StringBuilder("    nslRelabel(");
                            tempstr.append(sip.Name);
                            tempstr.append(',');
                            tempstr.append(((IconInst) conn.dest_iconOrSchPort).instanceName);
                            tempstr.append('.');
                            tempstr.append(conn.dest_port.Name);
                            tempstr.append(");\n");
                            pw.print(tempstr.toString());
                        }
                        //run thru
                        if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                        {
                            tempstr=new StringBuilder("    nslRelabel(");
                            tempstr.append(sip.Name);
                            tempstr.append(',');
                            tempstr.append(((SchematicOutport) conn.dest_iconOrSchPort).Name);
                            tempstr.append(");\n");
                            pw.print(tempstr);
                        }
                    } //end if (conn.dest_iconOrSchPort!=null)
                } //end for sip.links.size()
                // graphics.IconInst output pins only
            }
            else if (tempComponent instanceof IconInst)
            {
                String instanceName = ((IconInst) tempComponent).instanceName;
                Vector iconsDrawables = ((IconInst) tempComponent).drawableParts;
                if (iconsDrawables == null)
                {
                    break; //this cannot happen
                }
                for (int k = 0; k < iconsDrawables.size(); k++)
                {
                    GraphicPart gobj = (GraphicPart) iconsDrawables.get(k);
                    if (gobj instanceof IconOutport)
                    { //for each output port
                        IconOutport op = (IconOutport) gobj;
                        for (int m = 0; m < op.links.size(); m++)
                        { //for each link
                            conn = op.links.get(m);
                            if (conn.dest_iconOrSchPort != null)
                            {
                                if (conn.dest_iconOrSchPort instanceof IconInst && conn.dest_port!=null)
                                {
                                    tempstr=new StringBuilder("    nslConnect(");
                                    tempstr.append(instanceName);
                                    tempstr.append('.');
                                    tempstr.append(op.Name);
                                    tempstr.append(',');
                                    tempstr.append(((IconInst) conn.dest_iconOrSchPort).instanceName);
                                    tempstr.append('.');
                                    tempstr.append(conn.dest_port.Name);
                                    tempstr.append(");\n");
                                    pw.print(tempstr.toString());
                                }
                                if (conn.dest_iconOrSchPort instanceof SchematicOutport)
                                {
                                    tempstr=new StringBuilder("    nslConnect(");
                                    tempstr.append(instanceName);
                                    tempstr.append('.');
                                    tempstr.append(op.Name);
                                    tempstr.append(',');
                                    tempstr.append(((SchematicOutport)conn.dest_iconOrSchPort).Name);
                                    tempstr.append(");\n");
                                    pw.print(makeRelabel(tempstr.toString()));
                                }
                            }
                        }//end for op.links.size()
                    }//end if instanceof IconOutport
                } //end for iconsDrawables.size()
            }//end if instanceof IconInst
        } //end for (int i=0; i<drawableObjs.size(); i++)
    }//end writeNslm

    public void refresh()
    {
        for (int i = 0; i < drawableObjs.size(); i++)
        {
            Component tempComponent = drawableObjs.get(i);
            if(tempComponent instanceof IconInst)
            {
                ((IconInst)tempComponent).refresh();
            }
        }
        refreshConnections();
    }

    protected void refreshConnections()
    {
        for(int i=0; i<drawableObjs.size(); i++)
        {
            if(drawableObjs.get(i) instanceof Connection)
            {
                Connection conn=(Connection)drawableObjs.get(i);
                refreshPortSrc(conn);
                refreshPortDest(conn);
            }
        }
    }

    protected boolean refreshPortSrc(Connection conn)
    {
        if(conn!=null)
        {
            if(conn.src_iconOrSchPort instanceof SchematicInport)
            {
                for (int j = 0; j < drawableObjs.size(); j++)
                {
                    Component comp = drawableObjs.get(j);
                    if (comp instanceof SchematicInport)
                    {
                        SchematicInport sip = (SchematicInport) comp;
                        if (sip.Name.equals(((SchematicInport)conn.src_iconOrSchPort).Name))
                        {
                            conn.src_iconOrSchPort = sip;
                            conn.src_port = null;
                            break;
                        }
                    }
                }
            }
            else if(conn.src_iconOrSchPort instanceof IconInst)
            {
                int foundIconInst = -1;
                for (int j = 0; j < drawableObjs.size(); j++)
                {
                    Component comp = drawableObjs.get(j);
                    if (comp instanceof IconInst)
                    {
                        IconInst iconInst = (IconInst) comp;
                        if (iconInst.instanceName.equals(((IconInst)conn.src_iconOrSchPort).instanceName))
                        {
                            foundIconInst = j;
                            conn.src_iconOrSchPort = iconInst;
                            int connPinIndex = -1;
                            String connPinName="";
                            //write dest_port's index
                            Vector iconsDrawables = ((IconInst) conn.src_iconOrSchPort).drawableParts;
                            if (iconsDrawables != null)
                            {
                                for (int k = 0; k < iconsDrawables.size(); k++)
                                {
                                    Component icon_comp = (Component) iconsDrawables.get(k);

                                    if (icon_comp instanceof IconOutport)
                                    {
                                        if(((IconOutport)icon_comp).Name!=null && conn.src_port!=null)
                                        {
                                            if (((IconOutport) icon_comp).Name.equals(conn.src_port.Name))
                                            {
                                                connPinIndex=k;
                                                connPinName=conn.src_port.Name;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if(connPinIndex>-1 && connPinIndex<iconInst.drawableParts.size())
                            {
                                Component realPin = iconInst.drawableParts.get(connPinIndex);
                                //should be connecting to IconOutport
                                IconOutport realOutPin;
                                if (realPin instanceof IconOutport)
                                {
                                    realOutPin = (IconOutport) realPin;
                                    //check if name the same as before
                                    if (connPinName.equals(realOutPin.Name))
                                    {
                                        fixConnSrc(conn, realOutPin);
                                        conn.src_port = realOutPin;
                                        realOutPin.links.addElement(conn);
                                        break;
                                    }
                                    else if (frozenIconOutport(conn, realOutPin))
                                    {
                                        //assumes pin has been renamed
                                        fixConnSrc(conn, realOutPin); //not needed
                                        conn.src_port = realOutPin;
                                        realOutPin.links.addElement(conn);
                                        break;
                                    }
                                    //IconInport but pin moved
                                    else
                                    {
                                        //find by Name as second option
                                        findAndFixIconOutport(conn, connPinName, iconInst);
                                        break;
                                    }
                                } //end if IconOutport
                                else
                                {
                                    //find by Name as second option
                                    findAndFixIconOutport(conn, connPinName, iconInst);
                                    break;
                                }
                            }
                        } //instance name matches
                    }//iconinst
                } //end goThruDrawableObjs
                if (foundIconInst == -1)
                {
                    return false;
                }
            }
            else
            {
                    System.err.println("Error:Schematic:readPortSrcConnection: unknown type of connection on interconnect.");
                    return false;
            }
        }
        else
            return false;
        return true;
    }

    protected boolean refreshPortDest(Connection conn)
    {
        if(conn!=null)
        {
            if(conn.dest_iconOrSchPort instanceof SchematicOutport)
            {
                for (int j = 0; j < drawableObjs.size(); j++)
                {
                    Component comp = drawableObjs.get(j);
                    if (comp instanceof SchematicOutport)
                    {
                        SchematicOutport sop = (SchematicOutport) comp;
                        if (sop.Name.equals(((SchematicOutport)conn.dest_iconOrSchPort).Name))
                        {
                            conn.dest_iconOrSchPort = sop;
                            conn.dest_port = null;
                            sop.link=conn;
                            break;
                        }
                    }
                }
            }
            else if(conn.dest_iconOrSchPort instanceof IconInst)
            {
                int foundIconInst = -1;
                for (int j = 0; j < drawableObjs.size(); j++)
                {
                    Component comp = drawableObjs.get(j);
                    if (comp instanceof IconInst)
                    {
                        IconInst iconInst = (IconInst) comp;
                        if (iconInst.instanceName.equals(((IconInst)conn.dest_iconOrSchPort).instanceName))
                        {
                            foundIconInst = j;
                            conn.dest_iconOrSchPort = iconInst;
                            int connPinIndex = -1;
                            String connPinName="";
                            //write dest_port's index
                            Vector iconsDrawables = ((IconInst) conn.dest_iconOrSchPort).drawableParts;
                            if (iconsDrawables != null)
                            {
                                for (int k = 0; k < iconsDrawables.size(); k++)
                                {
                                    Component icon_comp = (Component) iconsDrawables.get(k);

                                    if (icon_comp instanceof IconInport)
                                    {
                                        if(((IconInport)icon_comp).Name!=null && conn.dest_port!=null)
                                        {
                                            if (((IconInport) icon_comp).Name.equals(conn.dest_port.Name))
                                            {
                                                connPinIndex=k;
                                                connPinName=conn.dest_port.Name;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if(connPinIndex>-1 && connPinIndex<iconInst.drawableParts.size())
                            {
                                Component realPin = iconInst.drawableParts.get(connPinIndex);
                                IconInport realInPin;
                                if (realPin instanceof IconInport)
                                {
                                    realInPin = (IconInport) realPin;
                                    //check if name the same as before
                                    if (connPinName.equals(realInPin.Name))
                                    {
                                        fixConnDest(conn, realInPin);
                                        conn.dest_port = realInPin;
                                        conn.dest_port.link = conn;
                                        break;
                                    }
                                    //if pin did not move, then assume pin has been renamed
                                    else if (frozenIconInport(conn, realInPin))
                                    {
                                        fixConnDest(conn, realInPin); //dont need here
                                        conn.dest_port = realInPin;
                                        conn.dest_port.link = conn;
                                        break;
                                    }
                                    //IconInport but not correct pin
                                    else
                                    {
                                        //find by Name as second option
                                        findAndFixIconInport(conn, connPinName, iconInst);
                                        break;
                                    }
                                }
                                //else not IconInport thus not right pin
                                else
                                {
                                    findAndFixIconInport(conn, connPinName, iconInst);
                                    break;
                                }
                            }
                        } //if iconInst
                    }//end goThruDrawableObjs2
                }
                if (foundIconInst == -1)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
            return false;
        return true;
    }

    //-------------------------------------------------------
    static String makeRelabel(String tempstr)
    {
        return "    nslRelabel" + tempstr.substring(14);
    }
    //------------------------------------------------------------------
    
    public boolean getMethodsSet()
    {
    	return methodsSet;
    }
    
} //end Class Schematic
