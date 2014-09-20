package scs.graphics;/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart - A base class representing graphic objects which is inherited by
 * classes of special shaped graphic objects.
 *
 * @author Weifang Xie
 * @version     %I%, %G%
 *
 * @param       select    a flag indicating whether this graphic object is
 *			selected and highlighted. different meaning with 
 *			different value:
 *			0  -- not selected
 *			1  -- selected as a whole
 *			>1 -- vertices selected, the first vertex having value
 *			2, and so on ...
 * @param       xmin    the x-coordinate of the left-up corner of the smallest
 *			surrounding rectangle border of this graphic object
 * @param       ymin    the y-coordinate of the left-up corner of the smallest
 *			surrounding rectangle border of this graphic object
 * @param       xmax    the x-coordinate of the right-down corner of the
 *			smallest surrounding rectangle border of this graphic 
 *			object
 * @param       ymax    the y-coordinate of the right-down corner of the
 *			smallest surrounding rectangle border of this graphic 
 *			object
 *
 * @since JDK1.1
 */

import org.w3c.dom.NodeList;

import java.awt.*;
import java.io.*;

import scs.util.SCSUtility;


public class GraphicPart extends Component implements Cloneable
{
    public int select;
    protected int xmin, ymin, xmax, ymax;

    /**
     * Constructor of this class with no parameters.
     */
    public GraphicPart()
    {
    }

    public GraphicPart clone() throws CloneNotSupportedException
    {
        GraphicPart clone = null;
        try
        {
            clone = (GraphicPart) super.clone();
        }
        catch (Exception e)
        {
            clone = new GraphicPart();
        }
        finally
        {
            if(clone==null)
                clone=new GraphicPart();

            clone.select = this.select;
            clone.xmin = this.xmin;
            clone.xmax = this.xmax;
            clone.ymin = this.ymin;
            clone.ymax = this.ymax;
        }
        return clone;
    }

    public int getXmin()
    {
        return xmin;
    }

    public int getYmin()
    {
        return ymin;
    }

    public int getXmax()
    {
        return xmax;
    }

    public int getYmax()
    {
        return ymax;
    }

    /**
     * An overridable method which checks whether this graphic object is finished     * building or not.
     */
    public boolean done()
    {
        return (true);
    }

    /**
     * Set the color of this graphic object to be c.
     */
    public void setcolor(Color c)
    {
    }

    /**
     * Paint this graphic object.
     */
    public void paintComponent(Graphics g)  //use to be called paint
    {
    }

    /**
     * Paint this graphic object but offset
     */
    public void paintComponent(Graphics g, int xmin, int ymin)
    {
    }

    /**
     * Move this graphic object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y)
    {
    }

    /**
     * Move the selected point of this graphic object by x offset in x-direction
     * and y offset in y-direction.
     */
    public void movepoint(int x, int y)
    {
    }

    /**
     * Select the point of this graphic object which is within close distance to
     * the point of (x,y).
     *
     * @return <code>true</code> if there exists one point on this graphic
     *         object which is within close distance to the point of (x,y)
     *         <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y)
    {
        return (false);
    }

    /**
     * Select this graphic object as a whole if the point (x,y) is within close
     * distance to this object given the offsets.
     *
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this graphic object
     *         <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset)
    {
        // todo: put in error message here, I do not think this should ever
        // be called
        return (false);
    }

    /**
     * Select this graphic object as a whole if the point (x,y) is within close
     * distance to this object.
     *
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this graphic object
     *         <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y)
    {
        // todo: put in error message here, I do not think this should ever
        // BE CALLED
        return (false);
    }

    /**
     * An overidable method which adds a point (x,y) to the outline of this
     * graphic object.
     */
    public void addpoint(int x, int y)
    {
    }

    /**
     * Make this graphic object unselected.
     */
    public void unselect()
    {
        select = 0;
    }
//------------------------------------------------------------------

    /**
     * Write this graphic object to the output stream os.
     *
     * @param os the output stream to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException
    {
        try
        {
            os.writeInt(xmin - x);
            os.writeInt(ymin - y);
            os.writeInt(xmax - x);
            os.writeInt(ymax - y);
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart write IOException");
            throw new IOException("Error:GraphicPart write IOException");
        }
    }

//------------------------------------------------------------------

    /**
     * WriteAllChars this graphic object to the output stream os.
     *
     * @param bw the buffered writer to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     */
    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<bounds>\n");
        bw.write(prefix+"\t<min>\n");
        bw.write(prefix+"\t\t<x>"+(xmin - x)+"</x>\n");
        bw.write(prefix+"\t\t<y>"+(ymin - y)+"</y>\n");
        bw.write(prefix+"\t</min>\n");
        bw.write(prefix+"\t<max>\n");
        bw.write(prefix+"\t\t<x>"+(xmax - x)+"</x>\n");
        bw.write(prefix+"\t\t<y>"+(ymax - y)+"</y>\n");
        bw.write(prefix+"\t</max>\n");
        bw.write(prefix+"</bounds>\n");
    }
//------------------------------------------------------------------

    /**
     * Read this graphic object from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    {
        try
        {
            xmin = os.readInt();
            ymin = os.readInt();
            xmax = os.readInt();
            ymax = os.readInt();
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart read IOException");
            throw new IOException("GraphicPart read IOException");
        }

        select = 0;
    }

    public void readXML(org.w3c.dom.Node boundsNode)
    {
        NodeList boundsChildren=boundsNode.getChildNodes();
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
                        xmin=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                    else if(minChild.getNodeName().equals("y"))
                        ymin=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                }
            }
            else if(boundsChild.getNodeName().equals("max"))
            {
                NodeList maxChildren=boundsChild.getChildNodes();
                for(int k=0; k<maxChildren.getLength(); k++)
                {
                    org.w3c.dom.Node maxChild=(org.w3c.dom.Node)maxChildren.item(k);
                    if(maxChild.getNodeName().equals("x"))
                        xmax=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                    else if(maxChild.getNodeName().equals("y"))
                        ymax=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                }
            }
        }
    }
} //end class GraphicPart
