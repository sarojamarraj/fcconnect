<?php

require_once(__DIR__ . '/CommonMacros.php');

class AutoInvoiceAllTest extends CommonMacros
{
    public static function requests()
    {
        return
            [

             [ 'GROUP', self::adminLogin() ],


             [
              'Run auto invoice',
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
