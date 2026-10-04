package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirebaseManager
import com.example.data.SampleData
import com.example.model.Appointment
import com.example.model.CartItem
import com.example.model.FirebaseConfigStatus
import com.example.model.Order
import com.example.model.OrderItemSummary
import com.example.model.ProductItem
import com.example.model.Specialist
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MaisonViewModel(
    private val firebaseManager: FirebaseManager,
    initialProfile: UserProfile
) : ViewModel() {

    private val _userProfile = MutableStateFlow(initialProfile)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _firebaseStatus = MutableStateFlow(firebaseManager.getFirebaseStatus())
    val firebaseStatus: StateFlow<FirebaseConfigStatus> = _firebaseStatus.asStateFlow()

    // Products & Filtering
    val allProducts = SampleData.Products
    private val _selectedCategory = MutableStateFlow("All Collections")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredProducts: StateFlow<List<ProductItem>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query ->
        allProducts.filter { product ->
            val matchesCategory = if (category == "All Collections") true else product.category == category
            val matchesQuery = query.isEmpty() ||
                product.name.contains(query, ignoreCase = true) ||
                product.subtitle.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true) ||
                product.topNotes.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allProducts)

    private val _selectedProduct = MutableStateFlow<ProductItem?>(null)
    val selectedProduct: StateFlow<ProductItem?> = _selectedProduct.asStateFlow()

    // Cart / Bag
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _giftPackaging = MutableStateFlow(true)
    val giftPackaging: StateFlow<Boolean> = _giftPackaging.asStateFlow()

    private val _appliedPromo = MutableStateFlow<String?>(null)
    val appliedPromo: StateFlow<String?> = _appliedPromo.asStateFlow()

    private val _promoDiscount = MutableStateFlow(0.0)
    val promoDiscount: StateFlow<Double> = _promoDiscount.asStateFlow()

    val subtotal: StateFlow<Double> = _cartItems.combine(_cartItems) { items, _ ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val grandTotal: StateFlow<Double> = combine(subtotal, _promoDiscount, _giftPackaging) { sub, disc, _ ->
        val result = sub - disc
        if (result < 0.0) 0.0 else result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Wishlist
    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Appointments
    val specialists: List<Specialist> = SampleData.Specialists
    val timeSlots: List<String> = SampleData.AvailableTimeSlots

    private val _userAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val userAppointments: StateFlow<List<Appointment>> = _userAppointments.asStateFlow()

    private val _selectedBookingService = MutableStateFlow("VIP Bespoke Perfume Crafting Session")
    val selectedBookingService: StateFlow<String> = _selectedBookingService.asStateFlow()

    private val _selectedSpecialist = MutableStateFlow(SampleData.Specialists.first())
    val selectedSpecialist: StateFlow<Specialist> = _selectedSpecialist.asStateFlow()

    private val _selectedBookingDate = MutableStateFlow(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000L))
    )
    val selectedBookingDate: StateFlow<String> = _selectedBookingDate.asStateFlow()

    private val _selectedBookingTime = MutableStateFlow(SampleData.AvailableTimeSlots.first())
    val selectedBookingTime: StateFlow<String> = _selectedBookingTime.asStateFlow()

    private val _bookingNotes = MutableStateFlow("")
    val bookingNotes: StateFlow<String> = _bookingNotes.asStateFlow()

    private val _isBookingSubmitting = MutableStateFlow(false)
    val isBookingSubmitting: StateFlow<Boolean> = _isBookingSubmitting.asStateFlow()

    // Orders
    private val _userOrders = MutableStateFlow<List<Order>>(emptyList())
    val userOrders: StateFlow<List<Order>> = _userOrders.asStateFlow()

    private val _isPlacingOrder = MutableStateFlow(false)
    val isPlacingOrder: StateFlow<Boolean> = _isPlacingOrder.asStateFlow()

    // Notification / Toast banner
    private val _userFeedback = MutableStateFlow<String?>(null)
    val userFeedback: StateFlow<String?> = _userFeedback.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        val uid = _userProfile.value.uid
        viewModelScope.launch {
            _firebaseStatus.value = firebaseManager.getFirebaseStatus()
            val appointments = firebaseManager.getAppointments(uid)
            _userAppointments.value = appointments

            val orders = firebaseManager.getOrders(uid)
            _userOrders.value = orders

            val wishlist = firebaseManager.getWishlist(uid)
            _wishlistIds.value = wishlist
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectProduct(product: ProductItem?) {
        _selectedProduct.value = product
    }

    // --- Cart Actions ---
    fun addToCart(product: ProductItem, quantity: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(CartItem(product = product, quantity = quantity))
        }
        _cartItems.value = current
        _userFeedback.value = "Added '${product.name}' to your luxury bag"
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val updatedQty = current[index].quantity + delta
            if (updatedQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = updatedQty)
            }
            _cartItems.value = current
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun toggleGiftPackaging() {
        _giftPackaging.value = !_giftPackaging.value
    }

    fun applyPromoCode(code: String) {
        val clean = code.trim().uppercase()
        if (clean == "ASTER10" || clean == "VIPSEHER") {
            _appliedPromo.value = clean
            _promoDiscount.value = subtotal.value * 0.10
            _userFeedback.value = "10% Haute Privilège discount applied!"
        } else if (clean.isEmpty()) {
            _appliedPromo.value = null
            _promoDiscount.value = 0.0
        } else {
            _userFeedback.value = "Code '$clean' is invalid. Use VIPSEHER or ASTER10."
        }
    }

    fun placeOrder() {
        if (_cartItems.value.isEmpty()) return

        val uid = _userProfile.value.uid
        val username = _userProfile.value.username
        val orderItems = _cartItems.value.map {
            OrderItemSummary(
                productId = it.product.id,
                productName = it.product.name,
                quantity = it.quantity,
                unitPrice = it.product.price
            )
        }

        val order = Order(
            userId = uid,
            username = username,
            items = orderItems,
            subtotal = subtotal.value,
            giftPackaging = _giftPackaging.value,
            discount = _promoDiscount.value,
            total = grandTotal.value,
            status = "Processing in Atelier"
        )

        _isPlacingOrder.value = true
        viewModelScope.launch {
            val result = firebaseManager.placeOrder(order)
            result.fold(
                onSuccess = { savedOrder ->
                    _userOrders.value = listOf(savedOrder) + _userOrders.value
                    _cartItems.value = emptyList()
                    _promoDiscount.value = 0.0
                    _appliedPromo.value = null
                    _isPlacingOrder.value = false
                    _userFeedback.value = "Order placed! Tracking ID: ${savedOrder.trackingCode}"
                },
                onFailure = { err ->
                    _isPlacingOrder.value = false
                    _userFeedback.value = "Order failed: ${err.message}"
                }
            )
        }
    }

    // --- Wishlist Actions ---
    fun toggleWishlist(productId: String) {
        val uid = _userProfile.value.uid
        viewModelScope.launch {
            val updated = firebaseManager.toggleWishlist(uid, productId)
            _wishlistIds.value = updated
        }
    }

    // --- Appointment Booking ---
    fun selectBookingService(service: String) {
        _selectedBookingService.value = service
    }

    fun selectSpecialist(specialist: Specialist) {
        _selectedSpecialist.value = specialist
    }

    fun selectBookingDate(date: String) {
        _selectedBookingDate.value = date
    }

    fun selectBookingTime(time: String) {
        _selectedBookingTime.value = time
    }

    fun onBookingNotesChanged(notes: String) {
        _bookingNotes.value = notes
    }

    fun bookAppointment() {
        val uid = _userProfile.value.uid
        val username = _userProfile.value.username

        val appointment = Appointment(
            userId = uid,
            username = username,
            serviceName = _selectedBookingService.value,
            specialistName = _selectedSpecialist.value.name,
            date = _selectedBookingDate.value,
            timeSlot = _selectedBookingTime.value,
            notes = _bookingNotes.value,
            status = "Confirmed"
        )

        _isBookingSubmitting.value = true
        viewModelScope.launch {
            val result = firebaseManager.saveAppointment(appointment)
            result.fold(
                onSuccess = { saved ->
                    _userAppointments.value = listOf(saved) + _userAppointments.value
                    _bookingNotes.value = ""
                    _isBookingSubmitting.value = false
                    _userFeedback.value = "Appointment confirmed for ${saved.date} at ${saved.timeSlot}!"
                },
                onFailure = { err ->
                    _isBookingSubmitting.value = false
                    _userFeedback.value = "Booking failed: ${err.message}"
                }
            )
        }
    }

    // --- Profile Actions ---
    fun updateProfile(displayName: String, favoriteNotes: String, bio: String) {
        val current = _userProfile.value
        val updated = current.copy(
            displayName = displayName.ifEmpty { current.username },
            favoriteNotes = favoriteNotes,
            vipBio = bio
        )
        viewModelScope.launch {
            val result = firebaseManager.updateProfile(updated)
            result.onSuccess {
                _userProfile.value = it
                _userFeedback.value = "Profile updated in Firestore"
            }
        }
    }

    fun dismissFeedback() {
        _userFeedback.value = null
    }

    fun refreshFirebaseStatus() {
        _firebaseStatus.value = firebaseManager.getFirebaseStatus()
    }
}
