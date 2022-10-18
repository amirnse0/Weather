package com.example.weather.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.MainApp
import com.example.weather.data.domain.WeatherRepository
import com.example.weather.data.domain.remote.api.ApiConfigurations
import com.example.weather.data.model.local.ForecastWithHours
import com.example.weather.data.model.local.WeatherAndCurrentWeather
import com.example.weather.data.model.local.WeatherWithForecasts
import com.example.weather.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val queryMap = mutableMapOf(
        "key" to ApiConfigurations.API_KEY,
        "days" to "7",
        "aqi" to "no",
        "alerts" to "no"
    )

    private suspend fun oldDataAndError(isDataExistInLocal: Boolean, errorMessage: String){
        if (isDataExistInLocal){
            TODO("toast that something is wrong and show old data")
        }else{
            TODO("Show the error")
        }
    }

    suspend fun mainFunction(query: Map<String, String>) {
        viewModelScope.launch {
            val isDataExistInLocal = weatherRepository.isDataExistInLocal()
            MainApp.isConnected().collect {
                if (it) {
                    try {
                        val data = weatherRepository.fetch(query)
                        weatherRepository.saveDataToLocal(data)


                    } catch (e:Exception){
                        oldDataAndError(isDataExistInLocal, e.message.toString())
                    }
                } else {
                    oldDataAndError(isDataExistInLocal, "")
                    TODO("Toast that no internet connection")
                }
            }
        }
    }

    private val _weatherAndCurrentWeatherStateFlow: MutableStateFlow<ResultOf<WeatherAndCurrentWeather>> =
        MutableStateFlow(ResultOf.Loading)
    val weatherAndCurrentWeatherStateFlow = _weatherAndCurrentWeatherStateFlow
    private val _weatherWithForecastsStateFlow: MutableStateFlow<ResultOf<List<WeatherWithForecasts>>> =
        MutableStateFlow(ResultOf.Loading)
    val weatherWithForecastsStateFlow = _weatherWithForecastsStateFlow
    private val _forecastWithHoursStateFlow: MutableStateFlow<ResultOf<List<ForecastWithHours>>> =
        MutableStateFlow(ResultOf.Loading)
    val forecastWithHoursStateFlow = _forecastWithHoursStateFlow

}
