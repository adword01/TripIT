package com.example.tripit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.sdk.maps.Image

class PredictionAdapter (private val data: List<Preditction>) :
    RecyclerView.Adapter<PredictionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prediction_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        // Bind data to the views in the list item layout
        holder.PredictedPlace.text = item.place_name
        holder.predictedDisctrict.text = item.city
//        holder.ratings.text = item.description

        getDrawableIdForPlace(item.place_name.toString(),placeItems)?.let {
            holder.ImagePlace.setBackgroundResource(
                it
            )
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val PredictedPlace: TextView = itemView.findViewById(R.id.PredictedPlace)
        val predictedDisctrict: TextView = itemView.findViewById(R.id.predictedDisctrict)
//        val ratings: TextView = itemView.findViewById(R.id.Rating)
        val ImagePlace: ImageView = itemView.findViewById(R.id.PlaceImage)
    }


    val placeItems = listOf(
        PlaceItem(R.drawable.andretta, "Andretta"),
        PlaceItem(R.drawable.awahdevi, "Awahdevi"),
        PlaceItem(R.drawable.baba_balak_nath_temple, "Baba Balak Nath Temple"),
        PlaceItem(R.drawable.baba_garib_nath_temple, "Baba Garib Nath Temple"),
        PlaceItem(R.drawable.bahardurpur_fort, "Bahardurpur Fort"),
        PlaceItem(R.drawable.bangana_lathain_piplu, "Bangana Lathain Piplu"),
        PlaceItem(R.drawable.barot_valley, "Barot Valley"),
        PlaceItem(R.drawable.bhagsu_nath_temple, "Bhagsu Nath Temple"),
        PlaceItem(R.drawable.bhakra_dam, "Bhakra Dam"),
        PlaceItem(R.drawable.bhuru_singh_museum, "Bhuru Singh Museum"),
        PlaceItem(R.drawable.bijli_mahadev_temple, "Bijli Mahadev Temple"),
        PlaceItem(R.drawable.bil_kaleshwar, "Bil Kaleshwar"),
        PlaceItem(R.drawable.bon_monastery, "Bon Monastery"),
        PlaceItem(R.drawable.chamba_chaugan, "Chamba Chaugan"),
        PlaceItem(R.drawable.chamba_palace, "Chamba Palace"),
        PlaceItem(R.drawable.chamera_lake, "Chamera Lake"),
        PlaceItem(R.drawable.champavati, "Champavati"),
        PlaceItem(R.drawable.chamunda_devi, "Chamunda Devi"),
        PlaceItem(R.drawable.chandrataal_lake, "Chandrataal Lake"),
        PlaceItem(R.drawable.changar, "Changar"),
        PlaceItem(R.drawable.chintapurni_temple, "Chintpurni Temple"),
        PlaceItem(R.drawable.chitkul_village, "Chitkul Village"),
        PlaceItem(R.drawable.christ_church, "Christ Church"),
        PlaceItem(R.drawable.dagshai, "Dagshai"),
        PlaceItem(R.drawable.deoli_fish_farm, "Deoli Fish Farm"),
        PlaceItem(R.drawable.deosidh_temple, "Deosidh Temple"),
        PlaceItem(R.drawable.dera_baba_barbhag, "Dera Baba Barbhag"),
        PlaceItem(R.drawable.dera_baba_rudru, "Dera Baba Rudru"),
        PlaceItem(R.drawable.dera_sant_pura, "Dera Sant Pura"),
        PlaceItem(R.drawable.dhankar_monastery, "Dhankar Monastery"),
        PlaceItem(R.drawable.dharamshala_mahanta, "Dharamshala Mahanta"),
        PlaceItem(R.drawable.dharamshala, "Dharamshala"),
        PlaceItem(R.drawable.gobind_sagar_lake, "Gobind Sagar Lake"),
        PlaceItem(R.drawable.great_himalayan_national_park, "Great Himalayan National Park"),
        PlaceItem(R.drawable.gurudwara_sahib_amb, "Gurudwara Sahib Amb"),
        PlaceItem(R.drawable.hamirpur_stadium, "Hamirpur Stadium"),
        PlaceItem(R.drawable.himalayan_bird_park, "Himalayan Bird Park"),
        PlaceItem(R.drawable.hippie_point, "Hippie Point"),
        PlaceItem(R.drawable.iias, "IIAS"),
        PlaceItem(R.drawable.jakhoo_temple, "Jakhoo Temple"),
        PlaceItem(R.drawable.janjheli, "Janhjeli"),
        PlaceItem(R.drawable.jatoli_shiv_temple, "Jatoli Shiv Temple"),
        PlaceItem(R.drawable.kalatop, "Kalatop"),
        PlaceItem(R.drawable.kalpa, "Kalpa"),
        PlaceItem(R.drawable.kamru_fort, "Kamru Fort"),
        PlaceItem(R.drawable.kamrunag_lake, "Kamrunag Lake"),
        PlaceItem(R.drawable.kandraur_bridge, "Kandraur Bridge"),
        PlaceItem(R.drawable.kangra_fort, "Kangra Fort"),
        PlaceItem(R.drawable.kangra_valley, "Kangra Valley"),
        PlaceItem(R.drawable.kareri_lake_trek, "Kareri Lake Trek"),
        PlaceItem(R.drawable.karol_tibba, "Karol Tibba"),
        PlaceItem(R.drawable.kasauli, "Kasauli"),
        PlaceItem(R.drawable.kasol, "Kasol"),
        PlaceItem(R.drawable.katghar_temple, "Kathgarh Temple"),
        PlaceItem(R.drawable.kaza, "Kaza"),
        PlaceItem(R.drawable.key_monastery, "Key Monastery"),
        PlaceItem(R.drawable.khajjar, "Khajjar"),
        PlaceItem(R.drawable.kiala_forest, "Kiala Forest"),
        PlaceItem(R.drawable.kibber_village, "Kibber Village"),
        PlaceItem(R.drawable.kinnaur_kailaish, "Kinnaur Kailaish"),
        PlaceItem(R.drawable.kothi, "Kothi"),
        PlaceItem(R.drawable.kufri, "Kufri"),
        PlaceItem(R.drawable.kunzum_pass, "Kunzum Pass"),
        PlaceItem(R.drawable.kuthar_fort, "Kuthar Fort"),
        PlaceItem(R.drawable.langza, "Langza"),
        PlaceItem(R.drawable.laxmi_narayan_temple, "Laxmi Narayan Temple"),
        PlaceItem(R.drawable.losar_khas, "Losar Khas"),
        PlaceItem(R.drawable.maharana_pratap_sagar, "Maharana Pratap Sagar"),
        PlaceItem(R.drawable.malana, "Malana"),
        PlaceItem(R.drawable.mall_road, "Mall Road"),
        PlaceItem(R.drawable.manali_sanctury, "Manali Sanctuary"),
        PlaceItem(R.drawable.mandi_palace, "Mandi Palace"),
        PlaceItem(R.drawable.manikaran_sahib, "Manikaran Sahib"),
        PlaceItem(R.drawable.manimahesh, "Manimahesh Lake"),
        PlaceItem(R.drawable.markandeya_temple, "Markandeya Temple"),
        PlaceItem(R.drawable.masroor_temple, "Masroor Temple"),
        PlaceItem(R.drawable.mohan_shakti_heritage_park, "Mohan Shakti Heritage Park"),
        PlaceItem(R.drawable.nadaun, "Nadaun"),
        PlaceItem(R.drawable.naggar_castle, "Naggar Castle"),
        PlaceItem(R.drawable.naina_devi, "Naina Devi Temple"),
        PlaceItem(R.drawable.nako_lake, "Nako Lake"),
        PlaceItem(R.drawable.nhutnath_temple, "Nhutnath Temple"),
        PlaceItem(R.drawable.pandoh_dam, "Pandoh Dam"),
        PlaceItem(R.drawable.pangi, "Pangi"),
        PlaceItem(R.drawable.panj_teerthi, "Panj Teerthi"),
        PlaceItem(R.drawable.parashar_lake, "Prashar Lake"),
        PlaceItem(R.drawable.pin_valley, "Pin Valley"),
        PlaceItem(R.drawable.pong_dam, "Pong Dam"),
        PlaceItem(R.drawable.raghunath_temple, "Raghunath Temple"),
        PlaceItem(R.drawable.reckong_pao, "Reckong Pao"),
        PlaceItem(R.drawable.rewalsar_lake, "Rewalsar Lake"),
        PlaceItem(R.drawable.ribba_village, "Ribba Village"),
        PlaceItem(R.drawable.roghi, "Roghi"),
        PlaceItem(R.drawable.sangla_valley, "Sangla Valley"),
        PlaceItem(R.drawable.sheetla_mata_temple, "Sheetla Mata Temple"),
        PlaceItem(R.drawable.shikari_devi_temple, "Shikari Devi Temple"),
        PlaceItem(R.drawable.shimla_state_mesuem, "Shimla State Museum"),
        PlaceItem(R.drawable.shiv_temple, "Shiv Temple"),
        PlaceItem(R.drawable.shoolani_temple, "Shoolani Temple"),
        PlaceItem(R.drawable.sirmaur_palace, "Sirmaur Palace"),
        PlaceItem(R.drawable.sirmaur_park, "Sirmaur Park"),
        PlaceItem(R.drawable.solan_brewery, "Solan Brewery"),
        PlaceItem(R.drawable.solang, "Solang"),
        PlaceItem(R.drawable.sujanpur_tira, "Sujanpur Tira"),
        PlaceItem(R.drawable.sundar_nagar_lake, "Sunder Nagar Lake"),
        PlaceItem(R.drawable.swarghat, "Swarghat"),
        PlaceItem(R.drawable.tabo_monastery, "Tabo Monastery"),
        PlaceItem(R.drawable.talai, "Talai"),
        PlaceItem(R.drawable.tara_devi_temple, "Tara Devi Temple"),
        PlaceItem(R.drawable.tauni_devi_temple, "Tauni Devi Temple"),
        PlaceItem(R.drawable.the_ridge, "The Ridge"),
        PlaceItem(R.drawable.tirthan_valley, "Tirthan Valley"),
        PlaceItem(R.drawable.triund_trek, "Triund Trek"),
        PlaceItem(R.drawable.una_fort, "Una Fort")
    )

    fun getDrawableIdForPlace(placeName: String, placeItems: List<PlaceItem>): Int? {
        // Convert the placeName to lowercase for case-insensitive comparison
        val lowercasePlaceName = placeName.toLowerCase()

        // Iterate over the list and find the matching PlaceItem
        val matchingPlace = placeItems.find {
            placeName == it.name
        }

        // Return the corresponding Drawable resource ID or a default placeholder ID
         return matchingPlace?.resourceId
    }
}