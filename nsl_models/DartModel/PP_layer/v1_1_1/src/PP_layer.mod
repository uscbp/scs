package DartModel.PP_layer.v1_1_1.src;

nslModule PP_layer(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  PP_layer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutDouble0 s_out(); // Strategy: [0:1] over/under
public NslDoutDouble2 pp_out(10,10); // PP population coding
private double sx2=12.25; // 3.5*3.5
private double sy2=9; // 3*3
private NslDouble0 a_out; // Aim direction [-30:30]
private NslDouble0 pp_noise; // 
private NslDouble0 pp_sep; // 

//methods 
public void initModule()
{
	pp_noise = (NslDouble0)nslGetValue("dartModel.pp_noise");
	pp_sep   = (NslDouble0)nslGetValue("dartModel.pp_sep");
}
 
public void simTrain() 
{
	simRun();
}
 
public void simRun()
{
	int i,j;
	NslDouble0 mx(), my();
	NslDouble0 dx(), dy();

	s_out = (NslDouble0)nslGetValue("dartModel.s_out");
	a_out = (NslDouble0)nslGetValue("dartModel.d_out");
     
	my = 4.5 + nslStep(s_out,.5,-1,1)*pp_sep/2.; // throw = over/under
	mx = 4.5 + 4.5*a_out/30.;
     
	for(i=0;i<10;i++)
	{
		dx = mx - i;
		for(j=0;j<10;j++)
		{
			dy = my - j;
			pp_out[i][j] = pp_noise * nslRandom() + (1.-pp_noise) * nslExp(-1.*(dx*dx/sx2 + dy*dy/sy2));
		}
	}
 }
public void makeConn(){
}
}//end PP_layer

