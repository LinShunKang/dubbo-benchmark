### 快速启动
#### 下载并打包 Dubbo 项目
* git clone git@github.com:LinShunKang/dubbo.git
* git checkout performance/reduce_object_allocation_v5
* mvn clean install -Dmaven.test.skip=true

#### 下载本项目
* git clone git@github.com:LinShunKang/dubbo-benchmark.git
* mvn clean package 
* 运行 `URLDecodeBenchmark`
* 运行 `MeasureURLDecodeGC`(单独运行每个方法，并在 JVM 启动参数中加上, -javaagent:/path/to/aprof.jar)

#### 压测环境
* JMH version: 1.22
* VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
* OS: macOS Mojave 10.14.6
* CPU: Intel(R) Core(TM) i9-8950HK CPU @ 2.90GHz

#### 压测结果
##### URLDecodeBenchmark (Use JMH)
```
Benchmark                                Mode  Cnt    Score    Error   Units
URLDecodeBenchmark.testDecodedOptimize  thrpt    5  536.920 ± 13.665  ops/ms
URLDecodeBenchmark.testDecodedOriginal  thrpt    5  517.530 ±  5.144  ops/ms
URLDecodeBenchmark.testEncodedOptimize  thrpt    5  355.157 ±  1.304  ops/ms
URLDecodeBenchmark.testEncodedOriginal  thrpt    5  163.323 ±  0.640  ops/ms
```

##### MeasureURLDecodeGC (Use aprof)
```
Benchmark                                Allocated
MeasureURLDecodeGC.testDecodedOptimize   213,874MB
MeasureURLDecodeGC.testDecodedOriginal   273,073MB  
MeasureURLDecodeGC.testEncodedOptimize   213,891MB
MeasureURLDecodeGC.testEncodedOriginal   382,050MB
``` 


#### 压测结论
* 内存分配
    - EncodedStr: 优化后 比 优化前 少分配 44% 的内存
    - DecodedStr: 优化后 比 优化前 少分配 22% 的内存
* 吞吐量
    - EncodedStr: 优化后 比 优化前 高 117%
    - DecodedStr: 优化后 比 优化前 高 3.5%

