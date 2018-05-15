# coding-challenge
Coding Challenge

Problem:
I have 3 environments where my apps are deployed: DEV, STAGE and PROD
I have 2 applications using this setup: app1, app2
In these environments, i keep a list of ips which identifies the list of clients that are allowed to access my apps

We want a webservice which can provide RESTful endpoints and serve the following functionality:
- add a client ip (IPv4 only) to the whitelist specific to an environment and app
- provide the list of ips without duplicates and can be filtered by environment, app or client name
- remove a client ip from the list

Pre-requisites:
a.) Maven: download here: https://maven.apache.org/download.cgi
b.) Java 8 or better

How to run:
1.) mvn clean install
2.) mvn jetty:run
server will run via port 8080

Note: Environments and Applications are already stored in the database. So there's no need to add them.



API Endpoints:
All Endpoints are available via Postman. Please use this link to open the collection
https://www.getpostman.com/collections/026e8de36930bb1cbab3

All Endpoints can accept request and return response with media type of
application/xml or application/json 

1.) List Environments 
(GET) http://localhost:8080/rest/environments



2.) List Applications
(GET) http://localhost:8080/rest/applications



3.) List Clients 
(GET) http://localhost:8080/rest/clients



4.) List Environment Applications 
(GET) http://localhost:8080/rest/environments/{environmentId}/applications
Sample Request:
(GET) http://localhost:8080/rest/environments/1/applications



5.) Get Single Environment Application
(GET) http://localhost:8080/rest/environments/{environmentId}/applications/{applicationId}
Sample Request:
(GET) http://localhost:8080/rest/environments/1/applications/2



6.) List of Whitelist IPs specific to Envi, App and Client
(GET) http://localhost:8080/rest/environments/{environmentId}/applications/{applicationId}/ipwhitelists?clientName={clientName}
query params: 
(Optional) clientName - name of client
Sample Request:
(GET) http://localhost:8080/rest/environments/1/applications/1/ipwhitelists?clientName=citi



7.) List of Whitelist IPs - query by Envi, App and client (GET)
(GET) http://localhost:8080/rest/ipwhitelists?environmentId={environmentId}&applicationId={applicationId}&clientName={clientName}
query params: 
(Optional) environmentId 
(Optional) applicationId 
(Optional) clientName
Sample Request:
(GET) http://localhost:8080/rest/ipwhitelists?applicationId=1&environmentid=1&clientName=citi



8.) Add a client ip (IPv4 only) to the whitelist specific to an environment and app (POST)
(POST) http://localhost:8080/rest/environments/{environmentId}/applications/{applicationId}/ipwhitelists
request body: 
(Required) ipAddress - IPv4 only 
(Required) clientName
Sample Request:
(POST) http://localhost:8080/rest/environments/1/applications/1/ipwhitelists
{"ipAddress": "100.99.1.1", "clientName": "citi"}



9.) Remove client ip to whitelist specific to envi, application and client
(DELETE) http://localhost:8080/rest/environments/{environmentId}/applications/{applicationId}/ipwhitelists/{ipAddressId}?clientName={clientName}
query params: 
(Required) clientName
Sample Request:
(DELETE) http://localhost:8080/rest/environments/1/applications/1/ipwhitelists/1?clientName=citi



10.) Remove client ip with envi, app and client as query params
(DELETE) http://localhost:8080/rest/ipwhitelists?ipAddressId={ipAddressId}&environmentId={environmentId}&applicationId={applicationId}&clientName={clientName}
query params: 
(Required) ipAddressId 
(Required) environmentId 
(Required) applicationId 
(Required) clientName
Sample Request:
(DELETE) http://localhost:8080/rest/ipwhitelists?ipAddressId=1&applicationId=1&environmentId=1&clientName=citi






















