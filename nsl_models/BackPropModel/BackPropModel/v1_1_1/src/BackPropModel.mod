package BackPropModel.BackPropModel.v1_1_1.src;
nslImport BackPropModel.TrainManager.v1_1_1.src.*;
nslImport BackPropModel.BackPropLayers.v1_1_1.src.*;

nslModel BackPropModel(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BackPropModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public TrainManager train(nPats, inSize, outSize); // 
public BackPropLayers layers(inSize, hidSize, outSize); // 
private int inSize=2; // 
private int hidSize=2; // 
private int outSize=1; // 
private int nPats=4; // 

//methods 
public void initSys() 
{
	system.setRunEndTime(nPats);
	system.setNumTrainEpochs(5000);
	system.setTrainEndTime(nPats);
}

public void initModule() 
{
	system.nslSetTrainDelta(1);     
	system.nslSetRunDelta(1);
}
public void makeConn(){
    nslConnect(train.dInput,layers.in);
    nslConnect(train.dOutput,layers.desired);
}
}//end BackPropModel

