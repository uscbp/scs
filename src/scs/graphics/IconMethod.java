package scs.graphics;/* SCCS  %W% --- %G% -- %U% */
//Copyright: Copyright (c) 1997 University of Southern California Brain Project.
//Copyright: This software may be freely copied provided the toplevel
//Copyright: COPYRIGHT file is included with each such copy.
//Copyright: Email nsl@java.usc.edu.

/**
* IconMethod - A class representing the graphical appearance of a method in
* schematic. Unlike the Icon class, this class is basically a graphical body
* with the name of the method.
*
*
* @param     libNickName    Alias name of library this iconMethod came from
*
* @param     methodName    name of method this iconMethod is related to
*
* @param     versionName    version of module this iconMethod is
*
* @param       shapexmin    the x-coordinate of the left-up corner of the smallest
*			enclosing rectangle border of this icon
*
* @param       shapeymin    the y-coordinate of the left-up corner of the smallest
*			enclosing rectangle border of this icon
*
* @param       shapexmax    the x-coordinate of the right-down corner of the
*			smallest enclosing rectangle border of this icon
*
* @param       shapeymax    the y-coordinate of the right-down corner of the
*			smallest enclosing rectangle border of this icon
*
* @param     drawableParts    an vector of components that make up of this
*				icon, ie graphical shapes
*
* @param    select    a flag indicating whether this icon is selected and
*			highlighted
*
* @param      methodLabel
*
* @since JDK1.1
* @author Eric Hu
*/


