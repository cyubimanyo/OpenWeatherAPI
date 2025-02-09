# API Automation - Katalon Studio - OpenWeather API

Repositori ini berisi API Automation yang dikembangkan menggunakan **Katalon Studio**. API Automation ini melakukan pengujian yang terkait dengan informasi cuaca dan polusi udara dari opensource OpenWeather API, serta dirancang untuk berinteraksi dengan API nyata menggunakan data yang disediakan dalam file Excel.

## Fitur
- Automation script ini menggunakan **data binding Excel**, di mana data input API diambil dari file `Data Files/OpenWeather.xlsx`.
- Kamu dapat dengan mudah memperbarui data dengan memodifikasi nilai di kolom-kolom Excel berikut:
    - **Location** (Lokasi)
    - **Latitude** (Lintang)
    - **Longitude** (Bujur)

## Test Cases

API Automation ini mencakup 4 Test Cases :

1. **Valid API Response (200 OK)**  
   - Memastikan API mengembalikan status **200 OK** saat data yang valid diberikan.

2. **Invalid API Key**  
   - Menguji API ketika API Key yang tidak valid digunakan, mengharapkan respons error status code 401.

3. **Invalid Latitude and Longitude**  
   - Memverifikasi bagaimana API merespons ketika nilai lintang dan bujur yang salah atau di luar batas diberikan.

4. **Rate Limit Exceeded**  
   - Mensimulasikan **1100 permintaan** ke API untuk menguji batasan kecepatan (rate limit) dan memastikan API merespons dengan benar ketika batasnya terlampaui. (Expected Result : Status Code 429)

## Cara Memodifikasi Test Cases

Jika kamu ingin mengubah data input yang digunakan dalam pengujian:

1. Buka file Excel yang terletak di `Data Files/OpenWeather.xlsx`.
2. Modifikasi nilai pada kolom berikut:
   - **Location** (misalnya: South Jakarta)
   - **Latitude** (nilai numerik lintang)
   - **Longitude** (nilai numerik bujur)

Setelah perubahan dilakukan, script API Automation akan mengikat data baru tersebut dan menggunakannya pada panggilan API berikutnya.

## Laporan Uji

Laporan **PDF** terbaru yang dihasilkan dari uji automasi ini meliputi:

- **[PDF 1 - 5 Days Weather Forecast of South Jakarta](https://drive.google.com/file/d/1MWnY6OE0PcZYcOLsZm8xzyiq0tiqq5YO/view?usp=sharing)**  
  Laporan ini berisi perkiraan cuaca selama 5 hari untuk Jakarta Selatan yang diambil dari API. (Latest Run Test Suites : 9 Februari 2025)

- **[PDF 2 - Current Air Pollution of South Jakarta](https://drive.google.com/file/d/1hoz8YiujWO5rQV-S8fCukK3PKqNA63cE/view?usp=sharing)**  
  Laporan ini memberikan data mengenai tingkat polusi udara saat ini di Jakarta Selatan yang diambil dari API. (Latest Run Test Suites : 9 Februari 2025)

## Cara Menjalankan API Automation
Clone repositori ini ke mesin lokal kamu:
   ```bash
   [git clone https://github.com/username-anda/nama-repositori.git](https://github.com/cyubimanyo/OpenWeatherAPI.git)
