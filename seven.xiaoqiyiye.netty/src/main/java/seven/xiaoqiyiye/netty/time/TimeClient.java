package seven.xiaoqiyiye.netty.time;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	public void connection(String host, int port) throws Exception{
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap bs = new Bootstrap();
			bs.group(workerGroup)
				.option(ChannelOption.TCP_NODELAY, true)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
			ChannelFuture future = bs.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally{
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		try {
			new TimeClient().connection("127.0.0.1", 7777);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
