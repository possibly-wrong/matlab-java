function out = callJava(methodName, javaObj, varargin)
%CALLJAVA Call Java method with primitive array reference arguments.
%
%   out = callJava(methodName, javaObj, varargin) acts like the built-in
%   javaMethod(methodName, javaObj, varargin), but supports modifiable
%   primitive array reference arguments constructed as matlab.JavaArray
%   instances.
%
%   Unlike the built-in javaMethod, this function requires that numeric
%   argument types match their "nearest" Java parameter types. For example:
%
%   s = java.lang.String('foo');
%   dst = matlab.JavaArray('java.lang.Character', 3);
%   callJava('getChars', s, int32(0), int32(3), dst, int32(0));
%   dst.get()
%
%   See also JAVAMETHOD, CALLLIB, LIBPOINTER.

    if isjava(javaObj)
        out = matlab.JavaArray.call(methodName, ...
            javaObj, javaObj.getClass(), varargin);
    else
        out = matlab.JavaArray.call(methodName, ...
            [], java.lang.Class.forName(javaObj), varargin);
    end
end
