
// Allow for vendor prefixes.
window.requestFileSystem = window.requestFileSystem ||
                           window.webkitRequestFileSystem;


// Create a variable that will store a reference to the FileSystem.
var filesystem = null;



// A simple error handler to be used throughout this demo.
function errorHandler(error) {
	alert("ERROR "+error);
}


// Request a FileSystem and set the filesystem variable.
function initFileSystem() {
  navigator.webkitPersistentStorage.requestQuota(1024 * 1024 * 5,
    function(grantedSize) {

      // Request a file system with the new size.
      window.requestFileSystem(window.PERSISTENT, grantedSize, function(fs) {

        // Set the filesystem variable.
        filesystem = fs;
		
		alert("at Start, filesystem "+filesystem);

      }, errorHandler);

    }, errorHandler);
}


function loadFile(filename) {
  filesystem.root.getFile(filename, {}, function(fileEntry) {

    fileEntry.file(function(file) {
      var reader = new FileReader();

      reader.onload = function(e) {
        // Update the form fields.
        filenameInput.value = filename;
        contentTextArea.value = this.result;
      };

      reader.readAsText(file);
    }, errorHandler);

  }, errorHandler);
}


function displayEntries(entries) {
  // Clear out the current file browser entries.
  fileList.innerHTML = '';

  entries.forEach(function(entry, i) {
    var li = document.createElement('li');

    var link = document.createElement('a');
    link.innerHTML = entry.name;
    link.className = 'edit-file';
    li.appendChild(link);

    var delLink = document.createElement('a');
    delLink.innerHTML = '[x]';
    delLink.className = 'delete-file';
    li.appendChild(delLink);

    fileList.appendChild(li);

    // Setup an event listener that will load the file when the link
    // is clicked.
    link.addEventListener('click', function(e) {
      e.preventDefault();
      loadFile(entry.name);
    });

    // Setup an event listener that will delete the file when the delete link
    // is clicked.
    delLink.addEventListener('click', function(e) {
      e.preventDefault();
      deleteFile(entry.name);
    });
  });
}


function listFiles() {
  var dirReader = filesystem.root.createReader();
  var entries = [];

  var fetchEntries = function() {
    dirReader.readEntries(function(results) {
      if (!results.length) {
        //displayEntries(entries.sort().reverse());
      } else {
        entries = entries.concat(results);
        fetchEntries();
      }
    }, errorHandler);
  };

  fetchEntries();
}


// Save a file in the FileSystem.
function saveFile(filename, content) {
  alert("initFileSystem "+filesystem+", target\n"+filename);
  filesystem.root.getFile(filename, {create: true}, function(fileEntry) {

    fileEntry.createWriter(function(fileWriter) {

      fileWriter.onerror = function(e) {
        console.log('Write error: ' + e.toString());
        alert('An error occurred and your file could not be saved!');
      };
		alert("TO write "+content);
      var contentBlob = new Blob([content], {type: 'text/plain'});
	  alert("contentBlob "+contentBlob);
      fileWriter.write(contentBlob);
	  alert("fileWriter  DONE ");

    }, errorHandler);

  }, errorHandler);
}


function deleteFile(filename) {
  filesystem.root.getFile(filename, {create: false}, function(fileEntry) {

    fileEntry.remove(function(e) {
      // Update the file browser.
      listFiles();

      // Show a deleted message.
      messageBox.innerHTML = 'File deleted!';
    }, errorHandler);

  }, errorHandler);
}


// Add event listeners on the form.
function setupFormEventListener() {
}

// Start the app by requesting a FileSystem (if the browser supports the API)
if (window.requestFileSystem) {
  initFileSystem();
} else {
  alert('Sorry! Your browser doesn\'t support the FileSystem API :(');
}