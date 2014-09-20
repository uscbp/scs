package DomineyModel.BasalGanglia.v1_1_1.src;
nslImport DomineyModel.Caudate.v1_1_1.src.*;
nslImport DomineyModel.SNR.v1_1_1.src.*;

nslModule BasalGanglia(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  BasalGanglia
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 fefmem(stdsz,stdsz); // 
public NslDinFloat2 fefsac(stdsz,stdsz); // 
public NslDoutFloat2 snrmem(stdsz,stdsz); // 
public NslDoutFloat2 snrsac(stdsz,stdsz); // 
public Caudate cd(stdsz); // 
public SNR snr(stdsz); // 

//methods 
public void simRun()
{
	if (system.debug>=21) 
	{
		nslPrintln("BG: simRun");
	}
}
public void makeConn(){
    nslRelabel(fefmem,cd.fefmem);
    nslRelabel(fefsac,cd.fefsac);
    nslRelabel(snr.snrmem,snrmem);
    nslRelabel(snr.snrsac,snrsac);
    nslConnect(cd.cdmem,snr.cdmem);
    nslConnect(cd.cdsac,snr.cdsac);
}
}//end BasalGanglia

