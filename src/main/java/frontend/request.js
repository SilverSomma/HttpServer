// let getRequest = (url, type, callback) => {
//     let xhr = new XMLHttpRequest();
//     xhr.open('GET', url, true);
//     xhr.responseType = type;
//     xhr.onload = () => {
//         let status = xhr.status;
//         if (status === 200) {
//             callback(null, xhr.response);
//         } else {
//             callback(status);
//         }
//     };
//     xhr.send();
// };
// let postRequest = (url, type, callback) => {
//     let xhr = new XMLHttpRequest();
//     xhr.open('POST', url, true);
//     xhr.responseType = type;
//     xhr.onload = () => {
//         let status = xhr.status;
//         if (status === 200) {
//             callback(null, xhr.response);
//         } else {
//             callback(status);
//         }
//     };
//     xhr.send();
// };

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
            'Content-Type': 'text'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    });
    return response.text();
}