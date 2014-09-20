/** 
 Here is the class representing the LimbicCortex (LC) input module.
 In old NSL it is an input array, but since NSLJ does not have
 input arrays yet we make them static arrays.
*/
package CrowleyModel.LC.v1_1_1.src;

nslModule LC(int array_size){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  LC
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutDouble2 LimbicCortex_out(array_size,array_size); // 

//methods 
public void initRun(){
    LimbicCortex_out=30;
  }
public void makeConn(){
}
}//end LC

