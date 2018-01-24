
package com.freightcom.api.carrier.ups.service;

import java.util.HashMap;

public class UPSConstants {

      public static final long CARRIER_ID = 46;
      public static final String ACCESS_KEY = "xxx";
      public static final String USER_NAME = "xxx";
      public static final String PASSWORD = "xxx";
      public static final String ACC_NUMBER = "xxx";
      public static final String UPS_COURIER_SERVICE = "UPS_COURIER_SERVICE";
      public static final String FCCOST_UPS_COURIER_SERVICE = "FCCOST_UPS_COURIER_SERVICE";



      public static HashMap<Long, String> pickupcodeMap = new HashMap<Long, String>();

      static {
            pickupcodeMap.put(4600l, "011"); // UPS Standard
            pickupcodeMap.put(4601l, "002"); //TODO UPS Expedited
            pickupcodeMap.put(4602l, "013"); //UPS Express Saver
            pickupcodeMap.put(4603l, "064"); //UPS Express
            pickupcodeMap.put(4604l, "014"); //UPS Express Early
            pickupcodeMap.put(4605l, "012"); //UPS 3 Day Select
            pickupcodeMap.put(4606l, "008"); //UPS WorldWide Expedited
            pickupcodeMap.put(4607l, "007"); //UPS WorldWide Express
            pickupcodeMap.put(4608l, "054"); //UPS WorldWide Express Plus
            pickupcodeMap.put(4609l, "065"); //UPS Express Saver(USA)

      }

      public static final int CARRIER_ID_EDI = 46;
      public static final String CARRIER_NAME_EDI = "UPS";


}
