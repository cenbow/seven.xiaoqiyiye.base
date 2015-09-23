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
		//定义主线程组，处理轮询
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		try {
			//这是客户端的启动类
			Bootstrap bs = new Bootstrap();
			//设置线程组
			bs.group(bossGroup)
			//设置option参数
			.option(ChannelOption.TCP_NODELAY, true)
			//设置客户端SocketChannel
			.channel(NioSocketChannel.class)
			//设置客户端处理器
			.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
			//连接到服务器端
			ChannelFuture future = bs.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally{
			//优雅退出
			bossGroup.shutdownGracefully();
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
