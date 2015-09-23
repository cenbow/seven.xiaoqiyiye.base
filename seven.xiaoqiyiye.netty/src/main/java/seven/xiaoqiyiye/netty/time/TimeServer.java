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
		//�������߳��飬������ѯ
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		//�������߳��飬����IO�¼�
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//���Ƿ������˵�������
			ServerBootstrap sbs = new ServerBootstrap();
			//�����߳���
			sbs.group(bossGroup, workerGroup)
			//����Channel���ͣ���������ʹ��NioServerSocketChannel	
			.channel(NioServerSocketChannel.class)
			//�������߳������
			.option(ChannelOption.SO_BACKLOG, 1024)
			//���ô�����	
			.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new TimeServerHandler());
					}
				});
			//�󶨵��˿�
			ChannelFuture future = sbs.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally{
			//�����˳�
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
