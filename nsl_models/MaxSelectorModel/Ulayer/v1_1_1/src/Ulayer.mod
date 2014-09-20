package MaxSelectorModel.Ulayer.v1_1_1.src;

nslModule Ulayer(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModel
//moduleName:  Ulayer
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 s_in(size); // 
public NslDinDouble0 v_in(); // 
public NslDoutDouble1 up(size); // 
public NslDoutDouble1 uf(size); // 
private double w1; // 
private double w2; // 
private NslDouble0 hu; // 
private double k; // 
private double tau; // 

//methods 
public void initModule() 
{
	hu.nslSetAccess('W');
	uf=0;
	up=0;
	tau = 1.0;
	w1= 1.0;
	w2= 1.0;
	hu= 0.1;
	k= 0.1;
}

public void simRun() 
{
	up = nslDiff(up, tau, -up + w1*uf-w2*v_in - hu + s_in);
	uf = nslStep(up,k,0,1.0);
}
public void makeConn(){
}
}//end Ulayer

