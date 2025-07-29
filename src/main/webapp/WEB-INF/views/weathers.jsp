<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Weather</title></head>
<body>
<form action="/weather" method="get">
    <input type="text" name="city" placeholder="Enter city" />
    <button type="submit">Run</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<c:if test="${not empty city}">
    <h3>Weather for ${city}</h3>
    <p>Temperature: ${temperature} °C</p>
    <p>Humidity: ${humidity}%</p>
    <p>Wind Speed: ${windSpeed} km/h</p>
    <p>Direction: ${windDeg}°</p>
    <p>Description: ${description}</p>
</c:if>
</body>
</html>
