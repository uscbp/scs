/** 
Here is the class representing the target locations for SRBNs (saccade
related burst neurons). This layer is called in the model quasi visual
layer (SCqv) layer. The SCqv cells receive their input from LIP.
*/
package CrowleyModel.SCqv.v1_1_1.src;

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

public class SCqv extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SCqv
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 LIPmem_in; // 
public  NslDoutDouble2 SCqv_out; // 
private  NslDouble2 scqv; // 
private double SCqvsigma1; // 
private double SCqvsigma2; // 
private double SCqvsigma3; // 
private double SCqvsigma4; // 
private double scqvtm; // 

//methods 
public void initRun()
{
	scqv.set(  0);
	SCqv_out.set(  0);

	SCqvsigma1 =   0;
	SCqvsigma2 =  90;
	SCqvsigma3 =   0;
	SCqvsigma4 =  90;
	scqvtm = 0.01;
}

public void simRun()
{
	// System.err.println("@@@@ SCqv simRun entered @@@@");
	scqv.set(system.nsldiff.eval(scqv,scqvtm, __tempSCqv1.setReference(NslAdd.eval(__tempSCqv1.get(), __tempSCqv0.setReference(NslSub.eval(__tempSCqv0.get(), 0, scqv)), LIPmem_in)) ));
	SCqv_out.set(Nsl2Sigmoid.eval(scqv,SCqvsigma1,SCqvsigma2,SCqvsigma3,SCqvsigma4));
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
	int array_size;

	/* Temporary variables */
		NslDouble2 __tempSCqv0 = new NslDouble2(1, 1);
		NslDouble2 __tempSCqv1 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public SCqv(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstSCqv(nslName, nslParent, array_size);
	}

	public void makeInstSCqv(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		LIPmem_in = new NslDinDouble2("LIPmem_in", this, array_size, array_size);
		SCqv_out = new NslDoutDouble2("SCqv_out", this, array_size, array_size);
		scqv = new NslDouble2("scqv", this, array_size, array_size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end SCqv
