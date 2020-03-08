package util;

public class FixedLengthEncoderUtil
{
    public static String encoder(String s)
    {
        String head = generatedHead(s.getBytes().length);
        return head + s;
    }

    public static String generatedHead(int length)
    {
        String headLengthPart = String.format("%10d", length);
        String reserved = String.format("%10s", " ");
        return headLengthPart + reserved;
    }



}
