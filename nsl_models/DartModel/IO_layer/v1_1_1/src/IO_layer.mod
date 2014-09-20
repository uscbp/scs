package DartModel.IO_layer.v1_1_1.src;

nslModule IO_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  IO_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 sens_in(2); // SENS input
public NslDinDouble1 nuc_in(2); // NUC input
public NslDoutDouble1 io_out(2); // 
private double f_max=10; // 
private double offset=1.3865; // gives output of 2 for 0 input
private double slope=1; // 
private NslDouble1 io_mp(2); // 

//methods 
public void initModule() 
{
	io_out = 2.;
}

public void simTrain() 
{
	simRun();
}

public void simRun() 
{
	double nuc_act;

	nuc_act = nslSumRows(nuc_in);
	io_mp   = sens_in - .01*nuc_act;
	io_out  = f_max * nslSigmoid(io_mp,slope,offset);
}
public void makeConn(){
}
}//end IO_layer

