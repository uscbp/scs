package MaxSelectorModelMatlab.MaxSelectorStimulus.v1_1_1.src;

nslModule MaxSelectorStimulus(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModelMatlab
//moduleName:  MaxSelectorStimulus
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutDouble1 s_out(size); // 

//methods 
public void initModule() 
{
	s_out=0;
	s_out[1]=0.5;
	s_out[3]=1.0;
	//s_out[5]=.6;
	//s_out[7]=.3;
                          /*
                          double[][] x=nslMatlabMatrix("5*ones(3,3)");
	for(int i=0; i<3; i++)
	{
		for(int j=0; j<3; j++)
		{
			nslPrintln(x[i][j]);
		}
	}
                          */
}
public void makeConn(){
}
}//end MaxSelectorStimulus

