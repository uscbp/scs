package MaxSelectorModelMatlab.MaxSelector.v1_1_1.src;
nslImport MaxSelectorModelMatlab.MUlayer.v1_1_1.src.*;
nslImport MaxSelectorModelMatlab.MVlayer.v1_1_1.src.*;

nslModule MaxSelector(int size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: MaxSelectorModelMatlab
//moduleName:  MaxSelector
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDinDouble1 in(size); // 
public NslDoutDouble1 out(size); // 
public NslDoutDouble1 up_out(size); // 
public MUlayer ul(size); // 
public MVlayer vl(size); // 

//methods 

public void makeConn(){
    nslRelabel(in,ul.s_in);
    nslRelabel(ul.uf,out);
    nslConnect(ul.uf,vl.u_in);
    nslRelabel(ul.up,up_out);
    nslConnect(vl.vf,ul.v_in);
}
}//end MaxSelector

