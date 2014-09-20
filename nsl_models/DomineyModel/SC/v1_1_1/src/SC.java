package DomineyModel.SC.v1_1_1.src;
import DomineyModel.DomineyLib.v1_1_1.src.*;

/*********************************/
/*                               */
/*   Importing all Nsl classes   */
/*                               */
/*********************************/

import nslj.src.system.*;
import nslj.src.cmd.*;
import nslj.src.lang.*;
import nslj.src.math.*;
import nslj.src.display.*;
import nslj.src.display.j3d.*;

/*********************************/

public class SC extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  SC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 snrsac; // 
public  NslDinFloat2 fefsac; // 
public  NslDinFloat2 stimulation; // 
public  NslDinFloat2 fon; // 
public  NslDinFloat2 retina; // 
public  NslDinFloat2 ppqv; // 
public  NslDinFloat2 saccademask; // 
public  NslDoutFloat2 scqv; // 
public  NslDoutFloat2 supcol; // 
public  NslDoutFloat2 scsac; // 
public  NslDoutFloat2 scDelay; // 
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
private  NslFloat2 scsupPot; // 
private  NslFloat2 scqvPot; // 
private  NslFloat2 scsacPot; // 
private  NslFloat2 supcolPot; // 
private  NslFloat2 scsup; // 
private  NslFloat2 ppqv_winner; // winner take all resultant
private  NslFloat2 sc_winner; // winner take all resultant
private NslFloat0 nWTAThreshold; // 
private NslInt0 protocolNum; // 
private int center; // 
public  DomineyLib domineyLib; // 

//methods 
public void initModule() 
{
	supcol.nslSetAccess('W');
	supcol_k3.nslSetAccess('W');
}

public void initRun() 
{
	center = (int)stdsz/2;
	nWTAThreshold.set(  (NslFloat0)nslGetValue("domineyModel.nWTAThreshold"));
	protocolNum.set(-1);
	protocolNum.set( (NslInt0)nslGetValue("domineyModel.protocolNum"));

	scqv.set(0);
	supcol.set(0);
	scDelay.set(0);
	scsupPot.set(0);
	scqvPot.set(0);
	scsacPot.set(0);
	supcolPot.set(0);
	scsac.set(0);
	scsup.set(0);
	ppqv_winner.set(0);
	sc_winner.set(0);

	scsupPot_tm.set( .03) ;
	scqvPot_tm.set(  .01);
	scsacPot_tm.set( .01) ;  //aa: 0.3 is mentioned in 2.1.7
	supcolPot_tm.set(  .038);
	scDelay_tm.set( 0.1) ;
	scsupPot_k1.set( 15) ;
	scsupPot_k2.set( 1) ;
	scsacPot_k1.set(  1);
	scsacPot_k2.set(  1.1);
	supcolPot_k1.set( 0);
	supcolPot_k2.set( 1) ;
	supcolPot_k3.set( 1.5) ;
	supcolPot_k4.set( 2) ;
	supcolPot_k5.set(  0);  // since k5 is always 0, we can remove it
				 // from calculations
	supcolPot_k6.set( 4) ;  // published doc has 2 for all experiments
	scsup_x1.set( 70) ;
	scsup_x2.set(  90);
	scsup_y1.set( 0) ;
	scsup_y2.set( 90) ;
	scsac_x1.set( 80) ;
	scsac_x2.set( 90) ;
	scsac_y1.set( 0) ;
	scsac_y2.set( 90) ;
	supcol_x1.set( 85);
	supcol_x2.set( 99) ;
	supcol_y1.set( 0) ;
	supcol_y2.set( 500) ;	
	supcol_k3.set( 0);


	// 98/11/18 aa: values for supcol_k3 depend on protocolNum:
	// 0 for single and double and memory
	// supcol_k3 =2.6 for stimulation SC CompensatoryI - why?
	// supcol_k3 =3.5 for stimulation SC CompensatoryII - why?
	// supcol_k3 =7 for collision - why?
	// published doc has supcol_k3=2.9 for experiment 13 but in 
	// the potential equation
	

	supcol_k3.set(0);  // if stimulation is not used 
	// if sc compI
	if ((protocolNum.get()==9)||(protocolNum.get()==14)) 
	{
		supcol_k3.set(2.6);  
	}
	// if sc compensatoryII
	else if (protocolNum.get()==10) 
	{ 
               supcol_k3.set(3.5);  
	} 
	//not doing collision protocolNums // if collision ??
	//              supcol_k3=7;  
}
	
