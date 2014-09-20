/** 
 Here is the class representing the Superior Colliculus (SC) module. The
 module contains three layers SCsac, SCqv, SCbu. This is converted
 as a parent module and 4 children module (the extra module is for
 non-neural stuff).

 The Execute Saccade schema is implemented in the SC module and
 the "brainstem saccade generator".
 Once PFC has issued a GO signal, the combination of increased activity from
 PFC and decreased inhibitory activity from BG allow activation to grow in
 the SC. This activation is projected to the brainstem where motor neurons
 are excited and cause the eye muscles to move the eyes to the new target
 location.
*/
package CrowleyModel.SC.v1_1_1.src;
import CrowleyModel.SCsac.v1_1_1.src.*;
import CrowleyModel.SCqv.v1_1_1.src.*;
import CrowleyModel.SCbu.v1_1_1.src.*;

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

public class SC extends NslJavaModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 SNRlatburst_in; // 
public  NslDinDouble2 FEFsac_in; // 
public  NslDinDouble2 LIPmem_in; // 
public  NslDinDouble2 PFCfovea_in; // 
public  NslDinDouble0 BSGsaccade_in; // 
public  NslDinDouble1 BSGEyeMovement_in; // 
public  NslDoutDouble2 SCsac_out; // 
public  NslDoutDouble2 SCbu_out; // 
public  SCsac SCsac1; // 
public  SCqv SCqv1; // 
public  SCbu SCbu1; // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 

//methods 
public void initModule() 
{
	FOVEAX.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAX"));
	FOVEAY.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAY"));  
	nslAddAreaCanvas("output", "sc_sac", SCsac_out, 0, 100);
	nslAddSpatialCanvas("output", "sc_bu", SCbu_out, 0, 100);
}
public void makeConn(){
    nslConnect(SNRlatburst_in,SCsac1.SNRlatburst_in);
    nslConnect(FEFsac_in,SCsac1.FEFsac_in);
    nslConnect(LIPmem_in,SCqv1.LIPmem_in);
    nslConnect(PFCfovea_in,SCbu1.PFCfovea_in);
    nslConnect(BSGsaccade_in,SCbu1.BSGsaccade_in);
    nslConnect(BSGEyeMovement_in,SCbu1.BSGEyeMovement_in);
    nslConnect(SCqv1.SCqv_out,SCsac1.SCqv_in);
    nslConnect(SCsac1.SCsac_out,SCbu1.SCsac_in);
    nslConnect(SCsac1.SCsac_out,SCsac_out);
    nslConnect(SCbu1.SCbu_out,SCsac1.SCbu_in);
    nslConnect(SCbu1.SCbu_out,SCbu_out);
}

	/******************************************************/
	/*                                                    */
	/* Generated by nslc.src.NslCompiler. Do not edit these lines! */
	/*                                                    */
	/******************************************************/

	/* Constructor and related methods */
	/* makeinst() declared variables */

	/* Formal parameters */
	int array_size;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public SC(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstSC(nslName, nslParent, array_size);
	}

	public void makeInstSC(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		SNRlatburst_in = new NslDinDouble2("SNRlatburst_in", this, array_size, array_size);
		FEFsac_in = new NslDinDouble2("FEFsac_in", this, array_size, array_size);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, array_size, array_size);
		PFCfovea_in = new NslDinDouble2("PFCfovea_in", this, array_size, array_size);
		BSGsaccade_in = new NslDinDouble0("BSGsaccade_in", this);
		BSGEyeMovement_in = new NslDinDouble1("BSGEyeMovement_in", this, array_size);
		SCsac_out = new NslDoutDouble2("SCsac_out", this, array_size, array_size);
		SCbu_out = new NslDoutDouble2("SCbu_out", this, array_size, array_size);
		SCsac1 = new SCsac("SCsac1", this, array_size);
		SCqv1 = new SCqv("SCqv1", this, array_size);
		SCbu1 = new SCbu("SCbu1", this, array_size);
		FOVEAX = new NslInt0("FOVEAX", this);
		FOVEAY = new NslInt0("FOVEAY", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end SC

