# laura-accounting-project

This program is designed to take an excel file containing accounting data of different companies and compare them to the data from the BI 360 report. It will create a new excel file with the results and keep a log of what the program did, along with any errors it encounters.

### Installation
1. Install Java 8. The easiest way I've found to install very popular software is to use this website: https://ninite.com/
2. Create this folder structure on your Windows PC:
<p align="center"><img src="https://raw.githubusercontent.com/mmaynar1/laura-accounting-project/master/src/main/configuration/directoryStructure.PNG"/></p>
3. Download the following files and save them in the directory structure shown above.
- [example excel file](https://github.com/mmaynar1/laura-accounting-project/blob/master/src/main/configuration/example.xlsx)
- [windows batch file](https://github.com/mmaynar1/laura-accounting-project/blob/master/src/main/configuration/excel-conversion-program.bat)
- [executable jar](https://github.com/mmaynar1/laura-accounting-project/blob/master/src/main/configuration/laura.jar)

### Running the Program
1. Put the input file into the excel-conversion-program directory.
2. Modify the name of the file in excel-conversion-program.bat to be the name of the input file, and save it.
3. Double click the excel-conversion-program.bat file. 
4. The resulting output file will be located in the results folder.
5. The resulting log file will be located in the logs folder.


