<?php

require_once(__DIR__ . '/CommonMacros.php');

class InvoiceAgentTest extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $password1 = substr(md5(mt_rand()), 0, 18);
    $password2 = substr(md5(mt_rand()), 0, 18);
    $password3 = substr(md5(mt_rand()), 0, 18);

    $agentLogin = 'agent_' . substr(md5(mt_rand()), 0, 10);
    $agentEmail = 'jac.jones_' . substr(md5(mt_rand()), 0, 10) . '@palominosys.com';

    $agentLogin2 = 'agent_' . substr(md5(mt_rand()), 0, 10);
    $agentEmail2 = 'farly_' . substr(md5(mt_rand()), 0, 10) . '@palominosys.com';

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
                                             'agentName' => 'Company 1',
                                             'commissionPercent' => '55',
                                             'allowNewOrders' => false,
                                             'viewInvoices' => false,
                                             'term' => 'BIWEEKLY'
                                             ]],
         [ 'agent1-id' => 'id',
           'agent1-role-id' => 'authorities.0.id' ],
         [],
         ],

       [ 'create another agent',
         [ 'POST', '/api/user/agent', null, [
                                             'login' => $agentLogin2,
                                             'email' => $agentEmail2,
                                             'firstname' => 'Michael',
                                             'lastname' => 'Liel',
                                             'password' => $password3,
                                             'agentName' => 'Farly Sans',
                                             ]],
         [ 'agent2-id' => 'id',
           'agent2-role-id' => 'authorities.0.id' ],
         [],
         ],

       [ 'update the first agent role',
         [ 'PUT', '/api/agent/${agent1-role-id}', null,
           [
            'agentName' => 'Company 2',
            'commissionPercent' => '75',
            'allowNewOrders' => true,
            'viewInvoices' => true,
            'term' => 'MONTHLY',
            'parentSalesAgentId' => '${agent2-role-id}'
            ]
           ],
         [
          'updated-agent-name' => 'name',
          'updated-commission-percent' => 'commissionPercent',
          'updated-allow-new-orders' => 'allowNewOrders',
          'updated-view-invoices' => 'viewInvoices',
          'updated-term' => 'term',
          'updated-parent-id-' => 'parentSalesAgentId',
          ],
         [
          'updated-agent-name' => 'Company 2',
          'updated-commission-percent' => 75,
          'updated-allow-new-orders' => true,
          'updated-view-invoices' => true,
          'updated-term' => 'MONTHLY',
          'updated-parent-id-' => '${agent2-role-id}',
          ]
         ],

       [
        'Create a customer',
        [ 'POST', '/api/customer', null,
          self::sampleCustomer($customer_name ,
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
                                                                    'lastname' => 'Gallagher',
                                                                    'firstname' => 'Linda'
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

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customer' => [ 'id' => '${customer-id}' ],
                                                   ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       [ 'list orders for logged in admin for new customer',
         [ 'GET', '/api/submitted-orders', [ 'customerId' => '${customer-id}' ], ],
         [],
         [],
         ],

       [ 'zap the parent of first agent role',
         [ 'PUT', '/api/agent/${agent1-role-id}', null,
           [
            'parentSalesAgentId' => null
            ]
           ],
         [
          'revised-parent-id-' => 'parentSalesAgentId'
          ],
         [
          'revised-parent-id-' => '@null'
          ]
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
          'amount' => 226
          ],
         ],

       [ 'update the invoice',
         [ 'PUT', '/api/invoice/${invoice-id}', null, [
                                                       'status' => 15,
                                                       ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       [ 'list orders for logged in customer staff',
         [ 'GET', '/api/submitted-orders' ],
         [],
         [],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       // Test as agent

       self::customer_staff_login_request($agentLogin, $password2),
       self::login_lookup_request('AGENT'),
       self::$login_set_role_request,

       [ 'list orders for logged in agent',
         [ 'GET', '/api/submitted-orders' ],
         [],
         [],
         ],

       [ 'list orders for logged in agent',
         [ 'GET', '/api/draft-orders' ],
         [],
         [],
         ],

       [ 'list invoices for logged in agent',
         [ 'GET', '/api/invoice' ],
         [
          'size' => '_embedded.invoice.@size',
          'amount' => '_embedded.invoice.0.amount',
          'agent-invoice-id' => '_embedded.invoice.0.id',
          ],
         [
          [ 'exactly one invoice', 'size', '1' ],
          [ 'amount is 226.00', 'amount', 226 ],
          [ 'the invoice id is that of the previously created invoice', 'agent-invoice-id', '${invoice-id}' ]
          ]
         ],

       [ 'get details for the invoice',
         [ 'GET', '/api/invoice/${agent-invoice-id}' ]
         ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // Test as second agent

       self::customer_staff_login_request($agentLogin2, $password3),
       self::login_lookup_request('AGENT'),
       self::$login_set_role_request,

       [ 'list invoices for logged in agent',
         [ 'GET', '/api/invoice' ],
         [
          'size' => '_embedded.invoice.@size',
          ],
         [
          'size' => '@null',
          ],
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Clean up

       ['logout', [ 'POST', '/api/logout', [], null, null ]],

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'delete the credit',
         [ 'DELETE', '/api/credit/${credit-id}',],
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

       [
        'Delete the second agent',
        [ 'DELETE', '/api/user/${agent2-id}' ],
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
