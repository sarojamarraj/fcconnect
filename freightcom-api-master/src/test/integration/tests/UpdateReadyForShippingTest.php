<?php

require_once(__DIR__ . '/CommonMacros.php');

class UpdateReadyForShippingTest extends CommonMacros
{
    public static function requests()
    {
        return
            [

             [ 'GROUP', self::adminLogin() ],


             [
              'Run update ready for shipping',
              [ 'POST', '/api/run-auto-invoice' ],
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
