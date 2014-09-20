package DomineyModel.SC.v1_1_1.src;
nslImport DomineyModel.DomineyLib.v1_1_1.src.*;

nslModule SC(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  SC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 snrsac(stdsz,stdsz); // 
public NslDinFloat2 fefsac(stdsz,stdsz); // 
public NslDinFloat2 stimulation(stdsz,stdsz); // 
public NslDinFloat2 fon(stdsz,stdsz); // 
public NslDinFloat2 retina(stdsz,stdsz); // 
public NslDinFloat2 ppqv(stdsz,stdsz); // 
public NslDinFloat2 saccademask(stdsz,stdsz); // 
public NslDoutFloat2 scqv(stdsz,stdsz); // 
public NslDoutFloat2 supcol(stdsz,stdsz); // 
public NslDoutFloat2 scsac(stdsz,stdsz); // 
public NslDoutFloat2 scDelay(stdsz,stdsz); // 
private NslFloat0 scsupPot_tm; // 
private NslFloat0 scqvPot_tm; // 
private NslFloat0 scsacPot_tm; // 
private NslFloat0 supcolPot_tm; // 
private NslFloat0 scDelay_tm; // 
private NslFloat0 scsupPot_k1; // 
private NslFloat0 scsupPot_k2; // 
private NslFloat0 scsacPot_k1; // 
private NslFloat0 scsacPot_k2; // 
private NslFloat0 supcolPot_k1; // 
private NslFloat0 supcolPot_k2; // 
private NslFloat0 supcolPot_k3; // 
private NslFloat0 supcolPot_k4; // 
private NslFloat0 supcolPot_k5; // 
private NslFloat0 supcolPot_k6; // 
private NslFloat0 scsup_x1; // 
private NslFloat0 scsup_x2; // 
private NslFloat0 scsup_y1; // 
private NslFloat0 scsup_y2; // 
private NslFloat0 scsac_x1; // 
private NslFloat0 scsac_x2; // 
private NslFloat0 scsac_y1; // 
private NslFloat0 scsac_y2; // 
private NslFloat0 supcol_x1; // 
private NslFloat0 supcol_x2; // 
private NslFloat0 supcol_y1; // 
private NslFloat0 supcol_y2; // 
private NslFloat0 supcol_k3; // 
private NslFloat2 scsupPot(stdsz, stdsz); // 
private NslFloat2 scqvPot(stdsz, stdsz); // 
private NslFloat2 scsacPot(stdsz, stdsz); // 
private NslFloat2 supcolPot(stdsz, stdsz); // 
private NslFloat2 scsup(stdsz, stdsz); // 
private NslFloat2 ppqv_winner(stdsz, stdsz); // winner take all resultant
private NslFloat2 sc_winner(stdsz, stdsz); // winner take all resultant
private NslFloat0 nWTAThreshold; // 
private NslInt0 protocolNum; // 
private int center; // 
public DomineyLib domineyLib(); // 

//methods 
public void initModule() 
{
	supcol.nslSetAccess('W');
	supcol_k3.nslSetAccess('W');
}

public void initRun() 
{
	center = (int)stdsz/2;
	nWTAThreshold = (NslFloat0)nslGetValue("domineyModel.nWTAThreshold");
	protocolNum=-1;
	protocolNum= (NslInt0)nslGetValue("domineyModel.protocolNum");

	scqv=0;
	supcol=0;
	scDelay=0;
	scsupPot=0;
	scqvPot=0;
	scsacPot=0;
	supcolPot=0;
	scsac=0;
	scsup=0;
	ppqv_winner=0;
	sc_winner=0;

	scsupPot_tm =.03 ;
	scqvPot_tm = .01;
	scsacPot_tm =.01 ;  //aa: 0.3 is mentioned in 2.1.7
	supcolPot_tm = .038;
	scDelay_tm =0.1 ;
	scsupPot_k1 =15 ;
	scsupPot_k2 =1 ;
	scsacPot_k1 = 1;
	scsacPot_k2 = 1.1;
	supcolPot_k1 =0;
	supcolPot_k2 =1 ;
	supcolPot_k3 =1.5 ;
	supcolPot_k4 =2 ;
	supcolPot_k5 = 0;  // since k5 is always 0, we can remove it
				 // from calculations
	supcolPot_k6 =4 ;  // published doc has 2 for all experiments
	scsup_x1 =70 ;
	scsup_x2 = 90;
	scsup_y1 =0 ;
	scsup_y2 =90 ;
	scsac_x1 =80 ;
	scsac_x2 =90 ;
	scsac_y1 =0 ;
	scsac_y2 =90 ;
	supcol_x1 =85;
	supcol_x2 =99 ;
	supcol_y1 =0 ;
	supcol_y2 =500 ;	
	supcol_k3 =0;


	// 98/11/18 aa: values for supcol_k3 depend on protocolNum:
	// 0 for single and double and memory
	// supcol_k3 =2.6 for stimulation SC CompensatoryI - why?
	// supcol_k3 =3.5 for stimulation SC CompensatoryII - why?
	// supcol_k3 =7 for collision - why?
	// published doc has supcol_k3=2.9 for experiment 13 but in 
	// the potential equation
	

	supcol_k3=0;  // if stimulation is not used 
	// if sc compI
	if ((protocolNum==9)||( protocolNum==14)) 
	{
		supcol_k3=2.6;  
	}
	// if sc compensatoryII
	else if (protocolNum==10) 
	{ 
               supcol_k3=3.5;  
	} 
	//not doing collision protocolNums // if collision ??
	//              supcol_k3=7;  
}
	
public void simRun() 
{
	scsupPot=nslDiff(scsupPot,scsupPot_tm, - scsupPot - scsupPot_k1*fon + scsupPot_k2*retina);

	ppqv_winner = domineyLib.winnerTakeAll(ppqv,nWTAThreshold.get(),stdsz);

	scqvPot=nslDiff(scqvPot,scqvPot_tm,-scqvPot + ppqv_winner);

	scsacPot=nslDiff(scsacPot,scsacPot_tm, -scsacPot +scsacPot_k1*fefsac - 
		scsacPot_k2*snrsac);

	// supcolPot=nslDiff(supcolPot,supcolPot_tm, -supcolPot  + 
	//	supcolPot_k2*scsac + 
	//	supcolPot_k3*scqv -
	//	supcolPot_k4*fon  + 
	//	supcolPot_k5*stimulation +  //removing because k5 always 0
	//	supcolPot_k6*scsup - 
	//	supcolPot_k1*scDelay); // this is zero.
	supcolPot=nslDiff(supcolPot,supcolPot_tm, -supcolPot + supcolPot_k2*scsac + supcolPot_k3*scqv -
		supcolPot_k4*fon + supcolPot_k6*scsup - supcolPot_k1*scDelay); // this is zero.
	// aa: should we remove the kx*stimulation factor
	// from both supcol and fefsac force equations 
	// and put the kx*stimulation back in the potential calculation as
	// it is in the published paper ? 

	supcolPot[center][center] = 0; // no saccades to where we already are! 

	sc_winner = domineyLib.winnerTakeAll(supcolPot,nWTAThreshold.get(),stdsz);

	scsup = nslSigmoid(scsupPot,scsup_x1,scsup_x2,scsup_y1,scsup_y2);

	scqv = (saccademask^scqvPot);

	scsac = nslSigmoid(scsacPot,scsac_x1,scsac_x2,scsac_y1,scsac_y2);

	//aa: from the 92 paper equation 15 is set  to zero if lesioning SC
        if ((protocolNum==6)|| (protocolNum==13))  
	{ 
		// lesion SC
               supcol=0;
        } 
	else 
	{
		supcol = nslSigmoid(sc_winner,supcol_x1,supcol_x2,supcol_y1,supcol_y2); 	
		supcol = supcol + (supcol_k3*stimulation);
        }

	scDelay=nslDiff(scDelay,scDelay_tm, -scDelay + supcol);

	if (system.debug>=24) 
	{
 		nslPrintln("SC: :supcolPot ");
		nslPrintln(supcolPot);
 		nslPrintln("SC: :scqv ");
		nslPrintln(scqv);
 		nslPrintln("SC: :scDelay ");
		nslPrintln(scDelay);
 		nslPrintln("SC: supcol ");
		nslPrintln(supcol);
 		nslPrintln("SC: scsac ");
		nslPrintln(scsac);
	}
}
public void makeConn(){
}
}//end SC

