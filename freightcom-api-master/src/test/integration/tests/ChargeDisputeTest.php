<?php

require_once(__DIR__ . '/CommonMacros.php');

class ChargeDisputeTest extends CommonMacros
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

       [ 'Turn off auto invoice',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'autoInvoice' => 'MONTHLY',
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

       [ 'show the status messages for the order after booking',
         [ 'GET', '/api/order/${order-id}/status-messages' ],
         [  ],
         [  ],
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
          'charges' => 'charges',
          'dispute-charge' => 'charges.0.id'
          ],
         [
          [ 'Unbilled charges > 0', 'unbilled-charges', '>0' ],
          [ 'Charge total = unbilled charges', 'charge-subtotals', function($totals, $map) {
                                                                                            return reduce(function($chargeSubtotal, $total) {
                                                                                                return $total + $chargeSubtotal;
                                                                                              }, $totals, 0) == $map['unbilled-charges'];
                                                                                            }],
          [ 'No null charge status', 'charge-statuses', function($statuses) {
                                                                               return all(function($status) { return $status !== null; }, $statuses);
                                                                             }],
          [ 'No null currencies', 'charge-currencies', function($currencies) {
                                                                                return all(function($currency) { return $currency !== null; }, $currencies);
                                                                              }],
          [ 'No null description', 'charge-descriptions', function($descriptions) {
                                                                                     return all(function($description) { return $description !== null; }, $descriptions);
                                                                                   }],
          ['Charge total = sub total + totalTax', 'charges', function($charges) {
                                                                                 return all(function($charge) {
                                                                                     return $charge['total'] == $charge['subTotal'] + $charge['totalTax'];
                                                                                   }, $charges);
                                                                                 }],
          ['Charge subtotal = charge * quantity', 'charges', function($charges) {
                                                                                 return all(function($charge) {
                                                                                     return $charge['subTotal'] == $charge['charge'] * $charge['quantity'];
                                                                                   }, $charges);
                                                                                 }]
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

       [ 'look for order with unbilled charges',
         [ 'GET', '/api/order', [
                                 'invoiceStatus' => 'unbilled charges',
                                 'customerId' => '${customer-id}',
                                 ]],
         [ 'invoice-status' => '_embedded.customerOrders.0.invoiceStatus', ],
         [ 'invoice-status' => 'Unbilled charges' ],
         ],

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customerId' => '${customer-id}'
                                                   ],
           ],
         [
          'invoice-id' => 'id',
          'due-date' => 'dueDate'
          ],
         [
          'invoice-id' => '@notnull',
          'due-date' => '@notnull'
          ],
         ],

       [ "List invoices for order",
         [ 'GET', '/api/order/${order-id}/invoices' ],
         [ 'list-due-date' => '_embedded.invoice.0.dueDate' ],
         [ 'list-due-date' => '${due-date}' ]
         ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // View invoice as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list unpaid invoices for test customer',
         [ 'GET', '/api/invoice', [ 'size' => 10, 'paymentStatus' => 0 ] ],
         [
          'size' => '_embedded.invoice.@size'
          ],
         [
          'size' => '1'
          ],
         ],

       [ 'view an invoice',
         [ 'POST', '/api/invoice/view/${invoice-id}',],
         [ 'viewed-at' => 'viewedAt' ],
         [ 'viewed-at' => '@notnull' ],
         ],

       [ 'dispute a charge',
         [ 'POST', '/api/order/charge/${dispute-charge}/dispute', null, [ 'comment' => "too much for my taste" ] ],
         [  ],
         [  ],
         ],

       [ 'show disputed orders',
         [ 'GET', '/api/orders-with-disputes' ],
         [  'disputed-order-count' => '_embedded.customerOrders.@size' ],
         [  'disputed-order-count' => 1 ],
         ],

       [ 'show the order after disputing',
         [ 'GET', '/api/order/${order-id}' ],
         [ 'confirm-dispute-charge' => 'charges.0.id' ],
         [ 'confirm-dispute-charge' => '${dispute-charge}' ],
         ],

       [ 'show the status messages for the order after disputing',
         [ 'GET', '/api/order/${order-id}/status-messages' ],
         [  ],
         [  ],
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
                                              'payment' => 75,
                                              'creditCardNumber' => '434343434343',
                                              'nameOnCard' => 'Company X'
                                              ],
           ],
         [ 'paid-invoice-id' => '0.id',
           'paid-amount' => '0.paidAmount' ],
         [ 'paid-invoice-id' => '${invoice-id}',
           'paid-amount' => 176 ],
         ],

       [ 'finish paying the invoice',
         [ 'POST', '/api/invoice/pay', null, [
                                              'invoices' => [ '${invoice-id}' ],
                                              'payment' => 5,
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

       [ 'list unpaid invoices for test customer',
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

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       // Log in as admin to resolve dispute
       [ 'GROUP', self::adminLogin() ],

       [
        'respond to dispute 1',
        [ 'POST', '/api/order/${order-id}/respond-to-dispute', null, [ 'comment' => 'I laugh in your face' ] ],
        ],

       [ 'show the status messages for the order after disputing',
         [ 'GET', '/api/order/${order-id}/status-messages' ],
         [  ],
         [  ],
         ],

       [
        'respond to dispute 2',
        [ 'POST', '/api/order/${order-id}/respond-to-dispute', null, [ 'comment' => 'Okay, you win', '' => true ] ],
        ],

       [ 'show the status messages for the order after disputing',
         [ 'GET', '/api/order/${order-id}/status-messages' ],
         [  ],
         [  ],
         ],


       ];
  }

  public static function cleanup()
  {
    return
      [

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

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
            'shipTo' => [ 'city' => 'Toronto' ],
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
