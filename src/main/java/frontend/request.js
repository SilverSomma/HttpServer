this.fileByteArray = [];


async function postRequest(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });
    return response;
}
async function postTextarea(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'POST',
        body: document.getElementById("files").querySelector("textarea").value,
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });
    return response;
}
async function postRequestFromFileInput(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'POST',
        body: this.fileByteArray,
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });
    return response;
}

async function getRequestJson(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'GET',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });

    return response.json();
}
async function getRequestText(url = '', data = {}) {
    const response = await fetch(url, {
        method: 'GET',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'text/html'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });
    return response.text();
}
function readFileInput() {
    const input = document.querySelector("input[type='file']");
    const reader = new FileReader();
    const fileByteArray = [];

    input.addEventListener('change', (e) => {
        reader.readAsArrayBuffer(e.target.files[0]);
        reader.onloadend = (evt) => {
            if (evt.target.readyState === FileReader.DONE) {
                const arrayBuffer = evt.target.result,
                    array = new Uint8Array(arrayBuffer);
                for (const a of array) {
                    fileByteArray.push(a);
                }
                console.log(fileByteArray);
                this.fileByteArray = fileByteArray;
            }
        }
    })
}