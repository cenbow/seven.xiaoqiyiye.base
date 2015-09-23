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
		//�������߳��飬������ѯ
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		try {
			//���ǿͻ��˵�������
			Bootstrap bs = new Bootstrap();
			//�����߳���
			bs.group(bossGroup)
			//����option����
			.option(ChannelOption.TCP_NODELAY, true)
			//���ÿͻ���SocketChannel
			.channel(NioSocketChannel.class)
			//���ÿͻ��˴�����
			.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
			//���ӵ���������
			ChannelFuture future = bs.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally{
			//�����˳�
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
