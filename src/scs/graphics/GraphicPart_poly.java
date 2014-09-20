package scs.graphics;/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart_poly - A class representing graphic objects which have a polygon
 * shape.
 *
 * @author Weifang Xie
 * @version     %I%, %G%
 *
 * @param       c       color of this graphic object
 * @param       n       how many vertices are there in this polygon
 * @param       x       an array of x-coordinates of the vertices of this 
 *			polygon
 * @param       y       an array of y-coordinates of the vertices of this 
 *			polygon
 *
 * @since JDK1.1
 */

import org.w3c.dom.NodeList;

import java.awt.*;
import java.io.*;

import scs.util.SCSUtility;


public class GraphicPart_poly extends GraphicPart
{
    Color c;
    int n = 0;
    int x[] = new int[64];
    int y[] = new int[64];

    /**
     * Constructor of this class with no parameters.
     */
    GraphicPart_poly()
    {
    }

    /**
     * Constructor of this class, setting the initial x value of the first two
     * vertices of this polygon to be xx0, the initial y value of the first two
     * vertices of this polygon to be yy0, the number of vertices to be 2, the
     * select flag to be 3, and the color c to be cc.
     */
    public GraphicPart_poly(int xx0, int yy0, Color cc)
    {
        x[0] = x[1] = xmin = xmax = xx0;
        y[0] = y[1] = ymin = ymax = yy0;
        n = 2;
        select = 3;
        c = cc;
    }

    public GraphicPart_poly clone() throws CloneNotSupportedException
    {
        GraphicPart_poly clone=null;
        try
        {
            clone = (GraphicPart_poly)super.clone();
        }
        catch(Exception e)
        {
            clone=new GraphicPart_poly();
        }
        finally
        {
            if(clone==null)
                clone=new GraphicPart_poly();
            clone.c=c;
            clone.n=n;
            clone.x=new int[64];
            clone.y=new int[64];
            System.arraycopy(this.x, 0, clone.x, 0, 64);
            System.arraycopy(this.y, 0, clone.y, 0, 64);
        }
        return clone;
    }

    /**
     * Set the color of this polygon object to be cc.
     */
    public void setcolor(Color cc)
    {
        c = cc;
    }

    /**
     * Paint this polygon object with offset.
     */
    public void paint(Graphics g, int xOffset, int yOffset)
    {
        int[] xS = new int[n];
        int[] yS = new int[n];

        for (int i = 0; i < n; i++)
        {
            xS[i] = x[i] + xOffset;
            yS[i] = y[i] + yOffset;
        }

        g.setColor(c);
        Polygon p = new Polygon();
        for (int i = 0; i < n; i++)
        {
            p.addPoint(xS[i], yS[i]);
        }
        g.fillPolygon(p);

        g.setColor(Color.black);
        for (int i = 0; i < n; i++)
        {
            g.drawLine(xS[i], yS[i], xS[(i + 1) % n], yS[(i + 1) % n]);
        }

        if (select != 0)
        {
            g.setColor(Color.red);
            for (int i = 0; i < n; i++)
            {
                g.fillRect(xS[i] - 2, yS[i] - 2, 4, 4);
            }
        }
    }

    /**
     * Paint this oval object for icon canvas.
     */
    public void paint(Graphics g)
    {
        paint(g, 0, 0);
    }

    /**
     * Calculate the xmin, ymin, xmax, ymax values of this polygon object.
     */
    public void setminmax()
    {
        xmin = 1000;
        ymin = 1000;
        xmax = -1000;
        ymax = -1000;
        for (int i = 0; i < n; i++)
        {
            if (x[i] < xmin) xmin = x[i];
            if (y[i] < ymin) ymin = y[i];
            if (x[i] > xmax) xmax = x[i];
            if (y[i] > ymax) ymax = y[i];
        }
    }

    /**
     * Move this polygon object as a whole by xx offset in x-direction and yy
     * offset in y-direction.
     */
    public void moveobj(int xx, int yy)
    {
        for (int i = 0; i < n; i++)
        {
            x[i] += xx;
            y[i] += yy;
        }
        setminmax();
    }

    /**
     * Move the selected point of this oval object by xx offset in x-direction
     * and yy offset in y-direction.
     */
    public void movepoint(int xx, int yy)
    {
        if (select > 1)
        {
            x[select - 2] += xx;
            y[select - 2] += yy;
        }
        setminmax();
    }

    /**
     * Select the point of this polygon object which is within close distance to
     * the point of (xx,yy).
     *
     * @return <code>true</code> if there exists one point among the vertices
     *         of this polygon object which is within close distance to the
     *         point of (xx,yy)
     *         <code>false</code> otherwise
     */
    public boolean selectpoint(int xx, int yy)
    {
        for (int i = 0; i < n; i++)
        {
            if (xx - x[i] < 3 && xx - x[i] > -3 && yy - y[i] < 3 && yy - y[i] > -3)
            {
                select = i + 2;
                return (true);
            }
        }
        return (false);
    }

