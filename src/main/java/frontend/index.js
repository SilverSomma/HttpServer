let getJSON = (url, type, callback) => {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = type;
    xhr.onload = () => {
        let status = xhr.status;
        if (status == 200) {
            callback(null, xhr.response);
        } else {
            callback(status);
        }
    };
    xhr.send();
};

function createSalaryCalculatorRequest() {
    const salary = document.getElementById("salary").value;
    getJSON("http://localhost:8080/salarycalculator?salary=" + salary, "json", (err, data) => {
        if (err != null) {
            console.error(err);
        } else {
            document.getElementById("employerExpense").textContent = `${data.employerExpense} ` + "€";
            document.getElementById("socialTax").textContent = `${data.socialTax} ` + "€";
            document.getElementById("insuranceByEmployer").textContent = `${data.unemploymentInsuranceByEmployer} ` + "€";
            document.getElementById("grossSalary").textContent = `${data.grossSalary} ` + "€";
            document.getElementById("pension").textContent = `${data.pension} ` + "€";
            document.getElementById("insuranceByEmployee").textContent = `${data.unemploymentInsuranceByEmployee} ` + "€";
            document.getElementById("incomeTaxFree").textContent = `${data.taxExemption} ` + "€";
            document.getElementById("incomeTax").textContent = `${data.incomeTax} ` + "€";
            document.getElementById("netSalary").textContent = `${data.netSalary} ` + "€";
        }
    });
}

function newHtmlImage(names) {
    for (let i = 0; i < names.length; i++) {
        var img = document.createElement('img');
        img.src = "./resources/" + names[i];
        document.getElementById('gallery').appendChild(img);
    }

}

function getImagesNameList() {
    let response = {};
    getJSON("http://localhost:8080/getpicturelist.json", "json", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            response = `${data}`
        }
        const names = response.split(",");
        newHtmlImage(names)

    });
}

function getProjectFiles(path) {
    sessionStorage.setItem("path", path + "/")
    getJSON("http://localhost:8080/getprojectfiles?path=." + path, "json", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            let response = `${data}`
            const fileNames = response.split(",")
            displayProjectFiles(fileNames, "folder");
        }
    });
}

function getProjectFileContent(path) {
    sessionStorage.setItem("path", path + "/")
    getJSON("http://localhost:8080/getprojectfiles?path=." + path, "text", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            let response = String(`${data}`);
            if ((path.includes(".jpg"))||(path.includes(".jpeg"))||(path.includes(".png"))||(path.includes(".ico"))) {
                displayProjectFiles(response, "image");
            } else {
                displayProjectFiles(response, "file");
            }
        }
    });
}

function displayProjectFiles(response, type) {
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
    anchors.forEach(anchor=>{
        anchor.remove();
    })
    if (type === "image") {
        let img = new Image()
        // let img = document.createElement("img");
        img.src = 'data:image/png;base64,'+response;
        document.getElementById('files').appendChild(img);
    } else if (type === "folder") {
        for (let i = 0; i < response.length; i++) {
            var a = document.createElement('a');
            var text = document.createElement('h4');
            var folder = document.createElement("i");
            folder.className = "fa-solid fa-folder";
            text.textContent = response[i];
            if (!response[i].substring(2).includes(".")) {
                a.onclick = function () {
                    getProjectFiles(sessionStorage.getItem("path") + response[i]);
                };
                text.appendChild(folder);
            } else {
                a.onclick = function () {
                    getProjectFileContent(sessionStorage.getItem("path") + response[i]);
                };
            }
            a.appendChild(text);
            document.getElementById('files').appendChild(a);
        }
    } else if (type === "file") {
        let p = document.createElement("textarea");
        p.textContent = response;
        document.getElementById('files').appendChild(p);
    }
}

function browseBack() {
    var path = sessionStorage.getItem("path");
    var removeLastSlash = path.substring(0, path.lastIndexOf("/"));
    var newPath = removeLastSlash.substring(0, removeLastSlash.lastIndexOf("/"));
    getProjectFiles(newPath);
}


