const API_URL = import.meta.env.VITE_API_URL

export async function doRequest(url: string, method: string, body?: any) {
  const reqBody = body ? new URLSearchParams(body) : undefined
  const response = await fetch(API_URL + url, {
    method: method,
    body: reqBody,
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await response.text())
  }

  return response
}
