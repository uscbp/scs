package scs.graphics;

import scs.graphics.GraphicPart;
import scs.util.SCSUtility;
import scs.Declaration;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.BufferedWriter;

import org.w3c.dom.NodeList;

public class SchematicPort extends GraphicPart
{
    public String Name;
    public String Type;
    String Parameters;
    char portDirection; // which way does the port point
    int x[] = new int[5];
    int y[] = new int[5];
    int px0, px1, py0, py1; //little connecting arrow
    int tx0, ty0; // bottom left corner of text of icon

    public SchematicPort()
    {
        Name = "";
        Type = "";
        Parameters = "";
    }

    public SchematicPort(String n, String t, String p, char p_portDirection)
    {
        Name = n;
        Type = t;
        Parameters = p;
        // todo: change to get current setting for portDirection
        portDirection = p_portDirection;
    }

    public SchematicPort(Declaration var)
    {
        Name = var.varName;
        Type = var.varType;
        Parameters = var.varParams;
        portDirection = var.portSchDirection;
    }

    /**
     * Calculate the values of xmin, ymin, xmax, and ymax.
     */
    public void setminmax()
    {
        xmin = 1000;
        ymin = 1000;
        xmax = -1000;
        ymax = -1000;
        for (int i = 0; i < 5; i++)
        {
            if (x[i] < xmin) xmin = x[i];
            if (y[i] < ymin) ymin = y[i];
            if (x[i] > xmax) xmax = x[i];
            if (y[i] > ymax) ymax = y[i];
        }
        //text done in paint

        //little connecting arrow
        xmin = Math.min(xmin, px0);
        xmin = Math.min(xmin, px1);
        ymin = Math.min(ymin, py0);
        ymin = Math.min(ymin, py1);
        xmax = Math.max(xmax, px0);
        xmax = Math.max(xmax, px1);
        ymax = Math.max(ymax, py0);
        ymax = Math.max(ymax, py1);
    }

    /**
     * Select the little arrow of this schematic inport if the point (xx,yy) is      * within close distance to this little arrow.
     *
     * @return <code>true</code> if the point (xx,yy) is within close
     *         distance to the little arrow of this schematic inport
     *         <code>false</code> otherwise
     */
    public boolean selectport(int xx, int yy)
    {
        if (px0 == px1 && Math.abs(xx - px0) <= 3 &&
                (yy <= py0 && yy >= py1 || yy <= py1 && yy >= py0)
                || py0 == py1 && Math.abs(yy - py0) <= 3 &&
                (xx <= px0 && xx >= px1 || xx <= px1 && xx >= px0))
        {
            return (true);
        }

        return (false);
    }

    /**
     * Select this schematic inport as a whole if the point (xx,yy) is within
     * the scope of this schematic inport.
     *
     * @return        <code>true</code> if the point (xx,yy) is within the
     * scope of this schematic inport
     * <code>false</code> otherwise
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
     * Make this schematic inport unselected.
     */
    public void unselect()
    {
        select = 0;
    }

    public void writeXML(BufferedWriter bw, String prefix) throws IOException
    {
        bw.write(prefix+"<name>"+Name+"</name>\n");
        bw.write(prefix+"<type>"+Type+"</type>\n");
        bw.write(prefix+"<parameters>"+Parameters+"</parameters>\n");
        bw.write(prefix+"<portDirection>"+portDirection+"</portDirection>\n");
        bw.write(prefix+"<corners>\n");
        for (int i = 0; i < 5; i++)
        {
            bw.write(prefix+"\t<corner>\n");
            bw.write(prefix+"\t\t<x>"+x[i]+"</x>\n");
            bw.write(prefix+"\t\t<y>"+y[i]+"</y>\n");
            bw.write(prefix+"\t</corner>\n");
        }
        bw.write(prefix+"</corners>\n");
        bw.write(prefix+"<bounds>\n");
        bw.write(prefix+"\t<min>\n");
        bw.write(prefix+"\t\t<x>"+px0+"</x>\n");
        bw.write(prefix+"\t\t<y>"+py0+"</y>\n");
        bw.write(prefix+"\t</min>\n");
        bw.write(prefix+"\t<max>\n");
        bw.write(prefix+"\t\t<x>"+px1+"</x>\n");
        bw.write(prefix+"\t\t<y>"+py1+"</y>\n");
        bw.write(prefix+"\t</max>\n");
        bw.write(prefix+"</bounds>\n");
    }
    
