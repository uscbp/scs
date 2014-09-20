package DartModel.SENS_layer.v1_1_1.src;

nslJavaModule SENS_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  SENS_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble0 t_in(); // Throw direction [-50:50]
public NslDoutDouble0 p_out(); // Prism angle [0:30]
public NslDoutDouble1 sens_out(2); // Error sensors

//methods 
public void initModule()
{
	nslAddAreaCanvas("output", "sens", sens_out, 0, 1);
}

public void simTrain() 
{
	simRun();
}
 
public void simRun()
{
	double Derror;

	p_out = (NslDouble0)nslGetValue("dartModel.p_out");
	Derror = (double)(p_out - t_in);
	/* go leftward or rightward */
	sens_out[0] = nslStep(Derror,0,.1-Derror/10.,0.1);
	sens_out[1] = nslStep(Derror,0,0.1,.1+Derror/10.);
}
public void makeConn(){
}
}//end SENS_layer

