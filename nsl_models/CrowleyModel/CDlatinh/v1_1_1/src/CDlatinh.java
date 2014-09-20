/** 
 CDlatinh class
 Represents the Caudate Non-dopaminergic Interneuron Cells Layer
see     CDlatinh
version 0.1 96-11-19
author  Michael Crowley
var public PFCgo_in - input coming from PFC module (of type NslDouble2)
var public FEFsac_in - input coming from FEF module (of type NslDouble2)
var public CDlatinh_out - output going to CDlattan module (of type NslDouble2)
*/
package CrowleyModel.CDlatinh.v1_1_1.src;

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

public class CDlatinh extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  CDlatinh
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 FEFsac_in; // 
public  NslDinDouble2 PFCgo_in; // 
public  NslDoutDouble2 CDlatinh_out; // 
private  NslDouble2 cdlatinh; // 
private double cdlatinhtm; // 
private double CDlisigma1; // 
private double CDlisigma2; // 
private double CDlisigma3; // 
private double CDlisigma4; // 

//methods 
public void initRun () 
{
	CDlatinh_out.set(0);
	cdlatinhtm = 0.01;
	CDlisigma1 = 45;
	CDlisigma2 = 90;
	CDlisigma3 = 0;
	CDlisigma4 = 20; // 60;
}

public void simRun () 
{
	// System.err.println("@@@@ CDlatinh simRun entered @@@@");
	cdlatinh.set(  system.nsldiff.eval(cdlatinh,cdlatinhtm, __tempCDlatinh2.setReference(NslAdd.eval(__tempCDlatinh2.get(), __tempCDlatinh1.setReference(NslAdd.eval(__tempCDlatinh1.get(), __tempCDlatinh0.setReference(NslSub.eval(__tempCDlatinh0.get(), 0, cdlatinh)), FEFsac_in)), PFCgo_in)) ));
	CDlatinh_out.set(  Nsl2Sigmoid.eval (cdlatinh,CDlisigma1, CDlisigma2, CDlisigma3, CDlisigma4));
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
	int CorticalArraySize;

	/* Temporary variables */
		NslDouble2 __tempCDlatinh0 = new NslDouble2(1, 1);
		NslDouble2 __tempCDlatinh1 = new NslDouble2(1, 1);
		NslDouble2 __tempCDlatinh2 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public CDlatinh(String nslName, NslModule nslParent, int CorticalArraySize)
{
		super(nslName, nslParent);
		this.CorticalArraySize=CorticalArraySize;
		initSys();
		makeInstCDlatinh(nslName, nslParent, CorticalArraySize);
	}

	public void makeInstCDlatinh(String nslName, NslModule nslParent, int CorticalArraySize)
{ 
		Object[] nslArgs=new Object[]{CorticalArraySize};
		callFromConstructorTop(nslArgs);
		FEFsac_in = new NslDinDouble2("FEFsac_in", this, CorticalArraySize, CorticalArraySize);
		PFCgo_in = new NslDinDouble2("PFCgo_in", this, CorticalArraySize, CorticalArraySize);
		CDlatinh_out = new NslDoutDouble2("CDlatinh_out", this, CorticalArraySize, CorticalArraySize);
		cdlatinh = new NslDouble2("cdlatinh", this, CorticalArraySize, CorticalArraySize);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end CDlatinh

