function instance=MVlayer(size)
%% Constructor function MVlayer

instance.size = size;
instance.inport_name = [{'u_in'}];
instance.memory_name = [{'vp'},{'h2'},{'tau'}];
instance.outport_name = [{'vf'}];
instance.classname = 'MVlayer';
instance.u_in = zeros(1,size);
instance.vf = zeros();
instance.vp = zeros();
instance.h2 = zeros();
instance.tau = zeros();
temp = NslModule('MVlayer');
instance = class(instance,'MVlayer',temp);
