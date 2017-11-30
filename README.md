# Web Crawler
Created by Jeremy Wood and Elijah Poulos

## Building 
The program can be built simply with gradle via the terminal. No additional installation is necessary, however,
`JAVA_HOME` must be set properly in your environment variables.

To build the application, simply run:

`gradlew clean build jar`

from a command line. This will create `webcrawler.jar` in the project root directory.

### Testing
The unit and acceptance test suite are run along with the normal build process. However, if you wish to run this
separately, simply run:

`gradlew clean test`

## Usage 
The Web Crawler requires the following three command line arguments.

1. a valid URL

2. a specified maximum page depth in the form of a natural integer. 

3. a file path to the desired local destination directory for the 
files to be downloaded by the crawler.

For example: 
 
`http://website.com 3 C://Users/user/Desktop/downloadRepo`

Run the program from the command line. If following the build instructions above, example usage would look like:

`java -jar webcrawler.jar <url> <depth> <destination folder>`

## How it works

The crawler works by parsing the first page of the given URL
for links to other web pages and files/images in the pages HTML
by using regex to match `<a>` and `<img>` tags, extracting their urls.
Any `<a>` tags must be further parsed to determine if the url points to another page or
a downloadable file.

Next, the staged web elements are classified as either "WebPage", "WebImage", or "WebFile". 
Images and files are added to DownloadRepository, while links to other pages are set as the new 
url for the program to crawl. 

The main class keeps track of the current depth, and once this matched the 
desired depth, the crawler stops at the bottom of the deepest pages. 

After all element URLs have been collected, the files are downloaded to the specified local download path.
After the program is finished, the user should expect their local download repository to contain
all images and text files down to the specified depth for the given website. Note that this 
program only works as described for static web pages in the current version. Behavior on dynamic sites such
as Facebook and Twitter is undefined.


