/**
 * Copyright (C) 2004 KuGou-Inc.All Rights Reserved
 */

package myframelib.zkzhou.com.common.app;

import android.content.Context;

import java.io.RandomAccessFile;

public class PackageIdentifierUtil {

    public static class Identifier {

        public byte type;

        public String value;

        public Identifier(byte type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public static final String MARK = "KG";

    public static final int MARK_SIZE = 2;

    public static final int LENGTH_SIZE = 2;

    public static final int TYPE_SIZE = 1;

    public static Identifier getIdentifier(Context context) {
        try {
            RandomAccessFile packageFile = new RandomAccessFile(context.getPackageResourcePath(),
                    "r");
            // seek to MARK and read KG.
            final long FILE_LENGTH = packageFile.length();
            packageFile.seek(FILE_LENGTH - MARK_SIZE);
            byte[] markBytes = new byte[MARK_SIZE];
            packageFile.read(markBytes, 0, markBytes.length);
            String mark = new String(markBytes, "UTF-8");
            if (MARK.equals(mark)) {
                // seek to LENGTH and read value length.
                packageFile.seek(FILE_LENGTH - (MARK_SIZE + LENGTH_SIZE));
                final short valueLength = packageFile.readShort();
                // seek to extend start index.
                packageFile.seek(FILE_LENGTH - (MARK_SIZE + LENGTH_SIZE + TYPE_SIZE + valueLength));
                // read value.
                byte[] valueBytes = new byte[valueLength];
                packageFile.read(valueBytes);
                // read type.
                String value = new String(valueBytes, "UTF-8");
                byte type = packageFile.readByte();
                // return entity.
                return new Identifier(type, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
