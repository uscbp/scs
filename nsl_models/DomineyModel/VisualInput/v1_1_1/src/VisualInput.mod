/** 
Saccade_visualinput
Saccade generator ala Scudder;  These arrays represent the increased
 density of supcol projection to llbn as a function of increase eccentricity
 in supcol. Function: spatial to temporal transformation.
 Note, the fixation point for a 27x27 grid is 13,13
 Also, the stimulation is applied in the stimulation module


 98/11/4: aa 
 Dominey originaly used non-buffered ports.

 Note: the second target has to be within 4 squares of the first target
 for the monkey to see the second target.

 Also, remember the coordinates below are in terms of array coordinates
 thus the first number is the vertical and the second the horizontal
 starting at the upper left. (sort of -y,x).

 In the NSL2.1.7 model, if the visualinput is specifed from 0.01 to 0.05, 
 it means that the stimulus does not go off until 0.055. 

 Inaddition, the NSL2.1.7 model, started time at t=one delta for
 simRuns.  This means that in terms of cycles something happening
 during reported time=0.4 is really happening at NSLJ current time = 3.5. 
 This also goes for stimulus; thus on at 0.02 becomes on at 0.015 in NSLJ,
 or the 4th cycle.
 Also stimulus is off at 0.07 it is really off at 0.075 in NSL2.1.7
 thus really off at (0.075 + delta)=0.08 in NSLJ.

 Note: FEF,LLBN,PPQV,SC,StimFEF,StimSC,VisualInput have parameters 
 that change depending on the protocolNum.

 1000 msec =1.0 = 1 second
  100 msec =0.1
   10 msec =0.01
    1 msec =0.001

 Note: the end time and step/delta size is defined in the Model module.

 protocolNum::1. single saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec
 Fixation  0-0.02 at [(i=center,j=center)]
 Target A 0.02-0.07 at [(i=center+3,j=(center-3)] 

 protocolNum::2. memorySingleI saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.2 at [(i=center),(center)]
 Target A & fixation 0.02 - 0.07 at [(i=center-2),(j=center+2)]
 fixation off at 0.58 
 aa: the simple protocol 
 causes the eyes to start moving at .15, and end at .195
 and since the fixation is off at .02, 
 it only takes .175 sec for the signal to propagate
 throughout the complete circuit
 Thus .58 changing to:
 fixation off at 0.28

 protocolNum::3. memorySingleII saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.2 at [(i=center),(center)]
 Target A 0.02 - 0.07 at [(i=center-2),(j=center-3)]
 fixation off at 0.58 
 aa: the simple protocol 
 causes the eyes to start moving at .15, and end at .195
 and since the fixation is off at .02, 
 it only takes .175 sec for the signal to propagate
 throughout the complete circuit
 Thus .58 changing to:
 fixation off at 0.28

 protocolNum::4. doubleI saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec
 Fixation 0-0.02 at [(i=center),(j=center)]
 Target A 0.02 - 0.07 at [(i=center-3),j=(center)]
 Target B 0.08 - 0.12 at [(i=center-3),j=(center+3)]
 aa: ppqv does a winnerTakeAll and the second target is not saying long
 enough in ppqv, thus delaying 4 cycles
 Target B 0.09 - 0.13 at [(i=center-3),j=(center+3)]

 protocolNum::5. doubleII saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec
 Fixation 0-0.02 at [(i=center),(j=center)]
 Target A 0.02 - 0.07 at [(i=center),j=(center-2)]
 Target B 0.08 - 0.12 at [(i=center-3),j=(center)]
 aa: ppqv does a winnerTakeAll and the second target is not saying long
 enough in ppqv, thus delaying 1 cycle
 Target B 0.085 - 0.125 at [(i=center-3),j=(center+3)]

 note the published doc does not give coordinates for 6 and 7
 protocolNum::6. lesionSC saccade. // similar to simple
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec
 Fixation  0-0.02 at [(i=center,j=center)]
 Target A 0.02-0.4 at [(i=center+3,j=(center-3)] 

 protocolNum::7. lesionFEF saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec
 Fixation  0-0.02 at [(i=center,j=center)]
 Target A 0.02-0.4 at [(i=center+3,j=(center-3)] 

 protocolNum::8. memoryDouble saccade
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center),(j=center)]
 Target A 0.02 - 0.05 at [(i=center-3),(j=center)]
 Target B 0.085 - 0.10 at [(i=center-3),(j=center+3)]
 New: Target B 0.085 - 0.11 at [(i=center-3),(j=center)]
 added 2 cycles to the above.
 Target A 0.165 - 0.665 at [(i=center-3),(j=center)]
 New: Target A 0.165 - 0.5 at [(i=center-3),(j=center)]
 since we do not need to keep in memory that long

 protocolNum::9. stimulated SC CompensatoryI saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center+3)]
 Stimulation 0.07 - .11 at [(i=center-3),(j=center)]
 Reduced target error due to location - only going 1 direction

 protocolNum::10. stimulated SC CompensatoryII saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center)]
 Stimulation 0.07 - 0.11 at [(i=center),(j=center-2)]
 Increased target error due to location - must go two directions

 protocolNum::11 stimulated FEF CompensatoryI saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center+3)]
 Stimulation 0.07 - 0.11 at [(i=center-3),(j=center)]

 protocolNum::12. stimulated FEF Compensatory II saccade.
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center)]
 Stimulation 0.07 - 0.11 at [(i=center),(j=center-2)]
 Increased target error due to location - must go two directions

 protocolNum::13. stimulated FEF LesionSC I saccade. - no SC
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center+3)]
 Stimulation 0.07 - 0.11 at [(i=center-3),(j=center)]

 protocolNum::14. stimulated SC LesionFEF I saccade. no FEF
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center,center)]
 Target A 0.02 - 0.07 at [(i=center-3),(j=center+3)]
 Stimulation 0.07 - 0.11 at [(i=center-3),(j=center)]

 protocolNum::15. memoryDouble2 saccade
 delta = 0.005 =  5 msec
 end-time time 0.7 = .7 sec.
 Fixation 0-0.02 at [(i=center),(j=center)]
 Target A 0.02 - 0.07 at [(i=center),(j=center-2)]
 Target B 0.09 - 0.105 at [(i=center-3),(j=center)]
 Target A 0.17 - 0.5 at [(i=center),(j=center-2)]
*/
package DomineyModel.VisualInput.v1_1_1.src;

