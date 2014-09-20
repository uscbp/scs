package DomineyModel.Bursters.v1_1_1.src;

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

public class Bursters extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  Bursters
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinFloat0 lebn; // 
public  NslDinFloat0 rebn; // 
public  NslDinFloat0 uebn; // 
public  NslDinFloat0 debn; // 
public  NslDoutFloat2 saccademask; // 
private NslFloat0 saccadebool_k1; // 
private NslFloat0 saccadebool_k2; // 
private NslFloat0 saccadebool_k3; // 
private NslFloat0 bursters; // 

//methods 
public void initRun() 
{
	saccadebool_k1.set(240);
	saccadebool_k2.set(1);
	saccadebool_k3.set(0);
}

public void simRun() 
{
	bursters.set(  __tempBursters2.setReference(__tempBursters1.setReference(__tempBursters0.setReference(uebn.get()+debn.get()).get()+lebn.get()).get()+rebn.get()) );
	// aa: this is reverse of what is intuitive
	// if x<240, then y=1.0
	// if x>=240, then y=0.0
	saccademask.set(  NslStep.eval(bursters,saccadebool_k1,saccadebool_k2,saccadebool_k3));
	if (system.debug>=14) 
	{
		system.nslPrintln("Bursters:simRun: saccademask");
		system.nslPrintln(saccademask);
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
		NslFloat0 __tempBursters0 = new NslFloat0();
		NslFloat0 __tempBursters1 = new NslFloat0();
		NslFloat0 __tempBursters2 = new NslFloat0();

	/* GENERIC CONSTRUCTOR: */
	public Bursters(String nslName, NslModule nslParent, int stdsz)
{
		super(nslName, nslParent);
		this.stdsz=stdsz;
		initSys();
		makeInstBursters(nslName, nslParent, stdsz);
	}

	public void makeInstBursters(String nslName, NslModule nslParent, int stdsz)
{ 
		Object[] nslArgs=new Object[]{stdsz};
		callFromConstructorTop(nslArgs);
		lebn = new NslDinFloat0("lebn", this);
		rebn = new NslDinFloat0("rebn", this);
		uebn = new NslDinFloat0("uebn", this);
		debn = new NslDinFloat0("debn", this);
		saccademask = new NslDoutFloat2("saccademask", this, stdsz, stdsz);
		saccadebool_k1 = new NslFloat0("saccadebool_k1", this);
		saccadebool_k2 = new NslFloat0("saccadebool_k2", this);
		saccadebool_k3 = new NslFloat0("saccadebool_k3", this);
		bursters = new NslFloat0("bursters", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end Bursters

