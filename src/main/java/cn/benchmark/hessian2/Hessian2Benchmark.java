/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.benchmark.hessian2;

import cn.benchmark.model.SimpleBO;
import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import org.apache.dubbo.common.serialize.hessian2.Hessian2SerializerFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class Hessian2Benchmark {

    private static final ThreadLocal<Hessian2Output> OUTPUT_TL = ThreadLocal.withInitial(() -> {
        Hessian2Output h2o = new Hessian2Output(null);
        h2o.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
        h2o.setCloseStreamOnClose(true);
        return h2o;
    });

    private static final ThreadLocal<Hessian2Input> INPUT_TL = ThreadLocal.withInitial(() -> {
        Hessian2Input h2i = new Hessian2Input(null);
        h2i.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
        h2i.setCloseStreamOnClose(true);
        return h2i;
    });

    private final SimpleBO simpleBO = SimpleBO.getInstance(System.nanoTime());

    private final byte[] bytes = nonExceptionSerialize(simpleBO);

    private static byte[] nonExceptionSerialize(SimpleBO simpleBO) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
            Hessian2Output mH2o = OUTPUT_TL.get();
            mH2o.init(outputStream);
            mH2o.writeObject(simpleBO);
            mH2o.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("!!!!!!!!!!!!!");
            System.exit(-1);
        }
        return null;
    }


    @Benchmark
    public Object testDeserializeOriginal() throws IOException {
        Hessian2Input mH2i = new Hessian2Input(new ByteArrayInputStream(bytes));
        mH2i.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
        return mH2i.readObject(SimpleBO.class);
    }

    @Benchmark
    public Object testDeserializeOptimize() throws IOException {
        Hessian2Input mH2i = INPUT_TL.get();
        mH2i.init(new ByteArrayInputStream(bytes));
        return mH2i.readObject(SimpleBO.class);
    }

    @Benchmark
    public byte[] testSerializeOriginal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        Hessian2Output mH2o = new Hessian2Output(outputStream);
        mH2o.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
        mH2o.writeObject(simpleBO);
        mH2o.flush();
        return outputStream.toByteArray();
    }

    @Benchmark
    public byte[] testSerializeOptimize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        Hessian2Output mH2o = OUTPUT_TL.get();
        mH2o.init(outputStream);
        mH2o.writeObject(simpleBO);
        mH2o.flush();
        return outputStream.toByteArray();
    }

    public static void main(String[] args) throws RunnerException {
        // 使用一个单独进程执行测试，执行5遍warmup，然后执行5遍测试
        Options opt = new OptionsBuilder()
                .include(Hessian2Benchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run(); }
}
