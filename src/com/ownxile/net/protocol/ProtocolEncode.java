package com.ownxile.net.protocol;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ownxile.net.packet.Packet;

public class ProtocolEncode implements ProtocolEncoder {

	/**
	 * Only CodecFactory can create us.
	 */
	public ProtocolEncode() {
	}

	@Override
	/**
	 * Releases resources used by this encoder.
	 * @param session
	 */
	public void dispose(IoSession session) throws Exception {
	}

	@Override
	/**
	 * Encodes a message.
	 * @param session
	 * @param message
	 * @param out
	 */
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		try {
			synchronized (session) {
				final Packet p = (Packet) message;
				final byte[] data = p.getData();
				final int dataLength = p.getLength();
				ByteBuffer buffer;
				if (!p.isBare()) {
					buffer = ByteBuffer.allocate(dataLength + 3);
					final int id = p.getId();
					buffer.put((byte) id);
					if (p.getSize() != Packet.Size.Fixed) { // variable length
						// Logger.log("variable length: id="+id+",dataLength="+dataLength);
						if (p.getSize() == Packet.Size.VariableByte) {
							if (dataLength > 255) {
								// then we can represent
								// with 8 bits!
								throw new IllegalArgumentException(
										"Tried to send packet length "
												+ dataLength
												+ " in 8 bits [pid="
												+ p.getId() + "]");
							}
							buffer.put((byte) dataLength);
						} else if (p.getSize() == Packet.Size.VariableShort) {
							if (dataLength > 65535) {
								// then we can represent
								// with 16 bits!
								throw new IllegalArgumentException(
										"Tried to send packet length "
												+ dataLength
												+ " in 16 bits [pid="
												+ p.getId() + "]");
							}
							buffer.put((byte) (dataLength >> 8));
							buffer.put((byte) dataLength);
						}
					}
				} else {
					buffer = ByteBuffer.allocate(dataLength);
				}
				buffer.put(data, 0, dataLength);
				buffer.flip();
				out.write(buffer);
			}
		} catch (final Exception err) {
			err.printStackTrace();
		}
	}

}
