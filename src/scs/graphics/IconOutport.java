package scs.graphics;
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * IconOutport - A class representing the icon outports or output pins
 * of a module's icon. This is an extended class from GraphicPart_line,
 * inherited all those graphical
 * attributes, plus some additional attributes special for an icon outport.
 * One new addition, is the index number of the instance of the icon
 * that this pin is attached to. (00/05/16 aa).
 *
 * @author Weifang Xie, Alexander
 * @version     %I%, %G%
 *
 * @param       Name    the name of this icon outport
 * @param       Type    the type of this icon outport, either Java native data
 *                      type, Nsl data type, or NslEnv type
 * @param       Parameters      the parameters of the corresponding variable
 *                              representation of this icon outport
 * @param       link    the hookup to a connection to this icon outport
 *
 * @since JDK1.1
 */

import scs.Declaration;
import scs.util.UserPref;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.util.Vector;

public class IconOutport extends IconPort
{
    public Vector<Connection> links;

    /**
     * Constructor of this class with no parameters.
     */
    public IconOutport()
    {
        links = new Vector<Connection>();
        direction = 'L';
        signalType = 'O';
    }

    /**
     * Constructor of this class, with initial values of Name, Type, Parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon outports, and color being set to n, t, p, xx0, yy0, and cc.
     *
     * @param n the name of this icon outport
     * @param t the type of this icon outport, either Java native data type, Nsl data type, or NslEnv type
     * @param p the parameters of the corresponding variable representation of this icon outport
     * @param p_dir - direction of the port
     * @param signalType - type of signal
     * @param xx0 - x coordinate
     * @param yy0 - y coordinate
     * @param cc - port color
     * @since JDK1.1
     */
    public IconOutport(String n, String t, String p, char p_dir, char signalType, int xx0, int yy0, Color cc)
    {
        super(n, t, p, p_dir, signalType, xx0, yy0, cc);
        direction = 'L';
        signalType = 'O';
        calcX1Y1(direction, xx0, yy0);
    }

    /**
     * Constructor of this class, with initial values of Name, Type, Parameters,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon outports, and color being set to n, t, p, xx0, yy0, and cc.
     *
     * @param var Declaration variable
     * @param xx0 - x coordinate
     * @param yy0 - y coordinate
     * @param cc - color of the port
     * @since JDK1.1
     */
    public IconOutport(Declaration var, int xx0, int yy0, Color cc)
    {
        super(var, xx0, yy0, cc);
        direction = 'L';
        signalType = 'O';
        calcX1Y1(direction, xx0, yy0);
    }

    protected void calcX1Y1(char direction, int xx0, int yy0)
    {
        x0 = xx0;
        y0 = yy0;

        if (direction == 'L')
        {
            x1 = xx0 + scs.graphics.Icon.pinLength;
            y1 = yy0;
        }
        if (direction == 'T')
        {
            x1 = xx0;
            y1 = yy0 + scs.graphics.Icon.pinLength;
        }
        if (direction == 'B')
        {
            x1 = xx0;
            y1 = yy0 - scs.graphics.Icon.pinLength;
            if (y1 < 0)
            {
                // todo: throw exception
                System.out.println("Error: IconOutport: y1 less than 0 ");
            }
        }
        if (direction == 'R')
        {
            x1 = xx0 - scs.graphics.Icon.pinLength;
            y1 = yy0;
            if (x1 < 0)
            {
                // todo: throw exception
                System.out.println("Error: IconOurport: x1 less than 0 ");
            }
        }
        // call GraphicPart_lines setminmax
        setminmax();
    }

    /**
     * Paint this icon outport.
     * @param g - Graphics object to write to
     */
    public void paint_arrow(Graphics g)
    {
        g.setColor(UserPref.outPin_col);
        g.drawLine(x0, y0, x1, y1);
        Polygon p = new Polygon();
        int x, y;
        double dist;

        g.setColor(UserPref.outPin_col);

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

    /**
     * Paint this icon outport.
     * @param g - Graphics object to paint to
     */
    public void paint(Graphics g)
    {

        if (UserPref.output_port_shape.equals("CROSS"))
        {
            paint_cross(g);
            return;
        }

        if (UserPref.output_port_shape.equals("ARROW"))
        {
            paint_arrow(g);
            return;
        }

        g.setColor(UserPref.outPin_col);

        g.drawLine(x0, y0, x1, y1);

        if (x0 == x1)
        {
            g.drawLine(x0 + 1, y0, x1 + 1, y1);
        }
        if (y0 == y1)
        {
            g.drawLine(x0, y0 - 1, x1, y1 - 1);
        }
    }

    /**
     * Paint this icon outport.
     * @param g - Graphics object to write to
     */
    public void paint_cross(Graphics g)
    {
        g.setColor(UserPref.outPin_col);
        g.drawLine(x0, y0, x1, y1);
        g.setColor(UserPref.outPin_col);

        if (y0 == y1)
        {
            g.drawLine(((x0 + x1) >> 1) - 5, y0 - 5, ((x0 + x1) >> 1) + 5, y0 + 5);
            g.drawLine(((x0 + x1) >> 1) + 5, y0 - 5, ((x0 + x1) >> 1) - 5, y0 + 5);
        }
        else
        {
            g.drawLine(x0 - 5, ((y0 + y1) >> 1) - 5, x0 + 5, ((y0 + y1) >> 1) + 5);
            g.drawLine(x0 - 5, ((y0 + y1) >> 1) + 5, x0 + 5, ((y0 + y1) >> 1) - 5);

        }
    }

    /**
     * Connect this icon outport to a connection "conn".
     * @param conn - Connection to connect to
     */
    public void connect(Connection conn)
    {
        links.addElement(conn);
    }

    /**
     * Disconnect this icon outport from its connected connection.
     * @param conn - Connection to disconnect from
     */
    public void disconnect(Connection conn)
    {
        links.removeElement(conn);
    }

    /**
     * Write this icon outport to the output stream os.
     * @param os - ObjectOutputStream to write to
     * @param x - x coordinate
     * @param y - y coordinate 
     * @throws IOException if an IO error occurred
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
            System.out.println("IconOutport write IOException");
            throw new IOException("IconOutport write IOException");
        }
    }

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
    
    /**
     * Read this icon outport from the input stream os.
     * @param os - ObjectInputStream to read from
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);
            //todo: why don't we read in the link?
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("IconOutport read ClassNotFoundException");
            throw new ClassNotFoundException("IconOutport read ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.out.println("IconOutport read IOException");
            throw new IOException("IconOutport read IOException");
        }
    }
}
