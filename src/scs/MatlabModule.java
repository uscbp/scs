package scs;

import scs.util.SCSUtility;
import scs.util.UserPref;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;

public class MatlabModule extends Module
{
    public MatlabModule()
    {
        super();
    }
    
    public MatlabModule(int sifVersionNum, String libNickName, String moduleName, String moduleVersion,
                        String moduleType, String arguments, boolean moduleBuffering)
    {
        super(sifVersionNum, libNickName, moduleName, moduleVersion, moduleType, arguments, moduleBuffering);
    }

    /**
     * Will only save first time - need to make manual changes after that
     * @return boolean
     */
    public boolean save(String dirStr, String sifFilePath) throws IOException
    {
        File currDir = new File(SCSUtility.getMatlabPathUsingNick(libNickName, moduleName));
        if (!currDir.isDirectory())
        {
            currDir.mkdirs();
            writeMatlab(currDir);
        }
        boolean success=false;
        try
        {
            success=super.save(dirStr,sifFilePath);
        }
        catch(Exception e)
        {}
        return success;
    }

    public void writeMatlab(File moduleMatlabDir) throws IOException
    {
        String moduleMatlabPathStr=moduleMatlabDir.getAbsolutePath();
        if(!moduleMatlabPathStr.endsWith(File.separator))
            moduleMatlabPathStr+=File.separatorChar;

        writeConstructor(moduleMatlabPathStr);
        writeFunction("simRun", moduleMatlabPathStr);
        writeFunction("endSys", moduleMatlabPathStr);
        writeFunction("initModule", moduleMatlabPathStr);
        writeFunction("endModule", moduleMatlabPathStr);
        writeFunction("initRun", moduleMatlabPathStr);
        writeFunction("endRun", moduleMatlabPathStr);
        writeFunction("initRunEpoch", moduleMatlabPathStr);
        writeFunction("endRunEpoch", moduleMatlabPathStr);
        writeFunction("initTrainEpoch", moduleMatlabPathStr);
        writeFunction("initTrain", moduleMatlabPathStr);
        writeFunction("simTrain", moduleMatlabPathStr);
        writeFunction("endTrain", moduleMatlabPathStr);
        writeFunction("endTrainEpoch", moduleMatlabPathStr);

        StringBuilder nslModuleDir=new StringBuilder(UserPref.installPath);
        if(!nslModuleDir.toString().endsWith(File.separator))
            nslModuleDir.append(File.separatorChar);
        nslModuleDir.append("lib");
        nslModuleDir.append(File.separatorChar);
        nslModuleDir.append("@NslModule");
        nslModuleDir.append(File.separatorChar);

        File newNslModuleDir= new File(SCSUtility.getMatlabPathUsingNick(libNickName,"NslModule"));
        if(!newNslModuleDir.exists())
            newNslModuleDir.mkdirs();

        SCSUtility.copyFolder(new File(nslModuleDir.toString()),newNslModuleDir);

        FileChannel inCh = new FileInputStream(nslModuleDir.toString()+"set.m").getChannel();
        FileChannel outCh = new FileOutputStream(moduleMatlabPathStr+"set.m").getChannel();
        inCh.transferTo( 0, inCh.size(), outCh);

        inCh = new FileInputStream(nslModuleDir.toString()+"get.m").getChannel();
        outCh = new FileOutputStream(moduleMatlabPathStr+"get.m").getChannel();
        inCh.transferTo( 0, inCh.size(), outCh);

        inCh = new FileInputStream(nslModuleDir.toString()+"subsasgn.m").getChannel();
        outCh = new FileOutputStream(moduleMatlabPathStr+"subsasgn.m").getChannel();
        inCh.transferTo( 0, inCh.size(), outCh);

        inCh = new FileInputStream(nslModuleDir.toString()+"subsref.m").getChannel();
        outCh = new FileOutputStream(moduleMatlabPathStr+"subsref.m").getChannel();
        inCh.transferTo( 0, inCh.size(), outCh);


    }

