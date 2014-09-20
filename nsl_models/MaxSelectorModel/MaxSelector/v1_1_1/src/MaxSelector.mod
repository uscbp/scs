package MaxSelectorModel.MaxSelector.v1_1_1.src;
nslImport MaxSelectorModel.Ulayer.v1_1_1.src.*;
nslImport MaxSelectorModel.Vlayer.v1_1_1.src.*;

nslModule MaxSelector(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModel
//moduleName:  MaxSelector
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 in(size); // 
public NslDoutDouble1 out(size); // 
public NslDoutDouble1 up_out(size); // 
public MaxSelectorModel.Ulayer.v1_1_1.src.Ulayer ul(size); // 
public MaxSelectorModel.Vlayer.v1_1_1.src.Vlayer vl(size); // 

//methods 

public void makeConn(){
    nslRelabel(in,ul.s_in);
    nslConnect(ul.uf,vl.u_in);
    nslRelabel(ul.uf,out);
    nslRelabel(ul.up,up_out);
    nslConnect(vl.vf,ul.v_in);
}
}//end MaxSelector

