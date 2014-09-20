function a = set(a,varargin)
% SET Set properties and return the updated object

% fname=fieldnames(a)
% flag=zeros(length(varargin)/2,1);
% for j=1:length(varargin)/2
%     for i=1:length(fname)
%         if strcmp(fname{i},varargin(2*j-1))==1
%             flag(j)=flag(j)+1;
%         end
%     end
% end
% if find(flag==0)>0
%     error([varargin(find(flag==0)),' is not a valid property'])
%     return
% end

propertyArgIn = varargin;
while length(propertyArgIn) >= 2,
    propName = propertyArgIn{1};
    val = propertyArgIn{2};
    propertyArgIn = propertyArgIn(3:end);
    eval(['a.' propName '=val;']);
end

return
