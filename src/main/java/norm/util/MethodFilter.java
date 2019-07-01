package norm.util;



import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class MethodFilter implements CallbackFilter {

    private static final MethodFilter instance = new MethodFilter();

    public static MethodFilter getInstance() {
        return instance;
    }

    private static final Set<String> ignoreMethods = new HashSet<String>();

    static {
        ignoreMethods.add("finalize");
        ignoreMethods.add("toString");
        ignoreMethods.add("equals");
        ignoreMethods.add("hashCode");
        ignoreMethods.add("clone");
    }


    @Override
    public int accept(Method method) {
        return ignoreMethods.contains(method.getName()) ? 1 : 0;
    }
}
