package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.model.Appointment
import com.example.model.FirebaseConfigStatus
import com.example.model.Order
import com.example.model.OrderItemSummary
import com.example.model.UserProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID

class FirebaseManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("maison_aster_vault", Context.MODE_PRIVATE)

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                firebaseAuth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
                Log.d("FirebaseManager", "Firebase successfully initialized with live backend")
            } else {
                Log.w("FirebaseManager", "FirebaseApp not initialized. Operating in resilient local persistence mode.")
            }
        } catch (e: Exception) {
            Log.e("FirebaseManager", "Error initializing Firebase instances: ${e.message}")
        }
    }

    fun isLiveFirebaseConfigured(): Boolean {
        return try {
            FirebaseApp.getApps(context).isNotEmpty() && firebaseAuth != null && firestore != null
        } catch (e: Exception) {
            false
        }
    }

    fun getFirebaseStatus(): FirebaseConfigStatus {
        val isLive = isLiveFirebaseConfigured()
        val app = try {
            if (FirebaseApp.getApps(context).isNotEmpty()) FirebaseApp.getInstance() else null
        } catch (e: Exception) {
            null
        }

        val projectId = app?.options?.projectId

        return FirebaseConfigStatus(
            isLiveConnected = isLive,
            isFallbackMode = !isLive,
            projectId = projectId,
            message = if (isLive) {
                "Connected to Firebase Project: ${projectId ?: "Active"}. Cloud Firestore & Auth are operating live."
            } else {
                "Operating in Secure Local Persistence Mode. To connect live Cloud Firestore, place your google-services.json in the /app folder."
            }
        )
    }

    // --- Validation Rules ---
    fun validateUsername(username: String): String? {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) return "Username cannot be empty"
        if (trimmed.length < 3) return "Username must be at least 3 characters"
        if (trimmed.length > 20) return "Username must not exceed 20 characters"
        if (!trimmed.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return "Username can only contain letters, numbers, and underscores"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "Password cannot be empty"
        if (password.length < 6) return "Password must be at least 6 characters for security"
        return null
    }

    // --- Registration (Strictly Username & Password) ---
    suspend fun registerUser(
        username: String,
        password: String,
        displayName: String
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val cleanUser = username.trim()
        val cleanName = displayName.trim().ifEmpty { cleanUser }

        val userErr = validateUsername(cleanUser)
        if (userErr != null) return@withContext Result.failure(IllegalArgumentException(userErr))

        val passErr = validatePassword(password)
        if (passErr != null) return@withContext Result.failure(IllegalArgumentException(passErr))

        val lowerUser = cleanUser.lowercase()
        val mappedEmail = "$lowerUser@maisonaster.auth"

        // 1. Check duplicate username in Live Firestore if available
        if (isLiveFirebaseConfigured()) {
            try {
                val db = firestore!!
                val doc = db.collection("usernames").document(lowerUser).get().await()
                if (doc.exists()) {
                    return@withContext Result.failure(
                        IllegalArgumentException("The username '$cleanUser' is already registered. Please choose another.")
                    )
                }

                // Register with Firebase Auth
                val authResult = firebaseAuth!!.createUserWithEmailAndPassword(mappedEmail, password).await()
                val uid = authResult.user?.uid ?: UUID.randomUUID().toString()

                val profile = UserProfile(
                    uid = uid,
                    username = cleanUser,
                    displayName = cleanName,
                    email = mappedEmail,
                    memberTier = "Aster Privilège - Gold",
                    loyaltyPoints = 1500,
                    memberSince = System.currentTimeMillis()
                )

                // Reserve username document in Firestore
                val usernameData = hashMapOf(
                    "username" to cleanUser,
                    "uid" to uid,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("usernames").document(lowerUser).set(usernameData).await()

                // Save user profile in Firestore
                val profileData = hashMapOf(
                    "uid" to profile.uid,
                    "username" to profile.username,
                    "displayName" to profile.displayName,
                    "email" to profile.email,
                    "memberTier" to profile.memberTier,
                    "loyaltyPoints" to profile.loyaltyPoints,
                    "memberSince" to profile.memberSince,
                    "favoriteNotes" to profile.favoriteNotes,
                    "vipBio" to profile.vipBio
                )
                db.collection("users").document(uid).set(profileData).await()

                saveSessionLocally(profile, password)
                return@withContext Result.success(profile)

            } catch (e: Exception) {
                Log.w("FirebaseManager", "Live Firebase registration failed: ${e.message}. Falling back gracefully.")
                // If live fails, proceed with secure local storage
            }
        }

        // Local Resilient Storage Fallback
        val localUsersJson = prefs.getString("registered_users", "{}") ?: "{}"
        val usersObj = JSONObject(localUsersJson)
        if (usersObj.has(lowerUser)) {
            return@withContext Result.failure(
                IllegalArgumentException("The username '$cleanUser' is already registered. Please choose another.")
            )
        }

        val uid = "aster_user_" + UUID.randomUUID().toString().take(12)
        val profile = UserProfile(
            uid = uid,
            username = cleanUser,
            displayName = cleanName,
            email = mappedEmail,
            memberTier = "Aster Privilège - Gold",
            loyaltyPoints = 1500,
            memberSince = System.currentTimeMillis()
        )

        // Store user with hashed password
        val userRecord = JSONObject().apply {
            put("uid", uid)
            put("username", cleanUser)
            put("displayName", cleanName)
            put("email", mappedEmail)
            put("passwordHash", hashPassword(password))
            put("memberTier", profile.memberTier)
            put("loyaltyPoints", profile.loyaltyPoints)
            put("memberSince", profile.memberSince)
            put("favoriteNotes", profile.favoriteNotes)
            put("vipBio", profile.vipBio)
        }
        usersObj.put(lowerUser, userRecord)
        prefs.edit().putString("registered_users", usersObj.toString()).apply()

        saveSessionLocally(profile, password)
        Result.success(profile)
    }

    // --- Sign In (Strictly Username & Password) ---
    suspend fun loginUser(username: String, password: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        val cleanUser = username.trim()
        val lowerUser = cleanUser.lowercase()
        val mappedEmail = "$lowerUser@maisonaster.auth"

        if (cleanUser.isEmpty() || password.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter both username and password"))
        }

        // 1. Try Live Firebase Auth if available
        if (isLiveFirebaseConfigured()) {
            try {
                val authResult = firebaseAuth!!.signInWithEmailAndPassword(mappedEmail, password).await()
                val uid = authResult.user?.uid ?: ""

                // Fetch profile from Firestore
                val doc = firestore!!.collection("users").document(uid).get().await()
                val profile = if (doc.exists()) {
                    UserProfile(
                        uid = uid,
                        username = doc.getString("username") ?: cleanUser,
                        displayName = doc.getString("displayName") ?: cleanUser,
                        email = doc.getString("email") ?: mappedEmail,
                        memberTier = doc.getString("memberTier") ?: "Aster Privilège - Gold",
                        loyaltyPoints = doc.getLong("loyaltyPoints")?.toInt() ?: 1500,
                        memberSince = doc.getLong("memberSince") ?: System.currentTimeMillis(),
                        favoriteNotes = doc.getString("favoriteNotes") ?: "Rare Taif Rose, Royal Amber & Smoked Agarwood",
                        vipBio = doc.getString("vipBio") ?: "Maison Aster Connoisseur"
                    )
                } else {
                    UserProfile(
                        uid = uid,
                        username = cleanUser,
                        displayName = cleanUser,
                        email = mappedEmail
                    )
                }

                saveSessionLocally(profile, password)
                return@withContext Result.success(profile)

            } catch (e: Exception) {
                Log.w("FirebaseManager", "Live login failed: ${e.message}. Checking local storage...")
            }
        }

        // 2. Local Fallback Verification
        val localUsersJson = prefs.getString("registered_users", "{}") ?: "{}"
        val usersObj = JSONObject(localUsersJson)

        if (!usersObj.has(lowerUser)) {
            // Check default demo account for instant ease of testing
            if (lowerUser == "aster" && password == "luxe123") {
                val demoProfile = UserProfile(
                    uid = "aster_demo_vip_001",
                    username = "aster",
                    displayName = "Comtesse de Laurent",
                    email = "aster@maisonaster.auth",
                    memberTier = "Aster Privilège - Haute Ambassadrice",
                    loyaltyPoints = 3200,
                    favoriteNotes = "Taif Rose, Black Amber & Saffron",
                    vipBio = "Curator of bespoke fine fragrances and Parisian eye couture."
                )
                saveSessionLocally(demoProfile, password)
                return@withContext Result.success(demoProfile)
            }
            return@withContext Result.failure(IllegalArgumentException("No account found for username '$cleanUser'. Please register."))
        }

        val record = usersObj.getJSONObject(lowerUser)
        val expectedHash = record.optString("passwordHash", "")
        if (expectedHash != hashPassword(password)) {
            return@withContext Result.failure(IllegalArgumentException("Incorrect password for '$cleanUser'."))
        }

        val profile = UserProfile(
            uid = record.optString("uid", UUID.randomUUID().toString()),
            username = record.optString("username", cleanUser),
            displayName = record.optString("displayName", cleanUser),
            email = record.optString("email", mappedEmail),
            memberTier = record.optString("memberTier", "Aster Privilège - Gold"),
            loyaltyPoints = record.optInt("loyaltyPoints", 1500),
            memberSince = record.optLong("memberSince", System.currentTimeMillis()),
            favoriteNotes = record.optString("favoriteNotes", "Taif Rose & Smoked Oud"),
            vipBio = record.optString("vipBio", "Connoisseur of Maison Aster")
        )

        saveSessionLocally(profile, password)
        Result.success(profile)
    }

    // --- Session Persistence ---
    private fun saveSessionLocally(profile: UserProfile, passwordPlain: String) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("current_uid", profile.uid)
            .putString("current_username", profile.username)
            .putString("current_display_name", profile.displayName)
            .putString("current_email", profile.email)
            .putString("current_tier", profile.memberTier)
            .putInt("current_points", profile.loyaltyPoints)
            .putLong("current_since", profile.memberSince)
            .putString("current_notes", profile.favoriteNotes)
            .putString("current_bio", profile.vipBio)
            .apply()
    }

    fun getCurrentSession(): UserProfile? {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (!isLoggedIn) return null

        val uid = prefs.getString("current_uid", null) ?: return null
        return UserProfile(
            uid = uid,
            username = prefs.getString("current_username", "Guest") ?: "Guest",
            displayName = prefs.getString("current_display_name", "Valued Guest") ?: "Valued Guest",
            email = prefs.getString("current_email", "") ?: "",
            memberTier = prefs.getString("current_tier", "Aster Privilège - Gold") ?: "Aster Privilège - Gold",
            loyaltyPoints = prefs.getInt("current_points", 1500),
            memberSince = prefs.getLong("current_since", System.currentTimeMillis()),
            favoriteNotes = prefs.getString("current_notes", "") ?: "",
            vipBio = prefs.getString("current_bio", "") ?: ""
        )
    }

    suspend fun updateProfile(updated: UserProfile): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            if (isLiveFirebaseConfigured()) {
                val db = firestore!!
                val profileMap = hashMapOf(
                    "displayName" to updated.displayName,
                    "favoriteNotes" to updated.favoriteNotes,
                    "vipBio" to updated.vipBio
                )
                db.collection("users").document(updated.uid).update(profileMap as Map<String, Any>).await()
            }
        } catch (e: Exception) {
            Log.w("FirebaseManager", "Firestore profile update fallback: ${e.message}")
        }

        prefs.edit()
            .putString("current_display_name", updated.displayName)
            .putString("current_notes", updated.favoriteNotes)
            .putString("current_bio", updated.vipBio)
            .apply()

        Result.success(updated)
    }

    fun logout() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            Log.e("FirebaseManager", "Logout exception: ${e.message}")
        }
        prefs.edit().putBoolean("is_logged_in", false).apply()
    }

    // --- Appointments Collection ---
    suspend fun saveAppointment(appointment: Appointment): Result<Appointment> = withContext(Dispatchers.IO) {
        val appointmentId = if (appointment.id.isEmpty()) "apt_" + UUID.randomUUID().toString().take(10) else appointment.id
        val finalApt = appointment.copy(id = appointmentId)

        if (isLiveFirebaseConfigured()) {
            try {
                val aptData = hashMapOf(
                    "id" to finalApt.id,
                    "userId" to finalApt.userId,
                    "username" to finalApt.username,
                    "serviceName" to finalApt.serviceName,
                    "specialistName" to finalApt.specialistName,
                    "date" to finalApt.date,
                    "timeSlot" to finalApt.timeSlot,
                    "status" to finalApt.status,
                    "notes" to finalApt.notes,
                    "createdAt" to finalApt.createdAt
                )
                firestore!!.collection("appointments").document(finalApt.id).set(aptData).await()
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Firestore appointment save error: ${e.message}")
            }
        }

        // Local cache
        val existingJson = prefs.getString("appointments_${finalApt.userId}", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val obj = JSONObject().apply {
            put("id", finalApt.id)
            put("userId", finalApt.userId)
            put("username", finalApt.username)
            put("serviceName", finalApt.serviceName)
            put("specialistName", finalApt.specialistName)
            put("date", finalApt.date)
            put("timeSlot", finalApt.timeSlot)
            put("status", finalApt.status)
            put("notes", finalApt.notes)
            put("createdAt", finalApt.createdAt)
        }
        array.put(obj)
        prefs.edit().putString("appointments_${finalApt.userId}", array.toString()).apply()

        Result.success(finalApt)
    }

    suspend fun getAppointments(userId: String): List<Appointment> = withContext(Dispatchers.IO) {
        if (isLiveFirebaseConfigured()) {
            try {
                val query = firestore!!.collection("appointments")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                if (!query.isEmpty) {
                    val list = query.documents.mapNotNull { doc ->
                        Appointment(
                            id = doc.getString("id") ?: doc.id,
                            userId = doc.getString("userId") ?: userId,
                            username = doc.getString("username") ?: "",
                            serviceName = doc.getString("serviceName") ?: "",
                            specialistName = doc.getString("specialistName") ?: "",
                            date = doc.getString("date") ?: "",
                            timeSlot = doc.getString("timeSlot") ?: "",
                            status = doc.getString("status") ?: "Confirmed",
                            notes = doc.getString("notes") ?: "",
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        )
                    }
                    return@withContext list.sortedByDescending { it.createdAt }
                }
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Firestore getAppointments fallback: ${e.message}")
            }
        }

        // Local storage
        val existingJson = prefs.getString("appointments_$userId", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val list = mutableListOf<Appointment>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                Appointment(
                    id = obj.optString("id"),
                    userId = obj.optString("userId"),
                    username = obj.optString("username"),
                    serviceName = obj.optString("serviceName"),
                    specialistName = obj.optString("specialistName"),
                    date = obj.optString("date"),
                    timeSlot = obj.optString("timeSlot"),
                    status = obj.optString("status", "Confirmed"),
                    notes = obj.optString("notes"),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                )
            )
        }
        list.sortedByDescending { it.createdAt }
    }

    // --- Orders Collection ---
    suspend fun placeOrder(order: Order): Result<Order> = withContext(Dispatchers.IO) {
        val orderId = if (order.id.isEmpty()) "ord_" + UUID.randomUUID().toString().take(10) else order.id
        val tracking = "MA-" + (100000..999999).random()
        val finalOrder = order.copy(id = orderId, trackingCode = tracking)

        if (isLiveFirebaseConfigured()) {
            try {
                val itemsList = finalOrder.items.map { item ->
                    mapOf(
                        "productId" to item.productId,
                        "productName" to item.productName,
                        "quantity" to item.quantity,
                        "unitPrice" to item.unitPrice
                    )
                }
                val orderData = hashMapOf(
                    "id" to finalOrder.id,
                    "userId" to finalOrder.userId,
                    "username" to finalOrder.username,
                    "items" to itemsList,
                    "subtotal" to finalOrder.subtotal,
                    "giftPackaging" to finalOrder.giftPackaging,
                    "discount" to finalOrder.discount,
                    "total" to finalOrder.total,
                    "status" to finalOrder.status,
                    "trackingCode" to finalOrder.trackingCode,
                    "createdAt" to finalOrder.createdAt
                )
                firestore!!.collection("orders").document(finalOrder.id).set(orderData).await()
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Firestore placeOrder error: ${e.message}")
            }
        }

        // Local cache
        val existingJson = prefs.getString("orders_${finalOrder.userId}", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val obj = JSONObject().apply {
            put("id", finalOrder.id)
            put("userId", finalOrder.userId)
            put("username", finalOrder.username)
            put("subtotal", finalOrder.subtotal)
            put("giftPackaging", finalOrder.giftPackaging)
            put("discount", finalOrder.discount)
            put("total", finalOrder.total)
            put("status", finalOrder.status)
            put("trackingCode", finalOrder.trackingCode)
            put("createdAt", finalOrder.createdAt)

            val itemsArr = JSONArray()
            finalOrder.items.forEach { item ->
                val itemObj = JSONObject().apply {
                    put("productId", item.productId)
                    put("productName", item.productName)
                    put("quantity", item.quantity)
                    put("unitPrice", item.unitPrice)
                }
                itemsArr.put(itemObj)
            }
            put("items", itemsArr)
        }
        array.put(obj)
        prefs.edit().putString("orders_${finalOrder.userId}", array.toString()).apply()

        Result.success(finalOrder)
    }

    suspend fun getOrders(userId: String): List<Order> = withContext(Dispatchers.IO) {
        if (isLiveFirebaseConfigured()) {
            try {
                val query = firestore!!.collection("orders")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                if (!query.isEmpty) {
                    val list = query.documents.mapNotNull { doc ->
                        @Suppress("UNCHECKED_CAST")
                        val itemsRaw = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                        val items = itemsRaw.map { map ->
                            OrderItemSummary(
                                productId = map["productId"] as? String ?: "",
                                productName = map["productName"] as? String ?: "",
                                quantity = (map["quantity"] as? Long)?.toInt() ?: 1,
                                unitPrice = (map["unitPrice"] as? Double) ?: 0.0
                            )
                        }

                        Order(
                            id = doc.getString("id") ?: doc.id,
                            userId = doc.getString("userId") ?: userId,
                            username = doc.getString("username") ?: "",
                            items = items,
                            subtotal = doc.getDouble("subtotal") ?: 0.0,
                            giftPackaging = doc.getBoolean("giftPackaging") ?: true,
                            discount = doc.getDouble("discount") ?: 0.0,
                            total = doc.getDouble("total") ?: 0.0,
                            status = doc.getString("status") ?: "Processing in Atelier",
                            trackingCode = doc.getString("trackingCode") ?: "",
                            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        )
                    }
                    return@withContext list.sortedByDescending { it.createdAt }
                }
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Firestore getOrders fallback: ${e.message}")
            }
        }

        // Local cache
        val existingJson = prefs.getString("orders_$userId", "[]") ?: "[]"
        val array = JSONArray(existingJson)
        val list = mutableListOf<Order>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val itemsArr = obj.optJSONArray("items") ?: JSONArray()
            val items = mutableListOf<OrderItemSummary>()
            for (j in 0 until itemsArr.length()) {
                val itemObj = itemsArr.getJSONObject(j)
                items.add(
                    OrderItemSummary(
                        productId = itemObj.optString("productId"),
                        productName = itemObj.optString("productName"),
                        quantity = itemObj.optInt("quantity", 1),
                        unitPrice = itemObj.optDouble("unitPrice", 0.0)
                    )
                )
            }

            list.add(
                Order(
                    id = obj.optString("id"),
                    userId = obj.optString("userId"),
                    username = obj.optString("username"),
                    items = items,
                    subtotal = obj.optDouble("subtotal"),
                    giftPackaging = obj.optBoolean("giftPackaging", true),
                    discount = obj.optDouble("discount", 0.0),
                    total = obj.optDouble("total"),
                    status = obj.optString("status", "Processing in Atelier"),
                    trackingCode = obj.optString("trackingCode"),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                )
            )
        }
        list.sortedByDescending { it.createdAt }
    }

    // --- Wishlist Storage ---
    suspend fun getWishlist(userId: String): Set<String> = withContext(Dispatchers.IO) {
        val json = prefs.getString("wishlist_$userId", "[]") ?: "[]"
        val arr = JSONArray(json)
        val set = mutableSetOf<String>()
        for (i in 0 until arr.length()) {
            set.add(arr.getString(i))
        }
        set
    }

    suspend fun toggleWishlist(userId: String, productId: String): Set<String> = withContext(Dispatchers.IO) {
        val current = getWishlist(userId).toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        val arr = JSONArray(current)
        prefs.edit().putString("wishlist_$userId", arr.toString()).apply()

        if (isLiveFirebaseConfigured()) {
            try {
                firestore!!.collection("wishlists").document(userId)
                    .set(mapOf("productIds" to current.toList()))
            } catch (e: Exception) {
                Log.w("FirebaseManager", "Firestore wishlist sync warning: ${e.message}")
            }
        }
        current
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
