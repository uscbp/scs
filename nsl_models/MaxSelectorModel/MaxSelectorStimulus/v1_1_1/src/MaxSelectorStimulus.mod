package MaxSelectorModel.MaxSelectorStimulus.v1_1_1.src;

nslJavaModule MaxSelectorStimulus(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModel
//moduleName:  MaxSelectorStimulus
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutDouble1 s_out(size); // 

//methods 
public void initModule() 
{
	s_out.nslSetAccess('W');
	s_out=0;
	s_out[1]=0.5;
	s_out[3]=1.0;
}
public void makeConn(){
}
}//end MaxSelectorStimulus

