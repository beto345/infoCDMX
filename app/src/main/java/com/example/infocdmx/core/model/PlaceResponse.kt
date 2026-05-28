package com.example.infocdmx.core.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("results") val results: List<Place>,
    @SerializedName("status") val status: String,
    @SerializedName("next_page_token") val nextPageToken: String? = null
)

data class Place(
    @SerializedName("place_id") val placeId: String,
    @SerializedName("name") val name: String,
    @SerializedName("vicinity") val vicinity: String? = null,
    @SerializedName("formatted_address") val formattedAddress: String? = null,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("rating") val rating: Double? = null,
    @SerializedName("user_ratings_total") val userRatingsTotal: Int? = null,
    @SerializedName("price_level") val priceLevel: Int? = null,
    @SerializedName("types") val types: List<String>? = null,
    @SerializedName("business_status") val businessStatus: String? = null,
    @SerializedName("opening_hours") val openingHours: OpeningHours? = null,
    @SerializedName("photos") val photos: List<Photo>? = null,
    @SerializedName("icon") val icon: String? = null
)

data class Geometry(
    @SerializedName("location") val location: Location,
    @SerializedName("viewport") val viewport: Viewport? = null
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class Viewport(
    @SerializedName("northeast") val northeast: Location,
    @SerializedName("southwest") val southwest: Location
)

data class OpeningHours(
    @SerializedName("open_now") val openNow: Boolean? = null,
    @SerializedName("weekday_text") val weekdayText: List<String>? = null
)

data class Photo(
    @SerializedName("photo_reference") val photoReference: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("html_attributions") val htmlAttributions: List<String>? = null
)
