package cn.itcast.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class TestPipeline {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup());
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                // 1. 通过 channel 拿到 pipeline
                ChannelPipeline pipeline = ch.pipeline();
                // 2. 添加处理器 head ->  h1 -> h2 ->  h4 -> h3 -> h5 -> h6 -> tail
                pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("1");
                        ByteBuf buf = (ByteBuf) msg;
                        String name = buf.toString(Charset.defaultCharset());
                        super.channelRead(ctx, name);
                    }
                });
                pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("2");
                        Student student = new Student(msg.toString());
                        super.channelRead(ctx, student); // 将数据传递给下个 handler，如果不调用，调用链会断开 或者调用 ctx.fireChannelRead(student);
                    }
                });

                pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("3，结果{}， class:{} ", msg, msg.getClass());
                        super.channelRead(ctx, msg);
                        ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes())); // 直接到tail
                    }
                });
                pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log.debug("4");
                        super.write(ctx, msg, promise);
                    }
                });
                pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log.debug("5");
                        super.write(ctx, msg, promise);
                    }
                });
                pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        log.debug("6");
                        super.write(ctx, msg, promise);
                    }
                });
            }
        });
        serverBootstrap.bind(8080);
    }
    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
    }
}
