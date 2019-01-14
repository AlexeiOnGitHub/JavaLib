package com.ak.lib;

/*
 * Title:        Library. Log system.
 * Description:  
 * Copyright:    Copyright (c) 2018
 * Company:      N/A
 * Author        Alexei KUCHUMOV
 * Date:         26.12.2018
 * Source:       https://github.com/AlexeiOnGitHub/JavaLib  
 *
 */

//---------------------//
//-- import section  --//
//---------------------//
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
//--
import r.j.*;
//---------------------//

public class Logger {

  //=============//
  //  Constants  //
  //=============//

  //----------------------------------------------------------
  //-- debug levels
  //----------------------------------------------------------
  public  static final int        eiDbgLevel_forced   = 0;
  public  static final int        eiDbgLevel_interr   = 1;
  public  static final int        eiDbgLevel_error    = 2;
  public  static final int        eiDbgLevel_warning  = 3;
  public  static final int        eiDbgLevel_debug    = 4;
  public  static final int        eiDbgLevel_details  = 5;

  //----------------------------------------------------------
  //-- prefixes
  //----------------------------------------------------------
  private static final String     ejsTracePrefix      = ":TRC:";
  private static final String[]   aejsDbgPrefixes     = {
    "",
    ":INTERNAL:",
    ":ERR:",
    ":WRN:",
    ":   :",
    ":>>>:",
  };

  //----------------------------------------------------------
  //-- init
  //----------------------------------------------------------
  public  static final int        eiDbgLevel_def      = 2;
  public  static final int        eiLogChunk_def      = 5000;
  public  static final boolean    eblIncludeMem_def   = false;
  public  static final boolean    eblIncludeTime_def  = true;
  public  static final boolean    eblUseLogFilter_def = false;
  public  static final boolean    eblUseTraceFilter_def = false;

  //----------------------------------------------------------
  //-- misc
  //----------------------------------------------------------
  private static final String     ejsEmpty            = "";
  private static final String     ejsAsterisk         = "*";
  private static final String     ejsDot              = ".";
  private static final String     ejsColon            = ":";
  private static final String     ejsThreePoints      = "...";
  private static final String     ejsTimeFormat       = new String("dd.MM.yy/HH:mm:ss");

  //=============//
  //  Variables  //
  //=============//

  //----------------------------------------------------------
  //-- logging
  //----------------------------------------------------------
  private static          SimpleDateFormat oDFTime    = new java.text.SimpleDateFormat(ejsTimeFormat);
  private static          Date    oDate               = new Date(0L);
  private static          String  jsTime              = oDFTime.format(oDate);
  private static          long    lSysTime            = 0L;
  //-- settings
  private static          boolean blIncludeMem        = eblIncludeMem_def;
  private static          boolean blIncludeTime       = eblIncludeTime_def;
  private static          int     iDbgLevel           = eiDbgLevel_def;
  private static          int     iLogChunk           = eiLogChunk_def;

  //----------------------------------------------------------
  //-- flags and lists
  //----------------------------------------------------------
  private static          boolean blUseLogFilter      = eblUseLogFilter_def;
  private static          ConcurrentHashMap<String, Integer> oLogFilterList = 
                            new ConcurrentHashMap<String, Integer>(100);
  private static          boolean blUseTraceFilter    = eblUseTraceFilter_def;
  private static          ConcurrentHashMap<String, Boolean> oTraceFilterList = 
                            new ConcurrentHashMap<String, Boolean>(100);

  //===============//
  //  Constructor  //
  //===============//

  //=============//
  //  Functions  //
  //=============//

  //----------------------------------------------------------
  //--  log functions
  //----------------------------------------------------------

