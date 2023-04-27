package com.amazon.spapi.SellingPartnerAPIAA;

import com.amazon.spapi.SellingPartnerAPIAA.LWAClientScopes;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_MIGRATION_API;
import static com.amazon.spapi.SellingPartnerAPIAA.ScopeConstants.SCOPE_NOTIFICATIONS_API;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class LWAClientScopesSerializerDeserializerTest {
    private static final String TEST_SCOPE_1 = SCOPE_NOTIFICATIONS_API;
    private static final String TEST_SCOPE_2 = SCOPE_MIGRATION_API;

    private static final Set<String> scopesTestSellerless = new HashSet<String>(Arrays.asList(TEST_SCOPE_1,
            TEST_SCOPE_2));

    private static final String SELLER_TYPE_SELLER = "seller";
    private static final String SELLER_TYPE_SELLERLESS = "sellerless";

    private Gson gson;

    @BeforeEach
    public void setup() {
        gson = new Gson();
    }

    public static Stream<Arguments> scopeSerialization(){

        return Stream.of(
                Arguments.of(SELLER_TYPE_SELLER, null),
                Arguments.of(SELLER_TYPE_SELLERLESS, new LWAClientScopes(scopesTestSellerless))
        );
    }

    public static Stream<Arguments> scopeDeserialization(){

        return Stream.of(
                Arguments.of(SELLER_TYPE_SELLER, null),
                Arguments.of(SELLER_TYPE_SELLERLESS, "{\"scope\":\"sellingpartnerapi::migration sellingpartnerapi::notifications\"}")
        );
    }

    @ParameterizedTest
    @MethodSource("scopeSerialization")
    public void testSerializeScope(String sellerType, LWAClientScopes testScope){

        String scopeJSON = gson.toJson(testScope);

        if (sellerType.equals(SELLER_TYPE_SELLER)) {
            Assert.assertEquals("null", scopeJSON);
        }
        else if (sellerType.equals(SELLER_TYPE_SELLERLESS)){
            Assert.assertTrue(!scopeJSON.isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("scopeDeserialization")
    public void testDeserializeScope(String sellerType, String serializedValue){

        LWAClientScopes deserializedValue = gson.fromJson(serializedValue, LWAClientScopes.class);
        if (sellerType.equals(SELLER_TYPE_SELLER)) {
            Assert.assertNull(deserializedValue);
        }
        else if (sellerType.equals(SELLER_TYPE_SELLERLESS)){
            Assert.assertEquals(deserializedValue.getScopes(),scopesTestSellerless);
        }
    }

}
