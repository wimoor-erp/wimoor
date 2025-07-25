package com.amazon.spapi.SellingPartnerAPIAA;

import okhttp3.Request;
import java.util.Arrays;
import java.util.List;

public class RestrictedDataTokenSigner {

    /**
     * Sign the request with a Restricted Data Token
     *
     * @param request Request to sign
     * @param restrictedDataToken The Restricted Data Token
     * @param operationName The operation name in format 'ClassName-operationId'
     * @return Signed request
     * @throws IllegalArgumentException If RDT is used for a non-restricted operation
     */
    public static Request sign(Request request, String restrictedDataToken, String operationName) {
        boolean isRestricted = isRestrictedOperation(operationName);
        if (!isRestricted) {
            throw new IllegalArgumentException(
                    "Operation '" + operationName + "' does not require a Restricted Data Token (RDT). " +
                            "Remove the RDT parameter for non-restricted operations."
            );
        }

        return request.newBuilder()
                .header(LWAAuthorizationSigner.SIGNED_ACCESS_TOKEN_HEADER_NAME, restrictedDataToken)
                .build();
    }

    /**
     * Check if an operation requires RDT access
     *
     * @param operationName The operation name in format 'ClassName-operationId'
     * @return True if the operation requires RDT, false otherwise
     */
    private static boolean isRestrictedOperation(String operationName) {
        List<String> restrictedOperations = Arrays.asList(
                // Direct Fulfillment Orders API
                "VendorOrdersApi-getOrders",
                "VendorOrdersApi-getOrder",

                // Direct Fulfillment Shipping API
                "VendorShippingLabelsApi-getShippingLabel",
                "VendorShippingLabelsApi-getShippingLabels",
                "VendorShippingApi-getPackingSlips",
                "VendorShippingApi-getPackingSlip",
                "VendorShippingLabelsApi-getCustomerInvoice",
                "VendorShippingLabelsApi-getCustomerInvoices",
                "VendorShippingLabelsApi-createShippingLabels",

                // Easy Ship API v2022-03-23
                "EasyShipApi-createScheduledPackageBulk",

                // Orders API
                "OrdersV0Api-getOrders",
                "OrdersV0Api-getOrder",
                "OrdersV0Api-getOrderBuyerInfo",
                "OrdersV0Api-getOrderAddress",
                "OrdersV0Api-getOrderItems",
                "OrdersV0Api-getOrderItemsBuyerInfo",
                "OrdersV0Api-getOrderRegulatedInfo",

                // Merchant Fulfillment API
                "MerchantFulfillmentApi-getShipment",
                "MerchantFulfillmentApi-cancelShipment",
                "MerchantFulfillmentApi-createShipment",

                // Shipment Invoicing
                "ShipmentInvoiceApi-getShipmentDetails",

                // Reports API
                "ReportsApi-getReportDocument"
        );

        return restrictedOperations.contains(operationName);
    }
}
