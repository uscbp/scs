package DartModel.DartModel.v1_1_1.src;
nslImport DartModel.CEREB_module.v1_1_1.src.*;
nslImport DartModel.THROW_layer.v1_1_1.src.*;
nslImport DartModel.SENS_layer.v1_1_1.src.*;
nslImport DartModel.PP_layer.v1_1_1.src.*;
nslImport DartModel.FCX_layer.v1_1_1.src.*;
nslImport DartModel.DART_UI_module.v1_1_1.src.*;

nslModel DartModel(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  DartModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public CEREB_module cereb_m(); // 
public THROW_layer throw_m(); // 
public SENS_layer sens_l(); // 
public PP_layer pp_l(); // 
public FCX_layer fcx_l(); // 
public DART_UI_module gui_m(); // 
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

	pp_noise = .05;
	pp_sep = 4.0;
	fcx_noise = .05;
	gc_offset = .5;
	gc_slope = 50.;
	gc_nd = 4;
	gc_dist = .9;
	alpha = -0.0005;
	protocol = 0;

	nslDeclareProtocol("basic", "Basic on/off");
	nslDeclareProtocol("transfer", "Overarm/Underarm transfer");
	nslDeclareProtocol("calibration", "Two gaze-throw calibrations");
          
	system.addProtocolToAll("basic");
	system.addProtocolToAll("transfer");
	system.addProtocolToAll("calibration");     
}
 
public void basicProtocol() 
{
	nslPrintln("Basic on/off protocol selected");
	protocol = 0; 	
}
 
public void transferProtocol() 
{
	nslPrintln("Overarm/Underarm transfer protocol selected");
	protocol = 1;
}
 
public void calibrationProtocol() 
{
	nslPrintln("Two gaze-throw calibrations protocol selected");
	protocol = 2;
}

public void setValues() 
{
	// Define outputs given current state
	switch(state)
	{
		case 0:       // over, no prisms
			p_out = 0.;
			d_out = 0.;
			s_out = 0.;
			break;
		case 1:       // over, prisms
			p_out = 30.;
			d_out = 0.;
			s_out = 0.;
			break;
		case 2:       // under, no prisms
			p_out = 0.;
			d_out = 0.;
			s_out = 1.;
			break;
		case 3:       // under, prisms
			p_out = 30.;
			d_out = 0.;
			s_out = 1.;
			break;
	}  
}

public void initTrainEpoch() 
{
	nslPrintln("The first 100 throws are for warm-up. Please be patient");    
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
     	
	switch ((int)protocol) 
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

	switch ((int)protocol) 
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
			else if(epoch % 20 == 0) 
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
}//end DartModel

