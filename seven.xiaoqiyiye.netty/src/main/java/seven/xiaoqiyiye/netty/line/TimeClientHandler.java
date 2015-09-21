package seven.xiaoqiyiye.netty.line;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter{

	
	/* 
	 * �ͻ������ӵ��������󣬻�����������
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//����query time�������
		ByteBuf msg = Unpooled.copiedBuffer("query time".getBytes());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//���ܵ�����˷��ص���Ӧ
		String resp = (String)msg;
		System.out.println("Now is:" + resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

	
}