    private void writeFunction(String functionName, String moduleMatlabPathStr) throws FileNotFoundException
    {
        StringBuilder fileName=new StringBuilder(moduleMatlabPathStr);
        fileName.append(functionName);
        fileName.append(".m");
        PrintWriter out=new PrintWriter(new FileOutputStream(fileName.toString()));

        out.print("function this=");
        out.print(functionName);
        out.print("(this)\n");
        out.println((new StringBuilder("%% ")).append(functionName).append(" function ").append(this.moduleName));
        out.println();
        out.println("%% Port information");

        StringBuilder inports=new StringBuilder();
        StringBuilder mem=new StringBuilder();
        StringBuilder outports=new StringBuilder();
        for(int i=0; i<variables.size(); i++)
        {
            Declaration var=(Declaration)variables.get(i);
            if(var.varDialogType.equals("InputPort"))
            {
                inports.append("%% this.");
                inports.append(var.varName);
                inports.append(" = zeros(");
                StringTokenizer params=new StringTokenizer(var.varParams,",");
                if(params.countTokens()==1)
                    inports.append("1,");
                inports.append(var.varParams);
                inports.append(");\n");
            }
            else if(var.varDialogType.equals("BasicVariable"))
            {
                mem.append("%% this.");
                mem.append(var.varName);
                mem.append(" = zeros(");
                StringTokenizer params=new StringTokenizer(var.varParams,",");
                if(params.countTokens()==1)
                    mem.append("1,");
                mem.append(var.varParams);
                mem.append(");\n");
            }
            else if(var.varDialogType.equals("OutputPort"))
            {
                outports.append("%% this.");
                outports.append(var.varName);
                outports.append(" = zeros(");
                StringTokenizer params=new StringTokenizer(var.varParams,",");
                if(params.countTokens()==1)
                    outports.append("1,");
                outports.append(var.varParams);
                outports.append(");\n");
            }
        }
        out.println("%% Inport information");
        out.print(inports.toString());
        out.println("%% Outport information");
        out.print(outports.toString());
        out.println("%% Memory information");
        out.print(mem.toString());
        out.println("%% In this function, all module port and variable values should be set as this.fieldname=value");
        out.println();
        out.println("%% fill out here");
        out.flush();
        out.close();
    }

    private void writeConstructor(String moduleMatlabPathStr) throws FileNotFoundException
    {
        StringBuilder constructorName=new StringBuilder(moduleMatlabPathStr);
        constructorName.append(this.moduleName);
        constructorName.append(".m");
        PrintWriter out=new PrintWriter(new FileOutputStream(constructorName.toString()));
        out.print("function instance=");
        out.print(this.moduleName);
        out.print('(');
        StringTokenizer args=new StringTokenizer(this.arguments, ",");
        int writtenArgs=0;
        while(args.hasMoreTokens())
        {
            StringTokenizer arg=new StringTokenizer(args.nextToken(), " ");
            if(arg.hasMoreTokens())
            {
                String argName=arg.nextToken();
                if(arg.hasMoreTokens())
                    argName=arg.nextToken();

                if(writtenArgs>0)
                    out.print(',');
                out.print(argName);
                writtenArgs++;
            }
        }
        out.print(")\n");
        out.println((new StringBuilder("%% Constructor function ")).append(this.moduleName));
        out.println();
        args=new StringTokenizer(this.arguments, ",");
        while(args.hasMoreTokens())
        {
            StringTokenizer arg=new StringTokenizer(args.nextToken(), " ");
            if(arg.hasMoreTokens())
            {
                String argName=arg.nextToken();
                if(arg.hasMoreTokens())
                    argName=arg.nextToken();

                out.print("instance.");
                out.print(argName);
                out.print(" = ");
                out.print(argName);
                out.print(";\n");
            }
        }
        StringBuilder inports=new StringBuilder("instance.inport_name = [");
        StringBuilder mem=new StringBuilder("instance.memory_name = [");
        StringBuilder outports=new StringBuilder("instance.outport_name = [");
        int numIn=0;
        int numMem=0;
        int numOut=0;
        for(int i=0; i<this.variables.size(); i++)
        {
            Declaration var=(Declaration)this.variables.get(i);
            if(var.varDialogType.equals("InputPort"))
            {
                if(numIn>0)
                    inports.append(',');
                inports.append("{'");
                inports.append(var.varName);
                inports.append("'}");
                numIn++;
            }
            else if(var.varDialogType.equals("BasicVariable"))
            {
                if(numMem>0)
                    mem.append(',');
                mem.append("{'");
                mem.append(var.varName);
                mem.append("'}");
                numMem++;
            }
            else if(var.varDialogType.equals("OutputPort"))
            {
                if(numOut>0)
                    outports.append(',');
                outports.append("{'");
                outports.append(var.varName);
                outports.append("'}");
                numOut++;
            }
        }
        inports.append("];");
        mem.append("];");
        outports.append("];");
        out.println(inports.toString());
        out.println(mem.toString());
        out.println(outports.toString());
        out.println((new StringBuilder("instance.classname = '")).append(this.moduleName).append("';"));
        for(int i=0; i<this.variables.size(); i++)
        {
            Declaration var=(Declaration)this.variables.get(i);
            if(!var.varDialogType.equals("SubModule"))
            {
                out.print("instance.");
                out.print(var.varName);
                out.print(" = zeros(");
                StringTokenizer params=new StringTokenizer(var.varParams,",");
                if(params.countTokens()==1)
                    out.print("1,");
                out.print(var.varParams);
                out.print(");\n");
            }
        }
        out.println((new StringBuilder("temp = NslModule('")).append(this.moduleName).append("');"));
        out.println((new StringBuilder("instance = class(instance,'")).append(this.moduleName).append("',temp);"));
        out.flush();
        out.close();
    }
}
