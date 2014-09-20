package scs.graphics;/* SCCS  %W%---%G%--%U% */
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.

/**
 * GraphicPart_text - A class representing text included in the icon or
 * schematic
 * 
 * @author nitgupta
 * @version  %I%, %G%
 *
 * @param       x0      x-coordinate of the starting corner of the smallest 
 *			surrounding rectangle border of this text string
 * @param       y0      y-coordinate of the starting corner of the smallest 
 *			surrounding rectangle border of this text string
 * @param       x1      x-coordinate of the ending corner of the smallest 
 *			surrounding rectangle border of this oval 
 * @param       y1      y-coordinate of the ending corner of the smallest 
 *			surrounding rectangle border of this oval 

 * @param     size    size of the text
 * @param     textString   text of the string
 * @param       c       color of this graphic object
 * @param     font    font used for the text
 *
 * @since JDK1.1
 */

import scs.util.SCSUtility;
import scs.util.UserPref;

import java.awt.Graphics2D;
import java.awt.*;
import java.io.*;
import java.util.StringTokenizer;

import org.w3c.dom.NodeList;

public class GraphicPart_text extends GraphicPart implements Cloneable
{
    int x0, y0, x1, y1;
    int lineBuffer=5;
    String textString;
    int size;
    Color c = UserPref.freeText_col; //this could be module or instance though
    Font font = UserPref.freeTextFont;
    public boolean hasBackground = false;
    

//------------------------------------------------------------------------

    /**
     * Constructor of this class with no parameters.
     */
    public GraphicPart_text()
    {
    }
//---------------------------------------------------

    public GraphicPart_text clone() throws CloneNotSupportedException
    {
        GraphicPart_text clone = null;
        try
        {
            clone = (GraphicPart_text) super.clone();
        }
        catch (Exception e)
        {
            clone = new GraphicPart_text();
            e.printStackTrace();
        }
        finally
        {
            if(clone==null)
                clone=new GraphicPart_text();
            clone.x0 = this.x0;
            clone.y0 = this.y0;
            clone.x1 = this.x1;
            clone.y1 = this.y1;
            clone.textString = this.textString;
            clone.size = this.size;
        }
        return clone;
    }

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc.
     * font to p_font , size to p_size
     * Note: the text location starts in the bottom left corner - not the upper left
     * so this function makes it so the two points coming in are assumed
     * to be xmin and ymin.
     */

    public GraphicPart_text(int px0, int py0, String p_text, Font p_font, Color p_color, int p_size)
    {
        initText(px0, py0, p_text, p_font, p_color, p_size);
    }

//------------------------------------------------------------------------

    /**
     * Constructor of this class, setting the initial value of x0 and x1 to be
     * xx0, the initial value of y0 and y1 to be yy0, and the color c to be cc,
     * font to p_font , size to p_size .
     * Note: this constructor will center the text in the rectangle you give it
     * if location is "CENTER",etc for
     * RIGHT,LEFT,ABOVE,BELOW .
     */

    GraphicPart_text(String location, int pxmin, int pymin, int pxmax, int pymax, String p_text, Font p_font, Color p_color, int p_size)
    {
        initText(location, pxmin, pymin, pxmax, pymax, p_text, p_font, p_color, p_size);
    }
//------------------------------------------------------------------------

    /**
     * Initialization of this class, setting the value of x0 and x1 to be
     * a function of the input based on location,
     * the value of y0 and y1 to be a function of the input based on location
     * , and the color c to be cc,
     * font to p_font , size to p_size .
     * Note: this constructor will center the text in the rectangle you give it
     * if location is "CENTER",etc for
     * RIGHT,LEFT,ABOVE,BELOW .
     * note: this method is also called repeatedly for the module label.
     */

