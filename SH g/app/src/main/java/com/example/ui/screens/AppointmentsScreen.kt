package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.Appointment
import com.example.model.Specialist
import com.example.ui.components.MaisonPrimaryButton
import com.example.ui.theme.EmeraldLuxe
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.OnyxBorder
import com.example.ui.theme.OnyxCard
import com.example.ui.theme.OnyxSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.viewmodel.MaisonViewModel

@Composable
fun AppointmentsScreen(
    viewModel: MaisonViewModel,
    modifier: Modifier = Modifier
) {
    val services = listOf(
        "VIP Bespoke Perfume Crafting Session",
        "Seher Aloyon Couture Eye & Lash Architecture",
        "24K Gold Cellular Radiance Facial",
        "Private Royal Bridal Consultation"
    )

    val selectedService by viewModel.selectedBookingService.collectAsState()
    val selectedSpecialist by viewModel.selectedSpecialist.collectAsState()
    val selectedDate by viewModel.selectedBookingDate.collectAsState()
    val selectedTime by viewModel.selectedBookingTime.collectAsState()
    val notes by viewModel.bookingNotes.collectAsState()
    val isSubmitting by viewModel.isBookingSubmitting.collectAsState()
    val userAppointments by viewModel.userAppointments.collectAsState()

    val upcomingDates = listOf("Tomorrow", "In 2 Days", "In 3 Days", "Next Weekend")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // Title Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "ATELIER & SALON PRIVÉ",
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontSize = 10.sp
                ),
                color = GoldLight
            )
            Text(
                text = "Reserve VIP Appointment",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp
                ),
                color = TextPrimaryDark
            )
        }

        // Booking Form Card
        Card(
            colors = CardDefaults.cardColors(containerColor = OnyxCard),
            border = BorderStroke(1.dp, OnyxBorder),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Service Selector
                Text(
                    text = "1. SELECT BESPOKE SERVICE",
                    color = GoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                services.forEach { service ->
                    val isSelected = selectedService == service
                    Surface(
                        onClick = { viewModel.selectBookingService(service) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) OnyxSurface else Color.Transparent,
                        border = BorderStroke(1.dp, if (isSelected) GoldPrimary else OnyxBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = if (isSelected) GoldPrimary else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = service,
                                color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Specialist Selector
                Text(
                    text = "2. SELECT ARTISAN SPECIALIST",
                    color = GoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                SampleData.Specialists.forEach { specialist ->
                    val isSelected = selectedSpecialist.id == specialist.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) OnyxSurface else Color.Transparent
                        ),
                        border = BorderStroke(1.dp, if (isSelected) GoldPrimary else OnyxBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.selectSpecialist(specialist) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = specialist.name,
                                    color = TextPrimaryDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = specialist.title,
                                    color = GoldLight,
                                    fontSize = 11.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${specialist.rating}",
                                    color = GoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Time Slots
                Text(
                    text = "3. SELECT ATELIER TIME SLOT",
                    color = GoldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SampleData.AvailableTimeSlots.forEach { slot ->
                        val isSelected = selectedTime == slot
                        Surface(
                            onClick = { viewModel.selectBookingTime(slot) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) GoldPrimary else OnyxSurface,
                            border = BorderStroke(1.dp, if (isSelected) GoldPrimary else OnyxBorder)
                        ) {
                            Text(
                                text = slot,
                                color = if (isSelected) ObsidianBlack else TextSecondaryDark,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Special Requests Field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { viewModel.onBookingNotesChanged(it) },
                    label = { Text("Special Requests / Fragrance Preferences", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Prefer amber accords, bridal eye veil styling...", fontSize = 11.sp, color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = OnyxBorder,
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark,
                        focusedContainerColor = OnyxSurface,
                        unfocusedContainerColor = OnyxSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Confirm Reservation Button
                MaisonPrimaryButton(
                    text = "Confirm Atelier Reservation",
                    onClick = { viewModel.bookAppointment() },
                    isLoading = isSubmitting,
                    icon = Icons.Default.CalendarToday,
                    testTag = "confirm_booking_button"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Existing Appointments Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Your Reserved Atelier Sessions",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                ),
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (userAppointments.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = OnyxSurface),
                    border = BorderStroke(0.5.dp, OnyxBorder),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No appointments reserved yet. Book your first VIP session above.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                userAppointments.forEach { appointment ->
                    AppointmentCard(appointment = appointment)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    Card(
        colors = CardDefaults.cardColors(containerColor = OnyxCard),
        border = BorderStroke(1.dp, OnyxBorder),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.serviceName,
                    color = TextPrimaryDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldLuxe.copy(alpha = 0.2f))
                        .border(0.5.dp, EmeraldLuxe, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = appointment.status.uppercase(),
                        color = EmeraldLuxe,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Specialist: ${appointment.specialistName}",
                    color = GoldLight,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${appointment.date} • ${appointment.timeSlot}",
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )
            }

            if (appointment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Notes: \"${appointment.notes}\"",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}
