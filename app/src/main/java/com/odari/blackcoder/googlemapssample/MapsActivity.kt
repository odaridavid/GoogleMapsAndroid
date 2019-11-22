package com.odari.blackcoder.googlemapssample

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val TAG = this.javaClass.simpleName

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     *
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val currentLocation = LatLng(37.7510, -97.8220)
        val zoomLevel = 16f

        //Move Cameras
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel))
        //Add Markers
        map.addMarker(MarkerOptions().position(currentLocation).title("Home"))

        //Click Listeners
        setMapOnLongClick(map)
        setMapPoiClick(map)

        //Style Map
        setMapStyle(map)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_type_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.normal_map -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.terrain_map -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_map -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            R.id.satellite_map -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setMapOnLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latlng ->
            val snippet = "${latlng.latitude.toFloat()} ${latlng.longitude.toFloat()}"
            map.addMarker(
                MarkerOptions().position(latlng)
                    .title("Pinned")
                    .snippet(snippet)
            )
        }
    }

    private fun setMapPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions().position(poi.latLng)
                    .title(poi.name)
                    .snippet(poi.placeId)
            )
            poiMarker.showInfoWindow()
        }

    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "$e")
        }
    }
}

