package com.example.data

import com.example.model.ProductItem
import com.example.model.Specialist

object SampleData {
    val Categories = listOf(
        "All Collections",
        "Haute Parfumerie",
        "Eye Couture & Lashes",
        "Radiance Skincare",
        "Lounge & Treatments"
    )

    val Products = listOf(
        ProductItem(
            id = "perf_01",
            name = "Oud Royal L'Étoile",
            subtitle = "Extrait de Parfum Absolu",
            category = "Haute Parfumerie",
            price = 380.0,
            rating = 4.95f,
            reviewCount = 128,
            description = "A hypnotic masterwork marrying centuries-old wild Cambodian Agarwood with velvety Bulgarian rose and spun golden amber.",
            topNotes = "Saffron Gold, Bergamot, Pink Peppercorn",
            heartNotes = "Bulgarian Rose Damascena, Night-blooming Jasmine",
            baseNotes = "Wild Cambodian Oud, Ambergris, Bourbon Vanilla",
            volume = "100ml / 3.4 fl oz",
            badge = "Masterpiece",
            iconType = "perfume"
        ),
        ProductItem(
            id = "eye_01",
            name = "Seher Al Oyoun 3D Silk Lash Couture",
            subtitle = "Handcrafted Mink & Silk Veil",
            category = "Eye Couture & Lashes",
            price = 95.0,
            rating = 4.98f,
            reviewCount = 210,
            description = "The iconic signature of Seher Aloyon. Feather-light, multidimensional lash strands designed to mesmerize with cat-eye elegance.",
            topNotes = "Ultra-fine Silk Fibers",
            heartNotes = "Flexible Invisible Band",
            baseNotes = "Reusable up to 35 VIP occasions",
            volume = "1 Pair Luxury Lacquered Case",
            badge = "Iconic Seher Aloyon",
            iconType = "eye"
        ),
        ProductItem(
            id = "perf_02",
            name = "Rose Sublime Impériale",
            subtitle = "Eau de Parfum Légendaire",
            category = "Haute Parfumerie",
            price = 320.0,
            rating = 4.91f,
            reviewCount = 94,
            description = "Crisp morning dew over centifolia rose petals harvested in Grasse, warmed with honeyed patchouli and white musk.",
            topNotes = "Taif Rose, Lychee, Mandarin Sparkle",
            heartNotes = "Grasse Centifolia Rose, Magnolia, Violet Leaf",
            baseNotes = "Indonesian Patchouli, White Cedarwood, Cashmeran",
            volume = "75ml / 2.5 fl oz",
            badge = "Bestseller",
            iconType = "perfume"
        ),
        ProductItem(
            id = "eye_02",
            name = "Kohl Noir & 24K Gold Eye Infusion",
            subtitle = "Waterproof Mineral Kohl + Peptide Serum",
            category = "Eye Couture & Lashes",
            price = 78.0,
            rating = 4.88f,
            reviewCount = 142,
            description = "Traditional Arabian kajal re-engineered with micro-milled gold flakes and restorative peptides for dramatic depth and lash vitality.",
            topNotes = "Jet Black Carbon Mineral Pigment",
            heartNotes = "Collagen Peptides & Argan Nectar",
            baseNotes = "Smudge-proof 24hr Wear",
            volume = "1.5g Precision Applicator",
            badge = "Cult Favorite",
            iconType = "eye"
        ),
        ProductItem(
            id = "skin_01",
            name = "Or Impérial Cellular Radiance Crème",
            subtitle = "24K Gold & Caviar Restorative Complex",
            category = "Radiance Skincare",
            price = 450.0,
            rating = 4.97f,
            reviewCount = 76,
            description = "A transformative, decadent cream rich in marine caviar extract and pure gold nanoparticles to smooth, illuminate, and firm the skin.",
            topNotes = "Pure 24 Karat Micro-Gold Flakes",
            heartNotes = "French Beluga Caviar Extract",
            baseNotes = "Hyaluronic Matrix & Botanical Peptides",
            volume = "50ml Luxury Crystal Jar",
            badge = "Ultra Luxe",
            iconType = "skincare"
        ),
        ProductItem(
            id = "perf_03",
            name = "Ambre Mystique & Encens",
            subtitle = "Extrait de Parfum Nocturne",
            category = "Haute Parfumerie",
            price = 345.0,
            rating = 4.93f,
            reviewCount = 68,
            description = "A warm, enigmatic aura of smoky Royal Omani Frankincense entwined with dark labdanum, cinnamon bark, and creamy tonka bean.",
            topNotes = "Royal Green Frankincense, Cardamom Seed",
            heartNotes = "Smoked Myrrh, Cinnamon Bark, Orris Root",
            baseNotes = "Dark Amber, Roasted Tonka, Benzoin Tears",
            volume = "100ml / 3.4 fl oz",
            badge = "Private Reserve",
            iconType = "perfume"
        ),
        ProductItem(
            id = "treat_01",
            name = "Aster Signature Lounge Experience",
            subtitle = "Private Bespoke Atelier Treatment",
            category = "Lounge & Treatments",
            price = 280.0,
            rating = 5.0f,
            reviewCount = 52,
            description = "An immersive 90-minute private consultation including personalized fragrance profiling, eye couture sculpting, and champagne hospitality.",
            topNotes = "Personalized Olfactory Diagnostic",
            heartNotes = "Custom Eye Silhouette & Facial Massage",
            baseNotes = "Complimentary 15ml Custom Flacon",
            volume = "90 Minutes Session",
            badge = "VIP Atelier",
            iconType = "treatment"
        ),
        ProductItem(
            id = "eye_03",
            name = "Sovereign Diamond Lash Curler & Case",
            subtitle = "Ergonomic Gold-Plated Precision Tool",
            category = "Eye Couture & Lashes",
            price = 65.0,
            rating = 4.89f,
            reviewCount = 88,
            description = "Engineered with Japanese precision springs and coated in 18K gold finish. Delivers instant upward sweep without pinching.",
            topNotes = "18K Gold Plated Stainless Steel",
            heartNotes = "Hypoallergenic Silicone Pads",
            baseNotes = "Velvet Carry Pouch Included",
            volume = "Collector's Edition",
            badge = "Limited",
            iconType = "eye"
        )
    )

    val Specialists = listOf(
        Specialist(
            id = "spec_01",
            name = "Mme. Camille Laurent",
            title = "Master Perfumer & Nose",
            specialty = "Haute Fragrance Formulation & Custom Blends",
            rating = 4.99f,
            experience = "16 yrs"
        ),
        Specialist(
            id = "spec_02",
            name = "Soraya Al-Hashemi",
            title = "Head of Seher Aloyon Eye Couture",
            specialty = "Eye Architecture, Micro-Lashes & Royal Bridal Styling",
            rating = 4.98f,
            experience = "11 yrs"
        ),
        Specialist(
            id = "spec_03",
            name = "Dr. Antoine Mercier",
            title = "Aesthetic Dermal Specialist",
            specialty = "24K Gold Cellular Rejuvenation & Radiance Therapy",
            rating = 4.95f,
            experience = "14 yrs"
        )
    )

    val AvailableTimeSlots = listOf(
        "10:00 AM - 11:30 AM",
        "12:00 PM - 01:30 PM",
        "02:30 PM - 04:00 PM",
        "04:30 PM - 06:00 PM",
        "07:00 PM - 08:30 PM"
    )
}