  //----------------------------------------------------------
  public  static final void log (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_forced, null, jsMsg_, false, false);
  }
  //----------------------------------------------------------
  public  static final void loginterr (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_interr, aejsDbgPrefixes[eiDbgLevel_interr], jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void loginterrT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_interr, aejsDbgPrefixes[eiDbgLevel_interr], jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void loginterrWT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_interr, aejsDbgPrefixes[eiDbgLevel_interr], jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  public  static final void logerr (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_error, aejsDbgPrefixes[eiDbgLevel_error], jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void logerrT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_error, aejsDbgPrefixes[eiDbgLevel_error], jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void logerrWT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_error, aejsDbgPrefixes[eiDbgLevel_error], jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  public  static final void logwrn (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_warning, aejsDbgPrefixes[eiDbgLevel_warning], jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void logwrnT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_warning, aejsDbgPrefixes[eiDbgLevel_warning], jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void logwrnWT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_warning, aejsDbgPrefixes[eiDbgLevel_warning], jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  public  static final void logdbg (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_debug, aejsDbgPrefixes[eiDbgLevel_debug], jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void logdbgT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_debug, aejsDbgPrefixes[eiDbgLevel_debug], jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void logdbgWT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_debug, aejsDbgPrefixes[eiDbgLevel_debug], jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  public  static final void logdetails (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_details, aejsDbgPrefixes[eiDbgLevel_details], jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void logdetailsT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_details, aejsDbgPrefixes[eiDbgLevel_details], jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void logdetailsWT (String jsMsg_) {
    // CODE
    ___log(eiDbgLevel_details, aejsDbgPrefixes[eiDbgLevel_details], jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  private static final void ___log (int iLevel_, 
                                    String jsPrefix_, 
                                    String jsMessage_, 
                                    boolean blMem_, 
                                    boolean blTime_) {
    boolean _blNeedLog = true;
    String _jsThread, _jsClass, _jsClass_short=null, _jsMethod, _jsMem;
    Integer _oIRV;
    StackTraceElement[] _aoSTE;
    int _iIdx, _iLen;
    long _lST;
    // CODE
    //-- check if logging is necessary
    _block: {
      if (blUseLogFilter && oLogFilterList.size()>0){
        _jsThread = Thread.currentThread().getName();
        _aoSTE = Thread.currentThread().getStackTrace();
        _jsClass = _aoSTE[3].getClassName();
        _iIdx = _jsClass.lastIndexOf(ejsDot);
        if (_iIdx>=0){
          _jsClass_short = _jsClass.substring(_iIdx+1);
        } //-- end if
        _jsMethod = _aoSTE[3].getMethodName();
        //-- vvv
        _oIRV = oLogFilterList.get(_jsThread+ejsColon+_jsClass+ejsColon+_jsMethod);
        if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oIRV = oLogFilterList.get(_jsThread+ejsColon+_jsClass_short+ejsColon+_jsMethod);
          if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- vv*
        _oIRV = oLogFilterList.get(_jsThread+ejsColon+_jsClass+ejsColon+ejsAsterisk);
        if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oIRV = oLogFilterList.get(_jsThread+ejsColon+_jsClass_short+ejsColon+ejsAsterisk);
          if (_oIRV!=null){
            _blNeedLog = iLevel_<=_oIRV.intValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- v**
        _oIRV = oLogFilterList.get(_jsThread+ejsColon+ejsAsterisk+ejsColon+ejsAsterisk);
        if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
          break _block;
        } //-- end if
        //-- *vv
        _oIRV = oLogFilterList.get(ejsAsterisk+ejsColon+_jsClass+ejsColon+_jsMethod);
        if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oIRV = oLogFilterList.get(ejsAsterisk+ejsColon+_jsClass_short+ejsColon+_jsMethod);
          if (_oIRV!=null){
            _blNeedLog = iLevel_<=_oIRV.intValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- *v*
        _oIRV = oLogFilterList.get(ejsAsterisk+ejsColon+_jsClass+ejsColon+ejsAsterisk);
        if (_oIRV!=null){
          _blNeedLog = iLevel_<=_oIRV.intValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oIRV = oLogFilterList.get(ejsAsterisk+ejsColon+_jsClass_short+ejsColon+ejsAsterisk);
          if (_oIRV!=null){
            _blNeedLog = iLevel_<=_oIRV.intValue();
            break _block;
          } //-- end if
        } //-- end if
      } //-- end if
      _blNeedLog = iLevel_<=iDbgLevel;
    } //-- end block
    //-- output log message
    if (_blNeedLog){
      if (blMem_){
        _jsMem = "Mem["+(Runtime.getRuntime().freeMemory()/1048576)+"M]: ";
      }else{
        _jsMem = ejsEmpty;
      } //-- end if
      if (blTime_){
        _lST = System.currentTimeMillis();
        if ((_lST-lSysTime)>200L) {
          lSysTime = _lST;
          oDate.setTime(lSysTime);
          jsTime = oDFTime.format(oDate);
        }
      } //-- end if
      _aoSTE = Thread.currentThread().getStackTrace();
      _jsClass = _aoSTE[3].getClassName();
      _iIdx = _jsClass.lastIndexOf(ejsDot);
      if (_iIdx>=0){
        _jsClass = _jsClass.substring(_iIdx+1);
      } //-- end if
      _jsMethod = _aoSTE[3].getMethodName();
      _iLen = jsMessage_.length();
      for (int i=0; i<_iLen; i+=iLogChunk){
        rjlog.log((blMem_?_jsMem:ejsEmpty)+
                  (blTime_?jsTime:ejsEmpty)+
                  (jsPrefix_==null?ejsEmpty:jsPrefix_+"{"+Thread.currentThread().getName()+"} "+_jsClass+ejsDot+_jsMethod+"(): ")+
                  (i==0?ejsEmpty:ejsThreePoints)+
                  (i+iLogChunk>=_iLen?(i==0?jsMessage_:jsMessage_.substring(i))
                                     :jsMessage_.substring(i, i+iLogChunk)));
      } //-- end for
    } //-- end if
    return;
  }

  //----------------------------------------------------------
  //--  trace functions
  //----------------------------------------------------------

  //----------------------------------------------------------
  public  static final void trace (String jsMsg_) {
    // CODE
    ___trace(jsMsg_, blIncludeMem, blIncludeTime);
  }
  //----------------------------------------------------------
  public  static final void traceT (String jsMsg_) {
    // CODE
    ___trace(jsMsg_, blIncludeMem, true);
  }
  //----------------------------------------------------------
  public  static final void traceWT (String jsMsg_) {
    // CODE
    ___trace(jsMsg_, blIncludeMem, false);
  }
  //----------------------------------------------------------
  private static final void ___trace (String jsMessage_, 
                                      boolean blMem_, 
                                      boolean blTime_) {
    boolean _blNeedTrace = false;
    String _jsThread, _jsClass, _jsClass_short=null, _jsMethod, _jsMem;
    Boolean _oBRV;
    StackTraceElement[] _aoSTE;
    int _iIdx, _iLen;
    long _lST;
    // CODE
    //-- check if logging is necessary
    _block: {
      if (blUseTraceFilter){
        _jsThread = Thread.currentThread().getName();
        _aoSTE = Thread.currentThread().getStackTrace();
        _jsClass = _aoSTE[3].getClassName();
        _iIdx = _jsClass.lastIndexOf(ejsDot);
        if (_iIdx>=0){
          _jsClass_short = _jsClass.substring(_iIdx+1);
        } //-- end if
        _jsMethod = _aoSTE[3].getMethodName();
        //-- vvv
        _oBRV = oTraceFilterList.get(_jsThread+ejsColon+_jsClass+ejsColon+_jsMethod);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oBRV = oTraceFilterList.get(_jsThread+ejsColon+_jsClass_short+ejsColon+_jsMethod);
          if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- vv*
        _oBRV = oTraceFilterList.get(_jsThread+ejsColon+_jsClass+ejsColon+ejsAsterisk);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oBRV = oTraceFilterList.get(_jsThread+ejsColon+_jsClass_short+ejsColon+ejsAsterisk);
          if (_oBRV!=null){
            _blNeedTrace = _oBRV.booleanValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- v**
        _oBRV = oTraceFilterList.get(_jsThread+ejsColon+ejsAsterisk+ejsColon+ejsAsterisk);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
        //-- *vv
        _oBRV = oTraceFilterList.get(ejsAsterisk+ejsColon+_jsClass+ejsColon+_jsMethod);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oBRV = oTraceFilterList.get(ejsAsterisk+ejsColon+_jsClass_short+ejsColon+_jsMethod);
          if (_oBRV!=null){
            _blNeedTrace = _oBRV.booleanValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- *v*
        _oBRV = oTraceFilterList.get(ejsAsterisk+ejsColon+_jsClass+ejsColon+ejsAsterisk);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
        if (_jsClass_short!=null){
          _oBRV = oTraceFilterList.get(ejsAsterisk+ejsColon+_jsClass_short+ejsColon+ejsAsterisk);
          if (_oBRV!=null){
            _blNeedTrace = _oBRV.booleanValue();
            break _block;
          } //-- end if
        } //-- end if
        //-- ***
        _oBRV = oTraceFilterList.get(ejsAsterisk+ejsColon+ejsAsterisk+ejsColon+ejsAsterisk);
        if (_oBRV!=null){
          _blNeedTrace = _oBRV.booleanValue();
          break _block;
        } //-- end if
      } //-- end if
    } //-- end block
    //-- output log message
    if (_blNeedTrace){
      if (blMem_){
        _jsMem = "Mem["+(Runtime.getRuntime().freeMemory()/1048576)+"M]: ";
      }else{
        _jsMem = ejsEmpty;
      } //-- end if
      if (blTime_){
        _lST = System.currentTimeMillis();
        if ((_lST-lSysTime)>200L) {
          lSysTime = _lST;
          oDate.setTime(lSysTime);
          jsTime = oDFTime.format(oDate);
        }
      } //-- end if
      _aoSTE = Thread.currentThread().getStackTrace();
      _jsClass = _aoSTE[3].getClassName();
      _iIdx = _jsClass.lastIndexOf(ejsDot);
      if (_iIdx>=0){
        _jsClass = _jsClass.substring(_iIdx+1);
      } //-- end if
      _jsMethod = _aoSTE[3].getMethodName();
      _iLen = jsMessage_.length();
      for (int i=0; i<_iLen; i+=iLogChunk){
        rjlog.log((blMem_?_jsMem:ejsEmpty)+
                  (blTime_?jsTime:ejsEmpty)+
                  (ejsTracePrefix+"{"+Thread.currentThread().getName()+"} "+_jsClass+ejsDot+_jsMethod+"(): ")+
                  (i==0?ejsEmpty:ejsThreePoints)+
                  (i+iLogChunk>=_iLen?(i==0?jsMessage_:jsMessage_.substring(i))
                                     :jsMessage_.substring(i, i+iLogChunk)));
      } //-- end for
    } //-- end if
    return;
  }

  //----------------------------------------------------------
  public static void printException (Throwable oE_){
    String jsS = "";
    StackTraceElement[] aoSTE = null;
    // CODE
    if (oE_!=null){
      jsS = "* -- " + oE_.getMessage()+"\n";
      aoSTE = oE_.getStackTrace();
      for (int i=0; i<aoSTE.length; i++){
        jsS += ("* --   " + aoSTE[i] + "\n");
      } //-- end for
    } //-- end if
    //--
    rjlog.log("\n* ------------------------------------------------------- *\n" +
              "* -- Exception in " + Thread.currentThread().getName() + "\n" +
              "* ------------------------------------------------------- *\n" +
              jsS +
              "* ------------------------------------------------------- *\n");
  }

  //----------------------------------------------------------
  //--  get/check functions
  //----------------------------------------------------------

  //----------------------------------------------------------
  public static int getDebugLevel () {return iDbgLevel;}

  //----------------------------------------------------------
  public static boolean isLogFilterOn () {return blUseLogFilter;}

  //----------------------------------------------------------
  public static boolean isTraceFilterOn () {return blUseTraceFilter;}

  //----------------------------------------------------------
  public static boolean isMemIncluded () {return blIncludeMem;}

  //----------------------------------------------------------
  public static boolean isTimeIncluded () {return blIncludeTime;}

  //----------------------------------------------------------
  //--  set functions
  //----------------------------------------------------------

  //----------------------------------------------------------
  public static boolean setDebugLevel (int iLevel_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (!_checkLevel(iLevel_)){break _block;} //-- end if
      iDbgLevel = iLevel_;
      blRV = true;
    } //-- end block
    return blRV;
  }

  //----------------------------------------------------------
  public static boolean addLogFilter (String jsThread_, 
                                      String jsClass_, 
                                      String jsMethod_, 
                                      int iLevel_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (!_checkLevel(iLevel_)){break _block;} //-- end if
      if (!_checkName(jsThread_)){break _block;} //-- end if
      if (!_checkName(jsClass_)){break _block;} //-- end if
      if (!_checkName(jsMethod_)){break _block;} //-- end if
      try {
        oLogFilterList.put(jsThread_+ejsColon+jsClass_+ejsColon+jsMethod_, iLevel_);
        blRV = true;
      }
      catch (Exception e){}
    } //-- end block
    return blRV;
  }

  //----------------------------------------------------------
  public static boolean removeLogFilter (String jsThread_, 
                                         String jsClass_, 
                                         String jsMethod_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (!_checkName(jsThread_)){break _block;} //-- end if
      if (!_checkName(jsClass_)){break _block;} //-- end if
      if (!_checkName(jsMethod_)){break _block;} //-- end if
      try {
        oLogFilterList.remove(jsThread_+ejsColon+jsClass_+ejsColon+jsMethod_);
        blRV = true;
      }
      catch (Exception e){}
    } //-- end block
    return blRV;
  }

  //----------------------------------------------------------
  public static void clearLogFilters () {oLogFilterList.clear();}

  //----------------------------------------------------------
  public static boolean addTraceFilter (String jsThread_, 
                                        String jsClass_, 
                                        String jsMethod_,
                                        boolean blFlag_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (!_checkName(jsThread_)){break _block;} //-- end if
      if (!_checkName(jsClass_)){break _block;} //-- end if
      if (!_checkName(jsMethod_)){break _block;} //-- end if
      try {
        oTraceFilterList.put(jsThread_+ejsColon+jsClass_+ejsColon+jsMethod_, blFlag_);
        blRV = true;
      }
      catch (Exception e){}
    } //-- end block
    return blRV;
  }

  //----------------------------------------------------------
  public static boolean removeTraceFilter (String jsThread_, 
                                           String jsClass_, 
                                           String jsMethod_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (!_checkName(jsThread_)){break _block;} //-- end if
      if (!_checkName(jsClass_)){break _block;} //-- end if
      if (!_checkName(jsMethod_)){break _block;} //-- end if
      try {
        oTraceFilterList.remove(jsThread_+ejsColon+jsClass_+ejsColon+jsMethod_);
        blRV = true;
      }
      catch (Exception e){}
    } //-- end block
    return blRV;
  }

  //----------------------------------------------------------
  public static void clearTraceFilters () {oTraceFilterList.clear();}

  //----------------------------------------------------------
  public static void activateLogFilter (boolean blFlag_) {blUseLogFilter=blFlag_;}

  //----------------------------------------------------------
  public static void activateTraceFilter (boolean blFlag_) {blUseTraceFilter=blFlag_;}

  //----------------------------------------------------------
  public static void includeMem (boolean blFlag_) {blIncludeMem=blFlag_;}

  //----------------------------------------------------------
  public static void includeTime (boolean blFlag_) {blIncludeTime=blFlag_;}

  //----------------------------------------------------------
  //--  aux functions
  //----------------------------------------------------------

  //----------------------------------------------------------
  public static boolean _checkLevel (int iValue_) {
    boolean blRV = false;
    // CODE
    if (iValue_>=eiDbgLevel_forced && iValue_<=eiDbgLevel_details){
      blRV = true;
    } //-- end if
    return blRV;
  }

  //----------------------------------------------------------
  public static boolean _checkName (String jsValue_) {
    boolean blRV = false;
    // CODE
    _block: {
      if (jsValue_==null){break _block;} //-- end if
      if (jsValue_.length()==0){break _block;} //-- end if
      if (jsValue_.indexOf(ejsColon)>=0){break _block;} //-- end if
      blRV = true;
    } //-- end block
    return blRV;
  }
} //-- end of class <Logger>

//----------------------------------------------------------
//----------------------------------------------------------
//----------------------------------------------------------