nslModule VisualInput(int stdsz, int bigsz){

//NSL Version: 3_0_n
//Sif Version: 9
//libNickName: DomineyModel
//moduleName:  VisualInput
//versionName: 1_1_1
//floatSubModules: true


//variables 
public NslDoutFloat2 visualinput(bigsz,bigsz); // 
public NslDoutFloat2 visualinputSub(stdsz,stdsz); // 
private double currentTime=0; // 
private double currentTimePlusDelta=0; // 
private float value=90; // 
private int center; // 
private int centerP3; // 
private int centerP2; // 
private int centerM2; // 
private int centerM3; // 
private int bigszDiv3; // 
private int bigszDiv3Time2Min1; // 
private NslInt0 protocolNum; // 

//methods 
public void initModule() 
{	
	visualinput.nslSetAccess('W');
}

public void initRun() 
{	
	protocolNum=-1;
	protocolNum=(NslInt0)nslGetValue("domineyModel.protocolNum");
	if (system.debug>1) 
	{
		nslPrintln("VisualInput:initRun:The protocol num is: "+protocolNum);
	}

	bigszDiv3 =bigsz/3;
	bigszDiv3Time2Min1=bigszDiv3 +bigszDiv3 -1;

	// Have to do the following because we start at 0 and we do not
	// know where the scheduler will schedule the VisualInput Module
	// Note: for 9xby9 the center is 4,4
	// Note: for 27xby27 the center is 13,13

	visualinput=0;	

	center = (int)bigsz/2;
	centerP3 = center + 3;
	centerP2 = center + 2;
	centerM3 = center - 3;
	centerM2 = center - 2;

	visualinput[center][center]=value;
}

public void simRun() 
{
	//currentTime=system.getCurRunTime();	
	currentTime=system.getCurTime();	
	currentTimePlusDelta=currentTime+0.005;  //doing to match NSL2.1.7 

	if (system.debug>=1) 
	{
		nslPrintln("VisualInput:simRun: NSLJ currentTime: "+currentTime);
		nslPrintln("VisualInput:simRun: NSLJ currentTimePlusDelta: "+currentTimePlusDelta);
        }
	currentTime=currentTimePlusDelta;

	// all non-manual protocolNums have fixation on from 0 to 0.02
	if ((protocolNum!=0)&&(currentTime>=0.00)&&(currentTime<=0.02))
	{
		visualinput[center][center]=value;
	}
	// ("single")
	if (protocolNum==1) 
	{
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07)) 
		{
			visualinput[centerP3][centerM3]=value;
//			visualinput[center][centerP3]=value;
		}
		//  turn off target
		if (currentTime>0.07) 
		{
			visualinput[centerP3][centerM3]=0;
//			visualinput[center][centerP3]=0;
		}
	}
	// ("memorySingleI"))
	else if (protocolNum==2) 
	{
		// was .58 but that delay was not needed
		if (currentTime>0.28)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM2][centerP2]=value;
		}
		// unlight first target
		if (currentTime>0.07)
		{
			visualinput[centerM2][centerP2]=0;
		}
	}
	// ("memorySingleII"))
	else if (protocolNum==3) 
	{
		// was .58 but that delay was not needed
		if (currentTime>0.28)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM2][centerM3]=value;
		}
		// unlight first target
		if (currentTime>0.07)
		{
			visualinput[centerM2][centerM3]=0;
		}
	}
	//("doubleI")
	else if (protocolNum==4) 
	{
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][center]=value;
		}
		// unlight first target
		if ((currentTime>0.07)&&(currentTime<=0.08)) 
		{
			visualinput[centerM3][center]=0;
		}
		// light up second target
		if ((currentTime>=0.09)&&(currentTime<=0.13)) 
		{
			visualinput[centerM3][centerP3]=value;
		}
		// unlight second target
		if (currentTime>0.13) 
		{
			visualinput[centerM3][centerP3]=0;
		}

	} 
	//("doubleII")
	else if (protocolNum==5) 
	{
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[center][centerM2]=value;
		}
		// unlight first target
		if ((currentTime>0.07)&&(currentTime<=0.08)) 
		{
			visualinput[center][centerM2]=0;
		}
		// light up second target
	        // 92doc: Target B 0.08 - 0.12
	        // new: Target B 0.085 - 0.125
		if ((currentTime>=0.085)&&(currentTime<=0.125)) 
		{
			visualinput[centerM3][center]=value;
		}
		// unlight second target
		if (currentTime>0.125) 
		{
			visualinput[centerM3][center]=0;
		}
	} 
	// ("lesion SC single")
	else if (protocolNum==6) 
	{
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.4)) 
		{
			visualinput[centerP3][centerM3]=value;
		}
		//  turn off target
		if (currentTime>0.4) 
		{
			visualinput[centerP3][centerM3]=0;
		}
	}
	// ("lesion FEF single")
	else if (protocolNum==7) 
	{
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.4)) 
		{
			visualinput[centerP3][centerM3]=value;
		}
		//  turn off target
		if (currentTime>0.4) 
		{
			visualinput[centerP3][centerM3]=0;
		}
	}
	// ("memoryDouble"))
	else if (protocolNum==8) 
	{
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.05))
		{
			visualinput[centerM3][center]=value;
		}
		// unlight first target
		if ((currentTime>0.05)&&(currentTime<=0.06))
		{
			visualinput[centerM3][center]=0;
		}
		// light up second target
		// doc92: if ((currentTime>=0.085)&&(currentTime<=0.10)){
		// worked:if ((currentTime>=0.095)&&(currentTime<=0.11)){
	        // but shot past fovea	a little
		if ((currentTime>=0.085)&&(currentTime<=0.11))
		{
			visualinput[centerM3][centerP3]=value;
		}
		// unlight second target
		if ((currentTime>0.11)&&(currentTime<=0.115)) 
		{
			visualinput[centerM3][centerP3]=0;
		}
		// light first target for second time
		if ((currentTime>=0.165)&&(currentTime<=0.5))
		{
			visualinput[centerM3][center]=value;
		}
		// unlight first target
		if (currentTime>0.5) 
		{
			visualinput[centerM3][center]=0;
		}
	}
	// Compensatory
	//("stimulated SC I")) 
	// rest done in StimSC.mod
	else if (protocolNum==9) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][centerP3]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][centerP3]=0;
		}
	}
	//("stimulated SC II"))
	// the rest of the stimulation is done in Stimulation
	else if (protocolNum==10) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][center]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][center]=0;
		}
	}
	// ("stimulated FEF I"))
	// the rest of the stimulation is done in Stimulation
	else if (protocolNum==11) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][centerP3]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][centerP3]=0;
		}
	}	
	// ("stimulated FEF II")) 
	// the rest of the stimulation is done in Stimulation
	else if (protocolNum==12) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][center]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][center]=0;
		}
		// the rest of the stimulation is done in Stimulation
	}
	// ("stimulated FEF - LesionSC I")) 
	// the rest of the stimulation is done in Stimulation 
	else if (protocolNum==13) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][centerP3]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][centerP3]=0;
		}
		// the rest of the stimulation is done in Stimulation
	}
	// ("stimulated SC - LesionFEF I")) 
	// the rest of the stimulation is done in Stimulation
	else if (protocolNum==14) 
	{
		// the stimulation is the difference in these two modules
		// fixation - off
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.07))
		{
			visualinput[centerM3][centerP3]=value;
		}
		// stimulus in Stimulation module
		// unlight first target
		if (currentTime>0.07) 
		{
			visualinput[centerM3][centerP3]=0;
		}
		// the rest of the stimulation is done in Stimulation
	}
	// ("memoryDouble2"))
	// from the description in the 92 article figure 12
	else if (protocolNum==15) 
	{
		if (currentTime>0.02)
		{
			visualinput[center][center]=0;
		}
		// light up first target
		if ((currentTime>=0.02)&&(currentTime<=0.06))
		{
			visualinput[center][centerM2]=value;
		}
		// unlight first target
		if ((currentTime>0.06)&&(currentTime<=0.07))
		{
			visualinput[center][centerM2]=0;
		}
		// light up second target
		if ((currentTime>=0.09)&&(currentTime<=0.105))
		{
			visualinput[centerM3][center]=value;
		}
		// unlight second target
		if ((currentTime>0.105)&&(currentTime<=0.11)) 
		{
			visualinput[centerM3][center]=0;
		}
		// light first target for second time
		if ((currentTime>=0.17)&&(currentTime<=0.5))
		{
			visualinput[center][centerM2]=value;
		}
		// unlight first target
		if (currentTime>0.5) 
		{
			visualinput[center][centerM2]=0;
		}
	}
	else if (protocolNum.get()==0) 
	{
		// do not do anything for the manual protocol
		//nslPrintln("The manual protocol has been choosen.");
	}
	else 
	{
		nslPrintln("That protocolNum is not recognized: protocolNum: "+protocolNum);
		//nslPrintln("That protocolNum is not recognized: protocolNum: ",protocolNum);
	}

	// for debug
	nslGetSector(visualinputSub.get(),visualinput,bigszDiv3,bigszDiv3,bigszDiv3Time2Min1,bigszDiv3Time2Min1);
	if (system.debug>=1) 
	{
		nslPrintln("VisualInput:visualinputSub");
		nslPrintln(visualinputSub);
	}
} //end simRun
public void makeConn(){
}
}//end VisualInput

