package CrowleyModel.SaccadeParameterPlot.v1_1_1.src;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;

public class SaccadeParameterPlot extends java.awt.Panel {

  Rectangle r_border;

  int gap_time, sac_amp;
  int targ_duration;

  public SaccadeParameterPlot() {
    setBackground(Color.white);
    gap_time = 0;
    sac_amp = 3;
    targ_duration = 100;
  }

  public void setDuration(int dur){
    targ_duration = dur;
  }

  public void setTime(int time) {
    gap_time = time;
  }

  public void setAmp(int amp) {
    sac_amp = amp;
  }

  public void paint(Graphics g) {
    int gx0, gx1, gy0, gy1;
    int x0, x1, y0, y1;
    int i;

    r_border = getBounds();
    // Draw grid
    gx0 = r_border.width/10 ;
    gx1 = r_border.width-gx0 ;
    gy0 = r_border.height/10 ;
    gy1 = r_border.height-gy0 ;
    g.setColor(Color.black);
    g.drawLine(gx0,gy1,gx1,gy1); // X-axis
    g.drawLine(gx0,gy0,gx0,gy1); // Y-axis
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
    // Y-ticks
    x0 = gx0-5 ;
    x1 = gx0+5 ;
    for(i=0;i<=7;i++){
      y0 = y1 = gy1-((gy1-gy0)*i)/7;
      g.drawLine(x0,y0,x1,y1);
      g.drawString(Integer.toString(i),x0-10,y0+5);
    }
    // Start of saccade
    g.setColor(Color.blue);
    y0 = gy0;
    y1 = gy1;
    x0 = x1 = gx0+4*(gx1-gx0)/12;
    g.drawLine(x0,y0,x1,y1);
    g.drawString("Saccade start",x0+5,y0+10);
    // target dissappearance
    g.setColor(Color.magenta);
    y0 = gy1;
    y1 = gy1-(gy1-gy0)/2;
    x0 = x1 = gx0+(200+targ_duration)*(gx1-gx0)/(12*50);
    g.drawLine(x0,y0,x1,y1);
    g.drawString("Target duration",x0+5,y0+(y1-y0)/2);
    g.drawString(Double.toString(targ_duration/1000.),x0+5,y0+(y1-y0)/2+8);
    // fixation point dissappearance
    g.setColor(Color.red);
    y0 = gy1;
    y1 = gy1-(gy1-gy0)/2;
    x0 = x1 = gx0+gap_time*(gx1-gx0)/(12*50);
    g.drawLine(x0,y0,x1,y1);
    g.drawString("Fixation off",x0+5,y0+(y1-y0)/2-18);
    g.drawString(Double.toString((gap_time-200)/1000.),x0+5,y0+(y1-y0)/2-10);
    // Amplitude
    x0 = gx0+(gx1-gx0)/2;
    x1 = gx1;
    y0 = y1 = gy1-sac_amp*(gy1-gy0)/4;
    g.drawLine(x0,y0,x1,y1);
    g.drawString(Integer.toString(sac_amp),x0+(x1-x0)/2-5,y0-5);
  }
}
