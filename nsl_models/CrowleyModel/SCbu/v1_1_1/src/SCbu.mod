/** 
Here is the class representing the superior colliculus build up neurons.
This layer is called as SCbu.
This way the 3 modules SCsac, SCqv, SCbu are homogenously composed of
.nslDifferential equations defining their function see SCtemp
*/
package CrowleyModel.SCbu.v1_1_1.src;
nslImport CrowleyModel._Target.v1_1_1.src.*;

nslModule SCbu(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  SCbu
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 PFCfovea_in(array_size,array_size); // 
public NslDinDouble2 SCsac_in(array_size,array_size); // 
public NslDinDouble1 BSGEyeMovement_in(array_size); // 
public NslDinDouble0 BSGsaccade_in(); // 
public NslDoutDouble2 SCbu_out(array_size,array_size); // 
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
private NslDouble2 scbu(array_size,array_size); // 
private NslInt0 FOVEAX; // 
private NslInt0 FOVEAY; // 
private NslDouble2 SCBUtemp(array_size,array_size); // 
public _Target SCBUTarget(); // 

//methods 
public void initModule()
{
	FOVEAX = (NslInt0)nslGetValue("crowleyModel.FOVEAX");
	FOVEAY = (NslInt0)nslGetValue("crowleyModel.FOVEAY");  
}

public void initRun()
{
	SCbu_out=0;
	SCbusigma1 = 0;
	SCbusigma2 =90;
	SCbusigma3 = 0;
	//LNK_SC4_1
	SCbusigma4 =90;
	scbu = 0;   
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

        if ( BSGsaccade_in != 0.0 )  //Performing, or starting, a saccade
	{
		if ( SCBUsaccade == 0 )
		{
			SCBUsaccade = 1;     // Indicates occurrence of a saccade.
			// Get target locations for target remapping of SC buildup
			// neurons
                	SCBUTarget = _Target.MakeTargets( SCsac_in );
		}
		// Assume some kind of signal from BSG that tells the buildup
		// cells how far the eyes have moved, i.e., efference copy.
		if ( SCBUTarget != null )
		{
			SCBUTarget.Move( BSGEyeMovement_in );
			// Update the activation of the buildup neurons
			// SCBUtemp = MoveEye( SCBUTarget, SCbu_out ) * SCBUMaxFire;
			// 96/12/20 aa put back as C++
			SCBUtemp = _Target.MoveEye( SCBUTarget, SCbu_out ); 
			SCBUtemp = SCBUtemp * SCBUMaxFire;
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
		SCBUtemp = 0;
	}

	scbu=nslDiff(scbu,scbutm, -scbu + SCBUtemp + PFCfovea_in);
	SCbu_out=Nsl2Sigmoid.eval(scbu,SCbusigma1, SCbusigma2, SCbusigma3, SCbusigma4 );
	//fovea = SCbu_out[CENTERX][CENTERY];

	//System.out.println("scbu: " + scbu);
	//System.out.println("PFCfovea_in: " + PFCfovea_in);

	//System.out.println("SCBUtemp: " + SCBUtemp);
}
public void makeConn(){
}
}//end SCbu

