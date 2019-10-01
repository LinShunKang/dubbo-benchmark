### 快速启动
#### 下载并打包 Dubbo 项目
* git clone git@github.com:apache/dubbo.git
* git checkout performance-tuning-2.7.x
* mvn clean install -Dmaven.test.skip=true

#### 下载本项目
* git clone git@github.com:LinShunKang/dubbo-benchmark.git
* mvn clean package 
* 运行 `ThroughputBenchmark` 和 `TimeCostBenchmark` 

#### 压测环境
* JMH version: 1.21
* VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
* OS: macOS Mojave 10.14.6
* CPU: Intel(R) Core(TM) i9-8950HK CPU @ 2.90GHz

#### 压测结果
##### ThroughputBenchmark 
````
Benchmark                                Mode  Cnt      Score      Error   Units
ThroughputBenchmark.testMethodModel     thrpt    5  49127.361 ± 1146.757  ops/ms
ThroughputBenchmark.testMethodResolver  thrpt    5  81325.656 ± 1090.294  ops/ms
````

##### TimeCostBenchmark 
````
Benchmark                                                          Mode      Cnt       Score   Error  Units
TimeCostBenchmark.testMethodModel                                  avgt        5      20.334 ± 0.520  ns/op
TimeCostBenchmark.testMethodResolver                               avgt        5      12.305 ± 0.538  ns/op
TimeCostBenchmark.testMethodModel                                sample  1029952      63.687 ± 4.538  ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.00          sample               12.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.50          sample               51.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.90          sample               54.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.95          sample               55.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.99          sample               65.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.999         sample              164.000          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p0.9999        sample            21504.150          ns/op
TimeCostBenchmark.testMethodModel:testMethodModel·p1.00          sample           867328.000          ns/op
TimeCostBenchmark.testMethodResolver                             sample  1477584      52.835 ± 1.055  ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.00    sample                6.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.50    sample               45.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.90    sample               46.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.95    sample               47.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.99    sample               48.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.999   sample               92.000          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p0.9999  sample            20015.456          ns/op
TimeCostBenchmark.testMethodResolver:testMethodResolver·p1.00    sample            88448.000          ns/op
````

#### 压测结论
* MethodResolver 吞吐量比 Dubbo 官方实现高 65.5%
* MethodResolver 延迟 TP999 比 Dubbo 官方实现低 43.9%
