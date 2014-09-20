package DartModel.DartModel.v1_1_1.src;
import DartModel.CEREB_module.v1_1_1.src.*;
import DartModel.THROW_layer.v1_1_1.src.*;
import DartModel.SENS_layer.v1_1_1.src.*;
import DartModel.PP_layer.v1_1_1.src.*;
import DartModel.FCX_layer.v1_1_1.src.*;
import DartModel.DART_UI_module.v1_1_1.src.*;

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

public class DartModel extends NslModel{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  DartModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  CEREB_module cereb_m; // 
public  THROW_layer throw_m; // 
public  SENS_layer sens_l; // 
public  PP_layer pp_l; // 
public  FCX_layer fcx_l; // 
public  DART_UI_module gui_m; // 
private NslDouble0 pp_noise; // 
private NslDouble0 pp_sep; // 
private NslDouble0 fcx_noise; // 
private NslDouble0 gc_offset; // 
private NslDouble0 gc_slope; // 
private NslDouble0 gc_dist; // 
private NslDouble0 alpha; // 
private NslInt0 protocol; // 
private NslDouble0 p_out; // Prism angle [0:30]
private NslDouble0 d_out; // Target direction [-30:30]
private NslDouble0 s_out; // Strategy: [0:1] over/under
private int state; // 
private NslInt0 gc_nd; // 

//methods 
public void initSys()
{
	system.setTrainEndTime(.07);
	system.setTrainDelta(.01);
     
	system.setRunEndTime(.07);
	system.setRunDelta(.01);
}


public void initModule()
{
	nslSetTrainDelta(0.01);
	nslSetRunDelta(0.01);
     
	system.nslSetApproximationDelta(.01);

	pp_noise.set(  .05);
	pp_sep.set(  4.0);
	fcx_noise.set(  .05);
	gc_offset.set(  .5);
	gc_slope.set(  50.);
	gc_nd.set(  4);
	gc_dist.set(  .9);
	alpha.set(  -0.0005);
	protocol.set(  0);

	nslDeclareProtocol("basic", "Basic on/off");
	nslDeclareProtocol("transfer", "Overarm/Underarm transfer");
	nslDeclareProtocol("calibration", "Two gaze-throw calibrations");
          
	system.addProtocolToAll("basic");
	system.addProtocolToAll("transfer");
	system.addProtocolToAll("calibration");     
}
 
public void basicProtocol() 
{
	system.nslPrintln("Basic on/off protocol selected");
	protocol.set(  0); 	
}
 
public void transferProtocol() 
{
	system.nslPrintln("Overarm/Underarm transfer protocol selected");
	protocol.set(  1);
}
 
public void calibrationProtocol() 
{
	system.nslPrintln("Two gaze-throw calibrations protocol selected");
	protocol.set(  2);
}

public void setValues() 
{
	// Define outputs given current state
	switch(state)
	{
		case 0:       // over, no prisms
			p_out.set(  0.);
			d_out.set(  0.);
			s_out.set(  0.);
			break;
		case 1:       // over, prisms
			p_out.set(  30.);
			d_out.set(  0.);
			s_out.set(  0.);
			break;
		case 2:       // under, no prisms
			p_out.set(  0.);
			d_out.set(  0.);
			s_out.set(  1.);
			break;
		case 3:       // under, prisms
			p_out.set(  30.);
			d_out.set(  0.);
			s_out.set(  1.);
			break;
	}  
}

public void initTrainEpoch() 
{
	system.nslPrintln("The first 100 throws are for warm-up. Please be patient");    
	state = 0; // Over-arm, no prisms      	      
	system.setNumTrainEpochs(100);
}

public void initTrain() 
{
	switch (system.getFinishedEpochs())
	{  
		case 10:
			state = 3;      	  
			break;
		case 30:
			state = 1;      	  
			break;
		case 50:
			state = 2;      	  
			break;
		case 70:
			state = 0;      	  
			break;
	}       
	setValues(); 
}
   
public void initRunEpoch() 
{
	state = 0; // Over-arm, no prisms
     	
	switch ((int)protocol.get()) 
	{
		case 0:
			system.setNumRunEpochs(60);
			break;
		case 1:
			system.setNumRunEpochs(1500);
			break;
		case 2:
			system.setNumRunEpochs(100);
			break;
	}
}
 
public void initRun()
{
	int epoch = system.getFinishedEpochs();

	switch ((int)protocol.get()) 
	{
		case 0:
			switch (epoch%40) 
			{
				case 20:
					state = 1;
					break;
				case 0:
					state = 0;
					break;
			}
			break;                
		case 1:
			switch (epoch%80) 
			{
				case 80:
					state = 2;
					break;
				case 20:
					state = 0;
					break;
				case 40:
					state = 1;
					break;
				case 60:
					state = 2;
					break;
				case 0:
					state = 0;
					break;
			}
			break;                
		case 2:
			if(epoch==0)
			{
				state = 0;
			}
			// state transition
			else if(epoch%20==0 ) 
			{
				switch(state)
				{
					case 0:
						state = 1;
						break;
					case 1:
						state = 0;
						break;
					case 2:
						state = 1;
						break;
					case 3:
						state = 0;
						break;
				}
			}
			break;
	}
     
	setValues(); 
}
public void makeConn(){
    nslConnect(throw_m.throw_out,gui_m.throw_in);
    nslConnect(throw_m.throw_out,sens_l.t_in);
    nslConnect(sens_l.p_out,gui_m.p_in);
    nslConnect(sens_l.p_out,fcx_l.p_in);
    nslConnect(sens_l.sens_out,cereb_m.sens_in);
    nslConnect(pp_l.s_out,gui_m.s_in);
    nslConnect(pp_l.pp_out,cereb_m.pp_in);
    nslConnect(fcx_l.fcx_out,cereb_m.fcx_in);
    nslConnect(cereb_m.cereb_out,throw_m.nuc_in);
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* EMPTY CONSTRUCTOR: Called only for the top level module */
	public DartModel() {
		super("dartModel",(NslModel)null);
		initSys();
		makeInstDartModel("dartModel",null);
	}

	/* Formal parameters */

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public DartModel(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstDartModel(nslName, nslParent);
	}

	public void makeInstDartModel(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		cereb_m = new CEREB_module("cereb_m", this);
		throw_m = new THROW_layer("throw_m", this);
		sens_l = new SENS_layer("sens_l", this);
		pp_l = new PP_layer("pp_l", this);
		fcx_l = new FCX_layer("fcx_l", this);
		gui_m = new DART_UI_module("gui_m", this);
		pp_noise = new NslDouble0("pp_noise", this);
		pp_sep = new NslDouble0("pp_sep", this);
		fcx_noise = new NslDouble0("fcx_noise", this);
		gc_offset = new NslDouble0("gc_offset", this);
		gc_slope = new NslDouble0("gc_slope", this);
		gc_dist = new NslDouble0("gc_dist", this);
		alpha = new NslDouble0("alpha", this);
		protocol = new NslInt0("protocol", this);
		p_out = new NslDouble0("p_out", this);
		d_out = new NslDouble0("d_out", this);
		s_out = new NslDouble0("s_out", this);
		gc_nd = new NslInt0("gc_nd", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end DartModel

