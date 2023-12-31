/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package io.tonapi.models

import io.tonapi.models.DecodedRawMessage

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param subwalletId 
 * @param boundedQueryId 
 * @param rawMessages 
 */


data class DecodedMessageExtInMsgDecodedWalletHighloadV2 (

    @Json(name = "subwallet_id")
    val subwalletId: kotlin.Int,

    @Json(name = "bounded_query_id")
    val boundedQueryId: kotlin.String,

    @Json(name = "raw_messages")
    val rawMessages: kotlin.collections.List<DecodedRawMessage>

)
