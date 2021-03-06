package DomineyModel.FON.v1_1_1.src;

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

public class FON extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  FON
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat2 posteriorParietal; // 
public  NslDoutFloat2 fon; // 
public  NslDoutFloat2 fovElem; // 
public  NslDoutFloat0 fovCenter; // 
public  NslDoutFloat0 fonCenter; // 
private NslFloat0 fonPot_k1; // 
private NslFloat0 fon_x1; // 
private NslFloat0 fon_y1; // 
private NslFloat0 fon_y2; // 
private  NslFloat2 fonPot; // 
private NslFloat0 fovElemCenter; // 
private int center; // 

//methods 
public void initRun() 
{
	center =(int)stdsz/2;
	fon.set(0);
	fovElem.set(0);
	fonPot.set(0);
	fovElemCenter.set(0);
	fonCenter.set(0);

	fonPot_k1.set(1);
	fon_x1.set(5);
	fon_y1.set(0);
	fon_y2.set(90);
}

public void simRun() 
{
	fonPot.set( __tempFON1.setReference(fonPot_k1.get()*__tempFON0.setReference(posteriorParietal.get(center, center)).get())); 

	fon.set(  NslStep.eval(fonPot,fon_x1,fon_y1,fon_y2));

	fovElem.set(center, center,   __tempFON2.setReference(posteriorParietal.get(center, center))); 

	fonCenter.set(__tempFON3.setReference(fon.get(center, center)));
	fovElemCenter.set(__tempFON4.setReference(fovElem.get(center, center)));

	if (system.debug>=20) 
	{
		system.nslPrintln("FON:simRun: fonCenter");
		system.nslPrintln(fonCenter);
		system.nslPrintln("FON:simRun: fovElemCenter");
		system.nslPrintln(fovElemCenter);
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
		NslFloat0 __tempFON0 = new NslFloat0();
		NslFloat0 __tempFON1 = new NslFloat0();
		NslFloat0 __tempFON2 = new NslFloat0();
		NslFloat0 __tempFON3 = new NslFloat0();
		NslFloat0 __tempFON4 = new NslFloat0();

	/* GENERIC CONSTRUCTOR: */
	public FON(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstFON(nslName, nslParent, stdsz);
	}

	public void makeInstFON(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		posteriorParietal = new NslDinFloat2("posteriorParietal", this, stdsz, stdsz);
		fon = new NslDoutFloat2("fon", this, stdsz, stdsz);
		fovElem = new NslDoutFloat2("fovElem", this, stdsz, stdsz);
		fovCenter = new NslDoutFloat0("fovCenter", this);
		fonCenter = new NslDoutFloat0("fonCenter", this);
		fonPot_k1 = new NslFloat0("fonPot_k1", this);
		fon_x1 = new NslFloat0("fon_x1", this);
		fon_y1 = new NslFloat0("fon_y1", this);
		fon_y2 = new NslFloat0("fon_y2", this);
		fonPot = new NslFloat2("fonPot", this, stdsz, stdsz);
		fovElemCenter = new NslFloat0("fovElemCenter", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end FON

