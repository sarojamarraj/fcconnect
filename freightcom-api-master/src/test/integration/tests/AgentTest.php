<?php

require_once(__DIR__ . '/CommonMacros.php');

class AgentTest extends CommonMacros
{
  public static function requests()
  {
    $password = substr(md5(mt_rand()), 0, 18);

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'list agents',
         [ 'GET', '/api/agent' ],
         [ 'agent-id' => '_embedded.agent.0.id' ],
         [
          [ 'There is an agent', 'agent-id', '@notnull' ]
          ]
         ],

       [ 'list agents ordered by name',
         [ 'GET', '/api/agent', [ 'sort' => 'name', 'page' => '2' ] ],
         [ 'agent-id' => '_embedded.agent.0.id' ],
         [
          [ 'There is an agent', 'agent-id', '@notnull' ]
          ]
         ],

       [ 'look at the agent',
         [ 'GET', '/api/agent/${agent-id}' ],
         [ 'name' => 'name' ],
         [
          [ 'Name is not null', 'name', '@notnull' ]
          ]
         ],

       [ 'list agents with name like halina',
         [ 'GET', '/api/agent', [ 'name' => 'halina' ] ],
         [ 'names' => '_embedded.agent.*.name' ],
         [
          [ 'All names match halina', 'names', function($names) {
              return all(function($name) { return preg_match('/halina/ui', $name); }, $names);
            }
            ]
          ]
         ],

       [ 'list agents with parent id 13145',
         [ 'GET', '/api/agent', [ 'parentid' => 13145 ] ],
         [ 'names' => '_embedded.agent.*.name' ],
         ],

       [ 'get an agent user',
         [ 'GET', '/api/user/1466' ],
         ],

       [ 'Set an agent\'s password',
         [ 'PUT', '/api/user/11047', null, [
                                            'password' => $password,
                                            'firstname' => 'Irfan',
                                            'lastname' => 'Kermally',
                                            'enabled' => 'true',
                                            'email' => 'irfan.kermally@palominosys.com',
                                            ]],
         ],

       // Sort tests

       [ 'list agents sort id',
         [ 'GET', '/api/agent', [ 'sort' => 'id,desc' ] ],
         [ 'agent-ids' => '_embedded.agent.*.id' ]
         ],

       [ 'list agents sort parent name',
         [ 'GET', '/api/agent', [ 'sort' => 'parentSalesAgentName,desc' ] ],
         [ 'parent-names' => '_embedded.agent.*.parentSalesAgentName' ]
         ],

       [ 'list agents sort unpaid commission',
         [ 'GET', '/api/agent', [ 'sort' => 'unpaidCommission,desc' ] ],
         [ 'unpaid-commissions' => '_embedded.agent.*.unpaidCommission' ]
         ],

       [ 'list agents sort paid commission',
         [ 'GET', '/api/agent', [ 'sort' => 'paidCommission,desc' ] ],
         [ 'paid-commmissions' => '_embedded.agent.*.paidCommission' ]
         ],

       [ 'list agents sort commission percent',
         [ 'GET', '/api/agent', [ 'sort' => 'commissionPercent,desc' ] ],
         [ 'commission-percentages' => '_embedded.agent.*.commissionPercent' ]
         ],

       [ 'list agents sort customer count',
         [ 'GET', '/api/agent', [ 'sort' => 'customerCount,desc' ] ],
         [ 'customer-counts' => '_embedded.agent.*.customerCount' ]
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       self::customer_staff_login_request('Irfank', $password),
       self::login_lookup_request('AGENT'),
       self::$login_set_role_request,

       [ 'get subagents',
         [ 'GET', '/api/user' ],
         ],

       [ 'update the first agent role',
         [ 'PUT', '/api/agent/${login-role}', null,
           [
            'commissionPercent' => '35',
            'term' => 'MONTHLY',
            'address' => [
                          'city' => 'Toronto'
                          ]
            ]
           ],
         [
          'updated-term' => 'term',
          'updated-commission' => 'commissionPercent'
          ],
         [
          'updated-term' => 'MONTHLY',
          'updated-commission' => 35
          ]
         ],

       [ 'fetch agent to check update took',
         [ 'GET', '/api/agent/${login-role}' ],
         [
          'updated-term-2' => 'term',
          'updated-commission-2' => 'commissionPercent',
          'updated-city' => 'address.city'
          ],
         [
          'updated-term-2' => 'MONTHLY',
          'updated-commission-2' => 35,
          'updated-city' => 'Toronto'
          ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
