<?php

require_once(__DIR__ . '/CommonMacros.php');

class CreditFromCardTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer(self::randomUser('customer')) ],
        [ 'customer-id' => 'id',
          ],
        [
         'customer-id' => '@notnull'
         ]
        ],

       [ 'create a credit from a card',
         [ 'POST', '/api/credit/add-from-card', null,
           [
            'payment' => 397.29,
            'ccType' => 'visa',
            'nameOnCard' => 'John Doe',
            'creditCardNumber' => '1234123412341234',
            'expiryMonth' => '02',
            'expiryYear' => '18',
            'cvs' => '321',
            'customerId' => '${customer-id}'
            ]
           ],
         [ 'credit-id' => 'id' ],
         [ 'credit-id' => '@notnull' ],
         ],

       [ 'list credits',
         [ 'GET', '/api/credit', ],
         [ 'size' => '_embedded.credit.@size' ],
         [ 'size' => '>0' ],
         ],

       [ 'list credits for this customer',
         [ 'GET', '/api/credit', [ 'customerid' => '${customer-id}' ] ],
         [ 'size' => '_embedded.credit.@size',
           'matched-customer' => '_embedded.credit.0.customerId', ],
         [ 'size' => '1',
           'matched-customer' => '${customer-id}', ],
         ],


       [ 'get the created credit',
         [ 'GET', '/api/credit/${credit-id}', ],
         [ 'fetched-id' => 'id' ],
         [ 'fetched-id' => '${credit-id}' ],
         ],


       [ 'get the total credit for the customer',
         [ 'GET', '/api/customer/total-credit/${customer-id}', ],
         [ 'fetched-amount' => 'totalCredit' ],
         [ 'fetched-amount' => '397.29' ],
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'delete the credit',
         [ 'DELETE', '/api/credit/${credit-id}'],
         ],


       [ 'delete the customer',
         [ 'DELETE', '/api/customer/${customer-id}' ],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]]

       ];
  }
}
