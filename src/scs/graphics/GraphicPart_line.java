package scs.graphics;
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.
/**
 * GraphicPart_line - A class representing graphic objects which have a line
 * shape.
 *
 * @author Xie, Gupta, Alexander
 * @version     %I%, %G%
 *
 * @param       c    color of this graphic object
 * @param       x0    x-coordinate of the first end-point of this line
 * @param       y0    y-coordinate of the first end-point of this line
 * @param       x1    x-coordinate of the second end-point of this line
 * @param       y1    y-coordinate of the second end-point of this line
 *
 * @since JDK1.1
 */

import org.w3c.dom.NodeList;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;

import scs.util.SCSUtility;

public class GraphicPart_line extends GraphicPart
{
    Color c;
    public int x0, y0, x1, y1;

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart_line()
    {
    }

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     * @param xx0 - x coordinate of first point
     * @param yy0 - y coordinate of first point
     * @param cc - line color
     */
    public GraphicPart_line(int xx0, int yy0, Color cc)
    {
        x0 = x1 = xmin = xmax = xx0;
        y0 = y1 = ymin = ymax = yy0;
        c = cc;
        select = 3;
    }

    /**
     * Set the color of this line object to be cc.
     */
    public void setcolor(Color cc)
    {
        c = cc;
    }

    /**
     * Paint this line object with offset.
     * This is for painting parts of icons on the schematic sheet.
     * @param g - Graphics object to paint to
     * @param xOffset - x offset to paint from
     * @param yOffset - y offset to paint from
     */
    public void paint(Graphics g, int xOffset, int yOffset)
    {
        g.setColor(c);

        int xS0 = x0 + xOffset;
        int yS0 = y0 + yOffset;
        int xS1 = x1 + xOffset;
        int yS1 = y1 + yOffset;
        g.drawLine(xS0, yS0, xS1, yS1);

        if (select != 0)
        {
            g.setColor(Color.red);
            g.fillRect(xS0 - 2, yS0 - 2, 4, 4);
            g.fillRect(xS1 - 2, yS1 - 2, 4, 4);
        }
    }

    /**
     * Paint this line object.
     * @param g - Graphics object to paint to
     */
    public void paint(Graphics g)
    {
        paint(g, 0, 0);
    }

    /**
     * Set min max x and y
     */
    public void setminmax()
    {
        if (x0 < x1)
        {
            xmin = x0;
            xmax = x1;
        }
        else
        {
            xmin = x1;
            xmax = x0;
        }
        if (y0 < y1)
        {
            ymin = y0;
            ymax = y1;
        }
        else
        {
            ymin = y1;
            ymax = y0;
        }
    }

    /**
     * Move this line object as a whole by x offset in x-direction and y
     * offset in y-direction.
     * @param x - x offset
     * @param y - y offset
     */
    public void moveobj(int x, int y)
    {
        x1 += x;
        x0 += x;
        y1 += y;
        y0 += y;
        setminmax();
    }

    /**
     * Move the selected point of this line object by x offset in x-direction
     * and y offset in y-direction.
     * @param x - x offset
     * @param y - y offset
     */
    public void movepoint(int x, int y)
    {
        if (select == 2)
        {
            x0 = x0 + x;
            y0 = y0 + y;
        }
        if (select == 3)
        {
            x1 = x1 + x;
            y1 = y1 + y;
        }
        setminmax();
    }

    /**
     * Select the point of this line object which is within close distance to
     * the point of (x,y).
     * @param x - x coordinate of point
     * @param y - y coordinate of point
     * @param xOffset - extent of offset in x axis
     * @param yOffset - extent of offset in y axis
     * @return <code>true</code> if there exists one point on this line
     *         object which is within close distance to the point of (x,y)
     *         <code>false</code> otherwise
     */
    public boolean selectpointWOffset(int x, int y, int xOffset, int yOffset)
    {
        int tempx0 = x0 + xOffset;
        int tempx1 = x1 + xOffset;
        int tempy0 = y0 + yOffset;
        int tempy1 = y1 + yOffset;

        if (x - tempx0 < 3 && x - tempx0 > -3 && y - tempy0 < 3 && y - tempy0 > -3)
        {
            select = 2;
            return (true);
        }
        else if (x - tempx1 < 3 && x - tempx1 > -3 && y - tempy1 < 3 && y - tempy1 > -3)
        {
            select = 3;
            return (true);
        }
        return (false);
    }

    /**
     * Select the point of this line object which is within close distance to
     * the point of (x,y).
     * @param x - x coordinate
     * @param y - y coordinate
     * @return <code>true</code> if there exists one point on this line
     *         object which is within close distance to the point of (x,y)
     *         <code>false</code> otherwise
     */
    public boolean selectpoint(int x, int y)
    {
        if (x - x0 < 3 && x - x0 > -3 && y - y0 < 3 && y - y0 > -3)
        {
            select = 2;
            return (true);
        }
        else if (x - x1 < 3 && x - x1 > -3 && y - y1 < 3 && y - y1 > -3)
        {
            select = 3;
            return (true);
        }
        return (false);
    }

