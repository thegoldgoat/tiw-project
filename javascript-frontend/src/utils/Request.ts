const API_URL = (import.meta as any).API_URL

export function doRequest(url: string, method: string, body: any) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest()

    xhr.open(method, API_URL + url)

    xhr.send(body)

    xhr.onload = function () {
      if (xhr.status === 200) {
        resolve(xhr.response)
      } else {
        reject(`Request failed with ${xhr.statusText}`)
      }
    }

    xhr.onerror = function () {
      reject('Request failed')
    }
  })
}
