package CrowleyModel.IJpair.v1_1_1.src;

nslClass IJpair(){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: CrowleyModel
//moduleName:  IJpair
//versionName: 1_1_1
//floatSubModules: true


//variables 
private int i; // 
private int j; // 

//methods 
public void callFromConstructorBottom(){
    i = 0;
    j = 0;
  }

  public void init() {
    i = 0;
    j = 0;
  }


  public int MaxIJ( NslDouble2 inmat ) {
    // Returns the i,j location of the maximum value element in the 
    // matrix passed as input

    // 99/8/2 aa: this would be better as nslMaxElem
    // pass out an array of values

    int    ic, jc;
    int    imax, jmax;
    int    foundmax;
    double max;

    foundmax = 0;
    max = -0.5;

    imax = inmat.getSize1();
    jmax = inmat.getSize2();

    for ( ic=0; ic<imax; ic++ ){
      for ( jc=0; jc<jmax; jc++ ){
	if ( inmat[ic][jc] > max ) {
	  max = (double)inmat[ic][jc];
	  foundmax = 1;
	  i = ic; 
	  j = jc;
	}
      }
    }
    if ( foundmax != 0)
      return 0;     //No positive maximum value

    return 1;
  }
  
  public int getI(){
    return i;
  }
  public int getJ(){
    return j;
  }
public void makeConn(){}
}//end IJpair

