<?php

/**
 * Simple test, just list users once
 */

require_once(__DIR__ . '/CommonMacros.php');


class AdminListUsers extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // Get users

       [ 'List users', [ 'GET', '/api/user', [ 'sort' => 'id,desc'], ],
         [
          'size' => '_embedded.user.@size',
          ],
         [
          'size' => '>0',
          ]],
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }

}