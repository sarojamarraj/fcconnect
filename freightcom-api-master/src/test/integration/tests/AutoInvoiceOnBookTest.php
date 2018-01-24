<?php

require_once(__DIR__ . '/CommonMacros.php');

class AutoInvoiceOnBookTest extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $password1 = substr(md5(mt_rand()), 0, 18);

    return
      [

       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff($customer_user_name, $password1) ],

       [ 'set auto invoice to on book',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'autoInvoice' => 'ON_BOOKING',
            'autoCharge' => 'NEVER'
            ]
           ],
         [],
         []

         ],

       [[ 'POST', '/api/logout', [], null, null ]],

       // Test as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'select a service',
         [ 'GET', '/api/service', [ 'size' => 2 ]],
         [ 'service-id' => '_embedded.services.1.id' ],
         [ 'service-id' => '@notnull' ],
         ],

       [ 'select a quote',
         [ 'PUT', '/api/order/test-rate/${service-id}/${order-id}', null, [ 'base' => 200 ]],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ],
         [],
         [],
         ],

       [ 'view the order to check unbilled charges',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'unbilled-charges' => 'unbilledCharges',
          'charge-subtotals' => 'charges.*.subTotal',
          'charge-totals' => 'charges.*.total',
          'charge-statuses' => 'charges.*.status',
          'charge-currencies' => 'charges.*.currency',
          'charge-descriptions' => 'charges.*.description',
          'charges' => 'charges'
          ],
         [
          [ 'Unbilled charges > 0', 'unbilled-charges', '0' ]
          ],
         ],

       [[ 'POST', '/api/logout', [], null, null ]],

       // Log in as admin to create the invoice
       [ 'GROUP', self::adminLogin() ],

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered', null, [ 'x' => 'y' ] ],
         [],
         [],
         ],

       [ 'create a credit',
         [ 'POST', '/api/credit', null, [
                                         'amount' => 50,
                                         'customerId' => '${customer-id}',
                                         ],],
         [ 'credit-id' => 'id' ],
         [ 'credit-id' => '@notnull' ],
         ],

       [ 'look at the order with charges',
         [ 'GET', '/api/order/${order-id}' ],
         [],
         [],
         ],

       [ "List invoices for order",
         [ 'GET', '/api/order/${order-id}/invoices' ],
         [
          'invoice-id' => '_embedded.invoice.0.id',
           ],
         [
           'invoice-id' => '@notnull' ]
         ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // View invoice as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list unpaid invoices for test customer before payment',
         [ 'GET', '/api/invoice', [ 'size' => 10, 'paymentStatus' => 0 ] ],
         [
          'size' => '_embedded.invoice.@size',
          'invoice-id' => '_embedded.invoice.0.id'
          ],
         [
          'size' => '1',
          'invoice-id' => '@notnull'
          ],
         ],

       [ 'view an invoice',
         [ 'POST', '/api/invoice/view/${invoice-id}',],
         [ 'viewed-at' => 'viewedAt' ],
         [ 'viewed-at' => '@notnull' ],
         ],

       [ 'partially pay the invoice',
         [ 'POST', '/api/invoice/pay', null, [
                                              'invoices' => [ '${invoice-id}' ],
                                              'payment' => 75,
                                              'reference' => 'foobar'
                                              ],
           ],
         [ 'paid-invoice-id' => '0.id',
           'paid-amount' => '0.paidAmount',
           'invoice-amount-remaining' => '0.amountRemaining',
           'invoice-credited-amount' => '0.creditedAmount' ],
         [ 'paid-invoice-id' => '${invoice-id}',
           'paid-amount' => 75,
           'invoice-credited-amount' => 50,
           'invoice-amount-remaining' => 101,
           ],
         ],

       [ 'partially pay the invoice',
         [ 'POST', '/api/invoice/pay', null, [
                                              'invoices' => [ '${invoice-id}' ],
                                              'payment' => 75,
                                              'creditCardNumber' => '434343434343',
                                              'expiryYear' => '18'
                                              ],
           ],
         [ 'paid-invoice-id' => '0.id',
           'paid-amount' => '0.paidAmount' ],
         [ 'paid-invoice-id' => '${invoice-id}',
           'paid-amount' => 150 ],
         ],

       [ 'partially pay the invoice',
         [ 'POST', '/api/invoice/pay', null, [
                                              'invoices' => [ '${invoice-id}' ],
                                              'payment' => 5,
                                              'creditCardNumber' => '434343434343',
                                              'nameOnCard' => 'Company X'
                                              ],
           ],
         [ 'paid-invoice-id' => '0.id',
           'paid-amount' => '0.paidAmount' ],
         [ 'paid-invoice-id' => '${invoice-id}',
           'paid-amount' => 155 ],
         ],

       [ 'finish paying the invoice',
         [ 'POST', '/api/invoice/pay', null, [
                                              'invoices' => [ '${invoice-id}' ],
                                              'payment' => 31,
                                              'creditCardNumber' => '373373737337',
                                              'nameOnCard' => 'Company Z'
                                              ],
           ],
         [ 'paid-invoice-id' => '0.id',
           'paid-amount' => '0.paidAmount' ],
         [ 'paid-invoice-id' => '${invoice-id}',
           'paid-amount' => 176 ],
         ],

       [ 'view the invoice again',
         [ 'POST', '/api/invoice/view/${invoice-id}',],
         [ 'viewed-at' => 'viewedAt',
           'payment-status' => 'paymentStatus',
           'costs' => 'groupedCharges.*.cost' ],
         [ 'viewed-at' => '@notnull',
           'payment-status' => 1,
           'costs' => function($costs) { return all(function($cost) {
                 return $cost === null;
                 }, $costs); }
           ],
         ],

       [ 'list unpaid invoices for test customer after',
         [ 'GET', '/api/invoice', [ 'size' => 10, 'paymentStatus' => 0 ] ],
         [
          'size' => '_embedded.invoice.@size'
          ],
         [
          'size' => '@null',
          ],
         ],

       [ 'list paid invoices for test customer',
         [ 'GET', '/api/invoice', [ 'size' => 10, 'paymentStatus' => 1 ] ],
         [
          'size' => '_embedded.invoice.@size'
          ],
         [
          'size' => '1'
          ],
         ],

       [ 'let\'s see if my credit cards are updated',
         [ 'GET', '/api/customer/${customer-id}' ]
         ],
       ];
  }

  public static function cleanup()
  {
    return
      [

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],


       [ 'CONDITION', [ 'invoice-id' => '@null' ], 'SKIP_DELETE_INVOICE' ],

       [ "Let's look at the charges",
         [ 'GET', '/api/charge/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],

       [
        'Delete the invoice',
        [ 'DELETE', '/api/invoice', null, [
                                           'invoices' => [ '${invoice-id}', ],
                                           ]],
        [],
        []
        ],


       [
        'Really Delete the invoice',
        [ 'DELETE', '/api/invoice/${invoice-id}', ],
        [],
        []
        ],

       [ 'LABEL', 'SKIP_DELETE_INVOICE' ],

       [ "Let's look at the charges - did delete remove them?",
         [ 'GET', '/api/charge/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],

       [ "Let's look at the charges by order",
         [ 'GET', '/api/charge/search/findByOrderId', [ 'order_id' => '${order-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],


       [ 'CONDITION', [ 'order-id' => '@null' ], 'SKIP_DELETE_ORDER' ],

       [
        'Delete the order',
        [ 'DELETE', '/api/order/${order-id}' ],
        [],
        []
        ],

       [ "Let's look at the charges by order, did they get deleted?",
         [ 'GET', '/api/charge/search/findByOrderId', [ 'order_id' => '${order-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],

       [ 'LABEL', 'SKIP_DELETE_ORDER' ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-staff-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ],
        [],
        []
        ],

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }

  static function getPayload()
  {
    return [
            'shipTo' => [
                         'city' => 'Toronto',
                         'saveToAddressBook' => true,
                         'company' => 'Little Big'
                         ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "signatureRequired" => "Yes",
            "accessorialServices" => [
                                      "Saturday Pickup" => true,
                                      "Residential Delivery" => true,
                                      "Saturday Delivery" => true,
                                      "Hold For Pickup" => true,
                                      "Residential Pickup" => true
                                      ],
            ];
  }
}
