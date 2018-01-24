package com.freightcom.api.carrier;

import java.util.Comparator;

import com.freightcom.api.model.Service;
import com.freightcom.api.model.ShippingAddress;


public class RateInfo  implements Comparable<RateInfo> {
	private long carrierId;
	private long serviceId;
	private double baseCharge;
	private double baseChargeTariff;
	private double fuelSurcharge; 
	private double fuelSurchargeTariff;
	private double fuel_perc;
	private String color="#000000"; 
	
	private double totalSurcharge = 0;
	
	private String carrierName;
	private String serviceName;
	private boolean disabled;
	
	private String modeTransport;
	
	//total cost for CWW
	private double totalCost;
	
	//total of baseCharge + fuelSurcharge + other surcharges
	private double total;
	
	//transit time 
	private int transitDays=0;
	
	private String deliveryInfo;
	
	private int currencyType;
	
	//place holder for DHL US AF and Trucking beyond, used in CWSAPI.java
	private String beyondFlagFrom;
	private String beyondFlagTo;
	
	private double ruralCharge;
	private double beyondCharge;
	
	private String instanceAPIName;

	//total cost for franchise
	private double totalCostFranchise;

	private Service service;
	
	private boolean isDiscounted = true;
	
	private double billWeight; 
	private double cubedWeight;    
    private int shipmentDensity;
	
	//carrierSpecific 
	//UPS
	private double handlingCharges;
	private double serviceOptionsCharges;
	private double negotiatedRates;
	private double largePackageSurcharges;
	private boolean ups_switch_res_comm; //indicator that rating response converted residential indicator to commercial
	private boolean schedPickup = false;
	
	private ShippingAddress originTerminal;
	private ShippingAddress destinationTerminal;
	private float customerMarkUp;
	private float franchiseMarkUp;
	private float customerMinMarkUp;
	private float customerMaxMarkUp;
	private double currencyRate;
	private int originalMarkdownType = 0; //we set this only if the rate was originally presented based on a markdown
	private int customerMarkupType;
	
	private double baseCostToF;
	private double baseCostToMF;
	
	private double fuelCostToF;
	private double fuelCostToMF;
	private double tariffRate; //no taxes
	
	//Carrier specific: MFW
	public String mfwConnectionKey="";
	public String mfwBookingKey="";
	
	// SFW 
    private String sfwContractId;
    private boolean guaranteed = false;
    
    public String internalNotes = "";
    private double totalCostToMFFranchise;
    private double grossProfit;
    private double freightcomCost;
    private String deliveryDate;
	
	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public RateInfo(String apiName) {
		this.instanceAPIName = apiName;
	}
	
