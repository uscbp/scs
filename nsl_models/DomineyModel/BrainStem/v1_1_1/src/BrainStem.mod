package DomineyModel.BrainStem.v1_1_1.src;
nslImport DomineyModel.LLBN.v1_1_1.src.*;
nslImport DomineyModel.Motor.v1_1_1.src.*;
nslImport DomineyModel.EyePositionAndVelocity.v1_1_1.src.*;
nslImport DomineyModel.TNChange.v1_1_1.src.*;
nslImport DomineyModel.Bursters.v1_1_1.src.*;

nslModule BrainStem(int stdsz, int numOfDirections, NslFloat0 nWTAThreshold){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  BrainStem
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 supcol(stdsz, stdsz); // 
public NslDinFloat2 fefsac(stdsz, stdsz); // 
public NslDoutFloat0 horizontalVelocity(); // 
public NslDoutFloat0 verticalVelocity(); // 
public NslDoutFloat0 horizontalTheta(); // 
public NslDoutFloat0 verticalTheta(); // 
public NslDoutFloat2 saccademask(stdsz, stdsz); // 
public LLBN llbnl(stdsz); // 
public Motor motorl(stdsz,1); // 
public Motor motorr(stdsz,2); // 
public Motor motoru(stdsz,3); // 
public Motor motord(stdsz,4); // 
public EyePositionAndVelocity eyePosAndVell(); // 
public TNChange tnChangel(); // 
public TNChange tnChanger(); // 
public TNChange tnChangeu(); // 
public TNChange tnChanged(); // 
public Bursters burstersl(stdsz); // 

//methods 
public void simRun() 
{
	if (system.debug>=2) 
	{
		nslPrintln("BrainStem:simRun");
	}
}
public void makeConn(){
    nslRelabel(supcol,llbnl.supcol);
    nslRelabel(supcol,motorl.supcol);
    nslRelabel(supcol,motorr.supcol);
    nslRelabel(supcol,motoru.supcol);
    nslRelabel(supcol,motord.supcol);
    nslRelabel(fefsac,llbnl.fefsac);
    nslRelabel(fefsac,motorl.fefsac);
    nslRelabel(fefsac,motorr.fefsac);
    nslRelabel(fefsac,motoru.fefsac);
    nslRelabel(fefsac,motord.fefsac);
    nslConnect(motord.ebn,burstersl.debn);
    nslConnect(motord.ebn,tnChanged.ebn);
    nslConnect(motord.ebn,tnChangeu.opposite_ebn);
    nslConnect(motord.ebn,eyePosAndVell.debn);
    nslConnect(motord.fefgate,tnChanged.fefgate);
    nslConnect(motord.tnDelta,tnChanged.tnDelta);
    nslRelabel(burstersl.saccademask,saccademask);
    nslConnect(tnChangeu.tn,motoru.tn);
    nslConnect(tnChangeu.tn,eyePosAndVell.utn);
    nslConnect(tnChangeu.tnChange,eyePosAndVell.utnChange);
    nslConnect(motoru.ebn,burstersl.uebn);
    nslConnect(motoru.ebn,tnChangeu.ebn);
    nslConnect(motoru.ebn,tnChanged.opposite_ebn);
    nslConnect(motoru.ebn,eyePosAndVell.uebn);
    nslConnect(motoru.fefgate,tnChangeu.fefgate);
    nslConnect(motoru.tnDelta,tnChangeu.tnDelta);
    nslConnect(tnChanged.tn,motord.tn);
    nslConnect(tnChanged.tn,eyePosAndVell.dtn);
    nslConnect(tnChanged.tnChange,eyePosAndVell.dtnChange);
    nslConnect(tnChanger.tn,motorr.tn);
    nslConnect(tnChanger.tn,eyePosAndVell.rtn);
    nslConnect(tnChanger.tnChange,eyePosAndVell.rtnChange);
    nslConnect(tnChangel.tn,motorl.tn);
    nslConnect(tnChangel.tn,eyePosAndVell.ltn);
    nslConnect(tnChangel.tnChange,eyePosAndVell.ltnChange);
    nslConnect(motorr.ebn,burstersl.rebn);
    nslConnect(motorr.ebn,tnChanger.ebn);
    nslConnect(motorr.ebn,tnChangel.opposite_ebn);
    nslConnect(motorr.ebn,eyePosAndVell.rebn);
    nslConnect(motorr.fefgate,tnChanger.fefgate);
    nslConnect(motorr.tnDelta,tnChanger.tnDelta);
    nslRelabel(eyePosAndVell.horizontalVelocity,horizontalVelocity);
    nslRelabel(eyePosAndVell.verticalVelocity,verticalVelocity);
    nslRelabel(eyePosAndVell.horizontalTheta,horizontalTheta);
    nslRelabel(eyePosAndVell.verticalTheta,verticalTheta);
    nslConnect(llbnl.llbn,motorl.llbn);
    nslConnect(llbnl.llbn,motord.llbn);
    nslConnect(llbnl.llbn,motorr.llbn);
    nslConnect(llbnl.llbn,motoru.llbn);
    nslConnect(motorl.ebn,burstersl.lebn);
    nslConnect(motorl.ebn,tnChangel.ebn);
    nslConnect(motorl.ebn,eyePosAndVell.lebn);
    nslConnect(motorl.ebn,tnChanger.opposite_ebn);
    nslConnect(motorl.fefgate,tnChangel.fefgate);
    nslConnect(motorl.tnDelta,tnChangel.tnDelta);
}
}//end BrainStem

