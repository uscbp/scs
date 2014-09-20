package DartModel.DART_UI_module.v1_1_1.src;

nslJavaModule DART_UI_module(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  DART_UI_module
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble0 throw_in(); // Throw direction
public NslDinDouble0 p_in(); // Prism angle [0:30]
public NslDinDouble0 s_in(); // Strategy: [0:1] over/under
private NslDouble1 throw_dart(4); // graph parameter

//methods 
public void initModule()
{
	nslAddUserCanvas("dartFrame", "dartCanvas", throw_dart,0,1,"DartModel/NslDartGraphCanvas/v1_1_1/src","DartGraph");
	initTrainEpoch();
	throw_dart = -1;
}

public void initTrainEpoch() 
{
	nslRemoveFromLocalProtocols("manual");
	nslRemoveFromLocalProtocols("basic");
	nslRemoveFromLocalProtocols("transfer");
	nslRemoveFromLocalProtocols("calibration");
}  

public void initRunEpoch() 
{
	nslAddProtocolRecursiveUp("basic");
	nslAddProtocolRecursiveUp("transfer");
	nslAddProtocolRecursiveUp("calibration");     
}

//public void initRun() 
//{
//	throw_dart = -1;
//}
   
public void endRun()
{
	throw_dart[0] = system.getFinishedEpochs();
	throw_dart[1] = throw_in;
	throw_dart[2] = p_in;
	throw_dart[3] = s_in;
}
public void makeConn(){
}
}//end DART_UI_module