import scs.util.SCSUtility;
import scs.util.UserPref;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IconMethod extends Component implements Cloneable{


	
    GraphicPart_oval selIconBody = null;
    public String libNickName = "LIB1"; //re-stating these here since it is needed
    // by the schematic editor - instances of icons
    public String methodName = null;
    public String versionName = null;

    int shapexmin, shapeymin, shapexmax, shapeymax;  //without ports

    public Vector<Component> drawableParts;
    GraphicPart_text moduleLabel = null;
    int select = 0; //is part of the icon selected?

    /**
     * Constructor of this class with no parameters.
     */
    public IconMethod()
    {
        libNickName = "LIB1"; //re-stating these here since it is needed
        // by the schematic editor - instances of icons
        methodName = null;
        versionName = null;
        selIconBody = null;

        drawableParts = new Vector<Component>();
        shapexmin = shapeymin = 10000; //where the icon is located on a icon canvas
        shapexmax = shapeymax = -10000;
    }
    
    /**
     * Constructor of this class with current method
     * @param libNickName - library nickname
     * @param methodName - name of the method
     * @param versionName - module version
     */
    public IconMethod(String libNickName, String methodName, String versionName)
    {
        this.libNickName = libNickName;
        this.methodName = methodName;
        this.versionName = versionName;

        selIconBody = null;
        drawableParts = new Vector<Component>();
        shapexmin = shapeymin = 0; //where the icon is located on a icon canvas
        shapeymax = 100;
        shapexmax = 10*methodName.length();
        GraphicPart_oval mainIcon = new GraphicPart_oval(shapexmin, shapeymin, shapexmax, shapeymax, UserPref.rect_col);
        mainIcon.unselect();
        this.addDrawablePart(mainIcon);
       
    }

    /**
     * Constructor of this class with with parameters.
     * @param libNickName - library nickname
     * @param methodName - method name
     * @param versionName - module version
     * @param shapexmin - min x coordinate of shape
     * @param shapeymin - min y coordinate of shape
     * @param shapexmax - max x coordinate of shape
     * @param shapeymax - max y coordinate of shape
     */
    public IconMethod(String libNickName, String methodName, String versionName,
                int shapexmin, int shapeymin, int shapexmax, int shapeymax)
    {
        this.libNickName = libNickName;
        // by the schematic editor - instances of icons
        this.methodName = methodName;
        this.versionName = versionName;

        selIconBody = null;
        drawableParts = new Vector<Component>();
        this.shapexmin = shapexmin;
        this.shapeymin = shapeymin;
        this.shapexmax = shapexmax;
        this.shapeymax = shapeymax;
        //note: moduleLabel is not part of the drawable objects since
        //their location is dependent on the final shape of the icon itself.  If they
        // were included, the definition of where to place the text would be recursive.
        moduleLabel = new GraphicPart_text(UserPref.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax,
                methodName, UserPref.moduleTextFont, UserPref.moduleText_col, UserPref.moduleTextSize);
    }
    /**
     * Constructor of this class with an existing icon
     * @param myicon - Icon template
     */
    public IconMethod(IconMethod myicon)
    {
        setIconMethodInfo(myicon);
    }

    public IconMethod clone() throws CloneNotSupportedException
    {
        IconMethod clone = null;
        try
        {
            clone = (IconMethod) super.clone();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            clone = new IconMethod();
        }
        finally
        {
            clone.drawableParts = new Vector<Component>();
            for (int i = 0; i < drawableParts.size(); i++)
            {
                try
                {
                    clone.drawableParts.add(((GraphicPart) drawableParts.get(i)).clone());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            clone.libNickName = this.libNickName;
            clone.moduleLabel = this.moduleLabel;
            clone.methodName = this.methodName;
            clone.select = this.select;
            clone.shapexmax = this.shapexmax;
            clone.shapexmin = this.shapexmin;
            clone.shapeymax = this.shapeymax;
            clone.shapeymin = this.shapeymin;
            clone.versionName = this.versionName;
        }
        return clone;
    }
    
    /**
     * Set the Icon's information
     * @param myicon - icon template
     */
    public void setIconMethodInfo(IconMethod myicon)
    {
        // by the schematic editor - instances of icons
        this.libNickName = myicon.libNickName;
        this.methodName = myicon.methodName;
        this.versionName = myicon.versionName;

        this.shapexmin = myicon.shapexmin;
        this.shapeymin = myicon.shapeymin;
        this.shapexmax = myicon.shapexmax;
        this.shapeymax = myicon.shapeymax;

        this.selIconBody = myicon.selIconBody;
        
        moduleLabel = myicon.moduleLabel;

        drawableParts = myicon.drawableParts;
    }
    
    /**
     * Set the Icon's information with an offset
     * This is for copy the new icon information to the iconInstances
     * that are in existence.
     * @param myicon - icon template
     * @param xOffset - offset in x direction
     * @param yOffset - offset in y direction
     */
    public void setIconMethodInfoWOffset(IconMethod myicon, int xOffset, int yOffset)
    {
        int ix;
        GraphicPart part;
        if (myicon == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon template is null");
            return;
        }

        if (myicon.libNickName == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon libNickName is null");
            return;
        }

        if (myicon.libNickName == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon libNickName is null");
            return;
        }

        if (myicon.methodName == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon moduleName is null");
            return;
        }

        if (myicon.versionName == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon versionName is null");
            return;
        }

        if (myicon.moduleLabel == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon moduleLabel is null");
            return;
        }

        if (myicon.drawableParts == null)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon drawableParts is null");
            return;
        }

        if (myicon.drawableParts.size() == 0)
        {
            System.err.println("Error:Icon:setIconInfoWOffset:icon drawableParts is zero");
            return;
        }

        // by the schematic editor - instances of icons
        this.libNickName = myicon.libNickName;
        this.methodName = myicon.methodName;
        this.versionName = myicon.versionName;

        this.shapexmin = myicon.shapexmin + xOffset;
        this.shapeymin = myicon.shapeymin + yOffset;
        this.shapexmax = myicon.shapexmax + xOffset;
        this.shapeymax = myicon.shapeymax + yOffset;

        //We are re-reading the module again to get the latest info.
        //We must erase the old drawableParts list.
        deleteAllDrawableParts(); //for this icon not the incoming

        if (myicon.drawableParts != null)
        {
            for (ix = 0; ix < myicon.drawableParts.size(); ix++)
            {
                part = (GraphicPart) myicon.drawableParts.elementAt(ix);
                part.moveobj(xOffset, yOffset);
                addDrawablePart(part);
            }
        }
    }
    /**
     * Returns the xmin value.
     * @return xmin value
     */
    public int getXmin()
    {
        return shapexmin;
    }

    /**
     * Returns the ymin value.
     * @return ymin value
     */
    public int getYmin()
    {
        return shapeymin;
    }

    /**
     * Returns the xmax value.
     * @return xmax value
     */
    public int getXmax()
    {
        return shapexmax;
    }

    /**
     * Returns the ymax value
     * @return ymax value
     */
    public int getYmax()
    {
        return shapeymax;
    }
    //////////////////////////////////////////////////////////////////////
    /**
     * Add a component to this icon. The component is probably a graphic shape.
     *
     * @param    gobj    the graphic component that's gonna be added to this icon
     * Note: adjust location of moduleLabel.
     */
    public void addDrawablePart(GraphicPart gobj)
    {
        drawableParts.addElement(gobj);
        setminmax(gobj);
        if (moduleLabel == null)
        {
            moduleLabel = new GraphicPart_text(UserPref.moduleTextLocation, shapexmin, shapeymin, shapexmax,
                    shapeymax, methodName, UserPref.moduleTextFont, UserPref.moduleText_col,
                    UserPref.moduleTextSize);
        }
        else
        {
            resetLabel();
        }
    }
    
    /**
     * Delete the ix'th component of this icon.
     *
     * @param    ix - the index of the component in the vector that's gonna be deleted
     * Note: adjust location of moduleLabel.
     * @return whether or not successfully deleted
     */
    public boolean deleteDrawablePart(int ix)
    {
        drawableParts.removeElementAt(ix);
        setminmax();
        if ((drawableParts.size() == 0) && (moduleLabel != null))
        {
            moduleLabel = null;
        }
        else
        {
            resetLabel();
        }
        return true;
    }

    /**
     * Resets the label.
     */
    public void resetLabel()
    {
        if(moduleLabel!=null)
        {
            //re-calculate the location of the moduleLabel
            moduleLabel.initText(UserPref.moduleTextLocation, shapexmin, shapeymin, shapexmax, shapeymax,
                    methodName, UserPref.moduleTextFont, UserPref.moduleText_col, UserPref.moduleTextSize);
        }
    }

    /**
     * Delete a graphical object
     * @param varName name of the object to be deleted
     * @return confirmation if object is deleted
     */
    public boolean deleteDrawablePart(String varName)
    {
        int idx = findDrawableIndex(varName);
        return idx > -1 && idx < drawableParts.size() && deleteDrawablePart(idx);
    }

    /**
     * Delete All drawableParts
     */
    public void deleteAllDrawableParts()
    {
        drawableParts = null;
        drawableParts = new Vector<Component>();
        if ((moduleLabel != null))
        {
            moduleLabel = null;
        }
    }
    /**
     * Move the ix'th component of this icon to the last of the components drawableParts vector.
     * Since we are drawing the components in the order that they're in the vector,
     * being the last component means it will be the last to be painted and
     * appear on top of other images.
     *
     * @param    ix    the index of the component in vector that's gonna be
     *			moved to the last position
     * Note: this is a very dangerous method and should NOT BE USED.
     * Rearranging the pins on a icon, causes the schematic editor to
     * chock when it reads in the connections.
     */
    public void moveToLast(int ix)
    {
        GraphicPart TempComponent;

        TempComponent = (GraphicPart) drawableParts.elementAt(ix);
        drawableParts.removeElementAt(ix);
        drawableParts.addElement(TempComponent);
    }

    public int findDrawableIndex(String varName)
    {
        for (int i = 0; i < drawableParts.size(); i++)
        {
            if (drawableParts.get(i) instanceof IconPort)
            {
                if (((IconPort) drawableParts.get(i)).Name.equals(varName))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Paint this icon.
     * @param g - Graphics object to paint to
     */
    public void paint(Graphics g)
    {
        int ix;
        GraphicPart TempComponent;

        if (drawableParts != null)
        {
            for (ix = 0; ix < drawableParts.size(); ix++)
            {
                TempComponent = (GraphicPart) drawableParts.elementAt(ix);
                TempComponent.paint(g);
            }
        }

        //if selected: draw selection box.
        if (select != 0)
        {
            g.setColor(Color.red);
            g.drawOval(shapexmin, shapeymin, shapexmax - shapexmin, shapeymax - shapeymin); // nitgupta
        }
      //if selIconBody: draw selection box.
        if (selIconBody != null)
        {
            // selIconBody should have the right coordinates
            selIconBody.paintOpen(g);// move must update the selection
        }
        if (moduleLabel != null)
        {
            moduleLabel.paint(g);
        }

        
    }
    
    /**
     * Move the ix'th part of this icon by x offset in x-direction and y offset * in y-direction.
     *
     * @param    ix    the index of the component that'll be moved
     * @param    x    the moving offset in x-direction
     * @param    y    the moving offset in y-direction
     */
    public void movePart(int ix, int x, int y)
    {
        GraphicPart part;

        part = (GraphicPart) drawableParts.elementAt(ix);
        part.moveobj(x, y);
        setminmax();
    }


    //------------------------------------------------------------
//    /**
//     * Move this icon as a whole by x offset in x-direction and y offset in
//     * y-direction.
//     *
//     * @param    x    the moving offset in x-direction
//     * @param    y    the moving offset in y-direction
//     */
//    public void moveobj(int x, int y)
//    {
//        int ix;
//        GraphicPart part;
//
//        if (drawableParts != null)
//        {
//            for (ix = 0; ix < drawableParts.size(); ix++)
//            {
//                part = (GraphicPart) drawableParts.elementAt(ix);
//                part.moveobj(x, y);
//            }
//            if (moduleLabel != null)
//            {
//                moduleLabel.moveobj(x, y);
//            }
//        }
//
//        //aa: note this setminmax has to be separate or else
//        // if you try and set it in the loop above the min and max
//        // settings for xmin, xmax, ymin, and ymax do not work
//        setminmax();
//        //label location should be based on xmin, xmax,ymin,ymax
//        if (moduleLabel != null)
//        {
//            moduleLabel.moveobj(x, y);
//        }
//    }
    
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
            }
        }

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
        if (x0 < shapexmin && x1 > shapexmax && y0 < shapeymin && y1 > shapeymax)
        {
        	select = 1;
            selIconBody = new GraphicPart_oval(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            return (true);
        }
        return (false);
    }


    /**
     * Select this icon as a whole if the point (x,y) is within the scope of this
     * icon.
     * @param x - x coordinate
     * @param y - y coordinate
     * @return    <code>true</code> if the point (x,y) is within the scope of this icon <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y)
    {
        if (x >= shapexmin && x <= shapexmax && y >= shapeymin && y <= shapeymax)
        {
            select = 1;
            selIconBody = new GraphicPart_oval(shapexmin, shapeymin, shapexmax, shapeymax, Color.red);
            
            return (true);
        }
        return (false);
    }
    
    

    /**
     * Make this IconInst unselected.
     */
    public void unselect()
    {
        //in C++ we would dispose of the old rect object but in java we wait for
        // the garbage collector to run
    	select = 0;
    	selIconBody = null;
        
        //todo:do we have to paint the rect black?
        repaint(); //00/5/10 aa
    }
    
    /**
     * Calculate the values of xmin, ymin, xmax, and ymax.
     */
    public void setminmax()
    {
        // this should work, even though the initial values stored
        // were only the first point, if drawableParts is storing
        // references to the graphic_obj, which gets continuously
        // updated.

        int ix;
        GraphicPart part;

        shapexmin = shapeymin = 10000;//99/4/14 added part
        shapexmax = shapeymax = -10000;

        if (drawableParts != null)
        {
            for (ix = 0; ix < drawableParts.size(); ix++)
            {
                part = (GraphicPart) drawableParts.elementAt(ix);
                setminmax(part); // this will be slower
            }
        }
    }
    /**
     * reset component min/maxusing the graphics object.
     *
     * @param gobj - GraphicPart to get min/max from
     */
    public void setminmax(GraphicPart gobj)
    {
            shapexmin = Math.min(shapexmin, gobj.xmin);
            shapeymin = Math.min(shapeymin, gobj.ymin);
            shapexmax = Math.max(shapexmax, gobj.xmax);
            shapeymax = Math.max(shapeymax, gobj.ymax);
    }
    

    public void writeXML(BufferedWriter bw, boolean xminZeroed, String prefix) throws IOException
    {
        //setminmax();  //todo: is this necessary? could be
        int sxmin = shapexmin; //saved xmin
        int symin = shapeymin;
        bw.write(prefix+"<method>\n");
        bw.write(prefix+"\t<library>"+libNickName+"</library>\n");
        bw.write(prefix+"\t<name>"+methodName+"</name>\n");
        bw.write(prefix+"\t<version>"+versionName+"</version>\n");
        
            bw.write(prefix+"\t<bounds>\n");
            bw.write(prefix+"\t\t<min>\n");
            bw.write(prefix+"\t\t\t<x>"+shapexmin+"</x>\n");
            bw.write(prefix+"\t\t\t<y>"+shapeymin+"</y>\n");
            bw.write(prefix+"\t\t</min>\n");
            bw.write(prefix+"\t\t<max>\n");
            bw.write(prefix+"\t\t\t<x>"+shapexmax+"</x>\n");
            bw.write(prefix+"\t\t\t<y>"+shapeymax+"</y>\n");
            bw.write(prefix+"\t\t</max>\n");
            bw.write(prefix+"\t</bounds>\n");
        bw.write(prefix+"\t<parts>\n");
        if(drawableParts!=null)
        {
            for (int ix = 0; ix < drawableParts.size(); ix++)
            {
                GraphicPart part = (GraphicPart) drawableParts.elementAt(ix);
                part.writeXML(bw, sxmin, symin, prefix+"\t\t");
            }
        }
        bw.write(prefix+"\t</parts>\n");
        bw.write(prefix+"</method>\n");
    }
    
    /**
     * Write this icon to the output stream os.
     * @param os - ObjectOutputStream to write to
     * @param xminZeroed -
     * @throws IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os, boolean xminZeroed) throws IOException
    {
        setminmax();  //todo: is this necessary? could be
        int sxmin = shapexmin; //saved xmin
        int symin = shapeymin;

        try
        {
            os.writeUTF(libNickName); // this is only need for the icons
            // within the schematics list of icons
            os.writeUTF(methodName);
            os.writeUTF(versionName);
            if (xminZeroed)
            {
                os.writeInt(0);  // zero
                os.writeInt(0);  // zero
                os.writeInt(shapexmax - shapexmin);  // this use to be xdelta=xmax-xmin
                os.writeInt(shapeymax - shapeymin);  // this use to be ydelta=ymax-ymin
//                os.writeInt(shapexmin - xmin);
//                os.writeInt(shapeymin - ymin);
//                os.writeInt(shapexmax - xmin);
//                os.writeInt(shapeymax - ymin);
            }
            else
            {
//                os.writeInt(xmin);  // non-zero
//                os.writeInt(ymin);  // non-zero
//                os.writeInt(xmax);  // this use to be xdelta=xmax-xmin
//                os.writeInt(ymax);  // this use to be ydelta=ymax-ymin
                os.writeInt(shapexmin);
                os.writeInt(shapeymin);
                os.writeInt(shapexmax);
                os.writeInt(shapeymax);
            }
            int ix;
            GraphicPart part;
            if (drawableParts == null)
            {
                os.writeInt(0);
            }
            else
            {
                os.writeInt(drawableParts.size());
                for (ix = 0; ix < drawableParts.size(); ix++)
                {
                    // GraphicParts are always repositioned to 0,0
                    part = (GraphicPart) drawableParts.elementAt(ix);
                     if (part instanceof GraphicPart_line)
                    {
                        os.writeUTF("Line");
                    }
                    else if (part instanceof GraphicPart_rect)
                    {
                        os.writeUTF("Rect");
                    }
                    else if (part instanceof GraphicPart_oval)
                    {
                        os.writeUTF("Oval");
                    }
                    else if (part instanceof GraphicPart_poly)
                    {
                        os.writeUTF("Poly");
                    }
                    else if (part instanceof GraphicPart_text)
                    {
                        os.writeUTF("Text");
                    }
                    else
                    {
                        System.err.println("Error:Icon:write: unknown part type.");
                        throw new IOException("Icon:write: unknown part type.");
                    }
                    part.write(os, sxmin, symin);
                }
            }//end if
        }
        catch (IOException e)
        {
            System.err.println("Error:Icon: write IOException");
            throw new IOException("Icon write IOException");
        }
    }
    /**
     * Load an IconMethod from an XML file.
     * @param iconNode the node representing data for the IconMethod
     */
    public void readXML(Node iconNode)
    {
        select = 0;

        if ((drawableParts != null) && (drawableParts.size() != 0))
        {
            drawableParts.removeAllElements();
        }
        NodeList iconChildren=iconNode.getChildNodes();
        for(int i=0; i<iconChildren.getLength(); i++)
        {
            org.w3c.dom.Node iconChild=(org.w3c.dom.Node)iconChildren.item(i);
            if(iconChild.getNodeName().equals("library"))
                libNickName= SCSUtility.getNodeValue(iconChild);
            else if(iconChild.getNodeName().equals("name"))
                methodName=SCSUtility.getNodeValue(iconChild);
            else if(iconChild.getNodeName().equals("version"))
                versionName=SCSUtility.getNodeValue(iconChild);
            else if(iconChild.getNodeName().equals("bounds"))
            {
                NodeList boundsChildren=iconChild.getChildNodes();
                for(int j=0; j<boundsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node boundsChild=(org.w3c.dom.Node)boundsChildren.item(j);
                    if(boundsChild.getNodeName().equals("min"))
                    {
                        NodeList minChildren=boundsChild.getChildNodes();
                        for(int k=0; k<minChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node minChild=(org.w3c.dom.Node)minChildren.item(k);
                            if(minChild.getNodeName().equals("x"))
                                shapexmin=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                            else if(minChild.getNodeName().equals("y"))
                                shapeymin=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                        }
                    }
                    else if(boundsChild.getNodeName().equals("max"))
                    {
                        NodeList maxChildren=boundsChild.getChildNodes();
                        for(int k=0; k<maxChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node maxChild=(org.w3c.dom.Node)maxChildren.item(k);
                            if(maxChild.getNodeName().equals("x"))
                                shapexmax=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                            else if(maxChild.getNodeName().equals("y"))
                                shapeymax=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                        }
                    }
                }
            }
            
            else if(iconChild.getNodeName().equals("parts"))
            {
                NodeList partsChildren=iconChild.getChildNodes();
                for(int j=0; j<partsChildren.getLength(); j++)
                {
                    org.w3c.dom.Node partsChild=(org.w3c.dom.Node)partsChildren.item(j);
                    if(partsChild.getNodeName().equals("part"))
                    {
                        String partType=partsChild.getAttributes().getNamedItem("type").getNodeValue();
                        if(partType.equals("line"))
                        {
                            GraphicPart_line line=new GraphicPart_line();
                            line.readXML(partsChild);
                            drawableParts.addElement(line);
                        }
                        else if(partType.equals("oval"))
                        {
                            GraphicPart_oval oval=new GraphicPart_oval();
                            oval.readXML(partsChild);
                            drawableParts.addElement(oval);
                        }
                        else if(partType.equals("poly"))
                        {
                            GraphicPart_poly poly=new GraphicPart_poly();
                            poly.readXML(partsChild);
                            drawableParts.addElement(poly);
                        }
                        else if(partType.equals("rect"))
                        {
                            GraphicPart_rect rect=new GraphicPart_rect();
                            rect.readXML(partsChild);
                            drawableParts.addElement(rect);
                        }
                        else if(partType.equals("text"))
                        {
                            GraphicPart_text text=new GraphicPart_text();
                            text.readXML(partsChild);
                            drawableParts.addElement(text);
                        }
                    }
                }
            }
        }
        moduleLabel = new GraphicPart_text(UserPref.moduleTextLocation, shapexmin, shapeymin, shapexmax,
                    shapeymax, methodName, UserPref.moduleTextFont, UserPref.moduleText_col,
                    UserPref.moduleTextSize);
    }

    /**
     * Read this icon from the input stream os.
     * @param os - ObjectInputStream to read from
     * @throws IOException             if an IO error occurred
     * @throws ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(ObjectInputStream os) throws IOException, ClassNotFoundException
    {
        try
        {
            libNickName = os.readUTF(); // this is only need for the icons
            // within the schematics list of icons
            methodName = os.readUTF();
            versionName = os.readUTF();
//            if (debug)
//            {
//                System.out.println("Debug:Icon:libNickName: " + libNickName);
//                System.out.println("Debug:Icon:moduleName: " + moduleName);
//                System.out.println("Debug:Icon:versionName: " + versionName);
//            }
//            xmin = os.readInt();
//            ymin = os.readInt();
//            xmax = os.readInt();
//            ymax = os.readInt();
            shapexmin = os.readInt();
            shapeymin = os.readInt();
            shapexmax = os.readInt();
            shapeymax = os.readInt();

//            if (debug)
//            {
//                System.out.println("Debug:Icon:xmin ymin xmax ymax: " + xmin + ' ' + ymin + ' ' + xmax + ' ' + ymax);
//                System.out.println("Debug:Icon:shapexmin shapeymin shapexmax shapeymax: " + shapexmin + ' ' +
//                        shapeymin + ' ' + shapexmax + ' ' + shapeymax);
//            }

            moduleLabel = new GraphicPart_text(UserPref.moduleTextLocation, shapexmin, shapeymin, shapexmax,
                    shapeymax, methodName, UserPref.moduleTextFont, UserPref.moduleText_col,
                    UserPref.moduleTextSize);

            select = 0;

            if ((drawableParts != null) && (drawableParts.size() != 0))
            {
                drawableParts.removeAllElements();
            }
            int ix;
            int n = os.readInt();  // read number of drawableParts
            String str;
            GraphicPart_line line;
            GraphicPart_rect rect;
            GraphicPart_oval oval;
            GraphicPart_poly poly;
            GraphicPart_text text;

            for (ix = 0; ix < n; ix++)
            {
                str = os.readUTF();
                 if (str.equals("Line"))
                {
                    line = new GraphicPart_line();
                    line.read(os);
                    drawableParts.addElement(line);
                }
                else if (str.equals("Rect"))
                {
                    rect = new GraphicPart_rect();
                    rect.read(os);
                    drawableParts.addElement(rect);
                }
                else if (str.equals("Oval"))
                {
                    oval = new GraphicPart_oval();
                    oval.read(os);
                    drawableParts.addElement(oval);
                }
                else if (str.equals("Poly"))
                {
                    poly = new GraphicPart_poly();
                    poly.read(os);
                    drawableParts.addElement(poly);
                }
                else if (str.equals("Text"))
                {
                    text = new GraphicPart_text();
                    text.read(os);
                    drawableParts.addElement(text);
                }
                else
                {
                    System.err.println("IconMethod:read: unknown part type.");
                    throw new IOException("IconMethod:read: unknown part type.");
                }

            }
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Icon read ClassNotFoundException");
            throw new ClassNotFoundException("Icon read ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Icon read IOException");
            throw new IOException("Icon read IOException");
        }
    }
    
    
}
