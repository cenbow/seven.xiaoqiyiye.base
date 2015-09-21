package seven.xiaoqiyiye.netty.line;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

	public void bind(int port) throws Exception{
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap sbs = new ServerBootstrap();
			sbs.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline()
								.addLast(new StringDecoder())
								.addLast(new TimeServerHandler());
					}
				});
			ChannelFuture future = sbs.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally{
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
