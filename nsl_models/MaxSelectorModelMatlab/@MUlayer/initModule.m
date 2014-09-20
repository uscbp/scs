function a=initModule(a)
%% initModule function MUlayer

%% Port information
%% Inport information
%% instance.s_in = zeros(1,size);
%% instance.v_in = zeros();
%% Memory information
%% instance.w1 = zeros();
%% instance.w2 = zeros();
%% instance.h1 = zeros();
%% instance.k = zeros();
%% instance.tau = zeros();
%% Outport information
%% instance.uf = zeros(1,size);
%% instance.up = zeros(1,size);
%% In this function, output should be from as instance.fieldname=value

%% fill out here

a.uf=zeros(1,a.size);
a.up=zeros(1,a.size);
a.tau=1.0;
a.w1=1.0;
a.w2=1.0;
a.h1=0.1;
a.k=0.1;