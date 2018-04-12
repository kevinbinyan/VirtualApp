package com.snail.antifake.jni;

public class EmulatorDetectUtil
{
  static
  {
//    System.loadLibrary("emulator_check");
  }
  
  public static boolean detectCheck()
  {
    return true;
//    return detect();
  }
  
  public static native boolean detect();
}
