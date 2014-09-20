package HopfieldModel.HopfieldModel.v1_1_1.src;
nslImport HopfieldModel.HopfieldNetwork.v1_1_1.src.*;

nslModel HopfieldModel(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: HopfieldModel
//moduleName:  HopfieldModel
//versionName: 1_1_1
//floatSubModules: true


//variables 
public HopfieldNetwork net(size); // 
private int size=6; // 

//methods 
public void initSys()
{
	system.setRunEndTime(300);
	system.setTrainEndTime(1);
	system.setTrainDelta(1);
	system.setRunDelta(1);
}

public void initModule() 
{
	nslSetTrainDelta(1);
	nslSetRunDelta(1);
	NslInt2 in=new NslInt2(size, size);
	in=-1;
	net.input.setReference(in);
	nslAddAreaCanvas("output", "output", net.output,-1,1);
	nslAddTemporalCanvas("output", "energy", net.energy, -size*size,0, NslColor.getColor("BLACK"));
	nslAddInputImageCanvas("input","input", net.input, -1, 1);
	addPanel("control", "input");
	addButtonToPanel("clear", "Clear Image", "control", "input");
}

public void clearPushed() 
{
	net.input.set(-1);
}
public void makeConn(){
}
}//end HopfieldModel

