##Before run required is to have installed:
####Docker (Docker Desktop will be helpful)
####MySQL client (I used in-built from Intellij IDE)
####Postman (or any tool you prefer to test API)

##To run
1) In your terminal go to directory ..\bestPictureAwards\ where are localized  **pom.xml** 
and **docker-compose.yml** (also Dockerfile)
2) Then run **docker-compose up -d --build** - this command will create MySQL database and app in one Container
3) If you want to work from IDE (e.g. with debugger) - go to ..\bestPictureAwards\src\main\resources and here is docker-compose.yml but prepared to create only database. After **docker-compose up -d --build** just start app from IDE :)
4) Database is prepared to use but please follow instruction in **how_to_test.md** to be sure that everthing will work correctly :) Don't worry, you will need only one thing to do and then you will be able to use my simple app :)