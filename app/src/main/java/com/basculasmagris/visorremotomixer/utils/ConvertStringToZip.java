package com.basculasmagris.visorremotomixer.utils;

import android.util.Log;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ConvertStringToZip {
    private static String TAG = "DEBZip";

    public byte[] compress(String source) {

        try {
            // Encode a String into bytes
            String inputString = source;
            int lenght = source.length();
            byte[] input = inputString.getBytes();

            // Compress the bytes
            byte[] output = new byte[lenght];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            compresser.end();

            // Decode the bytes into a String
            String outputString = new String(output, 0, compressedDataLength);
            Log.i(TAG, "outputString lenght: " + outputString.length() + "   output : " + compressedDataLength);
            return output;
        } catch (Exception ex) {
            // handle
        }
        return null;
    }
    public String decompress(byte [] output){
        return decompress(output,8192);
    }

    public String decompress(byte [] output,int lenght){

        try {
            // Encode a String into bytes

            // Compress the bytes
            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output);
            byte[] result = new byte[lenght+100];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength);
            return outputString;
        } catch(Exception ex) {
            // handle
        }
        return null;
    }


}
