/*******************************************************************************
 *  # Copyright (c) 2016 {UPRC}.
 *  # All rights reserved. This program and the accompanying materials
 *  # are made available under the terms of the The MIT License (MIT).
 *  # which accompanies this distribution, and is available at
 *  # http://opensource.org/licenses/MIT
 *
 *  # Contributors:
 *  #    {Constantinos Patsakis} {UPRC}
 *  #    {Stamatis Glykos} {UPRC}
 *  #    {Constantinos Alexandris} {UPRC}
 *  # Initially developed in the context of OPERANDO EU project www.operando.eu 
 *******************************************************************************/

package eu.operando.pdr.dan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class Helper {
	
	public static long stream(InputStream input, OutputStream output) throws IOException {
	    try (
	        ReadableByteChannel inputChannel = Channels.newChannel(input);
	        WritableByteChannel outputChannel = Channels.newChannel(output);
	    ) {
	        ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
	        long size = 0;

	        while (inputChannel.read(buffer) != -1) {
	            buffer.flip();
	            size += outputChannel.write(buffer);
	            buffer.clear();
	        }

	        return size;
	    }
	}
}
