# Manual Compile

Sql file can be found in `sql` folder

## Backend

- to compile, cd `simple-service` and run `mvn assembly:assembly`
- make sure u edit `.env` to fill in database details
- to run server, run `java -jar target/simple-service-1.0-jar-with-dependencies.jar`

## Frontend

- make sure u edit `.env` to fill in server url
- cd `simple-web` and run `npm install` to install all the required dependencies
- compile with built in dev server, run `npm run serve`

Compiled file can be found at [Link](https://github.com/lkloon123/jersey-web/releases)

## Login Details

Admin   
`admin@admin.com` `demo1234` - department(hr)

Manager  
`manager@manager.com` `demo1234` - department(hr)

User   
`user@user.com` `demo1234` -  department(hr)  
`developer@developer` `demo1234` - department(developer)

## Used Library

### Backend

- jersey (java REST framework)
- jdbi3 (database)
- jjwt (jwt)
- jBCrypt (bcrypt password hashing)
- java-dotenv (parse dotenv file)
- log4j (java logging)

### Frontend

- axios (http request client)
- bootstrap-vue (vue mapping for bootstrap)
- vue (progressive javascript framework)
- vue-router (SPA routing)
- vue-good-table (a vue table)
