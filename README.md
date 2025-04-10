Enter Unity ID wherever Mentioned:

To Compile all the Classes:
javac -d . common/\*.java info_processing/\*.java inventory/\*.java analytics/\*.java billing/\*.java rewards/\*.java

To run the SETUP (Add the Sample Data):
java -cp .:/mnt/ncsudrive/d/UnityID/mariadb-java-client-2.3.0.jar common.Setup

To run the Admin Interface:
java -cp .:/mnt/ncsudrive/d/UnityID/mariadb-java-client-2.3.0.jar common.AdminInterface

To run the Main Interface for users:
java -cp .:/mnt/ncsudrive/d/UnityID/mariadb-java-client-2.3.0.jar common.MainInterface

To mnt the DB in terminal:
/mnt/apps/public/CSC/Mysql-Shell/bin/mysql -u UnityID -p -h classdb2.csc.ncsu.edu
