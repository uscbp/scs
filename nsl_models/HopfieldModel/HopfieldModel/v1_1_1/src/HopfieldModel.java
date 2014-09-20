package HopfieldModel.HopfieldModel.v1_1_1.src;
import HopfieldModel.HopfieldNetwork.v1_1_1.src.*;

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

public class HopfieldModel extends NslModel{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: HopfieldModel
//moduleName:  HopfieldModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  HopfieldNetwork net; // 
private int size=6; // 

//methods 
public void initSys()
{
	system.setRunEndTime(300);
	system.setTrainEndTime(1);
	system.setTrainDelta(1);
	system.setRunDelta(1);
}

public void initModule() 
{
	nslSetTrainDelta(1);
	nslSetRunDelta(1);
	NslInt2 in= new  NslInt2(size, size);
	in.set(-1);
	net.input.setReference(in);
	nslAddAreaCanvas("output", "output", net.output,-1,1);
	nslAddTemporalCanvas("output", "energy", net.energy, -size*size,0, NslColor.getColor("BLACK"));
	nslAddInputImageCanvas("input","input", net.input, -1, 1);
	addPanel("control", "input");
	addButtonToPanel("clear", "Clear Image", "control", "input");
}

public void clearPushed() 
{
	net.input.set(-1);
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

	/* EMPTY CONSTRUCTOR: Called only for the top level module */
	public HopfieldModel() {
		super("hopfieldModel",(NslModel)null);
		initSys();
		makeInstHopfieldModel("hopfieldModel",null);
	}

	/* Formal parameters */

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public HopfieldModel(String nslName, NslModule nslParent)
{
		super(nslName, nslParent);
		initSys();
		makeInstHopfieldModel(nslName, nslParent);
	}

	public void makeInstHopfieldModel(String nslName, NslModule nslParent)
{ 
		Object[] nslArgs=new Object[]{};
		callFromConstructorTop(nslArgs);
		net = new HopfieldNetwork("net", this, size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end HopfieldModel
