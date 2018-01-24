<?php

require_once(__DIR__ . '/CommonMacros.php');

class AutoPayablesTest extends CommonMacros
{
    public static function requests()
    {
        return
            [

             [ 'GROUP', self::adminLogin() ],


             [
              'Run all auto payables crons',
              [ 'POST', '/api/payables/run-all' ],
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
