Ricky Mavlingcar - rrm130230 - CS2336.0U1 - Computer Science II - Semester Project 1 Readme

Bot utilises the weather api and an api that details the latitude and longitude of the International Space Station.
Weather api provided by openweathermap. https://openweathermap.org/current
ISS current location api provided by open-notify. http://open-notify.org/Open-Notify-API/ISS-Location-Now/
Bot connects to the freenode IRC and joins the #pircbot channel.
The bot scans the chat for messages that include the keywords weather or iss. 

I utilised hashmaps as we learnt it in class but never bothered using it. I wanted to experiment with hashmaps as a means to store the JSON data returned by 
the Java API calls.

ISS call example:
Where is the ISS currently located?
<returns long and lat>
iss
<returns long and lat>

Weather call example:
What is the weather in 75252	Note that the bot only recognises zip-codes for the weather api call.
<returns weather info>
weather 75252
<returns weather info>

I initially tried to use regex for parsing the user input but was unable to create a suitable pattern that would identify whether the user would use zip codes or name of a place, so the program now parses the user input in an array, seperated by spaces.


