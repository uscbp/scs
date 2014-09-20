package MaxSelectorModel.MaxSelectorModel.v1_1_1.src;
nslImport MaxSelectorModel.MaxSelectorStimulus.v1_1_1.src.*;
nslImport MaxSelectorModel.MaxSelector.v1_1_1.src.*;

nslModel MaxSelectorModel(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModel
//moduleName:  MaxSelectorModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public MaxSelectorStimulus stimulus(size); // 
public MaxSelector maxselector(size); // 
private int size=10; // 

//methods 
public void initSys() 
{
	system.setEndTime(10.0);
	system.setRunDelta(0.1);
}

public void initModule()
{
	nslSetRunDelta(0.1);
	nslAddAreaCanvas("output", "s_out", stimulus.s_out,0.0,1.0);
	nslAddTemporalCanvas("output", "up_out", maxselector.up_out,-2,2,NslColor.getColor("BLACK"));
	nslAddAreaCanvas("output", "out", maxselector.out,0.0,1.0);
}
public void makeConn(){
    nslConnect(stimulus.s_out,maxselector.in);
}
}//end MaxSelectorModel

