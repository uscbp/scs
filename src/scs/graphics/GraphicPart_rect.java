package scs.graphics;
/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart_rect - A class representing graphic objects which have a rectangle
 * shape.
 *
 * @author Xie, Gupta
 * @version     %I%, %G%
 *
 * @param       c       color of this graphic object
 * @param       x0      x-coordinate of the starting corner of this rectangle
 *                      object
 * @param       y0      y-coordinate of the starting corner of this rectangle
 *                      object
 * @param       x1      x-coordinate of the ending corner of this rectangle
 *                      object
 * @param       y1      y-coordinate of the ending corner of this rectangle
 *                      object
 *
 * @since JDK1.1
 */

import org.w3c.dom.NodeList;

import java.awt.*;
import java.io.*;

import scs.util.SCSUtility;


public class GraphicPart_rect extends GraphicPart
{
    Color c;
    int startX, startY, endX, endY;

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart_rect()
    {
    }
    //------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     */
    public GraphicPart_rect(int xx0, int yy0, Color cc)
    {
        startX = endX = xmin = xmax = xx0;
        startY = endY = ymin = ymax = yy0;
        c = cc;
        select = 5;
    }
    //------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 to be
     * xx0, the initial value of y0 to be yy0, and the color c to be cc.
     */
    GraphicPart_rect(int xx0, int yy0, int xx1, int yy1, Color cc)
    {
        startX = xx0;
        startY = yy0;
        endX = xx1;
        endY = yy1;
        c = cc;
        select = 5;
    }

    public int getStartX()
    {
        return startX;
    }

    public int getStartY()
    {
        return startY;
    }

    public int getEndX()
    {
        return endX;
    }

    public int getEndY()
    {
        return endY;
    }
    //------------------------------------------------------

    /**
     * Set the color of this rectangle object to be cc.
     */
    public void setcolor(Color cc)
    {
        c = cc;
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object with offset
     */
    public void paint(Graphics g, int xOffset, int yOffset)
    {
        int xS0 = startX + xOffset;
        int xS1 = endX + xOffset;
        int yS0 = startY + yOffset;
        int yS1 = endY + yOffset;

        int x = (xS1 > xS0) ? xS0 : xS1;
        int y = (yS1 > yS0) ? yS0 : yS1;
        int w = (xS1 > xS0) ? xS1 - xS0 : xS0 - xS1;
        int h = (yS1 > yS0) ? yS1 - yS0 : yS0 - yS1;

        g.setColor(c);
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        g.drawRect(x, y, w, h);
        if (select != 0) //draw handles
        {
            g.setColor(Color.red);
            g.fillRect(x - 2, y - 2, 4, 4);
            g.fillRect(x + w - 2, y - 2, 4, 4);
            g.fillRect(x - 2, y + h - 2, 4, 4);
            g.fillRect(x + w - 2, y + h - 2, 4, 4);
        }
    }

    //------------------------------------------------------

    /**
     * Paint this rectangle object.
     */
    public void paint(Graphics g)
    {
        paint(g, 0, 0);
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object with offset
     */
    public void paintOpen(Graphics g, int xOffset, int yOffset)
    {
        int xS0 = startX + xOffset;
        int xS1 = endX + xOffset;
        int yS0 = startY + yOffset;
        int yS1 = endY + yOffset;

        int x = (xS1 > xS0) ? xS0 : xS1;
        int y = (yS1 > yS0) ? yS0 : yS1;
        int w = (xS1 > xS0) ? xS1 - xS0 : xS0 - xS1;
        int h = (yS1 > yS0) ? yS1 - yS0 : yS0 - yS1;

        g.setColor(c);
        g.drawRect(x, y, w, h);
        if (select != 0) //draw handles
        {
            g.setColor(Color.red);
            g.fillRect(x - 2, y - 2, 4, 4);
            g.fillRect(x + w - 2, y - 2, 4, 4);
            g.fillRect(x - 2, y + h - 2, 4, 4);
            g.fillRect(x + w - 2, y + h - 2, 4, 4);
        }
    }
    //------------------------------------------------------

    /**
     * Paint this rectangle object.
     */
    public void paintOpen(Graphics g)
    {
        paintOpen(g, 0, 0);
    }
    //------------------------------------------------------


    /**
     * Move this rectangle object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y)
    {
        endX += x;
        startX += x;
        endY += y;
        startY += y;
        if (startX < endX)
        {
            xmin = startX;
            xmax = endX;
        }
        else
        {
            xmin = endX;
            xmax = startX;
        }
        if (startY < endY)
        {
            ymin = startY;
            ymax = endY;
        }
        else
        {
            ymin = endY;
            ymax = startY;
        }
    }
    //------------------------------------------------------

    /**
     * Move the selected point of this rectangle object by x offset in
     * x-direction and y offset in y-direction.
     */
    public void movepoint(int x, int y)
    {
        if (select == 5 || select == 3)
        {
            endX += x;
        }
        if (select == 4 || select == 2)
        {
            startX += x;
        }
        if (select == 5 || select == 4)
        {
            endY += y;
        }
        if (select == 3 || select == 2)
        {
            startY += y;
        }
        if (startX < endX)
        {
            xmin = startX;
            xmax = endX;
        }
        else
        {
            xmin = endX;
            xmax = startX;
        }
        if (startY < endY)
        {
            ymin = startY;
            ymax = endY;
        }
        else
        {
            ymin = endY;
            ymax = startY;
        }
    }
    //------------------------------------------------------

    /**
     * Select the point of this oval object which is within close distance to
     * the point of (x,y).
     *
     * @return <code>true</code> if there exists one point among the four
     *         vertices of this rectangle object which is within close distance
     *         to the point of (x,y)
     *         <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y)
    {
        if (x - startX < 3 && x - startX > -3 && y - startY < 3 && y - startY > -3)
        {
            select = 2;
            return (true);
        }
        if (x - endX < 3 && x - endX > -3 && y - startY < 3 && y - startY > -3)
        {
            select = 3;
            return (true);
        }
        if (x - startX < 3 && x - startX > -3 && y - endY < 3 && y - endY > -3)
        {
            select = 4;
            return (true);
        }
        else if (x - endX < 3 && x - endX > -3 && y - endY < 3 && y - endY > -3)
        {
            select = 5;
            return (true);
        }
        return (false);
    }
    //------------------------------------------------------

    /**
     * Select this rectangle object as a whole if the point (x,y) is within
     * close distance to this object.
     *
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this polygon object
     *         <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y)
    {
        if (x >= xmin && x <= xmax && y >= ymin && y <= ymax)
        {
            select = 1;
            return (true);
        }
        return (false);
    }
    //------------------------------------------------------

    /**
     * Select this rectangle object as a whole if the point (x,y) is within
     * close distance to this object.
     *
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this polygon object
     *         <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset)
    {
        int tempxmin = xmin + xOffset;
        int tempymin = ymin + yOffset;
        int tempxmax = xmax + xOffset;
        int tempymax = ymax + yOffset;
        if (x >= tempxmin && x <= tempxmax && y >= tempymin && y <= tempymax)
        {
            select = 1;
            return (true);
        }
        return (false);
    }

    //------------------------------------------------------

    /**
     * Write this rectangle object to the output stream os.
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
            super.write(os, x, y);
            os.writeObject(c);
            os.writeInt(startX - x);
            os.writeInt(startY - y);
            os.writeInt(endX - x);
            os.writeInt(endY - y);
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart_rect: write IOException.");
            throw new IOException("GraphicPart_rect write IOException");
        }
    }

    //------------------------------------------------------

    /**
     * WriteAllChars this rectangle object to the output stream os.
     *
     * @param bw the buffered writer to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     */
    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<part type=\"rect\">\n");
        super.writeXML(bw, x, y,prefix+"\t");
        bw.write(prefix+"\t<color>"+c.getRGB()+"</color>\n");
        bw.write(prefix+"\t<coordinates>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(startX - x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(startY - y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(endX - x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(endY - y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t</coordinates>\n");
        bw.write(prefix+"</part>\n");
    }
//-------------------------------------------------------------------------------

