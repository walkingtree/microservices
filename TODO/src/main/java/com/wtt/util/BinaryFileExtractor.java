package com.wtt.util;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.google.common.io.ByteStreams;

public class BinaryFileExtractor implements ResponseExtractor<byte[]> {

	@Override
	public byte[] extractData(ClientHttpResponse response) throws IOException {
		return ByteStreams.toByteArray(response.getBody());
	}
}