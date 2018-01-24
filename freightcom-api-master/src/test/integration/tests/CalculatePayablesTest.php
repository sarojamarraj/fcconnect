<?php

require_once(__DIR__ . '/CommonMacros.php');

class CalculatePayablesTest extends CommonMacros
{
  public static function requests()
  {
    $user = self::randomUser('hex01');
    $password = self::randomPassword();

    return
      [

       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff($user, $password) ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // As customer staff, create order
       self::customer_staff_login_request($user, $password),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id'
          ],
         [ 'order-id' => '@notNull'
           ],
         ],

       [ 'carrier rates',
         [ 'GET', '/api/carrier_rates/${order-id}' ],
         [ 'rate-id' => '_embedded.orderRateQuote.0.id' ],
         [ 'rate-id' => '@notnull' ],
         ],

       [ 'select the rate',
         [ 'POST', '/api/order/select-rate/${order-id}/${rate-id}', ],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ],
         [],
         [],
         ],

       [[ 'POST', '/api/logout', [], null, null ]],
       [ 'GROUP', self::adminLogin() ],

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered', null, [ 'x' => 'y' ] ],
         [],
         [],
         ],

       [ 'calculate payabales',
         [ 'PUT', '/api/payables/generate' ],
         [ 'id-0' => '0.id' ],
         [ 'id-0' => '@notnull' ]
         ],

       [ 'mark the payable paid',
         [ 'PUT', '/api/payablestatement/${id-0}/paid' ],
         [ 'pay-id' => 'id' ],
         [ 'pay-id' => '${id-0}' ]
         ],

       [ 'list payables', [ 'GET', '/api/payablestatement', [ 'sort' => 'service.name' ]  ]],

       [ 'list payables 2', [ 'GET', '/api/payablestatement', [ 'sort' => 'service.id' ]  ]],

       [ 'get new payable', [ 'GET', '/api/payablestatement/${id-0}' ]],
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

        [
        'Delete the payable',
        [ 'DELETE', '/api/payablestatement/${id-0}' ]
        ],

        [
        'Delete the order',
        [ 'DELETE', '/api/order/${order-id}' ]
        ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-staff-id}' ]
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ]
        ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]]
       ];
  }

  static function getPayload()
  {
    return [
            'shipTo' => [
                         'city' => 'Toronto',
                         'postalCode' => 'M5E 1E5',
                         'country' => 'CA'
                         ],
            'shipFrom' => [
                           'city' => 'Montreal',
                           'postalCode' => 'H1A 0A1',
                           'country' => 'CA'
                           ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}'
            ];
  }
}
