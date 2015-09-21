package seven.xiaoqiyiye.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class TimeClientHandler extends ChannelInboundHandlerAdapter{

	
	/* 
	 * 客户端连接到服务器后，会调用这个方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//发送query time给服务端
		byte[] req = "query time".getBytes();
		ByteBuf firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//接受到服务端返回的响应
		ByteBuf buf = (ByteBuf)msg;
		System.out.println("Now is:" + buf.toString(CharsetUtil.UTF_8));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

	
}
