function getFolderList(path) {
    sessionStorage.setItem("path", path + "/")
    getRequest("http://localhost:8080/getprojectfiles?path=." + path, "json", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            let response = `${data}`
            const fileNames = response.split(",")
            displayProjectFiles(fileNames, "folder");
        }
    });
}

function getFileContent(path) {
    sessionStorage.setItem("path", path + "/")
    getRequest("http://localhost:8080/getprojectfiles?path=." + path, "text", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            let response = `${data}`;
            if (isImage(path)) {
                displayProjectFiles(response, "image");
            } else {
                displayProjectFiles(response, "file");
            }
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
    let p = document.createElement("textarea");
    p.textContent = response;
    p.spellcheck = false;
    document.getElementById('files').appendChild(p);
}

function displayImage(response) {
    let img = new Image()
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

function browseBack() {
    var path = sessionStorage.getItem("path");
    var removeLastSlash = path.substring(0, path.lastIndexOf("/"));
    var newPath = removeLastSlash.substring(0, removeLastSlash.lastIndexOf("/"));
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
    }else{
        window.classList.add("hidden");
        main.classList.remove("blur");
        main.classList.remove("disabled");
    }
}


function postFile() {
    const fileTypeInput = document.querySelector("select").value;
    const fileNameInput = document.querySelector("input").value;
    console.log(fileTypeInput);
    console.log(fileNameInput);
    if (!isValidFilePostRequest(fileNameInput)) {
        alert("Midagi valesti")
    }
}

function isValidFilePostRequest(fileTypeInput, fileNameInput) {
    const validFileExtensions = [".jpg",".png",".ico", ""]
    if (fileNameInput.includes()) {

    }

    return false;
}
