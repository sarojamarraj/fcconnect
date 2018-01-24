<?php

require_once(__DIR__ . '/CommonMacros.php');

/**
 * Assumes user sdaudlin with known password
 */
class AddressBookTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::customer_login_request(),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,

       [ 'create an entry',
         [ 'POST', '/api/address_book', null, [ 'city' => 'Markham' ]],
         [ 'address-book-id' => 'id' ],
         [],
         ],

       [ 'Query addressbook for markh',
         [ 'GET', '/api/address_book', [ 'city' => 'markh' ], null, null ],
         [
          'cities' => '_embedded.addressBooks.*.city',
          ],
         [
          'cities' => function($cities) {
            return all(function($cityName) { return preg_match("/markh/ui", $cityName); },
                       $cities);
          }
          ]
         ],

       [ 'Query addressbook for a',
         [ 'GET', '/api/address_book', [ 'q' => 'a', 'page' => 0, 'size' => 10, 'sort' => 'consigneeName,asc' ], null, null ],
         [
          'cities' => '_embedded.addressBooks.*.city',
          ],
         [
          'cities' => '@notnull'
          ]
         ],

       [ 'delete the entry',
         [ 'DELETE', '/api/address_book/${address-book-id}'],
         [  ],
         [],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}