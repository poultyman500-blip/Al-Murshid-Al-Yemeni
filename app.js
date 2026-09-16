const API = 'https://api.open-meteo.com/v1/forecast';
const GEO_API = 'https://geocoding-api.open-meteo.com/v1/search';
const state = { unit: 'celsius', location: null, weather: null };
const $ = (id) => document.getElementById(id);
const weatherCodes = {
  0: ['Clear sky', '☀️'], 1: ['Mainly clear', '🌤️'], 2: ['Partly cloudy', '⛅'], 3: ['Overcast', '☁️'],
  45: ['Fog', '🌫️'], 48: ['Rime fog', '🌫️'], 51: ['Light drizzle', '🌦️'], 53: ['Drizzle', '🌦️'], 55: ['Heavy drizzle', '🌧️'],
  61: ['Light rain', '🌦️'], 63: ['Rain', '🌧️'], 65: ['Heavy rain', '🌧️'], 71: ['Light snow', '🌨️'], 73: ['Snow', '❄️'], 75: ['Heavy snow', '❄️'],
  80: ['Rain showers', '🌦️'], 81: ['Showers', '🌧️'], 82: ['Heavy showers', '⛈️'], 95: ['Thunderstorm', '⛈️'], 96: ['Storm with hail', '⛈️'], 99: ['Storm with hail', '⛈️']
};
const iconFor = (code) => weatherCodes[code] || ['Unknown', '🌡️'];
const formatTemp = (value) => `${Math.round(value)}°`;
const formatTime = (iso) => new Intl.DateTimeFormat(undefined, { hour: 'numeric', minute: '2-digit' }).format(new Date(iso));
const dayName = (iso, index) => index === 0 ? 'Today' : new Intl.DateTimeFormat(undefined, { weekday: 'long' }).format(new Date(`${iso}T12:00:00`));

async function geocode(query) {
  const response = await fetch(`${GEO_API}?name=${encodeURIComponent(query)}&count=1&language=en&format=json`);
  if (!response.ok) throw new Error('Could not search for that city.');
  const data = await response.json();
  if (!data.results?.length) throw new Error('City not found. Try another search.');
  return data.results[0];
}
async function fetchWeather(location) {
  const params = new URLSearchParams({ latitude: location.latitude, longitude: location.longitude, timezone: 'auto', forecast_days: 7,
    current: 'temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,wind_speed_10m,wind_direction_10m',
    hourly: 'temperature_2m,weather_code,precipitation_probability', daily: 'weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,sunrise,sunset,wind_speed_10m_max' });
  const response = await fetch(`${API}?${params}`);
  if (!response.ok) throw new Error('Weather service is unavailable right now.');
  return response.json();
}
function showError(message) { $('error').textContent = message; $('error').hidden = false; $('loading').hidden = true; }
function render(location, weather) {
  state.location = location; state.weather = weather; $('error').hidden = true; $('loading').hidden = true; $('dashboard').hidden = false;
  const c = weather.current, [condition, icon] = iconFor(c.weather_code);
  $('location-name').textContent = `${location.name}, ${location.country_code || ''}`;
  $('coordinates').textContent = `${weather.timezone} · ${Number(location.latitude).toFixed(2)}, ${Number(location.longitude).toFixed(2)}`;
  $('current-icon').textContent = icon; $('current-temp').textContent = Math.round(c.temperature_2m);
  $('current-condition').textContent = condition; $('feels-like').textContent = formatTemp(c.apparent_temperature); $('humidity').textContent = `${c.relative_humidity_2m}%`; $('wind').textContent = `${Math.round(c.wind_speed_10m)} km/h`; $('wind-direction').textContent = `${Math.round(c.wind_direction_10m)}°`;
  $('sunrise').textContent = formatTime(weather.daily.sunrise[0]); $('sunset').textContent = formatTime(weather.daily.sunset[0]); $('rain').textContent = `${weather.daily.precipitation_probability_max[0]}%`;
  $('updated-at').textContent = `Updated ${formatTime(c.time)}`;
  const start = weather.hourly.time.findIndex((time) => time >= c.time); const hourly = $('hourly'); hourly.innerHTML = '';
  for (let i = Math.max(0, start); i < Math.min(weather.hourly.time.length, Math.max(0, start) + 24); i++) { const [label, emoji] = iconFor(weather.hourly.weather_code[i]); hourly.insertAdjacentHTML('beforeend', `<div class="hour"><time>${formatTime(weather.hourly.time[i])}</time><span class="weather-icon" title="${label}">${emoji}</span><strong>${formatTemp(weather.hourly.temperature_2m[i])}</strong></div>`); }
  const daily = $('daily'); daily.innerHTML = ''; weather.daily.time.forEach((date, i) => { const [, emoji] = iconFor(weather.daily.weather_code[i]); daily.insertAdjacentHTML('beforeend', `<div class="day"><span class="day-name">${dayName(date, i)}</span><span class="day-condition"><span>${emoji}</span>${weatherCodes[weather.daily.weather_code[i]]?.[0] || 'Weather'}</span><span class="temperatures"><span class="high">${formatTemp(weather.daily.temperature_2m_max[i])}</span><span class="low">${formatTemp(weather.daily.temperature_2m_min[i])}</span></span><span class="rain-pill">${weather.daily.precipitation_probability_max[i]}% rain</span></div>`); });
}
async function load(query) { try { $('dashboard').hidden = true; $('error').hidden = true; $('loading').hidden = false; const location = await geocode(query); const weather = await fetchWeather(location); render(location, weather); } catch (error) { showError(error.message); } }
$('search-form').addEventListener('submit', (event) => { event.preventDefault(); const value = $('city-input').value.trim(); if (value) load(value); });
$('unit-toggle').addEventListener('click', () => { state.unit = state.unit === 'celsius' ? 'fahrenheit' : 'celsius'; $('unit-toggle').textContent = state.unit === 'celsius' ? '°C' : '°F'; });
load('Sanaa');
