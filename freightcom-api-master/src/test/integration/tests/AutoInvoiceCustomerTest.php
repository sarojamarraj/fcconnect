<?php

require_once(__DIR__ . '/CommonMacros.php');

class AutoInvoiceCustomerTest extends CommonMacros
{
  public static function requests()
  {
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
    $invoiceTestEmail = getenv("INVOICE_TEST_EMAIL");

    if (empty($invoiceTestEmail)) {
      $invoiceTestEmail = self::randomEmail();
    }

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name, [ "autoInvoice" => "ON_DELIVERY" ]) ],
        [ 'customer-id' => 'id',
          ],
        [
         'customer-id' => '@notnull',
         ]
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password1,
                                                                    'email' => $invoiceTestEmail,
                                                                    'firstname' => 'Helios',
                                                                    'lastname' => 'Cantalopolis',
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
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

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered', null, [ 'x' => 'y' ] ],
         [],
         [],
         ],
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       // View invoice as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ "Let's look at the customer as staff",
         [ 'GET', '/api/customer/${customer-id}' ],
         [ 'autoCharge' => 'autoCharge' ],
         [ 'autoCharge' => 'IMMEDIATELY' ]

         ],

       [ 'list auto invoice orders',
         [ 'GET', '/api/order', null, [
                                       'autoInvoice' => 'ON_DELIVERY',
                                       'invoiceStatus' => 'unbilled charges',
                                       'sort' => 'id,desc'
                                       ],
           ],
         [ 'auto-id' => '_embedded.customerOrders.0.id' ],
         [ 'auto-id' => '@notnull' ],
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

       [ "Let's look at the customer again as admin",
         [ 'GET', '/api/customer/${customer-id}' ]
         ],

       [
        'Run auto invoice',
        [ 'POST', '/api/run-auto-invoice' ],
        [ 'new-invoice-id' => '0.id' ],
        [ 'new-invoice-id' => '@notnull' ],
        ],


       [
        'Delete the invoice',
        [ 'DELETE', '/api/invoice', null, [ 'invoices' => [ '${new-invoice-id}' ] ] ],
        [],
        []
        ],


       [
        'Really Delete the invoice',
        [ 'DELETE', '/api/invoice/${new-invoice-id}', ],
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
