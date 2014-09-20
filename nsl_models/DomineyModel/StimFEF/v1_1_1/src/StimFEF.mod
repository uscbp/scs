/** 
The Saccade generator is ala Scudder;  
These arrays represent the increased density of supcol projection 
to llbn as a function of increase eccentricity in supcol. 

Note, the fixation point for a 27x27 grid is 13,13
However, the StimFEF module uses a 9x9 array

Note, see the documentation in VisualInput.nslm
*/
package DomineyModel.StimFEF.v1_1_1.src;

nslModule StimFEF(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  StimFEF
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutFloat2 stimFEF(stdsz, stdsz); // 
private double currentTime=0; // 
private double currentTimePlusDelta=0; // 
private float value=175; // 
private int localstdsz=0; // 
private int center; // 
private int centerP3; // 
private int centerP2; // 
private int centerM3; // 
private int centerM2; // 
private NslInt0 protocolNum; // 

//methods 
public void initModule() 
{
	stimFEF.nslSetAccess('W');
}

public void initRun() 
{
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");

	// Have to do the following because we start at 0.
	// Note: for 27xby27 the center is 13,13
	// however, the visual field has been pared down by the time
	// the signal gets to the supcol, thus we are dealing with
	// a 9x9 array here with a center of 4,4

	stimFEF=0;	

	center = (int)stdsz/2; //(9/2 =4.5 round down 4)
	centerP3 = center + 3;
	centerP2 = center + 2;
	centerM3 = center - 3;
	centerM2 = center - 2;
}

public void simRun() 
{
	currentTime=system.getCurTime();	
	currentTime=currentTime+0.005;

	// ("stimulatedFEFI")
	if (protocolNum==11) 
	{
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11))
		{
			stimFEF[centerM3][center]=value;
		}
		// unlight stimFEF
		if (currentTime>0.11) 
		{
			stimFEF[centerM3][center]=0;
		}
	}
	//("stimulatedFEFII")
	if (protocolNum==12) 
	{
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11))
		{
			stimFEF[center][centerM2]=value;
		}
		// unlight stimFEF
		if (currentTime>0.11) 
		{
			stimFEF[center][centerM2]=0;
		}
	}
	//("stimulatedFEF-LesionSC I")
	else if (protocolNum==13) 
	{
		// light up Fake Stimulated Target
		if ((currentTime>=0.07) &&(currentTime<=0.11))
		{
			stimFEF[centerM3][center]=value;
		}
		// unlight stimFEF
		if (currentTime>0.11) 
		{
			stimFEF[centerM3][center]=0;
		}
	}
	if (system.debug>1) 
	{
		//nslPrintln("debug: stimFEF ");
		//nslPrintln(stimFEF);
		nslPrintln("debug: stimFEF: "+ stimFEF);
	}
} //end simRun
public void makeConn(){
}
}//end StimFEF

