function getFolderList(path) {
    sessionStorage.setItem("path", path + "/")
    getRequestJson('http://localhost:8080/getprojectfiles?path=.' + path, {})
        .then(data => {
            // let response = data.toString();
            const fileNames = data.toString().split(",");
            displayProjectFiles(fileNames, "folder");
        })
}

function toggleSaveButtonHidden() {
   const btn = document.getElementById("saveContentButton");
    if (btn.classList.contains("hidden")) {
        btn.classList.remove("hidden");
    } else {
        btn.classList.add("hidden");
    }
}

function toggleNewWindowButtonHidden() {
    const btn = document.getElementById("newFileWindowButton");
    if (btn.classList.contains("hidden")) {
        btn.classList.remove("hidden");
    } else {
        btn.classList.add("hidden");
    }
}

function getFileContent(path) {
    sessionStorage.setItem("path", path + "/")
    getRequestText('http://localhost:8080/getprojectfiles?path=.' + path, {})
        .then(data => {
            let response = data;
            if (isImage(path)) {
                displayProjectFiles(response, "image");
            } else {
                displayProjectFiles(response, "file");
                toggleSaveButtonHidden();
            }
        });
}

function displayFolderList(response) {
    for (let i = 0; i < response.length; i++) {
        var a = document.createElement('a');
        var text = document.createElement('h4');
        var folder = document.createElement("i");
        folder.className = "fa-solid fa-folder";
        text.textContent = response[i];
        if (!response[i].substring(2).includes(".")) {
            a.onclick = function () {
                getFolderList(sessionStorage.getItem("path") + response[i]);
            };
            text.appendChild(folder);
        } else {
            a.onclick = function () {
                getFileContent(sessionStorage.getItem("path") + response[i]);
            };
        }
        a.appendChild(text);
        document.getElementById('files').appendChild(a);
    }
}

function displayFileContent(response) {
    toggleNewWindowButtonHidden();
    let p = document.createElement("textarea");
    p.textContent = response;
    p.spellcheck = false;
    document.getElementById('files').appendChild(p);
}

function displayImage(response) {
    let img = new Image()
    toggleNewWindowButtonHidden();
    img.src = 'data:image/png;base64,' + response;
    document.getElementById('files').appendChild(img);
}

function deletePreviousContent() {
    var container = document.querySelector("#files");
    var lines = document.querySelectorAll("h4");
    var paragraphs = document.querySelectorAll("textarea");
    var images = container.querySelectorAll("img");
    var anchors = container.querySelectorAll("a");
    lines.forEach(line => {
        line.remove();
    });
    paragraphs.forEach(paragraph => {
        paragraph.remove();
    });
    images.forEach(image => {
        image.remove();
    });
    anchors.forEach(anchor => {
        anchor.remove();
    })
}

function displayProjectFiles(response, type) {
    deletePreviousContent();
    if (type === "image") {
        displayImage(response);
    } else if (type === "folder") {
        displayFolderList(response);
    } else if (type === "file") {
        displayFileContent(response);
    }
}

async function browseBack() {
    const saveBtn = document.getElementById("saveContentButton");
    const newFileBtn = document.getElementById("newFileWindowButton");
    var path = sessionStorage.getItem("path");
    var removeLastSlash = path.substring(0, path.lastIndexOf("/"));
    var newPath = removeLastSlash.substring(0, removeLastSlash.lastIndexOf("/"));
    if (!saveBtn.classList.contains("hidden")) {
        toggleSaveButtonHidden();
    }
    if (newFileBtn.classList.contains("hidden")) {
        toggleNewWindowButtonHidden();
    }
    getFolderList(newPath);
}

function isImage(path) {
    return (path.includes(".jpg")) || (path.includes(".jpeg")) || (path.includes(".png")) || (path.includes(".ico"));
}

function toggleNewFileWindow() {
    let window = document.querySelector(".newFileWindow");
    let main = document.querySelector(".mainDiv");
    if (window.classList.contains("hidden")) {
        window.classList.remove("hidden");
        main.classList.add("blur");
        main.classList.add("disabled");
    } else {
        window.classList.add("hidden");
        main.classList.remove("blur");
        main.classList.remove("disabled");
    }
}


function postFile() {
    const fileTypeInput = document.querySelector("select").value;
    const fileNameTextInput = document.querySelector("input[type='text']").value;
    const fileNameFileInput = document.querySelector("input[type='file']").value;
    if (fileNameFileInput) {
        // Fail arvutist...
        // var fileBytes =  binaryString;
    } else {
        // Fail ise loodud...
        if (isValidName(fileNameTextInput, fileTypeInput)) {
            postFileFromNameInput(fileNameTextInput, fileTypeInput);
            toggleNewFileWindow();
            browseBack();
        }
    }
}


function isValidName(name, type) {
    if (type === "File") {
        return isValidFileName(name);
    } else {
        return isValidFolderName(name);
    }
}

function postFileFromNameInput(name, type) {
    const path = sessionStorage.getItem("path");
    postRequest("http://localhost:8080/newprojectfile?path=." + path + name + "&" + "type=" + type, {})
        .then(alert("File created!"));
}


function isValidFileName(fileName) {
    const validFileExtensions = [".jpg", ".png", ".ico", ".jpeg", ".java", ".css", ".js", ".html", ".pdf", ".txt"]
    if (fileName.length < 5) {
        alert("Type a valid file name!");
        return false;
    }
    for (let i = 0; i < validFileExtensions.length; i++) {
        if (fileName.includes(validFileExtensions[i])) {
            return true;
        }
    }
    alert("Type a valid file extension!");
    return false;
}

function isValidFolderName(fileName) {
    if (fileName.length > 0 && (fileName.includes("."))) {
        alert("Type a valid folder name!");
        return false;
    } else {
        return true;
    }
}

function saveContent() {
    const path = sessionStorage.getItem("path");
    postTextarea("http://localhost:8080/saveprojectfile?path=." + path,{})
        .then(response =>{
            alert("File saved!")
            browseBack();
        })


}