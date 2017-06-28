# KORONA.pos Client ScannerScale (API Application)
This example outlines the use of a scanner scale using the KORONA.pos Client API.  Please be aware that this is an API example application.  
This application is not part of KORONA and is not certified for live use.

## Requirements
- Java 1.7 or higher
- KORONIX 1.6 or higher (currently there is no Windows or MacOSX support)
- KORONA Point of Sale Client version 1.73 or higher
- ScannerScale must be Datalogix Magellan 8500xt, 8200 or 9500
- Device must be connected with a single serial cable (USB or dual cable is not supported)
- Scanner must be setup to append a TAB after each scan code

## Setup
#### 1) Download application file and copy it to the POS
    You can fin a current application for download unter "Releases" 
    Place application file (scale-connector.jar) into /opt/koronaposj/bin/
#### 2) Connect Device
    The device has to be connected with a single servial cable to COM1.
#### 3) Add application to autostart
    For the 8500:
    
    Edit the file /opt/koronaposj/scripts/before_koronaposj and add the following two lines:
    
      stty -file=/dev/ttyS0 raw
      java -jar /opt/koronaposj/bin/scale-connector.jar &
      
    For the 8200 and 9500
    
    Edit the file /opt/koronaposj/scripts/before_koronaposj and add the following two lines:
    
      stty -file=/dev/ttyS0 raw
      java -jar /opt/koronaposj/bin/scale-connector.jar &      
      
    
#### 5) Setup external system call
    Log into www.koronacloud.com and go to Settings > External System Calls.  Create a new external system call with the name "Get Weight" and use the following Display URL:
    
      http://localhost:8080/
      
    Then go to the Button Configuration and create a new button for the external system call.  This button will be used to get the weigth from the scale and assign it as item quantity.
    
#### 6) Retrieve master data and reboot the POS

## Useful Links
#### Get KORONA Point of Sale
http://www.koronapos.com
#### Access KORONA Cloud Backoffice
https://www.koronacloud.com
#### Get Programming Sheets for Datalogic Magellan 8500xt
http://www.datalogic.com/eng/support-services/automatic-data-capture/downloads/programming-sheets-ps-109.html?search_prod=155
#### Get Programming Sheets for Datalogic Magellan 8200
https://www.manualslib.com/manual/606346/Datalogic-Magellan-8200.html
#### Get Programming Sheets for Datalogic Magellan 9500
https://www.manualslib.com/manual/471138/Datalogic-Magellan-9500.html

