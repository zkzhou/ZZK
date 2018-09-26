package myframelib.zkzhou.com.common.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import android.content.Context;
import android.text.TextUtils;

import com.chinamobile.mcloud.R;

/**
 * 拼音工具
 * 
 * @author 刘剑
 */
public class PinyinUtils
{
    /** map */
    private static char[][] sT9Map;
    
    private static PinyinUtils instance;
    
    private String[] pinyin = null;
    
    private byte[] mBuffer = null;
    
    private HashMap<String, String> mMultiHash = new HashMap<String, String>();
    
    /**
     * 构造
     * 
     * @param context context
     */
    private PinyinUtils(Context context)
    {
        pinyin = context.getResources().getStringArray(R.array.pinyin);
        try
        {
            initalize(context);
            initMultipleWords(context);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 单例对象
     * 
     * @param context context
     * @return PinyinUtils
     */
    public static synchronized PinyinUtils getInstance(Context context)
    {
        if (instance == null)
        {
            init(context);
        }
        return instance;
    }
    
    /**
     * 初始化
     * 
     * @param context context
     */
    public static void init(Context context)
    {
        instance = new PinyinUtils(context);
    }
    
    /**
     * 获取对应拼音
     * 
     * @param paramChar paramChar
     * @return String
     */
    public String getPinyin(char paramChar)
    {
        String str = null;
        if (paramChar == '_')
        {
            paramChar = 'ぁ';
        }
        str = mMultiHash.get(String.valueOf(paramChar));
        if (str != null)
        {
            return str;
        }
        if (paramChar < 0)
        {
            return String.valueOf(paramChar).toUpperCase(Locale.CHINA);
        }
        int i = 19968;
        int j = 40869;
        int i2 = paramChar;
        if ((i2 >= i) && (i2 <= j))
        {
            int i3 = (i2 - i) * 2;
            int i4 = this.mBuffer[i3] * 256;
            int i5 = i3 + 1;
            int l = ((this.mBuffer[i5] & 0x80) >> 7) * 128;
            int i6 = i4 + l;
            int i7 = i3 + 1;
            int i1 = this.mBuffer[i7] & 0x7F;
            int i8 = i6 + i1;
            // str = pinyin[i8] + paramChar;
            str = pinyin[i8];// TODO 测试效果
        }
        else
        {
            str = String.valueOf(paramChar);
        }
        return String.valueOf(str).toUpperCase(Locale.ENGLISH);
    }
    
    /**
     * paramChar转换成纯拼音
     * 
     * @param paramChar paramChar
     * @return String
     */
    public String getQuanPinItem(char paramChar)
    {
        String str = null;
        if (paramChar == '_')
        {
            paramChar = 'ぁ';
        }
        str = mMultiHash.get(String.valueOf(paramChar));
        if (str != null)
        {
            StringBuilder multiString = new StringBuilder();
            for (int l = 0; l < str.length(); l++)
            {
                if (l == 0)
                {
                    multiString.append(String.valueOf(str.charAt(l))
                            .toUpperCase(Locale.ENGLISH));
                }
                else
                {
                    if (str.charAt(l) == '_')
                    {
                        break;
                    }
                    multiString.append(String.valueOf(str.charAt(l))
                            .toLowerCase(Locale.ENGLISH));
                }
            }
            return multiString.toString();
        }
        if (paramChar < 0)
        {
            return String.valueOf(paramChar).toUpperCase(Locale.CHINA);
        }
        int i = 19968;
        int j = 40869;
        // Object localObject = Integer.valueOf(paramChar);
        int i2 = paramChar;
        if ((i2 >= i) && (i2 <= j))
        {
            int i3 = (i2 - i) * 2;
            int i4 = this.mBuffer[i3] * 256;
            int i5 = i3 + 1;
            int l = ((this.mBuffer[i5] & 0x80) >> 7) * 128;
            int i6 = i4 + l;
            int i7 = i3 + 1;
            int i1 = this.mBuffer[i7] & 0x7F;
            int i8 = i6 + i1;
            str = pinyin[i8];
        }
        else
        {
            str = String.valueOf(paramChar);
        }
        // String.valueOf(str.charAt(0)).toUpperCase(Locale.ENGLISH);
        // return String.valueOf(str).toUpperCase(Locale.ENGLISH);
        StringBuilder string = new StringBuilder();
        for (int l = 0; l < str.length(); l++)
        {
            if (l == 0)
            {
                string.append(String.valueOf(str.charAt(l))
                        .toUpperCase(Locale.ENGLISH));
            }
            else
            {
                string.append(String.valueOf(str.charAt(l))
                        .toLowerCase(Locale.ENGLISH));
            }
        }
        return string.toString();
    }
    
    /**
     * 获取拼音队列
     * 
     * @param words 字符串
     * @return ArrayList<String>
     */
    public ArrayList<String> getPinyins(String words)
    {
        if (TextUtils.isEmpty(words))
        {
            throw new NullPointerException("words is null!");
        }
        
        ArrayList<String> pinyins = new ArrayList<String>();
        char[] param = words.toCharArray();
        
        for (int i = 0; i < param.length; i++)
        {
            if (param[i] != ' ')
            {
                String temp = getPinyin(param[i]);
                pinyins.add(temp);
            }
        }
        pinyins.add(words);
        return pinyins;
        
    }
    
    /**
     * 获取全拼集合
     * 
     * @param words 字符串
     * @return ArrayList<String>
     */
    public ArrayList<String> getQuanPinList(String words)
    {
        if (TextUtils.isEmpty(words))
        {
            throw new NullPointerException("words is null!");
        }
        
        ArrayList<String> pinyins = new ArrayList<String>();
        char[] param = words.toCharArray();
        
        for (int i = 0; i < param.length; i++)
        {
            if (param[i] != ' ')
            {
                String temp = getQuanPinItem(param[i]);
                pinyins.add(temp);
            }
        }
        
        return pinyins;
        
    }
    
    /**
     * 获取全拼集合
     * 
     * @param words words
     * @return String
     */
    public String getPinyin(String words)
    {
        ArrayList<String> tokens = getPinyins(words);
        StringBuilder pinyinBuilder = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++)
        {
            pinyinBuilder.append(tokens.get(i));
        }
        return pinyinBuilder.toString();
    }
    
    /**
     * words转换成纯拼音
     * 
     * @param words words
     * @return String
     */
    public String getQuanPin(String words)
    {
        ArrayList<String> tokens = getQuanPinList(words);
        StringBuilder pinyinx = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++)
        {
            pinyinx.append(tokens.get(i));
        }
        return pinyinx.toString();
    }
    
