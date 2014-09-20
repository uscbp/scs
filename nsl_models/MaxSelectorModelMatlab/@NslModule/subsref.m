function b = subsref(a,index)
%SUBSREF Define field name indexing for asset objects
switch index(1).type
    case '.'
        fname=fieldnames(a);
        if sum(strcmp(fname,index(1).subs))~=0
            switch length(index)
                case 1
                    b=eval(['a.' index(1).subs ]);
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
                        str=[str ');'];
                        b=eval(str);
                    end
            end
        end
        
        mname=methods(a);
        if sum(strcmp(mname,index(1).subs))~=0
            str=['b=' index(1).subs '(a' ];
            if length(index)>1
                ff=index(2).subs;
                for j=1:length(ff)
                    if strcmp(class(ff{j}),'char')
                        str=[str ',''' ff{j} ''''];
                    else
                        eval(['arg' num2str(j) '= ff{' num2str(j) '}']);
                        str=[str ',arg' num2str(j)];
                    end
                end
            end
            str=[str ');'];
            eval(str);

        end
    case '{}'
        error('Invalid')
end