	public String getDeliveryInfo() {
		return deliveryInfo;
	}
	public void setDeliveryInfo(String deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}
	/**
	 * @return Returns the transitDays.
	 */
	public int getTransitDays() {
		return transitDays;
	}
	/**
	 * @param transitDays The transitDays to set.
	 */
	public void setTransitDays(int transitDays) {
		this.transitDays = transitDays;
	}
	/**
	 * @return Returns the baseCharge.
	 */
	public double getBaseCharge() {
		return baseCharge;
	}
	/**
	 * @param baseCharge The baseCharge to set.
	 */
	public void setBaseCharge(double baseCharge) {
		this.baseCharge = baseCharge;
	}
	/**
	 * @return Returns the carrierId.
	 */
	public long getCarrierId() {
		return carrierId;
	}
	/**
	 * @param carrierId The carrierId to set.
	 */
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	/**
	 * @return Returns the fuelSurcharge.
	 */
	public double getFuelSurcharge() {
		return fuelSurcharge;
	}
	/**
	 * @param fuelSurcharge The fuelSurcharge to set.
	 */
	public void setFuelSurcharge(double fuelSurcharge) {
		this.fuelSurcharge = fuelSurcharge;
	}
	/**
	 * @return Returns the serviceId.
	 */
	public long getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId The serviceId to set.
	 */
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	/**
	 * @return Returns the modeTransport.
	 */
	public String getModeTransport() {
		return modeTransport;
	}
	/**
	 * @param modeTransport The modeTransport to set.
	 */
	public void setModeTransport(String modeTransport) {
		this.modeTransport = modeTransport;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = Math.round(totalCost * 100.0) / 100.0;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = Math.round(total * 100.0) / 100.0;
		
	}
	public double getTotalSurcharge() {
		return totalSurcharge;
	}
	public void setTotalSurcharge(double totalSurcharge) {
		this.totalSurcharge = totalSurcharge;
	}
	public int getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}
	public String getBeyondFlagFrom() {
		return beyondFlagFrom;
	}
	public void setBeyondFlagFrom(String beyondFlagFrom) {
		this.beyondFlagFrom = beyondFlagFrom;
	}
	public String getBeyondFlagTo() {
		return beyondFlagTo;
	}
	public void setBeyondFlagTo(String beyondFlagTo) {
		this.beyondFlagTo = beyondFlagTo;
	}
	
	public double getRuralCharge() {
		return ruralCharge;
	}
	public void setRuralCharge(double ruralCharge) {
		this.ruralCharge = ruralCharge;
	}
	
	public double getBeyondCharge() {
		return beyondCharge;
	}
	public void setBeyondCharge(double beyondCharge) {
		this.beyondCharge = beyondCharge;
	}
	
	public String getInstanceAPIName() {
		return instanceAPIName;
	}
	public void setInstanceAPIName(String instanceAPIName) {
		this.instanceAPIName = instanceAPIName;
	}

	public double getTotalCostFranchise() {
		return totalCostFranchise;
	}

	public void setTotalCostFranchise(double totalCostFranchise) {
		this.totalCostFranchise = totalCostFranchise;
	}

	public double getHandlingCharges() {
		return handlingCharges;
	}
	public void setHandlingCharges(double handlingCharges) {
		this.handlingCharges = handlingCharges;
	}
	public double getNegotiatedRates() {
		return negotiatedRates;
	}

	public void setNegotiatedRates(double negotiatedRates) {
		this.negotiatedRates = negotiatedRates;
	}

	public double getServiceOptionsCharges() {
		return serviceOptionsCharges;
	}

	public void setServiceOptionsCharges(double serviceOptionsCharges) {
		this.serviceOptionsCharges = serviceOptionsCharges;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public double getFuel_perc() {
		return fuel_perc;
	}

	public void setFuel_perc(double fuel_perc) {
		this.fuel_perc = fuel_perc;
	}
	
	
	public static Comparator<RateInfo> PriceComparator = new Comparator<RateInfo>() {
		public int compare(RateInfo arg0, RateInfo arg1) {
			double total1 = ((RateInfo) arg0).getTotal();
			double total2 = ((RateInfo) arg1).getTotal();
			
			if(total1>total2)
				return 1;
			else if(total1<total2)
				return -1;
			else return 0;
		
		}
	};
	
	public static Comparator<RateInfo> serviceNameComparator = new Comparator<RateInfo>() {
		public int compare(RateInfo arg0, RateInfo arg1) {
			String serviceName1 = ((RateInfo) arg0).getServiceName();
			String serviceName2 = ((RateInfo) arg1).getServiceName();
			
			return (serviceName1.compareTo(serviceName2));
		}
	};

	public static Comparator<RateInfo> CarrierNameComparator = new Comparator<RateInfo>() {
		public int compare(RateInfo arg0, RateInfo arg1) {
			String carrierName1 = ((RateInfo) arg0).getCarrierName()
					.toUpperCase();
			String carrierName2 = ((RateInfo) arg1).getCarrierName()
					.toUpperCase();

			// if (!(carrierName1.equals(carrierName2)))
			return carrierName1.compareTo(carrierName2);
			// else
			// return lastName1.compareTo(lastName2);
		}
	};

	public boolean isSchedPickup() {
		return schedPickup;
	}
	public boolean getSchedPickup() {
		return schedPickup;
	}

	public void setSchedPickup(boolean schedPickup) {
		this.schedPickup = schedPickup;
	}

	public boolean isDiscounted() {
		return isDiscounted;
	}

	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}	

	public double getBillWeight() {
		return billWeight;
	}

	public void setBillWeight(double billWeight) {
		this.billWeight = billWeight;
	}

	public double getLargePackageSurcharges() {
		return largePackageSurcharges;
	}

	public void setLargePackageSurcharges(double largePackageSurcharges) {
		this.largePackageSurcharges = largePackageSurcharges;
	}

	public boolean isUps_switch_res_comm() {
		return ups_switch_res_comm;
	}

	public void setUps_switch_res_comm(boolean ups_switch_res_comm) {
		this.ups_switch_res_comm = ups_switch_res_comm;
	}

	/**
	 * @return the originTerminal
	 */
	public ShippingAddress getOriginTerminal() {
		return originTerminal;
	}

	/**
	 * @param originTerminal the originTerminal to set
	 */
	public void setOriginTerminal(ShippingAddress originTerminal) {
		this.originTerminal = originTerminal;
	}

	/**
	 * @return the destinationTerminal
	 */
	public ShippingAddress getDestinationTerminal() {
		return destinationTerminal;
	}

	/**
	 * @param destinationTerminal the destinationTerminal to set
	 */
	public void setDestinationTerminal(ShippingAddress destinationTerminal) {
		this.destinationTerminal = destinationTerminal;
	}

	/**
	 * @return the mfwConnectionKey
	 */
	public String getMfwConnectionKey() {
		return mfwConnectionKey;
	}

	/**
	 * @param mfwConnectionKey the mfwConnectionKey to set
	 */
	public void setMfwConnectionKey(String mfwConnectionKey) {
		this.mfwConnectionKey = mfwConnectionKey;
	}

	/**
	 * @return the mfwBookingKey
	 */
	public String getMfwBookingKey() {
		return mfwBookingKey;
	}

	/**
	 * @param mfwBookingKey the mfwBookingKey to set
	 */
	public void setMfwBookingKey(String mfwBookingKey) {
		this.mfwBookingKey = mfwBookingKey;
	}

	/**
	 * @return the customerMarkUp
	 */
	public float getCustomerMarkUp() {
		return customerMarkUp;
	}

	/**
	 * @param customerMarkUp the customerMarkUp to set
	 */
	public void setCustomerMarkUp(float customerMarkUp) {
		this.customerMarkUp = customerMarkUp;
	}

	/**
	 * @return the franchiseMarkUp
	 */
	public float getFranchiseMarkUp() {
		return franchiseMarkUp;
	}

	/**
	 * @param franchiseMarkUp the franchiseMarkUp to set
	 */
	public void setFranchiseMarkUp(float franchiseMarkUp) {
		this.franchiseMarkUp = franchiseMarkUp;
	}	
	
	private double baseCost;

	public double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(double baseCost) {
		this.baseCost = baseCost;
	}
	
	private double fuelCost;
	
	public double getFuelCost() {
		return fuelCost;
	}

	public void setFuelCost(double fuelCost) {
		this.fuelCost = fuelCost;
	}

	/**
	 * @return the sfwContractId
	 */
	public String getSfwContractId() {
		return sfwContractId;
	}

	/**
	 * @param sfwContractId the sfwContractId to set
	 */
	public void setSfwContractId(String sfwContractId) {
		this.sfwContractId = sfwContractId;
	}
	/**
	 * @return the guaranteed
	 */
	public boolean isGuaranteed() {
		return guaranteed;
	}

	/**
	 * @param guaranteed the guaranteed to set
	 */
	public void setGuaranteed(boolean guaranteed) {
		this.guaranteed = guaranteed;
	}

	/**
	 * @return the customerMinMarkUp
	 */
	public float getCustomerMinMarkUp() {
		return customerMinMarkUp;
	}

	/**
	 * @param customerMinMarkUp the customerMinMarkUp to set
	 */
	public void setCustomerMinMarkUp(float customerMinMarkUp) {
		this.customerMinMarkUp = customerMinMarkUp;
	}

	/**
	 * @return the cubedWeight
	 */
	public double getCubedWeight() {
		return cubedWeight;
	}

	/**
	 * @param cubedWeight the cubedWeight to set
	 */
	public void setCubedWeight(double cubedWeight) {
		this.cubedWeight = cubedWeight;
	}
	
	/**
	 * @return the shipmentDensity
	 */
	public int getShipmentDensity() {
		return shipmentDensity;
	}


	/**
	 * @param shipmentDensity the shipmentDensity to set
	 */
	public void setShipmentDensity(int shipmentDensity) {
		this.shipmentDensity = shipmentDensity;
	}

	/**
	 * @return the currencyRate
	 */
	public double getCurrencyRate() {
		return currencyRate;
	}

	/**
	 * @param currencyRate the currencyRate to set
	 */
	public void setCurrencyRate(double currencyRate) {
		this.currencyRate = currencyRate;
	}

	public double getBaseChargeTariff() {
		return baseChargeTariff;
	}

	public void setBaseChargeTariff(double baseChargeTariff) {
		this.baseChargeTariff = baseChargeTariff;
	}

	public double getFuelSurchargeTariff() {
		return fuelSurchargeTariff;
	}

	public void setFuelSurchargeTariff(double fuelSurchargeTariff) {
		this.fuelSurchargeTariff = fuelSurchargeTariff;
	}

	public int getOriginalMarkdownType() {
		return originalMarkdownType;
	}

	public void setOriginalMarkdownType(int originalMarkdownType) {
		this.originalMarkdownType = originalMarkdownType;
	}

	public int getCustomerMarkupType() {
		return customerMarkupType;
	}

	public void setCustomerMarkupType(int customerMarkupType) {
		this.customerMarkupType = customerMarkupType;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public double getTotalCostToMFFranchise() {
		return totalCostToMFFranchise;
	}

	public void setTotalCostToMFFranchise(double totalCostToMFFranchise) {
		this.totalCostToMFFranchise = totalCostToMFFranchise;
	}

	public double getBaseCostToF() {
		return baseCostToF;
	}

	public void setBaseCostToF(double baseCostToF) {
		this.baseCostToF = baseCostToF;
	}

	public double getFuelCostToF() {
		return fuelCostToF;
	}

	public void setFuelCostToF(double fuelCostToF) {
		this.fuelCostToF = fuelCostToF;
	}

	public double getBaseCostToMF() {
		return baseCostToMF;
	}

	public void setBaseCostToMF(double baseCostToMF) {
		this.baseCostToMF = baseCostToMF;
	}

	public double getFuelCostToMF() {
		return fuelCostToMF;
	}

	public void setFuelCostToMF(double fuelCostToMF) {
		this.fuelCostToMF = fuelCostToMF;
	}

	public double getTariffRate() {
		return tariffRate;
	}

	public void setTariffRate(double tariffRate) {
		this.tariffRate = tariffRate;
	}

	public double getFreightcomCost() {
		return freightcomCost;
	}

	public void setFreightcomCost(double freightcomCost) {
		this.freightcomCost = freightcomCost;
	}

	public float getCustomerMaxMarkUp() {
		return customerMaxMarkUp;
	}

	public void setCustomerMaxMarkUp(float customerMaxMarkUp) {
		this.customerMaxMarkUp = customerMaxMarkUp;
	}

    @Override
    public int compareTo(RateInfo o)
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
