package seven.xiaoqiyiye.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	public void bind(int port) throws Exception{
		//定义主线程组，处理轮询
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		//定义子线程组，处理IO事件
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//这是服务器端的启动类
			ServerBootstrap sbs = new ServerBootstrap();
			//设置线程组
			sbs.group(bossGroup, workerGroup)
			//设置Channel类型，服务器端使用NioServerSocketChannel	
			.channel(NioServerSocketChannel.class)
			//设置主线程组参数
			.option(ChannelOption.SO_BACKLOG, 1024)
			//设置处理器	
			.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new TimeServerHandler());
					}
				});
			//绑定到端口
			ChannelFuture future = sbs.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally{
			//优雅退出
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		try {
			new TimeServer().bind(7777);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
