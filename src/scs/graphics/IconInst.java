package scs.graphics;/* SCCS  %W% --- %G% -- %U% */

// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconInst - A class representing the graphical appearance of a module in
 * schematic. It inherits the Icon class because of the special case where
 * we need instances of pins for the links to the connections in the model.
 *
 * If creating scheamtics that contain a lot of the same icon, and one
 * wanted to be very efficient, one could have the IconInst class
 * not inherit the Icon class but instead have a list of icons as
 * drawing templates for the iconinstances and the pins on the icons would
 * contain links to an object that contained both an index to the instance of the
 * icon and a link to the connection. You must draw using offset though.
 *
 *
 * @author Alexander
 * @version     %I%, %G%
 *
 * @since JDK1.2
 */

import scs.Module;
import scs.util.SCSUtility;
import scs.util.UserPref;

import java.awt.*;
import java.io.*;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class IconInst extends scs.graphics.Icon
{
    static int firstSpotForIcon = SCSUtility.gridT2;

    public String instanceName = "";
    public String parameters = "";

    GraphicPart_rect selIconBody = null;
    public GraphicPart selPort = null;
    GraphicPart_text instanceLabel = null;

    /**
     * Constructor of this class with no parameters.
     */
    public IconInst()
    {
        super();
        instanceName = "";
        selIconBody = null;
        instanceLabel = null;
        selPort = null;
        xmin = ymin = 0;  //where the icon plus ports is located on a icon canvas
        xmax = ymax = 0;  //
    }

    /**
     * Constructor of this class with instanceName and an Icon.
     *
     * @param instanceName             string contain the name of this
     *                                 instance of an icon
     * @param iconTemplate             Icon
     * @param parameters               String
     * @since JDK1.2
     */
    public IconInst(String instanceName, scs.graphics.Icon iconTemplate, String parameters)
    {
        super(iconTemplate);
        this.instanceName = instanceName;

        selIconBody = null;
        selPort = null;
        //where the icon plus ports is located on a schematic canvas
        xmin = iconTemplate.xmin; //iconTemplate should be 0
        ymin = iconTemplate.ymin; //iconTemplate should be 0
        xmax = iconTemplate.xmax; //iconTemplate should be delta
        ymax = iconTemplate.ymax; //iconTemplate should be delta
        shapexmin = iconTemplate.shapexmin; //iconTemplate should be 0
        shapeymin = iconTemplate.shapeymin; //iconTemplate should be 0
        shapexmax = iconTemplate.shapexmax; //iconTemplate should be delta
        shapeymax = iconTemplate.shapeymax; //iconTemplate should be delta

        //note: moduleLabel and instanceLabel are not part of the drawable objects since
        //their location is dependent on the final shape of the icon itself.  If they
        // were included, the definition of where to place the text would be recursive.
        instanceLabel = new GraphicPart_text(UserPref.instanceTextLocation, shapexmin, shapeymin, shapexmax, shapeymax,
                                             instanceName, UserPref.instanceTextFont, UserPref.instanceText_col,
                                             UserPref.instanceTextSize);

        this.parameters = parameters;
    }

    /**
     * Paint this icon.
     */
    public void paint(Graphics g)
    {
        int ix;
        GraphicPart tempPart;

        //todo: need to get font size and color from options
        if (drawableParts == null)
        {
            return;
        }
        int max = drawableParts.size();
        for (ix = 0; ix < max; ix++)
        {
            tempPart = (GraphicPart) drawableParts.elementAt(ix);
            tempPart.paint(g); // aa 00/05/05
        }
        //if selIconBody: draw selection box.
        if (selIconBody != null)
        {
            // selIconBody should have the right coordinates
            selIconBody.paintOpen(g);// move must update the selection
        }
        if (instanceLabel != null)
        {
            instanceLabel.paint(g);
        }
        if (moduleLabel != null)
        {
            moduleLabel.paint(g);
        }
        IconPort tempPort;
        for (ix = 0; ix < max; ix++)
        {
            if(drawableParts.elementAt(ix) instanceof IconPort)
            {
                tempPort = (IconPort) drawableParts.elementAt(ix);
                if(tempPort.select!=0)
                    tempPort.paintLabel(g);
            }
        }
    }

    /**
     * Move this icon as a whole by x offset in x-direction and y offset in
     * y-direction.
     *
     * @param x the moving offset in x-direction
     * @param y the moving offset in y-direction
     */
    public void moveobj(int x, int y)
    {
        GraphicPart part;
        Connection conn;

        xmin = xmin + x;
        ymin = ymin + y;
        xmax = xmax + x;
        ymax = ymax + y;
        shapexmin = shapexmin + x;
        shapeymin = shapeymin + y;
        shapexmax = shapexmax + x;
        shapeymax = shapeymax + y;

        // note: we do not move the drawableParts since they are part of the Icon
        // and their location is calculated each time we paint.

        //move bounding box indicating the object is selected
        if (selIconBody != null)
        {
            selIconBody.moveobj(x, y);
        }

        // move text label of instance selected
        if (instanceLabel != null)
        {
            instanceLabel.moveobj(x, y);
        }
        //todo: figure out how paint gets called after move
        if (moduleLabel != null)
        {
            moduleLabel.moveobj(x, y);
        }

        // move connections with object  - 00/05/16 aa
        if (drawableParts != null)
        {
            for (int i = 0; i < drawableParts.size(); i++)
            {
                part = (GraphicPart) drawableParts.elementAt(i);
                part.moveobj(x, y);
                if (part instanceof IconInport)
                {
                    conn = ((IconInport) part).link;
                    if (conn != null)
                    {
                        conn.movedest(x, y);
                    }
                }
                if (part instanceof IconOutport)
                {
                    IconOutport op = (IconOutport) part;
                    for (int j = 0; j < op.links.size(); j++)
                    {
                        conn = op.links.elementAt(j);
                        conn.movesrc(x, y);
                    }
                }
            }
        }

    }

    /**
     * Select this icon as a whole if the point (x,y) is within the scope of this
     * icon.
     *
     * @return <code>true</code> if the point (x,y) is within the scope of this
     *         icon
     *         <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y)
    {
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax)
        {
            selIconBody = new GraphicPart_rect(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            return (true);
        }
        return (false);
    }

    /**
     * Select this icon as a whole if this icon is completely within the rectangle
     * represented by the left-up corner (x0,y0) and right-down corner (x1,y1).
     *
     * @return <code>true</code> if this icon is completely within the
     *         rectangle represented by the left-up corner (x0,y0) and
     *         right-down corner (x1,y1)
     *         <code>false</code> otherwise
     */
    public boolean selectrect(int x0, int y0, int x1, int y1)
    {
        if (x0 < xmin && x1 > xmax && y0 < ymin && y1 > ymax)
        {
            selIconBody = new GraphicPart_rect(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            return (true);
        }
        return (false);
    }

    /**
     * get the corresponding Module that belongs with this Icon Instance
     *
     * @throws IOException if an IO error occurred
     */
    public Module getModuleFromFile() throws IOException, ClassNotFoundException, ParserConfigurationException,
            SAXException
    {
        Module returnModule = new Module();
        try
        {
            returnModule.getModuleFromFileUsingNick(libNickName, moduleName, versionName);
        }
        catch (IOException e)
        {
            System.err.println("IconInst:getModuleFromFile IOException " + moduleName);
            throw new IOException("IconInst getModleFromFile IOException");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("IconInst:getModuleFromFile ClassNotFoundException " + moduleName);
            throw new IOException("IconInst getModleFromFile ClassNotFoundException");
        }
        return (returnModule);
    }

    /**
     * Find an icon Inport of this IconInst's icon by name
     *
     * @return    <code>index</code> if the name matches
     */
    public int findInport(String name)
    {
        GraphicPart tempPart;
        int index = -1;
        for (int i = 0; i < drawableParts.size(); i++)
        {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if (tempPart instanceof IconInport)
            {
                IconInport ii = (IconInport) tempPart;
                if (ii.Name.equals(name))
                {
                    return (i);
                }
            }
        }
        return (index);
    }

    /**
     * Find an icon Outport of this IconInst's icon by name
     *
     * @return <code>index</code> if the name matches
     */
    public int findOutport(String name)
    {
        GraphicPart tempPart;
        int index = -1;
        for (int i = 0; i < drawableParts.size(); i++)
        {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if (tempPart instanceof IconOutport)
            {
                IconOutport io = (IconOutport) tempPart;
                if (io.Name.equals(name))
                {
                    return (i);
                }
            }
        }
        return (index);
    }

    /**
     * Select an icon port of this IconInst's icon if the point
     * (x,y) is within  close distance to that port.
     *
     * @return <code>true</code> if the point (x,y) is within
     *         close distance to
     *         an icon port of this IconInst's icon and meanwhile
     *         set that port's selPort variable to the found port.
     *         Also, unselect the port itself because ???
     *         Return true if found a port and false otherwise.
     */
    public boolean selectport(int x, int y)
    {
        GraphicPart tempPart;
        boolean status = false;
        for (int i = 0; i < drawableParts.size(); i++)
        {
            tempPart = (GraphicPart) drawableParts.elementAt(i);
            if ((tempPart instanceof IconInport ||
                    tempPart instanceof IconOutport) &&
                    tempPart.selectobj(x, y))
            { //00/5/11 aa
                selPort = tempPart;
                status=true;
            }
            else
                tempPart.unselect();
        }
        return (status);
    }

    /**
     * Make this IconInst unselected.
     */
    public void unselect()
    {
        //in C++ we would dispose of the old rect object but in java we wait for
        // the garbage collector to run
        selIconBody = null;
        for (int i = 0; i < drawableParts.size(); i++)
        {
            GraphicPart tempPart = (GraphicPart) drawableParts.elementAt(i);
            if (tempPart instanceof IconInport && ((IconInport) tempPart).select == 1)
            {
                ((IconInport) tempPart).select = 0;
            }
            else if (tempPart instanceof IconOutport && ((IconOutport) tempPart).select == 1)
            {
                ((IconOutport) tempPart).select = 0;
            }
        }
        //todo:do we have to paint the rect black?
        repaint(); //00/5/10 aa
    }

    /**
     * Write this icon to the output stream oos.
     *
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream oos)
            throws IOException
    {
        try
        {
            oos.writeUTF(instanceName);
            oos.writeBoolean(true);
            oos.writeUTF(parameters);
            boolean xminZeroed = false; //for instances we do not zero
            super.write(oos, xminZeroed); //write the icon info
        }
        catch (IOException e)
        {
            System.err.println("IconInst: write IOException");
            throw new IOException("IconInst write IOException");
        }

    }

    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {
        bw.write(prefix+"<iconInstance>\n");
        bw.write(prefix+"\t<instanceName>"+instanceName+"</instanceName>\n");
        bw.write(prefix+"\t<parameters>"+parameters+"</parameters>\n");
        super.writeXML(bw, false, prefix+"\t");
        bw.write(prefix+"</iconInstance>\n");
    }

    public void readXML(Schematic schematic, org.w3c.dom.Node iconInstNode)
    {
        NodeList iconInstChildren=iconInstNode.getChildNodes();
        for(int i=0; i<iconInstChildren.getLength(); i++)
        {
            org.w3c.dom.Node iconInstChild=(org.w3c.dom.Node)iconInstChildren.item(i);
            if(iconInstChild.getNodeName().equals("instanceName"))
                instanceName= SCSUtility.getNodeValue(iconInstChild);
            else if(iconInstChild.getNodeName().equals("parameters"))
                parameters= SCSUtility.getNodeValue(iconInstChild);
            else if(iconInstChild.getNodeName().equals("icon"))
                super.readXML(iconInstChild);
        }
        selIconBody=null;
        selPort=null;
        refresh();
    }

    /**
     * Read this icon from the input stream os.
     * @param ois
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream ois)
            throws IOException, ClassNotFoundException
    {
        try
        {
            instanceName = ois.readUTF();
            /*getCurrentVersion = */ois.readBoolean();
            parameters = ois.readUTF();
            super.read(ois); //we need the xmin and xmax
            selIconBody = null;
            selPort = null;
        }
        catch (IOException e)
        {
            System.err.println("Error:IconInst read part1 IOException.");
            throw new IOException("IconInst read part1 IOException");
        }
        refresh();

    }//end method

    /**
     * This next part, replaces the IconInst with the latest information from the file system
     * In the first case where the IconInst is a float, this is absolutely necessary.
     * In the second case where the IconInst is a specific, we go and get the same module againg because its looks
     * could have changes. If one wants to get really picky, they can disallow this, and leave specific IconInst as
     * they were when they were saved with the Schematic.
     */
    public void refresh()
    {
        try
        {
            int savedxmin = xmin;
            int savedymin = ymin;
            FileInputStream fis;
            Module module=null;
            //Specific
            // open new stream
            // find path that icon.libNickName points to
            // find moduleName and specific version of that module
            // read modules info and set icon to that
            module = SCSUtility.openModule(libNickName, moduleName, versionName);
            // todo: change to exception
            if (module == null)
            {
                System.err.println("Error:module file is null");

                throw new IOException("Error: module file is null");
            }
            // we use the location from the instance
            setIconInfoWOffset(module.myIcon, savedxmin, savedymin);

            instanceLabel = new GraphicPart_text(UserPref.instanceTextLocation, shapexmin, shapeymin, shapexmax,
                                                 shapeymax, instanceName, UserPref.instanceTextFont,
                                                 UserPref.instanceText_col, UserPref.instanceTextSize);
        } //end try
        catch (IOException e)
        {
            System.err.println("Error:IconInst read part2 IOException.");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:IconInst read part2 ClassNotFoundException.");
        }
        catch (ParserConfigurationException e)
        {
            System.err.println("Error:IconInst read part2 ParserConfigurationException.");
        }
        catch (SAXException e)
        {
            System.err.println("Error:IconInst read part2 SAXException.");
        }
    }
} //end class IconInst
