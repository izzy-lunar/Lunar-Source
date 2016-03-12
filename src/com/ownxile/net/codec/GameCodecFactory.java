package com.ownxile.net.codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ownxile.net.protocol.ProtocolDecode;
import com.ownxile.net.protocol.ProtocolEncode;
import com.ownxile.util.ISAAC;

/**
 * Provides access to the encoders and decoders for the 317 protocol.
 * 
 * @author Graham
 * 
 */
public class GameCodecFactory implements ProtocolCodecFactory {

	/**
	 * The decoder.
	 */
	private final ProtocolDecoder decoder;

	/**
	 * The encoder.
	 */
	private final ProtocolEncoder encoder = new ProtocolEncode();

	public GameCodecFactory(ISAAC inC) {
		decoder = new ProtocolDecode(inC);
	}

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
