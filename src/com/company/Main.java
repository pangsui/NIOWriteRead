package com.company;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try(FileOutputStream binFile = new FileOutputStream("data.dat");
            FileChannel binChannel = binFile.getChannel()){
            ByteBuffer buffer= ByteBuffer.allocate(100);
            byte[] outputBytes = "Hello World".getBytes();
            buffer.put(outputBytes);
            long inpos1 = outputBytes.length;
            buffer.putInt(100);
            long inpos2 = inpos1 + Integer.BYTES;
            buffer.putInt(-345);
            byte[] outputBytes2 = "Nice to meet you".getBytes();
            buffer.put(outputBytes2);
            buffer.putInt(1000);
            long inpos3 = inpos2 + outputBytes2.length + Integer.BYTES;
            buffer.flip();
            binChannel.write(buffer);


//let's read the data now
            RandomAccessFile ra = new RandomAccessFile("data.dat","rwd");
            FileChannel channel = ra.getChannel();
            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            channel.read(readBuffer);
            readBuffer.flip();
            byte[] inputSpring = new byte[outputBytes.length];
            readBuffer.get(inputSpring);
            System.out.println("InputString :"+ new String(inputSpring));
            System.out.println("Int1 :"+readBuffer.getInt());
            System.out.println("Int2 :"+readBuffer.getInt());

            byte[] inputSpring2 = new byte[outputBytes2.length];
            readBuffer.get(inputSpring2);
            System.out.println("InputString2 :"+ new String(inputSpring2));
            System.out.println("Int3 :"+readBuffer.getInt());

            //copying from one file to another
            RandomAccessFile fileCopy = new RandomAccessFile("dataCopy.dat","rw");//destination file
            FileChannel copyChannel = fileCopy.getChannel();
            channel.position(0);
            long numOfByteTransfer = copyChannel.transferFrom(channel,0,channel.size());
            System.out.println("number of byte transfer is :"+numOfByteTransfer);
            channel.close();
            binFile.close();
            copyChannel.close();


        }
        catch ( IOException e){
            e.printStackTrace();
        }
    }
}
