package DomineyModel.Bursters.v1_1_1.src;

nslModule Bursters(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Bursters
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat0 lebn(); // 
public NslDinFloat0 rebn(); // 
public NslDinFloat0 uebn(); // 
public NslDinFloat0 debn(); // 
public NslDoutFloat2 saccademask(stdsz,stdsz); // 
private NslFloat0 saccadebool_k1; // 
private NslFloat0 saccadebool_k2; // 
private NslFloat0 saccadebool_k3; // 
private NslFloat0 bursters; // 

//methods 
public void initRun() 
{
	saccadebool_k1=240;
	saccadebool_k2=1;
	saccadebool_k3=0;
}

public void simRun() 
{
	bursters = uebn + debn + lebn + rebn;
	// aa: this is reverse of what is intuitive
	// if x<240, then y=1.0
	// if x>=240, then y=0.0
	saccademask = nslStep(bursters,saccadebool_k1,saccadebool_k2,saccadebool_k3);
	if (system.debug>=14) 
	{
		nslPrintln("Bursters:simRun: saccademask");
		nslPrintln(saccademask);
	}
}
public void makeConn(){
}
}//end Bursters

