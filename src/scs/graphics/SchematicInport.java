package scs.graphics;
/* SCCS %W% --- %G% -- %U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * SchematicInport - A class representing the schematic inports of a module's
 * schematic. This is an extended class from GraphicPart, inherited all those
 * graphical attributes, plus some additional attributes special for a schematic
 * inport.
 *
 * @author Weifang Xie, Nitin Gupta, Amanda Alexander
 * @version     %I%, %G%
 *
 * @param       Name        the name of this schematic inport
 * @param       Type        the type of this schematic inport, either Java
 *				native data type, Nsl data type, or NslEnv type
 * @param       Parameters      the parameters of the corresponding variable
 *                              representation of this schematic inport
 * @param       links        the hookup to a connection from this schematic
 *				inport
 * @param    x        an array of x-coordinates of the  vertices
 *				of the box and triangle shape of this schematic inport
 * @param    y        an array of y-coordinates of the vertices
 *				of the box and triangle shape of this schematic inport
 * @param    px0        the x-coordinate of the starting point of the
 *				little connecting arros
schematic inport
 * @param    px1        the real world x-coordinate of the ending point of the
 *				little connecting arrow
schematic inport
 * @param    py0        the y-coordinate of the starting point of the
 *				little connecting arrow 
 * @param    py1        the  y-coordinate of the ending point of the
 *				little connecting arrow
schematic inport
 * @param    port_col        color of this schematic inport -set in Options Frame
 * @param    outline_col        outline color of this schematic inport - set in OptionsFrame
 *				
 * @since JDK1.1
 */

import scs.util.SCSUtility;
import scs.util.UserPref;
import scs.Declaration;

import java.awt.*;
import java.io.*;
import java.util.Vector;


public class SchematicInport extends SchematicPort
{
    public Vector<Connection> links;

