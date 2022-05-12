# Expense-Reimbursement-System
## To run the project
Build both the API images by running this on each project directory:
```
docker build -t [tag-name]
```
Then replace the environment variables in ``docker-compose.yml`` with your own credentials.
#### Environment Variables
##### Reimbursement API
`DB_USERNAME` - The username for the database.  
`DB_PASSWORD` - The password for the database.  
`DB_URL` - The URL of the database.  
`EMAIL_URL` - The URL for the email API.  

##### Email API
`EMAIL_USER` - The username of the gmail account your API is going to use to send emails.  
`EMAIL_PASS` - The password of the gmail account.  

Finally, run the project in the same directory of the `docker-compose.yml` file and type:
```
docker compose up
```
