# COMP 6421 compiler design

**Please note that there are several bugs that I havn't solve in this project, it is not full mark project so far!!!**

Here is the course project for COMP 6421 in Concordia University, given by Prof. Joey Paquet.

This is a compiler written in Java. The grammar of languate definition can be found in the `assignment` directory and the final code generate by the compiler is a assembly languate which can run the a virtual machine called moon machine, which you can find under directory `assingment/moon/`.

The whole project basically contains four parts:

1. a lexical analyser
2. a syntactic analyser
3. a semantic analyser
4. a code gnerator

For the first two parts, everything should work fine. In the syntactic part, I used a table dirven parser instead of recursively descendent parser.

For the implementation of the attribute mirrgration, I used a stack implementation, but I don't think it is the perfect solution. Produce an abstract-syntax tree after the second part should make the code more elegant and more useful.

For code generation, it can't handle the memeber function call and can only deal with the left recursion so far.

For the semantic and the code genration part, the big picture idea I have referenced here: https://github.com/grodtron/COMP442.

**Inside the code, I will put some signature randomly, for student who take this course, please do not just copy and paste.**
