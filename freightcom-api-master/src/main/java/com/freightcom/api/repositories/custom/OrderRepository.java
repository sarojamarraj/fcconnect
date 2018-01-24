/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/
package com.freightcom.api.repositories.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.freightcom.api.model.CustomerOrder;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface OrderRepository extends PagingAndSortingRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder>
{

    @SuppressWarnings("unchecked")
    @Override
    public CustomerOrder save(CustomerOrder order);

    Page<CustomerOrder> findByCustomerId(@Param("id") Long id, Pageable pageable);

    // Histogram by month

    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%Y-%m') as label  FROM CustomerOrder C where DATE_FORMAT(shipDate, '%Y-%m') >= DATE_FORMAT(?1, '%Y-%m') and DATE_FORMAT(shipDate, '%Y-%m') <= DATE_FORMAT(?2, '%Y-%m') group by DATE_FORMAT(shipDate, '%Y-%m') order by DATE_FORMAT(C.shipDate, '%Y-%m')")
    List<Map<String,String>> monthHistogram(Date from, Date to);


    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%Y-%m') as label  FROM CustomerOrder C where customerId=?3 and DATE_FORMAT(shipDate, '%Y-%m') >= DATE_FORMAT(?1, '%Y-%m') and DATE_FORMAT(shipDate, '%Y-%m') <= DATE_FORMAT(?2, '%Y-%m') group by DATE_FORMAT(shipDate, '%Y-%m') order by DATE_FORMAT(C.shipDate, '%Y-%m')")
    List<Map<String,String>> monthHistogramCustomer(Date from, Date to, Long customerId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%Y-%m') as label  FROM CustomerOrder C inner join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%Y-%m') >= DATE_FORMAT(?1, '%Y-%m') and DATE_FORMAT(C.shipDate, '%Y-%m') <= DATE_FORMAT(?2, '%Y-%m') group by DATE_FORMAT(C.shipDate, '%Y-%m') order by DATE_FORMAT(C.shipDate, '%Y-%m') ")
    List<Map<String,String>> monthHistogramAgent(Date from, Date to, Long agentId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%Y-%m') as label  FROM CustomerOrder C inner join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%Y-%m') >= DATE_FORMAT(?1, '%Y-%m') and DATE_FORMAT(C.shipDate, '%Y-%m') <= DATE_FORMAT(?2, '%Y-%m') and U.id=?4 group by DATE_FORMAT(C.shipDate, '%Y-%m') order by DATE_FORMAT(C.shipDate, '%Y-%m') ")
    List<Map<String,String>> monthHistogramAgentCustomer(Date from, Date to, Long agentId, Long customerId);


    // Histogram by week

    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%X-%V') as label  FROM CustomerOrder C where DATE_FORMAT(shipDate, '%X-%V') >= DATE_FORMAT(?1, '%X-%V') and DATE_FORMAT(shipDate, '%X-%V') <= DATE_FORMAT(?2, '%X-%V') group by DATE_FORMAT(shipDate, '%X-%V') order by DATE_FORMAT(C.shipDate, '%X-%V')")
    List<Map<String,String>> weekHistogram(Date from, Date to);


    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%X-%V') as label  FROM CustomerOrder C where customerId=?3 and DATE_FORMAT(shipDate, '%X-%V') >= DATE_FORMAT(?1, '%X-%V') and DATE_FORMAT(shipDate, '%X-%V') <= DATE_FORMAT(?2, '%X-%V') group by DATE_FORMAT(shipDate, '%X-%V') order by DATE_FORMAT(C.shipDate, '%X-%V')")
    List<Map<String,String>> weekHistogramCustomer(Date from, Date to, Long customerId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%X-%V') as label  FROM CustomerOrder C left join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%X-%V') >= DATE_FORMAT(?1, '%X-%V') and DATE_FORMAT(C.shipDate, '%X-%V') <= DATE_FORMAT(?2, '%X-%V') group by DATE_FORMAT(C.shipDate, '%X-%V') order by DATE_FORMAT(C.shipDate, '%X-%V') ")
    List<Map<String,String>> weekHistogramAgent(Date from, Date to, Long agentId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%X-%V') as label  FROM CustomerOrder C inner join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%X-%V') >= DATE_FORMAT(?1, '%X-%V') and DATE_FORMAT(C.shipDate, '%X-%V') <= DATE_FORMAT(?2, '%X-%V') and U.id=?4 group by DATE_FORMAT(C.shipDate, '%X-%V') order by DATE_FORMAT(C.shipDate, '%X-%V') ")
    List<Map<String,String>> weekHistogramAgentCustomer(Date from, Date to, Long agentId, Long customerId);


    // Histogram by day

    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%Y-%m-%d') as label  FROM CustomerOrder C where DATE_FORMAT(shipDate, '%Y-%m-%d') >= DATE_FORMAT(?1, '%Y-%m-%d') and DATE_FORMAT(shipDate, '%Y-%m-%d') <= DATE_FORMAT(?2, '%Y-%m-%d') group by DATE_FORMAT(shipDate, '%Y-%m-%d') order by DATE_FORMAT(C.shipDate, '%Y-%m-%d')")
    List<Map<String,String>> dayHistogram(Date from, Date to);


    @Query("SELECT count(*) as count, DATE_FORMAT(shipDate, '%Y-%m-%d') as label  FROM CustomerOrder C where customerId=?3 and DATE_FORMAT(shipDate, '%Y-%m-%d') >= DATE_FORMAT(?1, '%Y-%m-%d') and DATE_FORMAT(shipDate, '%Y-%m-%d') <= DATE_FORMAT(?2, '%Y-%m-%d') group by DATE_FORMAT(shipDate, '%Y-%m-%d') order by DATE_FORMAT(C.shipDate, '%Y-%m-%d')")
    List<Map<String,String>> dayHistogramCustomer(Date from, Date to, Long customerId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%Y-%m-%d') as label  FROM CustomerOrder C left join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%Y-%m-%d') >= DATE_FORMAT(?1, '%Y-%m-%d') and DATE_FORMAT(C.shipDate, '%Y-%m-%d') <= DATE_FORMAT(?2, '%Y-%m-%d') group by DATE_FORMAT(C.shipDate, '%Y-%m-%d') order by DATE_FORMAT(C.shipDate, '%Y-%m-%d') ")
    List<Map<String,String>> dayHistogramAgent(Date from, Date to, Long agentId);

    @Query("SELECT count(*) as count, DATE_FORMAT(C.shipDate, '%Y-%m-%d') as label  FROM CustomerOrder C inner join C.customer U where U.salesAgent.id = ?3 and DATE_FORMAT(C.shipDate, '%Y-%m-%d') >= DATE_FORMAT(?1, '%Y-%m-%d') and DATE_FORMAT(C.shipDate, '%Y-%m-%d') <= DATE_FORMAT(?2, '%Y-%m-%d') and U.id=?4 group by DATE_FORMAT(C.shipDate, '%Y-%m-%d') order by DATE_FORMAT(C.shipDate, '%Y-%m-%d') ")
    List<Map<String,String>> dayHistogramAgentCustomer(Date from, Date to, Long agentId, Long customerId);


    @Query("SELECT C.customWorkOrder as workorder, C.packageTypeName as type, count(*) as count from CustomerOrder C where customerId=?3 and DATE_FORMAT(shipDate, '%Y-%m-%d') >= DATE_FORMAT(?1, '%Y-%m-%d') and DATE_FORMAT(shipDate, '%Y-%m-%d') <= DATE_FORMAT(?2, '%Y-%m-%d') group by IFNULL(C.customWorkOrder,0), C.packageTypeName")
    List<Map<String,Object>> statsPerType(Date from, Date to, Long customerId);


    @Query("SELECT  C.customWorkOrder as workorder, C.packageTypeName as type, count(*) as count from CustomerOrder C group by IFNULL(C.customWorkOrder,0), C.packageTypeName")
    List<Map<String,Object>> statsPerType();

    @Query("SELECT C.customWorkOrder as workorder, C.packageTypeName as type, count(*) as count from CustomerOrder C where customerId=?1 group by IFNULL(C.customWorkOrder,0), C.packageTypeName")
    List<Map<String,Object>> statsPerType(Long customerId);
}
