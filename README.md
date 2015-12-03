<b>Generates a Live year-wise leaderboard for AU Local contests.</b>

<b>Algorithm :</b>

1) Read data (nickname, rank and number of problems solved) from spoj ranklist.

2) Find the email ids of the users in the rank list (from the Spoj groups page)

3) Using the email id, fetch all other detail of the users from google spreadsheet.

4) Generate the ranklist and publish into the blog.

Used for the following contests : http://spoj.com/AU9, http://spoj.com/AU10
Resultant Leaderbaords :http://cegcodingcamp.blogspot.in/2015/01/ceg-coding-camp-ranklist.html, http://cegcodingcamp.blogspot.in/2015/11/au10-live-ranklist.html

Built using Eclipse IDE

Language used : <b>JAVA</b>

Libraries used

* Selenium phatomjs headless driver,
* Google spreadsheet API and
* Blogger API

To run on windows or linux machine, either build using Eclipse in Java or create an executable Jar file using File->Export
