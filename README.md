# DSG Coding Challenge

### Purpose of the coding challenge
Help us understand how you think about designing, building, and delivering high quality mobile applications. After
completing the requirements detailed below, we will expect you to walk us through your design choices, architectural
decisions, and overall development process.
<br>
<br>

### Summary
Your assignment is to write a focused native mobile app which contains the features described below and adheres to the
UX design comps provided.
The primary goal is to utilize public API endpoints to retrieve data for the user to view, details provided at the end of this
document. The data returned from these endpoints should be used to power features described in the requirements
listed and presented to the user with UX matching the look-and-feel shown in the design comps.
<br>
<br>

### Guidelines
- A working app that meets the requirements below
  - No unfinished work will be accepted
- Submission
  - Via zip of the project
    - Clean the project
    - Send a link to a shared folder (such as Google Drive/Drop Box...) to the email address the
challenge was received
- Architecture
  - A discussion point of this application will be around architecture so make sure to use some architectural
structure
- Unit tests
  - These will also be a discussion point
  - No need for UI tests but unit tests are a must
- AI
  - This is designed to demonstrate your knowledge of development so please refrain from using AI to
generate code here
- Android Specific
  - Kotlin
  - Usage of Compose AND XML views
  - Demonstrating knowledge of these are important
    - Coroutines
    - Dependency Injection
    - LiveData or Flows
    - Retrofit
<br>

### Requirements
**GIVEN**: I have not accepted location permission\
**WHEN**: I view the landing screen\
**THEN**: I am prompted to provide location permission

**GIVEN**: I have accepted location permission\
**WHEN**: I view the landing screen\
**THEN**: I will see my current day’s weather information AND I will see 3-hour increments on weather for that day AND the
high and low temps will be shown for the day per the UX details below

**GIVEN**: I have accepted location permission\
**WHEN**: I am on the forecast screen\
**THEN**: I will see the five-day forecast per the UX details below

**GIVEN**: I am viewing temperature data\
**THEN**: the temperature will be in degrees Fahrenheit

**GIVEN**: I am viewing the landing screen OR the forecast screen\
**WHEN**: I want to refresh data\
**THEN**: I can pull down to refresh the data

**GIVEN**: I have forecast data\
**WHEN**: I am viewing the forecast screen\
**THEN**: the icon and description should be the most commonly occurring description/icon for that day AND the
temperatures shown should be the high temp and low temp for that day.

**Android Specific Requirements:**
As our application utilizes both XML and compose for this demo app please do the following
The Landing Screen should be in compose and the Forecast Screen should be in XML .
<br>
<br>

### Expected UI
| Landing Screen | Forecast Screen |
| ------------- | ------------- |
| ![Screenshot 2024-08-30 at 6 29 22 PM](https://github.com/user-attachments/assets/d1bf8563-6581-45ef-80e3-dfa51277acf9)  | ![Screenshot 2024-08-30 at 6 29 38 PM](https://github.com/user-attachments/assets/5944e371-a08f-431e-8650-4ccbef7c5cec)  |

**Green color used:** #008D75 ![#008D75](https://via.placeholder.com/15/008D75/008D75.png)
<br>
<br>

### API Details:
- The Open Weather API is free to use for our needs here, however you will have to create an account, this can be
done here
  - https://home.openweathermap.org/users/sign_up
  - The current weather API can be used to obtain the current weather for the landing screen:
  - https://openweathermap.org/current
- The 5-day forecast API can be used to obtain the details for the forecast screen:
  - https://openweathermap.org/forecast5
  - This API endpoint contains almost all data needed for this feature
- Weather icons can be obtained from
  - https://openweathermap.org/img/wn/[iconName]@2x.png
  - The iconName comes back in the other API responses listed above under the “weather” object
- Weather description
  - This can be found in the API’s mentioned above under the “weather” object
