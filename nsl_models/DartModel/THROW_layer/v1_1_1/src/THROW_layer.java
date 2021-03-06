package DartModel.THROW_layer.v1_1_1.src;

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

public class THROW_layer extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  THROW_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble1 nuc_in; // 
public  NslDoutDouble0 throw_out; // 

//methods 
public void simTrain()
{
	simRun();
}
 
public void simRun()
{
	throw_out.set(  __tempTHROW_layer8.setReference((__tempTHROW_layer7.setReference(.5-__tempTHROW_layer6.setReference((__tempTHROW_layer1.setReference(1.+__tempTHROW_layer0.setReference(nuc_in.get(0)).get())).get()/(__tempTHROW_layer5.setReference(__tempTHROW_layer4.setReference(2.+__tempTHROW_layer2.setReference(nuc_in.get(1)).get()).get()+__tempTHROW_layer3.setReference(nuc_in.get(0)).get())).get()).get())).get()*100.));
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
		NslDouble0 __tempTHROW_layer0 = new NslDouble0();
		NslDouble0 __tempTHROW_layer1 = new NslDouble0();
		NslDouble0 __tempTHROW_layer2 = new NslDouble0();
		NslDouble0 __tempTHROW_layer3 = new NslDouble0();
		NslDouble0 __tempTHROW_layer4 = new NslDouble0();
		NslDouble0 __tempTHROW_layer5 = new NslDouble0();
		NslDouble0 __tempTHROW_layer6 = new NslDouble0();
		NslDouble0 __tempTHROW_layer7 = new NslDouble0();
		NslDouble0 __tempTHROW_layer8 = new NslDouble0();

	/* GENERIC CONSTRUCTOR: */
	public THROW_layer(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstTHROW_layer(nslName, nslParent);
	}

	public void makeInstTHROW_layer(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		nuc_in = new NslDinDouble1("nuc_in", this, 2);
		throw_out = new NslDoutDouble0("throw_out", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end THROW_layer

