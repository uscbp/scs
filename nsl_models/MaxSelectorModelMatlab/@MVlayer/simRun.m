function a=simRun(a)
%% simRun function MVlayer

%% Port information
%% Inport information
%% instance.u_in = zeros(1,size);
%% Memory information
%% instance.vp = zeros();
%% instance.h2 = zeros();
%% instance.tau = zeros();
%% Outport information
%% instance.vf = zeros();
%% In this function, output should be from as instance.fieldname=value

%% fill out here

a.vp = a.vp+.1*(-a.vp + sum(a.u_in) - a.h2)/a.tau;
a.vf = max(0.0,a.vp);

