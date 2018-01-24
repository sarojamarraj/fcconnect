package com.freightcom.api.services;

import java.util.List;

import com.freightcom.api.model.PinNumberBlock;


public interface PinBlockManager
{

    long[] getNewPinNumbers(String type, int count);
    String getPropertyValue(String propertyName);
    long[] getNextPinForServiceAndProvider(String type, int count, long serviceId, String provider);
    List<PinNumberBlock> getPinBlocksForNotification();



}
