package MaxSelectorModelMatlab.MUlayer.v1_1_1.src;

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

public class MUlayer extends NslMatlabModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModelMatlab
//moduleName:  MUlayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble1 s_in; // 
public  NslDinDouble0 v_in; // 
public  NslDoutDouble1 uf; // 
private NslDouble0 w1; // 
private NslDouble0 w2; // 
private NslDouble0 h1; // 
private NslDouble0 k; // 
private NslDouble0 tau; // 
public  NslDoutDouble1 up; // 

//methods 

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
	int size;

	/* Temporary variables */

	/* GENERIC CONSTRUCTOR: */
	public MUlayer(String nslName, NslModule nslParent, int size)
{
		super(nslName, nslParent);
		this.size=size;
		initSys();
		makeInstMUlayer(nslName, nslParent, size);
	}

	public void makeInstMUlayer(String nslName, NslModule nslParent, int size)
{ 
		Object[] nslArgs=new Object[]{size};
		callFromConstructorTop(nslArgs);
		s_in = new NslDinDouble1("s_in", this, size);
		v_in = new NslDinDouble0("v_in", this);
		uf = new NslDoutDouble1("uf", this, size);
		w1 = new NslDouble0("w1", this);
		w2 = new NslDouble0("w2", this);
		h1 = new NslDouble0("h1", this);
		k = new NslDouble0("k", this);
		tau = new NslDouble0("tau", this);
		up = new NslDoutDouble1("up", this, size);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end MUlayer