    public void readXML(org.w3c.dom.Node rectNode)
    {
        NodeList rectChildren=rectNode.getChildNodes();
        for(int i=0; i<rectChildren.getLength(); i++)
        {
            org.w3c.dom.Node rectChild=(org.w3c.dom.Node)rectChildren.item(i);
            if(rectChild.getNodeName().equals("bounds"))
                super.readXML(rectChild);
            else if(rectChild.getNodeName().equals("color"))
                c=new Color(Integer.parseInt(SCSUtility.getNodeValue(rectChild)));
            else if(rectChild.getNodeName().equals("coordinates"))
            {
                int coordIdx=0;
                NodeList coordinatesChildren=rectChild.getChildNodes();
                for(int j=0; j<coordinatesChildren.getLength(); j++)
                {
                    org.w3c.dom.Node coordinatesChild=(org.w3c.dom.Node)coordinatesChildren.item(j);
                    if(coordinatesChild.getNodeName().equals("coordinate"))
                    {
                        NodeList coordinateChildren=coordinatesChild.getChildNodes();
                        for(int k=0; k<coordinateChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node coordinateChild=(org.w3c.dom.Node)coordinateChildren.item(k);
                            if(coordinateChild.getNodeName().equals("x"))
                            {
                                if(coordIdx==0)
                                    startX=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                                else
                                    endX=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                            }
                            else if(coordinateChild.getNodeName().equals("y"))
                            {
                                if(coordIdx==0)
                                    startY=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                                else
                                    endY=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                            }
                        }
                        coordIdx++;
                    }
                }
            }
        }
    }

    /**
     * Read this rectangle object from the input stream os.
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
            super.read(os);
            c = (Color) os.readObject();
            startX = os.readInt();
            startY = os.readInt();
            endX = os.readInt();
            endY = os.readInt();
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassNotFoundException("GraphicPart_rect read ClassNotFoundException");
        }
        catch (IOException e)
        {
            throw new IOException("GraphicPart_rect read IOException");
        }
    }
}

