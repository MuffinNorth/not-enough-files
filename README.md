<p align="center">
  <img src="https://i.postimg.cc/mDF60cgs/Upscales-ai-1705668132194.jpg" />
</p>

# Not Enough Files 
NEF is a system that allows you to store and track the current status of files and use tags to find them faster.

| Version |    Support database    |
|---------| -----------------------|
| 0.1     |    in-memmory only     |
| 0.2ex   |    sqlite impl         |

*Currently, an in-memory or sqlite database is used as a backend for storing data about files and tags used.*
## Examples
Simple store file and apply tag
```java
FileSystemCore core = context.getBean(FileSystemCore.class);

var file = new SystemFile("D:\\1.txt");
core.applyTag(file, "ImportantThings"); // Store file with tag

var fileImage = new SystemFile("D:\\Asuka.png");
core.applyTag(fileImage, "Anime", "ImportantThings"); // Store another file with two tags

var unknownFile = new SystemFile("D:\\WeirdFile");
core.watchFile(unknownFile); // Store file without tag


core.unwatchFile(new SystemFile("D:\\WeirdFile"); // Remove file from database

var allFiles = core.getAll(); // Get all stored files

var files =  core.getByTags("ImportantThings"); // Get files by bypass tags
// files contains {file, fileImage}
```
---