    public void initText(String location, int pxmin, int pymin, int pxmax, int pymax, String p_text, Font p_font, Color p_color, int p_size)
    {
        FontMetrics fm = getFontMetrics(p_font);
        int locationX = pxmin + Math.round(((pxmax - pxmin) >> 1)); //center of box
        int locationY = pymin + Math.round(((pymax - pymin) >> 1)); //center of box

        if (p_text == null || p_text.length()==0)
        {
            System.err.println("Error:GraphicPart_text:initText:string is null!");
            //todo: throw an exception
            return;
        }
        c = p_color;
        size = p_size;
        font = p_font;
        textString = p_text;

        int wD2 = Math.round((fm.stringWidth(p_text) >> 1));
        int hD2 = Math.round((fm.getHeight() >> 1));

        // where is the center of the text going
        if (location.equals("ABOVE"))
        {
            locationY = pymin - hD2 - SCSUtility.gridD2;
        }
        else if (location.equals("BELOW"))
        {
            locationY = pymax + hD2 + SCSUtility.gridD2;
        }
        else if (location.equals("LEFT"))
        {
            locationX = pxmin - wD2 - SCSUtility.gridD2;
        }
        else if (location.equals("RIGHT"))
        {
            locationX = pxmax + wD2 + SCSUtility.gridD2;
        }
        else if(!location.equals("CENTER"))
        {
            System.err.println("Error:GraphicPart_text:initText: incorrect justification given: " + location + '.');
            System.err.println("Error:GraphicPart_text:initText: using CENTER.");
        }

        //-----
        //locationX and locationY are the center of the text string
        // and  x0 and y0 are the upper left corner of the text string.
        x0 = xmin = locationX - wD2;  //upper left
        x1 = xmax = locationX + wD2;
        ymin = y0 = locationY - hD2;  //upper left
        ymax = y1 = locationY + hD2;
    }

//---------------------------------------------------

    /**
     * Initializion of this class, setting the initial value of x0 to be the
     * inputX and the initial value of y0 to be the inputY minus the height
     * of the text. (Input is left justified and lower)
     * Note: the input defines a location at the lower left, and this method
     * calculate where x0, y0 should then be at the upper left.
     */
    public void initText(int px0, int py0, String p_text, Font p_font, Color p_color, int p_size)
    {
        c = p_color;
        size = p_size;
        font = p_font;
        textString = p_text;

        x0 = xmin = px0;
        y1 = py0;
        ymax = y1;

        resize();
    }
    //-----------------------------------------------------

    /**
     * Set the color of this text object to be cc.
     */
    public void setcolor(Color cc)
    {
        c = cc;
    }

    /**
     * Set the font of this text object to be p_font.
     */
    public void setfont(Font p_font)
    {
        font = p_font;
    }

    public void settext(String p_text)
    {
        textString = p_text;
        resize();
    }

    private void resize()
    {
        FontMetrics fm = getFontMetrics(font);
        StringTokenizer lines=new StringTokenizer(textString,"\n");
        int height=lines.countTokens()*(fm.getHeight()+lineBuffer);
        int maxWidth=0;
        while(lines.hasMoreTokens())
        {
            String token=lines.nextToken();
            int width=fm.stringWidth(token);
            if(width>maxWidth)
                maxWidth=width;
        }
        x1 = xmax = x0 + maxWidth;
        y0 = y1 - height; //y0 is above y1
        ymin = y0;
    }

    /**
     * Set the size of this text object to be p_size
     */
    public void reSize(int p_size)
    {
        size = p_size;
    }

    /**
     * Paint this  text object with offset.
     */
    public void paint(Graphics g, int xOffset, int yOffset)
    {
    	

    	
        int xS0 = x0 + xOffset;
        int xS1 = x1 + xOffset;
        int yS0 = y0 + yOffset;
        int yS1 = y1 + yOffset;

    	if(hasBackground)
    	{
    		Graphics2D g2 = (Graphics2D)g;
            g2.setColor(this.getBackground());
            
            g2.fill3DRect(xS0, yS0, xS1 - xS0, yS1 - yS0+5, true);
    	}
        
        g.setColor(c);
        g.setFont(font);

        StringTokenizer lines=new StringTokenizer(textString,"\n");
        int tokenNum=0;
        while(lines.hasMoreTokens())
        {
            g.drawString(lines.nextToken(), xS0, yS0+getFontMetrics(font).getHeight()+tokenNum*(getFontMetrics(font).getHeight()+lineBuffer));
            tokenNum++;
        }

        if (select == 1)
        {
            g.setColor(Color.red);
            g.drawRect(xS0, yS0, xS1 - xS0, yS1 - yS0+5);
            g.setColor(c);
        }
    }

