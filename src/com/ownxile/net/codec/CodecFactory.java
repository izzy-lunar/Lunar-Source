package com.ownxile.net.codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ownxile.net.protocol.LoginProtocol;
import com.ownxile.net.protocol.ProtocolEncode;

public class CodecFactory implements ProtocolCodecFactory {

	/**
	 * The decoder.
	 */
	private final ProtocolDecoder decoder = new LoginProtocol();

	/**
	 * The encoder.
	 */
	private final ProtocolEncoder encoder = new ProtocolEncode();

	@Override
	/**
	 * Get the decoder.
	 */
	public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

	@Override
	/**
	 * Get the encoder.
	 */
	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

}
