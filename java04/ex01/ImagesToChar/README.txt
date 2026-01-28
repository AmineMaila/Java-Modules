cd to the root of the project (ImagesToChar).

build the binary:
    javac -d target $(find src/java -name "*.java")

copy resources to the target directory
    cp -r src/resources target

build the Java ARchive:
    jar -cfm target/images-to-chars-printer.jar src/manifest.txt -C target .

you can now run the program using the follwing command and arguments:
    java -jar target/images-to-chars-printer.jar <white-pixe-char> <black-pixel-char>