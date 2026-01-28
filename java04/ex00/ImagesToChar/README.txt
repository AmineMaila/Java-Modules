cd to the root of the project (ImagesToChar).
then run this command to build the binary:
    javac -d ./target $(find ./src -name "*.java")

you can now run the program using the follwing command and arguments:
    java -cp target <absolute-path-to-bmp-file> <white-pixe-char> <black-pixel-char>