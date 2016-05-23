package cn.sam.commontest.netty.client;

import java.util.List;

import cn.sam.commontest.netty.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder { // (1)
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
		if (in.readableBytes() < 4) {
			return; // (3)
		}

		out.add(new UnixTime(in.readUnsignedInt())); // (4)
	}
}