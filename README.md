## Usage 
The Web Crawler takes three arguments through the command line separated by a single space:

1. a valid URL

2. a specified maximum page depth in the form of a natural integer. 

3. a file path to the desired local destination repository for the 
files to be downloaded by the crawler. 

For example: 
 
`http://website.com 3 C://Users/user/Desktop/downloadRepo`

to run the program, open the command line, change to the directory
that contains webcrawler.jar, and run the command: 
java -jar webcrawler.jar <url> <depth> <destination file>

The crawler works by parsing the first page of the given URL
for links to other web pages and files/images in the pages HTML
by using regex to match `<a>` tags and `<img>` tags and common file extensions. 
The crawler does this for all pages down to the depth specified by the user, and stores a 
list of links to other pages and files in WebElementRepository.

Next, the staged web elements are classified as either "WebPage", "WebImage", or "WebFile". 
images and files are added to DownloadRepository, while links to other pages are set as the new 
url for the program to crawl. 

The files in DownloadRepository are then moved to the LocalFileRepository, whose path is 
specified in the third argument. The main class keeps track of the current depth, and once this matched the 
desired depth, the crawler stops at the bottom of the deepest pages. 

After the program is finished, the user should expect their local download repository to contain
all images and text files down to the specified depth for the given website. Note that this 
program only works as described for static web pages in the current version. If the page links to an
outside site with dynamic content such as Facebook or Twitter, it will attempt to behave crawl, and may 
run indefinitely. 

## Building 
The program can be built as a with gradle via the terminal. To do so, specify the following in the program configurations:

+ The URL to be crawled
+ The max depth
+ The desired download repository

Next, open the terminal and enter the command:

`gradlew clean build jar`
 
