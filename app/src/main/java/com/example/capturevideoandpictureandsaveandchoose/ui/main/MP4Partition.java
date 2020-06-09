package com.example.capturevideoandpictureandsaveandchoose.ui.main;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MP4Partition {

    public static void main(String[] args) {
        final int NUMS_OF_SEGMENT = 11;
        partition("file\\movie.wmv", NUMS_OF_SEGMENT);
    }

    public static void partition(String fromFilePath, int size) {
        File aFile = new File(fromFilePath);
        RandomAccessFile raf = null;

        int numberToSeg = 0;

        numberToSeg = (int)aFile.length()/size;
        Log.v("ggg777","" + numberToSeg);

        File[] tempfile = new File[numberToSeg];
        FileOutputStream[] fos = new FileOutputStream [numberToSeg];
        for(int i = 0; i < numberToSeg; i++) {
            tempfile[i] = new File(fromFilePath.substring(0,fromFilePath.length()-4) + "_"+ i + ".mp4");
            fos[i] = null;
        }

        try {
            raf = new RandomAccessFile(aFile, "rw");
            for(int i = 0; i < numberToSeg; i++) {
                fos[i] = new FileOutputStream(tempfile[i], true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        FileChannel fc = raf.getChannel();
        FileChannel[] fcout = new FileChannel[numberToSeg];
        for(int i = 0; i < numberToSeg; i++) {
            fcout[i] = fos[i].getChannel();
        }

        MappedByteBuffer[] mbuf = new MappedByteBuffer[numberToSeg];
        long segPosition = 0;
        try {
            long segSize = fc.size()/numberToSeg;
            long accSize = 0;

            for(int i = 0; i < numberToSeg; i++) {
                if(i == numberToSeg - 1) {
                    segSize = fc.size() - accSize;
                }

                accSize += segSize;
                mbuf[i] = fc.map(FileChannel.MapMode.READ_ONLY, segPosition, segSize);
                segPosition += fc.size()/numberToSeg;
                fcout[i].write(mbuf[i]);
                fcout[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
