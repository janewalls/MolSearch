# MolSearch
### Virtual Screening tool for drug discovery
2 GUI's: one to add molecules to a database, and GUI to search that database for molecules under drug property parameters relating to ADME.
Drugs can be filtered under:
1. Lapinski's Rule of 5
2. Lead-Likeness
3. Bio-availablility
4. Setting your own parameters

See the paper relating to the software [here](https://github.com/janewalls/MolSearch/tree/main/LabReport.pdf)

## Requirements

-  Java
-  MySQL database - setup as [below](#SQL-Database)
-  Marvin Beans API from [ChemAxon](#https://chemaxon.com/products/marvin)

## SDBMaker 
Takes molecule files or folders of files in and adds them to the key information to the Database.

## SDBAccess
Access and search database under given parameters that could be considered desirable for a potential drug candidate. 

## SQL Database
### Relational Schema for SQL Database for molecule data:
![Relational Schema for SQL Database for molecule data](https://github.com/janewalls/MolSearch/blob/main/Images/RelationalSchemaMolSearch.png)

## References:


Owens, J. (2003). Chris Lipinski discusses life and chemistry after the Rule of Five. Drug Discovery Today 8, 12–16.


Ursu, O., Rayan, A., Goldblum, A., and Oprea, T.I. (2011). Understanding drug‐likeness. WIREs Comput Mol Sci 1, 760–781.