function instance=MUlayer(size)
%% Constructor function MUlayer

instance.size = size;
instance.inport_name = [{'s_in'},{'v_in'}];
instance.memory_name = [{'w1'},{'w2'},{'h1'},{'k'},{'tau'}];
instance.outport_name = [{'uf'},{'up'}];
instance.classname = 'MUlayer';
instance.s_in = zeros(1,size);
instance.v_in = zeros();
instance.uf = zeros(1,size);
instance.w1 = zeros();
instance.w2 = zeros();
instance.h1 = zeros();
instance.k = zeros();
instance.tau = zeros();
instance.up = zeros(1,size);
temp = NslModule('MUlayer');
instance = class(instance,'MUlayer',temp);
