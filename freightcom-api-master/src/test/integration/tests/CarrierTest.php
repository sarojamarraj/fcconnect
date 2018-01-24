<?php

require_once(__DIR__ . '/CommonMacros.php');

class CarrierTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],

       // Tests start

       [ 'get carriers',
         [ 'GET', '/api/carrier' ],
         [ 'count' => '_embedded.carrier.@size' ],
         [ 'count' => '>0' ],
         ],

       [ 'create a carrier',
         [ 'POST', '/api/carrier', null, [
                                          'name' => 'foo',
                                          'term' => 'MONTHLY'
                                          ] ],
         [
          'term' => 'term',
          'carrier-id' => 'id'
          ],
         [
          'term' => 'MONTHLY',
          'carrier-id' => '@notnull'
          ]
         ],

       [ 'update the carrier',
         [ 'PUT', '/api/carrier/${carrier-id}', null, [
                                                       'term' => 'BIWEEKLY'
                                                       ] ],
         [
          'new-term' => 'term'
          ],
         [
          'new-term' => 'BIWEEKLY'
          ]
         ],

       [ 'look at the carrier',
         [ 'GET', '/api/carrier/${carrier-id}' ],
         [
          'new-term' => 'term'
          ],
         [
          'new-term' => 'BIWEEKLY'
          ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Logout

       [ 'delete the carrier', [ 'DELETE', '/api/carrier/${carrier-id}' ]],

       ['logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}