    /**
     * Select this polygon object as a whole if the point (xx,yy) is within
     * close distance to this object.
     *
     * @return <code>true</code> if the point (xx,yy) is within close distance
     *         to this polygon object
     *         <code>false</code> otherwise
     */
    public boolean selectobjWOffset(int xx, int yy, int xOffset, int yOffset)
    {
        int tempxmin = xmin + xOffset;
        int tempymin = ymin + yOffset;
        int tempxmax = xmax + xOffset;
        int tempymax = ymax + yOffset;
        if (xx >= tempxmin && xx <= tempxmax && yy >= tempymin && yy <= tempymax)
        {
            select = 1;
            return (true);
        }
        return (false);
    }

    /**
     * Select this polygon object as a whole if the point (xx,yy) is within
     * close distance to this object.
     *
     * @return <code>true</code> if the point (xx,yy) is within close distance
     *         to this polygon object
     *         <code>false</code> otherwise
     */
    public boolean selectobj(int xx, int yy)
    {
        if (xx >= xmin && xx <= xmax && yy >= ymin && yy <= ymax)
        {
            select = 1;
            return (true);
        }
        return (false);
    }

    /**
     * Add a point (xx,yy) as the last vertex of this polygon object.
     */
    public void addpoint(int xx, int yy)
    {
        x[n] = xx;
        y[n] = yy;
        select = n + 2;
        n++;
        setminmax();
    }

    /**
     * Check whether this polygon object has been finished building or not, so
     * that the state of the object can be changed from highlighted, which means
     * still under construction, to un-highlighted, which means finished.
     *
     * @return    <code>true</code> if finished building
     * <code>false</code> otherwise
     */
    public boolean done()
    {
        if (x[n - 1] == x[n - 2] && y[n - 1] == y[n - 2])
        {
            n--;
            return (true);
        }
        return (false);
    }
//-----------------------------------------------------------------------

    /**
     * Write this polygon object to the output stream os.
     *
     * @param os the output stream to be written to
     * @param x0 the x-coordinate of the reference point
     * @param y0 the y-coordinate of the reference point
     * @throws IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x0, int y0)
            throws IOException
    {
        int i;

        try
        {
            super.write(os, x0, y0);
            os.writeObject(c);
            os.writeInt(n);

            for (i = 0; i < n; i++)
            {
                os.writeInt(x[i] - x0);
                os.writeInt(y[i] - y0);
            }
        }
        catch (IOException e)
        {
            //System.out.println("GraphicPart_poly write IOException");
            throw new IOException("GraphicPart_poly write IOException");
        }
    }
//-----------------------------------------------------------------------

    /**
     * WriteAllChars this polygon object to the output stream os.
     *
     * @param bw the print writer to be written to
     * @param x0 the x-coordinate of the reference point
     * @param y0 the y-coordinate of the reference point
     */
    public void writeXML(BufferedWriter bw, int x0, int y0, String prefix) throws IOException
    {
        bw.write(prefix+"<part type=\"poly\">\n");
        super.writeXML(bw, x0, y0,prefix+"\t");
        bw.write(prefix+"\t<color>"+c.getRGB()+"</color>\n");
        bw.write(prefix+"\t<coordinates number=\""+n+"\">\n");
        for(int i=0; i<n; i++)
        {
            bw.write(prefix+"\t\t<coordinate>\n");
            bw.write(prefix+"\t\t\t<x>"+(x[i]-x0)+"</x>\n");
            bw.write(prefix+"\t\t\t<y>"+(y[i]-y0)+"</y>\n");
            bw.write(prefix+"\t\t</coordinate>\n");
        }
        bw.write(prefix+"\t</coordinates>\n");
        bw.write(prefix+"</part>\n");
    }
//-----------------------------------------------------------------------

    public void readXML(org.w3c.dom.Node polyNode)
    {
        NodeList polyChildren=polyNode.getChildNodes();
        for(int i=0; i<polyChildren.getLength(); i++)
        {
            org.w3c.dom.Node polyChild=(org.w3c.dom.Node)polyChildren.item(i);
            if(polyChild.getNodeName().equals("bounds"))
                super.readXML(polyChild);
            else if(polyChild.getNodeName().equals("color"))
                c=new Color(Integer.parseInt(SCSUtility.getNodeValue(polyChild)));
            else if(polyChild.getNodeName().equals("coordinates"))
            {
                int n=Integer.parseInt(polyChild.getAttributes().getNamedItem("number").getNodeValue());
                x=new int[n];
                y=new int[n];
                int idx=0;
                NodeList coordinatesChildren=polyChild.getChildNodes();
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
                                x[idx]=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                            else if(coordinateChild.getNodeName().equals("y"))
                                y[idx]=Integer.parseInt(SCSUtility.getNodeValue(coordinateChild));
                        }
                        idx++;
                    }
                }
            }
        }
    }

    /**
     * Read this polygon object from the input stream os.
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
            n = os.readInt();

            for (int i = 0; i < n; i++)
            {
                x[i] = os.readInt();
                y[i] = os.readInt();
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassNotFoundException("GrphicObj_poly read ClassNotFoundException");
        }
        catch (IOException e)
        {
            throw new IOException("GraphicPart_poly read IOException");
        }
    }
}
