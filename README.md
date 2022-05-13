# Expense-Reimbursement-System
The Expense Reimbursement System will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.
## To run the project
Build both the API images by running this on each project directory:
```
docker build -t [tag-name] .
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

Finally, go to the same directory as the `docker-compose.yml` file and type:
```
docker compose up
```
