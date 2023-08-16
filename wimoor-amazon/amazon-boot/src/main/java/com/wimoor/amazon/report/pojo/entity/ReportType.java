package com.wimoor.amazon.report.pojo.entity;

import java.util.HashSet;
import java.util.Set;

public class ReportType {
	public static final String ProductListings = "GET_MERCHANT_LISTINGS_ALL_DATA";
	public static final String ProductOpenListings = "GET_FLAT_FILE_OPEN_LISTINGS_DATA";
	public static final String ProductActiveListings="GET_MERCHANT_LISTINGS_DATA";
	public static final String ProductRecommendedListings="MBOP_RECOMMENDED_PRODUCTS_REPORT";
	public static final String OrdersByOrderDateReport = "GET_FLAT_FILE_ALL_ORDERS_DATA_BY_ORDER_DATE_GENERAL";
	public static final String OrdersByLastUpdateReport = "GET_FLAT_FILE_ALL_ORDERS_DATA_BY_LAST_UPDATE_GENERAL";
	public static final String InventoryReport = "GET_FBA_MYI_UNSUPPRESSED_INVENTORY_DATA";
	public static final String ReservedInventoryReport = "GET_RESERVED_INVENTORY_DATA";
	public static final String FBAUNOInventoryReport = "GET_FBA_UNO_INVENTORY_DATA";
	public static final String FbaFeeReport = "GET_FBA_ESTIMATED_FBA_FEES_TXT_DATA";
	public static final String FbaStorageFeeReport = "GET_FBA_STORAGE_FEE_CHARGES_DATA";
	public static final String FbaLongTermStorageFeeReport = "GET_FBA_FULFILLMENT_LONGTERM_STORAGE_FEE_CHARGES_DATA";
	public static final String ProductAdvertReport = "GET_PADS_PRODUCT_PERFORMANCE_OVER_TIME_WEEKLY_DATA_TSV";
	public static final String SettlementReport = "GET_V2_SETTLEMENT_REPORT_DATA_FLAT_FILE_V2";
	public static final String FBAInventoryHealthReport ="GET_FBA_FULFILLMENT_INVENTORY_HEALTH_DATA";
	public static final String FBAReturnsReport ="GET_FBA_FULFILLMENT_CUSTOMER_RETURNS_DATA";
	public static final String BrowseTreeReport ="GET_XML_BROWSE_TREE_DATA";
	public static final String PerformanceReport ="GET_V2_SELLER_PERFORMANCE_REPORT";
	public static final String FulfilledShipmentsReport ="GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL";
	public static final String FbaShipmentDetailRemoveReport="GET_FBA_FULFILLMENT_REMOVAL_ORDER_DETAIL_DATA";
	public static final String SalesAndTrafficBusinessReport="GET_SALES_AND_TRAFFIC_REPORT";
	public static final String OrdersInvoicingReport="GET_FLAT_FILE_ORDER_REPORT_DATA_INVOICING";
	public static final String InventoryByCountry="GET_AFN_INVENTORY_DATA_BY_COUNTRY";
	public static final String InventoryLoader="GET_STRANDED_INVENTORY_LOADER_DATA";
	public static final String InventoryAge="GET_FBA_INVENTORY_AGED_DATA";
	public static final String InventoryReceipts = "GET_FBA_FULFILLMENT_INVENTORY_RECEIPTS_DATA";
	public static final String InventoryDetailViewReport = "GET_LEDGER_DETAIL_VIEW_DATA";
	public static final String InventorySummaryViewReport = "GET_LEDGER_SUMMARY_VIEW_DATA";
	public static final String FbaReimbursementsFeeReport="GET_FBA_REIMBURSEMENTS_DATA";
	public static Set<String> set=null;
		public static Set<String> getSingleReport(){
			if(set==null) {
				set=new HashSet<String>();
				set.add(ReportType.SettlementReport);
				set.add(ReportType.InventoryReport);
				set.add(ReportType.FbaFeeReport);
				set.add(ReportType.OrdersByLastUpdateReport);
			}
			return set;
		}
}