public void simRun() 
{
	scsupPot.set(system.nsldiff.eval(scsupPot,scsupPot_tm, __tempSC4.setReference(NslAdd.eval(__tempSC4.get(), __tempSC3.setReference(NslSub.eval(__tempSC3.get(), __tempSC0.setReference(NslSub.eval(__tempSC0.get(), 0, scsupPot)), __tempSC1.setReference(NslElemMult.eval(__tempSC1.get(), scsupPot_k1, fon)))), __tempSC2.setReference(NslElemMult.eval(__tempSC2.get(), scsupPot_k2, retina))))));

	ppqv_winner.set(  domineyLib.winnerTakeAll(ppqv,nWTAThreshold.get(),stdsz));

	scqvPot.set(system.nsldiff.eval(scqvPot,scqvPot_tm,__tempSC6.setReference(NslAdd.eval(__tempSC6.get(), __tempSC5.setReference(NslSub.eval(__tempSC5.get(), 0, scqvPot)), ppqv_winner)) ));

	scsacPot.set(system.nsldiff.eval(scsacPot,scsacPot_tm, __tempSC11.setReference(NslSub.eval(__tempSC11.get(), __tempSC10.setReference(NslAdd.eval(__tempSC10.get(), __tempSC7.setReference(NslSub.eval(__tempSC7.get(), 0, scsacPot)), __tempSC8.setReference(NslElemMult.eval(__tempSC8.get(), scsacPot_k1, fefsac)))), __tempSC9.setReference(NslElemMult.eval(__tempSC9.get(), scsacPot_k2, snrsac))))));

	// supcolPot=nslDiff(supcolPot,supcolPot_tm, -supcolPot  + 
	//	supcolPot_k2*scsac + 
	//	supcolPot_k3*scqv -
	//	supcolPot_k4*fon  + 
	//	supcolPot_k5*stimulation +  //removing because k5 always 0
	//	supcolPot_k6*scsup - 
	//	supcolPot_k1*scDelay); // this is zero.
	supcolPot.set(system.nsldiff.eval(supcolPot,supcolPot_tm, __tempSC22.setReference(NslSub.eval(__tempSC22.get(), __tempSC21.setReference(NslAdd.eval(__tempSC21.get(), __tempSC20.setReference(NslSub.eval(__tempSC20.get(), __tempSC19.setReference(NslAdd.eval(__tempSC19.get(), __tempSC18.setReference(NslAdd.eval(__tempSC18.get(), __tempSC12.setReference(NslSub.eval(__tempSC12.get(), 0, supcolPot)), __tempSC13.setReference(NslElemMult.eval(__tempSC13.get(), supcolPot_k2, scsac)))), __tempSC14.setReference(NslElemMult.eval(__tempSC14.get(), supcolPot_k3, scqv)))), __tempSC15.setReference(NslElemMult.eval(__tempSC15.get(), supcolPot_k4, fon)))), __tempSC16.setReference(NslElemMult.eval(__tempSC16.get(), supcolPot_k6, scsup)))), __tempSC17.setReference(NslElemMult.eval(__tempSC17.get(), supcolPot_k1, scDelay)))))); // this is zero.
	// aa: should we remove the kx*stimulation factor
	// from both supcol and fefsac force equations 
	// and put the kx*stimulation back in the potential calculation as
	// it is in the published paper ? 

	supcolPot.set(center, center,   0); // no saccades to where we already are! 

	sc_winner.set(  domineyLib.winnerTakeAll(supcolPot,nWTAThreshold.get(),stdsz));

	scsup.set(  NslSigmoid.eval(scsupPot,scsup_x1,scsup_x2,scsup_y1,scsup_y2));

	scqv.set(  (__tempSC23.setReference(NslElemMult.eval(__tempSC23.get(), saccademask, scqvPot))));

	scsac.set(  NslSigmoid.eval(scsacPot,scsac_x1,scsac_x2,scsac_y1,scsac_y2));

	//aa: from the 92 paper equation 15 is set  to zero if lesioning SC
        if ((protocolNum.get()==6)||(protocolNum.get()==13))  
	{ 
		// lesion SC
               supcol.set(0);
        } 
	else 
	{
		supcol.set(  NslSigmoid.eval(sc_winner,supcol_x1,supcol_x2,supcol_y1,supcol_y2)); 	
		supcol.set(  __tempSC25.setReference(NslAdd.eval(__tempSC25.get(), supcol, (__tempSC24.setReference(NslElemMult.eval(__tempSC24.get(), supcol_k3, stimulation))))));
        }

	scDelay.set(system.nsldiff.eval(scDelay,scDelay_tm, __tempSC27.setReference(NslAdd.eval(__tempSC27.get(), __tempSC26.setReference(NslSub.eval(__tempSC26.get(), 0, scDelay)), supcol)) ));

	if (system.debug>=24) 
	{
 		system.nslPrintln("SC: :supcolPot ");
		system.nslPrintln(supcolPot);
 		system.nslPrintln("SC: :scqv ");
		system.nslPrintln(scqv);
 		system.nslPrintln("SC: :scDelay ");
		system.nslPrintln(scDelay);
 		system.nslPrintln("SC: supcol ");
		system.nslPrintln(supcol);
 		system.nslPrintln("SC: scsac ");
		system.nslPrintln(scsac);
	}
}
public void makeConn(){
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */
	int stdsz;

	/* Temporary variables */
		NslFloat2 __tempSC0 = new NslFloat2(1, 1);
		NslFloat2 __tempSC1 = new NslFloat2(1, 1);
		NslFloat2 __tempSC2 = new NslFloat2(1, 1);
		NslFloat2 __tempSC3 = new NslFloat2(1, 1);
		NslFloat2 __tempSC4 = new NslFloat2(1, 1);
		NslFloat2 __tempSC5 = new NslFloat2(1, 1);
		NslFloat2 __tempSC6 = new NslFloat2(1, 1);
		NslFloat2 __tempSC7 = new NslFloat2(1, 1);
		NslFloat2 __tempSC8 = new NslFloat2(1, 1);
		NslFloat2 __tempSC9 = new NslFloat2(1, 1);
		NslFloat2 __tempSC10 = new NslFloat2(1, 1);
		NslFloat2 __tempSC11 = new NslFloat2(1, 1);
		NslFloat2 __tempSC12 = new NslFloat2(1, 1);
		NslFloat2 __tempSC13 = new NslFloat2(1, 1);
		NslFloat2 __tempSC14 = new NslFloat2(1, 1);
		NslFloat2 __tempSC15 = new NslFloat2(1, 1);
		NslFloat2 __tempSC16 = new NslFloat2(1, 1);
		NslFloat2 __tempSC17 = new NslFloat2(1, 1);
		NslFloat2 __tempSC18 = new NslFloat2(1, 1);
		NslFloat2 __tempSC19 = new NslFloat2(1, 1);
		NslFloat2 __tempSC20 = new NslFloat2(1, 1);
		NslFloat2 __tempSC21 = new NslFloat2(1, 1);
		NslFloat2 __tempSC22 = new NslFloat2(1, 1);
		NslFloat2 __tempSC23 = new NslFloat2(1, 1);
		NslFloat2 __tempSC24 = new NslFloat2(1, 1);
		NslFloat2 __tempSC25 = new NslFloat2(1, 1);
		NslFloat2 __tempSC26 = new NslFloat2(1, 1);
		NslFloat2 __tempSC27 = new NslFloat2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public SC(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstSC(nslName, nslParent, stdsz);
	}

	public void makeInstSC(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		snrsac = new NslDinFloat2("snrsac", this, stdsz, stdsz);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		stimulation = new NslDinFloat2("stimulation", this, stdsz, stdsz);
		fon = new NslDinFloat2("fon", this, stdsz, stdsz);
		retina = new NslDinFloat2("retina", this, stdsz, stdsz);
		ppqv = new NslDinFloat2("ppqv", this, stdsz, stdsz);
		saccademask = new NslDinFloat2("saccademask", this, stdsz, stdsz);
		scqv = new NslDoutFloat2("scqv", this, stdsz, stdsz);
		supcol = new NslDoutFloat2("supcol", this, stdsz, stdsz);
		scsac = new NslDoutFloat2("scsac", this, stdsz, stdsz);
		scDelay = new NslDoutFloat2("scDelay", this, stdsz, stdsz);
		scsupPot_tm = new NslFloat0("scsupPot_tm", this);
		scqvPot_tm = new NslFloat0("scqvPot_tm", this);
		scsacPot_tm = new NslFloat0("scsacPot_tm", this);
		supcolPot_tm = new NslFloat0("supcolPot_tm", this);
		scDelay_tm = new NslFloat0("scDelay_tm", this);
		scsupPot_k1 = new NslFloat0("scsupPot_k1", this);
		scsupPot_k2 = new NslFloat0("scsupPot_k2", this);
		scsacPot_k1 = new NslFloat0("scsacPot_k1", this);
		scsacPot_k2 = new NslFloat0("scsacPot_k2", this);
		supcolPot_k1 = new NslFloat0("supcolPot_k1", this);
		supcolPot_k2 = new NslFloat0("supcolPot_k2", this);
		supcolPot_k3 = new NslFloat0("supcolPot_k3", this);
		supcolPot_k4 = new NslFloat0("supcolPot_k4", this);
		supcolPot_k5 = new NslFloat0("supcolPot_k5", this);
		supcolPot_k6 = new NslFloat0("supcolPot_k6", this);
		scsup_x1 = new NslFloat0("scsup_x1", this);
		scsup_x2 = new NslFloat0("scsup_x2", this);
		scsup_y1 = new NslFloat0("scsup_y1", this);
		scsup_y2 = new NslFloat0("scsup_y2", this);
		scsac_x1 = new NslFloat0("scsac_x1", this);
		scsac_x2 = new NslFloat0("scsac_x2", this);
		scsac_y1 = new NslFloat0("scsac_y1", this);
		scsac_y2 = new NslFloat0("scsac_y2", this);
		supcol_x1 = new NslFloat0("supcol_x1", this);
		supcol_x2 = new NslFloat0("supcol_x2", this);
		supcol_y1 = new NslFloat0("supcol_y1", this);
		supcol_y2 = new NslFloat0("supcol_y2", this);
		supcol_k3 = new NslFloat0("supcol_k3", this);
		scsupPot = new NslFloat2("scsupPot", this, stdsz, stdsz);
		scqvPot = new NslFloat2("scqvPot", this, stdsz, stdsz);
		scsacPot = new NslFloat2("scsacPot", this, stdsz, stdsz);
		supcolPot = new NslFloat2("supcolPot", this, stdsz, stdsz);
		scsup = new NslFloat2("scsup", this, stdsz, stdsz);
		ppqv_winner = new NslFloat2("ppqv_winner", this, stdsz, stdsz);
		sc_winner = new NslFloat2("sc_winner", this, stdsz, stdsz);
		nWTAThreshold = new NslFloat0("nWTAThreshold", this);
		protocolNum = new NslInt0("protocolNum", this);
		domineyLib = new DomineyLib("domineyLib", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end SC

