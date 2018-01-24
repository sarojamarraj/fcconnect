<?php

require_once(__DIR__ . '/CommonMacros.php');

class AgentSearchTest extends CommonMacros
{
    public static function requests()
    {
        self::clearCaches();

        return
            [
             [ 'GROUP', self::adminLogin() ],

             [ 'create an agent',
               [ 'POST', '/api/user/agent', null, [
                                                   'login' => self::randomUser("a"),
                                                   'email' => self::randomEmail("a"),
                                                   'firstname' => 'Jacqueline1',
                                                   'lastname' => 'Jones1',
                                                   'password' => self::randomEmail("a"),
                                                   'phone' => '416-452-5267'
                                                   ]],
               [ 'agent1-id' => 'id',
                 'agent1-role-id' => 'authorities.0.id' ]
               ],

             [ 'create another agent',
               [ 'POST', '/api/user/agent', null, [
                                                   'login' => self::randomUser("b"),
                                                   'email' => self::randomEmail("b"),
                                                   'firstname' => 'Jonas',
                                                   'lastname' => 'Schmidt',
                                                   'agentName' => 'Farly Sans',
                                                   'parentSalesAgent' => [ 'id' => '${agent1-role-id}' ]
                                                   ]
                 ],
               [ 'agent2-id' => 'id',
                 'agent2-role-id' => 'authorities.0.id' ]
               ],

             [ 'list agents with parent id',
               [ 'GET', '/api/agent', [ 'parentid' => '${agent1-role-id}' ] ],
               [ 'match-count' => '_embedded.agent.@size' ],
               [ 'match-count' => 1 ]
               ],

             [ 'list agents with parent name',
               [ 'GET', '/api/agent', [ 'parentname' => 'Jacqueline' ] ],
               [
                'match-count' => '_embedded.agent.@size',
                'matched-parent-id' => '_embedded.agent.0.parentSalesAgentId'
                 ],
               [ 'match-count' => '>0' ]
               ],

             [ 'list agents with matched id',
               [ 'GET', '/api/agent', [ 'parentId' => '${matched-parent-id}' ] ],
               [
                'match-count' => '_embedded.agent.@size',
                 ],
               [ 'match-count' => '>0' ]
               ],

             [ 'list agents with phone',
               [ 'GET', '/api/agent', [ 'phone' => '452' ] ],
               [ 'match-count' => '_embedded.agent.@size' ],
               [ 'match-count' => '>0' ]
               ]


             ];
    }

    public static function cleanup()
    {
      return
        [
         [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

         [ 'GROUP', self::adminLogin() ],

         ['Delete the agent2 role', [ 'DELETE', '/api/user/role/${agent2-role-id}']],
         [ 'Delete the second agent', [ 'DELETE', '/api/user/${agent2-id}' ]],



         ['Delete the agent1 role', [ 'DELETE', '/api/user/role/${agent1-role-id}']],
         [ 'Delete the first agent', [ 'DELETE', '/api/user/${agent1-id}' ]],


         [ 'logout', [ 'POST', '/api/logout', [], null, null ]]
         ];
    }
}
