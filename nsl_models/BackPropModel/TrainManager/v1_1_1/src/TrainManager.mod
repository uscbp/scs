package BackPropModel.TrainManager.v1_1_1.src;

nslJavaModule TrainManager(int nPats, int inSize, int outSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  TrainManager
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutFloat1 dInput(inSize); // 
public NslDoutFloat1 dOutput(outSize); // 
private NslFloat2 pInput(nPats, inSize); // 
private NslFloat2 pOutput(nPats, outSize); // 
private int counter=0; // 

//methods 
public void initModule()
{
	pInput.nslSetAccess('W');
	pOutput.nslSetAccess('W');
	nslAddAreaCanvas("backPropOut", "input", dInput, 0.0, 1.0);
	nslAddAreaCanvas("backPropOut", "desired", dOutput, 0.0, 1.0);
}

public void simTrain() 
{
	counter++;
	int pat = counter%nPats;
	dInput = pInput[pat];
	dOutput = pOutput[pat];
}
public void makeConn(){
}
}//end TrainManager

