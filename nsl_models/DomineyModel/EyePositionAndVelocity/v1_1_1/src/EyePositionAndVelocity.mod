package DomineyModel.EyePositionAndVelocity.v1_1_1.src;

nslModule EyePositionAndVelocity(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  EyePositionAndVelocity
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat0 ltn(); // 
public NslDinFloat0 rtn(); // 
public NslDinFloat0 utn(); // 
public NslDinFloat0 dtn(); // 
public NslDinFloat0 lebn(); // 
public NslDinFloat0 rebn(); // 
public NslDinFloat0 uebn(); // 
public NslDinFloat0 debn(); // 
public NslDinFloat0 ltnChange(); // 
public NslDinFloat0 rtnChange(); // 
public NslDinFloat0 utnChange(); // 
public NslDinFloat0 dtnChange(); // 
public NslDoutFloat0 horizontalVelocity(); // 
public NslDoutFloat0 verticalVelocity(); // 
public NslDoutFloat0 horizontalTheta(); // 
public NslDoutFloat0 verticalTheta(); // 
private NslFloat0 eyeH_k1; // 
private NslFloat0 eyeV_k1; // 
private NslFloat0 eyeH_k2; // 
private NslFloat0 eyeV_k2; // 
private NslFloat0 vv_k1; // 
private NslFloat0 vv_k2; // 
private NslFloat0 vv_k3; // 
private NslFloat0 hv_k1; // 
private NslFloat0 hv_k2; // 
private NslFloat0 hv_k3; // 
private NslFloat0 horizontalTheta_k1; // 
private NslFloat0 verticalTheta_k1; // 
private NslFloat0 eyeVdown_k1; // 
private NslFloat0 eyeHleft_k1; // 
private NslFloat0 eyeVdown_k2; // 
private NslFloat0 eyeHleft_k2; // 
private NslFloat0 eyeV; // 
private NslFloat0 eyeH; // 
private NslFloat0 eyeVdown; // 
private NslFloat0 eyeHleft; // 

//methods 
public void initRun() 
{
	horizontalVelocity=0;;
	verticalVelocity=0;
	horizontalTheta=0;
	verticalTheta=0;
	eyeV=0;
	eyeH=0;
	eyeVdown=0;
	eyeHleft=0;

	eyeV_k1 =154;
	eyeV_k2 = 0.3636364;
	eyeH_k1 = 154;
	eyeH_k2 = 0.3636364;
	vv_k1 = 0;
	vv_k2 = 17;
	vv_k3 = 17;
	hv_k1 = 0;
	hv_k2 = 17;
	hv_k3 = 17;
	horizontalTheta_k1 = 0.1;
	verticalTheta_k1 = 0.1;
	eyeVdown_k1 = 154;
	eyeVdown_k2 = 0.3636364;
	eyeHleft_k1 = 154;
	eyeHleft_k2 = 0.3636364;
}
	
public void simRun() 
{
	eyeH = eyeH_k2*rtn - 56;
	eyeV = eyeV_k2*utn - 56; 
	verticalVelocity = vv_k1*uebn - vv_k1*debn + vv_k2*utnChange - vv_k3*dtnChange;
	horizontalVelocity = hv_k1*rebn - hv_k1*lebn + hv_k2*rtnChange - hv_k3*ltnChange;
	horizontalTheta = horizontalTheta_k1*eyeH;
	verticalTheta = verticalTheta_k1*eyeV;

	//NOTE: eyeHdown and eyeVleft represents gaze angle, and is not used in the
	//model, but only as an indicator of eye position for experimenter
	eyeVdown = eyeVdown_k2*dtn - 56;
	eyeHleft = eyeHleft_k2*ltn - 56;

	if (system.debug>=12) 
	{
		nslPrintln("EyePos: simRun: horizontalVelocity:"+horizontalVelocity);
		nslPrintln("EyePos: simRun: verticalVelocity:"+verticalVelocity);
		nslPrintln("EyePos: simRun: horizontalTheta:"+horizontalTheta);
		nslPrintln("EyePos: simRun: verticalTheta:"+verticalTheta);
	}
}
public void makeConn(){
}
}//end EyePositionAndVelocity

