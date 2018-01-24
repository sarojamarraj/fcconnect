<?php

require_once(__DIR__ . '/CommonMacros.php');

class Once extends CommonMacros
{
  public static function requests()
  {
    $password = substr(md5(mt_rand()), 0, 18);

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'create an agent user',
         [ 'POST', '/api/user', null, [
                                       'login' => 'sample.agent',
                                       'email' => 'sample.agent@palominosys.com',
                                       'firstname' => 'Sample',
                                       'lastname' => 'Agent',
                                       'phone' => '416-555-3433',
                                       ]
           ],
         [ 'agent-id' => 'id' ],
         ],

       [ 'make the user an agent',
         [ 'POST', '/api/user/role/agent', null, [
                                                  'userId' => '${agent-id}',
                                                  'parentSalesAgentId' => 12288,
                                                  'cell' => '416-555-6343',
                                       ]
           ],
         ],

       [ 'create an agent user',
         [ 'POST', '/api/user', null, [
                                       'login' => 'another.agent',
                                       'email' => 'another.agent@palominosys.com',
                                       'firstname' => 'Another',
                                       'lastname' => 'Agent',
                                       'phone' => '416-555-6611',
                                       ]
           ],
         [ 'agent-id2' => 'id' ],
         ],

       [ 'make the user an agent',
         [ 'POST', '/api/user/role/agent', null, [
                                                  'userId' => '${agent-id2}',
                                                  'parentSalesAgentId' => 12288,
                                                  'cell' => '416-555-3313',
                                       ]
           ],
         ],

       [ 'list agents',
         [ 'GET', '/api/user', [ 'role' => 'agent', 'page' => 1, 'parentSalesAgentId' => 12288, ],
           ],
         [
          'agent1-size' => '_embedded.user.3.authorities.@size',
          'agent2-size' => '_embedded.user.4.authorities.@size',
          'agent1-role' => '_embedded.user.3.authorities.0.id',
          'agent2-role' => '_embedded.user.4.authorities.0.id',
          ],
         [
          'agent1-size' => 1,
          'agent2-size' => 1,
          ]
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
