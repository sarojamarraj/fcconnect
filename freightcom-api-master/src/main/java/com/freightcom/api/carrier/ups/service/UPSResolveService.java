package com.freightcom.api.carrier.ups.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freightcom.api.model.UpsRateCodes;
import com.freightcom.api.model.UpsTransitCodes;
import com.freightcom.api.repositories.ObjectBase;

/**
 * @author bryan
 *
 */

@Service
public class UPSResolveService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectBase objectBase;
    private final EntityManager entityManager;

    public UPSResolveService(final ObjectBase objectBase)
    {
        this.objectBase = objectBase;
        entityManager = this.objectBase.getEntityManager();
    }

    public UpsRateCodes resolveRatingService(String fromCountry, String toCountry, String code)
    {
        TypedQuery<UpsRateCodes> query = entityManager.createQuery(
                "select U from UpsRateCodes U where U.fromCountry=:fromCountry and U.toCountry=:toCountry and U.ratingCode=:code",
                UpsRateCodes.class);

        UpsRateCodes rateCode = null;

        log.debug("RATING " + fromCountry + " " + toCountry + " " + code);

        if (toCountry != null) {
            try {
                rateCode = query.setParameter("fromCountry", fromCountry)
                        .setParameter("toCountry", toCountry)
                        .setParameter("code", code)
                        .getSingleResult();
            } catch (NoResultException e) {
                // null result
            }
        }

        if (rateCode == null && toCountry != null) {
            try {
                query = entityManager.createQuery(
                        "select U from UpsRateCodes U where U.fromCountry=:fromCountry and U.toCountry is null and U.ratingCode=:code",
                        UpsRateCodes.class);
                rateCode = query.setParameter("fromCountry", fromCountry)
                        .setParameter("code", code)
                        .getSingleResult();
            } catch (NoResultException e) {
                // null result
            }
        }

        log.debug("FOUND RATE CODE " + rateCode);

        return rateCode;
    }

    public UpsTransitCodes resolveTransitService(String fromCountry, String toCountry, String code)
    {
        TypedQuery<UpsTransitCodes> query = entityManager.createQuery(
                "select U from UpsTransitCodes U where U.fromCountry=:fromCountry and U.toCountry=:toCountry and U.serviceCode=:code",
                UpsTransitCodes.class);

        UpsTransitCodes transitCode = null;

        log.debug("TIME IN TRANSIT " + fromCountry + " " + toCountry + " " + code);

        if (toCountry != null) {
            try {
                transitCode = query.setParameter("fromCountry", fromCountry)
                        .setParameter("toCountry", toCountry)
                        .setParameter("code", code)
                        .getSingleResult();
            } catch (NoResultException e) {
                // null result
            }
        }

        if (transitCode == null && toCountry != null) {
            try {
                query = entityManager.createQuery(
                        "select U from UpsTransitCodes U where U.fromCountry=:fromCountry and U.toCountry is null and U.serviceCode=:code",
                        UpsTransitCodes.class);
                transitCode = query.setParameter("fromCountry", fromCountry)
                        .setParameter("code", code)
                        .getSingleResult();
            } catch (NoResultException e) {
                // null result
            }
        }

        log.debug("FOUND TRANSIT CODE " + transitCode);

        return transitCode;
    }
}
