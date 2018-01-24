<?php

require_once(__DIR__ . '/CommonMacros.php');

class ThrowErrorTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'non-existent route',
         [ 'POST', '/api/test/error' ],
          null,
         null,
         [
          'key' => 'message',
          'message' => "Internal Error"
          ]
         ],

       [ 'trigger an explicit error with missing parameter',
         [ 'GET', '/api/test/error'  ],
         null,
         null,
         [
          'key' => 'message',
          'message' => "Required String parameter 'message' is not present"
          ]],

       [ 'trigger an explicit error with message',
         [ 'GET', '/api/test/error', [ 'message' => 'a test message' ] ],
         null,
         null,
         [
          'key' => 'message',
          'message' => "Internal Error"
          ]
         ],

       [ 'trigger an explicit exception with message',
         [ 'GET', '/api/test/exception', [ 'message' => 'a test message' ] ],
         null,
         null,
         [
          'key' => 'message',
          'message' => "a test message"
          ]
         ],

       [ 'trigger an explicit throwable with message',
         [ 'GET', '/api/test/throwable', [ 'message' => 'a test message' ] ],
         null,
         null,
         [
          'key' => 'message',
          'message' => "Failed to invoke handler method\nHandlerMethod details: \nController [com.freightcom.api.controllers.AdminController]\nMethod [public java.lang.Object com.freightcom.api.controllers.AdminController.xthrowable(java.lang.String) throws java.lang.Throwable]\nResolved arguments: \n[0] [type=java.lang.String] [value=a test message]\n"
          ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }


}
