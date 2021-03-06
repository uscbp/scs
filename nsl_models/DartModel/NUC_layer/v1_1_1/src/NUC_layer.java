package DartModel.NUC_layer.v1_1_1.src;

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

public class NUC_layer extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  NUC_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 pp_in; // PP input
public  NslDinDouble2 pc_in; // PC input
public  NslDoutDouble1 nuc_out; // 
private double f_max=100; // 
private double offset=-.3; // output = 50 for 0 input
private double slope=.08; // 
private  NslDouble1 nuc_mp; // 

//methods 
public void initModule()
{
	nuc_out.set(  50);
}
 
public void simTrain() 
{
	simRun();
}
 
public void simRun()
{
	// Map inputs onto 2x1 array
	NslDouble2 td = new NslDouble2(2, 10);
        
	td.set(0,   NslSumColumns.eval(NslGetSector.eval(pp_in,0,0,4,9)));
	td.set(1,   NslSumColumns.eval(NslGetSector.eval(pp_in,5,0,9,9)));
	nuc_mp.set(  __tempNUC_layer2 = (NslAdd.eval(__tempNUC_layer2, NslSumRows.eval(__tempNUC_layer0.setReference(NslElemMult.eval(__tempNUC_layer0.get(), 2.0, td))), NslSumRows.eval(__tempNUC_layer1.setReference(NslElemMult.eval(__tempNUC_layer1.get(), -.2, pc_in))))));
	nuc_out.set(  __tempNUC_layer3 = (NslElemMult.eval(__tempNUC_layer3, f_max, NslSigmoid.eval(nuc_mp,slope,offset))));
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

	/* Temporary variables */
		NslDouble2 __tempNUC_layer0 = new NslDouble2(1, 1);
		NslDouble2 __tempNUC_layer1 = new NslDouble2(1, 1);
		double[] __tempNUC_layer2 = new double[1];
		double[] __tempNUC_layer3 = new double[1];

	/* GENERIC CONSTRUCTOR: */
	public NUC_layer(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstNUC_layer(nslName, nslParent);
	}

	public void makeInstNUC_layer(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		pp_in = new NslDinDouble2("pp_in", this, 10, 10);
		pc_in = new NslDinDouble2("pc_in", this, 2, 5);
		nuc_out = new NslDoutDouble1("nuc_out", this, 2);
		nuc_mp = new NslDouble1("nuc_mp", this, 2);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end NUC_layer

