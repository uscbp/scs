package DomineyModel.FON.v1_1_1.src;

nslModule FON(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  FON
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 posteriorParietal(stdsz,stdsz); // 
public NslDoutFloat2 fon(stdsz, stdsz); // 
public NslDoutFloat2 fovElem(stdsz, stdsz); // 
public NslDoutFloat0 fovCenter(); // 
public NslDoutFloat0 fonCenter(); // 
private NslFloat0 fonPot_k1; // 
private NslFloat0 fon_x1; // 
private NslFloat0 fon_y1; // 
private NslFloat0 fon_y2; // 
private NslFloat2 fonPot(stdsz, stdsz); // 
private NslFloat0 fovElemCenter; // 
private int center; // 

//methods 
public void initRun() 
{
	center =(int) stdsz/2;
	fon=0;
	fovElem=0;
	fonPot=0;
	fovElemCenter=0;
	fonCenter=0;

	fonPot_k1=1;
	fon_x1=5;
	fon_y1=0;
	fon_y2=90;
}

public void simRun() 
{
	fonPot =fonPot_k1 *posteriorParietal[center][center]; 

	fon = nslStep(fonPot,fon_x1,fon_y1,fon_y2);

	fovElem[center][center] = posteriorParietal[center][center]; 

	fonCenter=fon[center][center];
	fovElemCenter=fovElem[center][center];

	if (system.debug>=20) 
	{
		nslPrintln("FON:simRun: fonCenter");
		nslPrintln(fonCenter);
		nslPrintln("FON:simRun: fovElemCenter");
		nslPrintln(fovElemCenter);
	}
}
public void makeConn(){
}
}//end FON

