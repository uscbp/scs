package DomineyModel.MLBN.v1_1_1.src;

nslModule MLBN(int stdsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  MLBN
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinFloat2 stm(stdsz, stdsz); // input - spatio temporal transformations
public NslDinFloat2 llbn(stdsz, stdsz); // 
public NslDoutFloat2 mlbn(stdsz, stdsz); // 
private NslFloat0 mlbnPot_tm; // 
private NslFloat0 mlbn_kx1; // 
private NslFloat0 mlbn_kx2; // 
private NslFloat0 mlbn_ky1; // 
private NslFloat0 mlbn_ky2; // 
private NslFloat2 mlbnPot(stdsz, stdsz); // medium lead burst neurons of the brainstem saccade generator

//methods 
public void initRun() 
{
	mlbn=0;
	mlbnPot=0;

	mlbnPot_tm=0.008;
	mlbn_kx1=0;
	mlbn_kx2=1500;
	mlbn_ky1=0;
	mlbn_ky2=950;

}

public void simRun() 
{
	// leftSTM, rightSTM etc have weights that increase with distance from fovea
	// performing the SpatioTeMporal transformation.
	// ^ = pointwise multiplication
	// medium lead burst neurons - see Hepp and Henn (in refs) for details.

	mlbnPot=nslDiff(mlbnPot,mlbnPot_tm, -mlbnPot + (stm^llbn));
	mlbn = nslSaturation(mlbnPot,mlbn_kx1,mlbn_kx2,mlbn_ky1,mlbn_ky2);
	if (system.debug>=7) 
	{
		nslPrintln("debug: MLBN");
		nslPrintln(mlbn);
	}
}
public void makeConn(){
}
}//end MLBN

