package DartModel.THROW_layer.v1_1_1.src;

nslModule THROW_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  THROW_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 nuc_in(2); // 
public NslDoutDouble0 throw_out(); // 

//methods 
public void simTrain()
{
	simRun();
}
 
public void simRun()
{
	throw_out = (.5 - (1.+nuc_in[0])/(2.+nuc_in[1] + nuc_in[0]))*100.;
}
public void makeConn(){
}
}//end THROW_layer

