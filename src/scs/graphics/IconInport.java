package scs.graphics;/* SCCS  %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconInport - A class representing the icon inports of a module's icon.
 * This is an extended class from GraphicPart_line, inherited all those graphical
 * attributes, plus some additional attributes special for an icon inport.
 *
 * @author Weifang Xie
 * @version 1.1, 03/15/99
 *
 */

import scs.Declaration;
import scs.util.UserPref;

import java.awt.*;
import java.io.*;

public class IconInport extends IconPort
{
    public Connection link = null;

    /**
     * Constructor of this class with no parameters.
     */
    public IconInport()
    {
        link = null;
        direction = 'L';
        signalType = 'E';
    }

    /**
     * Constructor of this class, with initial values of Name, Type, parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon inports, and color being set to n, t, p, xx0, yy0, and cc.
     * Note: xx1 and yy1 now represent the point with the arrow
     *
     * @param n            the name of this icon inport
     * @param t            the type of this icon inport, either Java native data
     *                     type, Nsl data type, or NslEnv type
     * @param params       the parameters of the corresponding variable
     *                     representation of this icon inport
     * @param p_dir        direction of the icon port
     * @param p_signalType excitatory or inhibitory. E or I
     * @since JDK1.1
     */
    public IconInport(String n, String t, String params, char p_dir, char p_signalType, int xx1, int yy1, Color cc)
    {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.
        super(n, t, params, p_dir, p_signalType, xx1, yy1, cc);
        direction = 'L';
        signalType = 'E';
        calcX0Y0(p_dir, xx1, yy1);
    }

    /**
     * Constructor of this class, with initial values of var of type declaration,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon inports, and color being set with cc.
     *
     * @since JDK1.2
     */
    // Note: xx1 and yy1 now represent the point with the arrow */
    public IconInport(Declaration var, int xx1, int yy1, Color cc)
    {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.
        super(var, xx1, yy1, cc);
        direction = 'L';
        signalType = 'E';
        calcX0Y0(direction, xx1, yy1);
    }

    //----------------------------------------------------------
    protected void calcX0Y0(char direction, int xx1, int yy1)
    {
        x1 = xx1;
        y1 = yy1;

        //left to right
        if (direction == 'L')
        {
            x0 = xx1 - scs.graphics.Icon.pinLength;
            y0 = yy1;
        }
        //top to bottom
        if (direction == 'T')
        {
            x0 = xx1;
            y0 = yy1 - scs.graphics.Icon.pinLength;
        }
        //bottom to top
        if (direction == 'B')
        {
            x0 = xx1;
            y0 = yy1 + scs.graphics.Icon.pinLength;
        }
        //right to left
        if (direction == 'R')
        {
            x0 = xx1 + scs.graphics.Icon.pinLength;
            y0 = yy1;
        }
        // call GraphicPart_line's setminmax
        setminmax();
    }
    //----------------------------------------------------------


    //----------------------------------------------------------
    // paint the inhibitory type input port
    public void paint_inhibit(Graphics g)
    {
        g.setColor(UserPref.inPin_col);
        g.drawLine(x0, y0, x1, y1);

        g.setColor(UserPref.inPin_col);

        if (y0 == y1)
        {
            g.drawOval((x1 - 4), y0, 8, 8);
        }
        else
        {
            g.drawOval(x0, ((y0 + y1) >> 1), 8, 8);
        }
    }

    //----------------------------------------------------------
    /**
     * Paint this icon inport.
     */
    public void paint(Graphics g)
    {
        int x, y;
        double dist;
        Polygon p = new Polygon();

        if (signalType == 'I')
        {
            // paint a circle at input
            paint_inhibit(g);
            return;
        }

        g.setColor(UserPref.inPin_col);

        g.drawLine(x0, y0, x1, y1);

        if (x0 == x1)
        {
            g.drawLine(x0 + 1, y0, x1 + 1, y1);
        }

        if (y0 == y1)
        {
            g.drawLine(x0, y0 - 1, x1, y1 - 1);
        }

        // I think this must be for the arrow
        p.addPoint(x1, y1);
        dist = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
        if (dist > 10)
        {
            x = (int) (x1 + (-10 * (x1 - x0) + 3 * (y1 - y0)) / dist);
            y = (int) (y1 + (-10 * (y1 - y0) - 3 * (x1 - x0)) / dist);
            p.addPoint(x, y);

            x = (int) (x1 + (-10 * (x1 - x0) - 3 * (y1 - y0)) / dist);
            y = (int) (y1 + (-10 * (y1 - y0) + 3 * (x1 - x0)) / dist);
            p.addPoint(x, y);
        }
        else
        {
            x = (int) (x1 + (-0.8 * (x1 - x0) + 0.24 * (y1 - y0)));
            y = (int) (y1 + (-0.8 * (y1 - y0) - 0.24 * (x1 - x0)));
            p.addPoint(x, y);

            x = (int) (x1 + (-0.8 * (x1 - x0) - 0.24 * (y1 - y0)));
            y = (int) (y1 + (-0.8 * (y1 - y0) + 0.24 * (x1 - x0)));
            p.addPoint(x, y);
        }

        g.fillPolygon(p);
    }

    //----------------------------------------------------------
    public void paint_vertical_line(Graphics g)
    {
        if (signalType == 'I')
        {
            // paint a circle at input
            paint_inhibit(g);
            return;
        }

        g.setColor(UserPref.inPin_col);
        g.drawLine(x0, y0, x1, y1);

        // for arrow?
        if (y0 == y1)
        {
            g.drawLine(((x0 + x1) >> 1), y0 + 5, ((x0 + x1) >> 1), y0 - 5);
        }

        else
        {
            g.drawLine(x0 - 5, ((y0 + y1) >> 1), x0 + 5, ((y0 + y1) >> 1));
        }

    }

    public void paintLabel(Graphics g)
    {
        if (select != -1)
        {
            g.setColor(UserPref.inPin_col);
            g.fillRect(x0 - 2, y0 - 2, 4, 4);
            g.fillRect(x1 - 2, y1 - 2, 4, 4);
        }
        super.paintLabel(g);
    }
    //----------------------------------------------------------
    /**
     * Connect this icon inport to a connection "conn".
     */
    public void connect(Connection conn)
    {
        link = conn;
    }

    //----------------------------------------------------------
    /**
     * Disconnect this icon inport from its connected connection.
     */
    public void disconnect()
    {
        link = null;
    }

    //-----------------------------------------------------
    /**
     * Write this icon inport to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException
    {
        try
        {
            super.write(os, x, y);
            //todo: why don't we write out the link?
        }
        catch (IOException e)
        {
            System.err.println("IconInport write IOException");
            throw new IOException("IconInport write IOException");
        }
    }

    //-----------------------------------------------------
    /**
     * Write this icon inport to the output stream os.
     *
     * @exception IOException     if an IO error occurred
     */
    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<part type=\""+Type+"\">\n");
        super.writeXML(bw, x, y,prefix+"\t");
        bw.write(prefix+"</part>\n");
    }

    public void readXML(org.w3c.dom.Node portNode)
    {
        super.readXML(portNode);
    }
    //----------------------------------------------------------------
    /**
     * Read this icon inport from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);
            //todo: why don't we read in the link?
        }
        catch (IOException e)
        {
            System.err.println("IconInport read Name&Type IOException");
            throw new IOException("IconInport read Name&Type IOException");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("IconInport read Name&TypeClassNotFoundException");
            throw new ClassNotFoundException("IconInport read&Type ClassNotFoundException");
        }

    } //end read

}//end class IconInport
