package com.order.cc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{
  public static String md5(String str)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte[] byteDigest = md.digest();

      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < byteDigest.length; offset++) {
        int i = byteDigest[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }

      return buf.toString();
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }return null;
  }
}