    /**
     * 自从转ascii
     * 
     * @param str str
     * @return String
     */
    public String pinyinToInteger(String str)
    {
        char[] chars = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, length = chars.length; i < length; i++)
        {
            int ascii = (int) chars[i];
            if ((65 <= ascii && ascii <= 67) || (97 <= ascii && ascii <= 99))
            {
                builder.append(2);
            }
            else if ((68 <= ascii && ascii <= 70)
                    || (100 <= ascii && ascii <= 102))
            {
                builder.append(3);
            }
            else if ((71 <= ascii && ascii <= 73)
                    || (103 <= ascii && ascii <= 105))
            {
                builder.append(4);
            }
            else if ((74 <= ascii && ascii <= 76)
                    || (106 <= ascii && ascii <= 108))
            {
                builder.append(5);
            }
            else if ((77 <= ascii && ascii <= 79)
                    || (109 <= ascii && ascii <= 111))
            {
                builder.append(6);
            }
            else if ((80 <= ascii && ascii <= 83)
                    || (112 <= ascii && ascii <= 115))
            {
                builder.append(7);
            }
            else if ((84 <= ascii && ascii <= 86)
                    || (116 <= ascii && ascii <= 118))
            {
                builder.append(8);
            }
            else if ((87 <= ascii && ascii <= 90)
                    || (119 <= ascii && ascii <= 122))
            {
                builder.append(9);
            }
            else
            {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }
    
    /**
     * 初始化
     * 
     * @param context context
     * @throws IOException 异常
     */
    public void initalize(Context context) throws IOException
    {
        InputStream mInputStream = null;
        mInputStream = context.getAssets().open("pinyin.dat");
        mBuffer = new byte[mInputStream.available()];
        mInputStream.read(mBuffer);
        mInputStream.close();
    }
    
    /**
     * 初始化拼音内容
     * 
     * @param contextx contextx
     * @throws IOException
     */
    public void initMultipleWords(Context contextx) throws IOException
    {
        InputStream mInputStream = contextx.getAssets().open("duopinyin.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                mInputStream));
        String temp = null;
        
        while ((temp = reader.readLine()) != null)
        {
            
            int i = 0;
            String word = "";
            String pinYinBuf = "";
            StringTokenizer token = new StringTokenizer(temp, " ");
            while (token.hasMoreTokens())
            {
                if (i == 0)
                {
                    word = token.nextElement().toString();
                }
                else
                {
                    pinYinBuf += token.nextElement().toString() + "_";
                    if (!token.hasMoreElements())
                    {
                        if (pinYinBuf.endsWith("_"))
                        {
                            pinYinBuf = pinYinBuf.substring(0,
                                    pinYinBuf.length() - 1);
                        }
                        mMultiHash.put(word, pinYinBuf);
                    }
                }
                i++;
            }
            temp = null;
        }
        
        reader.close();
        reader = null;
        
        mInputStream.close();
        mInputStream = null;
    }
    
    /**
     * <判读字符串的第一个字符是否为汉字> <功能详细描述>
     * 
     * @param str str
     * @return [参数说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isChineseAtFirst(String str)
    {
        if (str == null || "".equals(str))
        {
            return false;
        }
        return (str.substring(0, 1)).matches("[\\u4E00-\\u9FA5]+");
    }
    
    /***
     * 获取字符串中每个字的首字母组成的字符串 如： Allen 获取后为 Allen 字符串 获取后为 ZFC
     * 
     * @param str 什么
     * @return String
     */
    public String getJianPin(String str)
    {
        StringBuilder pinyinbuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            String ch = getQuanPinItem(str.charAt(i));
            if (ch != null && ch.length() > 0)
            {
                pinyinbuilder.append(ch.charAt(0));
            }
        }
        return pinyinbuilder.toString().replaceAll(" ", "").toUpperCase();
    }
}
