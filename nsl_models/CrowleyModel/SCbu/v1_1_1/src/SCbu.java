/** 
Here is the class representing the superior colliculus build up neurons.
This layer is called as SCbu.
This way the 3 modules SCsac, SCqv, SCbu are homogenously composed of
.nslDifferential equations defining their function see SCtemp
*/
package CrowleyModel.SCbu.v1_1_1.src;
import CrowleyModel._Target.v1_1_1.src.*;

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

public class SCbu extends NslModule{

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SCbu
//versionName: 1_1_1
//floatSubModules: true


//variables 
public  NslDinDouble2 PFCfovea_in; // 
public  NslDinDouble2 SCsac_in; // 
public  NslDinDouble1 BSGEyeMovement_in; // 
public  NslDinDouble0 BSGsaccade_in; // 
public  NslDoutDouble2 SCbu_out; // 
private double SCbusigma1; // 
private double SCbusigma2; // 
private double SCbusigma3; // 
private double SCbusigma4; // 
private double scbutm; // 
private final int NINE=9; // 
private final int CENTERX=4; // 
private final int CENTERY=4; // 
private double SCBUsaccade; // 
private double SCBUMaxFire; // 
private  NslDouble2 scbu; // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
private  NslDouble2 SCBUtemp; // 
public  _Target SCBUTarget; // 

//methods 
public void initModule()
{
	FOVEAX.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAX"));
	FOVEAY.set(  (NslInt0)nslGetValue("crowleyModel.FOVEAY"));  
}

public void initRun()
{
	SCbu_out.set(0);
	SCbusigma1 = 0;
	SCbusigma2 =90;
	SCbusigma3 = 0;
	//LNK_SC4_1
	SCbusigma4 =90;
	scbu.set(  0);   
	scbutm =   0.006;
	//fovea = SCbu_out[CENTERX][CENTERY];
	SCBUsaccade = 0;
	SCBUMaxFire = 90;
}

public void simRun()
{
	// 96/12/20 aa - put SCBUtemp calculations here since in the original
	// C++ code, it appears that SCbu_out may be sequentially dependent on
	// SCBUtemp

	// System.err.println("@@@@ SCbu simRun entered @@@@");

        if ( BSGsaccade_in.get()!=0.0  )  //Performing, or starting, a saccade
	{
		if ( SCBUsaccade==0  )
		{
			SCBUsaccade = 1;     // Indicates occurrence of a saccade.
			// Get target locations for target remapping of SC buildup
			// neurons
                	SCBUTarget = _Target.MakeTargets( SCsac_in );
		}
		// Assume some kind of signal from BSG that tells the buildup
		// cells how far the eyes have moved, i.e., efference copy.
		if ( SCBUTarget!=null  )
		{
			SCBUTarget.Move( BSGEyeMovement_in );
			// Update the activation of the buildup neurons
			// SCBUtemp = MoveEye( SCBUTarget, SCbu_out ) * SCBUMaxFire;
			// 96/12/20 aa put back as C++
			SCBUtemp.set(  _Target.MoveEye( SCBUTarget, SCbu_out )); 
			SCBUtemp.set(  __tempSCbu0.setReference(NslElemMult.eval(__tempSCbu0.get(), SCBUtemp, SCBUMaxFire)) );
		}
	}
        else 
	{
		// No saccade or saccade just finished.
		SCBUsaccade     = 0;
		// When there is no corollary feedback from the BSG, the buildup
		// neurons should gradually decay to a zero state as long as
		// there is no fixation activity from PFC.
		// SCBUtemp = 0;
		SCBUtemp.set(  0);
	}

	scbu.set(system.nsldiff.eval(scbu,scbutm, __tempSCbu3.setReference(NslAdd.eval(__tempSCbu3.get(), __tempSCbu2.setReference(NslAdd.eval(__tempSCbu2.get(), __tempSCbu1.setReference(NslSub.eval(__tempSCbu1.get(), 0, scbu)), SCBUtemp)), PFCfovea_in)) ));
	SCbu_out.set(Nsl2Sigmoid.eval(scbu,SCbusigma1, SCbusigma2, SCbusigma3, SCbusigma4 ));
	//fovea = SCbu_out[CENTERX][CENTERY];

	//System.out.println("scbu: " + scbu);
	//System.out.println("PFCfovea_in: " + PFCfovea_in);

	//System.out.println("SCBUtemp: " + SCBUtemp);
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
		NslDouble2 __tempSCbu0 = new NslDouble2(1, 1);
		NslDouble2 __tempSCbu1 = new NslDouble2(1, 1);
		NslDouble2 __tempSCbu2 = new NslDouble2(1, 1);
		NslDouble2 __tempSCbu3 = new NslDouble2(1, 1);

	/* GENERIC CONSTRUCTOR: */
	public SCbu(String nslName, NslModule nslParent, int array_size)
{
		super(nslName, nslParent);
		this.array_size=array_size;
		initSys();
		makeInstSCbu(nslName, nslParent, array_size);
	}

	public void makeInstSCbu(String nslName, NslModule nslParent, int array_size)
{ 
		Object[] nslArgs=new Object[]{array_size};
		callFromConstructorTop(nslArgs);
		PFCfovea_in = new NslDinDouble2("PFCfovea_in", this, array_size, array_size);
		SCsac_in = new NslDinDouble2("SCsac_in", this, array_size, array_size);
		BSGEyeMovement_in = new NslDinDouble1("BSGEyeMovement_in", this, array_size);
		BSGsaccade_in = new NslDinDouble0("BSGsaccade_in", this);
		SCbu_out = new NslDoutDouble2("SCbu_out", this, array_size, array_size);
		scbu = new NslDouble2("scbu", this, array_size, array_size);
		FOVEAX = new NslInt0("FOVEAX", this);
		FOVEAY = new NslInt0("FOVEAY", this);
		SCBUtemp = new NslDouble2("SCBUtemp", this, array_size, array_size);
		SCBUTarget = new _Target("SCBUTarget", this);
		callFromConstructorBottom();
	}

	/******************************************************/
	/*                                                    */
	/* End of automatic declaration statements.           */
	/*                                                    */
	/******************************************************/


}//end SCbu

