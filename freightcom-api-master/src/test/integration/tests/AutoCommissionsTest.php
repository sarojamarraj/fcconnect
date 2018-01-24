<?php

require_once(__DIR__ . '/CommonMacros.php');

class AutoCommissionsTest extends CommonMacros
{
    public static function requests()
    {
        return
            [

             [ 'GROUP', self::adminLogin() ],


             [
              'Run auto invoice',
              [ 'POST', '/api/commissionstatement/run-all' ],
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
