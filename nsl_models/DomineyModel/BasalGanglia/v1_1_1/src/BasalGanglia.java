package DomineyModel.BasalGanglia.v1_1_1.src;
import DomineyModel.Caudate.v1_1_1.src.*;
import DomineyModel.SNR.v1_1_1.src.*;

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

public class BasalGanglia extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  BasalGanglia
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 fefmem; // 
public  NslDinFloat2 fefsac; // 
public  NslDoutFloat2 snrmem; // 
public  NslDoutFloat2 snrsac; // 
public  Caudate cd; // 
public  SNR snr; // 

//methods 
public void simRun()
{
	if (system.debug>=21) 
	{
		system.nslPrintln("BG: simRun");
	}
}
public void makeConn(){
    nslConnect(fefmem,cd.fefmem);
    nslConnect(fefsac,cd.fefsac);
    nslConnect(snr.snrmem,snrmem);
    nslConnect(snr.snrsac,snrsac);
    nslConnect(cd.cdmem,snr.cdmem);
    nslConnect(cd.cdsac,snr.cdsac);
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

	/* GENERIC CONSTRUCTOR: */
	public BasalGanglia(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstBasalGanglia(nslName, nslParent, stdsz);
	}

	public void makeInstBasalGanglia(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		fefmem = new NslDinFloat2("fefmem", this, stdsz, stdsz);
		fefsac = new NslDinFloat2("fefsac", this, stdsz, stdsz);
		snrmem = new NslDoutFloat2("snrmem", this, stdsz, stdsz);
		snrsac = new NslDoutFloat2("snrsac", this, stdsz, stdsz);
		cd = new Caudate("cd", this, stdsz);
		snr = new SNR("snr", this, stdsz);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end BasalGanglia

