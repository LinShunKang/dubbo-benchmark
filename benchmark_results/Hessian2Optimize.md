### 快速启动
#### 下载并打包 Dubbo-Hessian-Lite 项目
* git clone git@github.com:LinShunKang/dubbo-hessian-lite.git
* git checkout master
* mvn clean install -Dmaven.test.skip=true

#### 下载并打包 Dubbo 项目
* git clone git@github.com:LinShunKang/dubbo.git
* git checkout performance/reduce_object_allocation_v6
* mvn clean install -Dmaven.test.skip=true

#### 下载本项目
* git clone git@github.com:LinShunKang/dubbo-benchmark.git
* mvn clean package 
* 运行 `Hessian2Benchmark`
* 运行 `MeasureHessian2GC`(单独运行每个方法，并在 JVM 启动参数中加上, -javaagent:/path/to/aprof.jar)

#### 压测环境
* JMH version: 1.22
* VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
* OS: macOS Mojave 10.14.6
* CPU: Intel(R) Core(TM) i9-8950HK CPU @ 2.90GHz

#### 压测结果
##### Hessian2Benchmark (Use JMH)
```
Benchmark                                   Mode  Cnt     Score    Error   Units
Hessian2Benchmark.testDeserializeOptimize  thrpt    5   706.627 ±  7.371  ops/ms
Hessian2Benchmark.testDeserializeOriginal  thrpt    5   625.794 ±  3.251  ops/ms
Hessian2Benchmark.testSerializeOptimize    thrpt    5  1204.872 ± 21.955  ops/ms
Hessian2Benchmark.testSerializeOriginal    thrpt    5   871.022 ± 15.290  ops/ms
```

##### MeasureHessian2GC (Use aprof)
```
Benchmark                                   Allocated
MeasureHessian2GC.testDeserializeOptimize   27,541MB
MeasureHessian2GC.testDeserializeOriginal   71,541MB  
MeasureHessian2GC.testSerializeOptimize     29,139MB
MeasureHessian2GC.testSerializeOriginal     157,139MB
``` 


#### 压测结论
* 内存分配
    - Serialize:   优化后 比 优化前 少分配 81.45% 的内存
    - Deserialize: 优化后 比 优化前 少分配 61.50% 的内存
* 吞吐量
    - Serialize:   优化后 比 优化前 高 12.91%
    - Deserialize: 优化后 比 优化前 高 38.33%
