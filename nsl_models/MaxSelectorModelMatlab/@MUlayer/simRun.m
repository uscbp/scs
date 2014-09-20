function this=simRun(this)
%% simRun function MUlayer

%% Port information
%% Inport information
%% this.s_in = zeros(1,size);
%% this.v_in = zeros();
%% Memory information
%% this.w1 = zeros();
%% this.w2 = zeros();
%% this.h1 = zeros();
%% this.k = zeros();
%% this.tau = zeros();
%% Outport information
%% this.uf = zeros(1,size);
%% this.up = zeros(1,size);
%% In this function, output should be from as this.fieldname=value

%% fill out here

this.up=.1*(-this.up + this.w1.*this.uf-this.w2.*this.v_in - this.h1 + this.s_in)./this.tau+this.up;

for i=1:this.size
    if this.up(i)<this.k
        this.uf(i)=0;
    else
        this.uf(i)=1.0;
    end
end
