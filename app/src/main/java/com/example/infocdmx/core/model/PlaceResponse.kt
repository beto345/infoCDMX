package com.example.infocdmx.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlaceResponse(
    @SerializedName("results") val results: List<Place>,
    @SerializedName("status") val status: String,
    @SerializedName("next_page_token") val nextPageToken: String? = null
)

@Parcelize
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
) : Parcelable

@Parcelize
data class Geometry(
    @SerializedName("location") val location: Location,
    @SerializedName("viewport") val viewport: Viewport? = null
) : Parcelable

@Parcelize
data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) : Parcelable

@Parcelize
data class Viewport(
    @SerializedName("northeast") val northeast: Location,
    @SerializedName("southwest") val southwest: Location
) : Parcelable

@Parcelize
data class OpeningHours(
    @SerializedName("open_now") val openNow: Boolean? = null
) : Parcelable

@Parcelize
data class Photo(
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("photo_reference") val photoReference: String,
    @SerializedName("html_attributions") val htmlAttributions: List<String>? = null
) : Parcelable

