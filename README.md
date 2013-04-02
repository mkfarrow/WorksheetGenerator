WorksheetGenerator
==================

So far, the system is broken into a few main pieces.

Descriptors:
The job of a descriptor is to identify and descriptor a class of problem. For an addition problem,
this might include how many carries are involved, how many terms are being added, and so on. The
ProblemDescriptor abstract class lays out the basic behavior that descriptors should have. Most
importantly, every class that implements ProblemDescriptor must have a method called makeProblem
that creates and returns a probably that satisfies the description of the Descriptor that was used
to generate it.

Problems:
These encapsulate actual problems by keeping track of state such as the terms in the problem and
the problem's solution. They can also perform some really useful tasks, such as generating
incorrect yet convincing answers to be used as multiple choice options. Also, they can generate
HTML that can be injected into the template called worksheet.html (the problems should be injected
into the div with the id "problemspace"). 


We also make use of two libraries (one for parsing JSON and one for parsing HTML)

GSON:
This library is used to encode and decode problem descriptions. This library can turn JSON into
instances of Java objects on the fly given that the JSON object has the same exact number and names
of fields as the class you tell the library to instantiate. This is really convenient for turning
JSON representations into instances of ProblemDescriptors.

JSoup:
This is a nice library for parsing HTML that is pretty self-explanatory to use.

There are a couple main changes that I think would probably be good to implement:
	- Generalizing IntSubProblemDescriptor to work for arbitrary numbers of operands
	- Implementing some algorithmic subtraction mistakes that students might make in the 
	IntSubProblem class that the getWrongAnswer method can use.
	- Generalizing Problem and ProblemDescriptor to use generic types for the types of their
	operands and solutions. This would already be useful to allow IntSubProblem and IntAddProblem
	to share a lot of code. This will also help if this system is ever used to generate totally
	different types of problems (e.g. fractions, algebra)
