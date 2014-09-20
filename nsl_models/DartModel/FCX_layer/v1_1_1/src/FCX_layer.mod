package DartModel.FCX_layer.v1_1_1.src;

nslModule FCX_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  FCX_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble0 p_in(); // Prism angle [0:30]
public NslDoutDouble2 fcx_out(10,4); // 
private double sx2=100; // 10*10
private double sy2=2.25; // 1.5*1.5
private NslDouble0 fcx_noise; // 

//methods 
public void initModule()
{
	fcx_out=0.;
	fcx_noise=(NslDouble0)nslGetValue("dartModel.fcx_noise");
}
   
public void simTrain() 
{
	simRun();
}  
 
public void simRun()
{
	int i,j;
	NslDouble0 mx(), my(), dx(), dy();
     
	mx = 1. + 9.*p_in/50.;
	my = 1.5;
     
	for(i=0;i<10;i++)
	{
		dx = mx-i;
		for(j=0;j<4;j++)
		{
			dy = my - j;
			fcx_out[i][j] = fcx_noise * nslRandom() + (1.-fcx_noise)*nslExp(-1.*(dx*dx/sx2 + dy*dy/sy2));
		}
	}
}
public void makeConn(){
}
}//end FCX_layer

