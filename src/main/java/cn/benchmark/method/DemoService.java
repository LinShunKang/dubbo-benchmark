package cn.benchmark.method;

public interface DemoService {

    void sayHello(String name);

    void echo(String name);

    long getId1(long id) throws InterruptedException;

    long getId2(long id) throws InterruptedException;

    long getId3(long id) throws InterruptedException;

    long getId4(long id) throws InterruptedException;

    long getId5(long id);

    long getId6(long id);

    long getId7(long id);

    long getId8(long id);

}
