function newHtmlImage(names) {
    for (let i = 0; i < names.length; i++) {
        var img = document.createElement('img');
        img.src = "./resources/" + names[i];
        document.getElementById('gallery').appendChild(img);
    }

}

function getImagesNameList() {
    getRequestJson("http://localhost:8080/getpicturelist.json", {})
        .then(data=>{
            newHtmlImage(data);
        })
}