    /**
     * Paint this  text object with no offset.
     */
    public void paint(Graphics g)
    {
        paint(g, 0, 0);
    }

    /**
     * Move this text object as a whole by x offset in x-direction and y
     * offset in y-direction.
     */
    public void moveobj(int x, int y)
    {
        x1 += x;
        x0 += x;

        y1 += y;
        y0 += y;

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
     * Move the selected point of this line object by x offset in x-direction
     * and y offset in y-direction.
     */
    public void movepoint(int x, int y)
    {
        if (select == 1)
        {
            x0 += x;
            y0 += y;
        }
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
     * Select the point of this line object which is within close distance to
     * the point of (x,y).
     *
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

    public boolean selectobjWOffset(int x, int y, int xOffset, int yOffset)
    {
        int tempx0 = x0 + xOffset;
        int tempx1 = x1 + xOffset;
        int tempy0 = y0 + xOffset;
        int tempy1 = y1 + xOffset;

        if (x - tempx0 > 0 && x - tempx1 < 0 && y - tempy0 > 0 && y - tempy1 < 0)
        {
            select = 1;
            return (true);
        }
        return (false);
    }
    //----------------------------------------------

    public boolean selectobj(int x, int y)
    {
        if (x - x0 >= 0 && x - x1 <= 0 && y - y0 >= 0 && y - y1 <= 0)
        {
            select = 1;
            return (true);
        }
        return (false);
    }

//---------------------------------------------------------

    /**
     * Write this line object to the output stream os.
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
            os.writeObject(textString);
            os.writeInt(x0 - x);
            os.writeInt(y0 - y);
            os.writeInt(x1 - x);
            os.writeInt(y1 - y);
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart_text write IOException");
            throw new IOException("GraphicPart_text  write IOException");
        }
    }

//---------------------------------------------------------

    /**
     * WriteAllChars this line object to the output stream os.
     *
     * @param bw the print writer to be written to
     * @param x  the x-coordinate of the reference point
     * @param y  the y-coordinate of the reference point
     */
    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<part type=\"text\">\n");
        bw.write(prefix+"\t<color>"+c.getRGB()+"</color>\n");
        bw.write(prefix+"\t<string>"+textString+"</string>\n");
        super.writeXML(bw, x, y, prefix+"\t");
        bw.write(prefix+"\t<coordinates>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(x0 - x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(y0 - y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t\t<coordinate>\n");
        bw.write(prefix+"\t\t\t<x>"+(x1 - x)+"</x>\n");
        bw.write(prefix+"\t\t\t<y>"+(y1 - y)+"</y>\n");
        bw.write(prefix+"\t\t</coordinate>\n");
        bw.write(prefix+"\t</coordinates>\n");
        bw.write(prefix+"</part>\n");
    }

//---------------------------------------------------------

    public void readXML(org.w3c.dom.Node textNode)
    {
        NodeList textChildren=textNode.getChildNodes();
        for(int i=0; i<textChildren.getLength(); i++)
        {
            org.w3c.dom.Node textChild=(org.w3c.dom.Node)textChildren.item(i);
            if(textChild.getNodeName().equals("bounds"))
                super.readXML(textChild);
            else if(textChild.getNodeName().equals("color"))
                c=new Color(Integer.parseInt(SCSUtility.getNodeValue(textChild)));
            else if(textChild.getNodeName().equals("string"))
                textString= SCSUtility.getNodeValue(textChild);
            else if(textChild.getNodeName().equals("coordinates"))
            {
                x0=Integer.MIN_VALUE;
                y0=Integer.MIN_VALUE;
                NodeList coordinatesChildren=textChild.getChildNodes();
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
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);
            textString = (String) os.readObject();
            x0 = os.readInt();
            y0 = os.readInt();
            x1 = os.readInt();
            y1 = os.readInt();
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error:GraphicPart_text read ClassNotFoundException");
            throw new ClassNotFoundException("GraphicPart_line read ClassNotFoundException");
        }
        catch (IOException e)
        {
            System.err.println("Error:GraphicPart_text  read IOException");
            throw new IOException("GraphicPart_text  read IOException");
        }
    }
} //end class GraphicPart_text

