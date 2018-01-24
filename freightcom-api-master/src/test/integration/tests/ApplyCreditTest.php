<?php

require_once(__DIR__ . '/CommonMacros.php');

class ApplyCreditTest extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $password1 = substr(md5(mt_rand()), 0, 18);
    $password2 = substr(md5(mt_rand()), 0, 18);

    $agentLogin = 'agent_' . substr(md5(mt_rand()), 0, 10);
    $agentEmail = 'jac.jones_' . substr(md5(mt_rand()), 0, 10) . '@palominosys.com';


    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'create an agent',
         [ 'POST', '/api/user/agent', null, [
                                             'login' => $agentLogin,
                                             'email' => $agentEmail,
                                             'firstname' => 'Jacqueline',
                                             'lastname' => 'Jones',
                                             'password' => $password2,
                                             ]],
         [ 'agent1-id' => 'id',
           'agent1-role-id' => 'authorities.0.id' ],
         [],
         ],

       [
        'Create a customer',
        [ 'POST', '/api/customer', null,
          self::sampleCustomer($customer_name,
                               [
                                'salesAgent' => [
                                                 'id' => '${agent1-role-id}' ],
                                ]) ],
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
                                                                    'email' => self::generateEmail(),
                                                                    'firstname' => 'Jonas',
                                                                    'lastname' => 'Schmidt',
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [ 'create a credit',
         [ 'POST', '/api/credit', null, [
                                         'amount' => 44.4,
                                         'customerId' => '${customer-id}',
                                         ],],
         [ 'credit-id' => 'id' ],
         [ 'credit-id' => '@notnull' ],
         ],

       [ 'create a second credit',
         [ 'POST', '/api/credit', null, [
                                         'amount' => 144.4,
                                         'customerId' => '${customer-id}',
                                         ],],
         [ 'credit-id-2' => 'id' ],
         [ 'credit-id-2' => '@notnull' ],
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


       [[ 'POST', '/api/logout', [], null, null ]],

       // Log in as admin to create invoice
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered' ],
         [],
         [],
         ],

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customer' => [ 'id' => '${customer-id}' ],
                                                   ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       // Test as customer staff

       self::customer_staff_login_request($customer_user_name, $password1),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list invoices for test customer',
         [ 'GET', '/api/invoice' ],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          ],
         [
          'size' => '1',
          'amount' => 226,
          ],
         ],

       [ 'apply credits',
         [ 'POST', '/api/apply-credit', null, [ 'invoice' => '${invoice-id}' ]],
         [ 'apply-credits-status' => 'status' ],
         [ 'apply-credits-status' => 'SUCCESS' ],
         ],

       [ "Make sure customer can't look at applied credits",
         [ 'GET', '/api/appliedcredit/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-applied-credits' => '_embedded.appliedcredit.@size' ],
         [],
         "Access is denied"
         ],

       [ 'update the invoice',
         [ 'PUT', '/api/invoice/${invoice-id}', null, [
                                                       'status' => 15,
                                                       ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       // Clean up

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ "Let's look at the applied credits",
         [ 'GET', '/api/appliedcredit/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-applied-credits' => '_embedded.appliedcredit.@size' ],
         [ 'num-applied-credits' => '>0' ],
         ],

       [ 'delete the credit',
         [ 'DELETE', '/api/credit/${credit-id}',],
         [ 'status' => '0' ],
         [ 'status' => 'ok' ],
         ],

       [ 'delete the second credit',
         [ 'DELETE', '/api/credit/${credit-id-2}',],
         [ 'status' => '0' ],
         [ 'status' => 'ok' ],
         ],

       [ "Let's look at the charges",
         [ 'GET', '/api/charge/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],

       [
        'Delete the invoice',
        [ 'DELETE', '/api/invoice', null, [ 'invoices' => [ '${invoice-id}' ] ]],
        [],
        []
        ],

       [ "Let's look at the charges - did delete remove them?",
         [ 'GET', '/api/charge/search/findByInvoiceId', [ 'invoice_id' => '${invoice-id}' ]],
         [ 'num-charges' => '_embedded.charge.@size' ],
         [],
         ],


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

       [
        'Delete the first agent',
        [ 'DELETE', '/api/user/${agent1-id}' ],
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
            'customerId' => '${customer-id}'
            ];
  }
}
