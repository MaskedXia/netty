package cn.itcast.server.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        int i = 1 / 1;
        return "你好, " + msg;
    }
}