    /**
     * Write this schematic port to the output stream os.
     *
     * @throws java.io.IOException if an IO error occurred
     */
    public void write(ObjectOutputStream os) throws IOException
    {
        int i;
        try
        {
            os.writeUTF(Name);
            os.writeUTF(Type);
            os.writeUTF(Parameters);
            os.writeChar(portDirection);

            //write the 5 corners of the port
            for (i = 0; i < 5; i++)
            {
                os.writeInt(x[i]);
                os.writeInt(y[i]);
            }
            //write the bounds
            os.writeInt(px0);
            os.writeInt(py0);
            os.writeInt(px1);
            os.writeInt(py1);
        }
        catch (IOException e)
        {
            System.err.println("SchematicPort:write IOException");
            throw new IOException("SchematicPort write IOException");
        }
    }

    public void readXML(org.w3c.dom.Node portNode)
    {
        NodeList portChildren=portNode.getChildNodes();
        for(int i=0; i<portChildren.getLength(); i++)
        {
            org.w3c.dom.Node portChild=(org.w3c.dom.Node)portChildren.item(i);
            if(portChild.getNodeName().equals("name"))
                Name= SCSUtility.getNodeValue(portChild);
            else if(portChild.getNodeName().equals("type"))
                Type=SCSUtility.getNodeValue(portChild);
            else if(portChild.getNodeName().equals("parameters"))
                Parameters=SCSUtility.getNodeValue(portChild);
            else if(portChild.getNodeName().equals("portDirection"))
                portDirection=SCSUtility.getNodeValue(portChild).charAt(0);
            else if(portChild.getNodeName().equals("corners"))
            {
                x=new int[5];
                y=new int[5];
                int idx=0;
                NodeList cornersChildren=portChild.getChildNodes();
                for(int j=0; j<cornersChildren.getLength(); j++)
                {
                    org.w3c.dom.Node cornersChild=(org.w3c.dom.Node)cornersChildren.item(j);
                    if(cornersChild.getNodeName().equals("corner"))
                    {
                        NodeList cornerChildren=cornersChild.getChildNodes();
                        for(int k=0; k<cornerChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node cornerChild=(org.w3c.dom.Node)cornerChildren.item(k);
                            if(cornerChild.getNodeName().equals("x"))
                                x[idx]=Integer.parseInt(SCSUtility.getNodeValue(cornerChild));
                            else if(cornerChild.getNodeName().equals("y"))
                                y[idx]=Integer.parseInt(SCSUtility.getNodeValue(cornerChild));
                        }
                        idx++;
                    }
                }
            }
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
                                px0=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                            else if(minChild.getNodeName().equals("y"))
                                py0=Integer.parseInt(SCSUtility.getNodeValue(minChild));
                        }
                    }
                    else if(boundsChild.getNodeName().equals("max"))
                    {
                        NodeList maxChildren=boundsChild.getChildNodes();
                        for(int k=0; k<maxChildren.getLength(); k++)
                        {
                            org.w3c.dom.Node maxChild=(org.w3c.dom.Node)maxChildren.item(k);
                            if(maxChild.getNodeName().equals("x"))
                                px1=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                            else if(maxChild.getNodeName().equals("y"))
                                py1=Integer.parseInt(SCSUtility.getNodeValue(maxChild));
                        }
                    }
                }
            }
        }
        select = 0;
        setminmax();
    }
    /**
     * Read this schematic port from the input stream os.
     *
     * @exception IOException             if an IO error occurred
     * @exception ClassNotFoundException  if a class-not-found error
     *						occurred
     */
    public void read(ObjectInputStream os) throws IOException, ClassNotFoundException
    {
        int i;

        try
        {
            Name = os.readUTF();
            Type = os.readUTF();
            Parameters = os.readUTF();
            portDirection = os.readChar();

            //read the 5 points that make up the port
            for (i = 0; i < 5; i++)
            {
                x[i] = os.readInt();
                y[i] = os.readInt();
            }
            //read bounds
            px0 = os.readInt();
            py0 = os.readInt();
            px1 = os.readInt();
            py1 = os.readInt();

            select = 0;
            setminmax();
        }
        catch (IOException e)
        {
            throw new IOException("SchematicInport read IOException");
        }
    }
}
