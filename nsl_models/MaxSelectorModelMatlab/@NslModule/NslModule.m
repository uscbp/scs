function a = NslModule(varargin)
% Constructor function (abstract class)

switch nargin
case 0
% if no input arguments, create a default object
   a.inport_name = [];
   a.memory_name = [];
   a.outport_name = [];
   a.classname = 'NslModule';
   a = class(a,'NslModule');  
case 1
% if single argument of class asset, return it
   if (isa(varargin{1},'asset'))
      a = varargin{1};
   elseif (isa(varargin{1},'char'))
   a.inport_name = [];
   a.memory_name = [];
   a.outport_name = [];
   a.classname=varargin{1};
   a = class(a,'NslModule');
   end 
otherwise
   error('Wrong number of input arguments')
end
