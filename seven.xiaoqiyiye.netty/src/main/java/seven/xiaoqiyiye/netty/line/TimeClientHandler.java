package seven.xiaoqiyiye.netty.line;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter{

	
	/* 
	 * 客户端连接到服务器后，会调用这个方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//发送query time给服务端
		ByteBuf msg = Unpooled.copiedBuffer("query time".getBytes());
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//接受到服务端返回的响应
		String resp = (String)msg;
		System.out.println("Now is:" + resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

	
}