    /**
     * Constructor of this class with no parameters.
     */
    public SchematicInport()
    {
        super();
        portDirection = 'L'; //L=left T=top
        links = new Vector<Connection>();
        // todo: change so that it reads the currently set direction
        initSchematicInport();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, and
     * Parameters
     * @param n - name
     * @param t - type
     * @param p - parameters
     * @param p_portDirection
     */
    public SchematicInport(String n, String t, String p, char p_portDirection)
    {
        super(n,t,p,p_portDirection);
        links = new Vector<Connection>();
        // todo: change so that it reads the currently set direction

        initSchematicInport();
    }

    /**
     * Constructor of this class, with initial values of Name, Type, and
     * Parameters
     * @param var - variable declaration
     */
    public SchematicInport(Declaration var)
    {
        super(var);
        links = new Vector<Connection>();
        initSchematicInport();
    }

    private void initSchematicInport()
    {
        select = 0;
        // todo: should be get port color
        switch (portDirection)
        {
            case 'T':    //top to bottom
                // @---   //first line x[0] to x[1] 32 across
                // |  |   // 12 down
                // |  |
                // \  /   // slant 12 down
                //  \/
                //   |
                //   V    // go clockwise from @

                x[0] = 0;
                y[0] = 0;
                x[1] = 32;
                y[1] = 0;
                x[2] = 32;
                y[2] = 12;
                x[3] = 16;
                y[3] = 24;
                x[4] = 0;
                y[4] = 12;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 24;
                px1 = 16;
                py1 = 40;
                xmin = 0;
                ymin = 0;
                xmax = 32;  //does not include little arrow
                ymax = 24;
                break;
            case 'L': // left to right
                //    @----\
                //    |     >->   //go clockwise from @
                //    |----/
                x[0] = 0;
                y[0] = 0;
                x[1] = 12;
                y[1] = 0;
                x[2] = 24;
                y[2] = 16;
                x[3] = 12;
                y[3] = 32;
                x[4] = 0;
                y[4] = 32;
                // corridinates of little connecting arrow
                px0 = 24;
                py0 = 16;
                px1 = 40;
                py1 = 16;
                xmin = 0;
                ymin = 0;
                xmax = 40;
                ymax = 32;
                break;

            case 'R': // right to left
                //     /----|
                // <- <     |          //go clockwise
                //     \----@
                x[0] = 40; //(16+24=40)
                y[0] = 32;
                x[1] = 28; //40-12
                y[1] = 32;
                x[2] = 16;
                y[2] = 16;
                x[3] = 28; //16+12=28
                y[3] = 0;
                x[4] = 40;
                y[4] = 0;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 16;
                px1 = 0;
                py1 = 16;
                xmin = 0;
                ymin = 0;
                xmax = 40;
                ymax = 32;
                break;
            case 'B':    //bottom to top
                //        ^
                //        |
                //        ^
                //	     / \
                //	    |   |   //go clockwise from @
                //	    @----
                x[0] = 0;
                y[0] = 40;
                x[1] = 0;
                y[1] = 28; //40-12
                x[2] = 16;
                y[2] = 16;  //28-12 =16
                x[3] = 32;
                y[3] = 28;
                x[4] = 32;
                y[4] = 40;
                // corridinates of little connecting arrow
                px0 = 16;
                py0 = 16;
                px1 = 16;
                py1 = 0;
                xmin = 0;
                ymin = 0;
                xmax = 32;  //does not include little arrow
                ymax = 40;
                break;

            default:
                System.err.println("Error:SchematicInport:Not a port direction.");
        }
    }

    /**
     * Paint this schematic inport.
     */
    public void paint(Graphics g)
    {
        Polygon p;
        int xx, yy;
        double dist;

        p = new Polygon();
        for (int i = 0; i < 5; i++)
        {
            p.addPoint(x[i], y[i]);
        }
        g.setColor(UserPref.inPortFill_col);
        g.fillPolygon(p);
        // fill Polygon

        g.setColor(UserPref.inPin_col);
        for (int i = 0; i < 5; i++)
        {
            g.drawLine(x[i], y[i], x[(i + 1) % 5], y[(i + 1) % 5]);
        }
        // draw border

        // todo: change to get font and size
        Font font = UserPref.instanceTextFont;
        g.setFont(font);
        FontMetrics fontmetrics = getFontMetrics(font);

        g.setColor(UserPref.instanceText_col);
        //g.drawString(Name, (xmin+xmax)/2-fontmetrics.stringWidth(Name)/2,
        //             (ymin+ymax)/2-8+fontmetrics.getHeight()/2);
        // This should be ymin - 1 grid.
        // todo: switch this to a GraphicPart_text and allow locations of
        // CENTER, BELOW, ABOVE, LEFT, RIGHT
        switch (portDirection)
        {
            case 'T': //top to bottom : put text at top center
                tx0 = ((xmin + xmax) >> 1) - (fontmetrics.stringWidth(Name) >> 1);
                ty0 = (ymin - SCSUtility.grid);
                g.drawString(Name, tx0, ty0);
                break;
            case 'L': //left to right : put text at bottom left justified
                tx0 = xmin;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;
            case 'B': //bottom to top: put text at bottom center
                tx0 = ((xmin + xmax) >> 1) - (fontmetrics.stringWidth(Name) >> 1);
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;
            case 'R': //right to left : put text at bottom left justified
                tx0 = xmin;
                ty0 = (ymax + SCSUtility.grid + fontmetrics.getHeight());
                g.drawString(Name, tx0, ty0);
                break;

            default:
                System.err.println("Error: SchematicOutport: not a port direction.");
        }

        // draw little connecting arrow
        // todo: again this should make a get color call from the defaults

        g.setColor(UserPref.inPin_col);
        g.drawLine(px0, py0, px1, py1);
        p = new Polygon();
        p.addPoint(px1, py1);
        dist = Math.sqrt(Math.pow(px1 - px0, 2) + Math.pow(py1 - py0, 2));

        if (dist > 10)
        {
            xx = (int) (px1 + (-10 * (px1 - px0) + 3 * (py1 - py0)) / dist);
            yy = (int) (py1 + (-10 * (py1 - py0) - 3 * (px1 - px0)) / dist);
            p.addPoint(xx, yy);

            xx = (int) (px1 + (-10 * (px1 - px0) - 3 * (py1 - py0)) / dist);
            yy = (int) (py1 + (-10 * (py1 - py0) + 3 * (px1 - px0)) / dist);
            p.addPoint(xx, yy);
        }
        else
        {
            xx = (int) (px1 + (-0.8 * (px1 - px0) + 0.24 * (py1 - py0)));
            yy = (int) (py1 + (-0.8 * (py1 - py0) - 0.24 * (px1 - px0)));
            p.addPoint(xx, yy);

            xx = (int) (px1 + (-0.8 * (px1 - px0) - 0.24 * (py1 - py0)));
            yy = (int) (py1 + (-0.8 * (py1 - py0) + 0.24 * (px1 - px0)));
            p.addPoint(xx, yy);
        }
        g.fillPolygon(p);
        // draw arrow

        if (select != 0)
        {
            g.setColor(UserPref.highlight_col);
            g.fillRect(xmin - 2, ymin - 2, 4, 4);
            g.fillRect(xmin - 2, ymax - 2, 4, 4);
            g.fillRect(xmax - 2, ymin - 2, 4, 4);
            g.fillRect(xmax - 2, ymax - 2, 4, 4);
        }
        // draw handles when selected
    }
//--------------------------------------------

    /**
     * Move this schematic inport as a whole by xx offset in x-direction and yy
     * offset in y-direction.
     *
     * @param xx the moving offset in x-direction
     * @param yy the moving offset in y-direction
     */
    public void moveobj(int xx, int yy)
    {
        int i;
        Connection conn;

        for (i = 0; i < 5; i++)
        {
            x[i] += xx;
            y[i] += yy;
        }
        px0 += xx;
        px1 += xx;
        py0 += yy;
        py1 += yy;
        setminmax();

        for (i = 0; i < links.size(); i++)
        {
            conn = links.elementAt(i);
            conn.movesrc(xx, yy);
        }
    }



    /**
     * Connect this schematic inport to a connection.
     * @conn - connection to connect to
     */
    public void connect(Connection conn)
    {
        links.addElement(conn);
    }

    /**
     * Disconnect this schematic inport from connection among all of its
     * connected connections.
     * @param conn - connection to disconnect from
     */
    public void disconnect(Connection conn)
    {
        links.removeElement(conn);
    }
//------------------------------------------------------------
    /**
     * Write this schematic port to the output stream os.
     *
     * @throws java.io.IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os) throws IOException
    {
        try
        {
            super.write(os);
        }
        catch (IOException e)
        {
            System.err.println("SchematicInort:write IOException");
            throw new IOException("SchematicInort write IOException");
        }
    }
//------------------------------------------------------------

    /**
     * WriteAllChars this schematic inport to the output stream os.
     * @param bw - BufferedWriter to write to
     */
    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {
        bw.write(prefix+"<inport>\n");
        super.writeXML(bw, prefix+"\t");
        bw.write(prefix+"</inport>\n");
    }

    public void readXML(org.w3c.dom.Node portNode)
    {
        super.readXML(portNode);
    }
    
    //------------------------------------------------------------------
    /**
     * Read this schematic inport from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *						occurred
     */
    public void read(ObjectInputStream os) throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);

            if (links.size() != 0)
            {
                links.removeAllElements();
            }
        }
        catch (IOException e)
        {
            throw new IOException("SchematicInport read IOException");
        }
    }

    //-----------------------
} //end class SchematicInport