    /**
     * Select this line object as a whole if the point (x,y) is within close
     * distance to this object.
     * @param x - x coordinate of point
     * @param y - y coordinate of point
     * @param xOffset - extent of offset in x axis
     * @param yOffset - extent of offset in y axis
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this line object
     *         <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset)
    {
        double k, dist, x2, y2;

        int tempx0 = x0 + xOffset;
        int tempx1 = x1 + xOffset;
        int tempy0 = y0 + yOffset;
        int tempy1 = y1 + yOffset;

        if (tempx0 == tempx1)
        {
            if (Math.abs(x - tempx0) <= 3 && y <= tempy1 && y >= tempy0 ||
                    Math.abs(x - tempx0) <= 3 && y <= tempy0 && y >= tempy1)
            {
                select = 1;
                return (true);
            }
            return (false);
        }

        k = (tempy1 - tempy0) * 1.0 / (tempx1 - tempx0);

        x2 = (k * k * tempx0 - k * tempy0 + k * y + x) / (k * k + 1);
        y2 = k * (x2 - tempx0) + tempy0;

        if (x2 >= tempx0 && x2 <= tempx1 || x2 >= tempx1 && x2 <= tempx0)
        {
            dist = Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
            if (dist <= 3)
            {
                select = 1;
                return (true);
            }
        }

        return (false);
    }

    /**
     * Select this line object as a whole if the point (x,y) is within close
     * distance to this object.
     * @param x - x coordinate of point
     * @param y - y coordinate of point
     * @return <code>true</code> if the point (x,y) is within close distance
     *         to this line object
     *         <code>false</code> otherwise
     */
    public boolean selectobj(int x, int y)
    {
        double k, dist, x2, y2;

        if (x0 == x1)
        {
            if (Math.abs(x - x0) <= 3 && y <= y1 && y >= y0 ||
                    Math.abs(x - x0) <= 3 && y <= y0 && y >= y1)
            {
                select = 1;
                return (true);
            }
            return (false);
        }

        k = (y1 - y0) * 1.0 / (x1 - x0);

        x2 = (k * k * x0 - k * y0 + k * y + x) / (k * k + 1);
        y2 = k * (x2 - x0) + y0;

        if (x2 >= x0 && x2 <= x1 || x2 >= x1 && x2 <= x0)
        {
            dist = Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
            if (dist <= 3)
            {
                select = 1;
                return (true);
            }
        }

        return (false);
    }

    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<part type=\"line\">\n");
        super.writeXML(bw, x, y,prefix+"\t");
        bw.write(prefix+"\t<color>"+c.getRGB()+"</color>\n");
        bw.write(prefix+"\t<coordinates>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(x0-x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(y0-y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(x1-x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(y1-y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t</coordinates>\n");
        bw.write(prefix+"</part>\n");
    }
    /**
     * Write this line object to the output stream os.
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
            // todo: this write routine should take as a parameter the color
            // this means probably changine all of the rest of
            // GraphicPart_xxx.
            super.write(os, x, y);
            int x0delta, y0delta, x1delta, y1delta;
            x0delta = (x0 - x);
            y0delta = (y0 - y);
            x1delta = (x1 - x);
            y1delta = (y1 - y);
            os.writeObject(c);
            os.writeInt(x0delta);
            os.writeInt(y0delta);
            os.writeInt(x1delta);
            os.writeInt(y1delta);
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart_line write IOException");
            throw new IOException("GraphicPart_line write IOException");
        }
    }

    public void readXML(org.w3c.dom.Node lineNode)
    {
        NodeList lineChildren=lineNode.getChildNodes();
        for(int i=0; i<lineChildren.getLength(); i++)
        {
            org.w3c.dom.Node lineChild=(org.w3c.dom.Node)lineChildren.item(i);
            if(lineChild.getNodeName().equals("bounds"))
                super.readXML(lineChild);
            else if(lineChild.getNodeName().equals("color"))
                c=new Color(Integer.parseInt(SCSUtility.getNodeValue(lineChild)));
            else if(lineChild.getNodeName().equals("coordinates"))
            {
                x0=Integer.MIN_VALUE;
                y0=Integer.MIN_VALUE;
                NodeList coordinatesChildren=lineChild.getChildNodes();
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
                                if(x0==Integer.MIN_VALUE)
                                    x0=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                                else
                                    x1=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                            }
                            else if(coordinateChild.getNodeName().equals("y"))
                            {
                                if(y0==Integer.MIN_VALUE)
                                    y0=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                                else
                                    y1=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Read this line object from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error
     *                                occurred
     */
    public void read(ObjectInputStream os) throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);
            c = (Color) os.readObject();
            x0 = os.readInt();
            y0 = os.readInt();
            x1 = os.readInt();
            y1 = os.readInt();
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:GraphicPart_line read ClassNotFoundException");
            throw new ClassNotFoundException("GraphicPart_line read ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart_line read IOException");
            throw new IOException("GraphicPart_line read IOException");
        }
    }
}
