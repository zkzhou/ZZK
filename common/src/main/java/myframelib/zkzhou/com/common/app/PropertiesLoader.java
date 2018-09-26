/**
 * Copyright (C) 2004 KuGou-Inc.All Rights Reserved
 */

package myframelib.zkzhou.com.common.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

/**
 * 描述：Properties文件读取
 * 
 */
public class PropertiesLoader {

    private static final int NONE = 0, SLASH = 1, UNICODE = 2, CONTINUE = 3, KEY_DONE = 4,
            IGNORE = 5;

    public static synchronized Hashtable<Object, Object> load(Reader in) throws IOException {

        Hashtable<Object, Object> properties = new Hashtable<Object, Object>();

        if (in == null) {
            throw new NullPointerException("in == null");
        }
        int mode = NONE, unicode = 0, count = 0;
        char nextChar, buf[] = new char[40];
        int offset = 0, keyLength = -1, intVal;
        boolean firstChar = true;

        BufferedReader br = new BufferedReader(in);

        while (true) {
            intVal = br.read();
            if (intVal == -1) {
                break;
            }
            nextChar = (char) intVal;

            if (offset == buf.length) {
                char[] newBuf = new char[buf.length * 2];
                System.arraycopy(buf, 0, newBuf, 0, offset);
                buf = newBuf;
            }
            if (mode == UNICODE) {
                int digit = Character.digit(nextChar, 16);
                if (digit >= 0) {
                    unicode = (unicode << 4) + digit;
                    if (++count < 4) {
                        continue;
                    }
                } else if (count <= 4) {
                    throw new IllegalArgumentException(
                            "Invalid Unicode sequence: illegal character");
                }
                mode = NONE;
                buf[offset++] = (char) unicode;
                if (nextChar != '\n') {
                    continue;
                }
            }
            if (mode == SLASH) {
                mode = NONE;
                switch (nextChar) {
                    case '\r':
                        mode = CONTINUE; // Look for a following \n
                        continue;
                    case '\n':
                        mode = IGNORE; // Ignore whitespace on the next line
                        continue;
                    case 'b':
                        nextChar = '\b';
                        break;
                    case 'f':
                        nextChar = '\f';
                        break;
                    case 'n':
                        nextChar = '\n';
                        break;
                    case 'r':
                        nextChar = '\r';
                        break;
                    case 't':
                        nextChar = '\t';
                        break;
                    case 'u':
                        mode = UNICODE;
                        unicode = count = 0;
                        continue;
                }
            } else {
                switch (nextChar) {
                    case '#':
                    case '!':
                        if (firstChar) {
                            while (true) {
                                intVal = br.read();
                                if (intVal == -1) {
                                    break;
                                }
                                nextChar = (char) intVal;
                                if (nextChar == '\r' || nextChar == '\n') {
                                    break;
                                }
                            }
                            continue;
                        }
                        break;
                    case '\n':
                        if (mode == CONTINUE) { // Part of a \r\n sequence
                            mode = IGNORE; // Ignore whitespace on the next line
                            continue;
                        }
                        // fall into the next case
                    case '\r':
                        mode = NONE;
                        firstChar = true;
                        if (offset > 0 || (offset == 0 && keyLength == 0)) {
                            if (keyLength == -1) {
                                keyLength = offset;
                            }
                            String temp = new String(buf, 0, offset);
                            properties.put(temp.substring(0, keyLength), temp.substring(keyLength));
                        }
                        keyLength = -1;
                        offset = 0;
                        continue;
                    case '\\':
                        if (mode == KEY_DONE) {
                            keyLength = offset;
                        }
                        mode = SLASH;
                        continue;
                    case ':':
                    case '=':
                        if (keyLength == -1) { // if parsing the key
                            mode = NONE;
                            keyLength = offset;
                            continue;
                        }
                        break;
                }
                if (Character.isWhitespace(nextChar)) {
                    if (mode == CONTINUE) {
                        mode = IGNORE;
                    }
                    // if key length == 0 or value length == 0
                    if (offset == 0 || offset == keyLength || mode == IGNORE) {
                        continue;
                    }
                    if (keyLength == -1) { // if parsing the key
                        mode = KEY_DONE;
                        continue;
                    }
                }
                if (mode == IGNORE || mode == CONTINUE) {
                    mode = NONE;
                }
            }
            firstChar = false;
            if (mode == KEY_DONE) {
                keyLength = offset;
                mode = NONE;
            }
            buf[offset++] = nextChar;
        }
        if (mode == UNICODE && count <= 4) {
            throw new IllegalArgumentException("Invalid Unicode sequence: expected format \\uxxxx");
        }
        if (keyLength == -1 && offset > 0) {
            keyLength = offset;
        }
        if (keyLength >= 0) {
            String temp = new String(buf, 0, offset);
            String key = temp.substring(0, keyLength);
            String value = temp.substring(keyLength);
            if (mode == SLASH) {
                value += "\u0000";
            }
            properties.put(key, value);
        }

        return properties;
    }
}
