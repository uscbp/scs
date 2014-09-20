package DartModel.NUC_layer.v1_1_1.src;

nslModule NUC_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  NUC_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble2 pp_in(10,10); // PP input
public NslDinDouble2 pc_in(2,5); // PC input
public NslDoutDouble1 nuc_out(2); // 
private double f_max=100; // 
private double offset=-.3; // output = 50 for 0 input
private double slope=.08; // 
private NslDouble1 nuc_mp(2); // 

//methods 
public void initModule()
{
	nuc_out = 50;
}
 
public void simTrain() 
{
	simRun();
}
 
public void simRun()
{
	// Map inputs onto 2x1 array
	NslDouble2 td(2,10);
	td[0] = nslSumColumns(nslGetSector(pp_in,0,0,4,9));
	td[1] = nslSumColumns(nslGetSector(pp_in,5,0,9,9));
	nuc_mp = nslSumRows(2.0*td) + nslSumRows(-.2*pc_in);
	nuc_out = f_max * nslSigmoid(nuc_mp,slope,offset);
}
public void makeConn(){
}
}//end NUC_layer

