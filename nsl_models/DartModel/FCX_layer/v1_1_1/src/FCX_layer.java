package DartModel.FCX_layer.v1_1_1.src;

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

public class FCX_layer extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  FCX_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble0 p_in; // Prism angle [0:30]
public  NslDoutDouble2 fcx_out; // 
private double sx2=100; // 10*10
private double sy2=2.25; // 1.5*1.5
private NslDouble0 fcx_noise; // 

//methods 
public void initModule()
{
	fcx_out.set(0.);
	fcx_noise.set((NslDouble0)nslGetValue("dartModel.fcx_noise"));
}
   
public void simTrain() 
{
	simRun();
}  
 
public void simRun()
{
	int i,j;
	NslDouble0 mx = new NslDouble0();
        NslDouble0 my = new NslDouble0();
        NslDouble0 dx = new NslDouble0();
        NslDouble0 dy = new NslDouble0();
        
     
	mx.set(  __tempFCX_layer2.setReference(1.+__tempFCX_layer1.setReference(__tempFCX_layer0.setReference(9.*p_in.get()).get()/50.).get()));
	my.set(  1.5);
     
	for(i=0;i<10;i++)
	{
		dx.set(  __tempFCX_layer3.setReference(mx.get()-i));
		for(j=0;j<4;j++)
		{
			dy.set(  __tempFCX_layer4.setReference(my.get()-j) );
			fcx_out.set(i, j,   __tempFCX_layer14.setReference(__tempFCX_layer5.setReference(fcx_noise.get()*NslRandom.eval()).get()+__tempFCX_layer13.setReference((__tempFCX_layer6.setReference(1.-fcx_noise.get())).get()*NslOperator.exp.eval(__tempFCX_layer12.setReference(-1.*(__tempFCX_layer11.setReference(__tempFCX_layer8.setReference(__tempFCX_layer7.setReference(dx.get()*dx.get()).get()/sx2).get()+__tempFCX_layer10.setReference(__tempFCX_layer9.setReference(dy.get()*dy.get()).get()/sy2).get())).get()))).get()));
		}
	}
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
		NslDouble0 __tempFCX_layer0 = new NslDouble0();
		NslDouble0 __tempFCX_layer1 = new NslDouble0();
		NslDouble0 __tempFCX_layer2 = new NslDouble0();
		NslDouble0 __tempFCX_layer3 = new NslDouble0();
		NslDouble0 __tempFCX_layer4 = new NslDouble0();
		NslDouble0 __tempFCX_layer5 = new NslDouble0();
		NslDouble0 __tempFCX_layer6 = new NslDouble0();
		NslDouble0 __tempFCX_layer7 = new NslDouble0();
		NslDouble0 __tempFCX_layer8 = new NslDouble0();
		NslDouble0 __tempFCX_layer9 = new NslDouble0();
		NslDouble0 __tempFCX_layer10 = new NslDouble0();
		NslDouble0 __tempFCX_layer11 = new NslDouble0();
		NslDouble0 __tempFCX_layer12 = new NslDouble0();
		NslDouble0 __tempFCX_layer13 = new NslDouble0();
		NslDouble0 __tempFCX_layer14 = new NslDouble0();

	/* GENERIC CONSTRUCTOR: */
	public FCX_layer(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstFCX_layer(nslName, nslParent);
	}

	public void makeInstFCX_layer(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		p_in = new NslDinDouble0("p_in", this);
		fcx_out = new NslDoutDouble2("fcx_out", this, 10, 4);
		fcx_noise = new NslDouble0("fcx_noise", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end FCX_layer
