package com.risk.narrative.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GlaxWeatherResponse {

  @JsonProperty("speed_unit")
  public String speed_unit;
  @JsonProperty("temperature_unit")
  public String temperature_unit;
  @JsonProperty("temperature")
  public double temperature;
  @JsonProperty("humidity")
  public int humidity;
  @JsonProperty("weather")
  public String weather;
  @JsonProperty("icon")
  public String icon;
  @JsonProperty("city")
  public String city;
  @JsonProperty("message")
  public String message;
  @JsonProperty("credits")
  public String credits;
  @JsonProperty("time")
  public Date time;
  @JsonProperty("timestamp_utc")
  public int timestamp_utc;
  @JsonProperty("wind_speed")
  public int wind_speed;
}
