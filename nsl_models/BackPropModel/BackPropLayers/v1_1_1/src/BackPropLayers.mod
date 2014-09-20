package BackPropModel.BackPropLayers.v1_1_1.src;
nslImport BackPropModel.BPForward.v1_1_1.src.*;
nslImport BackPropModel.BPBackwardError.v1_1_1.src.*;
nslImport BackPropModel.BPBackwardProp.v1_1_1.src.*;
nslImport BackPropModel.BPBackward.v1_1_1.src.*;

nslJavaModule BackPropLayers(int inSize, int hidSize, int outSize){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: BackPropModel
//moduleName:  BackPropLayers
//versionName: 1_1_1
//floatSubModules: true


//variables 
public BPForward fh(inSize, hidSize); // 
public BPForward fo(hidSize, outSize); // 
public BPBackwardError be(outSize); // 
public BPBackwardProp bo(hidSize, outSize); // 
public BPBackward bh(inSize, hidSize); // 
public NslDinFloat1 in(inSize); // 
public NslDinFloat1 desired(outSize); // 
public NslDoutFloat1 out(outSize); // 
public NslDoutFloat1 error(outSize); // 
public NslDoutDouble0 tss(); // 

//methods 
public void initModule()
{
	nslAddAreaCanvas("backPropOut", "output", out, 0.0, 1.0);
	nslAddTemporalCanvas("backPropOut", "error", error, -1.0, 1.0, NslColor.getColor("BLACK"));
	nslAddTemporalCanvas("backPropOut", "tss", tss, NslTemporalCanvas.TEMPORAL_MODE_EPOCH, 0.0, 2.0, 
		NslColor.getColor("BLACK"));
}
public void makeConn(){
    nslRelabel(in,bh.fInput);
    nslRelabel(in,fh.fInput);
    nslRelabel(desired,be.desired);
    nslConnect(bh.dh,fh.dh);
    nslConnect(bh.dw,fh.dw);
    nslConnect(fh.mf,fo.fInput);
    nslConnect(fh.mf,bo.fInput);
    nslConnect(fh.mf,bh.mf);
    nslRelabel(fo.mf,out);
    nslConnect(fo.mf,be.mf);
    nslConnect(fo.mf,bo.mf);
    nslConnect(fo.w,bo.w);
    nslConnect(bo.dh,fo.dh);
    nslConnect(bo.dw,fo.dw);
    nslConnect(bo.bOutput,bh.bInput);
    nslConnect(be.eOutput,bo.bInput);
    nslRelabel(be.eOutput,error);
    nslRelabel(be.tss,tss);
}
}//end BackPropLayers

