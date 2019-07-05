package matlab;

import java.lang.reflect.*;

/**
 * A JavaArray is a wrapper for a primitive array, to be passed to the MATLAB
 * function callJava as a modifiable reference argument.
 */
public class JavaArray {
    private Object array;

    /**
     * Create a zero-initialized primitive array.
     *
     * @param className name of component primitive wrapper class
     * @param size      array dimensions
     */
    public JavaArray(String className, int[] size)
        throws ClassNotFoundException,
               NoSuchFieldException,
               IllegalAccessException {
        this.array = Array.newInstance((Class)
            Class.forName(className).getField("TYPE").get(null), size);
    }

    /**
     * Return this primitive array.
     *
     * @return this primitive array
     */
    public Object get() {
        return this.array;
    }

    /**
     * Set array element at the given index to the given (boxed) value.
     *
     * @param index zero-based indices of array element to change
     * @param value new boxed value of the indexed array element
     */
    public void set(int[] index, Object value) {
        Object a = this.array;
        for (int i = 0; i < index.length - 1; ++i) {
            a = Array.get(a, index[i]);
        }
        Array.set(a, index[index.length - 1], value);
    }

    /**
     * Invoke the given method, replacing any JavaArray arguments with their
     * primitive array reference contents.
     *
     * @param methodName name of method to invoke
     * @param object     object whose method is invoked, or null if static
     * @param cls        class of object whose method is invoked
     * @param args       method arguments
     * @return           value returned by invoked method
     */
    public static Object call(String methodName, Object object, Class cls,
                              Object[] args)
        throws IllegalAccessException,
               InvocationTargetException {
        Object out = null;
        for (Method method : cls.getMethods()) {
            if (method.getName().equals(methodName) &&
                method.getParameterCount() == args.length) {
                for (int i = 0; i < args.length; ++i) {
                    if (args[i] instanceof JavaArray) {
                        args[i] = ((JavaArray)args[i]).get();
                    }
                }
                out = method.invoke(object, args);
                break;
            }
        }
        return out;
    }
}
