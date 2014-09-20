/** 
Here is the class representing the Brainstem Saccade Generator (BSG) module.
This is a completely non-neural module. It monitors (non-neurally) certain 
conditions and triggers the saccade by setting BSGsaccade to 1 when a certain
(see the ref.) contidion is met. The BSG module aslo extracts the velocity 
and  direction information for a saccade.  The actuall ending of a saccade
is also carried with BSG (again non-neurally)
== Recordings of SC neurons (Sparks, 1986) have shown that saccades are 
== initiated about the the time the peak in firing of the SRBNs accurs.
M. Crowley: "Since our BSG is non-neural, we calculate directly when the
activity from SC burst cell is declining. When this situation occurs, we
extract the saccade vector and peak firing rate from SCsac and initiate a 
saccade"
*/
package CrowleyModel.BSG.v1_1_1.src;

nslModule BSG(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  BSG
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 SCsac_in(array_size, array_size); // 
public NslDinDouble2 SCbu_in(array_size, array_size); // 
public NslDoutDouble0 BSGsaccade_out(); // 
public NslDoutDouble1 BSGEyeMovement_out(2); // is in R^2
public NslDoutDouble2 BSGEye(array_size,array_size); // 
public NslDoutDouble2 BSGsac(array_size,array_size); // 
private NslDouble1 BSGSaccadeVector(2); // 
private final int NINE=9; // 
private final int CENTERX=4; // 
private final int CENTERY=4; // 
private double BSGemtm; // 
private double BSGSaccadeVelocity; // 
private double BSGsactm; // 
private double BSGscsacK; // 
private double BSGscbuK; // 
private double SCsacmax; // 
private double SCsacprevmax; // 
private double BSGsacvel; // 
private double BSGsacvelsigma1; // 
private double BSGsacvelsigma2; // 
private double BSGsacvelsigma3; // 
private double BSGsacvelsigma4; // 
private double Inhibition; // 
private double Activation; // 
private double fovea; // 
private final int MaxCorticalFiring=90; // 

//methods 
public void initRun()
{
	// Runmodule BrainstemSaccadeGenerator parameters
	BSGemtm             =    0.01;
	BSGSaccadeVelocity  =    0;
	BSGEyeMovement_out  =    0;
	BSGSaccadeVector    =    0;
	BSGEye              =    0;

	BSGsac              =    0;

	BSGsactm            =    0.01;
	BSGscbuK            =    1;
	BSGscsacK           =    0.06;
	SCsacmax            =    0;
	SCsacprevmax        =    0;
	BSGsaccade_out      =    0;

	Inhibition = 0;
	Activation = 0;
	fovea = (double)SCbu_in[4][4];
	
	BSGsacvelsigma1     =    0;
	BSGsacvelsigma2     = 1000;
	BSGsacvelsigma3     =    10.0 * nslGetRunDelta();//SACCADE.get_delta();
	BSGsacvelsigma4     =    20.0 * nslGetRunDelta();//SACCADE.get_delta();
}

public void simRun()
{
	double r;

	/* @@@ */ 
	//System.err.println("@@@@ BSG simRun entered @@@@");
	fovea = (double)SCbu_in[4][4];
  
	r= fovea*BSGscsacK;
	// System.err.println("@@@@ BSG simRun AA @@@@");
	BSGsac=nslDiff(BSGsac,BSGsactm,-   BSGsac+ SCsac_in*BSGscsacK - fovea*BSGscbuK);
	// System.err.println("@@@@ BSG simRun BB @@@@");
	BSGsac.set(nslj.src.math.NslSaturation.eval( BSGsac,0,MaxCorticalFiring,0,MaxCorticalFiring)); 
	//System.err.println("@@@@ BSG simRun CC @@@@");
	SCsacmax = nslMaxValue(SCsac_in);
	//System.err.println("@@@@ BSG simRun DD @@@@");

	// System.out.println("BSG: SCsacmax "+SCsacmax +"\tSCsacprevmax "+ 
	//	     SCsacprevmax+"\tBSGsaccade_out "+BSGsaccade_out+"\n"+SCsac_in);

  
	// System.err.println("@@@@ BSG: BSGsaccade_out:"+BSGsaccade_out);
	if ((SCsacmax < SCsacprevmax) && (BSGsaccade_out.get() == 0.0) && (SCsacmax > 30.0)) 
	{
		BSGsaccade_out = 1;
		// System.err.println("@@@@ BSG simRun EE @@@@");
		BSGSaccadeVector = GetSaccadeVector(BSGsac);
		// System.err.println("@@@@ BSG simRun FF @@@@");
		BSGsacvel =0+ Nsl2Sigmoid.eval(SCsacmax,BSGsacvelsigma1,BSGsacvelsigma2, BSGsacvelsigma3, 
						BSGsacvelsigma4);
		// System.err.println("@@@@ BSG simRun GG @@@@");
		SCsacmax     = 0;
		SCsacprevmax = 0;  /* seems redundant... */
	}
	SCsacprevmax = SCsacmax;
  
	/**
	 * == M. Crowley explains the factor 0.33 as: 
	 * == "SCbu_in tends to max at 30 or above based on current model performance.  
	 * ==  That is why we uses .033 for XXX???"
	 */
	Activation = fovea * .033;
	if ( Activation > 1 ) 
		Activation = 1;
	Inhibition = ( 1 - Activation ) * ( 1 - Activation );
  
	BSGEyeMovement_out=nslDiff(BSGEyeMovement_out,BSGemtm,-BSGEyeMovement_out + (BSGSaccadeVector * BSGsacvel * Inhibition));
  
	if ( Inhibition <= 0.01 )
	{
		//      Saccade is over
		BSGsaccade_out      = 0;
		BSGSaccadeVector    = 0;
		Inhibition          = 0;
		BSGEyeMovement_out  = 0;
		BSGsacvel           = 0;
	}
	/*  
	//  Apply any movement of the eyes to the targets
	EyeTargets = MakeTargets( RETINA );
  
	if ( EyeTargets != null )
	{
		//      Get target locations for target remapping of RETINA for
		//      display purposes only
      
		EyeTargets.Move(BSGEyeMovement_out);
      
		//      Update the image on the retina
		//        BSGEye = RETINA.get_sector(0,8,0,8);
		//        BSGEye = MoveEye( EyeTargets, BSGEye );
		//        RETINA = BSGEye.get_sector(0,8,0,8);
	}
	*/
	// System.err.println("BSG: simRun finished!!!!!");
} /* End of simRun() */

public NslDouble1 GetSaccadeVector(NslDouble2 inmat2d) 
{
	/*
	This function determines the amplitude and direction (vector)
	of a saccade by determining the centroid of the activity in the input matrix.
	This is accomplished by:
	    (1)Calculating the SUM of all activity in inmat.
	    (2)Dividing all matrix elements by the sum in step 1 and multiplying by
	       the array element indices.  This creates the saccade vector as the
	       normalized sum of all activity in the input matrix.
	It returns the x,y components of the calculated saccade vector.
	*/
 
	double[][] inmat = inmat2d.get();
	int size1 = inmat.length;
	int size2 = inmat[0].length;
	int half1 = size1/2;
	int half2 = size2/2;
	// 99/8/4 aa: was : double SCsum=nslSum(inmat2d);

	double SCsum;
	SCsum = nslSum(inmat2d);
	//99/8/3 aa:    NslDouble1 temp1d = new NslDouble1(2);
	NslDouble1 temp1d(2);
	double[] temp = temp1d.get();
    
	if(SCsum==0) 
	{
		temp[0]=temp[1]=0;
	}
	else 
	{
		for(int i=0; i<size1; i++) 
			for(int j=0; j<size2; j++) 
				if(inmat[i][j]>0)
				{
					temp[0]=inmat[i][j]*(i-half1)/SCsum;
					temp[1]=inmat[i][j]*(j-half2)/SCsum;
				}
	}
	return temp1d;
}
public void makeConn(){
}
}//end BSG

