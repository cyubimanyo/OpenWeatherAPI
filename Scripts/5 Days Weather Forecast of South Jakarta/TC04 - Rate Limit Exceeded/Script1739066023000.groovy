import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import groovy.json.JsonOutput

// Data : South Jakarta
// Latitude : 6.232970
// Longitude : 106.834572

def filePath = 'Data Files/OpenWeather.xlsx'

def readExcelData(filePath) {
    def data = [:]

    FileInputStream file = new FileInputStream(new File(filePath))
    Workbook workbook = new XSSFWorkbook(file)
    Sheet sheet = workbook.getSheet("Valid Location")

    Row row = sheet.getRow(1)

    data["cityName"] = row.getCell(1).getStringCellValue()
    println("☑ City Name: " + data["cityName"])

    Cell latitudeCell = row.getCell(2)
    if (latitudeCell.getCellType() == CellType.NUMERIC) {
        data["latitude"] = String.valueOf(latitudeCell.getNumericCellValue()) // Convert to String
    } else {
        data["latitude"] = latitudeCell.getStringCellValue()
    }
    println("☑ Latitude: " + data["latitude"])

    Cell longitudeCell = row.getCell(3)
    if (longitudeCell.getCellType() == CellType.NUMERIC) {
        data["longitude"] = String.valueOf(longitudeCell.getNumericCellValue()) // Convert to String
    } else {
        data["longitude"] = longitudeCell.getStringCellValue()
    }
    println("☑ Longitude: " + data["longitude"])

    workbook.close()
    file.close()
    return data
}

def excelData = readExcelData(filePath)
def requestBody = [
    "cod": "200",
    "message": 0,
    "cnt": 40,
    "list": [
        [
            "dt": 1678886400,
            "main": [
                "temp": 298.15,
                "feels_like": 300.15,
                "temp_min": 297.15,
                "temp_max": 299.15,
                "pressure": 1012,
                "humidity": 78
            ],
            "weather": [
                [
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                ]
            ],
            "wind": [
                "speed": 3.6,
                "deg": 180
            ],
            "clouds": [
                "all": 0
            ],
            "visibility": 10000,
            "pop": 0,
            "dt_txt": "2023-03-15 12:00:00"
        ]
    ],
    "city": [
        "id": 123456,
        "name": excelData["cityName"] ?: "",
        "coord": [
            "lat": excelData["latitude"] ?: "",
            "lon": excelData["longitude"] ?: ""
        ],
        "country": "ID",
        "timezone": 25200,
        "sunrise": 1678843200,
        "sunset": 1678886400
    ]
]

def jsonRequestBody = JsonOutput.prettyPrint(JsonOutput.toJson(requestBody))
println(jsonRequestBody)

// Request
def requestObject = findTestObject('5 Days Weather Forecast/TC01 - Valid API Response (200 OK)', 
    [
        ('latitude') : excelData["latitude"], 
        ('longitude') : excelData["longitude"]
    ]
)

println("☑ API Endpoint: " + requestObject.getRestUrl())
println("☑ Request Headers: " + requestObject.getHttpHeaderProperties())
println("☑ Request Body: " + jsonRequestBody)

// **Rate Limit Exceeded Scenario**
int maxRequests = 1100
int requestCount = 0
boolean rateLimitExceeded = false

while (requestCount < maxRequests) {
	KeywordUtil.logInfo("Sending request #" + (requestCount + 1))

	def response = WS.sendRequest(requestObject)
	int statusCode = response.getStatusCode()
	KeywordUtil.logInfo("Response Code: " + statusCode)

	// Check if API returned Rate Limit Exceeded (HTTP 429)
	if (statusCode == 429) {
		KeywordUtil.logInfo("⚠ Rate Limit Exceeded at request #" + (requestCount + 1))
		rateLimitExceeded = true
		break // Stop further requests
	}

	requestCount++
}

if (rateLimitExceeded) {
	KeywordUtil.logInfo("✅ Test Passed: API Rate Limit Exceeded as expected.")
} else {
	KeywordUtil.logInfo("❌ Test Failed: Rate Limit was not exceeded.")
}

// Response
def response = WS.sendRequest(requestObject)
def responseBody = response.getResponseBodyContent()
def responseMap = [
	"statusCode": response.getStatusCode(),
	"responseBody": new JsonSlurper().parseText(responseBody)
]
def prettyResponse = JsonOutput.prettyPrint(JsonOutput.toJson(responseMap))

println("☑ API Result : " + prettyResponse)
