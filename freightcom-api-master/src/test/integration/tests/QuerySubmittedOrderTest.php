<?php

require_once(__DIR__ . '/CommonMacros.php');


class QuerySubmittedOrderTest extends CommonMacros
{
    public static function requests()
    {
        return
            [
             self::admin_login_request(),
             self::login_lookup_request('ADMIN'),
             self::$login_set_role_request,

             [ "get delivered orders",
               [ 'GET', '/api/submitted-orders', [ 'sort' => 'id,desc', 'size' => 10 ], ]
               ]
             ];
    }

    public static function cleanup()
    {
        return
            [
             ['logout', [ 'POST', '/api/logout', [], null, null ]],
             ];
    }
}
