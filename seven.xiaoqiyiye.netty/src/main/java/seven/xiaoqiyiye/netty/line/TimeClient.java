package seven.xiaoqiyiye.netty.line;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

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
						//添加基于行的解码器和字符串解码器
						ch.pipeline()
									.addLast(new StringDecoder())	
									.addLast(new TimeClientHandler());
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
