package cn.benchmark.method;


import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.rpc.Invocation;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MethodResolver {

    //Map<service, Map<methodName, MethodMappings>>
    private static final ConcurrentMap<String, ConcurrentMap<String, MethodMappings>> invocationMethodMap = new ConcurrentHashMap<String, ConcurrentMap<String, MethodMappings>>(4096);

    public static Type[] getMethodTypes(Invocation invocation, String service, String methodName) throws NoSuchMethodException {
        MethodMappings methodMappings = getMethodMappings(service, methodName);
        MethodEntry methodEntry = getMethodEntry(methodMappings, service, invocation);
        return methodEntry.getReturnTypes();
    }

    private static MethodMappings getMethodMappings(String service, String methodName) {
        ConcurrentMap<String, MethodMappings> methodMap = invocationMethodMap.get(service);
        if (methodMap == null) {
            invocationMethodMap.putIfAbsent(service, new ConcurrentHashMap<String, MethodMappings>(32));
            methodMap = invocationMethodMap.get(service);
        }

        MethodMappings methodMappings = methodMap.get(methodName);
        if (methodMappings == null) {
            methodMap.putIfAbsent(methodName, new MethodMappings());
            methodMappings = methodMap.get(methodName);
        }
        return methodMappings;
    }

    private static MethodEntry getMethodEntry(final MethodMappings methodMappings, String service, Invocation invocation) throws NoSuchMethodException {
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        MethodEntry methodEntry = methodMappings.getByParameterTypes(parameterTypes);
        if (methodEntry != null) {
            return methodEntry;
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (methodMappings) {
            methodEntry = methodMappings.getByParameterTypes(parameterTypes);
            if (methodEntry != null) {
                return methodEntry;
            }

            Class<?> clazz = ReflectUtils.forName(service);
            Method method = clazz.getMethod(invocation.getMethodName(), parameterTypes);
            methodEntry = new MethodEntry(method, parameterTypes);
            methodMappings.addMethod(methodEntry);
        }
        return methodEntry;
    }


    private static class MethodMappings {

        private final List<MethodEntry> methodEntryList;

        private MethodMappings() {
            this.methodEntryList = new CopyOnWriteArrayList<MethodEntry>();
        }

        public void addMethod(MethodEntry methodEntry) {
            this.methodEntryList.add(methodEntry);
        }

        public MethodEntry getByParameterTypes(Class<?>[] parameterTypes) {
            List<MethodEntry> list = this.methodEntryList;
            for (int i = 0; i < list.size(); i++) {
                MethodEntry methodEntry = list.get(i);
                if (isMatch(methodEntry.parameterTypes, parameterTypes)) {
                    return methodEntry;
                }
            }
            return null;
        }

        private boolean isMatch(Class<?>[] types1, Class<?>[] types2) {
            if (types1 == null || types2 == null || types1.length != types2.length) {
                return false;
            }

            for (int i = 0; i < types1.length; i++) {
                if (!types1[i].equals(types2[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class MethodEntry {

        private final Method method;

        private final Class<?>[] parameterTypes;

        private final Type[] returnTypes;

        public MethodEntry(Method method, Class<?>[] parameterTypes) {
            this.method = method;
            this.parameterTypes = parameterTypes;
            if (method.getReturnType() == void.class) {
                this.returnTypes = null;
            } else {
                this.returnTypes = new Type[]{method.getReturnType(), method.getGenericReturnType()};
            }
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public Method getMethod() {
            return method;
        }

        public Type[] getReturnTypes() {
            return returnTypes;
        }
    }
}
