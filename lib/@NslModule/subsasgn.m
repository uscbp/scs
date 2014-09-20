function a = subsasgn(a,index,val)
% SUBSASGN Define index assignment for asset objects
switch index(1).type
case '.'
    propName=index(1).subs;
    fname=fieldnames(a);
    if sum(strcmp(fname,propName)~=0)
        switch length(index)
            case 1
                eval(['a.' propName '=val;' ]);
            case 2
                if strcmp('()',index(2).type)~=0
                    idx=index(2).subs;
                    str=['a.' index(1).subs '('];
                    for i=1:length(idx)
                        str=[str num2str(idx{i})];
                        if i~=length(idx)
                            str=[str ','];
                        end
                    end
                    str=[str ')=val;'];
                    eval(str);
                end
        end
    end
end
