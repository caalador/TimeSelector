# TimeSelector Add-on for Vaadin 10

TimeSelector is an UI component add-on for Vaadin Platform 10+.

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/TimeSelector

## Building and running demo

git clone <url of the MyComponent repository>
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Release notes

### Version 10.0.alpha1
- Basic time selector
- Circle select

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the following features are planned for upcoming releases:
- AM/PM functionality


## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

MyComponent is written by Mikael Grankvist

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

For a more comprehensive example, see TimeSelector-demo/src/main/java/org/percepta/mgrankvi/TimeSelectorDemo.java
