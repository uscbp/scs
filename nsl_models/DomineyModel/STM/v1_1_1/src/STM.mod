package DomineyModel.STM.v1_1_1.src;

nslModule STM(int stdsz, int direction){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  STM
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutFloat2 stm(stdsz, stdsz); // 
public NslDoutFloat2 weights(stdsz, stdsz); // 
private NslFloat0 stm_k1; // 
private NslFloat1 weighttopo(stdsz); // 
private NslFloat1 stmtopo(stdsz); // 

//methods 
public void initRun() 
{
	stm_k1=0.32;
	weighttopo=0;
	stmtopo=0;
	weights=0;
	stm=0;

 	if ((direction==1)||(direction==3))
	{
		weighttopo[0] = 5.7;
		weighttopo[1] = 4.275;
		weighttopo[2] = 2.85;
		weighttopo[3] = 1.425;
		stmtopo[0]=stm_k1*weighttopo[0];
		stmtopo[1]=stm_k1*weighttopo[1];
		stmtopo[2]=stm_k1*weighttopo[2];
		stmtopo[3]=stm_k1*weighttopo[3];
	}
	if ((direction==2)||(direction==4))
	{
		weighttopo[5] = 1.425;
		weighttopo[6] = 2.85;
		weighttopo[7] = 4.275;
		weighttopo[8] = 5.7;
		stmtopo[5]=stm_k1*weighttopo[5];
		stmtopo[6]=stm_k1*weighttopo[6];
		stmtopo[7]=stm_k1*weighttopo[7];
		stmtopo[8]=stm_k1*weighttopo[8];
	}
	if ((direction==1)||(direction==2))
	{
		weights = nslFillRows(weights,weighttopo);//fill all rows the same
	        stm = nslFillRows(stm,stmtopo);
	}
	if ((direction==3)||(direction==4))
	{
		weights = nslFillColumns(weights,weighttopo);//fill all columns the same
		stm = nslFillColumns(stm,stmtopo);
	}
 	if (system.debug>=26) 
	{
		nslPrintln("STM: initRun: direction  :" + direction);
		nslPrintln("STM: initRun: stmtopo");
		nslPrintln(stmtopo);
		nslPrintln("STM: initRun: stm");
		nslPrintln(stm);
		nslPrintln("STM: initRun: weights:");
		nslPrintln(weights);
	}
}
public void makeConn(){
}
}//end STM

