package DartModel.CEREB_module.v1_1_1.src;
nslImport DartModel.GC_layer.v1_1_1.src.*;
nslImport DartModel.IO_layer.v1_1_1.src.*;
nslImport DartModel.PC_layer.v1_1_1.src.*;
nslImport DartModel.NUC_layer.v1_1_1.src.*;

nslJavaModule CEREB_module(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DartModel
//moduleName:  CEREB_module
//versionName: 1_1_1
//floatSubModules: true


//variables 
public GC_layer gc_l(); // 
public IO_layer io_l(); // 
public PC_layer pc_l(); // 
public NUC_layer nuc_l(); // 
public NslDinDouble1 sens_in(2); // SENS input (2x1)
public NslDinDouble2 pp_in(10,10); // Parietal input (10x10)
public NslDinDouble2 fcx_in(10,4); // Frontal cortex input (10x4)
public NslDoutDouble1 cereb_out(2); // 
public NslDoutDouble2 pc_out(2,5); // 
public NslDoutDouble1 io_out(2); // 

//methods 
public void initModule()
{
	nslAddAreaCanvas("output", "cereb_out", cereb_out, 0, 50);
	nslAddAreaCanvas("output", "pc_out", pc_out, 0, 100);
	nslAddAreaCanvas("output", "io_out", io_out, 0, 10);
}
public void makeConn(){
    nslRelabel(sens_in,io_l.sens_in);
    nslRelabel(pp_in,gc_l.pp_in);
    nslRelabel(pp_in,nuc_l.pp_in);
    nslRelabel(fcx_in,gc_l.fcx_in);
    nslRelabel(io_l.io_out,io_out);
    nslConnect(io_l.io_out,pc_l.io_in);
    nslRelabel(nuc_l.nuc_out,cereb_out);
    nslConnect(nuc_l.nuc_out,io_l.nuc_in);
    nslRelabel(pc_l.pc_out,pc_out);
    nslConnect(pc_l.pc_out,nuc_l.pc_in);
    nslConnect(gc_l.gc_out,pc_l.gc_in);
}
}//end CEREB_module

