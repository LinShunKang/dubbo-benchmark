package cn.benchmark.method;

import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.MethodModel;
import org.apache.dubbo.rpc.model.ServiceModel;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * Created by LinShunkang on 2019/09/30
 */
@State(Scope.Thread) // 每个测试线程一个实例
public class AbstractBenchmark {

    private String serviceName = DemoService.class.getName();

    private String methodName = "echo";

    private Invocation invocation = null;

    @Setup
    public void setup() throws NoSuchMethodException {
        ApplicationModel.registerServiceModel(DemoService.class);
        invocation = new RpcInvocation(methodName, serviceName, new Class<?>[]{String.class}, null, null, null);
        MethodResolver.getMethodTypes(invocation, serviceName, methodName);
    }

    @Benchmark
    public Type[] testMethodModel() {
        Optional<ServiceModel> serviceModelOpt = ApplicationModel.getServiceModel(serviceName);
        if (serviceModelOpt.isPresent()) {
            ServiceModel serviceModel = serviceModelOpt.get();
            Optional<MethodModel> methodModelOpt = serviceModel.getMethod(methodName, invocation.getParameterTypes());
            if (methodModelOpt.isPresent()) {
                return methodModelOpt.get().getReturnTypes();
            }
        }
        return null;
    }

    @Benchmark
    public Type[] testMethodResolver() throws NoSuchMethodException {
        return MethodResolver.getMethodTypes(invocation, serviceName, methodName);
    }

    public static void main(String[] args) throws RunnerException {
        // 使用一个单独进程执行测试，执行5遍warmup，然后执行5遍测试
        Options opt = new OptionsBuilder()
                .include(AbstractBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}
