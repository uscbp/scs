package BackPropModel.BPForward.v1_1_1.src;

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

public class BPForward extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BPForward
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat1 fInput; // 
public  NslDinFloat1 dh; // 
public  NslDinFloat2 dw; // 
public  NslDoutFloat1 mf; // 
public  NslDoutFloat2 w; // 
private  NslFloat1 mp; // 
private  NslFloat1 h; // 

//methods 
public void initModule() 
{
	for(int i=0; i<hidSize; i++)
	{
		h.set(i, NslRandom.eval(-0.5f,0.5f));
		for(int j=0; j<inSize; j++)
		{
			w.set(j, i, NslRandom.eval(-0.5f,0.5f));
		}
	}
	dw.set(  0.0);
	dh.set(  0.0);
}

public void forward()
{
	mp.set(  __tempBPForward0.setReference(NslProduct.eval(__tempBPForward0.get(), fInput, w)));
	mf.set(  NslSigmoid.eval(__tempBPForward1.setReference(NslAdd.eval(__tempBPForward1.get(), mp, h)) ));
}

public void simTrain() 
{
	w.set(  __tempBPForward2.setReference(NslAdd.eval(__tempBPForward2.get(), w, dw)) );
	h.set(  __tempBPForward3.setReference(NslAdd.eval(__tempBPForward3.get(), h, dh)) );
	forward();
}

public void simRun()
{
	forward();
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
	int inSize;
	int hidSize;

	/* Temporary variables */
		NslFloat1 __tempBPForward0 = new NslFloat1(1);
		NslFloat1 __tempBPForward1 = new NslFloat1(1);
		NslFloat2 __tempBPForward2 = new NslFloat2(1, 1);
		NslFloat1 __tempBPForward3 = new NslFloat1(1);

	/* GENERIC CONSTRUCTOR: */
	public BPForward(String nslName, NslModule nslParent, int inSize, int hidSize)
{
		super(nslName, nslParent);
		this.inSize=inSize;
		this.hidSize=hidSize;
		initSys();
		makeInstBPForward(nslName, nslParent, inSize, hidSize);
	}

	public void makeInstBPForward(String nslName, NslModule nslParent, int inSize, int hidSize)
{ 
		Object[] nslArgs=new Object[]{inSize, hidSize};
		callFromConstructorTop(nslArgs);
		fInput = new NslDinFloat1("fInput", this, inSize);
		dh = new NslDinFloat1("dh", this, hidSize);
		dw = new NslDinFloat2("dw", this, inSize, hidSize);
		mf = new NslDoutFloat1("mf", this, hidSize);
		w = new NslDoutFloat2("w", this, inSize, hidSize);
		mp = new NslFloat1("mp", this, hidSize);
		h = new NslFloat1("h", this, hidSize);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end BPForward

