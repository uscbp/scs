package DomineyModel.SNR.v1_1_1.src;

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

public class SNR extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  SNR
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 cdmem; // 
public  NslDinFloat2 cdsac; // 
public  NslDoutFloat2 snrmem; // 
public  NslDoutFloat2 snrsac; // 
private NslFloat0 snrmemPot_tm; // 
private NslFloat0 snrsacPot_tm; // 
private NslFloat0 snrmemPot_k1; // 
private NslFloat0 snrsacPot_k1; // 
private NslFloat0 snrmem_x1; // 
private NslFloat0 snrmem_x2; // 
private NslFloat0 snrmem_y1; // 
private NslFloat0 snrmem_y2; // 
private NslFloat0 snrsac_x1; // 
private NslFloat0 snrsac_x2; // 
private NslFloat0 snrsac_y1; // 
private NslFloat0 snrsac_y2; // 
private  NslFloat2 snrmemPot; // 
private  NslFloat2 snrsacPot; // 

//methods 
public void initRun() 
{
	snrmem.set(100);
	snrsac.set(100);
        snrmemPot.set(0);
        snrsacPot.set(0);

	snrmemPot_tm.set( .04) ;
	snrsacPot_tm.set(  .02);
	snrmemPot_k1.set( 1.5) ;
	snrsacPot_k1.set(  1);	
	snrmem_x1.set(0);
	snrmem_x2.set(  50);
	snrmem_y1.set( 100) ;
	snrmem_y2.set(0)  ;
	snrsac_x1.set(  0);
	snrsac_x2.set( 50) ;
	snrsac_y1.set(  100);
	snrsac_y2.set( 0) ;	
}

public void simRun() 
{
	snrsacPot.set(system.nsldiff.eval(snrsacPot,snrsacPot_tm,__tempSNR2.setReference(NslAdd.eval(__tempSNR2.get(), __tempSNR0.setReference(NslSub.eval(__tempSNR0.get(), 0, snrsacPot)), __tempSNR1.setReference(NslElemMult.eval(__tempSNR1.get(), snrsacPot_k1, cdsac))))));
	snrmemPot.set(system.nsldiff.eval(snrmemPot,snrmemPot_tm, __tempSNR5.setReference(NslAdd.eval(__tempSNR5.get(), __tempSNR3.setReference(NslSub.eval(__tempSNR3.get(), 0, snrmemPot)), __tempSNR4.setReference(NslElemMult.eval(__tempSNR4.get(), snrmemPot_k1, cdmem))))));
	snrsac.set(  NslSigmoid.eval(snrsacPot,snrsac_x1,snrsac_x2,snrsac_y1,snrsac_y2));
	snrmem.set(  NslSigmoid.eval(snrmemPot,snrmem_x1,snrmem_x2,snrmem_y1,snrmem_y2));

	if (system.debug>=23) 
	{
		system.nslPrintln("SNR: snrsac");
		system.nslPrintln(snrsac);
		system.nslPrintln("SNR: snrmem");
		system.nslPrintln(snrmem);
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
		NslFloat2 __tempSNR0 = new NslFloat2(1, 1);
		NslFloat2 __tempSNR1 = new NslFloat2(1, 1);
		NslFloat2 __tempSNR2 = new NslFloat2(1, 1);
		NslFloat2 __tempSNR3 = new NslFloat2(1, 1);
		NslFloat2 __tempSNR4 = new NslFloat2(1, 1);
		NslFloat2 __tempSNR5 = new NslFloat2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public SNR(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstSNR(nslName, nslParent, stdsz);
	}

	public void makeInstSNR(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		cdmem = new NslDinFloat2("cdmem", this, stdsz, stdsz);
		cdsac = new NslDinFloat2("cdsac", this, stdsz, stdsz);
		snrmem = new NslDoutFloat2("snrmem", this, stdsz, stdsz);
		snrsac = new NslDoutFloat2("snrsac", this, stdsz, stdsz);
		snrmemPot_tm = new NslFloat0("snrmemPot_tm", this);
		snrsacPot_tm = new NslFloat0("snrsacPot_tm", this);
		snrmemPot_k1 = new NslFloat0("snrmemPot_k1", this);
		snrsacPot_k1 = new NslFloat0("snrsacPot_k1", this);
		snrmem_x1 = new NslFloat0("snrmem_x1", this);
		snrmem_x2 = new NslFloat0("snrmem_x2", this);
		snrmem_y1 = new NslFloat0("snrmem_y1", this);
		snrmem_y2 = new NslFloat0("snrmem_y2", this);
		snrsac_x1 = new NslFloat0("snrsac_x1", this);
		snrsac_x2 = new NslFloat0("snrsac_x2", this);
		snrsac_y1 = new NslFloat0("snrsac_y1", this);
		snrsac_y2 = new NslFloat0("snrsac_y2", this);
		snrmemPot = new NslFloat2("snrmemPot", this, stdsz, stdsz);
		snrsacPot = new NslFloat2("snrsacPot", this, stdsz, stdsz);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end SNR

