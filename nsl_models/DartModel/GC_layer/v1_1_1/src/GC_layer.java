package DartModel.GC_layer.v1_1_1.src;

/*********************************/
/*                               */
/*   Importing all Nsl classes   */
/*                               */
/*********************************/

import nslj.src.system.*;
import nslj.src.cmd.*;
import nslj.src.lang.*;
import nslj.src.math.*;
import nslj.src.display.*;
import nslj.src.display.j3d.*;

/*********************************/

public class GC_layer extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  GC_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 pp_in; // 
public  NslDinDouble2 fcx_in; // 
public  NslDoutDouble2 gc_out; // 
private double f_max=100; // 
private NslDouble0 w; // 
private  NslDouble2 gc_mp; // 
private NslDouble0 gc_offset; // 
private NslDouble0 gc_slope; // 
private NslDouble0 gc_dist; // 
private NslInt0 gc_nd; // 
private  int src[]; // 
private  int Xo[]; // 
private  int Yo[]; // 
private  int Xd[]; // 
private  int Yd[]; // 
private int NC; // 

//methods 
public void initModule() 
{
	gc_offset.set(  (NslDouble0)nslGetValue("dartModel.gc_offset"));
	gc_slope.set(   (NslDouble0)nslGetValue("dartModel.gc_slope"));
	gc_dist.set(    (NslDouble0)nslGetValue("dartModel.gc_dist"));
	gc_nd.set(      (NslInt0)nslGetValue("dartModel.gc_nd"));

	w.set(  1./(double)gc_nd.get());

	int gx,gy,i,x,y;
     
	gc_out.set(  50.);
	gc_mp.set(  0.);
	// Create mapping function
	NC = 0;
	for(gx=0;gx<30;gx++)
	{
		for(gy=0;gy<30;gy++)
		{
			for(i=0;i<gc_nd.get();i++)
			{
				Xd[NC] = gx;
				Yd[NC] = gy;
				// PP input
				if(NslRandom.eval()<gc_dist.get() )
				{ 
					src[NC] = 0;
					Xo[NC] = (int)NslRandom.eval(3,8);
					Yo[NC] = (int)NslRandom.eval(0,10);
		 		} 
				// FCX input
				else 
				{ 
					src[NC] = 1;
					Xo[NC] = (int)NslRandom.eval(0,10);
					Yo[NC] = (int)NslRandom.eval(1,3);
				}
				NC++;
			}
		}
	}
}

public void simTrain() 
{
	simRun();
}

public void simRun()
{
	int i,j;
	int mx,my,ix,iy;

	// Map inputs onto 30x30 array using mapping function
          
	gc_mp.set(  0.);
	for(i=0;i<NC;i++)
	{
		mx = Xd[i];
		my = Yd[i];
		ix = Xo[i];
		iy = Yo[i];
		if(src[i]==0)
			gc_mp.set(mx, my,   __tempGC_layer2.setReference(__tempGC_layer0.setReference(gc_mp.get(mx, my)).get()+__tempGC_layer1.setReference(pp_in.get(ix, iy)).get()));
		else
			gc_mp.set(mx, my,   __tempGC_layer5.setReference(__tempGC_layer3.setReference(gc_mp.get(mx, my)).get()+__tempGC_layer4.setReference(fcx_in.get(ix, iy)).get()));
	}
     
	gc_mp.set(  __tempGC_layer6.setReference(NslElemMult.eval(__tempGC_layer6.get(), w, gc_mp)) );

	gc_out.set(  __tempGC_layer7 = (NslElemMult.eval(__tempGC_layer7, f_max, NslSigmoid.eval(gc_mp,gc_slope,gc_offset))));
}
public void makeConn(){
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */

	/* Temporary variables */
		NslDouble0 __tempGC_layer0 = new NslDouble0();
		NslDouble0 __tempGC_layer1 = new NslDouble0();
		NslDouble0 __tempGC_layer2 = new NslDouble0();
		NslDouble0 __tempGC_layer3 = new NslDouble0();
		NslDouble0 __tempGC_layer4 = new NslDouble0();
		NslDouble0 __tempGC_layer5 = new NslDouble0();
		NslDouble2 __tempGC_layer6 = new NslDouble2(1, 1);
		double[][] __tempGC_layer7 = new double[1][1];

	/* GENERIC CONSTRUCTOR: */
	public GC_layer(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstGC_layer(nslName, nslParent);
	}

	public void makeInstGC_layer(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		pp_in = new NslDinDouble2("pp_in", this, 10, 10);
		fcx_in = new NslDinDouble2("fcx_in", this, 10, 4);
		gc_out = new NslDoutDouble2("gc_out", this, 30, 30);
		w = new NslDouble0("w", this);
		gc_mp = new NslDouble2("gc_mp", this, 30, 30);
		gc_offset = new NslDouble0("gc_offset", this);
		gc_slope = new NslDouble0("gc_slope", this);
		gc_dist = new NslDouble0("gc_dist", this);
		gc_nd = new NslInt0("gc_nd", this);
		src = new int[3600];
		Xo = new int[3600];
		Yo = new int[3600];
		Xd = new int[3600];
		Yd = new int[3600];
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end GC_layer

