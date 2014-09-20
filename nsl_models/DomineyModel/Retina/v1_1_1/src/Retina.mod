package DomineyModel.Retina.v1_1_1.src;
nslImport DomineyModel.DomineyLib.v1_1_1.src.*;

nslModule Retina(int stdsz,int bigsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Retina
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 saccademask(stdsz, stdsz); // 
public NslDinFloat2 visualinput(bigsz,bigsz); // 
public NslDinFloat0 horizontalTheta(); // 
public NslDinFloat0 verticalTheta(); // 
public NslDoutFloat2 retina(stdsz,stdsz); // 
private NslFloat0 retinaPot_tm; // 
private NslFloat2 retinaPot(stdsz,stdsz); // 
private NslFloat2 retinaPot1(stdsz,stdsz); // 
public DomineyLib domineyLib(); // 

//methods 
public void initRun() 
{
	retina=0;
	retinaPot=0;
	retinaPot1=0;
	retinaPot_tm=.006;
}

public void simRun()
{
	retinaPot1 = domineyLib.eyeMove(visualinput,horizontalTheta,verticalTheta,stdsz,bigsz);
	retinaPot=nslDiff(retinaPot,retinaPot_tm, - retinaPot + retinaPot1);
	retina = saccademask^retinaPot;

	if (system.debug>=15) 
	{
		nslPrintln("debug: Retina:retinaPot1 ");
		nslPrintln(retinaPot1);
		nslPrintln("debug: Retina:retinaPot ");
		nslPrintln(retinaPot);
		nslPrintln("debug: Retina:retina ");
		nslPrintln(retina);
	}
}
public void makeConn(){
}
}//end Retina

