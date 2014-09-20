package DomineyModel.Motor.v1_1_1.src;
nslImport DomineyModel.MLBN.v1_1_1.src.*;
nslImport DomineyModel.EBN.v1_1_1.src.*;
nslImport DomineyModel.Pause.v1_1_1.src.*;
nslImport DomineyModel.TNDelta.v1_1_1.src.*;
nslImport DomineyModel.MN.v1_1_1.src.*;
nslImport DomineyModel.STM.v1_1_1.src.*;

nslModule Motor(int stdsz, int direction){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Motor
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 supcol(stdsz,stdsz); // 
public NslDinFloat2 llbn(stdsz,stdsz); // 
public NslDinFloat2 fefsac(stdsz,stdsz); // 
public NslDinFloat0 tn(); // 
public NslDoutFloat0 ebn(); // 
public NslDoutFloat0 fefgate(); // 
public NslDoutFloat0 mn(); // 
public NslDoutFloat0 tnDelta(); // 
public MLBN mlbnl(stdsz); // 
public EBN ebnl(stdsz); // 
public Pause pausel(stdsz); // 
public TNDelta tnDeltal(stdsz); // 
public MN mnl(); // 
public STM stml(stdsz, direction); // 

//methods 
public void initModule() 
{
	if (system.debug>=6) 
	{
		nslPrintln("debug:Motor:initModule:direction "+direction);
	}

}

public void initRun() 
{
	if (system.debug>=6) 
	{
		nslPrintln("debug:Motor:initRun:direction "+direction);
	}
}
public void makeConn(){
    nslRelabel(llbn,mlbnl.llbn);
    nslRelabel(supcol,pausel.supcol);
    nslRelabel(fefsac,pausel.fefsac);
    nslRelabel(fefsac,tnDeltal.fefsac);
    nslRelabel(tn,mnl.tn);
    nslRelabel(tn,tnDeltal.tn);
    nslConnect(mlbnl.mlbn,ebnl.mlbn);
    nslConnect(mlbnl.mlbn,pausel.mlbn);
    nslRelabel(mnl.mn,mn);
    nslConnect(stml.stm,mlbnl.stm);
    nslConnect(stml.stm,pausel.stm);
    nslConnect(stml.weights,pausel.weights);
    nslRelabel(tnDeltal.fefgate,fefgate);
    nslRelabel(tnDeltal.tnDelta,tnDelta);
    nslConnect(pausel.pause,ebnl.pause);
    nslConnect(ebnl.ebn,mnl.ebn);
    nslRelabel(ebnl.ebn,ebn);
    nslConnect(ebnl.ebn,pausel.ebn);
}
}//end Motor

