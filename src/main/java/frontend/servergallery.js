function newHtmlImage(names) {
    for (let i = 0; i < names.length; i++) {
        var img = document.createElement('img');
        img.src = "./resources/" + names[i];
        document.getElementById('gallery').appendChild(img);
    }

}

function getImagesNameList() {
    let response = {};
    getRequest("http://localhost:8080/getpicturelist.json", "json", (err, data) => {
        if (err != null) {
            console.log(err);
        } else {
            response = `${data}`
        }
        const names = response.split(",");
        newHtmlImage(names)

    });
}