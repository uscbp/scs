package DomineyModel.MN.v1_1_1.src;

nslModule MN(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  MN
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat0 ebn(); // 
public NslDinFloat0 tn(); // 
public NslDoutFloat0 mn(); // 

//methods 
public void initRun() 
{
	mn=0;
}

public void simRun() 
{
	mn = ebn + tn;

	if (system.debug>=11) 
	{
		nslPrintln("MN:simRun:mn"+mn);
	}
}
public void makeConn(){
}
}//end MN

