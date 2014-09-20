package MaxSelectorModel.Vlayer.v1_1_1.src;

nslJavaModule Vlayer(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModel
//moduleName:  Vlayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 u_in(size); // 
public NslDoutDouble0 vf(); // 
private NslDouble0 vp; // 
private NslDouble0 hv; // 
private double tau; // 

//methods 
public void initModule() 
{
	hv.nslSetAccess('W');
	vf=0;
	vp=0;
	tau=1.0;
	hv = 0.5;
}

public void simRun() 
{
	vp = nslDiff(vp, tau, -vp + nslSum(u_in)-hv);
	vf = nslRamp(vp);
}
public void makeConn(){
}
}//end Vlayer

