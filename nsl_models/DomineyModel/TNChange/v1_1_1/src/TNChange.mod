package DomineyModel.TNChange.v1_1_1.src;

nslModule TNChange(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  TNChange
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat0 fefgate(); // 
public NslDinFloat0 ebn(); // 
public NslDinFloat0 opposite_ebn(); // 
public NslDinFloat0 tnDelta(); // 
public NslDoutFloat0 tn(); // 
public NslDoutFloat0 tnChange(); // 
private NslFloat0 tn_k1; // 
private NslFloat0 tnDelay1_tm; // 
private NslFloat0 tnDelay1; // 

//methods 
public void initRun() 
{
	if (system.debug>=13) 
	{
		nslPrintln("debug:TNChange:initRun");
	}
	tn=0;
	tnChange=0;
	tnDelay1=154; // use to be TNDELAY
	tn=154;
	tnChange=0;

	tn_k1=.01527778;
	tnDelay1_tm=.006;
}

public void simRun() 
{
	tnDelay1=nslDiff(tnDelay1,tnDelay1_tm, - tnDelay1 + tn);
	//On each timestep,the tonic component uppdated by 2.75*(change in Theta).
	// Change in Theta is angular velocity * time =
	//			   opposite_ebn*(1/R)  * (simuation time step)
	tn = tn + tn_k1*ebn - tn_k1*opposite_ebn - fefgate*tnDelta;
	tnChange = tn - tnDelay1;

	if (system.debug>=13) 
	{
		nslPrintln("TNChange:tn: "+tn);
		nslPrintln("TNChange:tnChange: "+tnChange);
	}
}
public void makeConn(){
}
}//end TNChange

