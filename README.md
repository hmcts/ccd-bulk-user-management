# ccd-bulk-user-management

Initial project for Java application for managing bulk users
Uses JDK 11 and Gradle
Need to remove Utilities and List directory
Build target is currently a FatJar to compile then run in a terminal
Bulk of the logic is in the App.java
domain objects created for IDAM Roles, IDAM Users, Input users(imported from spreadsheet) and Ouput users (to be output into new spreadsheet)
Services created for managing file interaction, Idam API calls, manipulating the input and updating role lists