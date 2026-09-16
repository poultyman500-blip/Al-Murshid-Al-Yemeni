# SkyCast Weather Dashboard

A responsive weather dashboard powered by the public [Open-Meteo](https://open-meteo.com/) API. It includes city search, current conditions, a 24-hour forecast, a 7-day forecast, responsive cards, and accessible loading/error states.

## Run locally

No build step is required:

```bash
python3 -m http.server 8080
```

Open <http://localhost:8080> in your browser.

## API

Open-Meteo's geocoding and forecast endpoints are used directly from the browser and do not require an API key. The dashboard defaults to Sanaa and uses the browser's network connection to fetch current data.
