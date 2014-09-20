package scs.ui;
// Copyright: Copyright (c) 1997 University of Southern California Brain Project.
// Copyright: This software may be freely copied provided the toplevel
// Copyright: COPYRIGHT file is included with each such copy.
// Copyright: Email nsl@java.usc.edu.


import scs.util.SCSUtility;

import javax.swing.*;
import java.awt.*;

public class ColorDemo extends JPanel
{
    private String fillcol = "RED";
    Color testcol;

    public ColorDemo()
    {
        fillcol = "RED";
        testcol = Color.red;
        repaint();
    }

    protected void paintComponent(Graphics g)
    {
        //This is changing the color at the top of the ColorDemo frame
        g.drawRect(0, 0, 100, 30);

        g.setColor(SCSUtility.returnCol(fillcol));

        g.fillRect(0, 0, 100, 30); //
    }

    public void setColorFunc(String col)
    {
        fillcol = col;
    }

    public String getColorFunc()
    {
        return (fillcol);
    }
}
