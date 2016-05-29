***************************************************
*	Student Name: Shobhika Panda 				*	
*	Student Net Id- sxp150031     				*					
*	Spring, 2016 Database Design           		*
*				          						*
***************************************************

Folder Struture :
	sxp150031_cs6360
		- CONSTANTS.java
		- DavisBaseLite.java
		- InitClass.java
		- Type.java	
		- InsertUtil.java

Run Time Folder Structure:
	sxp150031_cs6360
		- Schema


How to compile ? 

Change to folder  sxp150031_cs6360
	cd sxp150031_cs6360

Compile:  javac *.java

How to run ? 

DavisBaseLite is the entry class. 

java DavisBaseLite 


sample command : java DavisBaseLite

Where to See output: 

The output Is printed on command line.

Instructions and Assumptions to use sample inputs:


NOTE: Only basic commands (Success Scenarios) work for this database engine:
NOTE: Not much exception handling has been done due to time constraints.
NOTE: Only the test cases given below shall work. (As given by professor in mail)
NOTE: Copy paste test cases as given below.
NOTE: Scroll Down the text file for expected Output:
NOTE: If any exception comes up, please delete schema folder inside the main project folder that is sxp150031_cs6360.
NOTE: It is recommended not to use default schema, since I implemented it only for create table due to time constraints. User based schema creation is recommended. 

Please follow this flow.
	- java DavisBaseLite
	- show tables;
	- show schemas;
	- create schema school;
	- show schemas;
	- use school;
	- CREATE TABLE Students (id int PRIMARY KEY,name varchar(25),birthdate date,credits short);
	- show tables;
	- INSERT INTO Students (id,name,birthdate,credits) VALUES (1,'Jason Day','1995-08-21',32);
	- INSERT INTO Students (id,name,birthdate,credits) VALUES (4,'Brenda Webb','1994-12-19',29);
	- INSERT INTO Students (id,name,birthdate,credits) VALUES (5,'Lois Simmons','1994-05-14',15);
	- SELECT * FROM Students;
	- SELECT * FROM Students where id > 3;
	- SELECT * FROM Students where id = 1;
	- SELECT * FROM Students where name > 'Cat';
	- SELECT * FROM Students WHERE credits > 25;
	- SELECT * FROM Students WHERE birthdate < '1995-01-01';
	- drop tables Students;
	- show tables;

1. At first when you run DaviBaseLite, Directories will be created

After create,insert and drop various related files will be updated/changed.
After create index files will be generated for each column of the table.

		
		-Schema
		- information_schema.schemata.tbl
		- school.schemata.tbl
		 
			-Tables
			-information_schema.table.tbl
			-school.Students.insertData.dat
			-school.Students.table_name.tbl
				
				-Columns
				-information_schema.column.tbl
				-school.Students.birthdate.ndx
				-school.Students.credits.ndx
				-school.Students.id.ndx
				-school.Students.name.ndx

You can manually check the index files after insertion.
Manually check schema files after drop.

2. InitClass will make the information schemas.All the files will be created.

3. Please copy paste the inputs from readme to run the DavisBaseLite.

4. Parsing is based on split space logic , even if you miss one space, it will throw exception.

5. Only simple query of Insert command will work.(Success Scenario)

6. This database engine doesn't support fail scenario of Insert command due to time constraints.

7. The syntax of INSERT is:INSERT INTO Students (id,name,birthdate,credits) VALUES (1,'Jason Day','1995-08-21',32);

8. Please note: INSERT will work only when fields are given.

9. Please follow the flow of the input output scenarios. Do not use drop tables command before any of the other commands.Use drop command only after you are done with all the commands.

10. CREATE TABLE : Use PRIMARY KEY IN CAPS LOCK. Copy paste the input used in readme.

11. Note: Use drop tables(****Not table*****)

first don't forget to compile:

Compile: javac *.java
sample input: java DavisBaseLite

output: 
********************************************************************************
Welcome to DavisBaseLite
DavisBaseLite v1.0

Type "help;" to display supported commands.
********************************************************************************
Directories created
davisql>


sample input&output:

davisql>show tables;
**********
TABLE NAMES
**********
SCHEMATA
TABLES
COLUMNS
**********

sample input&output:

davisql>show schemas;
**********
SCHEMA NAMES
**********
information_schema
**********

sample input&output:

davisql>create schema school;
davisql>show schemas;
**********
SCHEMA NAMES
**********
information_schema
school
**********

davisql>use school;

davisql>CREATE TABLE Students (id int PRIMARY KEY,name varchar(25),birthdate date,credits short);
Create Table successful

davisql>show tables;
**********
TABLE NAMES
**********
SCHEMATA
TABLES
COLUMNS
Students
**********

davisql>INSERT INTO Students (id,name,birthdate,credits) VALUES (1,'Jason Day','1995-08-21',32);
Insert table successfull

davisql>INSERT INTO Students (id,name,birthdate,credits) VALUES (4,'Brenda Webb','1994-12-19',29);
Insert table successfull

davisql>INSERT INTO Students (id,name,birthdate,credits) VALUES (5,'Lois Simmons','1994-05-14',15);
Insert table successfull

davisql>SELECT * FROM Students;
********************************************************************************
          id        name   birthdate     credits
           1    JasonDay  1995-08-21          32
           4  BrendaWebb  1994-12-19          29
           5 LoisSimmons  1994-05-14          15
********************************************************************************

davisql>SELECT * FROM Students where id > 3;
********************************************************************************
          id        name   birthdate     credits
           4  BrendaWebb  1994-12-19          29
           5 LoisSimmons  1994-05-14          15
********************************************************************************

davisql>SELECT * FROM Students where id = 1;
********************************************************************************
          id        name   birthdate     credits
           1    JasonDay  1995-08-21          32
********************************************************************************

davisql>SELECT * FROM Students where name > 'Cat';
********************************************************************************
          id        name   birthdate     credits
           1    JasonDay  1995-08-21          32
           5 LoisSimmons  1994-05-14          15
********************************************************************************

davisql>SELECT * FROM Students WHERE credits > 25;
********************************************************************************
          id        name   birthdate     credits
           4  BrendaWebb  1994-12-19          29
           1    JasonDay  1995-08-21          32
********************************************************************************

davisql>SELECT * FROM Students WHERE birthdate < '1995-01-01';
********************************************************************************
          id        name   birthdate     credits
           1    JasonDay  1995-08-21          32
********************************************************************************

davisql>drop tables Students;
Drop Table Successful

davisql>show tables;
**********
TABLE NAMES
**********
SCHEMATA
TABLES
COLUMNS
**********
