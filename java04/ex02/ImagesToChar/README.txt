cd to the root of the project (ImagesToChar).

create the lib directory
    mkdir lib

download dependencies
    curl https://repo1.maven.org/maven2/org/jcommander/jcommander/3.0/jcommander-3.0.jar -o lib/jcommander-3.0.jar
    curl https://repo1.maven.org/maven2/com/diogonunes/JColor/5.5.1/JColor-5.5.1.jar -o lib/JColor-5.5.1.jar

prepare to link the libraries
    mkdir target

extract the jar files
    jar -xf lib/jcommander-3.0.jar
    jar -xf lib/JColor-5.5.1.jar

move the extracted packages
    mv com target

build the binary while specifying the external libraries class path:
    javac -cp target -d target $(find src/java -name "*.java")

copy resources to the target directory
    cp -r src/resources target

build the Java ARchive:
    jar -cfm target/images-to-chars-printer.jar src/manifest.txt -C target .

you can now run the program using the follwing command and arguments:
    java -jar target/images-to-chars-printer.jar --white=RED --black=GREEN