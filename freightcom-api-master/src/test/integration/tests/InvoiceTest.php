<?php

require_once(__DIR__ . '/CommonMacros.php');

class InvoiceTest extends CommonMacros
{
  public static $referenceNumber;
  public static $referenceNumber2;
  public static $trackingNumber;

  public static function requests()
  {
    self::$referenceNumber = 'reference-code: ' . date('YmdHis');
    self::$referenceNumber2 = '  ,  reference-code2: ' . date('YmdHis');
    self::$trackingNumber = '111' . date('YmdHis');

    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $password1 = substr(md5(mt_rand()), 0, 18);

    $today = new DateTime();
    $date = new DateTime();
    $prevDay = $date->format('Y-m-d');
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify('+1 day');
    $orderDate = (new DateTime());
    $orderDate->setTimeZone(new DateTimeZone('UTC'));
    $orderDate->modify('+1 day');
    $dueDate = $date->format('Y-m-d');
    $date->modify('+1 day');
    $nextDay = $date->format('Y-m-d');

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id',
          ],
        [
         'customer-id' => '@notnull',
         ]
        ],

       [ 'set auto invoice to off',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'autoInvoice' => 'MONTHLY',
            'autoCharge' => 'NEVER'
            ]
           ],
         [],
         []
         ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password1,
                                                                    'email' => self::generateEmail(),
                                                                    'firstname' => 'Stanley',
                                                                    'lastname' => 'Windsor'
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [ 'list invoices',
         [ 'GET', '/api/invoice', [ 'size' => 10, 'page' => 0, ]],
         ],


       [ 'list invoices customer',
         [ 'GET', '/api/invoice', [ 'customerId' => 6571,
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         ],

       [ 'list invoices due date',
         [ 'GET', '/api/invoice', [ 'duedate' => '2016-01',
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         [ 'due-dates' => '_embedded.invoice.*.dueDate' ],
         ],

       // Logout

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

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customerId' => '${customer-id}',
                                                   'dueAt' => $dueDate,
                                                   ],
           ],
         [  ],
         [  ],
         "Access is denied",
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

       // Log in as admin to create the invoice
       [[ 'POST', '/api/logout', [], null, null ]],

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'List orders by color code after booked',
         [ 'GET', '/api/order', ['sort' => 'colorCode,desc', 'size' => 4],]
         ],

       [ 'the order is in transit',
         [ 'POST', '/api/order/${order-id}/mark-as-in-transit', null, [ 'comment' => 'thus we are in transit sic gloria mundi' ] ],
         [],
         [],
         ],

       [ 'List orders by color code after in transit',
         [ 'GET', '/api/order', ['sort' => 'colorCode,desc', 'size' => 4],]
         ],

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered', [ 'comment' => 'delivery comment test' ] ],
         [],
         [],
         ],

       [ 'List orders by color code after deliver',
         [ 'GET', '/api/order', ['sort' => 'colorCode,desc', 'size' => 4],]
         ],

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customer' => [ 'id' => '${customer-id}' ],
                                                   'dueAt' => $dueDate,
                                                   ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],


       [ 'list invoices tomorrow',
         [ 'GET', '/api/invoice', [ 'dueAt' => 'tomorrow',
                                    'customerId' => '${customer-id}',
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          ],
         [
          'size' => '1',
          'amount' => '226',
          ],
         ],


       [ 'list invoices yesterday to tomorrow',
         [ 'GET', '/api/invoice', [ 'dueAt' => 'custom',
                                    'dueAtFrom' => $prevDay,
                                    'dueAtTo' => $nextDay,
                                    'customerId' => '${customer-id}',
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          ],
         [
          'size' => '1',
          'amount' => '226',
          ],
         ],


       [ 'list invoices this year',
         [ 'GET', '/api/invoice', [ 'dueAt' => 'custom',
                                    'dueAtFrom' => $date->format('Y'),
                                    'dueAtTo' => $date->format('Y'),
                                    'customerId' => '${customer-id}',
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount'
          ],
         [
          'size' => '1',
          'amount' => '226',
          ],
         ],


       [ 'list invoices month of order',
         [ 'GET', '/api/invoice', [ 'dueAt' => 'custom',
                                    'dueAtFrom' => $orderDate->format('Y-m'),
                                    'dueAtTo' => $orderDate->format('Y-m'),
                                    'customerId' => '${customer-id}',
                                    'size' => 10,
                                    'sort' => 'id,desc' ]],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          ],
         [
          'size' => '1',
          'amount' => '226',
          ],
         ],


       [ 'get order by reference num',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 'referencenum' => self::$referenceNumber,
                                 'statusId' => '1,3,16'
                                 ] ],
         [ 'reference-code-matches' => '_embedded.customerOrders.@size' ],
         [ 'reference-code-matches' => 1 ],
         ],


       [ 'get order  by tracking num',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 'trackingnumber' => self::$trackingNumber,
                                 ] ],
         [ 'tracking-number-matches' => '_embedded.customerOrders.@size' ],
         [ 'tracking-number-matches' => 1 ],
         ],


       [ 'get order  by both reference and tracking num',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 'referencenum' => self::$referenceNumber . self::$referenceNumber2,
                                 'trackingnumber' => self::$trackingNumber
                                 ] ],
         [ 'both-code-matches' => '_embedded.customerOrders.@size' ],
         [ 'both-code-matches' => 1  ],
         ],

       [ 'List invoices reference code',
         [ 'GET', '/api/invoice', [
                                   'referencecode' => self::$referenceNumber . self::$referenceNumber2,
                                   'sort' => 'id,DESC',
                                   'page' => 0,
                                   'size' => 10
                                   ],

           ],
         [
          'reference-invoice-codes-size' => '_embedded.invoice.@size',
          ],
         [ 'reference-invoice-codes-size' => 1 ]
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       // View invoice as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list invoices for test customer 1',
         [ 'GET', '/api/invoice', [ 'size' => 10 ] ],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          ],
         [
          'size' => '1',
          'amount' => '226',
          ],
         ],

       [ 'view an invoice',
         [ 'POST', '/api/invoice/view/${invoice-id}',],
         [ 'viewed-at' => 'viewedAt' ],
         [ 'viewed-at' => '@notnull' ],
         ],

       [ 'update the invoice',
         [ 'PUT', '/api/invoice/${invoice-id}', null, [
                                                       'status' => 15,
                                                       ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Clean up

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Delete the invoice',
        [ 'DELETE', '/api/invoice', null, [
                                           'invoices' => [ '${invoice-id}', ],
                                           ]],
        [],
        []
        ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Customer should not see cancelled invoice

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list invoices for test customer 2',
         [ 'GET', '/api/invoice', [ 'size' => 10 ] ],
         [
          'size' => '_embedded.invoice.@size',
          ],
         [
          'size' => '@null',
          ],
         ],

       [ 'view an invoice',
         [ 'POST', '/api/invoice/view/${invoice-id}',],
         [  ],
         [  ],
         "Access is denied"
         ],

       [ 'update the invoice',
         [ 'PUT', '/api/invoice/${invoice-id}', null, [
                                                       'status' => 15,
                                                       ],
           ],
         [  ],
         [  ],
         "Access is denied"
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Clean up

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,


       [
        'Really Delete the invoice',
        [ 'DELETE', '/api/invoice/${invoice-id}', ],
        [],
        []
        ],

       [
        'Delete the order',
        [ 'DELETE', '/api/order/${order-id}' ],
        [],
        []
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
            'shipTo' => [
                         'city' => 'Toronto',
                         'contactName' => "James O'Reilly",
                         'email' => "bryan@palominosys.com",
                         'emailNotification' => true
                         ],
            'shipFrom' => [
                         'city' => 'Toronto',
                         'contactName' => "Harold Orris",
                         'email' => "bryan@palominosys.com",
                         'emailNotification' => true
                         ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "signatureRequired" => "Yes",
            'referenceCode' => self::$referenceNumber,
            'masterTrackingNum' => self::$trackingNumber,
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
