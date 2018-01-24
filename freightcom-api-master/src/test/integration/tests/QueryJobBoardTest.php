<?php

require_once(__DIR__ . '/CommonMacros.php');


class QueryJobBoardTest extends CommonMacros
{
    public static function requests()
    {
        return
            [
             self::admin_login_request(),
             self::login_lookup_request('ADMIN'),
             self::$login_set_role_request,

             [ "get job board orders",
               [ 'GET', '/api/job-board', [ 'sort' => 'id,desc', 'size' => 10 ], ]
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
