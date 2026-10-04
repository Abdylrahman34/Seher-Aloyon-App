package com.example.model

data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val memberTier: String = "Aster Privilège - Gold",
    val loyaltyPoints: Int = 1250,
    val memberSince: Long = System.currentTimeMillis(),
    val favoriteNotes: String = "Rare Taif Rose, Royal Amber & Smoked Agarwood",
    val vipBio: String = "Aficionado of niche French-Oriental perfumery & bespoke aesthetic couture."
)

data class ProductItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val category: String,
    val price: Double,
    val currency: String = "$",
    val rating: Float = 4.9f,
    val reviewCount: Int = 84,
    val description: String,
    val topNotes: String,
    val heartNotes: String,
    val baseNotes: String,
    val volume: String,
    val badge: String? = null,
    val inStock: Boolean = true,
    val iconType: String = "perfume" // "perfume", "eye", "skincare", "treatment", "jewelry"
)

data class Specialist(
    val id: String,
    val name: String,
    val title: String,
    val specialty: String,
    val rating: Float = 4.98f,
    val experience: String = "12 yrs"
)

data class Appointment(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val serviceName: String = "",
    val specialistName: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val status: String = "Confirmed",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class CartItem(
    val product: ProductItem,
    val quantity: Int = 1
)

data class OrderItemSummary(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0
)

data class Order(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val items: List<OrderItemSummary> = emptyList(),
    val subtotal: Double = 0.0,
    val giftPackaging: Boolean = true,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "Processing in Atelier",
    val trackingCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class FirebaseConfigStatus(
    val isLiveConnected: Boolean,
    val isFallbackMode: Boolean,
    val projectId: String?,
    val message: String
)
