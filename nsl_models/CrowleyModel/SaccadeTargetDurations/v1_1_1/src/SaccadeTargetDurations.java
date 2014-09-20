/* SCCS  %W%---%G%--%U% */
/* old kversion @(#)DoubleSaccade.mod	1.8 --- 08/05/99 -- 13:56:15 */
package CrowleyModel.SaccadeTargetDurations.v1_1_1.src;

import java.awt.*;
import java.awt.event.*;
import java.lang.*;

// Panel to show timing of targets
public class SaccadeTargetDurations extends java.awt.Panel {
  public static final int FS = 0;
  public static final int FE = 1;
  public static final int T1S = 2;
  public static final int T1E = 3;
  public static final int T2S = 4;
  public static final int T2E = 5;
  
  private float fix_start, fix_end, t1_start, t1_end, t2_start, t2_end;

  public SaccadeTargetDurations(){
    setBackground(Color.white);
    fix_start = (float) 0.;
    fix_end = (float) 0.2;
    t1_start = (float) 0.05;
    t1_end = (float) 0.1;
    t2_start = (float) 0.1;
    t2_end = (float) 0.15;
  }

  public void SetParam(int what, float val){
    switch(what){
    case FS:
      fix_start = val;
      break;
    case FE:
      fix_end = val;
      break;
    case T1S:
      t1_start = val;
      break;
    case T1E:
      t1_end = val;
      break;
    case T2S:
      t2_start = val;
      break;
    case T2E:
      t2_end = val;
      break;
    }
  }
  
  public void paint(Graphics g){
    Rectangle r_border;
    int gx0, gx1, gy0, gy1;
    int x0, x1, y0, y1;
    int i;

    r_border = getBounds();
    // Draw grid
    gx0 = r_border.width/10 ;
    gx1 = r_border.width-gx0 ;
    gy0 = r_border.height/5 ;
    gy1 = r_border.height-gy0 ;
    g.setColor(Color.black);
    g.drawLine(gx0,gy1,gx1,gy1); // X-axis
    // X-ticks
    y0 = gy1-5 ;
    y1 = gy1+5 ;
    for(i=0;i<=12;i++){
      x0 = x1 = gx0+((gx1-gx0)*i)/12;
      g.drawLine(x0,y0,x1,y1);
      if(i%2==0 ){
	g.drawString(Double.toString(i*5./100.),x0-5,y1+15);
      }
    }
    // Draw time bars
    y1 = gy0/2;
    // Fixation
    g.setColor(Color.red);
    y0 = gy0;
    x0 = gx0+(int)((fix_start/.6)*(gx1-gx0));
    x1 = (int)(((fix_end-fix_start)/.6)*(gx1-gx0));
    if(x1<=0)
      x1 = 1;
    g.fillRect(x0,y0,x1,y1);
    // T1
    g.setColor(Color.green);
    y0 = gy0*2;
    x0 = gx0+(int)((t1_start/.6)*(gx1-gx0));
    x1 = (int)(((t1_end-t1_start)/.6)*(gx1-gx0));
    if(x1<=0)
      x1 = 1;
    g.fillRect(x0,y0,x1,y1);
    // T2
    g.setColor(Color.blue);
    y0 = gy0*3;
    x0 = gx0+(int)((t2_start/.6)*(gx1-gx0));
    x1 = (int)(((t2_end-t2_start)/.6)*(gx1-gx0));
    if(x1<=0)
      x1 = 1;
    g.fillRect(x0,y0,x1,y1);
  }
}
