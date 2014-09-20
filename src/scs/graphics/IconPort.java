package scs.graphics;

import scs.Declaration;
import scs.util.SCSUtility;

import java.awt.*;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.BufferedWriter;

import org.w3c.dom.NodeList;

/**
 * Created by IntelliJ IDEA.
 * User: jbonaiuto
 * Date: Oct 28, 2006
 * Time: 12:39:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class IconPort extends GraphicPart_line
{
    public String Name = "";
    public String Type = "";
    public String Parameters = "";
    char direction;
    char signalType;  // excitatory 'E' vs. inhibitory 'I' or output 'O'

    public IconPort()
    {

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
    public IconPort(String n, String t, String params, char p_dir, char p_signalType, int xx1, int yy1, Color cc)
    {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.
        super(xx1, yy1, cc);
        Name = n;
        Type = t;
        Parameters = params;
        direction = p_dir;
        signalType = p_signalType;
    }

    /**
     * Constructor of this class, with initial values of var of type declaration,
     * x-coordinates and y-coordinates for the two end-points of this arrow-shaped
     * icon inports, and color being set with cc.
     * Note: xx1 and yy1 now represent the point with the arrow
     *
     * @since JDK1.2
     */
    public IconPort(Declaration var, int xx1, int yy1, Color cc)
    {
        // take in the end points of the connector - or in other words,
        // the end that is closest to the object.
        super(xx1, yy1, cc);
        Name = var.varName;
        Type = var.varType;
        Parameters = var.varParams;
        direction = var.portIconDirection;
        signalType = var.portSignalType;
    }

    public void movepoint(int x, int y)
    {
        moveobj(x, y);
    }

    //-----------------------------------------------------

    /**
     * Write this icon port to the output stream os.
     *
     * @throws java.io.IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os, int x, int y)
            throws IOException
    {
        try
        {
            super.write(os, x, y);
            os.writeUTF(Name);
            os.writeUTF(Type);
            os.writeUTF(Parameters);
            os.writeChar(direction);
            os.writeChar(signalType);
        }
        catch (IOException e)
        {
            System.err.println("IconPort write IOException");
            throw new IOException("IconPort write IOException");
        }
    }

    public void writeXML(BufferedWriter bw, int x, int y, String prefix) throws IOException
    {
        bw.write(prefix+"<name>"+Name+"</name>\n");
        bw.write(prefix+"<parameters>"+ Parameters +"</parameters>\n");
        bw.write(prefix+"<portDirection>"+direction+"</portDirection>\n");
        bw.write(prefix+"<signalType>"+signalType+"</signalType>\n");
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
        bw.write(prefix+"<color>"+c.getRGB()+"</color>\n");
        bw.write(prefix+"<coordinates>\n");
        bw.write(prefix+"\t<coordinate>\n");
        bw.write(prefix+"\t\t<x>"+(x0-x)+"</x>\n");
        bw.write(prefix+"\t\t<y>"+(y0-y)+"</y>\n");
        bw.write(prefix+"\t</coordinate>\n");
        bw.write(prefix+"\t<coordinate>\n");
        bw.write(prefix+"\t\t<x>"+(x1-x)+"</x>\n");
        bw.write(prefix+"\t\t<y>"+(y1-y)+"</y>\n");
        bw.write(prefix+"\t</coordinate>\n");
        bw.write(prefix+"</coordinates>\n");
    }

    public void readXML(org.w3c.dom.Node portNode)
    {
        Type=portNode.getAttributes().getNamedItem("type").getNodeValue();
        NodeList portChildren=portNode.getChildNodes();
        for(int i=0; i<portChildren.getLength(); i++)
        {
            org.w3c.dom.Node portChild=(org.w3c.dom.Node)portChildren.item(i);
            if(portChild.getNodeName().equals("name"))
                Name= SCSUtility.getNodeValue(portChild);
            else if(portChild.getNodeName().equals("parameters"))
                Parameters = SCSUtility.getNodeValue(portChild);
            else if(portChild.getNodeName().equals("portDirection"))
                direction= SCSUtility.getNodeValue(portChild).charAt(0);
            else if(portChild.getNodeName().equals("signalType"))
                signalType=SCSUtility.getNodeValue(portChild).charAt(0);
            else if(portChild.getNodeName().equals("bounds"))
            {
                NodeList boundsChildren=portChild.getChildNodes();
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
            else if(portChild.getNodeName().equals("color"))
                c=new Color(Integer.parseInt(SCSUtility.getNodeValue(portChild)));
            else if(portChild.getNodeName().equals("coordinates"))
            {
                x0=Integer.MIN_VALUE;
                y0=Integer.MIN_VALUE;
                NodeList coordinatesChildren=portChild.getChildNodes();
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
    //----------------------------------------------------------------

    /**
     * Read this icon port from the input stream os.
     *
     * @throws IOException            if an IO error occurred
     * @throws ClassNotFoundException if a class-not-found error occurred
     */
    public void read(ObjectInputStream os)
            throws IOException, ClassNotFoundException
    {
        try
        {
            super.read(os);
            Name = os.readUTF();
            Type = os.readUTF();
            Parameters = os.readUTF();
            direction = os.readChar();
            signalType = os.readChar();
        }
        catch (IOException e)
        {
            System.err.println("IconPort read Name&Type IOException");
            throw new IOException("IconPort read Name&Type IOException");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("IconPort read Name&TypeClassNotFoundException");
            throw new ClassNotFoundException("IconPort read&Type ClassNotFoundException");
        }

    } //end read

    public void paintLabel(Graphics g)
    {
        FontMetrics fontmetrics = g.getFontMetrics();
        g.setColor(Color.yellow);
        g.fillRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                fontmetrics.getHeight() + 10);
        g.setColor(Color.black);
        g.drawRect(xmax + 3, ymax + 3, fontmetrics.stringWidth(Name) + 10,
                fontmetrics.getHeight() + 10);
        g.setColor(Color.red);
        g.drawString(Name, xmax + 8, ymax + 8 + fontmetrics.getHeight());
    }
}
