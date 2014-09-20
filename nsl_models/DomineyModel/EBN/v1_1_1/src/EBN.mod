package DomineyModel.EBN.v1_1_1.src;

nslModule EBN(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  EBN
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat0 pause(); // 
public NslDinFloat2 mlbn(stdsz, stdsz); // 
public NslDoutFloat0 ebn(); // 
private NslFloat0 ebnPot_tm; // 
private NslFloat0 ebnPot_k1; // 
private NslFloat0 ebn_k1; // 
private NslFloat0 ebn_k2; // 
private NslFloat0 ebn_k3; // 
private NslFloat0 ebnPot; // excitatory burst neurons of the brainstem saccade generator
private NslFloat0 mlbnMax; // medium long lead burst neurons max

//methods 
public void initRun() 
{
	ebn=0;
	ebnPot=0;
	mlbnMax=0;

	ebnPot_tm = 0.006;
	ebnPot_k1 = 10;
	ebn_k1 = 240;
	ebn_k2 = 0;
	ebn_k3 = 240;
}

public void simRun() 
{
	mlbnMax= nslMaxValue(mlbn);
	ebnPot= nslDiff(ebnPot,ebnPot_tm,-ebnPot + mlbnMax -ebnPot_k1*pause);
	ebn = nslRamp(ebnPot,ebn_k1,ebn_k2,ebn_k3);
	if (system.debug>=8) 
	{
		nslPrintln("EBN:simRun: ebn:"+ebn);
		nslPrintln("EBN:simRun: mlbnMax:"+mlbnMax);
	}
}
public void makeConn(){
}
}//end EBN

