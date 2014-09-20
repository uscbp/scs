package DomineyModel.LLBN.v1_1_1.src;
nslImport DomineyModel.DomineyLib.v1_1_1.src.*;

nslModule LLBN(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  LLBN
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 supcol(stdsz, stdsz); // 
public NslDinFloat2 fefsac(stdsz, stdsz); // 
public NslDoutFloat2 llbn(stdsz, stdsz); // 
private NslFloat0 llbnPot_tm; // 
private NslFloat0 llbnPot_k1; // 
private NslFloat0 llbnPot_k3; // 
private NslFloat0 llbn_kx1; // 
private NslFloat0 llbn_kx2; // 
private NslFloat0 llbn_ky1; // 
private NslFloat0 llbn_ky2; // 
private NslFloat2 llbnwta(stdsz, stdsz); // 
private NslFloat2 llbnPot(stdsz,stdsz); // long lead burst neurons of the brainstem saccade
private NslFloat0 nWTAThreshold; // 
private NslFloat0 protocolNum; // 
public DomineyLib domineyLib(); // 

//methods 
public void initModule() 
{
	llbnPot_k1.nslSetAccess('W');  // adaptaion factor for lesion FEF
	llbnPot_k3.nslSetAccess('W');  // adaptaion factor for lesion SCS
}

public void initRun() 
{
	nWTAThreshold=(NslFloat0)nslGetValue("domineyModel.nWTAThreshold");
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");

	llbn=0;
	llbnwta=0;
	llbnPot=0;
        
	llbnPot_tm = 0.08;
	// aa: From the 92 paper is says that the connection strength
	// from SC to LLBN
	// is increase from 2.67 to 5.0 for 14
	// However this is not in the 2.1.7 stimulus file.
	llbnPot_k1 = 2.67;
	if (protocolNum==14) 
	{
		llbnPot_k1 = 5.0; // aa: lesioning of FEF causes
				// SC projections to LLBN to increase
	}
	// aa: From the 92 paper is says that the connection strength
	// from FEF to LLBN
	// is increase from 5.4 to 9.4 for 13
	// However this is not in the 2.1.7 stimulus file.
	llbnPot_k3 = 5.4;
	if (protocolNum==13)
	{
		 llbnPot_k3 = 9.4; // aa: lesioning of SC causes 
			// FEF projections to LLBN to increase
	}

	llbn_kx1 = 0;
	llbn_kx2 = 950;
	llbn_ky1 = 0;
	llbn_ky2 = 950; 
}

public void simRun()
{
	llbnPot=nslDiff(llbnPot,llbnPot_tm,-llbnPot + llbnPot_k1*supcol+ llbnPot_k3*fefsac);	// 		// visualinput from SC and FEF
	llbnwta = domineyLib.winnerTakeAll(llbnPot,nWTAThreshold.get(),stdsz);

		// the winner take all is what allows a stimulated
		// saccade to interrupt an ongoing saccade - 
		// implies that weighted averageing occurs upstream

		// note that in the double saccades, the llbnPot (membrane
		// Potential) layer sometimes shows activity at multiple sites

	llbn = nslSaturation(llbnwta,llbn_kx1,llbn_kx2,llbn_ky1,llbn_ky2);

	if (system.debug>=5) 
	{
		nslPrintln("debug: LLBN: ");
		nslPrintln(llbn);
	}
}
public void makeConn(){
}
}//end LLBN

